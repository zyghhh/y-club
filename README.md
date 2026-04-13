# y-club

## 项目简介

y-club 是一款专门为程序员打造的面试学习交流社区，采用主流微服务框架 + 主流 C 端技术栈作为技术架构。旨在减少程序员信息差，让学习、交流、分享统一化。程序员可以在平台上完善自身知识，配合练习、模拟面试、简历分析等模块来提升面试能力，降低多平台切换成本。

- 开源地址：[https://github.com/zyghhh/y-club](https://github.com/zyghhh/y-club)

---

## 技术架构

```
SpringBoot 2.4.x  +  Spring Cloud Alibaba 2021.1  +  Spring Cloud Gateway
MySQL  +  Redis  +  Minio  +  ElasticSearch
Nacos（注册中心 / 配置中心）  +  Sa-Token（鉴权）  +  xxl-job（定时任务）
MapStruct  +  MyBatis-Plus  +  Guava  +  CompletableFuture
```

### 微服务模块结构

```
y-club
├── y-club-gateway          # 网关服务（鉴权过滤、请求路由、Header 透传）
├── y-club-auth             # 认证授权服务（用户登录、角色权限管理）
│   ├── y-club-auth-api         # 对外 Feign 接口定义
│   ├── y-club-auth-application # Controller / 拦截器 / 用户上下文
│   ├── y-club-auth-domain      # 领域服务（角色、权限、用户业务逻辑）
│   ├── y-club-auth-infra       # 基础设施（Mapper、Redis）
│   ├── y-club-auth-common      # 公共类（枚举、工具）
│   └── y-club-auth-starter     # 启动模块
├── y-club-subject          # 题库服务（题目 CRUD、分类标签、点赞、ES 搜索）
│   ├── y-club-subject-api      # 对外接口定义
│   ├── y-club-subject-application # Controller
│   ├── y-club-subject-domain   # 领域服务（Handler 工厂、点赞、缓存、xxl-job）
│   ├── y-club-subject-infra    # 基础设施（Mapper、EsRestClient）
│   └── y-club-subject-starter  # 启动模块
├── y-club-oss              # 对象存储服务（文件上传下载，适配器模式）
├── y-club-practice         # 练习服务（刷题练习、提交记录）
└── y-club-wx               # 微信公众号服务（消息接收、工厂+策略处理）
```

---

## 系统功能模块

| 模块 | 功能说明 |
|------|----------|
| 用户与权限管理 | 注册 / 登录、角色管理、权限分配、Sa-Token 会话 |
| 题库与知识体系 | 题目分类、标签管理、多题型支持（单选 / 多选 / 判断 / 简答）|
| 题目点赞收藏 | Redis 计数 + xxl-job 定时同步至 MySQL |
| 全文搜索 | ElasticSearch 高亮搜索，支持多集群、多索引 |
| 配套练习 | 刷题练习、提交记录、错题回顾 |
| 对象存储 | 适配 Minio / 阿里云 OSS，文件上传下载 |
| 微信集成 | 公众号消息接收，工厂+策略模式分发处理 |
| 网关鉴权 | Sa-Token 全局过滤，登录态及权限从 Redis 读取 |

---

## 核心技术实践

### 1. 鉴权系统 —— Sa-Token 替代 Spring Security

项目使用 **Sa-Token** 代替传统 Spring Security，在网关层 (`y-club-gateway`) 通过 `SaReactorFilter` 完成统一鉴权。

**认证流程：**
```
前端请求 → Gateway SaReactorFilter（检查 Token）
    ↓ 已登录
LoginFilter（解析 loginId → 写入下游 Header）
    ↓
各微服务 LoginInterceptor（从 Header 读取 loginId → 存入 ThreadLocal）
```

**权限数据来源：** `StpInterfaceImpl` 实现 `StpInterface`，从 Redis 读取用户角色与权限列表（key: `auth.role.<loginId>` / `auth.permission.<loginId>`），实现分布式会话共享。

**路由级权限控制示例：**
```java
// SaTokenConfigure.java
SaRouter.match("/oss/**", r -> StpUtil.checkLogin());
SaRouter.match("/subject/subject/add", r -> StpUtil.checkPermission("subject:add"));
SaRouter.match("/subject/**", r -> StpUtil.checkLogin());
```

**关键文件：**
- [`SaTokenConfigure.java`](y-club-gateway/src/main/java/com/yclub/gateway/auth/SaTokenConfigure.java) — Sa-Token 全局过滤器配置
- [`StpInterfaceImpl.java`](y-club-gateway/src/main/java/com/yclub/gateway/auth/StpInterfaceImpl.java) — 从 Redis 获取角色与权限
- [`LoginFilter.java`](y-club-gateway/src/main/java/com/yclub/gateway/filter/LoginFilter.java) — 解析 loginId 并透传 Header

---

### 2. 用户上下文管理 —— InheritableThreadLocal

网关将 `loginId` 写入请求 Header，下游各微服务通过 `LoginInterceptor` 拦截器读取并存入 `LoginContextHolder`（基于 `InheritableThreadLocal`），整个请求生命周期内任意位置均可获取当前用户，请求结束后自动清理，防止内存泄漏。

```java
// LoginContextHolder.java（InheritableThreadLocal 封装）
public static String getLoginId() {
    return (String) getThreadLocalMap().get("loginId");
}

// LoginInterceptor.java（preHandle 写入 / afterCompletion 清理）
LoginContextHolder.set("loginId", request.getHeader("loginId"));
// ...
LoginContextHolder.remove();
```

**关键文件：**
- [`LoginContextHolder.java`](y-club-auth/y-club-auth-application/y-club-auth-application-controller/src/main/java/com/yclub/auth/application/controller/application/context/LoginContextHolder.java)
- [`LoginInterceptor.java`](y-club-auth/y-club-auth-application/y-club-auth-application-controller/src/main/java/com/yclub/auth/application/controller/application/interceptor/LoginInterceptor.java)

---

### 3. 工厂 + 策略模式 —— 题目创建 & 微信消息处理

**题目创建（多题型解耦）：**

`SubjectTypeHandlerFactory` 在 `afterPropertiesSet` 阶段自动扫描所有 `SubjectTypeHandler` 实现（单选、多选、判断、简答），构建类型 → Handler 的映射表。业务层仅需调用 `getHandler(subjectType)` 获取对应策略，无需 if-else 分支。

```
SubjectTypeHandlerFactory
  ├── RadioTypeHandler      （单选题）
  ├── MultipleTypeHandler   （多选题）
  ├── JudgeTypeHandler      （判断题）
  └── BriefTypeHandler      （简答题）
```

**微信消息处理（消息类型解耦）：**

`MsgTypeFactory` 采用相同模式，自动注入所有 `WeChatMsgHandler` 实现（文本消息、关注事件等），按消息类型分发，扩展新消息类型只需新增 Handler 实现，零侵入。

**关键文件：**
- [`SubjectTypeHandlerFactory.java`](y-club-subject/y-club-subject-domain/src/main/java/com/yclub/subject/domain/handler/subject/SubjectTypeHandlerFactory.java)
- [`MsgTypeFactory.java`](y-club-wx/src/main/java/com/yclub/handler/MsgTypeFactory.java)

---

### 4. 适配器模式 —— OSS 对象存储

`y-club-oss` 模块定义 `StorageAdapter` 接口，包含 `createBucket`、`uploadFile`、`downLoad`、`deleteObject`、`getUrl` 等标准操作。`MinioStorageAdapter` 和 `AliStorageAdapter` 分别适配 Minio 和阿里云 OSS，业务层面向接口编程，切换存储服务只需更换 Bean 注入，无需修改业务代码。

```
StorageAdapter（接口）
  ├── MinioStorageAdapter   （适配 Minio）
  └── AliStorageAdapter     （适配阿里云 OSS）
```

**关键文件：**
- [`StorageAdapter.java`](y-club-oss/src/main/java/com/yclub/oss/adapter/StorageAdapter.java)
- [`MinioStorageAdapter.java`](y-club-oss/src/main/java/com/yclub/oss/adapter/MinioStorageAdapter.java)

---

### 5. 性能优化 —— CompletableFuture 并发树状查询

分类标签树状查询经历三个优化阶段：

| 版本 | 方案 | 说明 |
|------|------|------|
| 一期 | 串行 forEach | 顺序查询每个子分类的标签，耗时叠加 |
| 二期 | FutureTask + 自定义线程池 | 并发提交任务，手动 get 汇聚结果 |
| 三期 | **CompletableFuture**（当前） | 链式异步编程，代码更简洁，性能最优 |

```java
// SubjectCategoryDomainServiceImpl.java（最终方案）
List<CompletableFuture<Map<Long, List<SubjectLabelBO>>>> futures = boList.stream()
    .map(category -> CompletableFuture.supplyAsync(
        () -> getSubjectLabelMap(category), labelThreadPool))
    .collect(Collectors.toList());
```

查询结果同时写入 **Guava 本地缓存**（`CacheUtil`），10 分钟内重复请求直接命中缓存，无需再次并发查库。

**关键文件：**
- [`SubjectCategoryDomainServiceImpl.java`](y-club-subject/y-club-subject-domain/src/main/java/com/yclub/subject/domain/service/impl/SubjectCategoryDomainServiceImpl.java)

---

### 6. 本地缓存 —— Guava Cache + 泛型函数式封装

`CacheUtil` 对 Guava `Cache<String, String>` 进行泛型 + 函数式封装，最大容量 5000 条，10 分钟写后过期，提供 `getListResult` 和 `getMappedResult` 两个工具方法，支持 List 和 Map 两种结果类型，通过 `Function<String, T>` 参数实现缓存穿透时的回调查询，业务层调用极为简洁。

```java
// 使用示例
List<SubjectCategoryBO> boList = CacheUtil.getListResult(
    "categoryAndLabel." + queryId,
    SubjectCategoryBO.class,
    (key) -> getSubjectCategoryBOS(queryId)
);
```

**关键文件：**
- [`CacheUtil.java`](y-club-subject/y-club-subject-domain/src/main/java/com/yclub/subject/domain/util/CacheUtil.java)

---

### 7. 搜索引擎 —— 自定义 EsRestClient（多集群 + 高亮）

`EsRestClient` 在 `@PostConstruct` 阶段读取 `EsConfigProperties` 配置，为每个集群创建 `RestHighLevelClient` 并存入 `clientMap`（`Map<clusterName, RestHighLevelClient>`）。查询时通过 `EsIndexInfo`（包含 `clusterName` 和 `indexName`）路由到对应集群及索引，实现多集群、多索引的统一管理。

`EsSearchRequest` 封装了 `BoolQueryBuilder`、分页、排序及 `HighlightBuilder`，支持关键词高亮返回，满足题目搜索的高亮展示需求。

**关键文件：**
- [`EsRestClient.java`](y-club-subject/y-club-subject-infra/src/main/java/com/yclub/subject/infra/basic/es/EsRestClient.java)
- [`EsSearchRequest.java`](y-club-subject/y-club-subject-infra/src/main/java/com/yclub/subject/infra/basic/es/EsSearchRequest.java)

---

### 8. 定时任务与数据持久化 —— xxl-job + Redis 点赞

**点赞写入流程（高性能）：**
```
用户点赞请求
  → 写入 Redis Hash（key: subject.liked, field: subjectId:userId, value: status）
  → 同步更新 Redis 点赞数计数器（subject.liked.count.<subjectId>）
  → 同步更新 Redis 明细 key（subject.liked.detail.<subjectId>.<userId>）
```

**异步持久化流程（xxl-job）：**
```
xxl-job 触发 syncLikedJobHandler
  → 调用 SubjectLikedDomainService.syncLiked()
  → getHashAndDelete 原子读取并清空 Redis Hash
  → 解析 field（subjectId:userId）批量构建实体
  → 批量 batchInsertOrUpdate 写入 MySQL（存在则更新，不存在则插入）
```

此方案将高频点赞操作的数据库写入转为 Redis 操作，大幅降低 DB 压力，通过 xxl-job 定时批量落库保证数据最终一致性。

**关键文件：**
- [`SyncLikedJob.java`](y-club-subject/y-club-subject-domain/src/main/java/com/yclub/subject/domain/job/SyncLikedJob.java)
- [`SubjectLikedDomainServiceImpl.java`](y-club-subject/y-club-subject-domain/src/main/java/com/yclub/subject/domain/service/impl/SubjectLikedDomainServiceImpl.java)

---

## 部署说明

### 环境依赖

| 组件 | 版本要求 | 用途 |
|------|----------|------|
| JDK | 1.8+ | 运行环境 |
| MySQL | 5.7+ | 主数据存储 |
| Redis | 6.0+ | 会话、缓存、点赞计数 |
| Nacos | 2.x | 注册中心 + 配置中心 |
| Minio | 最新稳定版 | 对象存储（或替换为阿里云 OSS）|
| ElasticSearch | 7.x | 全文搜索 |
| xxl-job | 2.x | 定时任务调度 |

### 启动顺序

1. 启动基础中间件：MySQL、Redis、Nacos、Minio、ElasticSearch、xxl-job-admin
2. 启动认证服务：`y-club-auth-starter`
3. 启动题库服务：`y-club-subject-starter`
4. 启动 OSS 服务：`y-club-oss` (`OSSApplication`)
5. 启动微信服务：`y-club-wx` (`WxApplication`)
6. 启动练习服务：`y-club-practice`
7. 最后启动网关：`y-club-gateway` (`GatewayApplication`)

### 配置说明

各服务通过 `bootstrap.yml` 对接 Nacos 配置中心，关键配置项包括：
- 数据库连接信息
- Redis 连接信息
- Minio endpoint / accessKey / secretKey
- ElasticSearch 集群节点配置（`esConfigs` 列表，支持多集群）
- Sa-Token 配置（token 名称、超时时间）
- xxl-job 执行器配置（appname、port、adminAddresses）

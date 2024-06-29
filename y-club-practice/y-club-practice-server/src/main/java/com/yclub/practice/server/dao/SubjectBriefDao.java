package com.yclub.practice.server.dao;


import com.yclub.practice.server.entity.po.SubjectBriefPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 简答题(SubjectBrief)表数据库访问层
 *
 * @author makejava
 * @since 2024-04-03 10:40:27
 */
public interface SubjectBriefDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SubjectBriefPO queryById(Long id);

}


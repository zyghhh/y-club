package com.yclub.subject.infra.basic.service.impl;

import com.yclub.subject.infra.basic.entity.SubjectLiked;
import com.yclub.subject.infra.basic.mapper.SubjectLikedDao;
import com.yclub.subject.infra.basic.service.SubjectLikedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * (SubjectLiked)表服务实现类
 *
 * @author makejava
 * @since 2024-04-24 11:16:42
 */
@Service
@Slf4j
public class SubjectLikedServiceImpl implements SubjectLikedService {
    @Resource
    private SubjectLikedDao subjectLikedDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SubjectLiked queryById(Long id) {
        return this.subjectLikedDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param subjectLiked 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<SubjectLiked> queryByPage(SubjectLiked subjectLiked, PageRequest pageRequest) {
        long total = this.subjectLikedDao.count(subjectLiked);
        return new PageImpl<>(this.subjectLikedDao.queryAllByLimit(subjectLiked, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param subjectLiked 实例对象
     * @return 实例对象
     */
    @Override
    public SubjectLiked insert(SubjectLiked subjectLiked) {
        this.subjectLikedDao.insert(subjectLiked);
        return subjectLiked;
    }

    /**
     * 修改数据
     *
     * @param subjectLiked 实例对象
     * @return 实例对象
     */
    @Override
    public int update(SubjectLiked subjectLiked) {
        return this.subjectLikedDao.update(subjectLiked);
    }


    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.subjectLikedDao.deleteById(id) > 0;
    }

    @Override
    public void batchInsert(List<SubjectLiked> subjectLikedList) {
        this.subjectLikedDao.insertBatch(subjectLikedList);
    }

    @Override
    public void batchInsertOrUpdate(List<SubjectLiked> subjectLikedList) {
        this.subjectLikedDao.batchInsertOrUpdate(subjectLikedList);
    }

    @Override
    public int countByCondition(SubjectLiked subjectLiked) {
        return this.subjectLikedDao.countByCondition(subjectLiked);
    }

    @Override
    public List<SubjectLiked> queryPage(SubjectLiked subjectLiked, int start, Integer pageSize) {
        return this.subjectLikedDao.queryPage(subjectLiked, start, pageSize);
    }
}

package com.yclub.practice.server.dao;

import com.yclub.practice.server.entity.po.SubjectMultiplePO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 多选题信息表(SubjectMultiple)表数据库访问层
 *
 * @author makejava
 * @since 2024-04-03 10:41:42
 */
public interface SubjectMultipleDao {

    List<SubjectMultiplePO> selectBySubjectId(Long id);
}


package com.yclub.practice.server.dao;

import com.yclub.practice.server.entity.po.SubjectJudgePO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 判断题(SubjectJudge)表数据库访问层
 *
 * @author makejava
 * @since 2024-04-03 10:41:15
 */
public interface SubjectJudgeDao {
    SubjectJudgePO selectBySubjectId(Long repeatSubjectId);

}


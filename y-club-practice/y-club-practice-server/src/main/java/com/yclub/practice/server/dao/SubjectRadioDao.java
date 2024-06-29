package com.yclub.practice.server.dao;

import com.yclub.practice.server.entity.po.SubjectRadioPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 单选题信息表(SubjectRadio)表数据库访问层
 *
 * @author makejava
 * @since 2024-04-03 10:42:21
 */
public interface SubjectRadioDao {
    List<SubjectRadioPO>  selectBySubjectId(Long id);

}


package com.yclub.practice.server.dao;

import com.yclub.practice.server.entity.po.LabelCountPO;
import com.yclub.practice.server.entity.po.SubjectMappingPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-02  17:12
 */
public interface SubjectMappingDao {

    List<LabelCountPO> getLabelSubjectCount(@Param("categoryId") Long categoryId,
                                            @Param("subjectTypeList") List<Integer> subjectTypeList);

    List<SubjectMappingPO> getLabelIdsBySubjectId(Long subjectId);

}

package com.yclub.subject.domain.convert;


import com.yclub.subject.domain.entity.SubjectLabelBO;
import com.yclub.subject.infra.basic.entity.SubjectLabel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-01  11:37
 */
@Mapper
public interface SubjectLabelConverter {
    SubjectLabelConverter INSTANCE = Mappers.getMapper(SubjectLabelConverter.class);

    SubjectLabel convertBOToLabel(SubjectLabelBO subjectLabelBO);

    List<SubjectLabelBO> convertToBOList(List<SubjectLabel> subjectLabelList);

    SubjectLabel convertBoToLabel(SubjectLabelBO subjectLabelBO);
}

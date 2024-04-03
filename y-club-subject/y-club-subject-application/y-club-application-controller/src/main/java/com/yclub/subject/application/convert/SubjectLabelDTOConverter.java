package com.yclub.subject.application.convert;


import com.yclub.subject.application.dto.SubjectCategoryDTO;
import com.yclub.subject.application.dto.SubjectLabelDTO;
import com.yclub.subject.domain.entity.SubjectCategoryBO;
import com.yclub.subject.domain.entity.SubjectLabelBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-01  11:37
 */
@Mapper
public interface SubjectLabelDTOConverter {
    SubjectLabelDTOConverter INSTANCE = Mappers.getMapper(SubjectLabelDTOConverter.class);

    SubjectLabelBO convertDTOToLabelBO(SubjectLabelDTO subjectLabelDTO);

    List<SubjectLabelDTO> convertBoToLabelDTOList(List<SubjectLabelBO> subjectLabelDTO);
    SubjectLabelDTO convertBoToLabelDTO(SubjectLabelBO subjectLabelBO);

}

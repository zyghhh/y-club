package com.yclub.subject.application.convert;


import com.yclub.subject.application.dto.SubjectAnswerDTO;
import com.yclub.subject.application.dto.SubjectInfoDTO;
import com.yclub.subject.domain.entity.SubjectAnswerBO;
import com.yclub.subject.domain.entity.SubjectInfoBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-01  11:37
 */
@Mapper
public interface SubjectAnswerDTOConverter {
    SubjectAnswerDTOConverter INSTANCE = Mappers.getMapper(SubjectAnswerDTOConverter.class);

    SubjectAnswerBO convertDTOToBO(SubjectAnswerDTO subjectAnswerDTO);

    List<SubjectAnswerBO> convertDTOToBOList(List<SubjectAnswerDTO> subjectAnswerDTOs);

}

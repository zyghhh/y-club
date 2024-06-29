package com.yclub.subject.application.convert;


import com.yclub.subject.application.dto.SubjectInfoDTO;
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
public interface SubjectDTOConverter {
    SubjectDTOConverter INSTANCE = Mappers.getMapper(SubjectDTOConverter.class);

    SubjectInfoBO convertDTOToBO(SubjectInfoDTO subjectInfoDTO);

    List<SubjectInfoDTO> convertBoToSubjectInfoList(List<SubjectInfoBO> subjectInfoBOS);
    SubjectInfoDTO convertBoToSubjectInfoDTO(SubjectInfoBO subjectInfoBO);


}

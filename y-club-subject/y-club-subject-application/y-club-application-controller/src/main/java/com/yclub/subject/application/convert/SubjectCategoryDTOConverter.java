package com.yclub.subject.application.convert;


import com.yclub.subject.application.dto.SubjectCategoryDTO;
import com.yclub.subject.domain.entity.SubjectCategoryBO;
import com.yclub.subject.infra.basic.entity.SubjectCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-01  11:37
 */
@Mapper
public interface SubjectCategoryDTOConverter {
    SubjectCategoryDTOConverter INSTANCE = Mappers.getMapper(SubjectCategoryDTOConverter.class);

    SubjectCategoryBO convertDTOToBOCategory(SubjectCategoryDTO subjectCategoryDTO);

}

package com.yclub.subject.domain.convert;


import com.yclub.subject.domain.entity.SubjectCategoryBO;
import com.yclub.subject.infra.basic.entity.SubjectCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-01  11:37
 */
@Mapper
public interface SubjectCategoryConverter {
    SubjectCategoryConverter INSTANCE = Mappers.getMapper(SubjectCategoryConverter.class);

    SubjectCategory convertBOToCategory(SubjectCategoryBO subjectCategoryBO);

    List<SubjectCategoryBO> convertToBOList(List<SubjectCategory> subjectCategoryList);

}

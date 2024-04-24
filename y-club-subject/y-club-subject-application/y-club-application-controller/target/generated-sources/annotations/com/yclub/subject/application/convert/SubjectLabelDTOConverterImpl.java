package com.yclub.subject.application.convert;

import com.yclub.subject.application.dto.SubjectLabelDTO;
import com.yclub.subject.domain.entity.SubjectLabelBO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-23T16:37:29+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_212 (Oracle Corporation)"
)
public class SubjectLabelDTOConverterImpl implements SubjectLabelDTOConverter {

    @Override
    public SubjectLabelBO convertDTOToLabelBO(SubjectLabelDTO subjectLabelDTO) {
        if ( subjectLabelDTO == null ) {
            return null;
        }

        SubjectLabelBO subjectLabelBO = new SubjectLabelBO();

        subjectLabelBO.setId( subjectLabelDTO.getId() );
        subjectLabelBO.setLabelName( subjectLabelDTO.getLabelName() );
        subjectLabelBO.setSortNum( subjectLabelDTO.getSortNum() );
        subjectLabelBO.setCategoryId( subjectLabelDTO.getCategoryId() );

        return subjectLabelBO;
    }

    @Override
    public List<SubjectLabelDTO> convertBoToLabelDTOList(List<SubjectLabelBO> subjectLabelDTO) {
        if ( subjectLabelDTO == null ) {
            return null;
        }

        List<SubjectLabelDTO> list = new ArrayList<SubjectLabelDTO>( subjectLabelDTO.size() );
        for ( SubjectLabelBO subjectLabelBO : subjectLabelDTO ) {
            list.add( convertBoToLabelDTO( subjectLabelBO ) );
        }

        return list;
    }

    @Override
    public SubjectLabelDTO convertBoToLabelDTO(SubjectLabelBO subjectLabelBO) {
        if ( subjectLabelBO == null ) {
            return null;
        }

        SubjectLabelDTO subjectLabelDTO = new SubjectLabelDTO();

        subjectLabelDTO.setId( subjectLabelBO.getId() );
        subjectLabelDTO.setLabelName( subjectLabelBO.getLabelName() );
        subjectLabelDTO.setSortNum( subjectLabelBO.getSortNum() );
        subjectLabelDTO.setCategoryId( subjectLabelBO.getCategoryId() );

        return subjectLabelDTO;
    }
}

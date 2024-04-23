package com.yclub.subject.domain.convert;

import com.yclub.subject.domain.entity.SubjectAnswerBO;
import com.yclub.subject.domain.entity.SubjectInfoBO;
import com.yclub.subject.domain.entity.SubjectOptionBO;
import com.yclub.subject.infra.basic.entity.SubjectInfo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-23T09:31:58+0800",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_212 (Oracle Corporation)"
)
public class SubjectInfoConverterImpl implements SubjectInfoConverter {

    @Override
    public SubjectInfo convertBOToSubjectInfo(SubjectInfoBO subjectInfoBO) {
        if ( subjectInfoBO == null ) {
            return null;
        }

        SubjectInfo subjectInfo = new SubjectInfo();

        subjectInfo.setId( subjectInfoBO.getId() );
        subjectInfo.setSubjectName( subjectInfoBO.getSubjectName() );
        subjectInfo.setSubjectDifficult( subjectInfoBO.getSubjectDifficult() );
        subjectInfo.setSettleName( subjectInfoBO.getSettleName() );
        subjectInfo.setSubjectType( subjectInfoBO.getSubjectType() );
        subjectInfo.setSubjectScore( subjectInfoBO.getSubjectScore() );
        subjectInfo.setSubjectParse( subjectInfoBO.getSubjectParse() );
        subjectInfo.setIsDeleted( subjectInfoBO.getIsDeleted() );

        return subjectInfo;
    }

    @Override
    public List<SubjectInfoBO> convertToBOList(List<SubjectInfo> subjectInfoList) {
        if ( subjectInfoList == null ) {
            return null;
        }

        List<SubjectInfoBO> list = new ArrayList<SubjectInfoBO>( subjectInfoList.size() );
        for ( SubjectInfo subjectInfo : subjectInfoList ) {
            list.add( subjectInfoToSubjectInfoBO( subjectInfo ) );
        }

        return list;
    }

    @Override
    public SubjectInfoBO convertOptionAndInfoToBo(SubjectOptionBO optionBO, SubjectInfo subjectInfo) {
        if ( optionBO == null && subjectInfo == null ) {
            return null;
        }

        SubjectInfoBO subjectInfoBO = new SubjectInfoBO();

        if ( optionBO != null ) {
            subjectInfoBO.setSubjectAnswer( optionBO.getSubjectAnswer() );
            List<SubjectAnswerBO> list = optionBO.getOptionList();
            if ( list != null ) {
                subjectInfoBO.setOptionList( new ArrayList<SubjectAnswerBO>( list ) );
            }
        }
        if ( subjectInfo != null ) {
            subjectInfoBO.setId( subjectInfo.getId() );
            subjectInfoBO.setSubjectName( subjectInfo.getSubjectName() );
            subjectInfoBO.setSubjectDifficult( subjectInfo.getSubjectDifficult() );
            subjectInfoBO.setSettleName( subjectInfo.getSettleName() );
            subjectInfoBO.setSubjectType( subjectInfo.getSubjectType() );
            subjectInfoBO.setSubjectScore( subjectInfo.getSubjectScore() );
            subjectInfoBO.setSubjectParse( subjectInfo.getSubjectParse() );
            subjectInfoBO.setIsDeleted( subjectInfo.getIsDeleted() );
        }

        return subjectInfoBO;
    }

    protected SubjectInfoBO subjectInfoToSubjectInfoBO(SubjectInfo subjectInfo) {
        if ( subjectInfo == null ) {
            return null;
        }

        SubjectInfoBO subjectInfoBO = new SubjectInfoBO();

        subjectInfoBO.setId( subjectInfo.getId() );
        subjectInfoBO.setSubjectName( subjectInfo.getSubjectName() );
        subjectInfoBO.setSubjectDifficult( subjectInfo.getSubjectDifficult() );
        subjectInfoBO.setSettleName( subjectInfo.getSettleName() );
        subjectInfoBO.setSubjectType( subjectInfo.getSubjectType() );
        subjectInfoBO.setSubjectScore( subjectInfo.getSubjectScore() );
        subjectInfoBO.setSubjectParse( subjectInfo.getSubjectParse() );
        subjectInfoBO.setIsDeleted( subjectInfo.getIsDeleted() );

        return subjectInfoBO;
    }
}

package com.yclub.practice.server.service;

import com.yclub.practice.api.common.PageResult;
import com.yclub.practice.api.vo.PracticeSetVO;
import com.yclub.practice.api.vo.PracticeSubjectListVO;
import com.yclub.practice.api.vo.SpecialPracticeVO;
import com.yclub.practice.api.vo.UnCompletePracticeSetVO;
import com.yclub.practice.server.entity.dto.PracticeSetDTO;
import com.yclub.practice.server.entity.dto.PracticeSubjectDTO;

import java.util.List;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-28  10:02
 */
public interface PracticeSetService {

    /**
     * 获取专项练习内容
     */
    List<SpecialPracticeVO> getSpecialPracticeContent();

//    /**
//     * 开始练习
//     */
//    PracticeSetVO addPractice(PracticeSubjectDTO dto);
//
//    /**
//     * 获取练习题
//     */
//    PracticeSubjectListVO getSubjects(GetPracticeSubjectsReq req);
//
//    /**
//     * 获取题目
//     */
//    PracticeSubjectVO getPracticeSubject(PracticeSubjectDTO dto);
//
//    /**
//     * 获取模拟套题内容
//     */
//    PageResult<PracticeSetVO> getPreSetContent(PracticeSetDTO dto);
//
//    /**
//     * 获取未完成练习内容
//     */
//    PageResult<UnCompletePracticeSetVO> getUnCompletePractice(GetUnCompletePracticeReq req);

}
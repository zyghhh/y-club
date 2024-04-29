package com.yclub.practice.server.service;

import com.yclub.practice.api.req.*;
import com.yclub.practice.api.vo.RankVO;
import com.yclub.practice.api.vo.ReportVO;
import com.yclub.practice.api.vo.ScoreDetailVO;
import com.yclub.practice.api.vo.SubjectDetailVO;

import java.util.List;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-29  10:00
 */
public interface PracticeDetailService {
    Boolean submit(SubmitPracticeDetailReq req);

    Boolean submitSubject(SubmitSubjectDetailReq req);

    List<ScoreDetailVO> getScoreDetail(GetScoreDetailReq req);

    SubjectDetailVO getSubjectDetail(GetSubjectDetailReq req);

    ReportVO getReport(GetReportReq req);

    List<RankVO> getPracticeRankList();

    Boolean giveUp(Long practiceId);
}

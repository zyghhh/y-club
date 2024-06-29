package com.yclub.practice.server.dao;

import com.yclub.practice.server.entity.po.PracticeSetDetailPO;

import java.util.List;

public interface PracticeSetDetailDao {

    /**
     * 新增套题
     */
    int add(PracticeSetDetailPO po);

    List<PracticeSetDetailPO> selectBySetId(Long setId);


    void batchInsert(List<PracticeSetDetailPO> practiceList);
}
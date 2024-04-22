package com.yclub.subject.infra.basic.service.impl;

import com.yclub.subject.infra.basic.entity.SubjectInfoEsByData;
import com.yclub.subject.infra.basic.esRepo.SubjectEsRepository;
import com.yclub.subject.infra.basic.service.SubjectEsService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-21  19:06
 */

@Service("subjectEsService")
@Slf4j
public class SubjectEsServiceImpl implements SubjectEsService {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    SubjectEsRepository subjectEsRepository;
    @Override
    public void createIndex() {
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(SubjectInfoEsByData.class);
        indexOperations.create();
        Document mapping = indexOperations.createMapping(SubjectInfoEsByData.class);
        indexOperations.putMapping(mapping);

    }

    @Override
    public void addDocs() {
        List<SubjectInfoEsByData> subjectInfoEsByDataList = new LinkedList<>();
        subjectInfoEsByDataList.add(new SubjectInfoEsByData(1L,"MySql是什么","是一个数据库","Derek",new Date()));
        subjectInfoEsByDataList.add(new SubjectInfoEsByData(2L,"Redis是什么","是一个数据库","Derek",new Date()));
        subjectInfoEsByDataList.add(new SubjectInfoEsByData(3L,"Redis有什么好","是一个数据库","Derek",new Date()));
        subjectInfoEsByDataList.add(new SubjectInfoEsByData(4L,"Redis有什么不好","是一个数据库","Derek",new Date()));
        subjectEsRepository.saveAll(subjectInfoEsByDataList);
    }

    @Override
    public void search() {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("subjectName","redis"))
                .build();
        SearchHits<SubjectInfoEsByData> search = elasticsearchRestTemplate.search(nativeSearchQuery, SubjectInfoEsByData.class);
        List<SearchHit<SubjectInfoEsByData>> searchHits = search.getSearchHits();
        for (SearchHit<SubjectInfoEsByData> searchHit : searchHits) {
            SubjectInfoEsByData content = searchHit.getContent();
            log.info("searchHit content:{}",content);
        }
    }

    @Override
    public void find() {
        Iterable<SubjectInfoEsByData> all = subjectEsRepository.findAll();
        all.forEach(subjectInfoEsByData -> {
            log.info("find subjectInfoEs:{}", subjectInfoEsByData);
        });
    }
}

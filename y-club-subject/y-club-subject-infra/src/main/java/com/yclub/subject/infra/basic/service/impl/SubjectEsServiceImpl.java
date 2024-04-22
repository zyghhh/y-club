package com.yclub.subject.infra.basic.service.impl;

import com.yclub.subject.infra.basic.entity.SubjectInfoEs;
import com.yclub.subject.infra.basic.esRepo.SubjectEsRepository;
import com.yclub.subject.infra.basic.service.SubjectEsService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
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
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(SubjectInfoEs.class);
        indexOperations.create();
        Document mapping = indexOperations.createMapping(SubjectInfoEs.class);
        indexOperations.putMapping(mapping);

    }

    @Override
    public void addDocs() {
        List<SubjectInfoEs> subjectInfoEsList = new LinkedList<>();
        subjectInfoEsList.add(new SubjectInfoEs(1L,"MySql是什么","是一个数据库","Derek",new Date()));
        subjectInfoEsList.add(new SubjectInfoEs(2L,"Redis是什么","是一个数据库","Derek",new Date()));
        subjectInfoEsList.add(new SubjectInfoEs(3L,"Redis有什么好","是一个数据库","Derek",new Date()));
        subjectInfoEsList.add(new SubjectInfoEs(4L,"Redis有什么不好","是一个数据库","Derek",new Date()));
        subjectEsRepository.saveAll(subjectInfoEsList);
    }

    @Override
    public void search() {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("subjectName","redis"))
                .build();
        SearchHits<SubjectInfoEs> search = elasticsearchRestTemplate.search(nativeSearchQuery, SubjectInfoEs.class);
        List<SearchHit<SubjectInfoEs>> searchHits = search.getSearchHits();
        for (SearchHit<SubjectInfoEs> searchHit : searchHits) {
            SubjectInfoEs content = searchHit.getContent();
            log.info("searchHit content:{}",content);
        }
    }

    @Override
    public void find() {
        Iterable<SubjectInfoEs> all = subjectEsRepository.findAll();
        all.forEach(subjectInfoEs -> {
            log.info("find subjectInfoEs:{}",subjectInfoEs);
        });
    }
}

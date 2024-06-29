package com.yclub.subject.infra.basic.service.impl;

import com.sun.javafx.collections.MappingChange;
import com.yclub.subject.common.entity.PageResult;
import com.yclub.subject.common.enums.SubjectInfoTypeEnum;
import com.yclub.subject.infra.basic.entity.EsSubjectFields;
import com.yclub.subject.infra.basic.entity.SubjectInfoEs;
import com.yclub.subject.infra.basic.es.EsIndexInfo;
import com.yclub.subject.infra.basic.es.EsRestClient;
import com.yclub.subject.infra.basic.es.EsSearchRequest;
import com.yclub.subject.infra.basic.es.EsSourceData;
import com.yclub.subject.infra.basic.service.SubjectEsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-22  16:49
 */
@Service("subjectEsService")
@Slf4j
public class SubjectEsServiceImpl implements SubjectEsService {
    @Override
    public boolean insert(SubjectInfoEs subjectInfoEs) {
        log.info("insert subjectInfoEs:{}", subjectInfoEs);
        EsSourceData esSourceData = new EsSourceData();
        Map<String, Object> data = convert2EsSourceData(subjectInfoEs);
        esSourceData.setData(data);
        esSourceData.setDocId(subjectInfoEs.getDocId().toString());
        return EsRestClient.insertDoc(getEsIndexInfo(), esSourceData);
    }

    private Map<String, Object> convert2EsSourceData(SubjectInfoEs subjectInfoEs) {
        Map<String, Object> map = new HashMap<>();
        map.put(EsSubjectFields.SUBJECT_ID, subjectInfoEs.getSubjectId());
        map.put(EsSubjectFields.SUBJECT_ANSWER, subjectInfoEs.getSubjectAnswer());
        map.put(EsSubjectFields.SUBJECT_NAME, subjectInfoEs.getSubjectName());
        map.put(EsSubjectFields.DOC_ID, subjectInfoEs.getDocId());
        map.put(EsSubjectFields.SUBJECT_TYPE, subjectInfoEs.getSubjectType());
        map.put(EsSubjectFields.CREATE_USER, subjectInfoEs.getCreateUser());
        map.put(EsSubjectFields.CREATE_TIME, subjectInfoEs.getCreateTime());
        return map;
    }

    private EsIndexInfo getEsIndexInfo() {
        EsIndexInfo esIndexInfo = new EsIndexInfo();
        esIndexInfo.setClusterName("4cbfc28edf70");
        esIndexInfo.setIndexName("subject_index");
        return esIndexInfo;
    }

    @Override
    public PageResult<SubjectInfoEs> querySubjectList(SubjectInfoEs subjectInfoEs) {
        PageResult<SubjectInfoEs> pageResult = new PageResult<>();
        EsSearchRequest esSearchRequest = createSearchListQuery(subjectInfoEs);
        if(esSearchRequest == null){
            return pageResult;
        }
        SearchResponse searchResponse = EsRestClient.searchWithTermQuery(getEsIndexInfo(), esSearchRequest);
        List<SubjectInfoEs> subjectInfoList = new LinkedList<>();
        SearchHits hits = searchResponse.getHits();
        pageResult.setPageSize(subjectInfoEs.getPageSize());
        pageResult.setPageNo(subjectInfoEs.getPageNo());
        if(hits == null || hits.getHits()== null){
            pageResult.setRecords(subjectInfoList);
            pageResult.setTotal(0);
            return pageResult;
        }
        SearchHit[] hits1 = hits.getHits();
        for (SearchHit hit : hits1) {
            SubjectInfoEs subjectInfo = getSubjectInfoEs(hit);
            subjectInfoList.add(subjectInfo);
        }
        pageResult.setTotal(subjectInfoList.size());
        pageResult.setRecords(subjectInfoList);
        return pageResult;
    }

    private  SubjectInfoEs getSubjectInfoEs(SearchHit hit) {
        Map<String, Object> sourceAsMap = hit.getSourceAsMap();
        if(sourceAsMap == null){
            return null;
        }
        SubjectInfoEs result = new SubjectInfoEs();
        result.setSubjectId(MapUtils.getLongValue(sourceAsMap, EsSubjectFields.SUBJECT_ID));
        result.setSubjectName(MapUtils.getString(sourceAsMap, EsSubjectFields.SUBJECT_NAME));
        result.setSubjectAnswer(MapUtils.getString(sourceAsMap, EsSubjectFields.SUBJECT_ANSWER));
        result.setSubjectType(MapUtils.getInteger(sourceAsMap, EsSubjectFields.SUBJECT_TYPE));
        result.setDocId(MapUtils.getLongValue(sourceAsMap, EsSubjectFields.DOC_ID));
        result.setScore(new BigDecimal(String.valueOf(hit.getScore()))
                .multiply(new BigDecimal("100.00").setScale(2, RoundingMode.HALF_UP)));

        //处理name的高亮
        Map<String, HighlightField> highlightFieldMap = hit.getHighlightFields();
        HighlightField highlightNameField = highlightFieldMap.get(EsSubjectFields.SUBJECT_NAME);
        if(Objects.nonNull(highlightNameField)){
            Text[] fragments = highlightNameField.getFragments();
            StringBuilder sb = new StringBuilder();
            for(Text fragment : fragments){
                sb.append(fragment);
            }
            result.setSubjectName(sb.toString());
        }

        //处理answer的高亮
        HighlightField highlightAnswerField = highlightFieldMap.get(EsSubjectFields.SUBJECT_ANSWER);
        if(Objects.nonNull(highlightAnswerField)){
            Text[] answerFragments = highlightAnswerField.getFragments();
            StringBuilder sb = new StringBuilder();
            for(Text fragment : answerFragments){
                sb.append(fragment);
            }
            result.setSubjectAnswer(sb.toString());
        }

        return result;
    }

    private EsSearchRequest createSearchListQuery(SubjectInfoEs subjectInfoEs) {
        EsSearchRequest esSearchRequest = new EsSearchRequest();
        BoolQueryBuilder bq = new BoolQueryBuilder();
        if(subjectInfoEs.getKeyWord() == null){
            return null;
        }
        MatchQueryBuilder nameQueryBuilder =
                QueryBuilders.matchQuery(EsSubjectFields.SUBJECT_NAME, subjectInfoEs.getKeyWord());
        nameQueryBuilder.boost(2);// 提升名字占的权重
        bq.should(nameQueryBuilder);
        MatchQueryBuilder answerQueryBuilder =
                QueryBuilders.matchQuery(EsSubjectFields.SUBJECT_ANSWER, subjectInfoEs.getKeyWord());
        bq.should(answerQueryBuilder);

        MatchQueryBuilder typeQueryBuilder = QueryBuilders.matchQuery(EsSubjectFields.SUBJECT_TYPE, SubjectInfoTypeEnum.BRIEF.getCode());
        bq.must(typeQueryBuilder);
        bq.minimumShouldMatch(1);

        HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");

        esSearchRequest.setBq(bq);
        esSearchRequest.setHighlightBuilder(highlightBuilder);
        esSearchRequest.setFields(EsSubjectFields.FIELD_QUERY);
        esSearchRequest.setSize(subjectInfoEs.getPageSize());
        esSearchRequest.setFrom((subjectInfoEs.getPageNo() - 1) * subjectInfoEs.getPageSize());
        esSearchRequest.setNeedScroll(false);
        return esSearchRequest;
    }
}

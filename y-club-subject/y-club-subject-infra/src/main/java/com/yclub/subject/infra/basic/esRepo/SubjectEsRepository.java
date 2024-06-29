package com.yclub.subject.infra.basic.esRepo;

import com.yclub.subject.infra.basic.entity.SubjectInfoEsByData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-21  19:01
 */
public interface SubjectEsRepository extends ElasticsearchRepository<SubjectInfoEsByData,Long> {

}

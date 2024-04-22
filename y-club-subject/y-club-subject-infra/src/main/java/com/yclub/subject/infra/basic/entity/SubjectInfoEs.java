package com.yclub.subject.infra.basic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.util.Date;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-21  16:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "subject_index",createIndex = false)
public class SubjectInfoEs {

    @Field(type = FieldType.Long)
    @Id
    private Long id;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String subjectName;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String subjectAnswer;

    @Field(type = FieldType.Keyword)
    private String createUser;

    @Field(type = FieldType.Date)
    private Date createTime;

}

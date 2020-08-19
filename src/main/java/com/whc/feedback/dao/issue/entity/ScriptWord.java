package com.whc.feedback.dao.issue.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author whc
 * @date 2020/7/8
 * @description
 */
@Entity
@Table(name = "script_word", schema = "dbo", catalog = "")
@Data
public class ScriptWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "word_id")
    private String wordId;
    @Column(name = "word_name")
    private String wordName;
    @Transient
    private Integer sort;
    @Transient
    private Integer scriptTotal;
    @Transient
    private Long scriptCount;
}

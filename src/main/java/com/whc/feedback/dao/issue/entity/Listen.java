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
@Table(name = "listen", schema = "dbo", catalog = "")
@Data
public class Listen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "script_id")
    private Integer scriptId;
    /**
     * 脚本状态 0正确 1核心单词没有在音频中 2相同例句重复 3歌曲 4例句翻译有误 5例句切割不完整
     */
    @Column(name = "script_status")
    private Integer scriptStatus;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "real_name")
    private String realName;
    @Column(name = "word_name")
    private String wordName;
    @Column(name = "word_id")
    private Integer wordId;
}

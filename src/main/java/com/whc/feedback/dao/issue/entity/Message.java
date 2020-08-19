package com.whc.feedback.dao.issue.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tblmessage", schema = "dbo", catalog = "")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "issue_id")
    private Integer issueId;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "message")
    private String message;
    @Column(name = "message_date")
    private Date messageDate;
    @Column(name = "issue_status")
    private Byte issueStatus;
}

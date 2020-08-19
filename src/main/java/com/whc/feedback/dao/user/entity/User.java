package com.whc.feedback.dao.user.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: WHC
 * @Date: 2019/9/9 16:10
 * @description
 */
@Data
@Entity
@Table(name = "tUser", schema = "dbo")
public class User {
    @Id
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "realname")
    private String realName;
    private String email;
    private String pwd;
    private Byte usertypeid;
}

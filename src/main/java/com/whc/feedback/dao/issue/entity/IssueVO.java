package com.whc.feedback.dao.issue.entity;

import lombok.Data;

import java.util.List;
@Data
public class IssueVO {
    private Issue issue;
    private List<Message> messageList;
}

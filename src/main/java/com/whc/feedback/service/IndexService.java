package com.whc.feedback.service;


import com.whc.feedback.common.ServerResponse;
import com.whc.feedback.dao.issue.entity.Issue;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: WHC
 * @Date: 2019/9/9 16:26
 * @description
 */
public interface IndexService {


    /**
     * 登录
     *
     * @param email
     * @param pwd
     * @return
     */
    ServerResponse login(String email, String pwd);

    /**
     * 上传文件
     * @param file
     * @return
     */
    ServerResponse upload(HttpServletRequest request, MultipartFile[] file);

    /**
     * 创建问题
     * @param issue
     * @return
     */
    ServerResponse createIssue(Issue issue);

    /**
     * 查看问题 分页
     * @param status 0未处理 1进行中 2已处理 3已关闭
     * @param userId 用户id
     * @param level 用户身份
     * @param stage 学段
     * @param system 系统 Android IOS
     * @param handlerId 处理者id
     * @param pageSize 第几页
     * @return
     */
    ServerResponse getListIssue(String status,String userId
            ,String level, String stage
            ,String system,String handlerId,Integer pageSize);

    /**
     * 查看问题 分页
     * @param status 0未处理 1进行中 2已处理 3已关闭
     * @param userId 用户id
     * @param level 用户身份
     * @param stage 学段
     * @param system 系统 Android IOS
     * @param handlerId 处理者id
     * @param category 分类
     * @param date 时间
     * @param pageSize 第几页
     * @return
     */
    ServerResponse getListIssue(String status,String userId
            ,String level, String stage
            ,String system,String handlerId,String category,String date, Integer pageSize);

    /**
     * 获取提交过问题的用户列表
     * @return
     */
    ServerResponse getUserList();

    /**
     * 获取消息列表
     * @param issueId 问题id
     * @return
     */
    ServerResponse getMessageList(Integer issueId);

    /**
     * 创建消息
     * @param issueId 问题id
     * @param userId 用户id
     * @param message 消息
     * @param status 问题状态
     * @return
     */
    ServerResponse createMessage(Integer issueId, Integer userId, String message,Byte status);

    /**
     * 分配问题给用户
     * @param issueId 问题id
     * @param userId 用户id
     * @return
     */
    ServerResponse assignmentIssue(Integer issueId, Integer userId);

    /**
     * 获取问题和消息
     * @param issueId 问题id
     * @return
     */
    ServerResponse getIssue(Integer issueId);


    /**
     * 获取更新
     * @param version 版本号
     * @return
     */
    ServerResponse getVersion(String version);

    /**
     * 获取等待自己处理的任务数量
     * @param handlerId 处理者id
     * @return
     */
    ServerResponse getNum(Integer handlerId);

    /**
     * 修改问题是否无法处理状态
     * @param isTemporarilyUnableToProcess 是否无法处理
     * @param issueId 问题id
     * @return
     */
    ServerResponse isTemporarilyUnableToProcess(Boolean isTemporarilyUnableToProcess,Integer issueId);

    /**
     * 获取单词列表
     * @return
     */
    ServerResponse getScriptWordList();

    /**
     * 根据wordId获取单词相关的难度下的脚本信息列表
     * @param wordId 单词id
     * @return
     */
    ServerResponse getListenScriptByWordId(String wordId);

    /**
     * 获取所有在库里的listen数据
     * @return
     */
    ServerResponse getListenList();

    /**
     * 新增或修改ListenStatus
     * @param scriptId 脚本id
     * @param scriptStatus  脚本状态
     * @param realName 真实名
     * @param userId 用户id
     * @param wordName 单词名
     * @param wordId 单词id
     * @return
     */
    ServerResponse updateListenStatus(Integer scriptId,Integer scriptStatus,String realName,Integer userId,String wordName,Integer wordId);

    /**
     * 校验有多少单词脚本不存在
     * @return
     */
    ServerResponse checkWordScriptExists();

    /**
     * 补充所有未null的wordid与wordname
     * @return
     */
    ServerResponse updateWordIdAndWordName();
}

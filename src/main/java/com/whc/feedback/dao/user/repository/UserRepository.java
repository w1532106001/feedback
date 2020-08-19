package com.whc.feedback.dao.user.repository;

import com.whc.feedback.dao.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: WHC
 * @Date: 2019/9/9 16:19
 * @description
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * 根据邮箱密码查询用户
     *
     * @param email
     * @param pwd
     * @return
     */
    User findUserByEmailAndPwd(String email, String pwd);
}

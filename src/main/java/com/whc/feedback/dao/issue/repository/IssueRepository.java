package com.whc.feedback.dao.issue.repository;

import com.whc.feedback.dao.issue.entity.Issue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Author: WHC
 * @Date: 2019/10/17 22:20
 * @description
 */
@Repository
public interface IssueRepository extends JpaRepository<Issue,Integer>,JpaSpecificationExecutor<Issue> {
    @Query("select a.creator from Issue a group by a.creator")
    List<Integer> findAllGroupByCreator();

    @Query(value = "select distinct a.creator,b.realname FROM tblfeedback a LEFT JOIN zenoDB.dbo.tUser b on a.creator = b.user_id order by b.realname",nativeQuery = true)
    List<Object[]> getUserList();

    Issue findIssueById(Integer id);

    @Modifying
    @Query(value = "update Issue set status = ?2 where id= ?1")
    Integer updateStatusById(Integer id,Byte status);

    @Modifying
    @Query(value = "update Issue set status = ?2 , closeDate = ?3  where id= ?1")
    Integer updateStatusAndCloseDateById(Integer id, Byte status, Date date);

    @Modifying
    @Query(value = "update Issue set status = ?2 , assignmentDate = ?3 , handlerId = ?4  where id= ?1")
    Integer updateStatusAndHandlerIdAndAssignmentDateById(Integer id, Byte status, Date date,Integer userId);

    @Query(value = "select count(id) from Issue where status = 1 and handlerId = ?1")
    int getNumByStatusAndHandlerId(Integer handlerId);

    @Modifying
    @Query(value = "update Issue set isTemporarilyUnableToProcess = ?2  where id= ?1")
    Integer isTemporarilyUnableToProcess(Integer id, Boolean isTemporarilyUnableToProcess);

}

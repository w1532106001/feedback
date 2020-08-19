package com.whc.feedback.dao.issue.repository;

import com.whc.feedback.dao.issue.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MessageRepository extends JpaRepository<Message,Integer> {
    @Query(value = "select a from Message a where a.issueId = ?1",nativeQuery = false)
    List<Message> findAllByIssueId(Integer issueId);
}

package com.whc.feedback.dao.issue.repository;


import com.whc.feedback.dao.issue.entity.ScriptWord;
import com.whc.feedback.dao.issue.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScriptWordRepository extends JpaRepository<ScriptWord,Integer> {
}

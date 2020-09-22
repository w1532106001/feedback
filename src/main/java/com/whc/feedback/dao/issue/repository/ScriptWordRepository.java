package com.whc.feedback.dao.issue.repository;


import com.whc.feedback.dao.issue.entity.ScriptWord;
import com.whc.feedback.dao.issue.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ScriptWordRepository extends JpaRepository<ScriptWord,Integer> {
    List<ScriptWord> findScriptWordsByStatus(Integer status);
    @Modifying
    @Transactional
    @Query(value = "update ScriptWord a set a.status = 1 where a.wordId = :wordId",nativeQuery = false)
    void updateScriptWord(@Param("wordId") String wordId);
}

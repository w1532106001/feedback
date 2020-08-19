package com.whc.feedback.dao.issue.repository;


import com.whc.feedback.dao.issue.entity.Listen;
import com.whc.feedback.dao.issue.entity.Message;
import com.whc.feedback.dao.issue.entity.ScriptWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ListenRepository extends JpaRepository<Listen,Integer> {
    Listen findByScriptIdAndWordId(Integer scriptId,Integer wordId);
    List<Listen> findByWordId(Integer wordId);

    @Query(value = "select new map(a.wordId as wordId,count(a.wordId) as count) from Listen a group by a.wordId")
    public List<Map<String, Object>> countByWordId();

}

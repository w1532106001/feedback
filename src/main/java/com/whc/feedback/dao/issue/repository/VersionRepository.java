package com.whc.feedback.dao.issue.repository;


import com.whc.feedback.dao.issue.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VersionRepository extends JpaRepository<Version,String> {
}

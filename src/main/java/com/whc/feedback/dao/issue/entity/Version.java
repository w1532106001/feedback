package com.whc.feedback.dao.issue.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblappversion", schema = "dbo", catalog = "")
@Data
public class Version {
    @Id
    @Column(name = "new_version")
    private String newVersion;
    @Column(name = "apk_file_url")
    private String apkFileUrl;
    @Column(name = "update_log")
    private String updateLog;
    @Column(name = "target_size")
    private String targetSize;
    @Column(name = "new_md5")
    private String newMd5;
    @Column(name = "is_constraint")
    private Byte constraint;
}

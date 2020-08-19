package com.whc.feedback.dao.issue.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: WHC
 * @Date: 2019/10/17 22:10
 * @description
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblfeedback", schema = "dbo", catalog = "")
@Data
public class Issue {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "status")
    private Byte status;
    @Column(name = "issue_date")
    private Date issueDate;
    @Column(name = "clazz")
    private Byte clazz;
    @Column(name = "category")
    private Byte category;
    @Column(name = "creator")
    private Integer creator;
    @Column(name = "phone_brand")
    private String phoneBrand;
    @Column(name = "phone_ios")
    private String phoneIos;
    @Column(name = "phone_model")
    private String phoneModel;
    @Column(name = "test_comments")
    private String testComments;
    @Column(name = "phone_status")
    private Byte phoneStatus;
    @Column(name = "phone_sound_status")
    private Byte phoneSoundStatus;
    @Column(name = "phone_sound_category")
    private Byte phoneSoundCategory;
    @Column(name = "microphone_status")
    private Byte microphoneStatus;
    @Column(name = "microphone_category")
    private Byte microphoneCategory;
    @Column(name = "phone_network_status")
    private Byte phoneNetworkStatus;
    @Column(name = "phone_network_category")
    private Byte phoneNetworkCategory;
    @Column(name = "phone_camera_status")
    private Byte phoneCameraStatus;
    @Column(name = "phone_storage_status")
    private Byte phoneStorageStatus;
    @Column(name = "phone_electricity_status")
    private Byte phoneElectricityStatus;
    @Column(name = "phone_screen_resolution")
    private String phoneScreenResolution;
    @Column(name = "phone_screen_orientation_lock")
    private Byte phoneScreenOrientationLock;
    @Column(name = "phone_screen_open_status")
    private Byte phoneScreenOpenStatus;
    @Column(name = "phone_gesture_action")
    private Byte phoneGestureAction;
    @Column(name = "fail_rate")
    private Byte failRate;
    @Column(name = "action_plan")
    private String actionPlan;
    @Column(name = "action_plan_date")
    private Date actionPlanDate;
    @Column(name = "second_test_comments")
    private String secondTestComments;
    @Column(name = "second_test_comments_date")
    private Date secondTestCommentsDate;
    @Column(name = "media_urls")
    private String mediaUrls;
    @Column(name = "close_date")
    private Date closeDate;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "level")
    private Byte level;
    @Column(name = "menu")
    private Byte menu;
    @Column(name = "stage")
    private Byte stage;
    @Column(name = "system")
    private String system;
    @Column(name = "version")
    private String version;
    @Column(name = "handler_id")
    private Integer handlerId;
    @Column(name = "assignment_date")
    private Date assignmentDate;
    @Column(name = "is_temporarily_unable_to_process")
    private Boolean isTemporarilyUnableToProcess;
}

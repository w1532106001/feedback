package com.whc.feedback.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whc
 * @date 2020/7/8
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScriptInfo {
    private Integer scriptid;
    private Integer  mediaid;
    private Integer  sequenceid;
    private Double  start_time;
    private Double end_time;
    private String script_eng;
    private Boolean  bhead;
    private String  script_chn;
    private String  keywords;
    private String  questionid;
    private String  question_starttime;
    private String  question_endtime;
    private Integer  paragraphid;
    private String  triggertime;
    private Boolean blistening;
    private String  subtitle_chinese;
    private String  subtitle_english;
    private String  title_chinese;
    private String  title_english;
    private String  syllabusname;
    private String  bookname;
    private String  chapter;
    private String  mp3;
    private String  wav;
    private String isPlaying;
}

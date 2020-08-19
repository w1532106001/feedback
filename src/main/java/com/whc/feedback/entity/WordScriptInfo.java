package com.whc.feedback.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author whc
 * @date 2020/7/8
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordScriptInfo {
    private Integer wordid;
    private String word;
    /**
     * 中小学出现过的听起来像的单词
     */
    private String similarsoundwords1;
    /**
     * 中小学出现过的听起来像的单词id
     */
    private String similarsoundwords1id;
    /**
     * 四六级出现过的听起来像的单词
     */
    private String similarsoundwords2;
    /**
     * 四六级出现过的听起来像的单词id
     */
    private String similarsoundwords2id;
    /**
     * 中小学出现过的看起来像的单词
     */
    private String similarlookwords1;
    /**
     * 中小学出现过的看起来像的单词id
     */
    private String similarlookwords1id;
    /**
     * 四六级出现过的看起来像的单词
     */
    private String similarlookwords2;
    /**
     * 四六级出现过的看起来像的单词id
     */
    private String similarlookwords2id;

    private List<ScriptInfo> level1;
    private List<ScriptInfo> level2;
    private List<ScriptInfo> level3;
    private List<ScriptInfo> level4;
    private List<ScriptInfo> level5;
    private List<ScriptInfo> level6;
    private List<ScriptInfo> level7;
    private List<ScriptInfo> level8;
    private List<ScriptInfo> level9;

    private List<ScriptInfo> scriptInfoList;

}

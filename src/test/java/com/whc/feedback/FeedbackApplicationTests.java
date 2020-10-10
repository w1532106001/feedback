package com.whc.feedback;

import com.alibaba.fastjson.JSONObject;
import com.whc.feedback.dao.issue.entity.Listen;
import com.whc.feedback.dao.issue.entity.ScriptWord;
import com.whc.feedback.dao.issue.repository.IssueRepository;
import com.whc.feedback.dao.issue.repository.ListenRepository;
import com.whc.feedback.dao.issue.repository.ScriptWordRepository;
import com.whc.feedback.entity.ScriptInfo;
import com.whc.feedback.entity.WordScriptInfo;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = FeedbackApplication.class)
class FeedbackApplicationTests {

    @Resource
    private IssueRepository issueRepository;

    @Resource
    private ListenRepository listenRepository;

    @Resource
    private ScriptWordRepository scriptWordRepository;

    @Test
    void contextLoads() {
    }

    /**
     * 转换List<Object[]>到List<Map>
     */
    @Test
    @Transactional
    void test() {
        List<ScriptWord> scriptWordList = scriptWordRepository.findAll();
        scriptWordList.stream().filter(scriptWord -> scriptWord.getWordName().equals("farmland")).forEach(scriptWord ->
                {
                    Map wordAndVariations = issueRepository.getWordAndVariations(scriptWord.getWordId());
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("'''");
                    stringBuilder.append(wordAndVariations.get("word"));
                    stringBuilder.append("''");
                    String variations = (String) wordAndVariations.get("variations");
                    if(StringUtils.isNotBlank(variations)){
                        String[] variationsArray = wordAndVariations.get("variations").toString().split(" ");
                        for (String variation : variationsArray) {
                            stringBuilder.append(" or ''" + variation.replaceAll(",","") + "''");
                        }
                    }

                    List<Map<String, Object>> objects = issueRepository.getScriptListByWordAndVariations(wordAndVariations.get("word").toString(), stringBuilder.toString());
                    List<ScriptInfo> scriptInfoList = JSONObject.parseArray(JSONObject.toJSONString(objects), ScriptInfo.class);
                    if(objects.size()==0&&scriptWord.getStatus()==0){
//                        scriptWordRepository.updateScriptWord(scriptWord.getWordId());
                        System.out.println(scriptWord.getWordId()+",");
                    }
                }
                );



    }


    private Map<String, Integer> objectArrayList2Map(List<Object[]> objects) {
        if (objects == null) {
            return null;
        }
        Map<String, Integer> map = new HashMap<>();
        for (Object[] object : objects) {
            map.put(String.valueOf(object[0]), Integer.valueOf(String.valueOf(object[1])));
        }
        return map;
    }


}

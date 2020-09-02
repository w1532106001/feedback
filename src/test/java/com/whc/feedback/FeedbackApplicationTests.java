package com.whc.feedback;

import com.alibaba.fastjson.JSONObject;
import com.whc.feedback.dao.issue.entity.Listen;
import com.whc.feedback.dao.issue.repository.IssueRepository;
import com.whc.feedback.dao.issue.repository.ListenRepository;
import com.whc.feedback.entity.ScriptInfo;
import com.whc.feedback.entity.WordScriptInfo;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

    @Test
    void contextLoads() {
    }

    /**
     * 转换List<Object[]>到List<Map>
     */
    @Test
    void test() {
        Map wordAndVariations = issueRepository.getWordAndVariations("22804");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("'''");
        stringBuilder.append(wordAndVariations.get("word"));
        stringBuilder.append("''");
        String[] variations = wordAndVariations.get("variations").toString().split(" ");
        for (String variation : variations) {
            stringBuilder.append(" or ''" + variation.replaceAll(",","") + "''");
        }
        List<Map<String, Object>> objects = issueRepository.getScriptListByWordAndVariations(wordAndVariations.get("word").toString(), stringBuilder.toString());
        System.out.println(objects);
        System.out.println(stringBuilder);
        List<ScriptInfo> scriptInfoList = JSONObject.parseArray(JSONObject.toJSONString(objects), ScriptInfo.class);
        List<Listen> listenList = listenRepository.findByWordId(22212);
        WordScriptInfo wordScriptInfo = new WordScriptInfo();
        wordScriptInfo.setWord(wordAndVariations.get("word").toString());
        wordScriptInfo.setWordid(22212);

        List<ScriptInfo> scriptInfoListByListen = new ArrayList<>();
        int count = 0;
        for (ScriptInfo scriptInfo : scriptInfoList) {
            boolean has = listenList.stream().anyMatch(e -> scriptInfo.getScriptid().equals(e.getScriptId()));
            if (!has) {
                scriptInfoListByListen.add(scriptInfo);
                count++;
            }
            if(count==20){
                break;
            }
        }
        wordScriptInfo.setScriptInfoList(scriptInfoListByListen);
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

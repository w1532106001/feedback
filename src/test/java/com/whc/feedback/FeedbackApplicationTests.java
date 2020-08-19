package com.whc.feedback;

import com.whc.feedback.dao.issue.repository.IssueRepository;
import com.whc.feedback.dao.user.repository.UserRepository;
import com.whc.feedback.service.IndexService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class FeedbackApplicationTests {

    @Autowired
    private IssueRepository issueRepository;

    @Test
    void contextLoads() {
    }

    /**
     * 转换List<Object[]>到List<Map>
     */
    @Test
    void test(){
        List<Object[]> objects = issueRepository.getUserList();
        Map<Integer,String> map = objectArrayList2Map(objects);
        System.out.println(map.keySet().toArray()[0]);
    }

    private Map<Integer,String> objectArrayList2Map(List<Object[]> objects){
        if(objects==null){
            return null;
        }
        Map<Integer,String> map = new HashMap<>();
        for (Object[] object : objects) {
            map.put(Integer.valueOf(String.valueOf(object[0])),String.valueOf(object[1]));
        }
        return map;
    }
}

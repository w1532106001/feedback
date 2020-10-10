package com.whc.feedback.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.whc.feedback.common.ServerResponse;
import com.whc.feedback.dao.issue.entity.*;
import com.whc.feedback.dao.issue.repository.*;
import com.whc.feedback.dao.user.entity.User;
import com.whc.feedback.dao.user.repository.UserRepository;
import com.whc.feedback.entity.ScriptInfo;
import com.whc.feedback.entity.WordScriptInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: WHC
 * @Date: 2019/9/9 16:31
 * @description
 */
@Service
@Slf4j
public class IndexServiceImpl implements IndexService {

    @Value("${file.filepath}")
    private String filepath;

    @Value("${file.wordListen}")
    private String wordListen;

    @Value("${file.levelScript}")
    private String levelScript;

    String encoding = "UTF-8"; //字符编码
    @Resource
    private UserRepository userRepository;
    @Resource
    private IssueRepository issueRepository;
    @Resource
    private MessageRepository messageRepository;
    @Resource
    private VersionRepository versionRepository;
    @Resource
    private ScriptWordRepository scriptWordRepository;

    @Resource
    private ListenRepository listenRepository;

    @Override
    public ServerResponse login(String email, String pwd) {
        User user = userRepository.findUserByEmailAndPwd(email, pwd);
        if (null == user) {
            return ServerResponse.createByErrorMessage("账号或密码错误");
        }
        user.setPwd(null);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse upload(HttpServletRequest request, MultipartFile[] file) {
        List fileNameList = new ArrayList();
        try {
            //如果目录不存在，自动创建文件夹
            File dir = new File(filepath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            //遍历文件数组执行上传
            for (int i = 0; i < file.length; i++) {
                if (file[i] != null) {
                    //文件后缀名
                    String suffix = file[i].getOriginalFilename().substring(file[i].getOriginalFilename().lastIndexOf("."));
                    //上传文件名
                    String filename = UUID.randomUUID() + suffix;
                    fileNameList.add(filename);
                    //调用上传方法
                    executeUpload(filepath, filename, file[i]);
                }
            }
        } catch (Exception e) {
            //打印错误堆栈信息
            e.printStackTrace();
            return ServerResponse.createBySuccessMessage("上传失败");
        }
        return ServerResponse.createBySuccess("上传成功", fileNameList.toString().replace("[", "").replace("]", ""));

    }


    @Override
    public ServerResponse createIssue(Issue issue) {
        issue.setIssueDate(new Date());
        issue.setStatus(Byte.valueOf("0"));
        issue.setIsTemporarilyUnableToProcess(false);
        Issue result = issueRepository.save(issue);
        return result == null ? ServerResponse.createByErrorMessage("创建失败") : ServerResponse.createBySuccessMessage("创建成功");
    }

    @Override
    public ServerResponse getListIssue(String status, String userId, String level, String stage, String system, String handlerId, Integer pageSize) {
        Issue issue = new Issue();
        if (StringUtils.isNotEmpty(status)) {
            issue.setStatus(Byte.valueOf(status));
        }
        if (StringUtils.isNotEmpty(userId)) {
            issue.setCreator(Integer.valueOf(userId));
        }
        if (StringUtils.isNotEmpty(level)) {
            issue.setLevel(Byte.valueOf(level));
        }
        if (StringUtils.isNotEmpty(stage)) {
            issue.setStage(Byte.valueOf(stage));
        }
        if (StringUtils.isNotEmpty(system)) {
            issue.setSystem(system);
        }
        if (StringUtils.isNotEmpty(handlerId)) {
            issue.setHandlerId(Integer.valueOf(handlerId));
        }

        Example<Issue> issueExample = Example.of(issue);
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "isTemporarilyUnableToProcess");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "issueDate");
//        Sort sort = Sort.by(Sort.Direction.DESC, "issueDate");

        Page<Issue> issueList = issueRepository.findAll(issueExample, PageRequest.of(pageSize, 10, Sort.by(order1, order2)));
        return ServerResponse.createBySuccess(issueList);
    }

    @Override
    public ServerResponse getListIssue(String status, String userId, String level, String stage, String system, String handlerId, String category, String date, Integer pageSize) {

        Specification<Issue> issueSpecification = (Specification<Issue>) (issueRoot, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicatesList = new ArrayList<>();
            if (StringUtils.isNotEmpty(status)) {
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(
                                        issueRoot.get("status"), status)));
            }
            if (StringUtils.isNotEmpty(userId)) {
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(
                                        issueRoot.get("creator"), userId)));
            }
            if (StringUtils.isNotEmpty(level)) {
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(
                                        issueRoot.get("level"), level)));
            }
            if (StringUtils.isNotEmpty(stage)) {
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(
                                        issueRoot.get("stage"), stage)));
            }
            if (StringUtils.isNotEmpty(system)) {
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(
                                        issueRoot.get("system"), system)));
            }
            if (StringUtils.isNotEmpty(handlerId)) {
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(
                                        issueRoot.get("handlerId"), handlerId)));
            }
            if (StringUtils.isNotEmpty(category)) {
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(
                                        issueRoot.get("category"), category)));
            }
            if (StringUtils.isNotBlank(date)) {
                predicatesList.add(
                        criteriaBuilder.and(
                                criteriaBuilder.between(
                                        issueRoot.get("issueDate"), getStartTime(date), getEndTime(date))));
            }
            return criteriaBuilder.and(predicatesList.toArray(new Predicate[predicatesList.size()]));
        };

        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "isTemporarilyUnableToProcess");
        Sort.Order order2 = new Sort.Order(Sort.Direction.DESC, "issueDate");
        Page<Issue> issueList = issueRepository.findAll(issueSpecification, PageRequest.of(pageSize, 10, Sort.by(order1, order2)));
        return ServerResponse.createBySuccess(issueList);
    }


    @Override
    public ServerResponse getUserList() {
        Map<Integer, String> map = objectArrayList2Map(issueRepository.getUserList());
        return ServerResponse.createBySuccess(map);
    }

    @Override
    public ServerResponse getMessageList(Integer issueId) {
        Message message = new Message();
        message.setIssueId(issueId);
        Example<Message> messageExample = Example.of(message);
        Sort sort = Sort.by(Sort.Direction.ASC, "messageDate");
        List<Message> messages = messageRepository.findAll(messageExample, sort);
        return ServerResponse.createBySuccess(messages);
    }

    @Override
    public ServerResponse createMessage(Integer issueId, Integer userId, String message, Byte status) {
        Message messageVO = new Message();
        messageVO.setIssueId(issueId);
        messageVO.setUserId(userId);
        messageVO.setMessage(message);
        messageVO.setMessageDate(new Date());
        messageVO.setIssueStatus(status);
        Message resultMessage = messageRepository.save(messageVO);
        if (resultMessage == null) {
            return ServerResponse.createByError();
        }
        /*
          问题是否关闭
         */
        if (status == 3) {
            issueRepository.updateStatusAndCloseDateById(issueId, status, new Date());
        } else {
            issueRepository.updateStatusById(issueId, status);
        }
        return ServerResponse.createBySuccess(resultMessage);
    }

    @Override
    @Transactional
    public ServerResponse assignmentIssue(Integer issueId, Integer userId) {
        Integer integer = issueRepository.updateStatusAndHandlerIdAndAssignmentDateById(issueId, Byte.valueOf("1"), new Date(), userId);
        return ServerResponse.createBySuccess(integer);
    }

    @Override
    public ServerResponse getIssue(Integer issueId) {
        IssueVO issueVO = new IssueVO();
        issueVO.setIssue(issueRepository.findIssueById(issueId));
        issueVO.setMessageList(messageRepository.findAllByIssueId(issueId));
        return ServerResponse.createBySuccess(issueVO);
    }

    @Override
    public ServerResponse getVersion(String version) {
        Version version1 = versionRepository.findAll().get(0);
        JSONObject jsonObject = new JSONObject();
        if (!version1.getNewVersion().equals(version)) {
            jsonObject.put("update", "Yes");
            jsonObject.put("new_version", version1.getNewVersion());
            jsonObject.put("apk_file_url", version1.getApkFileUrl());
            jsonObject.put("update_log", version1.getUpdateLog());
            jsonObject.put("target_size", version1.getTargetSize());
            jsonObject.put("new_md5", version1.getNewMd5());
            jsonObject.put("constraint", version1.getConstraint() == 1);
            return ServerResponse.createBySuccess(jsonObject);
        } else {
            jsonObject.put("update", "No");
        }
        return ServerResponse.createBySuccess(jsonObject);
    }

    @Override
    public ServerResponse getNum(Integer handlerId) {
        return ServerResponse.createBySuccess(issueRepository.getNumByStatusAndHandlerId(handlerId));
    }

    @Override
    @Transactional
    public ServerResponse isTemporarilyUnableToProcess(Boolean isTemporarilyUnableToProcess, Integer issueId) {
        return ServerResponse.createBySuccess(issueRepository.isTemporarilyUnableToProcess(issueId, isTemporarilyUnableToProcess));
    }

    /**
     * 提取上传方法为公共方法
     *
     * @param uploadDir 上传文件目录
     * @param file      上传对象
     * @throws Exception 异常
     */
    private void executeUpload(String uploadDir, String filename, MultipartFile file) throws Exception {

        //服务器端保存的文件对象
        File serverFile = new File(uploadDir + filename);
        //将上传的文件写入到服务器端文件内
        file.transferTo(serverFile);
    }

    public static void main(String[] args) {

        System.out.println((1604736 / 100000) + File.separator + 1604736 + ".json");

    }

    private Map<Integer, String> objectArrayList2Map(List<Object[]> objects) {
        if (objects == null) {
            return null;
        }
        Map<Integer, String> map = new HashMap<>();
        for (Object[] object : objects) {
            map.put(Integer.valueOf(String.valueOf(object[0])), String.valueOf(object[1]));
        }
        return map;
    }

    private Date getStartTime(String time) {
        Date date = null;
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //加上时间
        //必须捕获异常
        try {
            date = sDateFormat.parse(time + " 00:00:00");

        } catch (ParseException px) {
            px.printStackTrace();
        }
        return date;
    }

    private Date getEndTime(String time) {
        Date date = null;
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //加上时间
        //必须捕获异常
        try {
            date = sDateFormat.parse(time + " 23:59:59");

        } catch (ParseException px) {
            px.printStackTrace();
        }
        return date;
    }

    @Override
    @Transactional
    public ServerResponse getScriptWordList() {
        char a = 'A';
        List<ScriptWord> scriptWordList = scriptWordRepository.findScriptWordsByStatus(0);

        Integer num = 0;
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 26; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("alphabet", a);
            JSONArray wordArray = new JSONArray();
            for (ScriptWord scriptWord : scriptWordList) {
                if (scriptWord.getWordName().substring(0, 1).equalsIgnoreCase(String.valueOf(a))) {
                    scriptWord.setSort(++num);
                    wordArray.add(scriptWord);
                }
            }
            jsonObject.put("wordList", wordArray);
            jsonArray.add(jsonObject);
            a++;
        }
        return ServerResponse.createBySuccess(jsonArray);
    }

    @Override
    public ServerResponse getListenScriptByWordId(String wordId) {

        Map wordAndVariations = issueRepository.getWordAndVariations(wordId);
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
        List<Listen> listenList = listenRepository.findByWordId(Integer.valueOf(wordId));
        WordScriptInfo wordScriptInfo = new WordScriptInfo();
        wordScriptInfo.setWord(wordAndVariations.get("word").toString());
        wordScriptInfo.setWordid(Integer.valueOf(wordId));

        List<ScriptInfo> scriptInfoListByListen = new ArrayList<>();
        int count = 0;
        for (ScriptInfo scriptInfo : scriptInfoList) {
            boolean has = listenList.stream().anyMatch(e -> scriptInfo.getScriptid().equals(e.getScriptId()));
            if (!has) {
                scriptInfo.setMp3("https://content.smartmicky.com/media/scriptid/" + scriptInfo.getScriptid() / 100000 + "/" + scriptInfo.getScriptid() + ".mp3");
                if (checkMP3Exist(levelScript + "\\" + scriptInfo.getScriptid() / 100000 + "\\" + scriptInfo.getScriptid() + ".mp3") &&
                        checkMP3Exist(levelScript + "\\" + scriptInfo.getScriptid() / 100000 + "\\" + scriptInfo.getScriptid() + ".json")) {
                    scriptInfoListByListen.add(scriptInfo);
                    count++;
                }
            }
            if (count == 20) {
                break;
            }
        }

        if (scriptInfoListByListen.size() == 0) {
            scriptWordRepository.updateScriptWord(wordId);
        }
        wordScriptInfo.setScriptInfoList(scriptInfoListByListen);
        return ServerResponse.createBySuccess(wordScriptInfo);
    }

    @Override
    public ServerResponse getListenList() {
        return ServerResponse.createBySuccess(listenRepository.findAll());
    }

    @Override
    public ServerResponse updateListenStatus(Integer scriptId, Integer scriptStatus, String realName, Integer userId, String wordName, Integer wordId) {
        if (scriptId == null || scriptStatus == null || userId == null) {
            return ServerResponse.createByErrorMessage("数据异常");
        }
        Listen listen = listenRepository.findByScriptIdAndWordId(scriptId, wordId);
        Listen result = null;
        if (null == listen) {
            listen = new Listen();
            listen.setScriptId(scriptId);
            listen.setCreateTime(new Date());
            listen.setUpdateTime(new Date());
            listen.setUserId(userId);
            listen.setRealName(realName);
            listen.setScriptStatus(scriptStatus);
            listen.setWordId(wordId);
            listen.setWordName(wordName);
        } else {
            listen.setUpdateTime(new Date());
            listen.setScriptStatus(scriptStatus);
            listen.setUserId(userId);
            listen.setRealName(realName);
            listen.setWordId(wordId);
            listen.setWordName(wordName);
        }
        result = listenRepository.save(listen);
        if (result != null) {
            return ServerResponse.createBySuccess(result);
        }
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse checkWordScriptExists() {
        File wordListenFile = new File(wordListen);
        List<ScriptWord> scriptWordList = scriptWordRepository.findAll();
        List<ScriptWord> scriptWords = new ArrayList<>();
        for (ScriptWord scriptWord : scriptWordList) {
            boolean b = true;
            File file = new File(wordListenFile.getAbsolutePath() + File.separator + scriptWord.getWordId().toString() + ".json");
            log.info("检测单词脚本是否存在 file:" + file.getAbsolutePath());
            if (file.exists()) {
                b = false;
            }
            if (b) {
                scriptWords.add(scriptWord);
            }
        }

        return ServerResponse.createBySuccess(scriptWords);
    }

    @Override
    public ServerResponse updateWordIdAndWordName() {
        List<Listen> listenList = listenRepository.findAll();
        List<Listen> listens = new ArrayList<>();
        for (Listen listen : listenList) {
            if (listen.getWordId() == null) {
                listens.add(listen);
            }
        }

        File wordListenFile = new File(wordListen);
        File levelScriptFile = new File(levelScript);
        if (wordListenFile.exists() && levelScriptFile.exists()) {
            for (File file : wordListenFile.listFiles()) {

                StringBuilder result = new StringBuilder();
                BufferedReader br = null;//构造一个BufferedReader类来读取文件
                try {
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
                    String s = null;
                    while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                        result.append(System.lineSeparator() + s);
                    }
                    br.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONObject wordListenJSON = JSONObject.parseObject(result.toString());
                WordScriptInfo wordScriptInfo = new WordScriptInfo();
                wordScriptInfo.setWord(wordListenJSON.getString("word"));
                wordScriptInfo.setWordid(wordListenJSON.getInteger("wordid"));
                wordScriptInfo.setSimilarsoundwords1(wordListenJSON.getString("similarsoundwords1"));
                wordScriptInfo.setSimilarsoundwords1id(wordListenJSON.getString("similarsoundwords1id"));
                wordScriptInfo.setSimilarsoundwords2(wordListenJSON.getString("similarsoundwords2"));
                wordScriptInfo.setSimilarsoundwords2id(wordListenJSON.getString("similarsoundwords2id"));
                wordScriptInfo.setSimilarlookwords1(wordListenJSON.getString("similarlookwords1"));
                wordScriptInfo.setSimilarlookwords1id(wordListenJSON.getString("similarlookwords1id"));
                wordScriptInfo.setSimilarlookwords2(wordListenJSON.getString("similarlookwords2"));
                wordScriptInfo.setSimilarlookwords2id(wordListenJSON.getString("similarlookwords2id"));
                JSONObject level = wordListenJSON.getJSONObject("level");
                String level1Text = level.getString("1");
                String level2Text = level.getString("2");
                String level3Text = level.getString("3");


                List<ScriptInfo> level1 = new ArrayList<>();
                List<ScriptInfo> level2 = new ArrayList<>();
                List<ScriptInfo> level3 = new ArrayList<>();


                if (StringUtils.isNotEmpty(level1Text)) {
                    ScriptInfo scriptInfo = null;
                    for (String s : level1Text.split(",")) {
                        Integer levelScriptId = Integer.valueOf(s);
                        for (Listen listen : listens) {
                            if (listen.getScriptId().equals(levelScriptId)) {
                                map.forEach((k, v) -> {
                                    if (listen.getRealName().equals(k)) {
                                        for (String s1 : v) {
                                            if (wordScriptInfo.getWord().substring(0, 1).equalsIgnoreCase(s1)) {
                                                listen.setWordId(wordScriptInfo.getWordid());
                                                listen.setWordName(wordScriptInfo.getWord());
                                            }
                                        }
                                    }
                                });
//                                if(listen.getWordId()==null||StringUtils.isEmpty(listen.getWordName())){

//                                }
//                                break;
                            }
                        }
                    }
                }
                wordScriptInfo.setLevel1(level1);

                if (StringUtils.isNotEmpty(level2Text)) {
                    for (String s : level2Text.split(",")) {
                        Integer levelScriptId = Integer.valueOf(s);
                        for (Listen listen : listens) {
                            if (listen.getScriptId().equals(levelScriptId)) {
                                map.forEach((k, v) -> {
                                    if (listen.getRealName().equals(k)) {
                                        for (String s1 : v) {
                                            if (wordScriptInfo.getWord().substring(0, 1).equalsIgnoreCase(s1)) {
                                                listen.setWordId(wordScriptInfo.getWordid());
                                                listen.setWordName(wordScriptInfo.getWord());
                                            }
                                        }
                                    }
                                });
                            }
                        }

                    }
                }
                wordScriptInfo.setLevel2(level2);

                if (StringUtils.isNotEmpty(level3Text)) {
                    ScriptInfo scriptInfo = null;
                    for (String s : level3Text.split(",")) {
                        Integer levelScriptId = Integer.valueOf(s);
                        boolean b = listens.stream().anyMatch(e -> e.getScriptId().equals(levelScriptId));
                        for (Listen listen : listens) {
                            if (listen.getScriptId().equals(levelScriptId)) {
                                map.forEach((k, v) -> {
                                    if (listen.getRealName().equals(k)) {
                                        for (String s1 : v) {
                                            if (wordScriptInfo.getWord().substring(0, 1).equalsIgnoreCase(s1)) {
                                                listen.setWordId(wordScriptInfo.getWordid());
                                                listen.setWordName(wordScriptInfo.getWord());
                                            }
                                        }
                                    }
                                });
                            }
                        }

                    }
                }
                wordScriptInfo.setLevel3(level3);


//                }
            }
        }

        return ServerResponse.createBySuccess(listenRepository.saveAll(listens));

    }

    @Override
    public ServerResponse getDailyListenCount(String time) {
        return ServerResponse.createBySuccess(objectArrayList2Map2(issueRepository.getDailyListenCount(time)));
    }


    ScriptInfo getScriptInfo(Integer id) {
        File jsonFile = new File(levelScript + File.separator + (id / 100000) + File.separator + id + ".json");
        log.info("jsonFilePath:" + jsonFile.getAbsolutePath());
        File mp3 = new File(levelScript + File.separator + (id / 100000) + File.separator + id + ".mp3");
        log.info("mp3FilePath:" + mp3.getAbsolutePath());
        ScriptInfo scriptInfo = new ScriptInfo();
        if (jsonFile.exists()) {
            StringBuilder result = new StringBuilder();
            BufferedReader br = null;//构造一个BufferedReader类来读取文件
            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile), encoding));
                String s = null;
                while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                    result.append(System.lineSeparator() + s);
                }
                br.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mp3.exists()) {
                scriptInfo.setMp3("https://content.smartmicky.com/media/scriptid/" + (id / 100000) + "/" + id + ".mp3");
            }
            if (StringUtils.isNotEmpty(result)) {
                JSONObject jsonObject = JSONObject.parseObject(result.toString());
                scriptInfo.setScriptid(jsonObject.getInteger("scriptid"));
                scriptInfo.setMediaid(jsonObject.getInteger("mediaid"));
                scriptInfo.setSequenceid(jsonObject.getInteger("sequenceid"));
                scriptInfo.setStart_time(jsonObject.getDouble("start_time"));
                scriptInfo.setEnd_time(jsonObject.getDouble("end_time"));
                scriptInfo.setScript_eng(jsonObject.getString("script_eng"));
                scriptInfo.setBhead(jsonObject.getBoolean("bhead"));
                scriptInfo.setScript_chn(jsonObject.getString("script_chn"));
                scriptInfo.setKeywords(jsonObject.getString("keywords"));
                scriptInfo.setQuestionid(jsonObject.getString("questionid"));
                scriptInfo.setQuestion_starttime(jsonObject.getString("question_starttime"));
                scriptInfo.setQuestion_endtime(jsonObject.getString("question_endtime"));
                scriptInfo.setParagraphid(jsonObject.getInteger("paragraphid"));
                scriptInfo.setTriggertime(jsonObject.getString("triggertime"));
                scriptInfo.setBlistening(jsonObject.getBoolean("blistening"));
                scriptInfo.setSubtitle_chinese(jsonObject.getString("subtitle_chinese"));
                scriptInfo.setSubtitle_english(jsonObject.getString("subtitle_english"));
                scriptInfo.setTitle_chinese(jsonObject.getString("title_chinese"));
                scriptInfo.setTitle_english(jsonObject.getString("title_english"));
                scriptInfo.setSyllabusname(jsonObject.getString("syllabusname"));
                scriptInfo.setBookname(jsonObject.getString("bookname"));
                scriptInfo.setChapter(jsonObject.getString("chapter"));
                scriptInfo.setIsPlaying("false");
            }
        }
        return scriptInfo;
    }


    Map<String, List<String>> map = new HashMap<>();

    {
        map.put("慕富强", Arrays.asList("a", "b", "s", "t"));
        map.put("张晶", Arrays.asList("p"));
        map.put("傅晓峻", Arrays.asList("d", "e"));
        map.put("孙巍.学生", Arrays.asList("b"));
        map.put("穆晔", Arrays.asList("r", "s"));
        map.put("陈菲菲", Arrays.asList("c", "d"));
        map.put("程醇", Arrays.asList("h"));
        map.put("冯丽芳", Arrays.asList("o", "p"));
        map.put("郭继锴", Arrays.asList("b", "c"));
        map.put("叶菁", Arrays.asList("d"));
        map.put("李春凤", Arrays.asList("g", "h"));
        map.put("曹镇川", Arrays.asList("s"));
        map.put("赵剑萍", Arrays.asList("b"));
        map.put("贺东昶", Arrays.asList("p"));
        map.put("黄明玲", Arrays.asList("p", "q", "r"));
        map.put("龚杰", Arrays.asList("l"));
        map.put("胡小兰", Arrays.asList("n", "o"));
        map.put("章士骏", Arrays.asList("h", "i"));
        map.put("王浩晨", Arrays.asList("s"));
    }

    private Map<String, Integer> objectArrayList2Map2(List<Object[]> objects) {
        if (objects == null) {
            return null;
        }
        Map<String, Integer> map = new HashMap<>();
        for (Object[] object : objects) {
            map.put(String.valueOf(object[0]), Integer.valueOf(String.valueOf(object[1])));
        }
        return map;
    }


    boolean checkMP3Exist(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }
}

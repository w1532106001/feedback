package com.whc.feedback.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.whc.feedback.common.ServerResponse;
import com.whc.feedback.dao.issue.entity.Issue;
import com.whc.feedback.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: WHC
 * @Date: 2019/9/10 10:26
 * @description
 */
@RestController
public class IndexController {

    @Autowired
    private IndexService indexService;

    @Value("${file.downLoadPath}")
    private String downLoadPath;

    @Value("${file.filepath}")
    private String mediaPath;

    @PostMapping("/index/user_login")
    @ResponseBody
    public ServerResponse userLogin(@ModelAttribute(value = "email") String email,
                                    @ModelAttribute(value = "pwd") String pwd) {
        return indexService.login(email, pwd);
    }
    @PostMapping("/index/createIssue")
    public ServerResponse createIssue(@RequestBody Issue issue){
        return indexService.createIssue(issue);
    }

    @PostMapping("/index/file_upload")
    public ServerResponse fileUpload(HttpServletRequest request,MultipartFile[] files){
        return indexService.upload(request,files);
    }

    @PostMapping("/index/listIssue")
    public ServerResponse getAllIssue(@ModelAttribute("status") String status,@ModelAttribute("userId") String userId
            ,@ModelAttribute("level") String level, @ModelAttribute("stage") String stage
            , @ModelAttribute("system") String system, @ModelAttribute("handlerId") String handlerId
            ,@ModelAttribute("pageSize") Integer pageSize){
        return indexService.getListIssue(status, userId, level, stage, system,handlerId, pageSize);
    }

    @PostMapping("/index/newListIssue")
    public ServerResponse getNewListIssue(@ModelAttribute("status") String status,@ModelAttribute("userId") String userId
            ,@ModelAttribute("level") String level, @ModelAttribute("stage") String stage
            , @ModelAttribute("system") String system, @ModelAttribute("handlerId") String handlerId
            , @ModelAttribute("category") String category,@ModelAttribute("date") String date
            ,@ModelAttribute("pageSize") Integer pageSize){
        return indexService.getListIssue(status, userId, level, stage, system,handlerId,category,date,pageSize);
    }

    @PostMapping("/index/userList")
    public ServerResponse getUserList(){
        return indexService.getUserList();
    }

    @PostMapping("/index/listMessage")
    public ServerResponse getMessageList(Integer issueId){
        return indexService.getMessageList(issueId);
    }

    @PostMapping("/index/createMessage")
    public ServerResponse createMessage(@ModelAttribute("issueId") Integer issueId,@ModelAttribute("userId") Integer userId
            ,@ModelAttribute("message") String message,@ModelAttribute("status") Byte status){
        return indexService.createMessage(issueId,userId,message,status);
    }

    @PostMapping("/index/getIssue")
    public ServerResponse getIssue(@ModelAttribute("issueId") Integer issueId){
        return indexService.getIssue(issueId);
    }

    @PostMapping("/index/assignmentIssue")
    public ServerResponse assignmentIssue(@ModelAttribute("issueId") Integer issueId,@ModelAttribute("userId") Integer userId){
        return indexService.assignmentIssue(issueId,userId);
    }

    @PostMapping("/index/getNum")
    public ServerResponse getNum(@ModelAttribute("handlerId") Integer handlerId){
        return indexService.getNum(handlerId);
    }

    @PostMapping("/index/isTemporarilyUnableToProcess")
    public ServerResponse isTemporarilyUnableToProcess(@ModelAttribute("isTemporarilyUnableToProcess") Boolean isTemporarilyUnableToProcess,
                                                       @ModelAttribute("issueId") Integer issueId){
        return indexService.isTemporarilyUnableToProcess(isTemporarilyUnableToProcess,issueId);
    }

    /**
     * 文件下载接口
     * @param fileName  文件上传时，返回的相对路径
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "index/download/{fileName}",method = RequestMethod.GET)
    public void downLoad(@PathVariable String fileName,HttpServletRequest request, HttpServletResponse response) throws Exception {
        File f = new File(downLoadPath+fileName);
        if (!f.exists()) {
            response.sendError(404, "File not found!");
            return;
        }
        String fileName2 = f.getName();
        fileName2 = new String(fileName2.getBytes("UTF-8"), "ISO-8859-1");

        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;
        response.reset(); // 非常重要
        response.setHeader("Content-Length",String.valueOf(f.length()));
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName2);
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0)
            out.write(buf, 0, len);
        br.close();
        out.close();
    }


    @RequestMapping(value = "/index/media/{fileName}",method = RequestMethod.GET)
    public void getImage(@PathVariable String fileName,HttpServletRequest request,HttpServletResponse response) throws IOException {
        File f = new File(mediaPath+fileName);
        if (!f.exists()) {
            response.sendError(404, "File not found!");
            return;
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;
        response.reset(); // 非常重要
        if(fileName.contains(".mp4")||fileName.contains(".MP4")||fileName.contains(".mov")||fileName.contains(".MOV")){
            RandomAccessFile randomFile = new RandomAccessFile(f, "r");//只读模式

            long contentLength = randomFile.length();

            String range = request.getHeader("Range");

            int start = 0, end = 0;

            if(range != null && range.startsWith("bytes=")){

                String[] values = range.split("=")[1].split("-");

                start = Integer.parseInt(values[0]);

                if(values.length > 1){

                    end = Integer.parseInt(values[1]);

                }

            }

            int requestSize = 0;

            if(end != 0 && end > start){

                requestSize = end - start + 1;

            } else {

                requestSize = Integer.MAX_VALUE;

            }

            byte[] buffer = new byte[4096];

            response.setContentType("video/mp4");

            response.setHeader("Accept-Ranges", "bytes");

            response.setHeader("ETag", fileName);

            response.setHeader("Last-Modified", new Date().toString());

            //第一次请求只返回content length来让客户端请求多次实际数据

            if(range == null){

                response.setHeader("Content-length", contentLength + "");

            }else{

                //以后的多次以断点续传的方式来返回视频数据

                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);//206

                long requestStart = 0, requestEnd = 0;

                String[] ranges = range.split("=");

                if(ranges.length > 1){

                    String[] rangeDatas = ranges[1].split("-");

                    requestStart = Integer.parseInt(rangeDatas[0]);

                    if(rangeDatas.length > 1){

                        requestEnd = Integer.parseInt(rangeDatas[1]);

                    }

                }

                long length = 0;

                if(requestEnd > 0){

                    length = requestEnd - requestStart + 1;

                    response.setHeader("Content-length", "" + length);

                    response.setHeader("Content-Range", "bytes " + requestStart + "-" + requestEnd + "/" + contentLength);

                }else{

                    length = contentLength - requestStart;

                    response.setHeader("Content-length", "" + length);

                    response.setHeader("Content-Range", "bytes "+ requestStart + "-" + (contentLength - 1) + "/" + contentLength);

                }

            }

            ServletOutputStream out = response.getOutputStream();

            int needSize = requestSize;

            randomFile.seek(start);

            while(needSize > 0){

                int len1 = randomFile.read(buffer);

                if(needSize < buffer.length){

                    out.write(buffer, 0, needSize);

                } else {

                    out.write(buffer, 0, len1);

                    if(len1 < buffer.length){

                        break;

                    }

                }

                needSize -= buffer.length;

            }

            randomFile.close();

            out.close();

            return;

//            response.setContentType("video/mpeg4");
        }else {
            response.setContentType("image/jpeg");
        }
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0)
            out.write(buf, 0, len);
        br.close();
        out.close();
    }

    @GetMapping("/index/appVersion")
    public ServerResponse index(@ModelAttribute("version")  String version){
        return indexService.getVersion(version);
    }

    @GetMapping("/index/scriptWordList")
    public ServerResponse getScriptWordList(){
        return indexService.getScriptWordList();
    }

    @GetMapping("/index/scriptWordInfo/{id}")
    public ServerResponse getListenScriptByWordId(@PathVariable Integer id){
        return indexService.getListenScriptByWordId(id.toString());
    }

    @GetMapping("/index/listenList")
    public ServerResponse getListenList(){
        return indexService.getListenList();
    }

    @PostMapping("index/updateListenStatus")
    public ServerResponse updateListenStatus(Integer scriptId,Integer scriptStatus,String realName,Integer userId,String wordName,Integer wordId){
        return indexService.updateListenStatus(scriptId,scriptStatus,realName,userId,wordName,wordId);
    }

    @GetMapping("index/checkWordScriptExists")
    public ServerResponse checkWordScriptExists(){
        return indexService.checkWordScriptExists();
    }

//    @GetMapping("index/updateWordIdAndWordName")
//    public ServerResponse updateWordIdAndWordName(){
//        return indexService.updateWordIdAndWordName();
//    }

}

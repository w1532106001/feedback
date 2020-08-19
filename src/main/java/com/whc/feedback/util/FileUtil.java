package com.whc.feedback.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Author: WHC
 * @Date: 2019/10/19 21:36
 * @description
 */
@Component
public class FileUtil {

//    //文件上传路径
//    @Value("${file.filepath}")
//    private String filepath;

    //文件随机名称
    private String origName;

    public String getOrigName() {
        return origName;
    }

    public void setOrigName(String origName) {
        this.origName = origName;
    }

    /**
     *
     * @param request
     * @param path_deposit 新增目录名 支持多级不存在目录
     * @param file 待文件
     * @param isRandomName 是否要基于图片名称重新编排名称
     * @return
     */
    public String uploadImage(HttpServletRequest request, String path_deposit, MultipartFile file, boolean isRandomName) {


        //上传
        try {
            String[] typeImg={"gif","png","jpg","docx","doc","pdf"};

            if(file!=null){
                origName=file.getOriginalFilename();// 文件原名称
                System.out.println("上传的文件原名称:"+origName);
                // 判断文件类型
                String type=origName.indexOf(".")!=-1?origName.substring(origName.lastIndexOf(".")+1, origName.length()):null;
                if (type!=null) {
                    boolean booIsType=false;
                    for (int i = 0; i < typeImg.length; i++) {
                        if (typeImg[i].equals(type.toLowerCase())) {
                            booIsType=true;
                        }
                    }
                    //类型正确
                    if (booIsType) {
                        //存放图片文件的路径
                        //String path="O:\\QMDownload\\Hotfix\\";
                        //String path=filepath;
                        //System.out.print("文件上传的路径"+path);
                        //组合名称
                        //String fileSrc = path+path_deposit;
                        String fileSrc = path_deposit;
                        //是否随机名称
                        if(isRandomName){
                            //随机名规则：文件名+_CY+当前日期+8位随机数+文件后缀名
                            origName= UUID.randomUUID().toString()+origName.substring(origName.lastIndexOf("."));
                        }
                        System.out.println("随机文件名："+origName);
                        //判断是否存在目录
                        File targetFile=new File(fileSrc,origName);
                        if(!targetFile.exists()){
                            targetFile.getParentFile().mkdirs();//创建目录
                        }

                        //上传
                        file.transferTo(targetFile);
                        //完整路径
                        System.out.println("完整路径:"+targetFile.getAbsolutePath());
                        return fileSrc;
                    }
                }
            }
            return null;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期并去掉”-“
     * @param date
     * @return
     */
    public String formateString(Date date){
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        String list[] = dateFormater.format(date).split("-");
        String result = "";
        for (int i=0;i<list.length;i++) {
            result += list[i];
        }
        return result;
    }
}

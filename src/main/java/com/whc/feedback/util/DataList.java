package com.whc.feedback.util;

import java.util.ArrayList;
import java.util.List;

public class DataList {

    /**
     * 问题分类
     */
    public static List getIssueCategoryList() {
        List issueCategoryList = new ArrayList();

        return issueCategoryList;
    }
    
    /**
     * 问题二级分类
     */
    public static List getIssueSecondaryCategoryList() {
        List issueSecondaryCategoryList = new ArrayList();

        return issueSecondaryCategoryList;
    }
   
    /**
     * 系统状态
     */
    public static List getPhoneStatusList() {
        List phoneStatusList = new ArrayList();
        phoneStatusList.add("系统正常运行");
        phoneStatusList.add("系统中断后功能恢复:长时间不用休眠唤醒后");
        phoneStatusList.add("系统中断后功能恢复:突然来电话接听后");
        phoneStatusList.add("系统中断后功能恢复:突然来短信");
        phoneStatusList.add("系统中断后功能恢复:进入锁屏后解锁");
        phoneStatusList.add("系统中断后功能恢复:切换到后台再回来");
        phoneStatusList.add("系统中断后功能恢复:按power按键挂起系统后");
        phoneStatusList.add("系统中断后功能恢复:使用过程中强行关机");
        return phoneStatusList;
    }

    /**
     * 声音播放类型
     */
    public static List getPhoneSoundCategoryList() {
        List phoneSoundCategoryList = new ArrayList();
        phoneSoundCategoryList.add("静音");
        phoneSoundCategoryList.add("喇叭");
        phoneSoundCategoryList.add("耳机");
        phoneSoundCategoryList.add("蓝牙耳机");
        return phoneSoundCategoryList;
    }
   
    /**
     * 声音播放状态
     */
    public static List getPhoneSoundStatusList() {
        List phoneSoundStatusList = new ArrayList();
        phoneSoundStatusList.add("插入耳机(播放声音前)");
        phoneSoundStatusList.add("插入耳机(播放声音中)");
        phoneSoundStatusList.add("使用蓝牙耳机(播放声音中)");
        phoneSoundStatusList.add("使用蓝牙耳机(播放声音中)");
        phoneSoundStatusList.add("拔出耳机(播放声音时)");
        phoneSoundStatusList.add("断开蓝牙耳机(播放声音时)");
        phoneSoundStatusList.add("开启静音(播放声音前)");
        phoneSoundStatusList.add("开启静音(播放声音中)");
        phoneSoundStatusList.add("取消静音(播放声音前)");
        phoneSoundStatusList.add("取消静音(播放声音中)");
        phoneSoundStatusList.add("增加音量(播放声音前)");
        phoneSoundStatusList.add("增加音量(播放声音中)");
        phoneSoundStatusList.add("减少音量(播放声音前)");
        phoneSoundStatusList.add("减少音量(播放声音中)");
        return phoneSoundStatusList;
    }
   
    /**
     * 麦克风状态
     */
    public static List getMicrophoneStatusList() {    
        List microphoneStatusList = new ArrayList();
        microphoneStatusList.add("各类麦克风功能正常");
        microphoneStatusList.add("各类麦克风间切换(麦克风使用过程中)");
        microphoneStatusList.add("无麦克风访问权限");
        return microphoneStatusList;
    }
   
    /**
     * 麦克风类型
     */
    public static List getMicrophoneCategoryList() {
        List microphoneCategoryList = new ArrayList();
        microphoneCategoryList.add("本机麦克风");
        microphoneCategoryList.add("耳机麦克风");
        microphoneCategoryList.add("蓝牙耳机麦克风");
        return microphoneCategoryList;
    }
   
    /**
     * 手机网络状态
     */
    public static List getPhoneNetworkStatusList() {
        List phoneNetworkStatusList = new ArrayList();
        phoneNetworkStatusList.add("无网络");
        phoneNetworkStatusList.add("有网络");
        phoneNetworkStatusList.add("与WIFI网络切换");
        phoneNetworkStatusList.add("与移动网络切换");
        phoneNetworkStatusList.add("WIFI路由切换");
        phoneNetworkStatusList.add("网络突然断开");
        phoneNetworkStatusList.add("弱信号网络不稳定(时断时续)");
        phoneNetworkStatusList.add("APP无访问网络权限");
        return phoneNetworkStatusList;
    }
   
    /**
     * 手机网络分类
     */
    public static List getPhoneNetworkCategoryList() {
        List phoneNetworkCategoryList = new ArrayList();
        phoneNetworkCategoryList.add("WIFI 5G");
        phoneNetworkCategoryList.add("WIFI 2.4G");
        phoneNetworkCategoryList.add("WIFI 热点分享");
        phoneNetworkCategoryList.add("4G TD-LTE(移动)");
        phoneNetworkCategoryList.add("4G FDD-LTE(联通、电信)");
        phoneNetworkCategoryList.add("4G FDD-LTE(联通)");
        phoneNetworkCategoryList.add("3G TD-SCDMA(移动)");
        phoneNetworkCategoryList.add("3G WCDMA(联通)");
        phoneNetworkCategoryList.add("3G CDMA2000(电信)");
        phoneNetworkCategoryList.add("2G GSM(移动/联通)");
        phoneNetworkCategoryList.add("2G CDMA(电信)");
        return phoneNetworkCategoryList;
    }
  
    /**
     * 摄像头状态
     */
    public static List getPhoneCameraStatusList() {
        List phoneCameraStatusList = new ArrayList();
        phoneCameraStatusList.add("前置摄像头");
        phoneCameraStatusList.add("后置摄像头");
        phoneCameraStatusList.add("APP无访问权限");
        return phoneCameraStatusList;
    }
   
    /**
     * 手机存储空间状态
     */
    public static List getPhoneStorageStatusList() {
        List phoneStorageStatusList = new ArrayList();
        phoneStorageStatusList.add("空间不足");
        phoneStorageStatusList.add("空间充足");
        phoneStorageStatusList.add("APP无访问权限");
        return phoneStorageStatusList;
    }
   
    /**
     * 手机电量状态
     */
    public static List getPhoneElectricityStatusList() {
        List phoneElectricityStatusList = new ArrayList();
        phoneElectricityStatusList.add("正常电量");
        phoneElectricityStatusList.add("低电量");
        phoneElectricityStatusList.add("电量不足自动关机");
        phoneElectricityStatusList.add("充电时");
        return phoneElectricityStatusList;
    }
   
    /**
     * 手机屏幕分辨率
     */
    public static List getPhoneScreenResolutionList() {
        List phoneScreenResolutionList = new ArrayList();
        phoneScreenResolutionList.add("720p（1280×720）");
        phoneScreenResolutionList.add("1080p（1920×1080）");
        phoneScreenResolutionList.add("2k（2560×1440）");
        return phoneScreenResolutionList;
    }
    
    /**
     * 手机屏幕方向锁定
     */
    public static List getPhoneScreenOrientationLockList() {
        List phoneScreenOrientationLockList = new ArrayList();
        phoneScreenOrientationLockList.add("横竖屏锁定未开启");
        phoneScreenOrientationLockList.add("横屏锁定开启");
        phoneScreenOrientationLockList.add("竖屏锁定开启");
        return phoneScreenOrientationLockList;
    }
    
    /**
     * 手机屏幕关闭开启状态
     */
    public static List getPhoneScreenOpenStatusList() {
        List phoneScreenOpenStatusList = new ArrayList();
        phoneScreenOpenStatusList.add("轻按Power键关闭屏幕");
        phoneScreenOpenStatusList.add("自动进入休眠后再开启");
        return phoneScreenOpenStatusList;
    }
    
    /**
     * 手势动作
     */
    public static List getPhoneGestureActionList() {
        List phoneGestureActionList = new ArrayList();
        phoneGestureActionList.add("点击");
        phoneGestureActionList.add("滑动");
        phoneGestureActionList.add("放大");
        phoneGestureActionList.add("缩小");
        return phoneGestureActionList;
    }


}

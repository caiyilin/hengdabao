package com.movitech.mbox.modules.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.spreada.utils.chinese.ZHConverter;

public class PropertiesUtil {
    private static String i18n = null;
    // 国际化文件资料
    private static Properties p = null;
    // 配置文件资料
    private static Properties pro = null;
    
    /**
     * 加载文件
     * @throws IOException
     */
    private static void loadProperties() throws IOException {
        String rootPath = PropertiesUtil.class.getResource("/").getPath();
        PropertiesUtil.setLang();
        if("messages_zh_TW".equals(i18n)) {
            InputStream in = new BufferedInputStream(new FileInputStream(rootPath + "lang_zh_TW.properties"));
            p = new Properties();
            p.load(in);
        } else {
            InputStream in = new BufferedInputStream(new FileInputStream(rootPath + "lang_zh_CN.properties"));
            p = new Properties();
            p.load(in);
        }
    }
    
    /**
     * 设置国际化语言
     * @throws IOException
     */
    private static void setLang() throws IOException {
        if(i18n == null) {
            String rootPath = PropertiesUtil.class.getResource("/").getPath();
            InputStream in = new BufferedInputStream(new FileInputStream(rootPath + "db.properties"));
            pro = new Properties();
            pro.load(in);
            // 国际化标记
            i18n = pro.getProperty("i18n");
        }
    }
    
    /**
     * 获取配置信息
     * @param key
     * @return
     */
    public static String getValue(String key) {
        if(pro == null) {
            try {
                PropertiesUtil.setLang();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pro.getProperty(key);
    }
    
    /**
     * 获取国际化文字
     * @param key
     * @return
     */
    public static String getShowGJH(String key) {
        if(p == null) {
            try {
                PropertiesUtil.loadProperties();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return p.getProperty(key);
    }
    
    /**
     * 根据配置决定显示简体或繁体
     * @param str 输入字符串
     * @return
     */
    public static String showGJH(String str) {
        try {
            PropertiesUtil.setLang();
            if("messages_zh_TW".equals(i18n)) {
                return PropertiesUtil.zhcn2zhtw(str);
            } else {
                return str;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return str;
        }
    }
    
    /**
     * 简体转繁体
     * @param str 简体
     * @return
     */
    private static String zhcn2zhtw(String str) {
        ZHConverter converter = ZHConverter.getInstance(ZHConverter.TRADITIONAL);
        String traditionalStr = converter.convert(str);
        return traditionalStr;
    }
    
//    /**
//     * 繁体转简体
//     * @param str
//     * @return
//     */
//    public static String zhtw2zhcn(String str) {
//        ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
//        String simplifiedStr = converter.convert(str);
//        return simplifiedStr;
//    }
}

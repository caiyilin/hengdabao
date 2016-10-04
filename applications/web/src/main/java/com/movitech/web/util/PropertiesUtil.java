package com.movitech.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 全局配置类
 * 
 * @author ThinkGem
 * @version 2014-06-25
 */
public class PropertiesUtil {
    // 配置文件路径
    @SuppressWarnings("unused")
	private static String proPath = "E://workspace//nuFamily//applications//web//src//main//resources//db.properties";
    private static Properties pro;
    // 初始化
    private static void load() throws IOException {
        pro = new Properties();
        // 绝对路径获取
//        FileInputStream fis = new FileInputStream(proPath);
        // 相对路径获取
        InputStream fis = PropertiesUtil.class.getClassLoader().getResourceAsStream("db.properties");
        pro.load(fis);
    }
    
    /**
     * 获取对应的值
     * @param key
     * @return
     */
    public static String getValue(String key) {
        if(pro == null) {
            try {
                PropertiesUtil.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String property = pro.getProperty(key);
        return property;
    }
}

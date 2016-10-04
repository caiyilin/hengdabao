package com.movitech.mbox.modules.cms;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/** 
 * @author  kyle.wu@movit-tech.com 
 * @date 创建时间：2015年6月8日 上午10:20:46 
 * @version 1.0 
 */
public class Constants {
    static{
        loadProperties(true);
        file_show_url = getProperty("file_show_url");
        file_upload_url = getProperty("file_upload_url");
    }
    /**
     * 文件上传地址
     */
    public static String file_upload_url;
    /**
     * 文件查看地址
     */
    public static String file_show_url;
    
    private static Properties properties;

    public static void loadProperties(boolean force) {
        try {
            if (force || properties == null) {
                properties = new Properties();
                InputStream fis = Constants.class.getClassLoader()
                        .getResourceAsStream("db.properties");
                properties.load(fis);
            }
        } catch (FileNotFoundException fnfe) {
        } catch (IOException ioe) {
        } catch (Exception e) {
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}

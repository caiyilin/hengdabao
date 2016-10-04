/**
 * 
 */
package com.movitech.mbox.common.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * FreeMarkers工具类
 * @author ThinkGem
 * @version 2013-01-15
 */
public class FreeMarkers {

    public static String renderString(String templateString, Map<String, ?> model) {
        try {
            StringWriter result = new StringWriter();
            Template t = new Template("name", new StringReader(templateString), new Configuration());
            t.process(model, result);
            return result.toString();
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    public static String renderTemplate(Template template, Object model) {
        try {
            StringWriter result = new StringWriter();
            template.process(model, result);
            return result.toString();
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    public static Configuration buildConfiguration(String directory) throws IOException {
        Configuration cfg = new Configuration();
        Resource path = new DefaultResourceLoader().getResource(directory);
        cfg.setDirectoryForTemplateLoading(path.getFile());
        return cfg;
    }
    
    /**
     * 获取静态页面
     * @param model
     * @param templateName
     * @return
     *
     * @author Aibal.He   2015年12月17日
     */
    public static String getHtmlByTemplate(Map<String, String> model,String templateName){
        Configuration cfg;
        Template template;
        try {
            cfg = buildConfiguration("/freemarker");
            template = cfg.getTemplate(templateName+".html");
            return renderTemplate(template, model);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args) throws IOException {
        // renderString
        /*Map<String, String> model = com.google.common.collect.Maps.newHashMap();
        model.put("userName", "calvin");
        String result = FreeMarkers.renderString("hello ${userName}", model);
        System.out.println(result);
        // renderTemplate
        Configuration cfg = FreeMarkers.buildConfiguration("/freemarker");
        Template template = cfg.getTemplate("test.html");
        String result2 = FreeMarkers.renderTemplate(template, model);
        System.out.println(result2);*/
        
        /*Map<String, String> model = com.google.common.collect.Maps.newHashMap();
        model.put("userName", "calvin");
        String result = FreeMarkers.renderString("hello ${userName} ${r'${userName}'}", model);
        System.out.println(result);*/
        Map<String, String> model = new HashMap<String, String>();
        model.put("userName", "calvin");
        System.out.println(getHtmlByTemplate(model,"test"));
    }
    
}

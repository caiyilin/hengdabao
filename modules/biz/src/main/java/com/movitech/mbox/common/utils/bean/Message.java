package com.movitech.mbox.common.utils.bean;

public class Message {
    // 消息封面，为缩略图，不能 大于64K
    private String thumbUrl;
    // 消息内容
    private String content;
    // 原文URL
    private String contentURL;
    // 图文消息的标题
    private String title;
    // 文章的id
    private String docId;
    //================================================
    //===========上面属性必须设值，下面不需要===========
    //================================================
    // 图文消息的id
    private String thumbMediaId;
    
    public String getThumbUrl() {
        return thumbUrl;
    }
    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getContentURL() {
        return contentURL;
    }
    public void setContentURL(String contentURL) {
        this.contentURL = contentURL;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getThumbMediaId() {
        return thumbMediaId;
    }
    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }
    public String getDocId() {
        return docId;
    }
    public void setDocId(String docId) {
        this.docId = docId;
    }
}

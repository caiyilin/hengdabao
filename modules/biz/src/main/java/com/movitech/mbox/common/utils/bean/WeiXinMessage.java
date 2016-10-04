package com.movitech.mbox.common.utils.bean;

import java.util.List;

public class WeiXinMessage {
    // 群发接收者
    private List<String> users;
    // 测试接收者
    private String preUser;
    // 发送信息，数量不能超过8
    private List<Message> messages;
    
    public List<String> getUsers() {
        return users;
    }
    public void setUsers(List<String> users) {
        this.users = users;
    }
    public String getPreUser() {
        return preUser;
    }
    public void setPreUser(String preUser) {
        this.preUser = preUser;
    }
    public List<Message> getMessages() throws Exception {
        if(messages.size() > 7) {
            throw new Exception("wrong num of the message size, must less then 8!");
        }
        return messages;
    }
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}

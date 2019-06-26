package app.mrquan.wechatclient.pojo;

import java.util.Date;

public class Message {
    private String user;//用户
    private String sendObject;//发送对象
    private String news;//消息
    private Date date;
    private String function;//功能

    public Message() {
    }

    public Message(String user, String sendObject, String news, Date date, String function) {
        this.user = user;
        this.sendObject = sendObject;
        this.news = news;
        this.date = date;
        this.function = function;
    }

    @Override
    public String toString() {
        return "Message{" +
                "user='" + user + '\'' +
                ", sendObject='" + sendObject + '\'' +
                ", news='" + news + '\'' +
                ", date=" + date +
                ", function='" + function + '\'' +
                '}';
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSendObject() {
        return sendObject;
    }

    public void setSendObject(String sendObject) {
        this.sendObject = sendObject;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}

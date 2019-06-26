package app.mrquan.wechatclient.databases.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 消息表
 */
@Entity
public class Message {
    @Id
    private Long id;
    private String contacts;
    private Boolean mine;//是否为自己 true为自己 false为对方
    private String news;
    private Date date;
    @Generated(hash = 1639472544)
    public Message(Long id, String contacts, Boolean mine, String news, Date date) {
        this.id = id;
        this.contacts = contacts;
        this.mine = mine;
        this.news = news;
        this.date = date;
    }
    @Generated(hash = 637306882)
    public Message() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getContacts() {
        return this.contacts;
    }
    public void setContacts(String contacts) {
        this.contacts = contacts;
    }
    public Boolean getMine() {
        return this.mine;
    }
    public void setMine(Boolean mine) {
        this.mine = mine;
    }
    public String getNews() {
        return this.news;
    }
    public void setNews(String news) {
        this.news = news;
    }
    public Date getDate() {
        return this.date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", contacts='" + contacts + '\'' +
                ", mine=" + mine +
                ", news='" + news + '\'' +
                ", date=" + date +
                '}';
    }
}
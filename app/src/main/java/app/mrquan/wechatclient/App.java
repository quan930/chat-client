package app.mrquan.wechatclient;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.se.omapi.Session;

import org.greenrobot.greendao.database.Database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.mrquan.wechatclient.databases.dao.IMessageDao;
import app.mrquan.wechatclient.databases.dao.impl.MessageDaoImpl;
import app.mrquan.wechatclient.databases.table.DaoMaster;
import app.mrquan.wechatclient.databases.table.DaoSession;
import app.mrquan.wechatclient.databases.table.Message;
import app.mrquan.wechatclient.util.MyActivityManager;

public class App extends Application {
    private DaoSession daoSession;
    private IMessageDao messageDao;
    private List<List<Message>> allMessage;//全部消息
    private List<String> users;//用户列表
    private String userId;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "message-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        messageDao = new MessageDaoImpl();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                MyActivityManager.getInstance().setCurrentActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    /**
     * 从数据库 加载全部消息
     * @return 全部消息
     */
    public List<List<Message>> loadAllMessage(){
        allMessage = messageDao.loadAllMessage(daoSession);
        return allMessage;
    }


    /**
     * 添加消息
     * @param message pojo消息对象
     * @return table 消息对象
     */
    public Message addMessage(app.mrquan.wechatclient.pojo.Message message){
        Message tableMessage;
        if (!message.getSendObject().equals(userId)){
            tableMessage = new Message(null,message.getSendObject(),true,message.getNews(),new Date());
        }else {
            tableMessage = new Message(null,message.getUser(),false,message.getNews(),message.getDate());
        }
        //加入数据 数据库
        daoSession.getMessageDao().insert(tableMessage);
        //加入缓存
        boolean f = false;
        for (List<Message> messages:allMessage) {
            //判断联系人是否一致
            if (messages.get(0).getContacts().equals(tableMessage.getContacts())){
                messages.add(tableMessage);
                f = true;
                break;
            }
        }
        if (!f){
            //创建队列
            allMessage.add(new ArrayList<Message>());
            //加入消息
            allMessage.get(allMessage.size()-1).add(tableMessage);
        }
        return tableMessage;
    }

    /**
     * 返回用户消息列表
     * @param userId 用户id
     * @return 消息列表
     */
    public List<Message> getMessagesByContacts(String userId){
        for (List<Message> messages : allMessage) {
            if (messages.get(0).getContacts().equals(userId))
                return messages;
            continue;

        }
        return null;
    }

    public void setSocketReaderWriter(Socket socket,BufferedReader reader,BufferedWriter writer) {
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    /**
     * 判断socket 是否连接
     * @return
     */
    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<List<Message>> getAllMessage() {
        return allMessage;
    }
}

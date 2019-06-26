package app.mrquan.wechatclient.databases.dao.impl;

import java.util.ArrayList;
import java.util.List;

import app.mrquan.wechatclient.databases.dao.IMessageDao;
import app.mrquan.wechatclient.databases.table.DaoSession;
import app.mrquan.wechatclient.databases.table.Message;

public class MessageDaoImpl implements IMessageDao {

    @Override
    public List<List<Message>> loadAllMessage(DaoSession daoSession) {
        List<List<Message>> allMessage = new ArrayList<>();
        List<Message> list = daoSession.getMessageDao().loadAll();
        for (Message m:list) {
            //发送成功标志
            boolean f = false;
            for (List<Message> messages:allMessage) {
                //判断联系人是否一致
                if (messages.get(0).getContacts().equals(m.getContacts())){
                    messages.add(m);
                    f = true;
                    break;
                }
            }
            if (!f){
                //创建队列
                allMessage.add(new ArrayList<Message>());
                //加入消息
                allMessage.get(allMessage.size()-1).add(m);
            }
        }
        return allMessage;
    }
}
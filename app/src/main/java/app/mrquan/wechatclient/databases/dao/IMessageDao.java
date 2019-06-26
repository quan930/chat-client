package app.mrquan.wechatclient.databases.dao;

import java.util.List;

import app.mrquan.wechatclient.databases.table.DaoSession;
import app.mrquan.wechatclient.databases.table.Message;

public interface IMessageDao {
    /**
     * 加载全部消息
     * @param daoSession session
     * @return 全部消息
     */
    List<List<Message>> loadAllMessage(DaoSession daoSession);
}

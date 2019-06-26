package app.mrquan.wechatclient.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import app.mrquan.wechatclient.App;
import app.mrquan.wechatclient.MainActivity;
import app.mrquan.wechatclient.pojo.Message;
import app.mrquan.wechatclient.ui.activity.ChatActivity;
import app.mrquan.wechatclient.util.MyActivityManager;
import app.mrquan.wechatclient.util.MyUtil;

public class MessageService extends Service {
    private final String TAG="服务";
    private App app;
    public MessageService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = (App)getApplication();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /**
         * 启动消息读取流
         */
        Log.d(TAG, "onStartCommand: ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "线程启动");
                while (true){
                    if (app.getReader()==null)
                        break;
                    if (app.getSocket().isInputShutdown())
                        break;
                    try {
                        String json = app.getReader().readLine();
                        Log.d(TAG, "aaaaa"+json);
                        if (json==null)
                            continue;
                        //解析消息
                        Message message = new Gson().fromJson(json,Message.class);
                        //添加消息 进入数据库 缓存
                        Log.d(TAG, "异常"+message);
                        app.mrquan.wechatclient.databases.table.Message tableMessage = app.addMessage(message);
                        Log.d(TAG, "数据库"+tableMessage);
                        final Activity activity = MyActivityManager.getInstance().getCurrentActivity();
                        Log.d(TAG, "活动状态"+MyUtil.isForeground(activity));

                        if (MyUtil.isForeground(activity)){
                            //前台消息
                            if (activity instanceof MainActivity){
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((MainActivity)activity).getMessageFragment().getAdapter().notifyDataSetChanged();
                                    }
                                });
                                Log.d(TAG, ""+"前台消息");
                            }else if (activity instanceof ChatActivity){
                                if (((ChatActivity)activity).getContacts().equals(tableMessage.getContacts())){
                                    //是当前联系人
                                    ((ChatActivity)activity).dataChange(tableMessage);
                                }else {
                                    //后台 推送消息
                                    MyUtil.sendNotification(getBaseContext(),tableMessage);
                                    Log.d(TAG, ""+"后台消息");
                                }
                            }else {
                                //后台 推送消息
                                MyUtil.sendNotification(getBaseContext(),tableMessage);
                                Log.d(TAG, ""+"后台消息");
                            }
                        }else {
                            //后台 推送消息
                            MyUtil.sendNotification(getBaseContext(),tableMessage);
                            Log.d(TAG, ""+"后台消息");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

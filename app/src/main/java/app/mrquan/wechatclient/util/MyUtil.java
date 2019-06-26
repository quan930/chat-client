package app.mrquan.wechatclient.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import app.mrquan.wechatclient.R;
import app.mrquan.wechatclient.pojo.Message;
import app.mrquan.wechatclient.ui.activity.ChatActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyUtil {
    /**
     * 获得栈中最顶层的Activity
     * @param context
     * @return
     */
    public static String getTopActivity(Context context) {
        android.app.ActivityManager manager = (android.app.ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            return (runningTaskInfos.get(0).topActivity).getClassName();
        } else
            return null;
    }

    /**
     * 发送通知
     * @param context content
     * @param message 消息
     */
    public static void sendNotification(Context context, app.mrquan.wechatclient.databases.table.Message message){
        Intent intent = new Intent(context,ChatActivity.class);
        long timeMillis = System.currentTimeMillis();
        //传递消息 联系人
        intent.putExtra("contacts",message.getContacts());
//        Log.d("数据传递", "test: "+intent.getLongExtra("time",-1));
        //第二个参数设为消息类型notificationId 否则activity数据无法传递
        PendingIntent pendingIntent = PendingIntent.getActivity(context,(int) timeMillis,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            Notification notification = new NotificationCompat.Builder(context, "chat")
                    //聊天联系人
                    .setContentTitle(message.getContacts())
                    //聊天内容
                    .setContentText(message.getNews())
                    //消息时间
                    .setWhen(message.getDate().getTime())
                    .setSmallIcon(R.drawable.ttt)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.fff))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .build();
            manager.notify((int) timeMillis, notification);
        }else {
            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            Notification notification = new NotificationCompat.Builder(context.getApplicationContext())
                    .setContentTitle(message.getContacts())
                    .setContentText(message.getNews())
                    .setWhen(message.getDate().getTime())
                    .setSmallIcon(R.drawable.ttt)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.fff))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .build();
            manager.notify((int) timeMillis,notification);
        }
    }


    /** 判断程序是否在后台运行 */
    public static boolean isRunBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    // 表明程序在后台运行
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param activity 要判断的Activity
     * @return 是否在前台显示
     */
    public static boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }
    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }
}

package app.mrquan.wechatclient.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import app.mrquan.wechatclient.App;
import app.mrquan.wechatclient.MainActivity;
import app.mrquan.wechatclient.pojo.User;
import app.mrquan.wechatclient.util.SPUtils;

public class SplashActivity extends AppCompatActivity {
    private final String TAG = "引导界面";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 1.判断是否有用户/密码存储
                 * 2.没有进入登陆界面
                 * 2.有则 发送tcp连接验证是否登陆
                 * 3.没有登陆进入登陆页面
                 */
                if (!SPUtils.contains(getBaseContext(), "userId") || !SPUtils.contains(getBaseContext(), "userPassword")){
                    //没有存储数据
                    Log.d(TAG, "没有储存账户密码，跳转至登陆界面");
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    //存在数据
                    Log.d(TAG, "储存账户密码，进行判断");
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getBaseContext(),"验证密码",Toast.LENGTH_SHORT).show();
                            }
                        });
                        Socket socket = new Socket("47.94.13.255",7003);
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        User user = new User((String) SPUtils.get(getBaseContext(),"userId","null"),(String) SPUtils.get(getBaseContext(),"userPassword","null"));
                        writer.write(new Gson().toJson(user));
                        writer.newLine();
                        writer.flush();
                        String json = reader.readLine();
                        //判断是否已经登陆
                        if (json.contains("success")){
                            /*
                             * 成功
                             * 加载数据
                             * 登陆主页面
                             */
                            Log.d(TAG, "已经登陆，跳转至主页面");
                            //加载消息
                            ((App) getApplication()).loadAllMessage();
                            //设置当前用户
                            ((App) getApplication()).setUserId(user.getId());
                            //用户列表
                            ((App) getApplication()).setUsers(new ArrayList<String>(Arrays.asList(new Gson().fromJson(reader.readLine(), String[].class))));
                            //聊天socket
                            ((App) getApplication()).setSocketReaderWriter(socket,reader,writer);
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            //资源释放
                            reader.close();
                            writer.close();
                            socket.close();
                            Log.d(TAG, "没有登陆，跳转至登陆界面");
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}

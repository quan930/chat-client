package app.mrquan.wechatclient.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import app.mrquan.wechatclient.App;
import app.mrquan.wechatclient.MainActivity;
import app.mrquan.wechatclient.R;
import app.mrquan.wechatclient.pojo.User;
import app.mrquan.wechatclient.util.SPUtils;

public class LoginActivity extends AppCompatActivity {
    private EditText user;
    private EditText password;
    private final String TAG="登陆界面";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = findViewById(R.id.edit_userId);
        password = findViewById(R.id.edit_userPass);
    }

    /**
     * 登陆操作
     * socket 发送请求
     * 成功跳转main页面 存储账户密码
     * @param view
     */
    public void login(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String userId = user.getText().toString()+"";
                String userPass = password.getText().toString()+"";
                try {
                    Socket socket = new Socket("47.94.13.255",7002);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //提交账户密码
                    writer.write(new Gson().toJson(new User(userId,userPass)));
                    writer.newLine();
                    writer.flush();
                    String json = reader.readLine();
                    Log.d(TAG, ""+json);
                    //判断是否已经登陆
                    if (json.contains("success")){
                        /*
                         * 成功
                         * 加载数据
                         * 登陆主页面
                         */
                        reader.close();
                        writer.close();
                        socket.close();
                        Log.d(TAG, "登陆成功，启动聊天socket");
                        //启动聊天socket
                        Socket socket1 = new Socket("47.94.13.255",7003);
                        BufferedWriter writer1 = new BufferedWriter(new OutputStreamWriter(socket1.getOutputStream()));
                        BufferedReader reader1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
                        writer1.write(new Gson().toJson(new User(userId,userPass)));
                        writer1.newLine();
                        writer1.flush();
                        reader1.readLine();
                        //加载全部消息
                        ((App) getApplication()).loadAllMessage();
                        //设置当前用户
                        ((App) getApplication()).setUserId(userId);
                        //加载用户列表
                        ((App) getApplication()).setUsers(new ArrayList<>(Arrays.asList(new Gson().fromJson(reader1.readLine(), String[].class))));
                        //聊天socket
                        ((App) getApplication()).setSocketReaderWriter(socket1,reader1,writer1);
                        //数据持久化
                        SPUtils.put(getBaseContext(),"userId",userId);
                        SPUtils.put(getBaseContext(),"userPassword",userPass);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        //资源释放
                        reader.close();
                        writer.close();
                        socket.close();
                        Log.d(TAG, "登陆失败");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getBaseContext(),"登陆失败",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

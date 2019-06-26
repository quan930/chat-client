package app.mrquan.wechatclient.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import app.mrquan.wechatclient.App;
import app.mrquan.wechatclient.R;
import app.mrquan.wechatclient.pojo.Message;
import app.mrquan.wechatclient.ui.activity.LoginActivity;
import app.mrquan.wechatclient.ui.activity.SplashActivity;
import app.mrquan.wechatclient.util.MyUtil;
import app.mrquan.wechatclient.util.SPUtils;

/**
 * A simple {@link Fragment} subclass.
 * 我的界面
 */
public class MineFragment extends Fragment {


    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_mine, container, false);
        Button button = view.findViewById(R.id.button_offline);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         * 下线
                         */
                        App app = (App)getActivity().getApplication();
                        try {
                            //发送下线指令
                            app.getWriter().write(new Gson().toJson(new Message(app.getUserId(),null,null,null,"exit")));
                            app.getWriter().newLine();
                            app.getWriter().flush();
                            //释放资源
                            app.getReader().close();
                            app.getWriter().close();
                            app.getSocket().close();
                            app.setSocketReaderWriter(null,null,null);
                            //数据持久化 用户 密码更改
                            SPUtils.remove(getContext(),"userId");
                            SPUtils.remove(getContext(),"userPassword");
                            //跳转至登陆界面
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        Button button1 = view.findViewById(R.id.button_test);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),MyUtil.getTopActivity(getContext())+"",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}

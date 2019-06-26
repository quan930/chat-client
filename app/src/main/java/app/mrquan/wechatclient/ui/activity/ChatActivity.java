package app.mrquan.wechatclient.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import app.mrquan.wechatclient.App;
import app.mrquan.wechatclient.MainActivity;
import app.mrquan.wechatclient.R;
import app.mrquan.wechatclient.databases.table.Message;
import app.mrquan.wechatclient.ui.adapter.ChatAdapter;

public class ChatActivity extends AppCompatActivity {
    private String contacts;//联系人
    private List<Message> messages;
    private RecyclerView recyclerView;
    private TextView textView;
    private EditText editText;
    private ChatAdapter chatAdapter;

    public String getContacts() {
        return contacts;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView = findViewById(R.id.recycler_chat);
        textView = findViewById(R.id.textView_contacts);
        editText = findViewById(R.id.news_input);
        //获取当前联系人
        contacts = getIntent().getStringExtra("contacts");
        if (contacts==null){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        textView.setText(contacts);
        messages = ((App)getApplication()).getMessagesByContacts(contacts);
        if (messages==null){
            /*
             * 没有当前联系人消息
             */
        }else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            chatAdapter = new ChatAdapter(messages);
            recyclerView.setAdapter(chatAdapter);
            recyclerView.scrollToPosition(messages.size()-1);
        }
    }

    public void sendNews(View view) {
//        {"function":"chat","user":"0003","sendObject":"0001","news":"good good"}
        String news = editText.getText().toString();
        final app.mrquan.wechatclient.pojo.Message message = new app.mrquan.wechatclient.pojo.Message(
                ((App)getApplication()).getUserId(),contacts,news,null,"chat");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ((App)getApplication()).getWriter().write(new Gson().toJson(message));
                    ((App)getApplication()).getWriter().newLine();
                    ((App)getApplication()).getWriter().flush();
                    Log.d("socket推送", "run: "+message);
                    Message message1 = ((App)getApplication()).addMessage(message);
                    Log.d("数据库", ""+message1);
                    editText.setText("");
                    //数据更改
                    dataChange(message1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    public void dataChange(final Message message){
        if (chatAdapter==null){
            messages = ((App)getApplication()).getMessagesByContacts(contacts);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    chatAdapter = new ChatAdapter(messages);
                    recyclerView.setAdapter(chatAdapter);
                    chatAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(messages.size()-1);
                }
            });
        }else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chatAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(messages.size()-1);
                }
            });
        }
    }
}

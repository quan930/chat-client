package app.mrquan.wechatclient.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.List;

import app.mrquan.wechatclient.R;
import app.mrquan.wechatclient.databases.table.Message;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyHolder>{
    private List<Message> list;
    public ChatAdapter(List<Message> strings){
        list = strings;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {//创建视图
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {//子布局监听
        Message message = list.get(position);
        if (message.getMine()){
            holder.right.setVisibility(View.VISIBLE);
            holder.left.setVisibility(View.GONE);
            holder.rightText.setText(message.getNews());
        }else {
            holder.left.setVisibility(View.VISIBLE);//可见
            holder.right.setVisibility(View.GONE);//隐藏
            holder.leftText.setText(message.getNews());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 初始化控件 MyHolder
     */
    class MyHolder extends RecyclerView.ViewHolder{
        LinearLayout left;
        RelativeLayout right;
        TextView leftText;
        TextView rightText;
        public MyHolder(View itemView) {
            //子项外层布局
            super(itemView);
            left = itemView.findViewById(R.id.leftleft);
            right = itemView.findViewById(R.id.rightright);
            leftText = itemView.findViewById(R.id.leftView);
            rightText = itemView.findViewById(R.id.rightView);
        }
    }
}

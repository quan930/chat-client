package app.mrquan.wechatclient.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import app.mrquan.wechatclient.R;
import app.mrquan.wechatclient.databases.table.Message;

public class MessageAdapter extends ArrayAdapter<List<Message>> {
    public MessageAdapter(Context context, int resource,List<List<Message>> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.message_item,parent,false);
        TextView textViewId = view.findViewById(R.id.message_user_id);
        TextView textViewNews = view.findViewById(R.id.message_news);
        textViewId.setText(getItem(position).get(getItem(position).size()-1).getContacts());
        textViewNews.setText(getItem(position).get(getItem(position).size()-1).getNews());
        return view;
    }
}

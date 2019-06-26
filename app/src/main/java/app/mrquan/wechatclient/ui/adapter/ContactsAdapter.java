package app.mrquan.wechatclient.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import app.mrquan.wechatclient.R;

public class ContactsAdapter extends ArrayAdapter<String> {
    public ContactsAdapter(Context context, int resource,  List<String> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.contacts_item,parent,false);
        TextView textView = view.findViewById(R.id.user_id);
        textView.setText(getItem(position));
        return view;
    }
}

package app.mrquan.wechatclient.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import app.mrquan.wechatclient.App;
import app.mrquan.wechatclient.R;
import app.mrquan.wechatclient.ui.activity.ChatActivity;
import app.mrquan.wechatclient.ui.adapter.MessageAdapter;

/**
 * A simple {@link Fragment} subclass.
 * 消息界面
 */
public class MessageFragment extends Fragment {
    private MessageAdapter adapter;


    public MessageFragment() {
        // Required empty public constructor
    }


    public MessageAdapter getAdapter() {
        return adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ListView listView = view.findViewById(R.id.listView_message);
        adapter = new MessageAdapter(getContext(),R.layout.message_item,((App)getActivity().getApplication()).getAllMessage());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String contacts= ((App)getActivity().getApplication()).getAllMessage().get(position).get(0).getContacts();
                Intent intent = new Intent(getActivity(),ChatActivity.class);
                Log.d("联系人", "onItemClick: "+contacts);
                intent.putExtra("contacts",contacts);
                startActivity(intent);
            }
        });
        return view;
    }

}

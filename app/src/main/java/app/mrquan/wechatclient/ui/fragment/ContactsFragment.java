package app.mrquan.wechatclient.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import app.mrquan.wechatclient.App;
import app.mrquan.wechatclient.R;
import app.mrquan.wechatclient.ui.activity.ChatActivity;
import app.mrquan.wechatclient.ui.adapter.ContactsAdapter;

/**
 * A simple {@link Fragment} subclass.
 * 联系人列表
 */
public class ContactsFragment extends Fragment {


    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_contacts, container, false);
        ListView listView = view.findViewById(R.id.contacts_listView);
        listView.setAdapter(new ContactsAdapter(getContext(),R.layout.contacts_item,((App)getActivity().getApplication()).getUsers()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //联系人
                String contacts= ((App)getActivity().getApplication()).getUsers().get(position);
                Intent intent = new Intent(getActivity(),ChatActivity.class);
                intent.putExtra("contacts",contacts);
                startActivity(intent);
            }
        });
        return view;
    }
}

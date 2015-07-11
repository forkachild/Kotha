package com.suhel.kotha;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseUser;

import java.util.List;

public class ChatListFragment extends Fragment {

    private ListView lstChatList;
    private ChatListAdapter chatListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        lstChatList = (ListView) view.findViewById(R.id.lstChatList);
        lstChatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((BaseCallback) getActivity()).loadChatFragment(((ChatListItem) lstChatList.getItemAtPosition(position)).text);
            }

        });
        chatListAdapter = new ChatListAdapter(getActivity());

        ((BaseCallback) getActivity()).loadChatListItems();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem menuLogOut = menu.add("Log out");
        menuLogOut.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ((BaseCallback) getActivity()).logout();
                return true;
            }

        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void setChatListItems(List<ChatListItem> users) {

        chatListAdapter.clear();

        chatListAdapter.addMultiple(users);

        lstChatList.setAdapter(chatListAdapter);
    }

    public void addItem(ChatListItem item) {
        chatListAdapter.add(item);
        lstChatList.setAdapter(chatListAdapter);
    }

    public void updateItem(String firstName, String message) {
        for(int i = 0; i < chatListAdapter.getCount();i++) {
            if(chatListAdapter.getItem(i).text.equals(firstName)) {
                chatListAdapter.getItem(i).message = message;
                lstChatList.setAdapter(chatListAdapter);
                return;
            }
        }
    }

    public int getSize() {
        return chatListAdapter.getCount();
    }

    public ChatListAdapter getAdapter() {
        return chatListAdapter;
    }
}

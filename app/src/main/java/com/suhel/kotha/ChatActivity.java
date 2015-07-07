package com.suhel.kotha;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


public class ChatActivity extends AppCompatActivity {

    private ListView lvChatList;
    private ChatListAdapter lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        lvChatList = (ListView) findViewById(R.id.chatList);

        lst = new ChatListAdapter(this);

        lst.add(new ListItem("Hello", false));
        lst.add(new ListItem("Hi", true));

        lvChatList.setAdapter(lst);

    }
}

package com.suhel.kotha;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class ChatActivity extends AppCompatActivity{

    private ListView lvChatList;
    private ChatListAdapter lst;
    private EditText txtText;
    private Button bSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        lvChatList = (ListView) findViewById(R.id.chatList);
        txtText = (EditText) findViewById(R.id.txtText);
        bSend = (Button) findViewById(R.id.bSend);

        Typeface roboto = Typeface.createFromAsset(getAssets(), "roboto-light.ttf");
        bSend.setTypeface(roboto);
        txtText.setTypeface(roboto);

        lst = new ChatListAdapter(this);
        lvChatList.setAdapter(lst);

        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lst.add(new ListItem(txtText.getText().toString(), true));
                lvChatList.setAdapter(lst);
            }
        });
        bSend.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                lst.add(new ListItem(txtText.getText().toString(), false));
                lvChatList.setAdapter(lst);
                return false;
            }
        });

    }
}

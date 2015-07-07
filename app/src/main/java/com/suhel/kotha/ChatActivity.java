package com.suhel.kotha;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class ChatActivity extends AppCompatActivity {

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
        lst.add(new ListItem("Ki kortesos?", false));
        lst.add(new ListItem("Baal chirchi ar ki", true));
        lst.add(new ListItem(":P", true));
        lst.add(new ListItem("Really? Kal i to shave korli?", false));
        lst.add(new ListItem("Tweezer diye chirte hoche... XD", false));
        lst.add(new ListItem("?", false));
        lst.add(new ListItem("Na... Kajer lok on the knees... Baal job diche...", true));
        lst.add(new ListItem(":P", true));
        lst.add(new ListItem("Interesting... So bogol job tao koriye nis...", false));
        lst.add(new ListItem("Bogol ta agun diyei korbo bhabchi... Nahole mohila tar nak e chul dhuke bogol e heche debe", true));
        lst.add(new ListItem("OH LOOORD... YOU KILL ME... XD", false));
        lst.add(new ListItem("ikr... :P", true));
        lst.add(new ListItem("CIgarette patha to ekta...", false));
        lst.add(new ListItem("Brishti te jete jete bhije jabe", true));
        lst.add(new ListItem("Light er speed e ashbe to...", false));
        lst.add(new ListItem("Oto speed e tamak chitke jabe...", true));
        lst.add(new ListItem("Tamak er guarantee deyena cigarette er company... Amio debona... :P", false));
        lvChatList.setAdapter(lst);

        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lst.add(new ListItem(txtText.getText().toString(), true));
                txtText.setText("");
                lvChatList.setAdapter(lst);
                scrollToBottom();
            }
        });
        bSend.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                lst.add(new ListItem(txtText.getText().toString(), false));
                txtText.setText("");
                lvChatList.setAdapter(lst);
                scrollToBottom();
                return true;
            }
        });

    }

    private void scrollToBottom() {
        lvChatList.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                lvChatList.setSelection(lst.getCount() - 1);
            }
        });
    }
}

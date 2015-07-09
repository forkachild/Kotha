package com.suhel.kotha;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ConversationFragment extends Fragment {

    private ListView lvChatList;
    private ChatListAdapter lst;
    private EditText txtText;
    private Button bSend;
    private String contact = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        contact = getArguments().getString("ARGUMENT");

        ((BaseCallback) getActivity()).actionTitle(contact, "with love");

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        lvChatList = (ListView) view.findViewById(R.id.chatList);
        txtText = (EditText) view.findViewById(R.id.txtText);
        bSend = (Button) view.findViewById(R.id.bSend);

        Typeface roboto = Typeface.createFromAsset(getActivity().getAssets(), "roboto-medium.ttf");
        bSend.setTypeface(roboto);
        roboto = Typeface.createFromAsset(getActivity().getAssets(), "roboto-light.ttf");
        txtText.setTypeface(roboto);

        lst = new ChatListAdapter(getActivity());
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


    private void scrollToBottom() {
        lvChatList.post(new Runnable() {
            @Override
            public void run() {
                lvChatList.setSelection(lst.getCount() - 1);
            }
        });
    }
}

package com.suhel.kotha;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ChatFragment extends Fragment {

    private ListView lvChatList;
    private ChatAdapter lst;
    private EditText txtText;
    private Button bSend;
    private String username = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        username = getArguments().getString("ARGUMENT");

        ((BaseCallback) getActivity()).actionTitle(username, "messages");

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        lvChatList = (ListView) view.findViewById(R.id.chatList);
        txtText = (EditText) view.findViewById(R.id.txtText);
        bSend = (Button) view.findViewById(R.id.bSend);


        Typeface roboto = Typeface.createFromAsset(getActivity().getAssets(), "roboto-medium.ttf");
        bSend.setTypeface(roboto);
        roboto = Typeface.createFromAsset(getActivity().getAssets(), "roboto-light.ttf");
        txtText.setTypeface(roboto);

        /*
        lst.add(new ChatItem("Ki kortesos?", false));
        lst.add(new ChatItem("Baal chirchi ar ki", true));
        lst.add(new ChatItem(":P", true));
        lst.add(new ChatItem("Really? Kal i to shave korli?", false));
        lst.add(new ChatItem("Tweezer diye chirte hoche... XD", false));
        lst.add(new ChatItem("?", false));
        lst.add(new ChatItem("Na... Kajer lok on the knees... Baal job diche...", true));
        lst.add(new ChatItem(":P", true));
        lst.add(new ChatItem("Interesting... So bogol job tao koriye nis...", false));
        lst.add(new ChatItem("Bogol ta agun diyei korbo bhabchi... Nahole mohila tar nak e chul dhuke bogol e heche debe", true));
        lst.add(new ChatItem("OH LOOORD... YOU KILL ME... XD", false));
        lst.add(new ChatItem("ikr... :P", true));
        lst.add(new ChatItem("Cigarette patha to ekta...", false));
        lst.add(new ChatItem("Brishti te jete jete bhije jabe", true));
        lst.add(new ChatItem("Light er speed e ashbe to...", false));
        lst.add(new ChatItem("Oto speed e tamak chitke jabe...", true));
        lst.add(new ChatItem("Tamak er guarantee deyena cigarette er company... Amio debona... :P", false));
        lvChatList.setAdapter(lst);
*/

        lvChatList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText("simple text", ((ChatItem)lvChatList.getItemAtPosition(position)).text));
                Toast.makeText(getActivity(), "Coped to clipboard", Toast.LENGTH_SHORT).show();
                return true;
            }

        });

        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lst = (ChatAdapter) lvChatList.getAdapter();
                lst.add(new ChatItem(txtText.getText().toString().trim(), true));
                lvChatList.setAdapter(lst);
                scrollToBottom();
                ((BaseCallback) getActivity()).sendChatMessage(username, txtText.getText().toString().trim());
                txtText.setText("");
            }
        });

        ((BaseCallback) getActivity()).loadChatMessages(username);

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

    public void setAdapter(ChatAdapter adapter) {
        lst = adapter;
        lvChatList.setAdapter(lst);
    }

    public int getSize() {
        return lst.getCount();
    }
}

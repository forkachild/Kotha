package com.suhel.kotha;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatAdapter extends BaseAdapter {

    private List<ChatItem> chatMessages;
    private LayoutInflater chatInflater = null;
    private Typeface typeface = null;

    public ChatAdapter(Context context) {
        chatInflater = LayoutInflater.from(context);
        chatMessages = new ArrayList<>();
        typeface = Typeface.createFromAsset(context.getAssets(), "roboto-medium.ttf");
    }

    public void add(ChatItem object) {
        chatMessages.add(object);
    }

    public void addMultiple(ChatItem[] objects) {
        Collections.addAll(chatMessages, objects);
    }


    public void addMultiple(List<ChatItem> objects) {
        for (ChatItem item : objects)
            chatMessages.add(item);
    }

    public void clear() {
        chatMessages.clear();
    }

    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public ChatItem getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder theHolder;
        ChatItem theItem = chatMessages.get(position);

        theHolder = new ViewHolder();

        if (theItem.direction)
            convertView = chatInflater.inflate(R.layout.chat_message_me, parent, false);
        else
            convertView = chatInflater.inflate(R.layout.chat_message_you, parent, false);

        theHolder.tvText = (TextView) convertView.findViewById(R.id.theText);

        theHolder.tvText.setTypeface(typeface);
        theHolder.tvText.setText(theItem.text);

        return convertView;
    }

    class ViewHolder {
        TextView tvText;
    }

}

class ChatItem {

    public String text;
    public boolean direction;

    public ChatItem(String txt, boolean dir) {
        text = txt;
        direction = dir;
    }

}
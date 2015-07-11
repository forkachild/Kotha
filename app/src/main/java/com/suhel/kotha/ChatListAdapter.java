package com.suhel.kotha;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatListAdapter extends BaseAdapter {

    private List<ChatListItem> chatListItems;
    private LayoutInflater chatInflater = null;
    private Typeface typeface_thin, typeface_light;

    public ChatListAdapter(Context context) {
        chatInflater = LayoutInflater.from(context);
        chatListItems = new ArrayList<>();
        typeface_thin = Typeface.createFromAsset(context.getAssets(), "roboto-thin.ttf");
        typeface_light = Typeface.createFromAsset(context.getAssets(), "roboto-light.ttf");
    }

    public void add(ChatListItem object) {
        chatListItems.add(object);
    }

    public void addMultiple(ChatListItem[] objects) {
        Collections.addAll(chatListItems, objects);
    }


    public void addMultiple(List<ChatListItem> objects) {
        for(ChatListItem item : objects)
            chatListItems.add(item);
    }

    public void clear() {
        chatListItems.clear();
    }

    @Override
    public int getCount() {
        return chatListItems.size();
    }

    @Override
    public ChatListItem getItem(int position) {
        return chatListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder theHolder;
        ChatListItem theItem = chatListItems.get(position);

        theHolder = new ViewHolder();

        convertView = chatInflater.inflate(R.layout.chat_list_item, parent, false);

        theHolder.tvChatListItemName = (TextView) convertView.findViewById(R.id.tvChatListItemName);
        theHolder.tvChatListItemMessage = (TextView) convertView.findViewById(R.id.tvChatListItemMessage);

        theHolder.tvChatListItemName.setTypeface(typeface_thin);
        theHolder.tvChatListItemMessage.setTypeface(typeface_light);
        theHolder.tvChatListItemName.setText(theItem.text);
        if (theItem.message == null)
            theHolder.tvChatListItemMessage.setText("");
        else
            theHolder.tvChatListItemMessage.setText(theItem.message);

        return convertView;
    }

    class ViewHolder {
        TextView tvChatListItemName, tvChatListItemMessage;
    }

}

class ChatListItem {

    public String text, message;

    public ChatListItem(String txt, String msg) {
        text = txt;
        message = msg;
    }

}
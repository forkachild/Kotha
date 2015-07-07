package com.suhel.kotha;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Suhel on 7/7/2015.
 */

public class ChatListAdapter extends BaseAdapter {

    private ArrayList<ListItem> chatMessages;
    private LayoutInflater chatInflater = null;
    private Typeface roboto = null;

    public ChatListAdapter(Context context) {
        chatInflater = LayoutInflater.from(context);
        chatMessages = new ArrayList<ListItem>();
        roboto = Typeface.createFromAsset(context.getAssets(), "roboto-light.ttf");
    }

    public void add(ListItem object) {
        chatMessages.add(object);
    }

    public void addMultiple(ListItem[] objects) {
        for (int i = 0; i < objects.length; i++)
            chatMessages.add(objects[i]);
    }

    public void clear() {
        chatMessages.clear();
    }

    @Override
    public int getCount() {
        return chatMessages.size();
    }

    @Override
    public ListItem getItem(int position) {
        return chatMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder theHolder;
        ListItem theItem = chatMessages.get(position);

        if(convertView == null) {

            theHolder = new ViewHolder();

            if (theItem.direction)
                convertView = chatInflater.inflate(R.layout.chat_message_me, parent, false);
            else
                convertView = chatInflater.inflate(R.layout.chat_message_you, parent, false);

            theHolder.tvText = (TextView) convertView.findViewById(R.id.theText);

            convertView.setTag(theHolder);
        }
        else
            theHolder = (ViewHolder) convertView.getTag();

        theHolder.tvText.setTypeface(roboto);
        theHolder.tvText.setText(theItem.text);

        return convertView;
    }

    class ViewHolder {
        TextView tvText;
    }
}

class ListItem {

    public String text;
    public boolean direction;

    public ListItem(String txt, boolean dir) {
        text = txt;
        direction = dir;
    }

}
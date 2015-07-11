package com.suhel.kotha;

import com.parse.ParseUser;

public interface BaseCallback {

    void actionTitle(String title, String subTitle);

    void loadChatMessages(String username);

    void loadChatFragment(String username);

    void sendChatMessage(String username, String message);

    void loadChatListItems();

    void loadSignup();

    void loadLogin();

    void signup(String firstName, String lastName, String userName, String password, String email);

    void login(String username, String password);

    void logout();

}

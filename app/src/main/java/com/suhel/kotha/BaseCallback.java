package com.suhel.kotha;
public interface BaseCallback {

    void actionTitle(String title, String subTitle);

    void loadSignup();

    void loadLogin();

    void signup(String firstName, String lastName, String userName, String password, String email);

    void login(String username, String password);

    void logout();

}

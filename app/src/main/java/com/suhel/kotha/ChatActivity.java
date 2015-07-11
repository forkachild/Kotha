package com.suhel.kotha;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity implements BaseCallback {

    private ListView lvChatList;
    private ChatListAdapter lst;
    private EditText txtText;
    private Button bSend;
    private ChatListFragment fragChatList;
    private ChatFragment fragConv;
    private LoginFragment fragLogin;
    private SignupFragment fragSignup;
    private List<ParseUser> listOfUsers;
    private static DBHelper theDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        if (ParseUser.getCurrentUser() != null) {

            if (fragChatList == null)
                fragChatList = new ChatListFragment();
            loadFragment(fragChatList, null);

        } else {
            fragLogin = new LoginFragment();
            loadFragment(fragLogin, null);
        }

    }

    @Override
    public void actionTitle(String title, String subTitle) {
        getSupportActionBar().setTitle(title);
        if (subTitle != null)
            getSupportActionBar().setSubtitle(subTitle);
        else
            getSupportActionBar().setSubtitle(null);
    }

    @Override
    public void loadChatMessages(final String username) {
        ParseQuery<ParseObject> queryMessage1 = new ParseQuery<>(Kotha.CLASS_MESSAGES);
        queryMessage1.whereEqualTo(Kotha.FIELD_MESSAGE_FROM, ParseUser.getCurrentUser().getUsername());
        queryMessage1.whereEqualTo(Kotha.FIELD_MESSAGE_TO, username);

        ParseQuery<ParseObject> queryMessage2 = new ParseQuery<>(Kotha.CLASS_MESSAGES);
        queryMessage2.whereEqualTo(Kotha.FIELD_MESSAGE_TO, ParseUser.getCurrentUser().getUsername());
        queryMessage2.whereEqualTo(Kotha.FIELD_MESSAGE_FROM, username);

        ArrayList<ParseQuery<ParseObject>> listOfQueries = new ArrayList<>();
        listOfQueries.add(queryMessage1);
        listOfQueries.add(queryMessage2);

        ParseQuery<ParseObject> queryMessage = ParseQuery.or(listOfQueries);

        queryMessage.orderByAscending(Kotha.FIELD_DATE_CREATED);
        queryMessage.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> list, ParseException e) {
                ChatAdapter chatAdapter = new ChatAdapter(ChatActivity.this);
                for (ParseObject text : list) {
                    if (text.getString(Kotha.FIELD_MESSAGE_FROM).equals(username))
                        chatAdapter.add(new ChatItem(text.getString(Kotha.FIELD_MESSAGE), false));
                    else
                        chatAdapter.add(new ChatItem(text.getString(Kotha.FIELD_MESSAGE), true));
                }
                fragConv.setAdapter(chatAdapter);
            }

        });
    }

    @Override
    public void loadChatFragment(String username) {
        if (fragConv == null)
            fragConv = new ChatFragment();
        loadFragmentWithBackStack(fragConv, username);
    }

    @Override
    public void sendChatMessage(String username, String message) {
        ParseObject newMessage = new ParseObject(Kotha.CLASS_MESSAGES);
        newMessage.put(Kotha.FIELD_MESSAGE_FROM, ParseUser.getCurrentUser().getUsername());
        newMessage.put(Kotha.FIELD_MESSAGE_TO, username);
        newMessage.put(Kotha.FIELD_MESSAGE, message);
        newMessage.saveEventually();
    }

    @Override
    public void loadChatListItems() {

        if (theDB.getCount() > 0) {
            listOfUsers = theDB.loadUsers();
            for (final ParseUser user : listOfUsers) {
                String lastMessage = theDB.fetchLastMessage(user.getUsername());
                if (lastMessage.isEmpty()) {
                    loadLatestMessage(user.getUsername(), ParseUser.getCurrentUser().getUsername(), new GetCallback<ParseObject>() {

                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if (e == null)
                                fragChatList.updateItem(user.getString(Kotha.FIELD_FIRST_NAME),
                                        parseObject.getString(Kotha.FIELD_MESSAGE));
                        }

                    });
                } else {
                    fragChatList.addItem(new ChatListItem(
                            user.getString(Kotha.FIELD_FIRST_NAME),
                            lastMessage));
                }
            }
        }

        ParseQuery<ParseUser> queryUsers = ParseUser.getQuery();
        queryUsers.whereNotEqualTo(Kotha.FIELD_USERNAME, ParseUser.getCurrentUser().getUsername());
        queryUsers.orderByDescending(Kotha.FIELD_DATE_CREATED);
        queryUsers.findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(List<ParseUser> listUsers, ParseException e) {

                if ((listOfUsers != null) && (listUsers.size() == listOfUsers.size()))
                    return;

                if (listUsers.size() > fragChatList.getSize()) {
                    ChatListAdapter searchAdapter = fragChatList.getAdapter();
                    for (final ParseUser user : listUsers) {
                        boolean isPresent = false;
                        for (int i = 0; i < searchAdapter.getCount(); i++) {
                            if (user.getUsername().equals(searchAdapter.getItem(i).text)) {
                                isPresent = true;
                                break;
                            }
                        }
                        if (!isPresent) {
                            //Add user to local db and also to list
                            loadLatestMessage(user.getUsername(), ParseUser.getCurrentUser().getUsername(), new GetCallback<ParseObject>() {

                                @Override
                                public void done(ParseObject parseObject, ParseException e) {
                                    if (e != null)
                                        fragChatList.addItem(new ChatListItem(
                                                user.getString(Kotha.FIELD_FIRST_NAME),
                                                ""));
                                    else
                                        fragChatList.addItem(new ChatListItem(
                                                user.getString(Kotha.FIELD_FIRST_NAME),
                                                parseObject.getString(Kotha.FIELD_MESSAGE)));
                                }

                            });

                        }
                    }
                } else {
                    return;
                }

                for (final ParseUser user : listUsers) {
                    loadLatestMessage(user.getUsername(), ParseUser.getCurrentUser().getUsername(), new GetCallback<ParseObject>() {

                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if (e != null)
                                fragChatList.addItem(new ChatListItem(
                                        user.getString(Kotha.FIELD_FIRST_NAME),
                                        ""));
                            else
                                fragChatList.addItem(new ChatListItem(
                                        user.getString(Kotha.FIELD_FIRST_NAME),
                                        parseObject.getString(Kotha.FIELD_MESSAGE)));
                        }

                    });
                }
            }

        });
    }

    private void loadLatestMessage(String firstUser, String secondUser, GetCallback<ParseObject> callback) {
        ParseQuery<ParseObject> queryMessage1 = new ParseQuery<>(Kotha.CLASS_MESSAGES);
        queryMessage1.whereEqualTo(Kotha.FIELD_MESSAGE_FROM, firstUser);
        queryMessage1.whereEqualTo(Kotha.FIELD_MESSAGE_TO, secondUser);

        ParseQuery<ParseObject> queryMessage2 = new ParseQuery<>(Kotha.CLASS_MESSAGES);
        queryMessage2.whereEqualTo(Kotha.FIELD_MESSAGE_TO, firstUser);
        queryMessage2.whereEqualTo(Kotha.FIELD_MESSAGE_FROM, secondUser);

        ArrayList<ParseQuery<ParseObject>> listOfQueries = new ArrayList<>();
        listOfQueries.add(queryMessage1);
        listOfQueries.add(queryMessage2);

        ParseQuery<ParseObject> queryMessage = ParseQuery.or(listOfQueries);

        queryMessage.orderByDescending(Kotha.FIELD_DATE_CREATED);
        queryMessage.getFirstInBackground(callback);
    }

    @Override
    public void loadSignup() {
        if (fragSignup == null)
            fragSignup = new SignupFragment();
        loadFragment(fragSignup, null);
    }

    @Override
    public void loadLogin() {
        if (fragLogin == null)
            fragLogin = new LoginFragment();
        loadFragment(fragLogin, null);
    }

    @Override
    public void signup(String firstName, String lastName, String userName, String password, String email) {
        ParseUser newUser = new ParseUser();
        newUser.setUsername(userName);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.put(Kotha.FIELD_FIRST_NAME, firstName);
        newUser.put(Kotha.FIELD_LAST_NAME, lastName);
        newUser.put(Kotha.FIELD_USER_TYPE, "normal");

        newUser.signUpInBackground(new SignUpCallback() {

            @Override
            public void done(ParseException e) {
                if (e == null) {

                    if (fragChatList == null)
                        fragChatList = new ChatListFragment();
                    loadFragment(fragChatList, null);

                } else {
                    switch (e.getCode()) {

                        case ParseException.USERNAME_TAKEN:

                            Toast.makeText(ChatActivity.this, "Username already registered", Toast.LENGTH_LONG).show();
                            break;

                        case ParseException.EMAIL_TAKEN:

                            Toast.makeText(ChatActivity.this, "Email already registered", Toast.LENGTH_LONG).show();
                            break;

                        case ParseException.INVALID_EMAIL_ADDRESS:

                            Toast.makeText(ChatActivity.this, "Email is invalid", Toast.LENGTH_LONG).show();
                            break;

                        default:

                            Toast.makeText(ChatActivity.this, "Unknown error", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }

        });
    }

    @Override
    public void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {

            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null) {

                    if (fragChatList == null)
                        fragChatList = new ChatListFragment();
                    loadFragment(fragChatList, null);

                } else {
                    switch (e.getCode()) {

                        case ParseException.USERNAME_MISSING:

                            Toast.makeText(ChatActivity.this, "Username invalid", Toast.LENGTH_LONG).show();
                            break;

                    }
                }
            }

        });
    }

    @Override
    public void logout() {
        ParseUser.logOutInBackground(new LogOutCallback() {

            @Override
            public void done(ParseException e) {
                if (e == null) {
                    fragChatList = null;
                    fragConv = null;
                    if (fragLogin == null)
                        fragLogin = new LoginFragment();
                    loadFragment(fragLogin, null);
                } else {
                    Toast.makeText(ChatActivity.this, "Problem logging out", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void loadFragment(Fragment fragment, String argument) {
        if (argument != null) {
            Bundle args = new Bundle();
            args.putString("ARGUMENT", argument);
            fragment.setArguments(args);
        }
        getSupportFragmentManager().
                beginTransaction().
                setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out).
                replace(R.id.theFrame, fragment).
                commit();
    }

    private void loadFragmentWithBackStack(Fragment fragment, String argument) {
        if (argument != null) {
            Bundle args = new Bundle();
            args.putString("ARGUMENT", argument);
            fragment.setArguments(args);
        }
        getSupportFragmentManager().
                beginTransaction().
                setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out).
                addToBackStack(null).
                replace(R.id.theFrame, fragment).
                commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (theDB == null)
            theDB = new DBHelper(this);
    }
}

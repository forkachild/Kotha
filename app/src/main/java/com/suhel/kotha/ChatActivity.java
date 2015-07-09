package com.suhel.kotha;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class ChatActivity extends AppCompatActivity implements BaseCallback {

    private ListView lvChatList;
    private ChatListAdapter lst;
    private EditText txtText;
    private Button bSend;
    private ConversationFragment fragConv;
    private LoginFragment fragLogin;
    private SignupFragment fragSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        if (ParseUser.getCurrentUser() != null) {

            if (fragConv == null)
                fragConv = new ConversationFragment();
            loadFragment(fragConv, ParseUser.getCurrentUser().getString("firstName"));

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
        newUser.put("firstName", firstName);
        newUser.put("lastName", lastName);

        newUser.signUpInBackground(new SignUpCallback() {

            @Override
            public void done(ParseException e) {
                if (e == null) {

                    if (fragConv == null)
                        fragConv = new ConversationFragment();
                    loadFragment(fragConv, ParseUser.getCurrentUser().getString("firstName"));

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

                    if (fragConv == null)
                        fragConv = new ConversationFragment();
                    loadFragment(fragConv, ParseUser.getCurrentUser().getString("firstName"));

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
}

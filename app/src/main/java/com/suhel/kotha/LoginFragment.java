package com.suhel.kotha;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment {

    private EditText txtUser, txtPass;
    private Button bLogin;
    private TextView bSignup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((BaseCallback) getActivity()).actionTitle("Login", null);

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Typeface roboto_light = Typeface.createFromAsset(getActivity().getAssets(), "roboto-light.ttf");
        Typeface roboto_medium = Typeface.createFromAsset(getActivity().getAssets(), "roboto-medium.ttf");

        txtUser = (EditText) view.findViewById(R.id.txtLUser);
        txtPass = (EditText) view.findViewById(R.id.txtLPass);
        bLogin = (Button) view.findViewById(R.id.bLLogin);
        bSignup = (TextView) view.findViewById(R.id.bLSignup);

        txtUser.setTypeface(roboto_light);
        txtPass.setTypeface(roboto_light);
        bLogin.setTypeface(roboto_medium);
        bSignup.setTypeface(roboto_light);

        bLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String userName = txtUser.getText().toString().trim();
                String password = txtPass.getText().toString().trim();

                if (userName.isEmpty()) {
                    Toast.makeText(getActivity(), "Username is mandatory", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(getActivity(), "Password is mandatory", Toast.LENGTH_SHORT).show();
                    return;
                }

                ((BaseCallback) getActivity()).login(userName, password);

            }

        });

        bSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ((BaseCallback) getActivity()).loadSignup();

            }

        });

        return view;
    }
}

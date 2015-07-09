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

public class SignupFragment extends Fragment {

    private EditText txtFirstName, txtLastName, txtEmail, txtUser, txtPass;
    private Button bSignup;
    private TextView bLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((BaseCallback) getActivity()).actionTitle("Sign Up", null);

        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        Typeface roboto_light = Typeface.createFromAsset(getActivity().getAssets(), "roboto-light.ttf");
        Typeface roboto_medium = Typeface.createFromAsset(getActivity().getAssets(), "roboto-medium.ttf");

        txtFirstName = (EditText) view.findViewById(R.id.txtSFirstName);
        txtLastName = (EditText) view.findViewById(R.id.txtSLastName);
        txtEmail = (EditText) view.findViewById(R.id.txtSEmail);
        txtUser = (EditText) view.findViewById(R.id.txtSUser);
        txtPass = (EditText) view.findViewById(R.id.txtSPass);
        bSignup = (Button) view.findViewById(R.id.bSSignup);
        bLogin = (TextView) view.findViewById(R.id.bSLogin);

        txtFirstName.setTypeface(roboto_light);
        txtLastName.setTypeface(roboto_light);
        txtEmail.setTypeface(roboto_light);
        txtUser.setTypeface(roboto_light);
        txtPass.setTypeface(roboto_light);
        bSignup.setTypeface(roboto_medium);
        bLogin.setTypeface(roboto_light);

        bSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String firstName = txtFirstName.getText().toString().trim();
                String lastName = txtLastName.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String userName = txtUser.getText().toString().trim();
                String password = txtPass.getText().toString().trim();

                if (firstName.isEmpty()) {
                    Toast.makeText(getActivity(), "First name is mandatory", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (lastName.isEmpty()) {
                    Toast.makeText(getActivity(), "Last name is mandatory", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (email.isEmpty()) {
                    Toast.makeText(getActivity(), "Email is mandatory", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (userName.isEmpty()) {
                    Toast.makeText(getActivity(), "Username is mandatory", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(getActivity(), "Password is mandatory", Toast.LENGTH_SHORT).show();
                    return;
                }

                ((BaseCallback) getActivity()).signup(firstName, lastName, userName, password, email);

            }

        });

        bLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ((BaseCallback) getActivity()).loadLogin();

            }

        });

        return view;
    }
}

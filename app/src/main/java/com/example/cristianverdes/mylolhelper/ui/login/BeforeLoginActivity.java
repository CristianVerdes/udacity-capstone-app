package com.example.cristianverdes.mylolhelper.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.cristianverdes.mylolhelper.ui.MainActivity;
import com.example.cristianverdes.mylolhelper.R;

public class BeforeLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verify if user already logged in
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        boolean userLoggedIn = sharedPreferences.getBoolean(getString(R.string.KEY_USER_LOGGED_IN), false);

        if (!userLoggedIn) {
            // User needs to Login in
            setContentView(R.layout.activity_before_login);

            // LOGIN
            Button loginButton = findViewById(R.id.bt_login);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = BeforeLoginActivity.this.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putBoolean(getString(R.string.KEY_USER_LOGGED_IN), true);
                    editor.apply();

                    // Start Home activity with login
                    MainActivity.start(BeforeLoginActivity.this, true);
                }
            });

            // SKIP LOGIN
            Button skipLoginButton = findViewById(R.id.bt_skip_login);
            skipLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Setting the user "legged in" if he pressed skip to not show this interface the next time.
                    SharedPreferences sharedPreferences = BeforeLoginActivity.this.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putBoolean(getString(R.string.KEY_USER_LOGGED_IN), true);
                    editor.apply();

                    // Start Home activity with login
                    MainActivity.start(BeforeLoginActivity.this, false);
                }
            });

        } else {
            // User already logged in or skipped login the first time
            MainActivity.start(this, false);
        }
    }























}

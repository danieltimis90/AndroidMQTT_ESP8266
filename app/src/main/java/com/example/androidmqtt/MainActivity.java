package com.example.androidmqtt;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ConnectionClass sqlConnect;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button btnLogin;
    private String email;
    private String password;
    private Button btnSignup;
    private SharedPreferences mPrefrences;
    private SharedPreferences.Editor mEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        mPrefrences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPrefrences.edit();

        setValuesLogin();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditor.putString(getString(R.string.email), editTextEmail.getText().toString());
                mEditor.putString(getString(R.string.password), editTextPassword.getText().toString());
                mEditor.commit();

            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    private void setValuesLogin()
    {
        email = mPrefrences.getString(getString(R.string.email),"Email");
        password = mPrefrences.getString(getString(R.string.password),"Password");

        editTextEmail.setText(email);
        editTextPassword.setText(password);
    }
}
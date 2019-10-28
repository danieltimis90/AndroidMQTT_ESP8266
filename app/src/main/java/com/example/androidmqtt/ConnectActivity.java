package com.example.androidmqtt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ConnectActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText portEditText;
    private EditText subTopicEditText;
    private EditText hostEditText;
    private EditText pubTopicEditText;
    private SharedPreferences mPrefrences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        subTopicEditText = (EditText) findViewById(R.id.subTopicEditText);
        hostEditText = (EditText) findViewById(R.id.hostEditText);
        portEditText = (EditText) findViewById(R.id.portEditText);
        pubTopicEditText = (EditText) findViewById(R.id.pubTopicEditText);

        mPrefrences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPrefrences.edit();

        setValuesConnect();

        Button startBtn = (Button) findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditor.putString(getString(R.string.user), usernameEditText.getText().toString());
                mEditor.putString(getString(R.string.password2), passwordEditText.getText().toString());
                mEditor.putString(getString(R.string.subTopic), subTopicEditText.getText().toString());
                mEditor.putString(getString(R.string.pubTopic), pubTopicEditText.getText().toString());
                mEditor.putString(getString(R.string.port), portEditText.getText().toString());
                mEditor.putString(getString(R.string.host), hostEditText.getText().toString());

                mEditor.commit();

                Intent startIntent = new Intent(getApplicationContext(), PublishActivity.class);
                startIntent.putExtra("user", usernameEditText.getText().toString());
                startIntent.putExtra("password", passwordEditText.getText().toString());
                startIntent.putExtra("port", portEditText.getText().toString());
                startIntent.putExtra("subTopic", subTopicEditText.getText().toString());
                startIntent.putExtra("pubTopic", pubTopicEditText.getText().toString());
                startIntent.putExtra("host", hostEditText.getText().toString());
                startActivity(startIntent);
            }
        });
    }
    private void setValuesConnect()
    {
        usernameEditText.setText(mPrefrences.getString(getString(R.string.user),"User"));
        passwordEditText.setText(mPrefrences.getString(getString(R.string.password2),"Password"));
        subTopicEditText.setText(mPrefrences.getString(getString(R.string.subTopic),"Subscribe Topic"));
        hostEditText.setText(mPrefrences.getString(getString(R.string.password),"Host"));
        portEditText.setText(mPrefrences.getString(getString(R.string.port),"Port"));
        pubTopicEditText.setText(mPrefrences.getString(getString(R.string.pubTopic),"Publish Topic"));
    }
}

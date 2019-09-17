package com.example.androidmqtt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        final EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        final EditText portEditText = (EditText) findViewById(R.id.portEditText);
        final EditText subTopicEditText = (EditText) findViewById(R.id.subTopicEditText);
        final EditText hostEditText = (EditText) findViewById(R.id.hostEditText);
        final EditText pubTopicEditText = (EditText) findViewById(R.id.pubTopicEditText);
        Button startBtn = (Button) findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(), PublishActivity.class);
                startIntent.putExtra("username", usernameEditText.getText().toString());
                startIntent.putExtra("password", passwordEditText.getText().toString());
                startIntent.putExtra("port", portEditText.getText().toString());
                startIntent.putExtra("subTopic", subTopicEditText.getText().toString());
                startIntent.putExtra("pubTopic", pubTopicEditText.getText().toString());
                startIntent.putExtra("host", hostEditText.getText().toString());
                startActivity(startIntent);
            }
        });
    }

}
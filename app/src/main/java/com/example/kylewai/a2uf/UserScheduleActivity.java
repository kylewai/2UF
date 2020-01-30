package com.example.kylewai.a2uf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class UserScheduleActivity extends AppCompatActivity {
    private TextView userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_schedule);
        userInfo = findViewById(R.id.user_info);
        Intent intent = getIntent();
        String username = intent.getStringExtra(MainActivity.EXTRA_EMAIL);
        String uid = intent.getStringExtra(MainActivity.EXTRA_UID);
        String credentials = "Firebase:\nUsername: " + username + "\nUID: " + uid;
        userInfo.setText(credentials);
    }
}

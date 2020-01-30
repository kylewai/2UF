package com.example.kylewai.a2uf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private TextView text_username;
    private TextView text_password;
    private String sharedPrefFile = "com.example.kylewai.test";
    private FirebaseAuth mAuth;
    Intent intent_user_schedule;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        text_username = findViewById(R.id.textView_username);
        text_password = findViewById(R.id.textView_password);
        mAuth = FirebaseAuth.getInstance();
        intent_user_schedule = new Intent(this, UserScheduleActivity.class);
        sharedPref = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
    }


    public void addSharedPreferences(String email, String password){
        if(!sharedPref.contains(MainActivity.EXTRA_EMAIL)) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(MainActivity.EXTRA_EMAIL, email);
            editor.putString(MainActivity.EXTRA_PASSWORD, password);
            editor.apply();
        }
        return;
    }


    public void firebase_auth_result_handler(FirebaseUser user, String message, String password){
        if(user == null){
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            return;
        }
        addSharedPreferences(user.getEmail(), password);
        Bundle extras = new Bundle();
        extras.putString(MainActivity.EXTRA_EMAIL, user.getEmail());
        extras.putString(MainActivity.EXTRA_UID, user.getUid());
        intent_user_schedule.putExtras(extras);
        startActivity(intent_user_schedule);
    }


    public void firebase_register(View view){
        Log.d("Main", "Got here");
        String email = text_username.getText().toString();
        String password = text_password.getText().toString();
        firebase_sign_in(email, password);
    }

    public void firebase_sign_in(String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            firebase_auth_result_handler(user, "", password);
                        }
                        else{
                            Log.d("Register", task.getException().getMessage());
                            firebase_auth_result_handler(null, task.getException().getMessage(), "");
                        }
                    }
                });
    }
}

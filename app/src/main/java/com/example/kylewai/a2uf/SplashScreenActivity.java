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

public class SplashScreenActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private String sharedPrefFile = "com.example.kylewai.test";
    static final String EXTRA_EMAIL = "com.example.kylewai.email";
    static final String EXTRA_PASSWORD = "com.example.kylewa.password";
    static final String EXTRA_UID = "com.example.kylewai.uid";
    private boolean starting = false;
    private FirebaseAuth mAuth;
    Intent intent_user_schedule;
    Intent intent_sign_in;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sharedPref = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        intent_user_schedule = new Intent(this, MainActivity.class);
        intent_sign_in = new Intent(this, LoginActivity.class);

        if(mAuth.getCurrentUser() != null){
            start_user_activity(mAuth.getCurrentUser());
        }
        else if(sharedPref.contains(EXTRA_EMAIL)){
            String email = "";
            String password = "";
            email = sharedPref.getString(EXTRA_EMAIL, email);
            password = sharedPref.getString(EXTRA_PASSWORD, password);
            firebase_sign_in(email, password);
        }
        else{
            startActivity(intent_sign_in);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity", "HERE ON PAUSE!!!!**");
    }


    public void firebase_sign_in(String email, final String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            firebase_auth_result_handler(user, "", password);
                        }
                        else{
                            firebase_auth_result_handler(null, task.getException().getMessage(), "");
                        }
                    }
                });
    }


    public void firebase_auth_result_handler(FirebaseUser user, String message, String password){
        if(user == null){
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            return;
        }
        start_user_activity(user);
    }

    public void start_user_activity(FirebaseUser user){
        Bundle extras = new Bundle();
        extras.putString(EXTRA_EMAIL, user.getEmail());
        extras.putString(EXTRA_UID, user.getUid());
        intent_user_schedule.putExtras(extras);
        startActivity(intent_user_schedule);
    }
}

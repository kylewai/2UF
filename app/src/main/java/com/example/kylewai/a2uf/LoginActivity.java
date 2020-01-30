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

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private String sharedPrefFile = "com.example.kylewai.test";
    static final String EXTRA_EMAIL = "com.example.kylewai.email";
    static final String EXTRA_PASSWORD = "com.example.kylewa.password";
    static final String EXTRA_UID = "com.example.kylewai.uid";
    private TextView text_username;
    private TextView text_password;
    private FirebaseAuth mAuth;
    Intent intent_user_schedule;
    Intent register_intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent_user_schedule = new Intent(this, UserScheduleActivity.class);
        sharedPref = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        if(sharedPref.contains(EXTRA_EMAIL)){
            String email = "";
            String password = "";
            email = sharedPref.getString(EXTRA_EMAIL, email);
            password = sharedPref.getString(EXTRA_PASSWORD, password);
            firebase_sign_in(email, password);
        }
        setContentView(R.layout.activity_login);

        text_username = findViewById(R.id.textView_username);
        text_password = findViewById(R.id.textView_password);
        register_intent = new Intent(this, RegisterActivity.class);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LoginActivity", "HERE ON PAUSE!!!!**");
    }

    public void authenticate(View view){
        String email = text_username.getText().toString();
        String password = text_password.getText().toString();
        firebase_sign_in(email, password);
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

    public void addSharedPreferences(String email, String password){
        if(!sharedPref.contains(EXTRA_EMAIL)) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(EXTRA_EMAIL, email);
            editor.putString(EXTRA_PASSWORD, password);
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
        extras.putString(EXTRA_EMAIL, user.getEmail());
        extras.putString(EXTRA_UID, user.getUid());
        intent_user_schedule.putExtras(extras);
        startActivity(intent_user_schedule);
    }


    public void register(View view){
        startActivity(register_intent);
    }
}

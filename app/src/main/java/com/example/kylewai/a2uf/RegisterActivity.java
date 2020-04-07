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

import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.AppUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private TextView text_email;
    private TextView text_username;
    private TextView text_password;
    private String sharedPrefFile = "com.example.kylewai.test";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    Intent intent_user_schedule;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        text_email = findViewById(R.id.textView_email);
        text_password = findViewById(R.id.textView_password);
        text_username = findViewById(R.id.textView_username);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        intent_user_schedule = new Intent(this, MainActivity.class);
        sharedPref = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
    }


    public void addSharedPreferences(String email, String password){
        if(!sharedPref.contains(LoginActivity.EXTRA_EMAIL)) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(LoginActivity.EXTRA_EMAIL, email);
            editor.putString(LoginActivity.EXTRA_PASSWORD, password);
            editor.apply();
        }
        return;
    }


    public void firebase_auth_result_handler(FirebaseUser user, String message, String password){
        if(user == null){
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            return;
        }
        Map<String, Object> docData = new HashMap<>();
        String username = text_username.getText().toString();
        AppUser newUser = new AppUser(username, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<Map<String, String>>());
        db.collection("users").document(user.getUid())
                .set(newUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("RegisterActivity", "Firestore user created");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("RegisterActivity", "Failed to create Firestore user");
                    }
                });

        addSharedPreferences(user.getEmail(), password);
        start_user_activity(user);
    }


    public void firebase_register(View view){
        Log.d("Main", "Got here");
        String email = text_email.getText().toString().trim();
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


    public void start_user_activity(FirebaseUser user){
        Bundle extras = new Bundle();
        extras.putString(LoginActivity.EXTRA_EMAIL, user.getEmail());
        extras.putString(LoginActivity.EXTRA_UID, user.getUid());
        intent_user_schedule.putExtras(extras);
        startActivity(intent_user_schedule);
    }
}

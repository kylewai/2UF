package com.example.kylewai.a2uf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.ChangeBounds;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kylewai.a2uf.myPosts.MyPostsActivity;
import com.example.kylewai.a2uf.userSchedule.AddCourseFragment;
import com.example.kylewai.a2uf.userSchedule.ExpandFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private String uid;
    private FirebaseUser currUser;
    private ViewPager viewPager;
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("MainC", "saved" + uid);
        outState.putString("uid", uid);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainC", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainC", "onStop");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.sign_out){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            SharedPreferences sharedPref = getSharedPreferences(LoginActivity.sharedPrefFile, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear().commit();
            startActivity(intent);

        }
        if(item.getItemId() == R.id.my_posts){
            Intent intent = new Intent(this, MyPostsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainC", "onDestroy");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            this.uid = savedInstanceState.getString("uid");
            Log.d("MainC", uid);
        }
        else{
            Intent intent = getIntent();
            SharedPreferences pref = getSharedPreferences(LoginActivity.sharedPrefFile, MODE_PRIVATE);
            boolean hasIntentData = intent.getStringExtra(LoginActivity.EXTRA_UID) != null;
            currUser = FirebaseAuth.getInstance().getCurrentUser();
            uid = hasIntentData? intent.getStringExtra(LoginActivity.EXTRA_UID) : currUser.getUid();
        }
        ActivityCompat.postponeEnterTransition(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label1));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label2));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label3));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label4));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(4);
        Log.d("MainC", "Yeahnew");
        Log.d("MainC", uid);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), uid);

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new
                TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new
                       TabLayout.OnTabSelectedListener() {
           @Override
           public void onTabSelected(TabLayout.Tab tab) {
               viewPager.setCurrentItem(tab.getPosition());
           }

           @Override
           public void onTabUnselected(TabLayout.Tab tab) {
           }

           @Override
           public void onTabReselected(TabLayout.Tab tab) {
           }
       });


    }
}

package com.example.kylewai.a2uf.SOC;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.kylewai.a2uf.R;

public class SOCSearchActivity extends AppCompatActivity {

    private EditText instructor;
    private EditText courseCode;
    private EditText classNumber;
    private EditText courseTitleKeyword;
    private ImageView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socsearch);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();

        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Search Filters");

        instructor = findViewById(R.id.instructor_input);
        courseCode = findViewById(R.id.courseCode_input);
        classNumber = findViewById(R.id.classNumber_input);
        courseTitleKeyword = findViewById(R.id.courseTitleKeyword_input);
        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("instructor", instructor.getText().toString());
                intent.putExtra("courseCode", courseCode.getText().toString());
                intent.putExtra("classNumber", classNumber.getText().toString());
                intent.putExtra("courseTitleKeyword", courseTitleKeyword.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent();
            intent.putExtra("instructor", instructor.getText().toString());
            intent.putExtra("courseCode", courseCode.getText().toString());
            intent.putExtra("classNumber", classNumber.getText().toString());
            intent.putExtra("courseTitleKeyword", courseTitleKeyword.getText().toString());
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

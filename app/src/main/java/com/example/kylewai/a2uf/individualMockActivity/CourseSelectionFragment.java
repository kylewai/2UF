package com.example.kylewai.a2uf.individualMockActivity;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kylewai.a2uf.R;


//This is a fragment used in the viewpager of an add class activity that shows SOC to add class
public class CourseSelectionFragment extends Fragment {


    public CourseSelectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_selection, container, false);
    }

}

package com.example.kylewai.a2uf.individualMockActivity;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.volleypojo.Section;
import com.example.kylewai.a2uf.volleypojo.UFCourse;
import com.example.kylewai.a2uf.volleypojo.UFData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


//This is a fragment used in the viewpager of an add class activity that shows SOC to add class
public class CourseSelectionFragment extends Fragment {

    private static final String ENDPOINT = "https://one.ufl.edu/apix/soc/schedule/?category=RES&term=2201";
    private List<Section> sectionList;
    private RequestQueue requestQueue;
    private RecyclerView mRecyclerView;
    private MockCourseListAdapter mAdapter;
    private Gson gson;

    public CourseSelectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_soc, container, false);
        sectionList = new ArrayList<Section>();
        mRecyclerView = view.findViewById(R.id.reyclerview);
        mAdapter = new MockCourseListAdapter(getContext(), sectionList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);


        requestQueue = Volley.newRequestQueue(getContext());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        fetchPosts();



        // Inflate the layout for this fragment
        return view;
    }

    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onDataLoaded, onDataError);
        requestQueue.add(request);
    }

    private final Response.Listener<String> onDataLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d("PostActivity", response);
            List<UFData> dataWrap = Arrays.asList(gson.fromJson(response, UFData[].class));
            UFData data = dataWrap.get(0);
            List<UFCourse> courseList = data.getCOURSES();
            for (UFCourse course : courseList) {
                for(Section section : course.getSections()){
                    section.setCode(course.getCode());
                    section.setCourseId(course.getCourseId());
                    section.setName(course.getName());
                    section.setTermInd(course.getTermInd());
                    section.setDescription(course.getDescription());
                    section.setPrerequisites(course.getPrerequisites());
                }
                sectionList.addAll(course.getSections());
                Log.i("PostActivity", course.getCode());
            }
            Log.i("PostActivity", sectionList.toString());
            mAdapter.update(sectionList);
        }
    };

    private final Response.ErrorListener onDataError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());
        }
    };


}

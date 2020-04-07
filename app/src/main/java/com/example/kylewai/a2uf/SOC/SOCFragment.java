package com.example.kylewai.a2uf.SOC;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.mockList.MockListAdapter;
import com.example.kylewai.a2uf.userSchedule.UserScheduleFragment;
import com.example.kylewai.a2uf.volleypojo.Section;
import com.example.kylewai.a2uf.volleypojo.UFCourse;
import com.example.kylewai.a2uf.volleypojo.UFData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


//Lists courses in SOC
public class SOCFragment extends Fragment {

    private static final String BASEENDPOINT = "https://one.ufl.edu/apix/soc/schedule/?category=RES&term=2201";
    private String endPoint;
    private List<Section> sectionList;
    private RequestQueue requestQueue;
    private RecyclerView mRecyclerView;
    private SOCCourseListAdapter mAdapter;
    private ImageView search;
    private String instructorQuery;
    private String courseCodeQuery;
    private String classNumberQuery;
    private String courseTitleKeywordQuery;
    private Gson gson;

    public int fragmentDecider;
    public String mockId;

    public SOCFragment() {
        // Required empty public constructor
    }

    public static SOCFragment newInstance(int fragmentDecider){
        SOCFragment soc = new SOCFragment();
        Bundle args = new Bundle();
        args.putInt("fragmentDecider", fragmentDecider);
        soc.setArguments(args);
        return soc;
    }

    public static SOCFragment newInstance(int fragmentDecider, String mockId){
        SOCFragment soc = new SOCFragment();
        Bundle args = new Bundle();
        args.putInt("fragmentDecider", fragmentDecider);
        args.putString("mockId", mockId);
        soc.setArguments(args);
        return soc;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_soc, container, false);
        sectionList = new ArrayList<Section>();
        mRecyclerView = view.findViewById(R.id.reyclerview);


        endPoint = BASEENDPOINT;
        requestQueue = Volley.newRequestQueue(getContext());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        fetchPosts();

        //For using different ExpandFragments depending on where it is accessed from.
        fragmentDecider = getArguments().getInt("fragmentDecider");
        mockId = getArguments().getString("mockId");

        search = view.findViewById(R.id.search);
        addSearchListener();


        // Inflate the layout for this fragment
        return view;
    }

    private void addSearchListener(){
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SOCSearchActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            instructorQuery = data.getStringExtra("instructor");
            courseCodeQuery = data.getStringExtra("courseCode");
            classNumberQuery = data.getStringExtra("classNumber");
            courseTitleKeywordQuery = data.getStringExtra("courseTitleKeyword");
            endPoint = BASEENDPOINT + "&instructor=" + instructorQuery + "&course-code="
                    + courseCodeQuery + "&class-num=" + classNumberQuery + "&course-title="
                    + courseTitleKeywordQuery;
            fetchPosts();
        }
    }

    private void fetchPosts() {
        Log.i("PostActivity", endPoint);
        StringRequest request = new StringRequest(Request.Method.GET, endPoint, onDataLoaded, onDataError);
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    private final Response.Listener<String> onDataLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d("PostActivity", response);
            List<UFData> dataWrap = Arrays.asList(gson.fromJson(response, UFData[].class));
            UFData data = dataWrap.get(0);
            List<UFCourse> courseList = data.getCOURSES();
            sectionList.clear();
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
            }
            mAdapter = new SOCCourseListAdapter(getContext(), sectionList);
            mAdapter.fragmentDecider = fragmentDecider;
            mAdapter.mockId = mockId;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setAdapter(mAdapter);
        }
    };

    private final Response.ErrorListener onDataError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());
        }
    };

}

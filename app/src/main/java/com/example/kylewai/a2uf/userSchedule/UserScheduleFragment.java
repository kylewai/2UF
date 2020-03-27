package com.example.kylewai.a2uf.userSchedule;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.ChangeBounds;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.AppUser;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.Course;
import com.example.kylewai.a2uf.individualMockActivity.MockScheduleActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//Fragment showing user weekly schedule
public class UserScheduleFragment extends Fragment {
    private String uid;
    public static String transferuid;
    FirebaseFirestore db;
    String[]periods = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "E1", "E2", "E3"};
    ViewGroup container;

    String userID;

    public UserScheduleFragment() {
        // Required empty public constructor
    }

    public static UserScheduleFragment newInstance(String uid) {
        // Required empty public constructor
        UserScheduleFragment userFrag = new UserScheduleFragment();
        Bundle args = new Bundle();
        args.putString("uid", uid);
        userFrag.setArguments(args);
        return userFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.uid = getArguments().getString("uid");
        transferuid = uid;
        db = FirebaseFirestore.getInstance();
        this.container = container;
        Log.d("UserScheduleFragment", "oncreate");
        //Horrible workaround for having to use fragment transitions to prevent previous transitions from showing
        //up again.
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        View view = inflater.inflate(R.layout.fragment_user_schedule, container, false);

        //Gets floating action button and gives it the functionality of opening a new
        //activity for adding classes.
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "User Schedule", Toast.LENGTH_LONG);
                toast.show();

                //Creating and opening a new activity for adding classes.
                Intent intent = new Intent(getActivity(), AddClassPager.class); //Was: AddClassActivity.class
                String message = uid;
                Log.d("putID", "" + uid);
                intent.putExtra("ID", message);
                getContext().startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData(view);
//        setSceneForTransition();
    }

    //Queries firebase for the data requried to render the user schedule
    public void getData(View view){
        DocumentReference docRef = db.collection("users").document(this.uid);

        //Query firebase for AppUser object based on uid
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        AppUser user = document.toObject(AppUser.class);
                        List<Map<String, String>> meetings = user.getWeeklyMeetTimes();
                        HashMap<String, ArrayList<String>> course_cells = fillWeeklySchedule(meetings);
                        getClassInfo(course_cells);
                    }
                    else{
                        Log.d("UserSchedules", "Nothing");
                    }
                }
                else{
                    Log.d("UserSchedule", "get failed");
                }
            }
        });
    }


    private HashMap<String, ArrayList<String>> fillWeeklySchedule(List<Map<String, String>> meetings){
        HashMap<String, ArrayList<String>> course_cells = new HashMap<>();
        ArrayList<String> cells_to_assign;
        for(int i = 0; i < meetings.size(); i++){
            Map<String, String> meetTime = meetings.get(i);
            String course = meetTime.get("course");
            String classNumber = meetTime.get("classNumber");
            String days = meetTime.get("days");
            String periodBegin = meetTime.get("periodBegin");
            String periodEnd = meetTime.get("periodEnd");
            cells_to_assign = getCellstoAssign(days, periodBegin, periodEnd);
            if(course_cells.containsKey(classNumber)){
                ArrayList<String> combined = course_cells.get(classNumber);
                Log.d("cells_to_assign", classNumber + " before " + combined.toString());
                combined.addAll(cells_to_assign);
                Log.d("cells_to_assign", classNumber + " " + combined.toString());
                course_cells.put(classNumber, combined);
            }
            else {
                course_cells.put(classNumber, cells_to_assign);
            }
            TextView cell;
            //Set text for cells with cell id in cells_to_assign
            for(int k = 0; k < cells_to_assign.size(); k++){
                String viewName = cells_to_assign.get(k);
                cell = getView().findViewById(getResources().getIdentifier(viewName, "id", getActivity().getPackageName()));
                cell.setText(course);
            }
        }
//                        Toast.makeText(getActivity(), cells_to_assign.toString(), Toast.LENGTH_LONG).show();
        return course_cells;
    }


    //Helper for fillWeeklySchedule that generates list of view ids of table cells corresponding to the day and periods
    private ArrayList<String> getCellstoAssign(String days, String periodBegin, String periodEnd){
        int k = 0;
        ArrayList<String> cells = new ArrayList<>();
        for(int i = 0; i < days.length(); i++){
            char day = days.charAt(i);
            boolean consecutivePeriod = false;
            for(int j = 0; j < periods.length; j++){
                if(periods[j].equals(periodBegin)) consecutivePeriod = true;
                if(consecutivePeriod){
                    cells.add(day + periods[j]);
                    if(periods[j].equals(periodEnd)) break;
                }
            }
        }
        return cells;
    }

    //Queries firebase for information on course associated with class number
    private void getClassInfo(HashMap<String, ArrayList<String>> course_cells){
        for(String classNumber : course_cells.keySet()) {
            final ArrayList<String> cells = course_cells.get(classNumber);
            DocumentReference docRef = db.collection("classes").document(classNumber);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Log.d("UserSchedule", "Complete");
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("UserSchedule", "got");
                            Course course = document.toObject(Course.class);
                            Log.d("UserSchedule", "Finally" + course.getCode());

                            for(String cell : cells){
                                addCourseOnClickListener(course, cell, classNumber);
                            }

                        } else {
                            Log.d("UserSchedule", "No course for classnum");
                        }
                    } else {
                        Log.d("UserSchedule", "Get course failed");
                    }
                }
            });
        }

    }


    private void addCourseOnClickListener(Course course, final String cell, String classNum){
        final String classNumber = classNum;
        final String courseCode = course.getCode();
        final String name = course.getName();
        final String description = course.getDescription();
        final String department = course.getDepartment();
        final String prereqs = course.getPrereqs();
        final String coreqs = course.getCoreqs();
        final List<String> instructors = course.getInstructors();
        final List<Map<String, String>> meetTimes = course.getMeetTimes();
        final String examTime = course.getExamTime();
        //final String courseID = course.getCourseID();
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        final View course_expand_view = inflater.inflate(R.layout.fragment_course_expand, null);
//
        final TextView cell_text = getActivity().findViewById(getResources().getIdentifier(cell, "id", getActivity().getPackageName()));
//        View view = (View)cell_text.getParent();
//        view.setId(getResources().getIdentifier("root", "id", getActivity().getPackageName()));
//        View root = course_expand_view.findViewById(R.id.root);
//        root.setId(getResources().getIdentifier(cell, "id", getActivity().getPackageName()));

//        TextView code = course_expand_view.findViewById(R.id.code);
//        code.setText(courseCode);
        cell_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ViewGroup sceneRoot = (ViewGroup) getActivity().findViewById(R.id.scene_root).getParent().getParent();
//                TableLayout tableLayout = getView().findViewById(R.id.table_layout);
//                Scene schedule_scene = new Scene(container, tableLayout);
//                Scene expand_scene = new Scene(sceneRoot, course_expand_view);
//                Transition transition = TransitionInflater.from(getActivity()).inflateTransition(R.transition.expand_transition);
//                TransitionManager.go(expand_scene, transition);
                Fragment fr = new ExpandFragment(courseCode, name, description, department, prereqs, coreqs, instructors, meetTimes, examTime, classNumber);
                fr.setSharedElementEnterTransition(new ChangeBounds());
                fr.setSharedElementReturnTransition(new ChangeBounds());
                fr.setEnterTransition(new ChangeBounds());
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.addSharedElement(cell_text, "expand");
                ft.setReorderingAllowed(true).replace(R.id.scene_root, fr).addToBackStack("schedule").commit();
            }
        });
    }
}

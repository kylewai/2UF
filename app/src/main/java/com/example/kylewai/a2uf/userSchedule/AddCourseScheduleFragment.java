package com.example.kylewai.a2uf.userSchedule;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.ChangeBounds;

import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.AppUser;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.Course;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.UserMock;
import com.example.kylewai.a2uf.individualMockActivity.MockCourseExpandFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//This is a fragment showing weekly schedule that is used in the viewpager of an add class activity
public class AddCourseScheduleFragment extends Fragment {

    FirebaseFirestore db;
    String[]periods = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "E1", "E2", "E3"};
    static String userID;

    final FirebaseFirestore database = FirebaseFirestore.getInstance();

    public AddCourseScheduleFragment() {
        // Required empty public constructor
    }

    public static AddCourseScheduleFragment newInstance(String uid) {
        // Required empty public constructor
        AddCourseScheduleFragment schedFrag = new AddCourseScheduleFragment();
        Bundle args = new Bundle();
        userID = uid;

        return schedFrag;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DocumentReference docRef = database.collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("getCurrentSched", "DocumentSnapshot data: " + document.getData());
                        List<Map<String, String>> currentUserSchedule = (List<Map<String, String>>) document.get("weeklyMeetTimes");
                        HashMap<String, ArrayList<String>> course_cells = fillWeeklySchedule(currentUserSchedule);
                        getClassInfo(course_cells);
                    }
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        db = FirebaseFirestore.getInstance();
        return inflater.inflate(R.layout.fragment_user_schedule, container, false);
    }

    private HashMap<String, ArrayList<String>> fillWeeklySchedule(List<Map<String, String>> meetings){
        HashMap<String, ArrayList<String>> course_cells = new HashMap<>();
        ArrayList<String> cells_to_assign;

        for(int i = 0; i < meetings.size(); i++){
            Map<String, String> meetTime = meetings.get(i);
            String course = meetTime.get("course");
            String classNumber = meetTime.get("classNumber");
            String days = meetTime.get("days");
            Log.i("Mister", days);
            String periodBegin = meetTime.get("periodBegin");
            String periodEnd = meetTime.get("periodEnd");
            cells_to_assign = getCellstoAssign(days, periodBegin, periodEnd);
            if(course_cells.containsKey(classNumber)){
                ArrayList<String> combined = course_cells.get(classNumber);
                combined.addAll(cells_to_assign);
                course_cells.put(classNumber, combined);
            }
            course_cells.put(classNumber, cells_to_assign);
            TextView cell;
            //Set text for cells with cell id in cells_to_assign
            for(int k = 0; k < cells_to_assign.size(); k++){
                String viewName = cells_to_assign.get(k);
                cell = (TextView)getView().findViewById(getResources().getIdentifier(viewName, "id", getActivity().getPackageName()));
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
                    Log.d("SchedFrag", "Complete");
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("SchedFrag", "got");
                            Course course = document.toObject(Course.class);
                            Log.d("SchedFrag", "Finally" + course.getCode());

                            for(String cell : cells){
                                addCourseOnClickListener(course, cell, classNumber);
                            }

                        } else {
                            Log.d("SchedFrag", "No course for classnum");
                        }
                    } else {
                        Log.d("SchedFrag", "Get course failed");
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
        final List<String> instructors = course.getInstructors();
        final List<Map<String, String>> meetTimes = course.getMeetTimes();
        final String examTime = course.getExamTime();
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
        Log.d("SchedFrag", "Added on click");
        cell_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ViewGroup sceneRoot = (ViewGroup) getActivity().findViewById(R.id.scene_root).getParent().getParent();
//                TableLayout tableLayout = getView().findViewById(R.id.table_layout);
//                Scene schedule_scene = new Scene(container, tableLayout);
//                Scene expand_scene = new Scene(sceneRoot, course_expand_view);
//                Transition transition = TransitionInflater.from(getActivity()).inflateTransition(R.transition.expand_transition);
//                TransitionManager.go(expand_scene, transition);
                Fragment fr = new ExpandFragment(courseCode, name, description, department, prereqs, instructors, meetTimes, examTime, classNumber);
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

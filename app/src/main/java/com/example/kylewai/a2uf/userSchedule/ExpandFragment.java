package com.example.kylewai.a2uf.userSchedule;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kylewai.a2uf.PagerAdapter;
import com.example.kylewai.a2uf.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;

//The fragment that pops up with class info when you select a cell with a class in the week schedule
//TODO: Delete functionality, Formatting
public class ExpandFragment extends Fragment {
    String classNumber;
    String courseCode;
    String name;
    String description;
    String department;
    String prereqs;
    List<String> instructors;
    List<Map<String, String>> meetTimes;
    String examTime;
    TextView textView_code;
    TextView textView_name;
    TextView textView_description;
    TextView textView_department;
    TextView textView_prereqs;
    TextView textView_instructors;
    TextView textView_meetTimes;
    TextView textView_examTime;
    TextView textView_coreqs;
    TextView textView_credits;
    static PagerAdapter.FirstFragmentListener listener;
    int fragmentSelector;
    String credits;

    public ExpandFragment(){}

    public ExpandFragment(String courseCode, String name, String description,
                          String department, String prereqs,
                          List<String> instructors, List<Map<String, String>> meetTimes,
                          String examTime, String classNumber, int fragmentSelector,
                          String credits) {
        // Required empty public constructor
        this.fragmentSelector = fragmentSelector;
        this.courseCode = courseCode;
        this.name = name;
        this.description = description;
        this.department = department;
        this.prereqs = prereqs;
        this.instructors = instructors;
        this.meetTimes = meetTimes;
        this.examTime = examTime;
        this.classNumber = classNumber;
        this.credits = credits;
    }

    public ExpandFragment(String courseCode, String name, String description,
                          String department, String prereqs,
                          List<String> instructors, List<Map<String, String>> meetTimes, String examTime, String classNumber,
                          PagerAdapter.FirstFragmentListener listener, String credits) {
        // Required empty public constructor
        ExpandFragment.listener = listener;
        this.courseCode = courseCode;
        this.name = name;
        this.description = description;
        this.department = department;
        this.prereqs = prereqs;
        this.instructors = instructors;
        this.meetTimes = meetTimes;
        this.examTime = examTime;
        this.classNumber = classNumber;
        this.credits = credits;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_expand, container, false);
        createCloseButtonListener(view);
        textView_code = view.findViewById(R.id.code);
        textView_code.setText(this.courseCode);
        textView_name = view.findViewById(R.id.name);
        textView_name.setText(this.name);
        textView_credits = view.findViewById(R.id.credits);
        textView_credits.setText(this.credits);
        textView_description = view.findViewById(R.id.description);
        textView_description.setText(this.description);
        textView_prereqs = view.findViewById(R.id.prereqs);
        String[] prereqsSplit = this.prereqs.split(":|\\.", -1);
        String prereqsString;
        if(this.prereqs.equals("")) {
            prereqsString = "";
        }
        else{
            prereqsString = prereqsSplit[1].substring(1);
        }
        textView_prereqs.setText(prereqsString);
        textView_coreqs = view.findViewById(R.id.coreqs);
        TextView coreqs_label = view.findViewById(R.id.coreqs_label);
        if(prereqsSplit.length <= 3){
            textView_coreqs.setVisibility(View.GONE);
            coreqs_label.setVisibility(View.GONE);
        }
        else{
            textView_coreqs.setText(prereqsSplit[3].substring(1));
        }
        textView_instructors = view.findViewById(R.id.instructors);
        String instructorString = "";
        int k = 0;
        for(String instructor : instructors){
            instructorString += (k == 0)? instructor : "\n" + instructor;
            k++;
        }

        textView_instructors.setText(instructorString);

        textView_department = view.findViewById(R.id.department);
        textView_department.setText(this.department);

        textView_meetTimes = view.findViewById(R.id.meetTimes);
        String meetTimesString = "";
        k = 0;
        for(Map<String, String> meetTime : meetTimes){
            meetTimesString += (k == 0)? "Days: " + meetTime.get("days") : "\n\nDays: " + meetTime.get("days");
            meetTimesString += "\nPeriods: " + meetTime.get("periodBegin");
            meetTimesString += " - " + meetTime.get("periodEnd");
            meetTimesString += "\nBuilding: " + meetTime.get("building");
            meetTimesString += "\nRoom: " + meetTime.get("room");
            k++;
        }
        textView_meetTimes.setText(meetTimesString);
        textView_examTime = view.findViewById(R.id.examTime);
        textView_examTime.setText(this.examTime);

        Button dropButton = view.findViewById(R.id.dropButton);
        //Don't want the drop button if fragmentSelector = 2 indicating this is the addcourseschedulefragment
        if(fragmentSelector == 2){
            dropButton.setVisibility(GONE);
        }
        else {
            dropButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setTitle("Drop " + courseCode + "?");

                    alertDialog.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dropClass();
                                }
                            }
                    );

                    alertDialog.setNegativeButton("Cancel",
                            new Dialog.OnClickListener(){
                                public void onClick(DialogInterface dialogInterface, int i){
                                    dialogInterface.cancel();
                                }
                            }
                    );
                    alertDialog.show();
                }
            });
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postponeEnterTransition();
        startPostponedEnterTransition();
    }

    public void createCloseButtonListener(View inflatedView){
        Button closeButton = inflatedView.findViewById(R.id.close);
        if(fragmentSelector == 1 || fragmentSelector == 2){
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().popBackStack();
                }
            });
        }
        else {
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onSwitch(courseCode, name, description, department, prereqs, instructors, meetTimes, examTime, classNumber, credits);
                }
            });
        }
    }

    private void makeToast(){
        Toast toast = Toast.makeText(getContext(), "Dropped " + courseCode, Toast.LENGTH_LONG);
        View toastView = toast.getView();
        toastView.getBackground().setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorSecondaryLight, null), PorterDuff.Mode.SRC_IN);
        TextView toastText = toastView.findViewById(android.R.id.message);
        toastText.setTextColor(ResourcesCompat.getColor(getResources(), R.color.textOnSecondary, null));
        toastText.setTypeface(toastText.getTypeface(), Typeface.BOLD);
        toastText.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
        toast.show();
    }

    private void dropClass(){
        List<Map<String, String>> dropList = new ArrayList<>();
        for (Map<String, String> meetTime : meetTimes) {
            Map<String, String> dropClass = new HashMap<>();
            //dropClass.put("classNumber", );
            dropClass.put("classNumber", classNumber);
            dropClass.put("course", courseCode);
            dropClass.put("days", meetTime.get("days"));
            dropClass.put("periodBegin", meetTime.get("periodBegin"));
            dropClass.put("periodEnd", meetTime.get("periodEnd"));

            dropList.add(dropClass);
        }

        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        //Log.d("ArraySize", "Num Meets: " + dropList.size());
        //Toast toasty = Toast.makeText(view.getContext(), "Num Meets" + dropList.size(), Toast.LENGTH_LONG);
        //toasty.show();

        for (Map<String, String> dropClass : dropList) {
            database.collection("users").document(UserScheduleFragment.transferuid).update("weeklyMeetTimes", FieldValue.arrayRemove(dropClass)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("dbUpdate", "DocumentSnapshot successfully updated!");
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("dbUpdate", "Error updating document", e);
                        }
                    });
        }
        makeToast();
        listener.onSwitch(courseCode, name, description, department, prereqs, instructors, meetTimes, examTime, classNumber, credits);
    }
}

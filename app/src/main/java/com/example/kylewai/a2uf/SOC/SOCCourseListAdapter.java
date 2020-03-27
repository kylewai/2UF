package com.example.kylewai.a2uf.SOC;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kylewai.a2uf.MainActivity;
import com.example.kylewai.a2uf.R;
import com.example.kylewai.a2uf.userSchedule.ExpandFragment;
import com.example.kylewai.a2uf.userSchedule.ExpandFragmentAddClass;
import com.example.kylewai.a2uf.volleypojo.Instructor;
import com.example.kylewai.a2uf.volleypojo.MeetTime;
import com.example.kylewai.a2uf.volleypojo.Section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;

//RecyclerView adapter for SOC
public class SOCCourseListAdapter extends RecyclerView.Adapter<SOCCourseViewHolder> {
    private final List<Section> sectionList;
    private LayoutInflater mInflater;
    public int fragmentDecider = 0;

    public SOCCourseListAdapter(Context context, List<Section> sectionList){
        this.sectionList = sectionList;
        mInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public SOCCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.course_item_view, parent, false);
        return new SOCCourseViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull SOCCourseViewHolder holder, int position) {
        Section mCurrent = this.sectionList.get(position);
        if(position % 2 == 0){
            holder.itemView.setBackgroundColor(Color.parseColor("#65a7f7"));
        }
        else{
            holder.itemView.setBackgroundColor(Color.parseColor("#7db5fa"));
        }
        holder.sectionObj = mCurrent;
        String code = mCurrent.getCode();
        String name = mCurrent.getName();
        String credit = mCurrent.getCredits();
        holder.codeText.setText(code);
        holder.nameText.setText(name);
        holder.creditText.setText("Credits " + credit);
        holder.fragmentDecider = fragmentDecider;
        Log.i("Adapter", code);
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    public void update(List<Section> sectionList){
        this.sectionList.addAll(sectionList);
        Log.i("Adapter", this.sectionList.size() + "");
        notifyDataSetChanged();
    }
}

class SOCCourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public final TextView codeText;
    public final TextView nameText;
    public final TextView creditText;
    final SOCCourseListAdapter mAdapter;
    public Section sectionObj;
    public int fragmentDecider = 0;

    public SOCCourseViewHolder(View courseItemView, SOCCourseListAdapter adapter) {
        super(courseItemView);
        codeText = courseItemView.findViewById(R.id.code);
        nameText = courseItemView.findViewById(R.id.name);
        creditText = courseItemView.findViewById(R.id.credit);
        courseItemView.setOnClickListener(this);
        this.mAdapter = adapter;
        this.sectionObj = null;
    }

    @Override
    public void onClick(View view) {

        //Log.d("SOC", "Click: " + sectionObj.getName());
        //Log.d("SOC", "Click: " + sectionObj.getMeetTimes().get(0).getMeetDays().get(0));
        Log.d("FragDecide", "Fragment Decider: " + fragmentDecider);

        //Turn sectionObj.getInstructors into a List of Strings.
        //turn sectionObj.getMeetTimes into a list of Maps of string string.

        //Turning sectionObj.getMeetTimes into something we can use.
        List<Map<String, String>> meetTimes = new ArrayList<>();
        for(MeetTime meet : sectionObj.getMeetTimes())
        {
            Map<String, String> toAdd = new HashMap<>();
            String daysCode = "";
            for(String day : meet.getMeetDays())
            {
                daysCode = daysCode + day;
            }
            toAdd.put("days", daysCode);
            toAdd.put("periodBegin", meet.getMeetPeriodBegin());
            toAdd.put("periodEnd", meet.getMeetPeriodEnd());
            toAdd.put("building", meet.getMeetBuilding());

            meetTimes.add(toAdd);
        }

        //Turning sectionObj.getInstructors into something we can use.
        List<String> instructors = new ArrayList<>();
        for(Instructor instructor : sectionObj.getInstructors())
        {
            instructors.add(instructor.getName());
        }
        Fragment fr = new ExpandFragment(sectionObj.getCode(), sectionObj.getName(), sectionObj.getDescription(), sectionObj.getDeptName(), sectionObj.getPrerequisites(), sectionObj.getPrerequisites(), instructors, meetTimes, sectionObj.getFinalExam(), sectionObj.getClassNumber());

        if(fragmentDecider == 1)
        {
            fr = new ExpandFragmentAddClass(sectionObj.getCode(), sectionObj.getName(), sectionObj.getDescription(), sectionObj.getDeptName(), sectionObj.getPrerequisites(), sectionObj.getPrerequisites(), instructors, meetTimes, sectionObj.getFinalExam(), sectionObj.getClassNumber());
        }
        else
        {
            fr = new ExpandFragment(sectionObj.getCode(), sectionObj.getName(), sectionObj.getDescription(), sectionObj.getDeptName(), sectionObj.getPrerequisites(), sectionObj.getPrerequisites(), instructors, meetTimes, sectionObj.getFinalExam(), sectionObj.getClassNumber());
        }

        //Fragment fr = new ExpandFragment(sectionObj.getCode(), sectionObj.getName(), sectionObj.getDescription(), sectionObj.getDeptName(), sectionObj.getPrerequisites(), sectionObj.getPrerequisites(), instructors, meetTimes, sectionObj.getFinalExam(), sectionObj.getClassNumber());
        fr.setSharedElementEnterTransition(new ChangeBounds());
        fr.setSharedElementReturnTransition(new ChangeBounds());
        fr.setEnterTransition(new ChangeBounds());

        //Really ghetto fix.
        FragmentTransaction ft = ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction();

//                ft.addSharedElement(cell_text, "expand");
        ft.setReorderingAllowed(true).replace(R.id.soc_root, fr).addToBackStack("SOC").commit();



    }
}
package com.example.kylewai.a2uf.individualMockActivity;


import android.util.Log;

import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.UserMock;
import com.example.kylewai.a2uf.forum.ForumFragment;
import com.example.kylewai.a2uf.SOC.SOCFragment;
import com.example.kylewai.a2uf.userSchedule.ExpandFragment;
import com.example.kylewai.a2uf.userSchedule.UserScheduleFragment;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

//This is the pager adapter for flipping between a weekly schedule and course selection
public class MockAddClassPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    UserMock userMock;
    String mockId;
    FragmentManager fm;
    Fragment firstFragment;
    FirstFragmentListener listener;
//    private String uid;

    public final class FirstFragmentListener{
        public void onSwitch(String courseCode, String name, String description,
                             String department, String prereqs,
                             List<String> instructors,
                             List<Map<String, String>> meetTimes,
                             String examTime, String classNumber, String credits){

            fm.beginTransaction().remove(firstFragment).commit();
            if(firstFragment instanceof ScheduleFragment){
                firstFragment = new MockCourseExpandFragment(courseCode, name, description, department, prereqs,
                        instructors, meetTimes, examTime, classNumber, mockId, listener, credits);
            }
            else{
                firstFragment = ScheduleFragment.newInstance(userMock, mockId, listener);
            }
            notifyDataSetChanged();
        }
    }

    public MockAddClassPagerAdapter(FragmentManager fm, int NumOfTabs, UserMock userMock, String mockId) {
        super(fm);
        this.fm = fm;
        this.userMock = userMock;
        this.mockId = mockId;
        this.mNumOfTabs = NumOfTabs;
        listener = new FirstFragmentListener();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if(firstFragment == null){
                    firstFragment = ScheduleFragment.newInstance(userMock, mockId, listener);
                }
                return firstFragment;
            case 1:
                SOCFragment newFrag = SOCFragment.newInstance(2, mockId);
                return newFrag; //Was new CourseSelectionFragment();;
            default: return null;
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if(object instanceof MockCourseExpandFragment && firstFragment instanceof ScheduleFragment){
            return POSITION_NONE;
        }
        if(object instanceof ScheduleFragment && firstFragment instanceof MockCourseExpandFragment){
            return POSITION_NONE;
        }
        return POSITION_UNCHANGED;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

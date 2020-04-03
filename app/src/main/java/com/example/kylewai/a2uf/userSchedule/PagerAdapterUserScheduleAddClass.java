package com.example.kylewai.a2uf.userSchedule;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.kylewai.a2uf.FragmentListener;
import com.example.kylewai.a2uf.SOC.SOCFragment;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.AppUser;
import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.UserMock;
import com.example.kylewai.a2uf.individualMockActivity.CourseSelectionFragment;
import com.example.kylewai.a2uf.individualMockActivity.MockCourseExpandFragment;

import java.util.List;
import java.util.Map;


//This is the pager adapter for flipping between a weekly schedule and course selection
public class PagerAdapterUserScheduleAddClass extends FragmentPagerAdapter {
    int mNumOfTabs;
    String userID;
//    private String uid;


    public PagerAdapterUserScheduleAddClass(FragmentManager fm, int NumOfTabs, String userID) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.userID = userID;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return AddCourseScheduleFragment.newInstance(userID);
            case 1:
                SOCFragment newFrag = SOCFragment.newInstance(1);
                return newFrag; //Was new CourseSelectionFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

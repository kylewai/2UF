package com.example.kylewai.a2uf.individualMockActivity;

import com.example.kylewai.a2uf.forum.ForumFragment;
import com.example.kylewai.a2uf.SOC.SOCFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

//This is the pager adapter for flipping between a weekly schedule and course selection
public class PagerAdapterAddClass extends FragmentPagerAdapter {
    int mNumOfTabs;
//    private String uid;
    public PagerAdapterAddClass(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new ScheduleFragment();
            case 1: return new CourseSelectionFragment();
            case 2: return new SOCFragment();
            case 3: return new ForumFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

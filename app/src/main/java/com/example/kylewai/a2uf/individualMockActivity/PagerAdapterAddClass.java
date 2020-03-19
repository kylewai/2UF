package com.example.kylewai.a2uf.individualMockActivity;

import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.UserMock;
import com.example.kylewai.a2uf.forum.ForumFragment;
import com.example.kylewai.a2uf.SOC.SOCFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

//This is the pager adapter for flipping between a weekly schedule and course selection
public class PagerAdapterAddClass extends FragmentPagerAdapter {
    int mNumOfTabs;
    UserMock userMock;
    String mockId;
//    private String uid;
    public PagerAdapterAddClass(FragmentManager fm, int NumOfTabs, UserMock userMock, String mockId) {
        super(fm);
        this.userMock = userMock;
        this.mockId = mockId;
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return ScheduleFragment.newInstance(userMock, mockId);
            case 1: return new CourseSelectionFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

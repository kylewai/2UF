package com.example.kylewai.a2uf;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;
    private String uid;
    public PagerAdapter(FragmentManager fm, int NumOfTabs, String uid) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.uid = uid;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new UserScheduleFragment(uid);
            case 1: return new MockListFragment();
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

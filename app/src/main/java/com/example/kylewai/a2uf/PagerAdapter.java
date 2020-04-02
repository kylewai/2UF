package com.example.kylewai.a2uf;

import com.example.kylewai.a2uf.SOC.SOCFragment;
import com.example.kylewai.a2uf.forum.ForumFragment;
import com.example.kylewai.a2uf.mockList.MockListFragment;
import com.example.kylewai.a2uf.userSchedule.ExpandFragment;
import com.example.kylewai.a2uf.userSchedule.UserScheduleFragment;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private String uid;
    FragmentManager fm;
    Fragment firstFragment;
    public final class FirstFragmentListener{
        public void onSwitch(String courseCode, String name, String description,
                             String department, String prereqs,
                             List<String> instructors,
                             List<Map<String, String>> meetTimes,
                             String examTime, String classNumber){
            fm.beginTransaction().remove(firstFragment).commit();
            if(firstFragment instanceof UserScheduleFragment){
                firstFragment = new ExpandFragment(courseCode, name, description, department, prereqs,
                        instructors, meetTimes, examTime, classNumber, listener);
            }
            else{
                firstFragment = UserScheduleFragment.newInstance(uid, listener);
            }
            notifyDataSetChanged();
        }
    }

    FirstFragmentListener listener;




    public PagerAdapter(FragmentManager fm, int NumOfTabs, String uid) {
        super(fm);
        this.fm = fm;
        this.mNumOfTabs = NumOfTabs;
        this.uid = uid;
        listener = new FirstFragmentListener();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if(firstFragment == null){
                    firstFragment = UserScheduleFragment.newInstance(uid, listener);
                }
                return firstFragment;
            case 1: return MockListFragment.newInstance(uid);
            case 2: return new SOCFragment();
            case 3: return new ForumFragment();
            default: return null;
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if(object instanceof UserScheduleFragment && firstFragment instanceof ExpandFragment){
            return POSITION_NONE;
        }
        if(object instanceof ExpandFragment && firstFragment instanceof UserScheduleFragment){
            return POSITION_NONE;
        }
        return POSITION_UNCHANGED;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

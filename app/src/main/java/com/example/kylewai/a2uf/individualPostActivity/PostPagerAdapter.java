package com.example.kylewai.a2uf.individualPostActivity;

import com.example.kylewai.a2uf.com.example.kylewai.firebasemodel.Post;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PostPagerAdapter extends FragmentPagerAdapter {
    private int numOfTabs;
    private Post post;
    private String postId;

    public PostPagerAdapter(FragmentManager fm, int numOfTabs, Post post, String postId) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.post = post;
        this.postId = postId;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: return PostScheduleFragment.newInstance(post);
            case 1: return CommentsFragment.newInstance(postId);
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}

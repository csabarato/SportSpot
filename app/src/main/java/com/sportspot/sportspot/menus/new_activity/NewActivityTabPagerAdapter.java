package com.sportspot.sportspot.menus.new_activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.sportspot.sportspot.menus.new_activity.tabs.ActivityDetailsTab;
import com.sportspot.sportspot.menus.new_activity.tabs.ActivityLocationTab;

public class NewActivityTabPagerAdapter extends FragmentStatePagerAdapter {

    int numOfTabs;

    public NewActivityTabPagerAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm, numOfTabs);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: return new ActivityDetailsTab();
            case 1: return new ActivityLocationTab();
            default:return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}

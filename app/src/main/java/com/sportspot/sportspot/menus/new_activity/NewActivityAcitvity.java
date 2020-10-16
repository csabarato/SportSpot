package com.sportspot.sportspot.menus.new_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.sportspot.sportspot.R;

public class NewActivityAcitvity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_acitvity);

        setToolbar();

        TabLayout tabLayout = findViewById(R.id.new_activity_tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_activity_details)));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_activity_location));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Add View Pager
        final ViewPager tabItem = findViewById(R.id.tab_item);

        final PagerAdapter adapter = new NewActivityTabPagerAdapter(getSupportFragmentManager(),2);
        tabItem.setAdapter(adapter);

        tabItem.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        tabItem.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                }
        );
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.app_toolbar);
        toolbar.setTitle(getString(R.string.menu_new_activity));
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
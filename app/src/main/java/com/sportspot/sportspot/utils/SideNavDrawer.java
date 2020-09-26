package com.sportspot.sportspot.utils;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.sportspot.sportspot.R;

public class SideNavDrawer {

    private Toolbar appToolbar;
    private DrawerLayout drawerLayout;
    private AppCompatActivity activity;
    private NavigationView.OnNavigationItemSelectedListener navItemSelectedListener;

    public SideNavDrawer(AppCompatActivity activity, Toolbar toolbar, DrawerLayout drawerLayout, NavigationView.OnNavigationItemSelectedListener navItemSelectedListener) {
        this.activity = activity;
        this.appToolbar = toolbar;
        this.drawerLayout = drawerLayout;
        this.navItemSelectedListener = navItemSelectedListener;

        setToolbar();
        setupNavDrawer();
        setupNavigationView();
    }

    private void setToolbar() {
        activity.setSupportActionBar(appToolbar);
    }

    private void setupNavDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity, drawerLayout,appToolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        if (drawerLayout != null) {
            drawerLayout.addDrawerListener(toggle);
        }
        toggle.syncState();
    }

    private void setupNavigationView() {
        NavigationView navigationView = activity.findViewById(R.id.nav_view_main_menu);

        if(navigationView != null) {
            navigationView.setNavigationItemSelectedListener(navItemSelectedListener);
        }
    }
}

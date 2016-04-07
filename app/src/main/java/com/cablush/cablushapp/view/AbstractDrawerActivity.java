package com.cablush.cablushapp.view;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.view.drawer.DrawerActivityConfiguration;

/**
 * Created by oscar on 13/12/15.
 */
public abstract class AbstractDrawerActivity extends CablushActivity {

    protected NavigationView navigationView;
    protected DrawerLayout drawerLayout;
    protected ActionBarDrawerToggle mDrawerToggle;

    protected int mainDrawerContent;
    protected int optionalDrawerContent;
    protected boolean isMainContentVisible;

    protected abstract DrawerActivityConfiguration getNavDrawerConfiguration();

    protected abstract boolean onNavItemSelected(int id);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DrawerActivityConfiguration navConf = getNavDrawerConfiguration();

        setContentView(navConf.getMainLayout());

        Toolbar toolbar = (Toolbar) findViewById(navConf.getToolbarId());
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(navConf.getNavigationId());
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(false); // Do not leave item checked
                drawerLayout.closeDrawers(); // Always close the drawer
                return onNavItemSelected(menuItem.getItemId());
            }
        });
        View headerView = navigationView.inflateHeaderView(navConf.getHeaderId());
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavItemSelected(v.getId());
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(getTitle());
        }

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(navConf.getDrawerLayoutId());
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                navConf.getDrawerOpenDesc(),
                navConf.getDrawerCloseDesc()
        ) {
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
                if (!isMainContentVisible) {
                    toggleDrawerContent();
                }
                super.onDrawerClosed(drawerView);
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mainDrawerContent = navConf.getMainDrawerContent();
        optionalDrawerContent = navConf.getOptionalDrawerContent();
        isMainContentVisible = true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    protected void toggleDrawerContent() {
        if (isMainContentVisible) {
            navigationView.getMenu().setGroupVisible(mainDrawerContent, false);
            navigationView.getMenu().setGroupVisible(optionalDrawerContent, true);
            isMainContentVisible = false;
        } else {
            navigationView.getMenu().setGroupVisible(mainDrawerContent, true);
            navigationView.getMenu().setGroupVisible(optionalDrawerContent, false);
            isMainContentVisible = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item);
    }
}

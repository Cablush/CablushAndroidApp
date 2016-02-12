package com.cablush.cablushapp.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.presenter.CadastroPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 07/11/15.
 */
public abstract class CadastroActivity<T> extends CablushActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    protected CadastroPresenter<T> presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cadastro);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(getTitle());
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        presenter = setupPresenter();
    }

    /**
     * Configure the fragments (tabs) on the ViewPager.
     * <p>
     * ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
     * adapter.addFragment(new Fragment1(), "Fragment 1");
     * adapter.addFragment(new Fragment2(), "Fragment 2");
     * ...
     * viewPager.setAdapter(adapter);
     * </p>
     *
     * @param viewPager The configured ViewPager.
     */
    protected abstract void setupViewPager(ViewPager viewPager);

    /**
     *
     * @return
     */
    protected abstract CadastroPresenter<T> setupPresenter();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.option_save:
                doSave();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called by the save menu option.
     */
    private void doSave() {
        if (validate()) {
            presenter.doSave(save());
        }
    }

    /**
     * Validate the data before save.
     *
     * @return True, if it is everything ok to save.
     */
    protected abstract boolean validate();

    /**
     * Save the registry.
     * <p>Called by save button.</p>
     *
     * @return The registry to be saved.
     */
    protected abstract T save();

    /**
     * Adapter to handle the tabs
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

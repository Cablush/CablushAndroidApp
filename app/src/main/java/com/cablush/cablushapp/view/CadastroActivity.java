package com.cablush.cablushapp.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
     * Setup the "Presenter" responsible for this Activity.
     *
     * @return The Presenter responsible for this Activity.
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
                showExitConfirmation();
                return true;
            case R.id.option_save:
                doSave();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showExitConfirmation();
    }

    private void showExitConfirmation() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.msg_confirm_no_save)
                .setCancelable(false)
                .setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpFromSameTask(CadastroActivity.this);
                        // TODO retornar de onde chamou (?)
                    }
                })
                .setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    /**
     * Return to the main activity.
     */
    protected void navigateBack() {
        setResult(RESULT_OK, MainActivity.makeIntent(this));
        finish();
    }

    /**
     * Called by the save menu option.
     */
    private void doSave() {
        T t = getData();
        if (validate(t)) {
            presenter.doSave(t);
        }
    }

    /**
     * Get the datea from the concrete activity.
     *
     * @return
     */
    protected abstract T getData();

    /**
     * Validate the data before save.
     *
     * @param t
     * @return True, if it is everything ok to save.
     */
    protected abstract boolean validate(T t);

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

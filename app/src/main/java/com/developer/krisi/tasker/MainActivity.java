package com.developer.krisi.tasker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.developer.krisi.tasker.ui.login.SelectProjectActivity;
import com.developer.krisi.tasker.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String PROJECT_ID = SelectProjectActivity.PROJECT_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String projectId = intent.getStringExtra(PROJECT_ID);
        Log.i(TAG, "Creating view for MainActivity");
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), projectId);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}
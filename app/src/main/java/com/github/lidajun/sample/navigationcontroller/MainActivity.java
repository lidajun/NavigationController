package com.github.lidajun.sample.navigationcontroller;

import android.os.Bundle;

import com.github.lidajun.android.navigationcontroller.activity_fragment.NavigationActivityV4;
import com.github.lidajun.android.navigationcontroller.widget.NavigationToolbar;


public class MainActivity extends NavigationActivityV4 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hasOffset = false;
        edgeTimes = 2;
        addAndCommitFragment(R.id.frameLayout, new FragmentA());
    }

    @Override
    protected NavigationToolbar initNavigationToolbar() {
        NavigationToolbar toolbar = (NavigationToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    @Override
    protected void backPressed() {
        finish();
    }
}

package com.lurenshuo.sample.navigationcontroller;

import android.os.Bundle;

import com.lurenshuo.android.navigationcontroller.activity_fragment.NavigationActivity;
import com.lurenshuo.android.navigationcontroller.widget.NavigationToolbar;


public class MainActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction().add(R.id.frameLayout, new FragmentA()).addToBackStack(null).commit();
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

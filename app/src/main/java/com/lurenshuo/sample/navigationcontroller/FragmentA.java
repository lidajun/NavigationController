package com.lurenshuo.sample.navigationcontroller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lurenshuo.android.navigationcontroller.activity_fragment.NavigationFragment;


/**
 * Created by lidajun on 17-6-27.
 */

public class FragmentA extends NavigationFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setToolbarTitle("FragmentA");
        return inflater.inflate(R.layout.fragment_a, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .hide(FragmentA.this)
                        .add(R.id.frameLayout, new FragmentB())
                        .addToBackStack(null)
                        .setCustomAnimations(R.animator.fragment_slide_left_enter, R.animator.fragment_slide_left_exit)
                        .commit();
            }
        });
    }
}

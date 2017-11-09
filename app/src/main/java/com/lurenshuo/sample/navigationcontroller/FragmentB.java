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

public class FragmentB extends NavigationFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setToolbarTitle("FragmentB");
        return inflater.inflate(R.layout.fragment_b,container,false);
    }

}

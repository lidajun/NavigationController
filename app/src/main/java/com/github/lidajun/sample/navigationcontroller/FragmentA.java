package com.github.lidajun.sample.navigationcontroller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.lidajun.android.navigationcontroller.activity_fragment.NavigationActivityV4;
import com.github.lidajun.android.navigationcontroller.activity_fragment.NavigationFragmentV4;


/**
 * Created by lidajun on 17-6-27.
 */

public class FragmentA extends NavigationFragmentV4 {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_a, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbarTitle("FragA");
        view.findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationActivityV4) getActivity()).addAndCommitFragment(R.id.frameLayout, new FragmentB());
            }
        });
    }
}

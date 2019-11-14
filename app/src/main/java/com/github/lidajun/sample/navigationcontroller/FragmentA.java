package com.github.lidajun.sample.navigationcontroller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import com.github.lidajun.android.navigationcontroller.activity_fragment.NavigationActivity;
import com.github.lidajun.android.navigationcontroller.activity_fragment.NavigationFragment;


/**
 * Created by lidajun on 17-6-27.
 */

public class FragmentA extends NavigationFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_a, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbarTitle("FragmentA");
        view.findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationActivity) getActivity()).addAndCommitFragment(R.id.frameLayout, new FragmentB());
            }
        });
    }
}

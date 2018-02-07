package com.github.lidajun.sample.navigationcontroller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.lidajun.android.navigationcontroller.activity_fragment.NavigationFragment;

/**
 * Created by lidajun on 17-6-27.
 */

public class FragmentB extends NavigationFragment {

    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_b,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbarTitle("FragmentB");
        initViewPager(view);
        view.findViewById(R.id.long_click_btn).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(v.getContext(), "long-click", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    private void initViewPager(View view) {
        mViewPager = ((ViewPager) view.findViewById(R.id.view_pager));
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View child = new View(container.getContext());
                if (position == 0) {

                    child.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else {
                    child.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
                container.addView(child);
                return child;
            }
        });
    }
}

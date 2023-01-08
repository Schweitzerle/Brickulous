package com.example.brickulous.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.brickulous.Adapter.MyPagerMySetsFragmentAdapter;
import com.example.brickulous.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MySetsFragment extends Fragment {

    BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_sets, container, false);

        initUI(view);

        return view;
    }

    private void initUI(View view) {
        ViewPager2 viewPager2 = view.findViewById(R.id.view_pager);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation_bar_my_sets);

        MyPagerMySetsFragmentAdapter adapter = new MyPagerMySetsFragmentAdapter(requireActivity());
        viewPager2.setAdapter(adapter);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_my_sets_inner:
                    viewPager2.setCurrentItem(0);
                    return true;
                case R.id.nav_favorites:
                    viewPager2.setCurrentItem(1);
                    return true;
                default:
                    viewPager2.setCurrentItem(0);
                    return false;
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

}
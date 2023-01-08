package com.example.brickulous.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.brickulous.MySetsFragments.FavoritesFragment;
import com.example.brickulous.MySetsFragments.MySetsInnerFragment;
import com.example.brickulous.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MySetsFragment extends Fragment {

    BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_sets, container, false);

        bottomNavigationView = view.findViewById(R.id.bottom_navigation_bar_my_sets);

        getChildFragmentManager().beginTransaction().replace(R.id.main_container_my_sets, new MySetsInnerFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment1 = null;
            switch (item.getItemId()) {
                case R.id.nav_my_sets_inner:
                    fragment1 = new MySetsInnerFragment();
                    break;
                case R.id.nav_favorites:
                    fragment1 = new FavoritesFragment();
                    break;
            }
            assert fragment1 != null;
            getChildFragmentManager().beginTransaction().replace(R.id.main_container_my_sets, fragment1).commit();

            return true;
        });


        return view;
    }
}
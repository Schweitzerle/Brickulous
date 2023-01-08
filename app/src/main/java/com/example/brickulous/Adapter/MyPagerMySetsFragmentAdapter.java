package com.example.brickulous.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.brickulous.MySetsFragments.FavoritesFragment;
import com.example.brickulous.MySetsFragments.MySetsInnerFragment;


public class MyPagerMySetsFragmentAdapter extends FragmentStateAdapter {


    public MyPagerMySetsFragmentAdapter(FragmentActivity fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MySetsInnerFragment();
            case 1:
                return new FavoritesFragment();
            // weitere Fälle für weitere Fragmente hinzufügen
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}



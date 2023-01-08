package com.example.brickulous.Fragments;

import static android.content.Context.MODE_PRIVATE;

import static com.example.brickulous.Fragments.HomeFragment.API_KEY;

import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.brickulous.Api.APIRequests;
import com.example.brickulous.Api.GetSetByNumberData;
import com.example.brickulous.Api.GetSetByNumberNoAdapterData;
import com.example.brickulous.Api.GetSetByNumberPlaneData;
import com.example.brickulous.Api.LegoSetData;
import com.example.brickulous.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ProfileFragment extends Fragment {

    TextView setsOwned, setsFavored, bricksOwned;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initUI(view);
        initNuberAnimator(view);
        return view;
    }

    private void initNuberAnimator(View view) {
        SharedPreferences sharedPreferencesFav= requireContext().getSharedPreferences("favorite_sets", MODE_PRIVATE);
        Set<String> favoriteSets = sharedPreferencesFav.getStringSet("favorite_sets", new HashSet<>());

        SharedPreferences sharedPreferences  = requireContext().getSharedPreferences("my_sets", MODE_PRIVATE);
        Set<String> mySets = sharedPreferences.getStringSet("my_sets", new HashSet<>());

        List<LegoSetData> favoriteList = new ArrayList<>();

        int start = 0;

        int endFav = favoriteSets.size();

        int endMy = mySets.size();

        GetSetByNumberPlaneData getSetData = null;

        for (String strings: mySets) {
            String JSON_URL =  APIRequests.GET_SET.getURL() + strings + API_KEY;
            getSetData = new GetSetByNumberPlaneData(requireContext(), JSON_URL, favoriteList);
            getSetData.execute();
        }
        assert getSetData != null;
        favoriteList = getSetData.getLegoSetData();

        int endBricks = 0;

        for (LegoSetData legoSetData: favoriteList) {
            endBricks += legoSetData.getNumbOfParts();
        }
        Log.d("Sets", String.valueOf(favoriteList.size()));

        ValueAnimator animatorFav = ValueAnimator.ofInt(start, endFav);
        animatorFav.setDuration(1000);
        animatorFav.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                setsFavored.setText(String.valueOf(value));
            }
        });
        animatorFav.start();


        ValueAnimator animatorMy = ValueAnimator.ofInt(start, endMy);
        animatorMy.setDuration(1000);
        animatorMy.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                setsOwned.setText(String.valueOf(value));
            }
        });
        animatorMy.start();


        ValueAnimator animatorBricks = ValueAnimator.ofInt(start, endBricks);
        animatorBricks.setDuration(1000);
        animatorBricks.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                bricksOwned.setText(String.valueOf(value));
            }
        });
        animatorBricks.start();

    }

    private void initUI(View view) {
        setsFavored = view.findViewById(R.id.sets_favored);
        setsOwned = view.findViewById(R.id.sets_owned);
        bricksOwned = view.findViewById(R.id.bricks_owned_in_sets);
    }
}
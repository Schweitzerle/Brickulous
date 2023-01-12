package com.example.brickulous.Fragments;

import static android.content.Context.MODE_PRIVATE;

import static com.example.brickulous.Fragments.HomeFragment.API_KEY;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.brickulous.Api.APIRequests;
import com.example.brickulous.Api.GetSetByNumberPlaneData;
import com.example.brickulous.Api.LegoSetData;
import com.example.brickulous.Database.UserSession;
import com.example.brickulous.LoginActivity;
import com.example.brickulous.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    TextView setsOwned, setsFavored, bricksOwned, statusTextView;
    Button signOutButton;
    ImageView profileImage;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initUI(view);
        initSignIn(view);
        initNuberAnimator();
        return view;
    }

    private void initSignIn(View view) {
        profileImage = view.findViewById(R.id.user_profile);
        statusTextView = view.findViewById(R.id.status_text_view);
        signOutButton = view.findViewById(R.id.sign_out_button);

        signOutButton.setOnClickListener(this);

        if (UserSession.getInstance().getCurrentUser().getPhotoUrl() != null) {
            Glide.with(requireContext())
                    .asBitmap()
                    .load(UserSession.getInstance().getCurrentUser().getPhotoUrl())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            profileImage.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
        statusTextView.setText(UserSession.getInstance().getCurrentUser().getDisplayName());
    }

    private void initNuberAnimator() {
        SharedPreferences sharedPreferencesFav = requireContext().getSharedPreferences("favorite_sets", MODE_PRIVATE);
        Set<String> favoriteSets = sharedPreferencesFav.getStringSet("favorite_sets", new HashSet<>());

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("my_sets", MODE_PRIVATE);
        Set<String> mySets = sharedPreferences.getStringSet("my_sets", new HashSet<>());

        List<LegoSetData> favoriteList = new ArrayList<>();

        int start = 0;

        int endFav = favoriteSets.size();

        int endMy = mySets.size();

        GetSetByNumberPlaneData getSetData = null;

        for (String strings : mySets) {
            String JSON_URL = APIRequests.GET_SET.getURL() + strings + API_KEY;
            getSetData = new GetSetByNumberPlaneData(requireContext(), JSON_URL, favoriteList);
            getSetData.execute();
        }
        assert getSetData != null;
        favoriteList = getSetData.getLegoSetData();

        int endBricks = 0;

        for (LegoSetData legoSetData : favoriteList) {
            endBricks += legoSetData.getNumbOfParts();
        }
        Log.d("Sets", String.valueOf(favoriteList.size()));

        if (endFav != 0) {
            ValueAnimator animatorFav = ValueAnimator.ofInt(start, endFav);
            animatorFav.setDuration(1000);
            animatorFav.addUpdateListener(animation -> {
                int value = (int) animation.getAnimatedValue();
                setsFavored.setText(String.valueOf(value));
            });
            animatorFav.start();
        } else {
            setsFavored.setText("0");
        }



        if (endMy != 0) {
            ValueAnimator animatorMy = ValueAnimator.ofInt(start, endMy);
            animatorMy.setDuration(1000);
            animatorMy.addUpdateListener(animation -> {
                int value = (int) animation.getAnimatedValue();
                setsOwned.setText(String.valueOf(value));
            });
            animatorMy.start();
        } else {
                setsOwned.setText("0");
        }


        if (endBricks != 0) {
            ValueAnimator animatorBricks = ValueAnimator.ofInt(start, endBricks);
            animatorBricks.setDuration(1000);
            animatorBricks.addUpdateListener(animation -> {
                int value = (int) animation.getAnimatedValue();
                bricksOwned.setText(String.valueOf(value));
            });
            animatorBricks.start();
        } else {
            bricksOwned.setText("0");
        }


    }

    private void initUI(View view) {
        setsFavored = view.findViewById(R.id.sets_favored);
        setsOwned = view.findViewById(R.id.sets_owned);
        bricksOwned = view.findViewById(R.id.bricks_owned_in_sets);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_out_button) {
            signOut();
        }
    }


    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getContext(), LoginActivity.class));
    }


}
package com.example.brickulous;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.brickulous.Api.APIRequests;
import com.example.brickulous.Api.GetSetByNumberData;
import com.example.brickulous.Api.GetSetByNumberNoAdapterData;
import com.example.brickulous.Api.LegoSetData;
import com.example.brickulous.Fragments.HomeFragment;

import java.util.HashSet;
import java.util.Set;

public class ItemDetailActivity extends AppCompatActivity {

    public static final String SET_NUMBER_KEY = "SET NUMBER";

    TextView setNumber, setName, year, numberOfPieces, themeID, setURL;
    ImageView setImage;
    ToggleButton favButton, mySetsButton;
    String setNumberString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        initUI();
    }

    private void initUI() {
        setNumberString = getIntent().getStringExtra(SET_NUMBER_KEY);
        setNumber = findViewById(R.id.set_numb);
        setImage = findViewById(R.id.set_image);
        setName = findViewById(R.id.set_name);
        setURL = findViewById(R.id.set_URL);
        setURL.setMovementMethod(LinkMovementMethod.getInstance());
        year = findViewById(R.id.year);
        themeID = findViewById(R.id.theme_ID);
        numberOfPieces = findViewById(R.id.number_of_pieces);
        favButton = findViewById(R.id.favorite_toggle);
        mySetsButton = findViewById(R.id.my_set_toggle);

        GetSetByNumberNoAdapterData getSetByNumberData = new GetSetByNumberNoAdapterData(getApplicationContext(), setNumber, setName, setURL, year, themeID, numberOfPieces, setImage, APIRequests.GET_SET.getURL() + setNumberString + HomeFragment.API_KEY);
        getSetByNumberData.execute();

        initmySetsButton();
        initFavButton();




    }

    private void initFavButton() {
        SharedPreferences sharedPreferencesToggleButtonState = getSharedPreferences("favorite_button_state" + setNumberString, MODE_PRIVATE);

        boolean isChecked = sharedPreferencesToggleButtonState.getBoolean("is_checked" + setNumberString, false);
        favButton.setChecked(isChecked);

        if (favButton.isChecked()) {
            favButton.setBackgroundResource(R.drawable.ic_favorite_red_24);
        } else {
            favButton.setBackgroundResource(R.drawable.ic_favorite_24);
        }

        favButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferencesFavoriteList = getSharedPreferences("favorite_sets", MODE_PRIVATE);
                SharedPreferences.Editor editorFavList = sharedPreferencesFavoriteList.edit();
                editorFavList.putBoolean("is_checked", isChecked);
                editorFavList.apply();

                SharedPreferences.Editor editor = sharedPreferencesToggleButtonState.edit();
                editor.putBoolean("is_checked" + setNumberString, isChecked);
                editor.apply();

                if (favButton.isChecked()) {
                    favButton.setBackgroundResource(R.drawable.ic_favorite_red_24);
                    Set<String> favoriteSets = sharedPreferencesFavoriteList.getStringSet("favorite_sets", new HashSet<>());
                    favoriteSets.add(setNumberString);

                    SharedPreferences.Editor editor2 = sharedPreferencesFavoriteList.edit();
                    editor2.putStringSet("favorite_sets", favoriteSets);
                    editor2.apply();
                } else {
                    favButton.setBackgroundResource(R.drawable.ic_favorite_24);
                    Set<String> favoriteSets = sharedPreferencesFavoriteList.getStringSet("favorite_sets", new HashSet<>());
                    favoriteSets.remove(setNumberString);

                    SharedPreferences.Editor editor2 = sharedPreferencesFavoriteList.edit();
                    editor2.putStringSet("favorite_sets", favoriteSets);
                    editor2.apply();
                }

            }
        });

    }

    private void initmySetsButton() {
        SharedPreferences sharedPreferencesToggleMyButtonState = getSharedPreferences("my_sets_button_state" + setNumberString, MODE_PRIVATE);

        boolean isCheckedMy = sharedPreferencesToggleMyButtonState.getBoolean("is_checked_my_sets" + setNumberString, false);
        mySetsButton.setChecked(isCheckedMy);

        if (mySetsButton.isChecked()) {
            mySetsButton.setBackgroundResource(R.drawable.ic_baseline_inventory_2_24);
        } else {
            mySetsButton.setBackgroundResource(R.drawable.ic_inventory_2_24);
        }

        mySetsButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferencesMyList = getSharedPreferences("my_sets", MODE_PRIVATE);
                SharedPreferences.Editor editorMySetsList = sharedPreferencesMyList.edit();
                editorMySetsList.putBoolean("is_checked_my_sets", isChecked);
                editorMySetsList.apply();

                SharedPreferences.Editor editor = sharedPreferencesToggleMyButtonState.edit();
                editor.putBoolean("is_checked_my_sets" + setNumberString, isChecked);
                editor.apply();
                if (mySetsButton.isChecked()) {
                    mySetsButton.setBackgroundResource(R.drawable.ic_baseline_inventory_2_24);
                    Set<String> mySets = sharedPreferencesMyList.getStringSet("my_sets", new HashSet<>());
                    mySets.add(setNumberString);

                    SharedPreferences.Editor editor2 = sharedPreferencesMyList.edit();
                    editor2.putStringSet("my_sets", mySets);
                    editor2.apply();
                } else {
                    mySetsButton.setBackgroundResource(R.drawable.ic_inventory_2_24);
                    Set<String> favoriteSets = sharedPreferencesMyList.getStringSet("my_sets", new HashSet<>());
                    favoriteSets.remove(setNumberString);

                    SharedPreferences.Editor editor2 = sharedPreferencesMyList.edit();
                    editor2.putStringSet("my_sets", favoriteSets);
                    editor2.apply();
                }
            }
        });

    }

}
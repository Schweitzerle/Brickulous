package com.example.brickulous;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.brickulous.Api.APIGetSet;
import com.example.brickulous.Api.LegoSetData;
import com.example.brickulous.Database.FirebaseDatabaseInstance;
import com.example.brickulous.Database.UserSession;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import io.github.muddz.styleabletoast.StyleableToast;

public class ItemDetailActivity extends AppCompatActivity {

    public static final String SET_NUMBER_KEY = "SET NUMBER";

    TextView setNumber, setName, year, numberOfPieces, themeID, setURL;
    ImageView setImage;
    ToggleButton favButton, mySetsButton;
    String setNumberString;
    LegoSetData legoSet = new LegoSetData();
    long[] pattern = {0, 200, 300, 400, 500};
    long[] pattern2 = {0, 400, 500, 200, 300};




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

        APIGetSet getSet = new APIGetSet(getBaseContext(), setNumberString);
        getSet.run(new APIGetSet.RequestListener() {
            @Override
            public void onResult(LegoSetData data) {
                legoSet = data;
                setNumberString = data.getSetNumb();
                setNumber.setText(data.getSetNumb());
                setName.setText(data.getName());
                year.setText(String.valueOf(data.getYear()));

                Glide.with(getBaseContext())
                        .asBitmap()
                        .load(data.getImageURL())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                setImage.setImageBitmap(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });

                numberOfPieces.setText(String.valueOf(data.getNumbOfParts()));
                themeID.setText(String.valueOf(data.getThemeID()));
                setURL.setClickable(true);
                setURL.setMovementMethod(LinkMovementMethod.getInstance());
                String text = "<a href=" + data.getSetURL() + "> Link zum Set auf Rebrickable </a>";
                setURL.setText(Html.fromHtml(text));
            }

            @Override
            public void onError() {

            }
        });

        initmySetsButton();
        initFavButton();
    }

    private void initFavButton() {
        SharedPreferences sharedPreferencesToggleButtonState = getSharedPreferences("favorite_button_state" + setNumberString, MODE_PRIVATE);
        AtomicReference<String> legoSetID = new AtomicReference<>("");

        boolean isChecked = sharedPreferencesToggleButtonState.getBoolean("is_checked" + setNumberString, false);
        favButton.setChecked(isChecked);

        if (favButton.isChecked()) {
            favButton.setBackgroundResource(R.drawable.ic_favorite_red_24);
        } else {
            favButton.setBackgroundResource(R.drawable.ic_favorite_24);
        }

        favButton.setOnCheckedChangeListener((buttonView, isChecked1) -> {
            SharedPreferences sharedPreferencesFavoriteList = getSharedPreferences("favorite_sets", MODE_PRIVATE);
            SharedPreferences.Editor editorFavList = sharedPreferencesFavoriteList.edit();
            editorFavList.putBoolean("is_checked", isChecked1);
            editorFavList.apply();

            SharedPreferences.Editor editor = sharedPreferencesToggleButtonState.edit();
            editor.putBoolean("is_checked" + setNumberString, isChecked1);
            editor.apply();

            if (favButton.isChecked()) {
                favButton.setBackgroundResource(R.drawable.ic_favorite_red_24);

                if (UserSession.getInstance().getCurrentUser() != null) {
                    DatabaseReference favoritesRef = FirebaseDatabaseInstance.getInstance().getFirebaseDatabase().getReference("Users").child(UserSession.getInstance().getCurrentUser().getUid()).child("Favorites");

                    DatabaseReference legoSetRef = favoritesRef.push();
                    legoSetID.set(legoSetRef.getKey());

                    Map<String, Object> legoSetData = new HashMap<>();
                    legoSetData.put("Set_Number", legoSet.getSetNumb());
                    legoSetData.put("Name", legoSet.getName());
                    legoSetData.put("Number_Of_Bricks", legoSet.getNumbOfParts());
                    legoSetRef.setValue(legoSetData);

                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(pattern, -1);

                    StyleableToast.makeText(getApplicationContext(), "Favorit erfolgreich hinzugefügt!", R.style.customToastAddFav).show();
                }
            } else {
                favButton.setBackgroundResource(R.drawable.ic_favorite_24);

                if (UserSession.getInstance().getCurrentUser() != null) {
                    DatabaseReference favoritesRef = FirebaseDatabaseInstance.getInstance().getFirebaseDatabase().getReference("Users").child(UserSession.getInstance().getCurrentUser().getUid()).child("Favorites");

                    DatabaseReference legoSetRef = favoritesRef.child(legoSetID.get());
                    legoSetRef.removeValue();

                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(pattern2, -1);

                    StyleableToast.makeText(getApplicationContext(), "Favorit erfolgreich entfernt!", R.style.customToastRemFav).show();
                }
            }
        });
    }



    private void initmySetsButton() {
        SharedPreferences sharedPreferencesToggleMyButtonState = getSharedPreferences("my_sets_button_state" + setNumberString, MODE_PRIVATE);
        AtomicReference<String> legoSetID = new AtomicReference<>("");

        boolean isCheckedMy = sharedPreferencesToggleMyButtonState.getBoolean("is_checked_my_sets" + setNumberString, false);
        mySetsButton.setChecked(isCheckedMy);

        if (mySetsButton.isChecked()) {
            mySetsButton.setBackgroundResource(R.drawable.ic_baseline_inventory_2_24);
        } else {
            mySetsButton.setBackgroundResource(R.drawable.ic_inventory_2_24);
        }

        mySetsButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences sharedPreferencesMyList = getSharedPreferences("my_sets", MODE_PRIVATE);
            SharedPreferences.Editor editorMySetsList = sharedPreferencesMyList.edit();
            editorMySetsList.putBoolean("is_checked_my_sets", isChecked);
            editorMySetsList.apply();

            SharedPreferences.Editor editor = sharedPreferencesToggleMyButtonState.edit();
            editor.putBoolean("is_checked_my_sets" + setNumberString, isChecked);
            editor.apply();


            if (mySetsButton.isChecked()) {
                mySetsButton.setBackgroundResource(R.drawable.ic_baseline_inventory_2_24);

                if (UserSession.getInstance().getCurrentUser() != null) {
                    DatabaseReference favoritesRef = FirebaseDatabaseInstance.getInstance().getFirebaseDatabase().getReference("Users").child(UserSession.getInstance().getCurrentUser().getUid()).child("My_Sets");

                    DatabaseReference legoSetRef = favoritesRef.push();
                    legoSetID.set(legoSetRef.getKey());

                    Map<String, Object> legoSetData = new HashMap<>();
                    legoSetData.put("Set_Number", legoSet.getSetNumb());
                    legoSetData.put("Name", legoSet.getName());
                    legoSetData.put("Number_Of_Bricks", legoSet.getNumbOfParts());
                    legoSetRef.setValue(legoSetData);

                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(pattern, -1);

                    StyleableToast.makeText(getApplicationContext(), "Mein Set erfolgreich hinzugefügt!", R.style.customToastAddMy).show();

                }
            } else {
                mySetsButton.setBackgroundResource(R.drawable.ic_inventory_2_24);

                if (UserSession.getInstance().getCurrentUser() != null) {
                    DatabaseReference favoritesRef = FirebaseDatabaseInstance.getInstance().getFirebaseDatabase().getReference("Users").child(UserSession.getInstance().getCurrentUser().getUid()).child("My_Sets");

                    DatabaseReference legoSetRef = favoritesRef.child(legoSetID.get());

                    legoSetRef.removeValue();

                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(pattern2, -1);

                    StyleableToast.makeText(getApplicationContext(), "Mein Set erfolgreich entfernt!", R.style.customToastRemMy).show();

                }
            }
        });

    }

}
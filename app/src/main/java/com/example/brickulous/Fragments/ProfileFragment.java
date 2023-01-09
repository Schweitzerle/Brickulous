package com.example.brickulous.Fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import static com.example.brickulous.Fragments.HomeFragment.API_KEY;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.brickulous.Api.APIRequests;
import com.example.brickulous.Api.GetSetByNumberData;
import com.example.brickulous.Api.GetSetByNumberNoAdapterData;
import com.example.brickulous.Api.GetSetByNumberPlaneData;
import com.example.brickulous.Api.LegoSetData;
import com.example.brickulous.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ProfileFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    TextView setsOwned, setsFavored, bricksOwned, statusTextView;
    SignInButton signInButton;
    Button signOutButton;
    GoogleApiClient googleApiClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initUI(view);
        initSignIn(view);
        initNuberAnimator(view);
        return view;
    }

    private void initSignIn(View view) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        statusTextView = view.findViewById(R.id.status_text_view);
        signInButton = view.findViewById(R.id.sign_in_button);
        signOutButton = view.findViewById(R.id.sign_out_button);

        signInButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);


    }

    private void initNuberAnimator(View view) {
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

    private void initFirebaseAuth() {
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setAndroidPackageName(
                        /* yourPackageName= */ "...",
                        /* installIfNotAvailable= */ true,
                        /* minimumVersion= */ null)
                .setHandleCodeInApp(true) // This must be set to true
                .setUrl("https://google.com") // This URL needs to be whitelisted
                .build();


        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder()
                        .enableEmailLinkSignIn()
                        .setActionCodeSettings(actionCodeSettings)
                        .build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());


        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);


    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }


    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            // See: https://developer.android.com/training/basics/intents/result
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            statusTextView.setText("Hello, " + account.getDisplayName());
        } else {

        }
    }



    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                statusTextView.setText("Signed Out");
            }
        });
    }


}
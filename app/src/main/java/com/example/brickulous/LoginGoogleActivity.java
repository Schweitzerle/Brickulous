package com.example.brickulous;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.brickulous.Database.FirebaseDatabaseInstance;
import com.example.brickulous.Database.UserSession;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;

public class LoginGoogleActivity extends LoginActivity {

    GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    String name;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Google Sign in...");
        progressDialog.show();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        googleSignInClient = GoogleSignIn.getClient(getBaseContext(), gso);

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserSession.getInstance().setCurrentUser(user);
                            DatabaseReference favoritesRef = FirebaseDatabaseInstance.getInstance().getFirebaseDatabase().getReference("Users").child(UserSession.getInstance().getCurrentUser().getUid()).child("User");

                            DatabaseReference legoSetRef = favoritesRef.push();

                            if (UserSession.getInstance().getCurrentUser() != null) {
                                favoritesRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        name = "";
                                        for (DataSnapshot legoSetSnapshot : dataSnapshot.getChildren()) {
                                            name = legoSetSnapshot.child("User_Name").getValue(String.class);
                                        }
                                        assert name != null;
                                        if (!name.equals(Objects.requireNonNull(UserSession.getInstance().getCurrentUser().getDisplayName()))) {
                                            Map<String, Object> userData = new HashMap<>();
                                            assert user != null;
                                            userData.put("User_Name", Objects.requireNonNull(user.getDisplayName()));
                                            legoSetRef.setValue(userData);
                                        }
                                        StyleableToast.makeText(getBaseContext(), "Willkommen " + name + "!", R.style.customToastLoggedIn).show();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }

                            updateUI(user);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginGoogleActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(LoginGoogleActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
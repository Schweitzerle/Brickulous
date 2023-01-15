package com.example.brickulous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brickulous.Database.FirebaseDatabaseInstance;
import com.example.brickulous.Database.User;
import com.example.brickulous.Database.UserSession;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AuthenticationActivity extends AppCompatActivity {

    MaterialTextView existingAccount;
    TextInputEditText email, password, passwordConfirm, username;
    MaterialButton confirmButton;
    String emailValPattern = "[a-zA-Z0-9._-]+@[a-z-]+\\.+[a-z]+";

    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        initUI();
    }

    private void initUI() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passwordConfirm = findViewById(R.id.password_confirm);
        confirmButton = findViewById(R.id.confirm_button);
        existingAccount = findViewById(R.id.existing_acc);
        username = findViewById(R.id.username);

        progressDialog = new ProgressDialog(AuthenticationActivity.this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        initButtons();
    }

    private void initButtons() {
        existingAccount.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), MainActivity.class)));

        confirmButton.setOnClickListener(v -> PerformAuthentication());
    }

    private void PerformAuthentication() {
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();
        String passwordConfirmString = passwordConfirm.getText().toString();


        if (!emailString.matches(emailValPattern)) {
            email.setError("Keine korrekte Email");
        } else if (passwordString.isEmpty() || passwordString.length() < 6) {
            password.setError("Password erfüllt nicht den Anforderungen");
        } else if (!passwordString.matches(passwordConfirmString)) {
            passwordConfirm.setError("Passwörter stimmen nicht überein");
        } else {
            progressDialog.setMessage("Bitte warten während Registrierung...");
            progressDialog.setTitle("Registrierung");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    FirebaseUser user = mAuth.getCurrentUser();
                    UserSession.getInstance().setCurrentUser(user);
                    DatabaseReference favoritesRef = FirebaseDatabaseInstance.getInstance().getFirebaseDatabase().getReference("Users").child(UserSession.getInstance().getCurrentUser().getUid()).child("User");

                    DatabaseReference legoSetRef = favoritesRef.push();

                    Map<String, Object> userData = new HashMap<>();
                    userData.put("User_Name", Objects.requireNonNull(username.getText()).toString());
                    legoSetRef.setValue(userData);
                    progressDialog.dismiss();
                    Toast.makeText(AuthenticationActivity.this, "Registrierung erfolgreich", Toast.LENGTH_SHORT).show();
                    sendUserToNextActivity();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getBaseContext(), "Registrierung fehlgeschlagen:" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
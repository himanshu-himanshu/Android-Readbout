package com.finalproject.milestone_readbout.ui.activities;

import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.milestone_readbout.MainActivity;
import com.finalproject.milestone_readbout.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        AppCompatEditText emailInputText;
        AppCompatEditText passwordInputText;
        AppCompatEditText usernameText;
        MaterialButton signupButton;
        TextView loginLink;

        usernameText = findViewById(R.id.nameInputText);
        emailInputText = findViewById(R.id.emailInputText);
        passwordInputText = findViewById(R.id.passwordInputText);
        signupButton = findViewById(R.id.signupBtn);
        loginLink = findViewById(R.id.loginNow);

        sharedpreferences = getSharedPreferences("READBOUT_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        signupButton.setOnClickListener(v -> {
            if (isEmpty(emailInputText.getText()) || isEmpty(passwordInputText.getText())) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(emailInputText.getText().toString(), passwordInputText.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getBaseContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            editor.putString("loggedUserID", user.getUid());
                            saveToDatabase(usernameText.getText().toString(), emailInputText.getText().toString(), passwordInputText.getText().toString(), user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getBaseContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void saveToDatabase(String username, String email, String password, String uid) {

        Map<String, Object> user = new HashMap<>();

        user.put("uid", uid);
        user.put("username", username);
        user.put("email", email);
        user.put("darkMode", true);

        db.collection("users").document(uid).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.e("Signup Data Saving", "Saved to firebase");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Log.e("Signup Data Saving", "Error in saving");
                }
            }
        });
    }
}
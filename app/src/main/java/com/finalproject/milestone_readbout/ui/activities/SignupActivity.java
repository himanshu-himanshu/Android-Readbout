package com.finalproject.milestone_readbout.ui.activities;

import static android.text.TextUtils.isEmpty;
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
import com.finalproject.milestone_readbout.utils.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    /** Variable initialization */
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

        /*---- Shared Preferences to store logged user ID received from Firebase ----*/
        sharedpreferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        /*---- Navigate to Login Activity on clicking login link on signup page ----*/
        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        /*---- Handles click on signup button ----*/
        signupButton.setOnClickListener(v -> {
            if (isEmpty(emailInputText.getText()) || isEmpty(passwordInputText.getText())) {
                Toast.makeText(this, Constants.EMPTY_FIELDS_ERROR, Toast.LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(emailInputText.getText().toString(), passwordInputText.getText().toString()).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(getBaseContext(), Constants.REGISTRATION_SUCCESSFUL, Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Store User ID into shared preference
                        editor.putString(Constants.LOGGED_USER_ID, user.getUid());
                        editor.apply();
                        editor.commit();

                        // Save data to firebase store
                        saveToDatabase(usernameText.getText().toString(), emailInputText.getText().toString(), user.getUid());
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(getBaseContext(), Constants.REGISTRATION_FAILED, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /** Function to save data to firebase after successful registration */
    private void saveToDatabase(String username, String email, String uid) {

        Map<String, Object> user = new HashMap<>();

        // Store data into map object
        user.put("uid", uid);
        user.put("username", username);
        user.put("email", email);
        user.put("darkMode", true);
        user.put("french", false);
        user.put("pageSize", 20);
        user.put("orderBy", "newest");
        user.put("allowNotifications", true);

        // Add data into database
        db.collection("users").document(uid).set(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.e("TAG", "Saved to firebase");
                Toast.makeText(getApplicationContext(), "uid from pref" + sharedpreferences.getString("loggedUserID", ""), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else {
                Log.e("TAG", "Error in saving");
            }
        });
    }
}
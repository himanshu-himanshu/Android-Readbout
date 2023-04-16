package com.finalproject.milestone_readbout.ui.activities;

import static android.text.TextUtils.isEmpty;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.finalproject.milestone_readbout.MainActivity;
import com.finalproject.milestone_readbout.R;
import com.finalproject.milestone_readbout.notification.NotificationDecorator;
import com.finalproject.milestone_readbout.utils.Constants;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    /** Variable Initialization */
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    SharedPreferences sharedpreferences;
    private NotificationManager notificationMgr;
    private NotificationDecorator notificationDecorator;
    RelativeLayout relativeLayout;
    public static Boolean isLoggedOut = false;
    Boolean allowNotifications;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        notificationMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationDecorator = new NotificationDecorator(this, notificationMgr);

        AppCompatEditText emailInputText;
        AppCompatEditText passwordInputText;
        MaterialButton loginButton;
        TextView signupLink;

        emailInputText = findViewById(R.id.emailInputField);
        emailInputText.setText("guru@yopmail.com");
        passwordInputText = findViewById(R.id.passwordInputField);
        passwordInputText.setText("12345678");
        loginButton = findViewById(R.id.loginBtn);
        signupLink = findViewById(R.id.registerNow);
        relativeLayout = findViewById(R.id.loginRelative);

        if(isLoggedOut) {
            Snackbar snackbar = Snackbar
                    .make(relativeLayout, Constants.SEE_YOU_SOON, Snackbar.LENGTH_LONG);
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
            snackbar.show();
            isLoggedOut = false;
        }

        /*---- Shared Preferences to store logged user ID received from Firebase ----*/
        sharedpreferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        /*---- Navigate to SignUp Activity on clicking signup link on login page ----*/
        signupLink.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
        });

        /*--- Handles click on login button ---*/
        loginButton.setOnClickListener(v -> {
            if (isEmpty(emailInputText.getText()) || isEmpty(passwordInputText.getText())) {
                Toast.makeText(this, Constants.EMPTY_FIELDS_ERROR, Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(emailInputText.getText().toString(), passwordInputText.getText().toString()).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Store User ID into shared preference
                        editor.putString(Constants.LOGGED_USER_ID, user.getUid());
                        editor.apply();
                        editor.commit();

                        fetchDataFromFirebase(user.getUid());

                        new Handler().postDelayed(() -> {
                            if(allowNotifications)
                                notificationDecorator.displayExpandableNotification("Save News", "Save your favourites to your list and read them later on.");
                        }, 8 * 1000);

                        // Navigate to MainActivity after logging in
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Snackbar snackbar = Snackbar
                                .make(relativeLayout, Constants.LOGIN_FAILED, Snackbar.LENGTH_LONG);
                        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                        snackbar.show();
                    }
                });
            }
        });
    }

    /** Function to fetch data from Firebase */
    private void fetchDataFromFirebase(String uid) {
        DocumentReference doc = db.collection("users").document(uid);
        doc.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                allowNotifications = documentSnapshot.getBoolean("allowNotifications");
                if(allowNotifications) {
                    /* Send notification messages */
                    notificationDecorator.displayExpandableNotification(Constants.LOGIN_SUCCESSFUL, Constants.WELCOME_BACK_MSG);
                }
            } else {
                Toast.makeText(getApplicationContext(), Constants.DATA_FETCHING_FAILED_FIREBASE, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), Constants.DATA_FETCHING_FAILED_FIREBASE, Toast.LENGTH_SHORT).show());
    }
}
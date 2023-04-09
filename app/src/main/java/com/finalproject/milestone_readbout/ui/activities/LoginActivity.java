package com.finalproject.milestone_readbout.ui.activities;

import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        AppCompatEditText emailInputText;
        AppCompatEditText passwordInputText;
        MaterialButton loginButton;
        TextView signupLink;

        emailInputText = findViewById(R.id.emailInputField);
        passwordInputText = findViewById(R.id.passwordInputField);
        loginButton = findViewById(R.id.loginBtn);
        signupLink = findViewById(R.id.registerNow);
        sharedpreferences = getSharedPreferences("READBOUT_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        signupLink.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            if (isEmpty(emailInputText.getText()) || isEmpty(passwordInputText.getText())) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(emailInputText.getText().toString(), passwordInputText.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getBaseContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            editor.putString("loggedUserID", user.getUid());
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getBaseContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
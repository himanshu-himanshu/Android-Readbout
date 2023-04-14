package com.finalproject.milestone_readbout.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.finalproject.milestone_readbout.R;
import com.finalproject.milestone_readbout.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GeneralSettingsActivity extends AppCompatActivity {

    RadioGroup textRadioGroup;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_settings);
        ImageView backImage = findViewById(R.id.backImage);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String loggedUserID = intent.getStringExtra(Constants.LOGGED_USER_ID);

        Log.e("GENERAL", "General Activity " + loggedUserID);

        //textRadioGroup = findViewById(R.id.textSizeRadioGroup);

//        textRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.rbSmallText:
//                        Toast.makeText(getApplicationContext(), "Small is selected", Toast.LENGTH_LONG).show();
//                        break;
//                    case R.id.rbMediumText:
//                        Toast.makeText(getApplicationContext(), "Medium is selected", Toast.LENGTH_LONG).show();
//                        break;
//                    case R.id.rbLargeText:
//                        Toast.makeText(getApplicationContext(), "Large is selected", Toast.LENGTH_LONG).show();
//                        break;
//                    default:
//                        return;
//                }
//            }
//        });

        backImage.setOnClickListener(v -> {
            this.finish();
        });
    }

    /**
    private void fetchDataFromFirebase(String uid) {
        DocumentReference doc = db.collection("users").document(uid);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    boolean allowNotifications = documentSnapshot.getBoolean("allowNotifications");
                } else {
                    Toast.makeText(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to fetch data from firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveDataToFirebase() {
        Map<String, Object> user = new HashMap<>();
        user.put("french", french.isChecked());
        user.put("allowNotifications", notification.isChecked());
        db.collection("users").document(loggedUserID).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.e("Setting Data Saving", "Saved to firebase");
                } else {
                    Log.e("Signup Data Saving", "Error in saving");
                }
            }
        });
    } */
}
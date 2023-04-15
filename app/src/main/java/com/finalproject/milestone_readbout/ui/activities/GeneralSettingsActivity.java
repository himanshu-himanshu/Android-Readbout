package com.finalproject.milestone_readbout.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.finalproject.milestone_readbout.R;
import com.finalproject.milestone_readbout.utils.Constants;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class GeneralSettingsActivity extends AppCompatActivity {
    private RadioGroup pageSizeRG, orderByRG;
    FirebaseFirestore db;
    String firebasePageSize, orderBy, loggedUserID;
    RadioButton rb, rb2, pageSize10, pageSize20, pageSize30, newestRb, oldestRb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_settings);
        ImageView backImage = findViewById(R.id.backImage);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();

        loggedUserID = intent.getStringExtra(Constants.LOGGED_USER_ID);

        /* UI Connections */
        pageSizeRG = findViewById(R.id.pageSizeRadioGroup);
        orderByRG = findViewById(R.id.orderRadioGroup);

        pageSize10 = findViewById(R.id.rb10);
        pageSize20 = findViewById(R.id.rb20);
        pageSize30 = findViewById(R.id.rb30);
        newestRb = findViewById(R.id.rbNewest);
        oldestRb = findViewById(R.id.rbOldest);

        fetchDataFromFirebase(loggedUserID);

        /* Set listener on radio group for page size */
        pageSizeRG.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb10:
                case R.id.rb20:
                case R.id.rb30:
                    saveDataToFirebase();
                    break;
                default:
                    return;
            }
        });

        orderByRG.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbNewest:
                case R.id.rbOldest:
                    saveDataToFirebase();
                    break;
                default:
                    return;
            }
        });

        backImage.setOnClickListener(v -> this.finish());
    }

    /** Fetch data from firebase to set up UI */
    private void fetchDataFromFirebase(String uid) {
        DocumentReference doc = db.collection("users").document(uid);
        doc.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                firebasePageSize = documentSnapshot.getString("pageSize");
                switch (firebasePageSize) {
                    case "10":
                        pageSize10.setChecked(true);
                        break;
                    case "20":
                        pageSize20.setChecked(true);
                        break;
                    case "30":
                        pageSize30.setChecked(true);
                        break;
                    default:
                }
                orderBy = documentSnapshot.getString("orderBy");
                switch (orderBy) {
                    case "newest":
                        newestRb.setChecked(true);
                        break;
                    case "oldest":
                        oldestRb.setChecked(true);
                        break;
                    default:
                }
            } else {
                Toast.makeText(this, Constants.DATA_FETCHING_FAILED_FIREBASE, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(this, Constants.DATA_FETCHING_FAILED_FIREBASE, Toast.LENGTH_SHORT).show());
    }

    /** Save data to firebase if any preference is changed */
    private void saveDataToFirebase() {
        Map<String, Object> user = new HashMap<>();
        int selectedIdPageSize = pageSizeRG.getCheckedRadioButtonId();
        int selectedOrderBy = orderByRG.getCheckedRadioButtonId();

        rb = findViewById(selectedIdPageSize);
        rb2 = findViewById(selectedOrderBy);

        user.put("pageSize", rb.getText());
        user.put("orderBy", rb2.getText());

        db.collection("users").document(loggedUserID).update(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
            } else {
                Log.e(Constants.TAG, "Error in saving");
            }
        });
    }
}
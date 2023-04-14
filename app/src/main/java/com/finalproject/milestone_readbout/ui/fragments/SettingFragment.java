package com.finalproject.milestone_readbout.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.finalproject.milestone_readbout.MainActivity;
import com.finalproject.milestone_readbout.R;
import com.finalproject.milestone_readbout.ui.activities.GeneralSettingsActivity;
import com.finalproject.milestone_readbout.ui.activities.LoginActivity;
import com.finalproject.milestone_readbout.ui.activities.SingleCategoryActivity;
import com.finalproject.milestone_readbout.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;
import java.util.Map;

public class SettingFragment extends Fragment {
    private FirebaseFirestore db;
    LinearLayout generalLinearLayout, logoutLinearLayout;
    TextView usernameText;
    TextView emailText;
    SwitchMaterial notification, french;
    private String loggedUserID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, null);
        db = FirebaseFirestore.getInstance();
        Bundle data = getArguments();

        if (data != null) {
            loggedUserID = data.getString(Constants.LOGGED_USER_ID);
            fetchDataFromFirebase(loggedUserID);
        }
        generalLinearLayout = view.findViewById(R.id.generalTab);
        logoutLinearLayout = view.findViewById(R.id.logoutLinear);
        usernameText = view.findViewById(R.id.settingUsername);
        emailText = view.findViewById(R.id.settingEmail);
        notification = view.findViewById(R.id.notificationSwitch);
        french = view.findViewById(R.id.languageSwitch);

        generalLinearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GeneralSettingsActivity.class);
            intent.putExtra(Constants.LOGGED_USER_ID, loggedUserID);
            startActivity(intent);
        });

        logoutLinearLayout.setOnClickListener(v -> {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
            builder1.setMessage("Do you want to logout?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(getContext(), "See you soon!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        });

        notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                saveDataToFirebase();
            }
        });

        french.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                saveDataToFirebase();
            }
        });

        return view;
    }

    private void fetchDataFromFirebase(String uid) {
        DocumentReference doc = db.collection("users").document(uid);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    usernameText.setText(documentSnapshot.getString("username"));
                    emailText.setText(documentSnapshot.getString("email"));
                    boolean allowNotifications = documentSnapshot.getBoolean("allowNotifications");
                    boolean isFrench = documentSnapshot.getBoolean("french");
                    notification.setChecked(allowNotifications);
                    french.setChecked(isFrench);
                } else {
                    Toast.makeText(getContext(), "Data not found", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to fetch data from firebase", Toast.LENGTH_SHORT).show();
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
    }
}


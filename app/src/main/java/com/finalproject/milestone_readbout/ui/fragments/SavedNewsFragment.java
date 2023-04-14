package com.finalproject.milestone_readbout.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.finalproject.milestone_readbout.R;
import com.finalproject.milestone_readbout.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SavedNewsFragment extends Fragment {
    String loggedUserID;
    private FirebaseFirestore db;

    Boolean isAnyNews;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saved_news_fragment, null);
        Bundle data = getArguments();
        db = FirebaseFirestore.getInstance();

        if (data != null) {
            loggedUserID = data.getString(Constants.LOGGED_USER_ID);
            fetchDataFromFirebase(loggedUserID);
        } else {
            Toast.makeText(getContext(), "Bundle data Null", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    private void fetchDataFromFirebase(String uid) {
        db.collection("savedNews").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.e("SAVED NEWS", document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.e("SAVED NEWS", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}

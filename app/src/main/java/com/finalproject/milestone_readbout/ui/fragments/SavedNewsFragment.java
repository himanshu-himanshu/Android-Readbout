package com.finalproject.milestone_readbout.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.finalproject.milestone_readbout.R;
import com.finalproject.milestone_readbout.adapters.SavedNewsAdapter;
import com.finalproject.milestone_readbout.models.SavedNewsFirebaseModel;
import com.finalproject.milestone_readbout.utils.Constants;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class SavedNewsFragment extends Fragment {
    private ArrayList<SavedNewsFirebaseModel> savedNewsFirebaseModels;
    private SavedNewsAdapter adapter;
    private RecyclerView savedNewsRecyclerView;
    private ProgressBar progressBar;
    private TextView progressBarText;
    String loggedUserID;
    private FirebaseFirestore db;
    FrameLayout noSavedNewsLinear;
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

        progressBar = view.findViewById(R.id.savedNewsProgressBar);
        progressBarText = view.findViewById(R.id.savedNewsProgressBarText);
        savedNewsRecyclerView = view.findViewById(R.id.savedNewsRecyclerView);
        noSavedNewsLinear = view.findViewById(R.id.noSavedNewsLinear);

        progressBar.setVisibility(View.VISIBLE);
        progressBarText.setVisibility(View.VISIBLE);
        noSavedNewsLinear.setVisibility(View.INVISIBLE);

        savedNewsFirebaseModels = new ArrayList<>();
        savedNewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SavedNewsAdapter(getContext(), savedNewsFirebaseModels);
        savedNewsRecyclerView.setAdapter(adapter);

        return view;
    }

    private void fetchDataFromFirebase(String uid) {
        db.collection("savedNews").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                savedNewsFirebaseModels.clear();
                if (error != null) {
                    Log.e("TAG", Constants.DATA_FETCHING_FAILED_FIREBASE, error);
                    return;
                }
                for (QueryDocumentSnapshot document : value) {
                    savedNewsFirebaseModels.add(new SavedNewsFirebaseModel(
                            document.getString("userID"),
                            document.getString("title"),
                            document.getString("description"),
                            document.getString("webUrl"),
                            document.getString("imageUrl")
                    ));
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
                progressBarText.setVisibility(View.INVISIBLE);
                noSavedNewsLinear.setVisibility(View.INVISIBLE);

                if(savedNewsFirebaseModels.size() == 0) {
                    noSavedNewsLinear.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}

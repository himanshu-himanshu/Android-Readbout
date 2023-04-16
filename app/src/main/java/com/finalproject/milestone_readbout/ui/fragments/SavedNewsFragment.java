package com.finalproject.milestone_readbout.ui.fragments;

import android.app.NotificationManager;
import android.graphics.Canvas;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.finalproject.milestone_readbout.R;
import com.finalproject.milestone_readbout.adapters.SavedNewsAdapter;
import com.finalproject.milestone_readbout.models.SavedNewsFirebaseModel;
import com.finalproject.milestone_readbout.notification.NotificationDecorator;
import com.finalproject.milestone_readbout.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

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

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                SavedNewsFirebaseModel deleteData = savedNewsFirebaseModels.get(position);
                Log.e("TAG", String.valueOf(deleteData.getDocumentID()));
                deleteDataFromFirebase(deleteData.getDocumentID());
            }

            public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,float dX, float dY,int actionState, boolean isCurrentlyActive){

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(getContext(), R.color.my_background))
                        .addActionIcon(R.drawable.baseline_delete_24)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(savedNewsRecyclerView);

        return view;
    }

    private void fetchDataFromFirebase(String uid) {
        db.collection("savedNews").addSnapshotListener((value, error) -> {
            savedNewsFirebaseModels.clear();
            if (error != null) {
                Log.e("TAG", Constants.DATA_FETCHING_FAILED_FIREBASE, error);
                return;
            }
            for (QueryDocumentSnapshot document : value) {
                Log.e("TAG", uid + " ID from firebase: " + document.getString("documentID"));
                if (document.getString("userID").equals(uid)) {
                    savedNewsFirebaseModels.add(new SavedNewsFirebaseModel(
                            document.getId(),
                            document.getString("userID"),
                            document.getString("title"),
                            document.getString("description"),
                            document.getString("webUrl"),
                            document.getString("imageUrl")
                    ));
                }
            }
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
            progressBarText.setVisibility(View.INVISIBLE);
            noSavedNewsLinear.setVisibility(View.INVISIBLE);

            if (savedNewsFirebaseModels.size() == 0) {
                noSavedNewsLinear.setVisibility(View.VISIBLE);
            }
        });
    }

    private void deleteDataFromFirebase(String docID) {
        db.collection("savedNews").document(docID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Constants.TAG, "DocumentSnapshot successfully deleted!");
                        Snackbar snackbar = Snackbar
                                .make(savedNewsRecyclerView, "Removed from list", Snackbar.LENGTH_LONG);
                        snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                        snackbar.show();
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Constants.TAG, "Error deleting document", e);
                    }
                });
    }
}

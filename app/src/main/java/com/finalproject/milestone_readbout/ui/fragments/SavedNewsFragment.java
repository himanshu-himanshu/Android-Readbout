package com.finalproject.milestone_readbout.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.finalproject.milestone_readbout.R;

public class SavedNewsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saved_news_fragment, null);
        Bundle data = getArguments();

        if (data != null) {
            Toast.makeText(getContext(), "Bundle data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Bundle data Null", Toast.LENGTH_SHORT).show();
        }
        return view;
    }
}

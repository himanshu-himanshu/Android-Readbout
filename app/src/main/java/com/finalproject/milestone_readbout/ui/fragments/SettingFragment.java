package com.finalproject.milestone_readbout.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.finalproject.milestone_readbout.R;
import com.finalproject.milestone_readbout.ui.activities.GeneralSettingsActivity;
import com.finalproject.milestone_readbout.ui.activities.SingleCategoryActivity;

public class SettingFragment extends Fragment {
    LinearLayout generalLinearLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, null);

        generalLinearLayout = view.findViewById(R.id.generalTab);

        generalLinearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GeneralSettingsActivity.class);
            startActivity(intent);
        });

        return view;
    }
}


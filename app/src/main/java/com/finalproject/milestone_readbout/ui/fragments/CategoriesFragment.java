package com.finalproject.milestone_readbout.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.finalproject.milestone_readbout.R;
import com.finalproject.milestone_readbout.ui.activities.SingleCategoryActivity;

public class CategoriesFragment extends Fragment {
    LinearLayout entertainmentLinearLayout, sportsLinearLayout, scienceLinearLayout, environmetLinearLayout, businessLinearLayout, technologyLinearLayout;
    TextView headingTextView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categories_fragment, null);
        entertainmentLinearLayout = view.findViewById(R.id.entertainmentCard);
        sportsLinearLayout = view.findViewById(R.id.sportsCard);
        scienceLinearLayout = view.findViewById(R.id.scienceCard);
        environmetLinearLayout = view.findViewById(R.id.healthCard);
        businessLinearLayout = view.findViewById(R.id.businessCard);
        technologyLinearLayout = view.findViewById(R.id.technologyCard);

        entertainmentLinearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SingleCategoryActivity.class);
            headingTextView = view.findViewById(R.id.entertainmentText);
            intent.putExtra("sectionName", "film");
            intent.putExtra("heading", headingTextView.getText());
            startActivity(intent);
        });

        sportsLinearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SingleCategoryActivity.class);
            headingTextView = view.findViewById(R.id.sportsText);
            intent.putExtra("heading", headingTextView.getText());
            intent.putExtra("sectionName", "sport");
            startActivity(intent);
        });

        scienceLinearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SingleCategoryActivity.class);
            headingTextView = view.findViewById(R.id.scienceText);
            intent.putExtra("heading", headingTextView.getText());
            intent.putExtra("sectionName", "science");
            startActivity(intent);
        });

        environmetLinearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SingleCategoryActivity.class);
            headingTextView = view.findViewById(R.id.environmentText);
            intent.putExtra("heading", headingTextView.getText());
            intent.putExtra("sectionName", "environment");
            startActivity(intent);
        });

        businessLinearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SingleCategoryActivity.class);
            headingTextView = view.findViewById(R.id.businessText);
            intent.putExtra("heading", headingTextView.getText());
            intent.putExtra("sectionName", "business");
            startActivity(intent);
        });

        technologyLinearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SingleCategoryActivity.class);
            headingTextView = view.findViewById(R.id.technologyText);
            intent.putExtra("heading", headingTextView.getText());
            intent.putExtra("sectionName", "technology");
            startActivity(intent);
        });

        return view;
    }
}

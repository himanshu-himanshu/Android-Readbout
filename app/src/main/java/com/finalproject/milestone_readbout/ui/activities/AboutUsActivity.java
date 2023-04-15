package com.finalproject.milestone_readbout.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.finalproject.milestone_readbout.R;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        ImageView backImage = findViewById(R.id.aboutBackImage);
        backImage.setOnClickListener(v -> this.finish());
    }
}
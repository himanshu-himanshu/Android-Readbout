package com.finalproject.milestone_readbout.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.finalproject.milestone_readbout.R;

public class GeneralSettingsActivity extends AppCompatActivity {

    RadioGroup textRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_settings);
        ImageView backImage = findViewById(R.id.backImage);

        textRadioGroup = findViewById(R.id.textSizeRadioGroup);

        textRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbSmallText:
                        Toast.makeText(getApplicationContext(), "Small is selected", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.rbMediumText:
                        Toast.makeText(getApplicationContext(), "Medium is selected", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.rbLargeText:
                        Toast.makeText(getApplicationContext(), "Large is selected", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        return;
                }
            }
        });


        backImage.setOnClickListener(v -> {
            this.finish();
        });
    }
}
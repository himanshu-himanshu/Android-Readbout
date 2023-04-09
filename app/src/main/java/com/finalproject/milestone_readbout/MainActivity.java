package com.finalproject.milestone_readbout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.finalproject.milestone_readbout.ui.fragments.CategoriesFragment;
import com.finalproject.milestone_readbout.ui.fragments.SavedNewsFragment;
import com.finalproject.milestone_readbout.ui.fragments.SettingFragment;
import com.finalproject.milestone_readbout.ui.fragments.TrendingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences("READBOUT_PREF", MODE_PRIVATE);
        String loggedUserID = sharedpreferences.getString("loggedUserID", "");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        bottomNavigationView = findViewById(R.id.bottomTab);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new TrendingFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new TrendingFragment()).commit();
                        return true;
                    case R.id.categories:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new CategoriesFragment()).commit();
                        return true;
                    case R.id.saved:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SavedNewsFragment()).commit();
                        return true;
                    case R.id.setting:
                        SettingFragment settingFragment = new SettingFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("loggedUserID", loggedUserID);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        settingFragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.container, settingFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}
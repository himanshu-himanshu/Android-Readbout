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
import com.finalproject.milestone_readbout.utils.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    /** Variable Initialization */
    BottomNavigationView bottomNavigationView;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
        String loggedUserID = sharedpreferences.getString(Constants.LOGGED_USER_ID, "");

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        bottomNavigationView = findViewById(R.id.bottomTab);

        /** Transfer data to fragment using Bundle and FragmentTransaction */
        Bundle bundle = new Bundle();
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        bundle.putString(Constants.LOGGED_USER_ID, loggedUserID);
        TrendingFragment trendingFragment = new TrendingFragment();
        trendingFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container, trendingFragment).commit();

        /** Handle onClick listener on bottom tab bar */
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bundle bundle = new Bundle();
                FragmentTransaction fragmentTransaction;
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                bundle.putString(Constants.LOGGED_USER_ID, loggedUserID);

                switch (item.getItemId()) {
                    case R.id.home:
                        TrendingFragment trendingFragment = new TrendingFragment();
                        trendingFragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.container, trendingFragment).commit();
                        return true;
                    case R.id.categories:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new CategoriesFragment()).commit();
                        return true;
                    case R.id.saved:
                        SavedNewsFragment savedNewsFragment = new SavedNewsFragment();
                        savedNewsFragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.container, savedNewsFragment).commit();
                        return true;
                    case R.id.setting:
                        SettingFragment settingFragment = new SettingFragment();
                        settingFragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.container, settingFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}
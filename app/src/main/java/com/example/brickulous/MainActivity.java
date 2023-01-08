package com.example.brickulous;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.brickulous.Fragments.HomeFragment;
import com.example.brickulous.Fragments.MySetsFragment;
import com.example.brickulous.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_bar_main);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new HomeFragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment1 = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    fragment1 = new HomeFragment();
                    break;
                case R.id.nav_profile:
                    fragment1 = new ProfileFragment();
                    break;
                case R.id.nav_my_sets:
                    fragment1 = new MySetsFragment();
                    break;
            }
            assert fragment1 != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment1).commit();

            return true;
        });

    }

}
package com.example.arbird;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.arbird.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.navV.setOnItemSelectedListener(itemSelectListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.containerrrr, new CameraFr()).commit();
        int a = 0;
    }

    private NavigationBarView.OnItemSelectedListener itemSelectListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment=null;
            switch (item.getItemId()){
                case R.id.camerabt:
                    selectedFragment = new CameraFr();
                    break;
                case R.id.historybt:
                    selectedFragment = new HistoryFr();
                    break;
                case R.id.profilebt:
                    selectedFragment = new GPSFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.containerrrr, selectedFragment).commit();
            return true;
        }
    };

}
package com.example.arbird;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.arbird.AddressDataBase.AdressRepository;
import com.example.arbird.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AdressRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new AdressRepository(this);


        binding.navV.setOnItemSelectedListener(itemSelectListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.containerrrr, new ScanFragment()).commit();
        int a = 0;
    }


    private NavigationBarView.OnItemSelectedListener itemSelectListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment=null;
            switch (item.getItemId()){
                case R.id.scanbt:
                    selectedFragment = new ScanFragment();
                    break;
                case R.id.historybt:
                    selectedFragment = new HistoryFragment();
                    break;
                case R.id.profilebt:
                    selectedFragment = new ProfileFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.containerrrr, selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        repository.close();
    }
}
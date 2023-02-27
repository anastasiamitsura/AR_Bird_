package com.example.arbird;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arbird.databinding.FragmentCameraBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.Priority;


public class CameraFr extends Fragment  {

    private FragmentCameraBinding binding;
    int a = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentCameraBinding.inflate(inflater, container, false);
        binding.skan.setOnClickListener(view -> {
            a++;
            binding.countClick.setText(a + "");
        });
        return binding.getRoot();

    }

}
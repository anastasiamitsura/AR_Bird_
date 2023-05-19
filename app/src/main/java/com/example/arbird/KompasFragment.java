package com.example.arbird;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.example.arbird.databinding.FragmentCameraBinding;
import com.example.arbird.databinding.FragmentGPSBinding;
import com.example.arbird.databinding.FragmentKompasBinding;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;


public class KompasFragment extends Fragment{

    private FragmentKompasBinding binding;
    private AdressRepository repository;
    private final AdresAdapter adapter = new AdresAdapter();
    private final ItemTouchHelper.SimpleCallback swipeToDelete = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.ACTION_STATE_IDLE,
            ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(
                @NonNull RecyclerView recyclerView,
                @NonNull RecyclerView.ViewHolder viewHolder,
                @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            repository.removeByPosition(viewHolder.getAdapterPosition());
            adapter.removeItemByPosition(viewHolder.getAdapterPosition());
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = FragmentKompasBinding.inflate(inflater, container, false);
        repository = new AdressRepository(requireContext());
        binding.container.setAdapter(adapter);
        new ItemTouchHelper(swipeToDelete).attachToRecyclerView(binding.container);
        adapter.setData(repository.getAdresss());
        return binding.getRoot();
    }

}
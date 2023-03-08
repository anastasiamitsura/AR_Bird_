package com.example.arbird;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arbird.databinding.FragmentCameraBinding;
import com.example.arbird.databinding.FragmentGPSBinding;

import java.util.Date;


public class GPSFragment extends Fragment {

    //это я для себя пишу
    /**
     * LocationManager – (класс) обеспечивает доступ к системной службе определения местоположения Android;
     * LocationListener — (интерфейс) регламентирует обработку приложение событий службы определения местоположения Android;
     * Location – (класс) представляет географические координаты полученные от навигационной системы.*/

    private LocationManager locationManager;
    StringBuilder sbGPS = new StringBuilder();
    private FragmentGPSBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState
    ) {
        //обращение к сервису определения локации
        locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        binding = FragmentGPSBinding.inflate(inflater, container, false);
        //переход в настройки разрешений приложений к gps
        binding.btnLocationSettings.setOnClickListener(view -> {
            startActivity(new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        });
        return binding.getRoot();
    }
    //обновление данных о позиции
    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //я не знаю зачем это надо, но видимо для какого то разрешения это жизнено необходимо
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 10, 10, locationListener);
        checkEnabled();
    }

    //остановка считывания геопозиции
    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    //я так понимаю это обработка всего связаного с изменение или считывание позиции
    private LocationListener locationListener = new LocationListener() {

        //если локация изменилась и тп
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        //проверка доступа к провайдеру
        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }

        //забить на наличие разрешений
        @SuppressLint("MissingPermission")
        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            showLocation(locationManager.getLastKnownLocation(provider));
        }

        //проврека работоспоcобности провайдера
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                binding.tvStatusGPS.setText("Status: " + String.valueOf(status));
            }
        }
    };

    //вывод местоположения
    private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            binding.tvLocationGPS.setText(formatLocation(location));
        }
    }

    //форматирование местоположеня и времяни
    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "Coordinates: lat = %1$.14f, lon = %2$.14f, time = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(
                        location.getTime()));
    }

    //вывод проверки рабоспособности провайдера
    private void checkEnabled() {
        binding.tvEnabledGPS.setText("Enabled: "
                + locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER));
    }



}
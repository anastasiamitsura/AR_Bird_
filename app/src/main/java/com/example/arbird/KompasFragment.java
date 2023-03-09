package com.example.arbird;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.example.arbird.databinding.FragmentGPSBinding;
import com.example.arbird.databinding.FragmentKompasBinding;

import java.util.Date;


public class KompasFragment extends Fragment implements SensorEventListener{

    private FragmentKompasBinding binding;
    private SensorManager sensorManager;
    Location mMyLocation = new Location("MyLocation");
    Location mKabaLocation = new Location("Kaba");
    private final float kabaSharifLongitude = 39.8261f;
    private final float kebaSharifLatitude = 21.4225f;
    private float RotateDegree = 0f;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentKompasBinding.inflate(inflater, container, false);
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager
                        .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_UI);

        sensorManager.registerListener(this, sensorManager
                        .getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
        return binding.getRoot();
    }

    float [] mGravity;
    float [] mGeomagnetic;
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            if (SensorManager.getRotationMatrix(R, I,
                    mGravity, mGeomagnetic)) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                float degree = (float) Math.toDegrees(orientation[0]);
                float actualAngle = getBearing(degree);
                // rotate your compass image by actualAngle
                binding.textC.setText("Отклонение от севера: " + Float.toString(degree) + " градусов");
                //Создаем анимацию вращения:
                RotateAnimation rotateAnimation = new RotateAnimation(
                        RotateDegree,
                        -degree,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f);

                //Продолжительность анимации в миллисекундах:
                rotateAnimation.setDuration(200);

                //Настраиваем анимацию после завершения подсчетных действий датчика:
                rotateAnimation.setFillAfter(true);

                //Запускаем анимацию:
                binding.imgC.startAnimation(rotateAnimation);
                RotateDegree = -degree;
            }
        }
    }
    GeomagneticField mGeoMag = null;
    public void onResume() {
        super.onResume();
        mGeoMag = new GeomagneticField(kebaSharifLatitude, kabaSharifLongitude, 0, System.currentTimeMillis());

    }
    public float getBearing(float heading) {
        if (mGeoMag == null) return heading;
        heading -= mGeoMag.getDeclination();
        return mMyLocation.bearingTo(mKabaLocation) - heading;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
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
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arbird.databinding.FragmentCameraBinding;
import com.example.arbird.databinding.FragmentKompasBinding;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CameraFr extends Fragment implements SensorEventListener{


    private AdressRepository repository1;
    private final AdresAdapter adapter1 = new AdresAdapter();
    private FragmentCameraBinding binding;
    private final PlaceAdapter adapter = new PlaceAdapter();
    private final PlaceRepository repository = new PlaceRepository();



    Geocoder geocoder;
    List<Address> addresses;
    private SensorManager sensorManager;
    Location mMyLocation = new Location("MyLocation");
    Location mKabaLocation = new Location("Kaba");
    private final float kabaSharifLongitude = 39.8261f;
    private final float kebaSharifLatitude = 21.4225f;
    private LocationManager locationManager;
    Location locationNow;
    double latityde;
    double longtyde;
    float degreeNow;
    private float RotateDegree = 0f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        geocoder = new Geocoder(requireContext(), Locale.getDefault());
        binding = FragmentCameraBinding.inflate(inflater, container, false);
        repository1 = new AdressRepository(requireContext());
        int permissionStatus = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {

        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        binding.skan.setOnClickListener(view -> {
            checkNapraw(locationNow);
            goSearch(latityde, longtyde);
            binding.locaT.setText(formatLocation(locationNow));
        });
        binding.recycler.setAdapter(adapter);
        repository.setOnLoadingPlaceState(state -> {
            if(state instanceof OnLoadingPlaceState.State.Success){
                onUpdateData((OnLoadingPlaceState.State.Success)state);
            }
        });

        binding.getadress.setOnClickListener(view -> {
            if(locationNow != null){
                try {
                    getAdress();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(getActivity(), "Ваше местоположение ещё не опрелено, подождите немного",
                        Toast.LENGTH_LONG).show();
            }
        });
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager
                        .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_UI);

        sensorManager.registerListener(this, sensorManager
                        .getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
        return binding.getRoot();

    }
    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        // PERMISSION GRANTED
                    } else {
                        Toast.makeText(getActivity(), "Без данных разрешений приложение не сможет работать",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
    );

    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(
                        location.getTime()));
    }


    private void onUpdateData(OnLoadingPlaceState.State.Success state) {
        adapter.setItems(state.getPlaces());
    }

    //TODO: вроде так должно работать, но это не точно
    private void goSearch(double latityde, double longtyde) {
        if(latityde == 0 && longtyde == 0){
            Toast.makeText(getActivity(), "Ваше местоположение ещё не опрелено, подождите немного",
                    Toast.LENGTH_LONG).show();
        }
        else{
            String[] categorys = {"красота", "магазин", "фитнес", "авто", "кафе", "ресторан", "аптека"};
            for (int i = 0; i < categorys.length; i++) {
                repository.search(latityde, longtyde, categorys[i]);
            }

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        repository.removeOnLoadingPlaceState();
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
                degreeNow =  degree;

                RotateDegree = -degree;
            }
        }
    }
    GeomagneticField mGeoMag = null;
    public float getBearing(float heading) {
        if (mGeoMag == null) return heading;
        heading -= mGeoMag.getDeclination();
        return mMyLocation.bearingTo(mKabaLocation) - heading;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    @Override
    public void onResume() {
        super.onResume();
        mGeoMag = new GeomagneticField(kebaSharifLatitude, kabaSharifLongitude, 0, System.currentTimeMillis());
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
            locationNow = location;
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
            locationNow =  locationManager.getLastKnownLocation(provider);

        }

        //проврека работоспоcобности провайдера
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                //binding.tvStatusGPS.setText("Status: " + String.valueOf(status));
            }
        }
    };

    //вывод местоположения
    private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
       //     binding.tvLocationGPS.setText(formatLocation(location));
        }
    }


    //вывод проверки рабоспособности провайдера
    private void checkEnabled() {
    }

    private  void checkNapraw(Location location){
        latityde = location.getLatitude();
        longtyde = location.getLongitude();
        Log.e(latityde + "", longtyde + "");
        if (degreeNow >= -22.5 && degreeNow <= 22.5){
            latityde += 0.0004;
        }
        else if (degreeNow >= 22.5 && degreeNow <= 67.5){
            latityde += 0.0004;
            longtyde += 0.0004;
        }
        else if (degreeNow >= 67.5 && degreeNow <= 112.5){
            longtyde += 0.0004;
        }
        else if (degreeNow >= 112.5 && degreeNow <= 157.5){
            latityde -= 0.0004;
            longtyde += 0.0004;
        }
        else if (degreeNow >= 157.5 && degreeNow <= -157.5){
            latityde -= 0.0004;
        }
        else if (degreeNow >= -157.5 && degreeNow <= -112.5){
            latityde -= 0.0004;
            longtyde -= 0.0004;
        }
        else if (degreeNow >= -112.5 && degreeNow <= -67.5){
            longtyde -= 0.0004;
        }
        else{
            latityde += 0.0004;
            longtyde -= 0.0004;
        }
        Log.e(latityde + "", longtyde + "");
    }

    private void getAdress() throws IOException {
        checkNapraw(locationNow);
        addresses = geocoder.getFromLocation(latityde, longtyde, 1);
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        int iend = address.indexOf(",");
        String subString = "";
        if (iend != -1)
        {
            subString = address.substring(0 , iend);
        }
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        binding.AdresT.setText(address);
        repository1.addAdress(
                new AdresData(
                        subString,
                        "Город:" + city,
                        "Страна:" + country,
                        "Почтовый индес: " + postalCode,
                        "Название: " + knownName
                )
        );
        adapter1.setData(repository1.getAdresss());
    }

}
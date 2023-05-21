package com.example.arbird;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.arbird.AddressDataBase.AdresAdapter;
import com.example.arbird.AddressDataBase.AdresData;
import com.example.arbird.AddressDataBase.AdressRepository;
import com.example.arbird.PlacesFiles.PlaceAdapter;
import com.example.arbird.databinding.FragmentScanBinding;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ScanFragment extends Fragment implements SensorEventListener {

    private FragmentScanBinding binding;

    public static int countScan = 0;
    SharedPreferences sPref;
    final String SAVED_SCAN_COUNT = "saved_scan_count";

    private AdressRepository repositoryAddress;
    private final AdresAdapter adapterAddress = new AdresAdapter();

    private final PlaceAdapter adapter = new PlaceAdapter();
    private final PlaceRepository repository = new PlaceRepository();

    Geocoder geocoder;
    List<Address> addresses;
    private SensorManager sensorManager;
    Location mMyLocation = new Location("MyLocation");
    private LocationManager locationManager;
    Location locationNow;
    double latitude;
    double longitude;
    float degreeNow;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        geocoder = new Geocoder(requireContext(), Locale.getDefault());
        binding = FragmentScanBinding.inflate(inflater, container, false);
        repositoryAddress = new AdressRepository(requireContext());
        int permissionStatus = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {

        }
        else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        binding.skan.setOnClickListener(view -> {
            if (locationNow != null) {
                checkNapraw(locationNow);
                goSearch(latitude, longitude);
                countScan++;
            } else {
                Toast.makeText(getActivity(), "Ваше местоположение ещё не опрелено, подождите немного",
                        Toast.LENGTH_LONG).show();
            }
        });
        binding.recycler.setAdapter(adapter);
        repository.setOnLoadingPlaceState(state -> {
            if (state instanceof OnLoadingPlaceState.State.Success) {
                onUpdateData((OnLoadingPlaceState.State.Success) state);
            }
        });

        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager
                        .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_UI);

        sensorManager.registerListener(this, sensorManager
                        .getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
        loadCount();
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



    private void onUpdateData(OnLoadingPlaceState.State.Success state) {
        adapter.setItems(state.getPlaces());
    }

    private void goSearch(double latitude, double longitude) {
        if (latitude == 0 && longitude == 0) {
            Toast.makeText(getActivity(), "Ваше местоположение ещё не опрелено, подождите немного",
                    Toast.LENGTH_LONG).show();
        } else {
            repository.search(latitude, longitude);
            try {
                getAdress();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    float[] mGravity;
    float[] mGeomagnetic;

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
                degreeNow = degree;
            }
        }
    }

    GeomagneticField mGeoMag = null;

    public float getBearing(float heading) {
        if (mGeoMag == null) return heading;
        heading -= mGeoMag.getDeclination();
        return mMyLocation.bearingTo(locationNow) - heading;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mGeoMag = new GeomagneticField((float) latitude, (float) longitude, 0, System.currentTimeMillis());
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //я не знаю зачем это надо, но видимо для какого то разрешения это жизнено необходимо
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 10, 10, locationListener);
        checkEnabled();
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }


    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            locationNow = location;
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }

        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                return;
            }
            locationNow = locationManager.getLastKnownLocation(provider);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
            }
        }
    };



    //вывод проверки рабоспособности провайдера
    private void checkEnabled() {
    }

    private  void checkNapraw(Location location){
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.e(latitude + "", longitude + "");
        if (degreeNow >= -22.5 && degreeNow <= 22.5){
            latitude += 0.0004;
        }
        else if (degreeNow >= 22.5 && degreeNow <= 67.5){
            latitude += 0.0004;
            longitude += 0.0004;
        }
        else if (degreeNow >= 67.5 && degreeNow <= 112.5){
            longitude += 0.0004;
        }
        else if (degreeNow >= 112.5 && degreeNow <= 157.5){
            latitude -= 0.0004;
            longitude += 0.0004;
        }
        else if (degreeNow >= 157.5 && degreeNow <= -157.5){
            latitude -= 0.0004;
        }
        else if (degreeNow >= -157.5 && degreeNow <= -112.5){
            latitude -= 0.0004;
            longitude -= 0.0004;
        }
        else if (degreeNow >= -112.5 && degreeNow <= -67.5){
            longitude -= 0.0004;
        }
        else{
            latitude += 0.0004;
            longitude -= 0.0004;
        }
        Log.e(latitude + "", longitude + "");
    }

    private void getAdress() throws IOException {
        addresses = geocoder.getFromLocation(latitude, longitude, 1);
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        int iend = address.indexOf(",");
        String subString = "";
        if (iend != -1)
        {
            subString = address.substring(0 , iend);
        }
        String city = addresses.get(0).getLocality();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        binding.Addrestxt.setText("Адрес: " + address);
        repositoryAddress.addAdress(
                new AdresData(
                        subString,
                        "Город:" + city,
                        "Страна:" + country,
                        "Почтовый индес: " + postalCode,
                        "Название: " + knownName
                )
        );
        adapterAddress.setData(repositoryAddress.getAdresss());
    }

    private void saveCount() {
        sPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt(SAVED_SCAN_COUNT, countScan);
        ed.commit();
    }

    private void loadCount() {
        sPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int scanCount = sPref.getInt(SAVED_SCAN_COUNT, 0);
        countScan = scanCount;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        repository.removeOnLoadingPlaceState();
        saveCount();
    }

}
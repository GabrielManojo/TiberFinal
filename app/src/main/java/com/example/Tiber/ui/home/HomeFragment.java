package com.example.Tiber.ui.home;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.Tiber.R;
import com.example.Tiber.data.SQLite.DbHandler;
import com.example.Tiber.data.account.Driver;
import com.example.Tiber.data.vehicle.Truck;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {

    private MapView mapView;
    private GoogleMap myMap;
    private final int ACCESS_LOCATION_REQUEST_CODE = 1001;
    FusedLocationProviderClient fusedLocationProviderClient;    //API to manage location request
    private LocationRequest locationRequest;    //hold location request
    Marker deviceLocationMarker;    //vehicle icon on map
    Circle deviceLocationAccurancyCircle; //vehicle icon setup
    private EditText addressInput;
    private Button btnShowOnMap;
    private LatLng currentLocation;
    private Marker addressMarker; // Marker for the address
    private Polyline currentPolyline; // Reference to the current Polyline

    public Driver driver = new Driver(null, null);
    public Truck truck = new Truck();

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the map element by its ID
        mapView = view.findViewById(R.id.mapView);
        addressInput = view.findViewById(R.id.addressInput);
        btnShowOnMap = view.findViewById(R.id.btnShowOnMap);

        // Initialize the MapView
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        //Initialize Google Location API
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        //Initialize location request
        locationRequest = new LocationRequest.Builder(6_000)
                .setMinUpdateIntervalMillis(500)
                .setMinUpdateDistanceMeters(1)
                .setPriority(android.location.LocationRequest.QUALITY_HIGH_ACCURACY)
                .build();

        //load driver's details on Home Fragment's Driver Class
        SetAccountDetails();

        //load vehicle's details on Home Fragment's Truck Class
        SetVehicleDetails();

        // Set up the button click listener
        btnShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = addressInput.getText().toString();
                if (!address.isEmpty()) {
                    showAddressOnMap(address);
                } else {
                    Toast.makeText(getContext(), "Please enter an address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {

        myMap = map;

        //Checking permission to get location
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //map configuration
            myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);    //default map (normal)
            myMap.setOnMapLongClickListener(this);  //to perform actions for user's long click on map
            myMap.setOnMarkerDragListener(this);    // to set a listener that responds to events related to dragging markers on map
            //myMap.setMyLocationEnabled(true);   //to create a button on the top right corner of the map to focus on device's location

            //method to check settings and start location updates
            checkSettingsAndStartLocationUpdates();

        }
        //If not authorized, Request user's permission to access device location
        else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_LOCATION_REQUEST_CODE);
        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_LOCATION_REQUEST_CODE);
        }
    }

    private void checkSettingsAndStartLocationUpdates() {

        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(getActivity());

        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                //settings of device are satisfied and start location updates
                startLocationUpdates();
            }
        });

        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if (e instanceof ResolvableApiException) {
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(getActivity(), ACCESS_LOCATION_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    //location Callback
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {

            if (locationResult == null){

                return;
            }
            super.onLocationResult(locationResult);

            Log.d(TAG, "onLocationResult " + locationResult.getLastLocation());

            //call method to set device location marker
            if (myMap != null){

                setDeviceLocationMarker(locationResult.getLastLocation());
            }
        }
    };

    //method to start location updates
    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    //method to stop location updates. Called when map destroyed
    private void stopLocationUpdates() {

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void setDeviceLocationMarker(Location location){

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        currentLocation = latLng; // Save the current location

        if(deviceLocationMarker == null){

            //create a new marker
            MarkerOptions markerOptions = new MarkerOptions();    //vehicle icon setup
            markerOptions.position(latLng);
            markerOptions.rotation(location.getBearing());
            markerOptions.anchor((float) 0.5, (float) 0.5);

            switch (driver.getDriverStatus()){

                case 0:
                    //unavailable
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.black_car));

                    break;

                case 1:
                    //available
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.black_car));

                    break;

                default:
                    //do something
                    break;
            }

            deviceLocationMarker = myMap.addMarker(markerOptions);
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
        }
        else{
            //use the previously created marker
            deviceLocationMarker.setPosition(latLng);
            deviceLocationMarker.setRotation(location.getBearing());
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
        }

        if (deviceLocationAccurancyCircle == null){
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.strokeWidth(2);
            circleOptions.strokeColor(Color.TRANSPARENT);
            circleOptions.fillColor(Color.TRANSPARENT);
            circleOptions.radius(location.getAccuracy());
            deviceLocationAccurancyCircle = myMap.addCircle(circleOptions);
        }
        else{
            deviceLocationAccurancyCircle.setCenter(latLng);
            deviceLocationAccurancyCircle.setRadius(location.getAccuracy());
        }

        //Update latitude and longitude at Truck Class
        truck.setLatitude(String.valueOf(location.getLatitude()));
        truck.setLongitude(String.valueOf(location.getLongitude()));

        //calling method to transmit data
        TransmitDataToServer();
    }

    //get driver account details and set them into Driver Class
    public void SetAccountDetails() {

        //Instantiating DbHandler
        com.example.Tiber.data.SQLite.DbHandler dbHandler = new com.example.Tiber.data.SQLite.DbHandler(getActivity());

        //get opened session (equal 1) on app by account id
        int accountId = dbHandler.getOpenedSession();

        //get account details
        String[] driverData;
        driverData = dbHandler.getAccountDetails(accountId);

        //close dbHandler
        dbHandler.close();

        driver.setAccountId(Integer.parseInt(driverData[0]));
        driver.setFirstName(driverData[1]);
        driver.setLastName(driverData[2]);
        driver.setPhone(Long.parseLong(driverData[3]));
        driver.setEmail(driverData[4]);
        driver.setActiveAccount(Integer.parseInt(driverData[6]));
        driver.setDriverStatus(Integer.parseInt(driverData[7]));
    }

    //method to get vehicle details and set them into Truck Class
    public void SetVehicleDetails() {
        // Instantiating DbHandler
        DbHandler dbHandler = new DbHandler(getActivity());

        // Get vehicle details
        String[] vehicleData = dbHandler.getVehicleDetails(driver.getAccountId());

        // Close dbHandler
        dbHandler.close();

        // Check if vehicleData is not empty
        if (vehicleData[0].isEmpty()) {
            Log.d("HomeFragment", "No vehicle data available for driver with account ID: " + driver.getAccountId());
            // Handle the case where vehicle data is not available
            // For example, you might want to display a message to the user
        } else {
            truck.setVehicleId(Integer.parseInt(vehicleData[0]));
            truck.setPlate(vehicleData[1]);
            truck.setVin(vehicleData[2]);
            truck.setManufacturer(vehicleData[3]);
            truck.setModel(vehicleData[4]);
            truck.setVehicleColor(vehicleData[5]);
            truck.setCapacity(vehicleData[6]);
            truck.setOwnerAccountNumber(Integer.parseInt(vehicleData[8]));
        }
    }

    public void ResetMarkerOption() {
        // Implement this method if needed
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        stopLocationUpdates();

        deviceLocationMarker = null;

        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopLocationUpdates();

        deviceLocationMarker = null;

        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        // Implement this method if needed
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {
        // Implement this method if needed
    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        // Implement this method if needed
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {
        // Implement this method if needed
    }

    //method to transmit data to the server
    public void TransmitDataToServer(){
        /*
        Specifically for this project, sending data to the server is not included in the scope of the application and, therefore,
        the data will only be logged in app.
        */

        String dataQueryT;

        dataQueryT = driver.getAccountId() +
                ", " + driver.getFirstName() +
                ", " + driver.getLastName() +
                ", " + driver.getPhone() +
                ", " + driver.getEmail() +
                ", " + driver.getDriverStatus();

        Log.e("Data Transmission (account)", dataQueryT);

        dataQueryT = truck.getVehicleId() +
                ", " + truck.getPlate() +
                ", " + truck.getOwnerAccountNumber() +
                ", " + truck.getVin() +
                ", " + truck.getManufacturer() +
                ", " + truck.getModel() +
                ", " + truck.getVehicleColor() +
                ", " + truck.getCapacity() +
                ", " + truck.getLatitude() +
                ", " + truck.getLongitude();

        Log.e("Data Transmission (vehicle)", dataQueryT);

    }

    private void showAddressOnMap(String address) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                // Remove previous address marker and polyline
                if (addressMarker != null) {
                    addressMarker.remove();
                }
                if (currentPolyline != null) {
                    currentPolyline.remove();
                }

                addressMarker = myMap.addMarker(new MarkerOptions().position(latLng).title(address));
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                Log.d(TAG, "Location found: " + latLng.toString());

                // Draw route
                drawRoute(currentLocation, latLng);
            } else {
                Toast.makeText(getContext(), "Address not found", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "No location found for the address");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error finding address", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Geocoder IOException: " + e.getMessage());
        }
    }

    private void drawRoute(LatLng origin, LatLng destination) {
        String url = getDirectionsUrl(origin, destination);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = new URL(url).openStream();
                    StringBuffer buffer = new StringBuffer();
                    Scanner scanner = new Scanner(inputStream);
                    while (scanner.hasNext()) {
                        buffer.append(scanner.nextLine());
                    }
                    scanner.close();
                    inputStream.close();

                    JSONObject jsonObject = new JSONObject(buffer.toString());
                    JSONArray routes = jsonObject.getJSONArray("routes");
                    JSONObject route = routes.getJSONObject(0);
                    JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                    String encodedPolyline = overviewPolyline.getString("points");


                    List<LatLng> points = PolyUtil.decode(encodedPolyline);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PolylineOptions polylineOptions = new PolylineOptions()
                                    .addAll(points)
                                    .color(Color.BLUE)
                                    .width(10);
                            currentPolyline = myMap.addPolyline(polylineOptions);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error drawing route: " + e.getMessage());
                }
            }
        }).start();
    }

    private String getDirectionsUrl(LatLng origin, LatLng destination) {
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        String strDest = "destination=" + destination.latitude + "," + destination.longitude;
        String mode = "mode=driving";
        String parameters = strOrigin + "&" + strDest + "&" + mode + "&key=" + getString(R.string.google_maps_api_key);
        return "https://maps.googleapis.com/maps/api/directions/json?" + parameters;
    }
}

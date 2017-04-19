package id.ac.its.driverezlyn.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import id.ac.its.driverezlyn.R;

public class Track extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    MarkerOptions markerOptions;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    Marker angkot,halte1,halte2;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    DatabaseReference mFirebaseDatabase;
    FirebaseDatabase mFirebaseInstance;
    double lati,lngi;
    boolean locsetted=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        mapFrag  = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseInstance.getReference("angkot").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String[] lokasi = dataSnapshot.getValue(String.class).split(",");
                lati = Double.parseDouble(lokasi[0]);
                lngi = Double.parseDouble(lokasi[1]);
                moveangkot();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });
        Button batal = (Button) findViewById(R.id.button2);
        batal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Track.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void moveangkot(){
        if (angkot != null) {
            angkot.remove();
        }
        markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(lati,lngi));
        markerOptions.title("Angkot WK");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_angkot));
        angkot = mGoogleMap.addMarker(markerOptions);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lati,lngi)));
        if(!locsetted){
            locsetted=true;mapsetted(lati,lngi,-7.279890,112.784973);
        }
    }

    public void mapsetted(double lat,double lng,double lat2,double lng2){
        LatLng awal = new LatLng(lat, lng);
        LatLng tujuan = new LatLng(lat2, lng2);
        GoogleDirection.withServerKey(getResources().getString(R.string.googlegeneralkey))
                .from(awal)
                .to(tujuan)
                .avoid(AvoidType.TOLLS)
                .avoid(AvoidType.FERRIES)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if(direction.isOK()) {
                            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
                            String distance = direction.getRouteList().get(0).getLegList().get(0).getDistance().getText();
                            String duration = direction.getRouteList().get(0).getLegList().get(0).getDuration().getText();
//                            textView.setText("Jarak: "+distance+"\nWaktu: "+duration);
                            mGoogleMap.addPolyline(DirectionConverter.createPolyline(Track.this, directionPositionList, 5, ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null)));
                        } else {
                            Log.d("mapse", rawBody);
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Log.d("mapse", t.toString());
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }

        markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(-7.279890,112.784973));
        markerOptions.title("Halte 1");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_halte));
        halte1 = mGoogleMap.addMarker(markerOptions);

        markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(-7.279337,112.789393));
        markerOptions.title("Halte 2");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_halte));
        halte2 = mGoogleMap.addMarker(markerOptions);

        mapsetted(-7.279890,112.784973,-7.279337,112.789393);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mFirebaseInstance.getReference("angkot").setValue(location.getLatitude()+","+location.getLongitude());
    }


}

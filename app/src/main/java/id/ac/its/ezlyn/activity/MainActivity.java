package id.ac.its.ezlyn.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.TextView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.ac.its.ezlyn.R;
import id.ac.its.ezlyn.Tutorial_fix;
import id.ac.its.ezlyn.model.Halte;
import id.ac.its.ezlyn.new_tab;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    MarkerOptions markerOptions;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location myloc;
    Halte halte;
    List<Halte> haltes;
    boolean locsetted = false;
    DatabaseReference databaseHalte;
    Polyline polyline;
    TextView jumlah, nama, jarak;
    PolylineOptions[] map_poli = new PolylineOptions[10];
    String[] map_distance = new String[10];
    Handler mHandler;
    Runnable mAnimation;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.konfirmasi)
    Button konfirmasi;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        statusCheckInt();
        statusCheckGPS();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryText, null));
        databaseHalte = FirebaseDatabase.getInstance().getReference("halte");
        mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        Log.d("STATE", "Google map inisiasi");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
            Log.d("STATE", "Sudah enable location");
        }

        mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.infohalte, null);
                nama = (TextView) v.findViewById(R.id.nama);
                jarak = (TextView) v.findViewById(R.id.jarak);
                jumlah = (TextView) v.findViewById(R.id.jumlah);
                nama.setText(marker.getTitle());
                for (Halte h : haltes) {
                    if (h.getName().contentEquals(marker.getTitle())) {
                        int index = haltes.indexOf(h);
                        halte=h;
                        jumlah.setText(h.getWaiting() + " penunggu");
                        jarak.setText(map_distance[index]);
                        if (polyline != null) {
                            polyline.remove();
                        }
                        polyline = mGoogleMap.addPolyline(map_poli[index]);
                        break;
                    }
                }
                Log.d("STATE", "Harusnya disini sudah jalan");
                return v;
            }
        });

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (polyline != null) {
                    polyline.remove();
                }
                konfirmasi.setVisibility(View.GONE);
            }
        });
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                mHandler = new Handler();
                final long start = SystemClock.uptimeMillis();
                final long duration = 400L;
                mHandler.removeCallbacks(mAnimation);
                mAnimation = new BounceAnimation(start, duration, marker, mHandler);
                mHandler.post(mAnimation);
                konfirmasi.setVisibility(View.VISIBLE);
                marker.showInfoWindow();
                return false;
            }
        });
    }

    @OnClick(R.id.konfirmasi)
    public void onViewClicked() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Konfirmasi");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                databaseHalte.child(halte.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int waiting = dataSnapshot.getValue(Halte.class).getWaiting();
                        waiting=waiting+1;
                        databaseHalte.child(halte.getName()).child("waiting").setValue(waiting);
                    }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                });
                Intent intent = new Intent(MainActivity.this, LynListActivity.class);
                intent.putExtra("halte", Parcels.wrap(halte));
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    private static class BounceAnimation implements Runnable {

        private final long mStart, mDuration;
        private final Interpolator mInterpolator;
        private final Marker mMarker;
        private final Handler mHandler;

        private BounceAnimation(long start, long duration, Marker marker, Handler handler) {
            mStart = start;
            mDuration = duration;
            mMarker = marker;
            mHandler = handler;
            mInterpolator = new BounceInterpolator();
        }

        @Override
        public void run() {
            long elapsed = SystemClock.uptimeMillis() - mStart;
            float t = Math.max(1 - mInterpolator.getInterpolation((float) elapsed / mDuration), 0f);
            mMarker.setAnchor(0.5f, 1.0f + 0.5f * t);

            if (t > 0.0) {
                // Post again 16ms later.
                mHandler.postDelayed(this, 16L);
            }
        }
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
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        myloc = location;
        if (!locsetted) {
            final LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(new LatLng(myloc.getLatitude(), myloc.getLongitude()));
            haltes = new ArrayList<>();
            locsetted = true;
            databaseHalte.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        halte = dsp.getValue(Halte.class);
                        haltes.add(halte);
                        final int index = haltes.indexOf(halte);
                        markerOptions = new MarkerOptions();
                        LatLng halteloc = new LatLng(halte.getLat(), halte.getLng());
                        markerOptions.position(halteloc);
                        builder.include(halteloc);
                        markerOptions.title(halte.getName());
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_halte_k));
                        mGoogleMap.addMarker(markerOptions);
                        GoogleDirection.withServerKey(getResources().getString(R.string.googlegeneralkey))
                                .from(new LatLng(myloc.getLatitude(), myloc.getLongitude()))
                                .to(halteloc)
                                .unit(Unit.IMPERIAL)
                                .transitMode(TransportMode.WALKING)
                                .avoid(AvoidType.TOLLS)
                                .avoid(AvoidType.FERRIES)
                                .execute(new DirectionCallback() {
                                    @Override
                                    public void onDirectionSuccess(Direction direction, String rawBody) {
                                        if (direction.isOK()) {
                                            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
                                            //duration = Double.parseDouble(direction.getRouteList().get(0).getLegList().get(0).getDuration().getText().replace(" mins",""));
                                            map_distance[index] = direction.getRouteList().get(0).getLegList().get(0).getDistance().getText();
                                            map_poli[index] = DirectionConverter.createPolyline(MainActivity.this, directionPositionList, 5, ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
                                        } else {
                                            Log.d("mapse", rawBody);
                                            // Do something
                                        }
                                    }

                                    @Override
                                    public void onDirectionFailure(Throwable t) {
                                        Log.d("mapse", t.toString());
                                    }
                                });
                    }
                    LatLngBounds bounds = builder.build();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                    mGoogleMap.moveCamera(cu);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("Oye", "Failed to read value.", error.toException());
                }
            });
        }
    }

    public void statusCheckInt() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected()){}
            else buildAlertMessageNoInt();
        }else buildAlertMessageNoInt();
    }

    public void statusCheckGPS() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Mohon aktifkan GPS Anda")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void buildAlertMessageNoInt() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Mohon aktifkan Internet Anda")
                .setCancelable(false)
                .setPositiveButton("Data", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName(
                                "com.android.settings",
                                "com.android.settings.Settings$DataUsageSummaryActivity"));
                        startActivity(intent);
                    }
                }).setNegativeButton("Wifi", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void GoTutor(){
        Intent intent = new Intent(MainActivity.this, new_tab.class);
        startActivity(intent);
        finish();
    }
}

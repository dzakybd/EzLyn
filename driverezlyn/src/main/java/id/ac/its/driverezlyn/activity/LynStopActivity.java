package id.ac.its.driverezlyn.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.Switch;
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
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.ac.its.driverezlyn.R;
import id.ac.its.driverezlyn.model.Halte;
import id.ac.its.driverezlyn.model.Lyn;

public class LynStopActivity extends AppCompatActivity implements
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
    boolean penuhbool,kerjabool;
    DatabaseReference databaseHalte,databaseLyn;
    Polyline polyline;
    TextView jumlah, nama, jarak;
    PolylineOptions[] map_poli = new PolylineOptions[10];
    String[] map_distance = new String[10];
    String[] map_duration = new String[10];
    Handler mHandler;
    Runnable mAnimation;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.kerja)
    Switch kerja;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nama_halte)
    TextView namaHalte;
    @BindView(R.id.jarak_halte)
    TextView jarakHalte;
    @BindView(R.id.waktu_halte)
    TextView waktuHalte;
    @BindView(R.id.jumlah_halte)
    TextView jumlahHalte;
    @BindView(R.id.cardhalte)
    CardView cardhalte;
    @BindView(R.id.penuh)
    Button penuh;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyn_stop);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryText, null));
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName("EzLyn")
                .setUseDefaultSharedPreference(true)
                .build();
        databaseLyn = FirebaseDatabase.getInstance().getReference("lyn");
        databaseLyn.child(Prefs.getString("plat","")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Lyn.class).isFull()){
                    penuh.setText("Angkot penuh");
                    penuhbool=true;
                }
                else{
                    penuh.setText("Angkot tersedia");
                    penuhbool=false;
                }

                if(dataSnapshot.getValue(Lyn.class).isStatus()){
                    kerjabool=true;
                    kerja.setChecked(true);
                }
                else{
                    penuh.setText("Anda sedang istirahat");
                    kerjabool=false;
                    kerja.setChecked(false);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        databaseHalte = FirebaseDatabase.getInstance().getReference("halte");
        databaseLyn = FirebaseDatabase.getInstance().getReference("lyn");
        mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.driver, menu);
        menu.findItem(R.id.out).setIcon(
                new IconicsDrawable(this)
                        .icon(GoogleMaterial.Icon.gmd_exit_to_app)
                        .color(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryText, null))
                        .actionBar()
        );
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.out) {
            out();
        }
        return super.onOptionsItemSelected(item);
    }

    private void out() {
        AlertDialog.Builder pilihan = new AlertDialog.Builder(this);
        pilihan.setMessage("Anda ingin mengganti data?");
        pilihan.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseLyn.child(Prefs.getString("plat","")).removeValue();
                Prefs.clear();
                startActivity(new Intent(LynStopActivity.this, RegistrationActivity.class));
                finish();
            }
        });
        pilihan.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alert = pilihan.create();
        alert.show();
    }


    @OnClick({R.id.penuh,R.id.kerja})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.kerja:
                databaseLyn.child(Prefs.getString("plat","")).child("status").setValue(!kerjabool);
                progressDialog = new ProgressDialog(LynStopActivity.this);
                progressDialog.setMessage("Memproses");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        if (kerjabool) {
                            penuh.setText("Anda sedang istirahat");
                            kerjabool=false;
                            kerja.setChecked(false);
                        } else {
                            if(penuhbool){
                                penuh.setText("Angkot penuh");
                            }
                            else{
                                penuh.setText("Angkot tersedia");
                            }
                            kerjabool=true;
                            kerja.setChecked(true);
                        }
                    }
                }, 2000);
                break;
            case R.id.penuh:
                if(kerjabool){
                    databaseLyn.child(Prefs.getString("plat","")).child("full").setValue(!penuhbool);
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Memproses");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            if(penuhbool){
                                penuhbool=false;
                                penuh.setText("Angkot tersedia");
                            }
                            else{
                                penuhbool=true;
                                penuh.setText("Angkot penuh");
                            }
                        }
                    }, 2000);
                }
                break;
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
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
                namaHalte.setText(marker.getTitle());
                jarakHalte.setCompoundDrawables(
                        new IconicsDrawable(LynStopActivity.this)
                                .icon(GoogleMaterial.Icon.gmd_directions_car)
                                .color(ResourcesCompat.getColor(getResources(), R.color.colorSecondaryText, null))
                                .actionBar(),
                        null, null, null );
                waktuHalte.setCompoundDrawables(
                        new IconicsDrawable(LynStopActivity.this)
                                .icon(GoogleMaterial.Icon.gmd_av_timer)
                                .color(ResourcesCompat.getColor(getResources(), R.color.colorSecondaryText, null))
                                .actionBar(),
                        null, null, null );
                jumlahHalte.setCompoundDrawables(
                        new IconicsDrawable(LynStopActivity.this)
                                .icon(GoogleMaterial.Icon.gmd_group)
                                .color(ResourcesCompat.getColor(getResources(), R.color.colorSecondaryText, null))
                                .actionBar(),
                        null, null, null );
                cardhalte.setVisibility(View.VISIBLE);
                for (Halte h : haltes) {
                    if (h.getName().contentEquals(marker.getTitle())) {
                        int index = haltes.indexOf(h);
                        halte=h;
                        jumlah.setText(h.getWaiting() + " penunggu");
                        jumlahHalte.setText(h.getWaiting() + " orang");
                        jarak.setText(map_distance[index]);
                        jarakHalte.setText(map_distance[index]);
                        waktuHalte.setText(map_duration[index]);
                        if (polyline != null) {
                            polyline.remove();
                        }
                        polyline = mGoogleMap.addPolyline(map_poli[index]);
                        break;
                    }
                }

                return v;
            }
        });

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (polyline != null) {
                    polyline.remove();
                }
                cardhalte.setVisibility(View.GONE);
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
                marker.showInfoWindow();
                return false;
            }
        });
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
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_halte));
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
                                            map_duration[index] = direction.getRouteList().get(0).getLegList().get(0).getDuration().getText();
                                            map_distance[index] = direction.getRouteList().get(0).getLegList().get(0).getDistance().getText();
                                            map_poli[index] = DirectionConverter.createPolyline(LynStopActivity.this, directionPositionList, 5, ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
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

}

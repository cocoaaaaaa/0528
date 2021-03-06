package com.example.cafemoa;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class Home2Activity extends AppCompatActivity
        implements
        OnMapReadyCallback,
        PlacesListener {

    String loginID;
    String loginSort;
    private GoogleMap mMap;
    private   AlertDialog dialog;
    List<Marker> previous_marker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        Intent intent = getIntent();
        loginID = intent.getExtras().getString("loginID");
        loginSort = intent.getExtras().getString("loginSort");

        previous_marker = new ArrayList<Marker>();

        Button cafeButton = (Button) findViewById(R.id.cafeButton);
        cafeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng location = null;
                showPlaceInformation(location);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;

        LatLng SUWON = new LatLng(37.2848145, 127.0118573);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SUWON);
        markerOptions.title("행리단길");


        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(SUWON));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                Intent intent = new Intent(getBaseContext(), CafelistActivity.class);

                String title = marker.getTitle();
                String address = marker.getSnippet();

                intent.putExtra("title", title);
                intent.putExtra("address", address);

                startActivity(intent);
            }
        });
    }


    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(final List<Place> places) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (noman.googleplaces.Place place : places) {

                    LatLng latLng
                            = new LatLng(place.getLatitude()
                            , place.getLongitude());


                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(place.getName());
                    String markerSnippet = null;
                    markerOptions.snippet(markerSnippet);
                    Marker item = mMap.addMarker(markerOptions);
                    previous_marker.add(item);

                }

                //중복 마커 제거
                HashSet<Marker> hashSet = new HashSet<Marker>();
                hashSet.addAll(previous_marker);
                previous_marker.clear();
                previous_marker.addAll(hashSet);

            }
        });

    }

    @Override
    public void onPlacesFinished() {

    }

    public void showPlaceInformation(LatLng location) {
        mMap.clear();//지도 클리어


        if (previous_marker != null)
            previous_marker.clear();//지역정보 마커 클리어

        new NRPlaces.Builder()
                .listener(Home2Activity.this)
                .key("AIzaSyD16rYhUbrean9eF_eQ082rnYL6L6cCCrA")
                .latlng(37.2848145, 127.0118573)
                .radius(500) //500 미터 내에서 검색
                .type(PlaceType.CAFE) //카페
                .build()
                .execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home2menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutMenu:

                AlertDialog.Builder builder=new AlertDialog.Builder( Home2Activity.this );
                dialog=builder.setMessage("로그아웃하시겠습니까?")
                        .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Home2Activity.this, HomeActivity.class);
                            Home2Activity.this.startActivity(intent);
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create();
                dialog.show();

                break;

            case R.id.action_search2:
                if(loginSort.equals("1")) {            //점주
                    Intent intent2 = new Intent(Home2Activity.this, InformationActivity.class);
                    intent2.putExtra("loginID", loginID);
                    intent2.putExtra("loginSort", loginSort);
                    Home2Activity.this.startActivity(intent2);


                }
                /*else if(loginSort.equals("2")){       //고객


                }*/

                break;


            case R.id.pointMenu:
                Intent intent3 = new Intent(Home2Activity.this, Point2Activity.class);
                intent3.putExtra("loginID", loginID);
                intent3.putExtra("loginSort", loginSort);
                Home2Activity.this.startActivity(intent3);
                break;

        }
        return true;
    }
}
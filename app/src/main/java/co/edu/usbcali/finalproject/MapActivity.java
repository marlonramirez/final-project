package co.edu.usbcali.finalproject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import co.edu.usbcali.finalproject.domain.RouteLogic;

/**
 * Created by Marlon.Ramirez on 16/03/2018.
 */

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, OnStreetViewPanoramaReadyCallback {
    private GoogleMap map;
    private StreetViewPanorama streetView;
    private LatLng teacherPosition;
    private LatLng studentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle extras = getIntent().getExtras().getBundle("teacher");
        double latitude = extras.getDouble("latitude");
        double longitude = extras.getDouble("longitude");
        teacherPosition = new LatLng(latitude, longitude);
        studentPosition = new LatLng(3.4590, -76.5290);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.map);
        StreetViewPanoramaFragment streetViewPanoramaFragment = (StreetViewPanoramaFragment) getFragmentManager().findFragmentById(R.id.street_view);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
        SupportMapFragment supportmapfragment = (SupportMapFragment)fragment;
        supportmapfragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(studentPosition, 13));
        MarkerOptions teacherMarker = new MarkerOptions();
        teacherMarker.position(teacherPosition);
        teacherMarker.title("Su docente esta aca");
        teacherMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_learn));
        teacherMarker.flat(true);
        map.addMarker(teacherMarker);
        MarkerOptions studentMarker = new MarkerOptions();
        studentMarker.position(studentPosition);
        studentMarker.title("Usted se encuentra aca");
        studentMarker.flat(true);
        map.addMarker(studentMarker);
        animatePath(studentPosition, teacherPosition);
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        streetView = streetViewPanorama;
        if (streetViewPanorama != null) {
            streetViewPanorama.setPosition(teacherPosition);
        }
    }

    private void animatePath(LatLng origin, LatLng target) {
        CameraPosition cameraPosition = CameraPosition.builder().target(origin).zoom(13).bearing(90).build();
        PolylineOptions polylineOptions = RouteLogic.getInstance().getRoute(origin, target);
        map.addPolyline(polylineOptions);
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
    }
}

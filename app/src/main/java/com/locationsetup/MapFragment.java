package com.locationsetup;


import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, MainActivity.OnItemChangedListener {

    private final String TAG = MapFragment.class.getSimpleName();

    private MapView mapView;
    private GoogleMap mGoogleMap;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = layout.findViewById(R.id.map);
        mapView.getMapAsync(this);

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mapView != null)
            mapView.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ((MainActivity)context).setItemClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        updateMap(googleMap);
    }

    public void updateMap(GoogleMap googleMap) {
        googleMap.clear();

        double sumLat = 0.0d;
        double sumLng = 0.0d;
        for (LocationItem item : MainActivity.items) {
            LatLng latLng = new LatLng(item.getLatitude(), item.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng)
                    .title(item.getName())
                    .snippet(item.getAddress());
            if (!item.isEnabled()) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_VIOLET));
            }
            googleMap.addMarker(markerOptions).setTag(item.getId());
            googleMap.setOnInfoWindowClickListener(mInfoWindowClickListener);

            sumLat += item.getLatitude();
            sumLng += item.getLongitude();
        }

        double avgLat = sumLat / MainActivity.items.size();
        double avgLnt = sumLng / MainActivity.items.size();
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(avgLat, avgLnt)));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
    }

    @Override
    public void onItemChanged() {
        updateMap(mGoogleMap);
        Log.d(TAG, "update map");
    }

    GoogleMap.OnInfoWindowClickListener mInfoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            String id = (String) marker.getTag();
            for (LocationItem item : MainActivity.items) {
                if (id.equals(item.getId())) {
                    // MainActivity.modify(item);
                    Log.d(TAG, "call->MainActivity.modify(LocationItem item)");
                }
            }
        }
    };

    /*public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }*/

}

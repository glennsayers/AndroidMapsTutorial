package com.glennsayers.mapapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity {

    /** Member variables **/
    GoogleMap m_googleMap;
    StreetViewPanorama m_StreetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createMapView();
        createStreetView();

        /**
         * Set up the onClickListener that will pass the selected lat/long
         * co-ordinates through to the Street View fragment for loading
         */
        m_googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                /**
                 * Hide the map view to expose the street view.
                 */
                Fragment mapView = getFragmentManager().findFragmentById(R.id.mapView);
                getFragmentManager().beginTransaction().hide(mapView).commit();

                /**
                 * Ensure the street view has been initialise correctly and
                 * pass it through the selected lat/long co-ordinates.
                 */
                if (m_StreetView != null) {
                    m_StreetView.setPosition(latLng);
                }
            }
        });
    }

    /**
     * Initialises the street view member variable with the appropriate
     * fragment from the FragmentManager
     */
    private void createStreetView() {
        m_StreetView = ((StreetViewPanoramaFragment)
                getFragmentManager().findFragmentById(R.id.streetView))
                .getStreetViewPanorama();
    }

    /**
     * Initialises the mapview
     */
    private void createMapView(){
        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {
            if(null == m_googleMap){
                m_googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();

                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if(null == m_googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map",Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception){
            Log.e("mapApp", exception.toString());
        }
    }

    /**
     * Adds a marker to the map
     */
    private void addMarker(){

        /** Make sure that the map has been initialised **/
        if(null != m_googleMap){
            m_googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(0, 0))
                                .title("Marker")
                                .draggable(true)
            );
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

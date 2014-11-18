package net.dgistudio.guillaume.operacarto;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Guillaume on 11/10/2014.
 */
public class LocalisationInfo implements LocationListener {
    private static Context context = null;
    public double latitude = 0;
    public double longitude = 0;
    LocationManager locationManager;
    private signalStrenghtProprieties signalProprieties;

    public void setSignal(signalStrenghtProprieties prop)
    {
        this.signalProprieties = prop;
    }

    public LocalisationInfo(Context context)
    {
        this.context = context;
    }
    private LocationManager getSystemLocalisationInfo()
    {
        LocationManager locationManager = (LocationManager)LocalisationInfo.context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager;
    }
    public void getLocation()
    {
        locationManager = getSystemLocalisationInfo();
        ArrayList<LocationProvider> providers = new ArrayList<LocationProvider>();
        ArrayList<String> names = (ArrayList<String>) locationManager.getProviders(true);

        for(String name : names)
            providers.add(locationManager.getProvider(name));

        Criteria critere = new Criteria();
        critere.setAccuracy(Criteria.ACCURACY_FINE);
        critere.setAltitudeRequired(false);
        critere.setBearingRequired(false);
        critere.setCostAllowed(false);
        critere.setPowerRequirement(Criteria.POWER_LOW);
        critere.setSpeedRequired(false);



        locationManager.requestLocationUpdates(locationManager.getBestProvider(critere,true), 60, 150, this);



    }
    public void stop(){
        locationManager.removeUpdates( this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.d("GPS", "Latitude " + location.getLatitude() + " et longitude " + location.getLongitude());
    }
}

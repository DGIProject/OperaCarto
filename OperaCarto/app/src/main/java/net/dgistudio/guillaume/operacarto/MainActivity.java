package net.dgistudio.guillaume.operacarto;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


/**
 * Created by Guillaume on 18/09/2014.
 * this file is the main Activity of the project
 */

public class MainActivity extends FragmentActivity{
    ConnectivityInfo connectionInformation;
    public static final String RECEIVE_JSON = "net.dgiproject.guillaume.RECEIVER";
    TextView powerSignal;
    private LocalisationInfo l = null;
    signalStrenghtProprieties SignalProprieties = null;
    GoogleMap mMap;

    private BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(RECEIVE_JSON)) {
                String serviceJsonString = intent.getStringExtra("json");
                Log.d("Receiver", "J'ai recu des Data et meme "+serviceJsonString);
                MainActivity.this.updateData();
            }
        }
    };

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (mMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        if (prefs.getBoolean("show_info",true))
        {
            Intent newAct = new Intent(MainActivity.this, InformationActivity.class);
            startActivity(newAct);
        }
        powerSignal = (TextView)findViewById(R.id.networkPower);
        connectionInformation = new ConnectivityInfo(MainActivity.this);

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVE_JSON);
        bManager.registerReceiver(bReceiver, intentFilter);

        MyPhoneStateListener phoneStateListenner = new MyPhoneStateListener();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListenner, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        l  = new LocalisationInfo(this);
        l.getLocation();
        setUpMapIfNeeded();

        mMap.setMyLocationEnabled(false);

        this.updateData();

    }

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

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent preferences = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(preferences);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onResume(){
        super.onResume();
        setUpMapIfNeeded();
        android.util.Log.d("Resume", "ActivityResumed updating data");
        this.updateData();
    }

    public void updateData(){
        TextView networkType = (TextView)findViewById(R.id.networkType);
        networkType.setText(connectionInformation.getConnectionType());

        TextView operatorName = (TextView)findViewById(R.id.operatorName);
        operatorName.setText(connectionInformation.getNetworkName());

        TextView pos = (TextView)findViewById(R.id.userLocalisation);
        pos.setText("lat: "+l.latitude+" lon: "+l.longitude);

        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(l.latitude, l.longitude), new LatLng(l.latitude-0.000001, l.longitude-0.000001))
                .width(5)
                .color(Color.RED));

        LatLng sydney = new LatLng(l.latitude, l.longitude);


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

    }

    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            MainActivity.this.updateSignal(signalStrength);
        }
    }

    private signalStrenghtProprieties getSignalQuality(int signalStrenght){

        if (signalStrenght >= 0 && signalStrenght <=7)
        {
            return new signalStrenghtProprieties(1,"Faible réception");
        }
        else if (signalStrenght >=8 && signalStrenght <=15)
        {
            return new signalStrenghtProprieties(2,"réception médiocre");
        }
        else if (signalStrenght >=16 && signalStrenght <=23)
        {
            return new signalStrenghtProprieties(3,"réception correcte");
        }
        else if (signalStrenght >=24 && signalStrenght <=31)
        {
            return new signalStrenghtProprieties(4,"réception exelente");
        }
        else
        {
            return new signalStrenghtProprieties(0,"Inconnue/Pas de réseau");
        }
    }

    private void updateSignal(SignalStrength signal) {

        TextView view = (TextView) findViewById(R.id.networkPower);
        SignalProprieties = getSignalQuality(signal.getGsmSignalStrength());
        String signalText = SignalProprieties.barNumber+"/4 - "+SignalProprieties.qualityText;
        view.setText(signalText);
        Log.d("Etat", "UpdateSignal apellé");

        Log.d("Etat", String.valueOf(signal.getGsmSignalStrength()));
        this.updateData();
    }
}

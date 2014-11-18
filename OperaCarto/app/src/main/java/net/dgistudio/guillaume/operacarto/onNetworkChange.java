package net.dgistudio.guillaume.operacarto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

public class onNetworkChange extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("e", "Something receive !");
        System.out.println("intent Received");
        String jsonString = "STRING";
        Intent RTRetur = new Intent(MainActivity.RECEIVE_JSON);
        RTRetur.putExtra("json", jsonString);
        LocalBroadcastManager.getInstance(context).sendBroadcast(RTRetur);
    }
}

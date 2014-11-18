package net.dgistudio.guillaume.operacarto;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;


public class InformationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnAcces = (Button) findViewById(R.id.accBtn);
        btnAcces.setOnClickListener(new onclickBtn());
    }


    private class onclickBtn implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(InformationActivity.this);
            CheckBox checkbox = (CheckBox) findViewById(R.id.checkBox);
            if (checkbox.isChecked())
            {
                prefs.edit().putBoolean("show_info",false).apply();
            }
            InformationActivity.this.finish();
        }
    }
}

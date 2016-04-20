package com.mikarney.jernil.shoutout;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import static android.location.LocationManager.GPS_PROVIDER;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private int count = 0;
    private long startMillis=0;

    private static String Imei = null;
    protected LocationManager locationManager;
    Location location;

    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat;
    String lat;
    String provider;
    protected double latitude,longitude;
    protected boolean gps_enabled,network_enabled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE},
                0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Use either the beacon icon/push volume button down 7 times to make panic call", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, Main2Activity.class);
                startActivity(intent);

            }

        });
       /* Button button2 = (Button) findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, Main3Activity.class);
                startActivity(intent);

            }

        });
        Button button3 = (Button) findViewById(R.id.button7);

        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, MapsActivity.class);
                startActivity(intent);



            }

        });

        Button button4 = (Button) findViewById(R.id.button13);

        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, MapsActivity2.class);
                startActivity(intent);



            }

        });*/

    }

    public boolean onKeyDown(int keyCode,KeyEvent event) {
        //public boolean onKeyDown(int keyCode,KeyEvent event) {

        if (keyCode ==KeyEvent.KEYCODE_VOLUME_DOWN) {
            event.startTracking();

            //get system current milliseconds
            long time= System.currentTimeMillis();


            //if it is the first time, or if it has been more than 3 seconds since the first tap ( so it is like a new try), we reset everything
            if (startMillis==0 || (time-startMillis> 3000) ) {
                startMillis=time;
                count=1;
            }
            //it is not the first, and it has been  less than 3 seconds since the first
            else{ //  time-startMillis< 3000
                count++;
            }

            if (count==5) {
                send_sms();

            }
            return true;

        }
        return false;
    }
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            event.startTracking();
            send_sms();
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }
    public class AutoBoot extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent i = new Intent(context, Main2Activity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    public void send_sms(){
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
       // final SharedPreferences.Editor editor = sharedpreferences.edit();
        //EditText e1=(EditText) findViewById(R.id.editText1);
        //EditText e2=(EditText) findViewById(R.id.editText2);
        //EditText e3=(EditText) findViewById(R.id.editText3);
        //String phone_no1=e1.getText().toString();
        //String phone_no2=e2.getText().toString();
        //String phone_no3=e3.getText().toString();
        String phone_no1=sharedpreferences.getString("phone1",null);
        String phone_no2=sharedpreferences.getString("phone2", null);
        String phone_no3=sharedpreferences.getString("phone3",null);
        //editor.putString("phone1",phone_no1);
        //editor.putString("phone2",phone_no2);
        //editor.putString("phone3",phone_no3);
        // editor.putBoolean("filled", true);
        //editor.commit();
        SmsManager smsManager = SmsManager.getDefault();
        // smsManager.sendTextMessage(phone_no1, null, "help me", null, null);
        //smsManager.sendTextMessage(phone_no2, null, "help me", null, null);
        //smsManager.sendTextMessage(phone_no3, null, "help me", null, null);

        //SmsManager smsManager = SmsManager.getDefault();
        TelephonyManager tManager= (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        Imei = tManager.getDeviceId();
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(GPS_PROVIDER,0,0,this);

        if (lm != null) {
            location = lm.getLastKnownLocation(GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            } else {

                Log.e("Printf", "Location returned null");

            }
        } else {
            Log.e("printf", "Location Manager returned null");
        }

        try{

            smsManager.sendTextMessage(phone_no1, null, "help me!! Imei:"  + Imei + " http://maps.google.com/maps?z=12&t=m&q=loc:"+ Double.toString(latitude) + "+" + Double.toString(longitude), null, null);

            smsManager.sendTextMessage(phone_no2, null, "help me!! Imei:"  + Imei + " http://maps.google.com/maps?z=12&t=m&q=loc:"+ Double.toString(latitude) + "+" + Double.toString(longitude), null, null);

            smsManager.sendTextMessage(phone_no3, null, "help me!! Imei:"  + Imei + " http://maps.google.com/maps?z=12&t=m&q=loc:"+ Double.toString(latitude) + "+" + Double.toString(longitude), null, null);

            //Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT).show();

            Log.v("debug_tag", "inside sms intent");
        }
        catch (Exception e) {
           // Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void onLocationChanged(Location location) {

        txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
    }


    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }


    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }


    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    public void panicCall(View v)
    {
        /*SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String phone_no1=sharedpreferences.getString("phone1",null);
        String phone_no2=sharedpreferences.getString("phone2", null);
        String phone_no3=sharedpreferences.getString("phone3",null);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone_no1, null, "help me", null, null);
        smsManager.sendTextMessage(phone_no2, null, "help me", null, null);
        smsManager.sendTextMessage(phone_no3, null, "help me", null, null);*/
        //String s=prefs.getString("name1",null);
       // Log.v("app log::", s);
       // Main2Activity.sms_intent();
        send_sms();
        Toast.makeText(this.getApplicationContext(),"Panic call made!!",Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

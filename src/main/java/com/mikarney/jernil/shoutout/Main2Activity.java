package com.mikarney.jernil.shoutout;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.SmsManager;

import static android.location.LocationManager.GPS_PROVIDER;


public class Main2Activity extends AppCompatActivity implements LocationListener{
    static Main2Activity instance;


    public static final int  MY_PERMISSIONS_REQUEST_READ_CONTACTS=0;
    public static final int MY_PERMISSIONS_REQUEST_SEND_SMS=1;
    public int  button_id=0;
    private static final int CONTACT_PICKER_RESULT = 1001;
    static String DEBUG_TAG="ShoutOut contact picker";
    String contact[]=new String[3];
    String phone_no[]=new String[3];
    String phone="";
    String name="";
    boolean filled;
    String proj_name="";
    public static final String MyPREFERENCES = "MyPrefs" ;
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
    //public static final String Contacts = "Emergency_nos" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button b2=(Button) findViewById(R.id.button6);
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
       // editor.putString("proj", "shoutout");
        //editor.commit();
        filled=sharedpreferences.getBoolean("filled", false);
        if(filled){
        //    contact[1]=sharedpreferences.getString("name1",null);
        //    contact[2]=sharedpreferences.getString("name2",null);
        //    contact[3]=sharedpreferences.getString("name3",null);
            contact[0]=sharedpreferences.getString("name1",null);
            contact[1]=sharedpreferences.getString("name2",null);
            contact[2]=sharedpreferences.getString("name3",null);
            phone_no[0]=sharedpreferences.getString("phone1",null);
            phone_no[1]=sharedpreferences.getString("phone2",null);
            phone_no[2]=sharedpreferences.getString("phone3",null);
            EditText e1=(EditText) findViewById(R.id.editText1);
            EditText e2=(EditText) findViewById(R.id.editText2);
            EditText e3=(EditText) findViewById(R.id.editText3);
            e1.setText(contact[0]+" : "+phone_no[0]);
            e2.setText(contact[1]+" : "+phone_no[1]);
            e3.setText(contact[2]+" : "+phone_no[2]);
         //   break;
        }
            proj_name = sharedpreferences.getString("proj", null);
            Log.v(DEBUG_TAG, "proj name:" + proj_name);
            //Log.v(DEBUG_TAG,"ennada");
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ContextCompat.checkSelfPermission(Main2Activity.this,
                            Manifest.permission.SEND_SMS)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(Main2Activity.this,
                                Manifest.permission.SEND_SMS)) {

                            // Show an expanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.

                        } else {

                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(Main2Activity.this,
                                    new String[]{Manifest.permission.SEND_SMS},
                                    MY_PERMISSIONS_REQUEST_SEND_SMS);

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    } else
                        sms_intent();

                }
            });

    }


    //@Override
    /*public void onBackPressed() {
        // your code.
        this.finishActivity(123);
    }*/

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
    public void contact_intent()
    {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }
    public void sms_intent() {
        //Main2Activity m=new Main2Activity();
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
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

            Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT).show();

            Log.v(DEBUG_TAG, "inside sms intent");
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
       Toast.makeText(getApplicationContext(),"Message sent",Toast.LENGTH_SHORT).show();
        this.finishActivity(123);
    }

  //  public void doLaunchSms(View v)
   // {

   // }

    public void doLaunchContactPicker(View view) {
        // Here, thisActivity is the current activity
        switch(view.getId())
        {
            case R.id.button3:
                button_id=1;
                break;
            case R.id.button4:
                button_id=2;
                break;
            case R.id.button5:
                button_id=3;
        }
       if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
       }else
          contact_intent();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    contact_intent();
                }}
                break;
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        sms_intent();
                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.
                    }
                }
                }// else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
               // }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
   //     }
  //  }}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //final SharedPreferences sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    Cursor cursor = null;
                    String email = "";
                    try {
                        Uri result = data.getData();
                        Log.v(DEBUG_TAG, "Got a contact result: "
                                + result.toString());

                        // get the contact id from the Uri
                        String id = result.getLastPathSegment();

                        // query for everything email
                        cursor = getContentResolver().query(Phone.CONTENT_URI,
                                null, Phone.CONTACT_ID + "=?", new String[] { id },
                                null);
                        int nameIdx = cursor.getColumnIndex(Phone.SORT_KEY_PRIMARY);
                        int phoneIdx = cursor.getColumnIndex(Phone.DATA1);


                        if (cursor.moveToFirst()) {
                            name = cursor.getString(nameIdx);
                            phone = cursor.getString(phoneIdx);
                            Log.v(DEBUG_TAG, "Got name: " + name);
                            Log.v(DEBUG_TAG, "Got phone: " + phone);
                        } else {
                            Log.w(DEBUG_TAG, "No results");
                        }
                    } catch (Exception e) {
                        Log.e(DEBUG_TAG, "Failed to get name and number data", e);
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                        EditText dispEntry=null;
                        switch (button_id) {
                            case 1:
                                dispEntry = (EditText) findViewById(R.id.editText1);
                                break;
                            case 2:
                                dispEntry = (EditText) findViewById(R.id.editText2);
                                break;
                            case 3:
                                dispEntry = (EditText) findViewById(R.id.editText3);
                        }
                        dispEntry.setText(name + " : " + phone);
                        editor.putString("name" + button_id, name);
                        editor.putString("phone" + button_id, phone);
                        editor.commit();
                        if (name.length() == 0 && phone.length()==0) {
                            Toast.makeText(this, "No display entries found for contact.",Toast.LENGTH_LONG).show();
                        }

                    }
                    editor.putBoolean("filled",true);
                    editor.commit();
                    break;
            }

        } else {
            Log.w(DEBUG_TAG, "Warning: activity result not ok");
        }
    }
}

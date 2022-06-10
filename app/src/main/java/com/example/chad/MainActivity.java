package com.example.chad;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    General general;
    Filling filling;
    ProgressDialog p;

    LinearLayout lytQuestions;
    FloatingActionButton fab;

    String answer_code = "";
    CharSequence other = "";

    double longitude = 0.0;
    double latitude = 0.0;
    double accurancy = 0.0;

    String filename = "";
    String string_object_answers = "";
    Bundle extras = null;

    LocationManager mLocationManager;
    FusedLocationProviderClient fusedLocationProviderClient;

    LinearLayout main_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        general = new General(this);
        filling = new Filling();

        Intent intent = getIntent();
        extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("FILENAME")) {
                filename = intent.getStringExtra("FILENAME");
                string_object_answers = intent.getStringExtra("ANSWERS");
            }
        }

        String questions = general.getFile("questionnaire", MainActivity.this);
        try {
            createAQuestion(questions);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (extras != null) {
            if (extras.containsKey("FILENAME")) {
                main_layout = (LinearLayout) findViewById(R.id.main_layout);//change this
                main_layout.setBackgroundColor(getResources().getColor(R.color.grey));

                lytQuestions = (LinearLayout) findViewById(R.id.lytQuestions);
                Button btn_edit = new Button(this);
                btn_edit.setText(R.string.edit);
                setMargins(btn_edit, 0, 10, 0, 10);
                btn_edit.setBackgroundResource(R.drawable.orange_corner_gradient);
                btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JSONObject object_answers = null;
                        JSONArray array_results = null;
                        JSONObject object1 = null;
                        try {
                            object_answers = new JSONObject(string_object_answers);
                            array_results = object_answers.getJSONArray("results");

                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("code", "CHAD4");//change this
                            jsonObject.put("answer_code", answer_code);
                            jsonObject.put("comment", "None");

                            for(int i=0; i<array_results.length(); i++){
                                object1 = array_results.getJSONObject(i);
                                if(object1.getString("code").equals("CHAD4")){//change this
                                    array_results.remove(i);
                                    array_results.put(i, jsonObject);
                                    break;
                                }
                            }
                            filling.writeToFile("ResultArray", array_results.toString(), MainActivity.this);
                            general.CreateMainObject2("results", array_results, MainActivity.this);
                            File yourDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Answers/");

                            File fdelete = new File(yourDir+"/"+filename);
                            if (fdelete.exists()) {
                                if (fdelete.delete()) {
                                    filling.saveFileToFolderContext(filename, general.getFile("MainObject", MainActivity.this), MainActivity.this);
                                } else {
                                    System.out.println(R.string.file_not_deleted);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(MainActivity.this, Dashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);

                    }
                });
                lytQuestions.addView(btn_edit);
            }
        }else{
            Button btn_back = new Button(this);
            btn_back.setText(R.string.go_back_to_dashboard);
            btn_back.setBackgroundResource(R.drawable.cyan_corner_gradient);
            setMargins(btn_back, 0, 10, 0, 10);
            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    general.goBackDialoag(MainActivity.this);
                }
            });

            lytQuestions.addView(btn_back);
        }//Hapa

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!answer_code.equals("")) {
                    if (setup4()) {
                        getLastKnownLocation();
                    }else {
                        Toast.makeText(MainActivity.this, R.string.enable_location_settings, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, R.string.select_type_visit, Toast.LENGTH_SHORT).show();
                }


            }
        });
        general.CreateObjectForMain(MainActivity.this);
    }



    public void createAQuestion(String questions) throws JSONException {
        JSONObject json = new JSONObject(questions);
        JSONArray questionnaire = json.getJSONArray("questionnaire");
        lytQuestions = (LinearLayout) findViewById(R.id.lytQuestions);

        for (int i = 0; i < questionnaire.length(); i++) {
            JSONObject json_data = questionnaire.getJSONObject(i);

            final TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            setMargins(textView, 0, 100, 0, 0);
            textView.setText(json_data.getString("name_sw"));
            textView.setTextSize(20);
            textView.setTextColor(getResources().getColor(R.color.black));
            if (lytQuestions != null) {
                lytQuestions.addView(textView);
            }
            final RadioGroup rg = new RadioGroup(this);
            RadioButton spinner;
            final EditText editText = new EditText(this);

            String opt = json_data.getString("options");
            JSONArray optArray = new JSONArray(opt);
            for (int j = 0; j < optArray.length(); j++) {
                final JSONObject opt_data = optArray.getJSONObject(j);
                spinner = new RadioButton(this);
                spinner.setId(View.generateViewId());
                spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120));

                if (extras != null) {
                    if (extras.containsKey("FILENAME")) {
                        JSONObject object_answers = new JSONObject(string_object_answers);
                        JSONArray array_results = object_answers.getJSONArray("results");
                        for (int k = 0; k < array_results.length(); k++) {
                            JSONObject opt_data_results = array_results.getJSONObject(k);
                            if(json_data.getString("code").equals("CHAD4")){//change this
                                if(opt_data.getString("code").equals(opt_data_results.getString("answer_code"))){
                                    spinner.setChecked(true);
                                    spinner.setTextColor(getResources().getColor(R.color.orange1));
                                }
                            }
                        }
                    }
                }//Hapa

                setMargins(spinner, 0, 0, 0, 100);
                spinner.setText(opt_data.getString("name_en"));
                spinner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((RadioButton) v).isChecked()) {
                            try {
                                answer_code = opt_data.getString("code");
                                //editText.setText("");
                                //other = "";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                rg.addView(spinner);

            }
            if (lytQuestions != null) {
                lytQuestions.addView(rg);
            }

            // Add Spinner to LinearLayout

            break;
        }
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public boolean setup4() {
        LocationManager lm = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage(R.string.Gps_network_not_enable)
                    .setPositiveButton(R.string.Open_Location_Settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            MainActivity.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        }

        return gps_enabled;
    }

    public void toContinueDialoag(String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(R.string.gps_location);
            builder.setMessage(message);

            builder.setPositiveButton(R.string.con, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    switch (answer_code) {
                        case "CHAD4A": {
                            if (extras != null) {
                                if (extras.containsKey("FILENAME")) {
                                    JSONObject object_answers = null;
                                    JSONArray array_results = null;
                                    JSONObject object1 = null;
                                    try {
                                        object_answers = new JSONObject(string_object_answers);
                                        array_results = object_answers.getJSONArray("results");

                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("code", "CHAD4");
                                        jsonObject.put("answer_code", answer_code);
                                        jsonObject.put("comment", "None");

                                        for (int i = 0; i < array_results.length(); i++) {
                                            object1 = array_results.getJSONObject(i);
                                            if (object1.getString("code").equals("CHAD4")) {
                                                array_results.remove(i);
                                                array_results.put(i, jsonObject);
                                                break;
                                            }
                                        }
                                        filling.writeToFile("ResultArray", array_results.toString(), MainActivity.this);
                                        Intent intent = new Intent(MainActivity.this, Part29.class);
                                        intent.putExtra("FILENAME", filename);
                                        intent.putExtra("ANSWERS", string_object_answers);
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    general.AddObjectToResultArray("CHAD4", answer_code, MainActivity.this);
                                    Intent intent = new Intent(MainActivity.this, Part29.class);
                                    startActivity(intent);
                                }
                            } else {
                                general.AddObjectToResultArray("CHAD4", answer_code, MainActivity.this);
                                Intent intent = new Intent(MainActivity.this, Part29.class);
                                startActivity(intent);
                            }
                            break;
                        }
                        case "CHAD4B": {
                            if (extras != null) {
                                if (extras.containsKey("FILENAME")) {
                                    JSONObject object_answers = null;
                                    JSONArray array_results = null;
                                    JSONObject object1 = null;
                                    try {
                                        object_answers = new JSONObject(string_object_answers);
                                        array_results = object_answers.getJSONArray("results");

                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("code", "CHAD4");//change this
                                        jsonObject.put("answer_code", answer_code);
                                        jsonObject.put("comment", "None");

                                        for (int i = 0; i < array_results.length(); i++) {
                                            object1 = array_results.getJSONObject(i);
                                            if (object1.getString("code").equals("CHAD4")) {//change this
                                                array_results.remove(i);
                                                array_results.put(i, jsonObject);
                                                break;
                                            }
                                        }
                                        filling.writeToFile("ResultArray", array_results.toString(), MainActivity.this);
                                        Intent intent = new Intent(MainActivity.this, Part2.class);
                                        intent.putExtra("FILENAME", filename);
                                        intent.putExtra("ANSWERS", string_object_answers);
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    general.AddObjectToResultArray("CHAD4", answer_code, MainActivity.this);
                                    Intent intent = new Intent(MainActivity.this, Part2.class);
                                    startActivity(intent);
                                }
                            } else {
                                general.AddObjectToResultArray("CHAD4", answer_code, MainActivity.this);
                                Intent intent = new Intent(MainActivity.this, Part2.class);
                                startActivity(intent);
                            }
                            break;
                        }
                        case "CHAD4C": {
                            if (extras != null) {
                                if (extras.containsKey("FILENAME")) {
                                    JSONObject object_answers = null;
                                    JSONArray array_results = null;
                                    JSONObject object1 = null;
                                    try {
                                        object_answers = new JSONObject(string_object_answers);
                                        array_results = object_answers.getJSONArray("results");

                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("code", "CHAD4");//change this
                                        jsonObject.put("answer_code", answer_code);
                                        jsonObject.put("comment", "None");

                                        for (int i = 0; i < array_results.length(); i++) {
                                            object1 = array_results.getJSONObject(i);
                                            if (object1.getString("code").equals("CHAD4")) {//change this
                                                array_results.remove(i);
                                                array_results.put(i, jsonObject);
                                                break;
                                            }
                                        }
                                        filling.writeToFile("ResultArray", array_results.toString(), MainActivity.this);
                                        Intent intent = new Intent(MainActivity.this, Part29.class);
                                        intent.putExtra("FILENAME", filename);
                                        intent.putExtra("ANSWERS", string_object_answers);
                                        startActivity(intent);
                                    } catch (JSONException e) {

                                        e.printStackTrace();
                                    }
                                } else {
                                    general.AddObjectToResultArray("CHAD4", answer_code, MainActivity.this);
                                    Intent intent = new Intent(MainActivity.this, Part24.class);
                                    startActivity(intent);
                                }
                            } else {
                                general.AddObjectToResultArray("CHAD4", answer_code, MainActivity.this);
                                Intent intent = new Intent(MainActivity.this, Part24.class);
                                startActivity(intent);
                            }
                            break;
                        }
                        default: {
                            general.AddObjectToResultArray("CHAD4", other.toString(), MainActivity.this);
                            Intent intent = new Intent(MainActivity.this, End.class);
                            startActivity(intent);
                            break;
                        }
                    }
                    dialog.dismiss();
                }

            });

/*        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // I do not need any action here you might
                dialog.dismiss();
            }
        });*/

            AlertDialog alert = builder.create();
            alert.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getLastKnownLocation() {
        p = new ProgressDialog(MainActivity.this);
        p.setMessage(getString(R.string.getting_gps));
        p.setIndeterminate(false);
        p.setCancelable(false);
        p.show();

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location l = task.getResult();
                if(l != null){
                    p.hide();
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    //List<Address> addresses = geocoder.getFromLocation(l.getLatitude(), l.getLongitude(), 1);
/*                        longitude = addresses.get(0).getLongitude();
                    latitude = addresses.get(0).getLatitude();   */
                    longitude = l.getLatitude();
                    latitude = l.getLongitude();
                    accurancy = l.getAccuracy();

                    if (longitude != 0.0 && latitude != 0.0) {
                        String message = "Longitude: " + longitude + "\n" + "Latitude: " + latitude + "\n"+ "Accuracy: " +accurancy;
                        general.CreateMainObject("latitude", String.valueOf(latitude), MainActivity.this);
                        general.CreateMainObject("longitude", String.valueOf(longitude), MainActivity.this);
                        general.CreateMainObject("accuracy", String.valueOf(accurancy), MainActivity.this);
                        toContinueDialoag(message);
                    } else {
                        p.hide();
                        toContinueDialoag(getString(R.string.fail_current_location));
                    }
                }else{
                    try{
                        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                        List<String> providers = mLocationManager.getProviders(true);
                        Location bestLocation = null;
                        for (String provider : providers) {
                            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            Location loc = mLocationManager.getLastKnownLocation(provider);

                            if (loc == null) {
                                continue;
                            }else{
                                p.hide();
                                if (bestLocation == null || loc.getAccuracy() < bestLocation.getAccuracy()) {
                                    // Found best last known location: %s", l);
                                    longitude = loc.getLongitude();
                                    latitude = loc.getLatitude();
                                    accurancy = loc.getAccuracy();
                                }
                            }
                        }
                        if (longitude != 0.0 && latitude != 0.0) {
                            String message = "Longitude: " + longitude + "\n" + "Latitude: " + latitude;
                            general.CreateMainObject("latitude", String.valueOf(latitude), MainActivity.this);
                            general.CreateMainObject("longitude", String.valueOf(longitude), MainActivity.this);
                            general.CreateMainObject("accuracy", String.valueOf(accurancy), MainActivity.this);
                            toContinueDialoag(message);
                        } else {
                            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                            GsmCellLocation cellLocation = (GsmCellLocation)telephonyManager.getCellLocation();

                            int cellid= cellLocation.getCid();
                            int celllac = cellLocation.getLac();

                            Log.d("CellLocation", cellLocation.toString());
                            Log.d("GSM CELL ID",  String.valueOf(cellid));
                            Log.d("GSM Location Code", String.valueOf(celllac));
                            p.hide();
                            toContinueDialoag(getString(R.string.fail_current_location));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }

        });

    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        if (Build.VERSION.SDK_INT >= 24) {
            configuration.setLocale(locale);
            getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        } else {
            configuration.locale = locale;
            getBaseContext().getApplicationContext().createConfigurationContext(configuration);
        }
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }
    private void changeLanguageDialog(){
        final String[] list_languages = {"Swahili", "English"};
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.change_language)
                .setCancelable(false)
                .setSingleChoiceItems(list_languages, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            setLocale("sw");
                            recreate();
                        }else if(which == 1){
                            setLocale("en");
                            recreate();
                        }
                        dialog.dismiss();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_change_language:
                changeLanguageDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }





}
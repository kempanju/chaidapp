package com.example.chad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

public class End extends AppCompatActivity {

    private static final Set<String> CODES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("CHAD14")));

    General general;
    ProgressDialog p;

    LinearLayout lytQuestions;
    String answer_code = "";

    String filename = "";
    String string_object_answers = "";
    Bundle extras = null;
    ScrollView main_layout;
    Calendar today = Calendar.getInstance();
    Date todayDate = today.getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part3);
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        general = new General(this);

        Intent intent = getIntent();
        extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("FILENAME")) {
                filename = intent.getStringExtra("FILENAME");
                string_object_answers = intent.getStringExtra("ANSWERS");
            }
        }

        String questions = general.getFile("questionnaire", End.this);
        try {
            createAQuestion(questions);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void createAQuestion(String questions) throws JSONException {
        JSONObject json = new JSONObject(questions);
        JSONArray questionnaire = json.getJSONArray("questionnaire");
        lytQuestions = (LinearLayout) findViewById(R.id.lytQuestions);

        Button fab = new Button(End.this);
        fab.setId(R.id.fab);
        fab.setText(getResources().getString(R.string.send));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER_HORIZONTAL;

        fab.setLayoutParams(params);
        fab.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!answer_code.equals("")){
                    sendDialog();
                }else{
                    Toast.makeText(End.this, getString(R.string.select_statust_interview), Toast.LENGTH_SHORT).show();
                }

            }
        });
        for(int i=0; i<questionnaire.length(); i++){
            JSONObject json_data = questionnaire.getJSONObject(i);
            if(CODES.contains(json_data.getString("code"))){
                final TextView textView = new TextView(this);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                setMargins(textView, 0, 0, 0, 10);
                textView.setText(json_data.getString("name_sw"));
                textView.setTextSize(18);
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                textView.setTextColor(getResources().getColor(R.color.black));
                lytQuestions.addView(textView);

                RadioGroup rg = new RadioGroup(End.this);
                RadioButton spinner;

                String opt = json_data.getString("options");
                JSONArray optArray = new JSONArray(opt);
                for(int j=0; j<optArray.length(); j++){
                    final JSONObject opt_data = optArray.getJSONObject(j);
                    spinner = new RadioButton(this);
                    spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 90));
                    spinner.setId(View.generateViewId());
                    if (extras != null) {
                        if (extras.containsKey("FILENAME")) {
                            JSONObject object_answers = new JSONObject(string_object_answers);
                            JSONArray array_results = object_answers.getJSONArray("results");
                            for (int k = 0; k < array_results.length(); k++) {
                                JSONObject opt_data_results = array_results.getJSONObject(k);
                                if (opt_data_results.getString("code").equals("CHAD14")) {
                                    String answer_code = opt_data_results.getString("answer_code");
                                    if (answer_code.equals(opt_data.getString("code"))) {
                                        spinner.setChecked(true);
                                        spinner.setTextColor(getResources().getColor(R.color.orange1));
                                    }
                                }
                            }
                        }
                    }//Hapa
                    setMargins(spinner, 0, 0, 0, 10);
                    spinner.setText(opt_data.getString("name_en"));
                    spinner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ( ((RadioButton)v).isChecked() ) {
                                try {
                                    answer_code = opt_data.getString("code");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    rg.addView(spinner);
                    //personNames[j] = opt_data.getString("name_en");
                }
                lytQuestions.addView(rg);
            }

        }
        assert lytQuestions != null;
        if (extras != null) {
            if (extras.containsKey("FILENAME")) {
                main_layout = (ScrollView) findViewById(R.id.main_layout);//change this
                main_layout.setBackgroundColor(getResources().getColor(R.color.grey));

                lytQuestions = (LinearLayout) findViewById(R.id.lytQuestions);
                Button btn_edit = new Button(this);
                btn_edit.setText(getResources().getString(R.string.edit));
                setMargins(btn_edit, 0, 10, 0, 10);
                btn_edit.setBackgroundResource(R.drawable.orange_corner_gradient);
                btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!answer_code.equals("")){
                            JSONObject object_answers;
                            JSONArray array_results;
                            JSONObject object1;
                            try {
                                object_answers = new JSONObject(string_object_answers);
                                array_results = object_answers.getJSONArray("results");

                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("code", "CHAD14");
                                jsonObject.put("answer_code", answer_code);
                                jsonObject.put("comment", "None");

                                for(int i=0; i<array_results.length(); i++){
                                    object1 = array_results.getJSONObject(i);
                                    if(object1.getString("code").equals("CHAD14")){
                                        array_results.remove(i);
                                        array_results.put(jsonObject);
                                        break;
                                    }
                                }

                                object_answers.remove("results");
                                object_answers.put("results", array_results);

                                string_object_answers = object_answers.toString();

                                File yourDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Answers/");

                                File fdelete = new File(yourDir+"/"+filename);
                                if (fdelete.exists()) {
                                    if (fdelete.delete()) {
                                        //saveFileToFolderContext direct
                                        try {
                                            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Answers/";    // it will return root directory of internal storage
                                            File root = new File(path);
                                            if (!root.exists()) {
                                                root.mkdirs();       // create folder if not exist
                                            }
                                            File file = new File(path + filename);
                                            if (!file.exists()) {
                                                file.createNewFile();  // create file if not exist
                                            }
                                            BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
                                            buf.append(object_answers.toString());
                                            buf.close();

                                            Toast.makeText(End.this, getResources().getString(R.string.dataupdated), Toast.LENGTH_LONG).show();

                                            Intent intent = new Intent(End.this, Dashboard.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.putExtra("EXIT", true);
                                            startActivity(intent);
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                            System.out.println(getString(R.string.file_not_deleted));
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Intent intent = new Intent(End.this, Dashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra(getResources().getString(R.string.exit), true);
                            startActivity(intent);
                        }
                    }
                });
                lytQuestions.addView(btn_edit);
            }else{
                lytQuestions.addView(fab);
            }
        }else{
            lytQuestions.addView(fab);
        }//Hapa

    }

    public static void setMargins (View v, int l, int t, int r, int b) {
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LocationRequest.PRIORITY_HIGH_ACCURACY:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Toast.makeText(End.this, getResources().getString(R.string.location_enabled_succcesfully), Toast.LENGTH_LONG).show();
                        // All required changes were successfully made
                        //Log.i(TAG, "onActivityResult: GPS Enabled by user");
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(End.this, getResources().getString(R.string.location_disabled), Toast.LENGTH_LONG).show();
                        // The user was asked to change settings, but chose not to
                        //Log.i(TAG, "onActivityResult: User rejected GPS request");
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    public boolean setup4(){
        LocationManager lm = (LocationManager)End.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(End.this)
                    .setMessage(getResources().getString(R.string.Gps_network_not_enable))
                    .setPositiveButton(getResources().getString(R.string.Open_Location_Settings), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            End.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.cancel),null)
                    .show();
        }

        return gps_enabled;
    }

    public void sendDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(End.this);

        builder.setTitle(getResources().getString(R.string.confirm));
        builder.setMessage(getResources().getString(R.string.areyousuretosend));

        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                DateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
                Date dateobj = new Date();
                String todayDate = df.format(dateobj);
                if(!answer_code.equals("")){
                    general.AddObjectToResultArray("CHAD14", answer_code, End.this);
                    if(!setup4()){
                        Toast.makeText(End.this, getResources().getString(R.string.enable_location), Toast.LENGTH_LONG).show();
                    }else{
                        JSONObject json = null;
                        try {
                            String questions = general.getFile("questionnaire", End.this);
                            json = new JSONObject(questions);
                            String full_name = json.getString("full_name");
                            int id = json.getInt("id");
                            general.CreateMainObject("user_id", String.valueOf(id), End.this);
                            general.CreateMainObject("unique_code", String.valueOf(System.currentTimeMillis()), End.this);
                            JSONArray jsonArray = new JSONArray(general.getFile("ResultArray", End.this));
                            general.CreateMainObject2("results", jsonArray, End.this);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if(general.isNetworkAvailable(End.this)){
                                Log.i("mkapa","passed netwokk");
                                general.saveData(End.this);
                            }else{

                                JSONObject object = new JSONObject(general.getFile("MainObject", End.this));
                                String respondent = object.getString("respondent");
                                JSONObject house_hold = object.getJSONObject("house_hold");

                                   Filling filling = new Filling();
                                   //int house_hold_id  = house_hold.getInt("house_hold_id");
                                filling.saveFileToFolderContext(respondent+" "+todayDate, general.getFile("MainObject", End.this), End.this);
                                //filling.saveFileToFolderContext(System.currentTimeMillis()+" "+todayDate, general.getFile("MainObject", End.this), End.this);

                            }

                        } catch (UnsupportedEncodingException | JSONException e) {
                            saveDialog();
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    Toast.makeText(End.this, getResources().getString(R.string.select_statust_interview), Toast.LENGTH_SHORT).show();
                }
            }

        }).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }

        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void saveDialog() {
        DateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm:ss");
        Date dateobj = new Date();
        final String todayDate = df.format(dateobj);
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(End.this);
        View promptsView = li.inflate(R.layout.save_dialog, null);

        final EditText filename = promptsView.findViewById(R.id.filename);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                End.this);
        alertDialogBuilder.setTitle(getResources().getString(R.string.save_file));
        alertDialogBuilder.setIcon(getDrawable(R.drawable.survey));
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(End.this.getResources().getString(R.string.save),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                if(!filename.getText().toString().equals("")){
                                    Filling filling = new Filling();
                                    filling.saveFileToFolderContext(filename.getText().toString()+" "+todayDate, general.getFile("MainObject", End.this), End.this);

                                }
                            }
                        })
                .setNegativeButton(End.this.getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //go back
                                sendDialog();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
        new AlertDialog.Builder(End.this)
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
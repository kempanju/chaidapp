package com.example.chad;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Part2 extends AppCompatActivity {

    private static final Set<String> CODES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("CHAD5")));

    General general;
    ProgressDialog p;

    LinearLayout lytQuestions;

    String answer_code = "";
    CharSequence other = "";

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

        String questions = general.getFile("questionnaire", Part2.this);
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

        FloatingActionButton fab = new FloatingActionButton(Part2.this);
        fab.setId(R.id.fab);
        fab.setImageResource(R.drawable.next_icon);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.gravity = Gravity.CENTER_HORIZONTAL;

        fab.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        ));
        fab.setLayoutParams(params);
        setMargins(fab, 0, 20, 0, 10);
        fab.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (extras != null) {
                    if (extras.containsKey("FILENAME")) {
                        if(!answer_code.equals("")){
                            JSONObject object_answers;
                            JSONArray array_results;
                            JSONObject object1;
                            try {
                                object_answers = new JSONObject(string_object_answers);
                                array_results = object_answers.getJSONArray("results");

                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("code", "CHAD5");
                                jsonObject.put("answer_code", answer_code);
                                jsonObject.put("comment", "None");

                                for(int i=0; i<array_results.length(); i++){
                                    object1 = array_results.getJSONObject(i);
                                    if(object1.getString("code").equals("CHAD5")){
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

                                            Toast.makeText(Part2.this, getString(R.string.dataupdated), Toast.LENGTH_LONG).show();

                                            Intent intent = new Intent(Part2.this, Part68.class);
                                            intent.putExtra("FILENAME", filename);
                                            intent.putExtra("ANSWERS", string_object_answers);
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
                            Intent intent = new Intent(Part2.this, Part68.class);
                            intent.putExtra("FILENAME", filename);
                            intent.putExtra("ANSWERS", string_object_answers);
                            startActivity(intent);
                        }
                    }else{
                        if(!answer_code.equals("")){
                            general.AddObjectToResultArray("CHAD5", answer_code, Part2.this);
                            Intent intent = new Intent(Part2.this, Part68.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(Part2.this, R.string.select_type_meeting, Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    if(!answer_code.equals("")){
                        general.AddObjectToResultArray("CHAD5", answer_code, Part2.this);
                        Intent intent = new Intent(Part2.this, Part68.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(Part2.this, R.string.select_type_meeting, Toast.LENGTH_SHORT).show();
                    }
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

                if (lytQuestions != null) {
                    lytQuestions.addView(textView);
                }
                final RadioGroup rg = new RadioGroup(Part2.this);
                RadioButton spinner;
                final EditText editText = new EditText(this);
                String code = "";
                String opt = json_data.getString("options");
                code = json_data.getString("code");
                JSONArray optArray = new JSONArray(opt);
                final String[] personNames = new String[optArray.length()];
                for(int j=0; j<optArray.length(); j++){
                    final JSONObject opt_data = optArray.getJSONObject(j);
                    if(opt_data.getString("code").equals("CHAD5E")){

                        editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
                        setMargins(editText, 0, 0, 0, 30);
                        editText.setTextSize(18);
                        editText.setBackgroundResource(R.drawable.corner_edittext);
                        editText.setHint(opt_data.getString("name_en"));
                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                if(charSequence.length() != 0){
                                    other = charSequence;
                                    int id = rg.getCheckedRadioButtonId();
                                    RadioButton r = (RadioButton) findViewById(id);
                                    if(r != null){
                                        r.setChecked(false);
                                        answer_code = "";
                                    }
                                }

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {}
                        });
                        rg.addView(editText);
                    }else{
                        spinner = new RadioButton(this);
                        spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 90));
                        setMargins(spinner, 0, 0, 0, 10);
                        spinner.setId(View.generateViewId());
                        if (extras != null) {
                            if (extras.containsKey("FILENAME")) {
                                JSONObject object_answers = new JSONObject(string_object_answers);
                                JSONArray array_results = object_answers.getJSONArray("results");
                                for (int k = 0; k < array_results.length(); k++) {
                                    JSONObject opt_data_results = array_results.getJSONObject(k);
                                    if (opt_data_results.getString("code").equals("CHAD5")) {
                                        String answer_code = opt_data_results.getString("answer_code");
                                        if (answer_code.equals(opt_data.getString("code"))) {
                                            spinner.setChecked(true);
                                            spinner.setTextColor(getResources().getColor(R.color.orange1));
                                        }
                                    }
                                }
                            }
                        }//Hapa
                        spinner.setText(opt_data.getString("name_en").toString());
                        spinner.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                if ( ((RadioButton)v).isChecked() ) {
                                    try {
                                        //Toast.makeText(Part16.this, opt_data.getString("code"), Toast.LENGTH_SHORT).show();
                                        answer_code = opt_data.getString("code");
                                        editText.setText("");
                                        other = "";
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        rg.addView(spinner);
                    }
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
                btn_edit.setText(getString(R.string.edit));
                setMargins(btn_edit, 0, 30, 0, 10);
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
                                jsonObject.put("code", "CHAD5");
                                jsonObject.put("answer_code", answer_code);
                                jsonObject.put("comment", "None");

                                for(int i=0; i<array_results.length(); i++){
                                    object1 = array_results.getJSONObject(i);
                                    if(object1.getString("code").equals("CHAD5")){
                                        array_results.remove(i);
                                        array_results.put(jsonObject);
                                        break;
                                    }
                                }
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

                                            Toast.makeText(Part2.this, getString(R.string.dataupdated), Toast.LENGTH_LONG).show();

                                            Intent intent = new Intent(Part2.this, Dashboard.class);
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
                            Intent intent = new Intent(Part2.this, Dashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("EXIT", true);
                            startActivity(intent);
                        }
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
                    general.goBackDialoag(Part2.this);
                }
            });

            lytQuestions.addView(btn_back);
        }//Hapa
        lytQuestions.addView(fab);
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

    public void requestPermision(){
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.VIBRATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.SEND_SMS};
        if(!hasPermissions(getApplicationContext(), PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
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
        new AlertDialog.Builder(Part2.this)
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
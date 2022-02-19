package com.example.chad;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

public class Part46 extends AppCompatActivity {

    private static final Set<String> CODES_WITH_EDIT_TEXT = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "CHAD12")));
    General general;
    ProgressDialog p;

    LinearLayout lytQuestions;
    String answer_code = "";
    CharSequence other;

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

        String questions = general.getFile("questionnaire", Part46.this);
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

        FloatingActionButton fab = new FloatingActionButton(Part46.this);
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
                            JSONObject object_answers = null;
                            try {
                                object_answers = new JSONObject(string_object_answers);

                                object_answers.remove("respondent");
                                object_answers.put("respondent", answer_code);
                                String respondent = object_answers.getString("respondent");

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
                                            File file = new File(path + respondent+" "+todayDate);
                                            if (!file.exists()) {
                                                file.createNewFile();  // create file if not exist
                                            }
                                            BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
                                            buf.append(object_answers.toString());
                                            buf.close();
                                            Toast.makeText(Part46.this, getString(R.string.dataupdated), Toast.LENGTH_LONG).show();

                                            string_object_answers = object_answers.toString();
                                            Intent intent = new Intent(Part46.this, Part43.class);
                                            intent.putExtra("FILENAME", filename);
                                            intent.putExtra("ANSWERS", string_object_answers);
                                            startActivity(intent);
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                            System.out.println(getString(R.string.file_not_deleted));
                                        }
                                    } else {
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Intent intent = new Intent(Part46.this, Part43.class);
                            intent.putExtra("FILENAME", filename);
                            intent.putExtra("ANSWERS", string_object_answers);
                            startActivity(intent);
                        }
                    }else{
                        if(!answer_code.equals("")){
                            general.CreateMainObject("respondent", answer_code, Part46.this);
                            Intent intent = new Intent(Part46.this, Part43.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(Part46.this, R.string.please_respondent_name, Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    if(!answer_code.equals("")){
                        general.CreateMainObject("respondent", answer_code, Part46.this);
                        Intent intent = new Intent(Part46.this, Part43.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(Part46.this, getString(R.string.please_respondent_name), Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });


        for(int i=0; i<questionnaire.length(); i++){
            JSONObject json_data = questionnaire.getJSONObject(i);

            if(CODES_WITH_EDIT_TEXT.contains(json_data.getString("code"))){
                final TextView textView2 = new TextView(this);
                textView2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                setMargins(textView2, 0, 0, 0, 10);
                textView2.setText(json_data.getString("name_sw"));
                textView2.setTextSize(18);
                textView2.setTypeface(textView2.getTypeface(), Typeface.BOLD);
                textView2.setTextColor(getResources().getColor(R.color.black));

                final EditText editText = new EditText(this);
                editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                setMargins(editText, 0, 0, 0, 10);
                editText.setTextSize(18);
                if (extras != null) {
                    if (extras.containsKey("FILENAME")) {
                        JSONObject object_answers = new JSONObject(string_object_answers);
                        String respondent = object_answers.getString("respondent");
                        editText.setText(respondent);
                        //answer_code = respondent;
                    }
                }//Hapa
                editText.setTextColor(getResources().getColor(R.color.black));
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(charSequence.length() != 0){
                            other = charSequence;
                            answer_code = "";
                            answer_code = other.toString();
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {}
                });

                //final Spinner spinner = new Spinner(this);


                if (lytQuestions != null) {
                    lytQuestions.addView(textView2);
                    if(CODES_WITH_EDIT_TEXT.contains(json_data.getString("code"))){
                        lytQuestions.addView(editText);
                    }
                }
            }


        }
        if (extras != null) {
            if (extras.containsKey("FILENAME")) {
                main_layout = (ScrollView) findViewById(R.id.main_layout);//change this
                main_layout.setBackgroundColor(getResources().getColor(R.color.grey));

                lytQuestions = (LinearLayout) findViewById(R.id.lytQuestions);
                Button btn_edit = new Button(this);
                btn_edit.setText(getString(R.string.edit));
                setMargins(btn_edit, 0, 10, 0, 10);
                btn_edit.setBackgroundResource(R.drawable.orange_corner_gradient);
                btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JSONObject object_answers = null;
                        String txt = "";
                        View v1 = lytQuestions.getChildAt(1);
                        if (v1 instanceof EditText) {
                            txt = ((EditText)v1).getText().toString();
                        }
                        if(txt.equals("") && answer_code.equals("")){
                            Toast.makeText(Part46.this, getString(R.string.please_respondent_name), Toast.LENGTH_SHORT).show();
                        }else{
                            if(!answer_code.equals("")){
                                try {
                                    object_answers = new JSONObject(string_object_answers);

                                    object_answers.remove("respondent");
                                    object_answers.put("respondent", answer_code);
                                    String respondent = object_answers.getString("respondent");

                                    JSONObject house_hold = object_answers.getJSONObject("house_hold");

                                    File yourDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Answers/");

                                    File fdelete = new File(yourDir+"/"+filename);
                                    if (fdelete.exists()) {
                                        if (fdelete.delete()) {
                                            Filling filling = new Filling();
                                            filling.saveFileToFolderContext(respondent+" "+todayDate, object_answers.toString(), Part46.this);
                                        } else {
                                            System.out.println(getString(R.string.file_not_deleted));
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            Intent intent = new Intent(Part46.this, Dashboard.class);
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
            btn_back.setText(getString(R.string.go_back_to_dashboard));
            btn_back.setBackgroundResource(R.drawable.cyan_corner_gradient);
            setMargins(btn_back, 0, 10, 0, 10);
            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    general.goBackDialoag(Part46.this);
                }
            });

            lytQuestions.addView(btn_back);
        }//Hapa
        assert lytQuestions != null;
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
        new AlertDialog.Builder(Part46.this)
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
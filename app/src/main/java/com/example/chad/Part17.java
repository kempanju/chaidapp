package com.example.chad;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Part17 extends AppCompatActivity{

    private static final Set<String> CODES_WITH_EDIT_TEXT = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "CHAD18A")));


    General general;
    ProgressDialog p;

    LinearLayout lytQuestions;
    String answer_code = "";

    String filename = "";
    String string_object_answers = "";
    Bundle extras = null;
    ScrollView main_layout;

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

        String questions = general.getFile("questionnaire", Part17.this);
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

        FloatingActionButton fab = new FloatingActionButton(Part17.this);
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
                        try {
                            JSONObject object = new JSONObject(string_object_answers);
                            JSONArray array = object.getJSONArray("results");
                            JSONObject obj;
                            for(int a=0; a<array.length(); a++){
                                obj = array.getJSONObject(a);
                                String code = obj.getString("code");
                                if(code.equals("CHAD18D")){
                                    String ans_code = obj.getString("answer_code");
                                    if(ans_code.equals("CHAD18D1")){
                                        general.createFabBlockString(Part17.this, extras, filename, Part25.class,answer_code,string_object_answers,"pregnant_woman", "last_menstrual" );
                                    }else{
                                        general.createFabBlockString(Part17.this, extras, filename, Dashboard.class,answer_code,string_object_answers,"pregnant_woman", "last_menstrual" );
                                    }
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        general.CreateSmallObjectForMainObjectString(Part17.this, "pregnant_woman", "last_menstrual", answer_code);
                        Intent intent = new Intent(Part17.this, Part31.class);
                        startActivity(intent);
                    }
                }else{
                    general.CreateSmallObjectForMainObjectString(Part17.this, "pregnant_woman", "last_menstrual", answer_code);
                    Intent intent = new Intent(Part17.this, Part31.class);
                    startActivity(intent);
                }



            }
        });


        for(int i=0; i<questionnaire.length(); i++){
            JSONObject json_data = questionnaire.getJSONObject(i);
            if(CODES_WITH_EDIT_TEXT.contains(json_data.getString("code"))){
                final TextView textView = new TextView(this);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                setMargins(textView, 0, 0, 0, 10);
                textView.setText(json_data.getString("name_sw"));
                textView.setTextSize(18);
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                textView.setTextColor(getResources().getColor(R.color.black));

                final TextView textView2 = new TextView(this);
                textView2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                setMargins(textView2, 0, 0, 0, 10);
                textView2.setText(json_data.getString("name_sw"));
                textView2.setTextSize(18);
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                textView2.setTextColor(getResources().getColor(R.color.black));

                final Calendar c = Calendar.getInstance();
                final Button button_date = new Button(this);
                button_date.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                button_date.setTextSize(18);
                button_date.setText(R.string.select_date);
                button_date.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                //button_date.setTextColor(getResources().getColor(R.color.black));
                if (extras != null) {
                    if (extras.containsKey("FILENAME")) {
                        JSONObject object_answers = new JSONObject(string_object_answers);
                        JSONObject pregnant_woman_obj = object_answers.getJSONObject("pregnant_woman");
                        button_date.setText(pregnant_woman_obj.getString("last_menstrual"));
                        button_date.setTextColor(getResources().getColor(R.color.orange1));
                    }else{
                        button_date.setTextColor(getResources().getColor(R.color.black));
                    }
                }else{
                    button_date.setTextColor(getResources().getColor(R.color.black));
                }
                setMargins(button_date, 0, 25, 0, 50);
                button_date.setOnClickListener(new View.OnClickListener() {
                    public void setReturnDate(int year, int month, int day) {
                        button_date.setText(""+year+"-"+(month+1)+"-"+day);
                        answer_code = button_date.getText().toString();
                    }
                    @Override
                    public void onClick(View view) {
                        Dialog datePickerDialog = new DatePickerDialog(Part17.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                                .get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                setReturnDate(((DatePickerDialog) dialog).getDatePicker().getYear(),
                                        ((DatePickerDialog) dialog).getDatePicker().getMonth(), ((DatePickerDialog) dialog)
                                                .getDatePicker().getDayOfMonth());
                            }
                        });
                        datePickerDialog.show();
                    }
                });

                final Button button_clear = new Button(this);
                button_clear.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                button_clear.setTextSize(18);
                button_clear.setText(getResources().getString(R.string.clear));
                button_clear.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                button_clear.setTextColor(getResources().getColor(R.color.black));
                setMargins(button_clear, 0, 25, 0, 50);
                button_clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_date.setText(R.string.select_date);
                        answer_code = "";
                    }
                });
                CheckBox spinner;

                String opt = json_data.getString("options");
                JSONArray optArray = new JSONArray(opt);
                for(int j=0; j<optArray.length(); j++){
                    JSONObject opt_data = optArray.getJSONObject(j);
                    spinner = new CheckBox(this);
                    spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 90));
                    setMargins(spinner, 0, 0, 0, 10);
                    spinner.setText(opt_data.getString("name_en"));
                    lytQuestions.addView(spinner);
                }

                if (lytQuestions != null) {
                    lytQuestions.addView(textView2);
                    lytQuestions.addView(button_date);
                    lytQuestions.addView(button_clear);
                }
            }


        }
        assert lytQuestions != null;
        if (extras != null) {
            if (extras.containsKey("FILENAME")) {
                main_layout = findViewById(R.id.main_layout);//change this
                main_layout.setBackgroundColor(getResources().getColor(R.color.grey));

                lytQuestions = findViewById(R.id.lytQuestions);
                Button btn_edit = new Button(this);
                btn_edit.setText(R.string.edit);
                setMargins(btn_edit, 0, 10, 0, 10);
                btn_edit.setBackgroundResource(R.drawable.orange_corner_gradient);
                btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        general.createEditBlockString(Part17.this, filename, answer_code, string_object_answers, "pregnant_woman", "last_menstrual" );
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
                    general.goBackDialoag(Part17.this);
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

    public void finishDialog(final String filename, final String string_object_answers) {
        final Filling filling = new Filling();
        AlertDialog.Builder builder = new AlertDialog.Builder(Part17.this);

        builder.setTitle("SAVE");
        builder.setMessage(R.string.saving_file);

        builder.setPositiveButton(R.string.save,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        filling.saveFileToFolderContext2(filename, string_object_answers, Part17.this);
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }

        });

        AlertDialog alert = builder.create();
        alert.show();
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
        new AlertDialog.Builder(Part17.this)
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
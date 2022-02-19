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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Part56 extends AppCompatActivity {

    private static final Set<String> CODES_WITH_EDIT_TEXT = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "CHAD24"
    )));

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

        String questions = general.getFile("questionnaire", Part56.this);
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

        FloatingActionButton fab = new FloatingActionButton(Part56.this);
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
        setMargins(fab, 0, 0, 0, 10);
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
                                jsonObject.put("code", "CHAD24");
                                jsonObject.put("answer_code", answer_code);
                                jsonObject.put("comment", "None");

                                for(int i=0; i<array_results.length(); i++){
                                    object1 = array_results.getJSONObject(i);
                                    if(object1.getString("code").equals("CHAD24")){
                                        array_results.remove(i);
                                        array_results.put(jsonObject);
                                        break;
                                    }
                                }

                                object_answers.remove("results");
                                object_answers.put("results", array_results);

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
                                            Toast.makeText(Part56.this, getString(R.string.dataupdated), Toast.LENGTH_LONG).show();

                                            string_object_answers = object_answers.toString();
                                            Intent intent = new Intent(Part56.this, End.class);
                                            intent.putExtra("FILENAME", filename);
                                            intent.putExtra("ANSWERS", string_object_answers);
                                            startActivity(intent);
/*                                            JSONObject obj;
                                            JSONObject object2 = new JSONObject(string_object_answers);
                                            JSONArray arr = object2.getJSONArray("results");
                                            for(int b=0; b<arr.length(); b++){
                                                obj = arr.getJSONObject(b);
                                                String code = obj.getString("code");
                                                if(code.equals("CHAD23B")){
                                                    String ans_code = obj.getString("answer_code");
                                                    if(ans_code.equals("CHAD23B1")){
                                                        Intent intent = new Intent(Part56.this, Part35.class);
                                                        intent.putExtra("FILENAME", filename);
                                                        intent.putExtra("ANSWERS", string_object_answers);
                                                        startActivity(intent);
                                                    }else{
                                                        Intent intent = new Intent(Part56.this, Part21.class);
                                                        intent.putExtra("FILENAME", filename);
                                                        intent.putExtra("ANSWERS", string_object_answers);
                                                        startActivity(intent);
                                                    }
                                                }
                                            }*/

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
                            try {
                                Intent intent = new Intent(Part56.this, End.class);
                                intent.putExtra("FILENAME", filename);
                                intent.putExtra("ANSWERS", string_object_answers);
                                startActivity(intent);
/*                                JSONObject obj;
                                JSONObject object2 = new JSONObject(string_object_answers);
                                JSONArray arr = object2.getJSONArray("results");
                                for(int b=0; b<arr.length(); b++){
                                    obj = arr.getJSONObject(b);
                                    String code = obj.getString("code");
                                    if(code.equals("CHAD23B")){
                                        String ans_code = obj.getString("answer_code");
                                        if(ans_code.equals("CHAD23B1")){
                                            Intent intent = new Intent(Part56.this, Part35.class);
                                            intent.putExtra("FILENAME", filename);
                                            intent.putExtra("ANSWERS", string_object_answers);
                                            startActivity(intent);
                                        }else{
                                            Intent intent = new Intent(Part56.this, Part21.class);
                                            intent.putExtra("FILENAME", filename);
                                            intent.putExtra("ANSWERS", string_object_answers);
                                            startActivity(intent);
                                        }
                                    }
                                }*/
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }else{
                        general.AddObjectToResultArray("CHAD24", answer_code, Part56.this);
                        Intent intent = new Intent(Part56.this, Part7.class);
                        startActivity(intent);
                    }
                }else{
                    general.AddObjectToResultArray("CHAD24", answer_code, Part56.this);
                    Intent intent = new Intent(Part56.this, Part7.class);
                    startActivity(intent);
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

                final Calendar c = Calendar.getInstance();
                final Button button_date = new Button(this);
                button_date.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                button_date.setTextSize(18);
                button_date.setText(getString(R.string.select_date));
                button_date.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                if (extras != null) {
                    if (extras.containsKey("FILENAME")) {
                        JSONObject object_answers = new JSONObject(string_object_answers);
                        JSONArray array_results = object_answers.getJSONArray("results");
                        for (int k = 0; k < array_results.length(); k++) {
                            JSONObject opt_data_results = array_results.getJSONObject(k);
                            if (opt_data_results.getString("code").equals("CHAD24")) {
                                String ans_code = opt_data_results.getString("answer_code");
                                try {
                                    button_date.setText(ans_code);
                                    button_date.setTextColor(getResources().getColor(R.color.orange1));
                                    this.answer_code = ans_code;
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }else{
                        button_date.setTextColor(getResources().getColor(R.color.black));
                    }
                }else{
                    button_date.setTextColor(getResources().getColor(R.color.black));
                }
                setMargins(button_date, 0, 25, 0, 50);
                button_date.setOnClickListener(new View.OnClickListener() {
                    public void setReturnDate(int year, int month, int day) {
                        button_date.setText(""+year+"-"+month+"-"+day);
                        answer_code = button_date.getText().toString();
                    }
                    @Override
                    public void onClick(View view) {
                        Dialog datePickerDialog = new DatePickerDialog(Part56.this, new DatePickerDialog.OnDateSetListener() {
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
                if (lytQuestions != null) {
                    lytQuestions.addView(textView2);
                    lytQuestions.addView(button_date);
                }
            }
        }

        if (extras != null) {
            if (extras.containsKey("FILENAME")) {
                main_layout = findViewById(R.id.main_layout);//change this
                main_layout.setBackgroundColor(getResources().getColor(R.color.grey));

                lytQuestions = (LinearLayout) findViewById(R.id.lytQuestions);
                Button btn_edit = new Button(this);
                btn_edit.setText(getString(R.string.edit));
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
                                jsonObject.put("code", "CHAD24");
                                jsonObject.put("answer_code", answer_code);
                                jsonObject.put("comment", "None");

                                for(int i=0; i<array_results.length(); i++){
                                    object1 = array_results.getJSONObject(i);
                                    if(object1.getString("code").equals("CHAD24")){
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

                                            Toast.makeText(Part56.this, getString(R.string.dataupdated), Toast.LENGTH_LONG).show();

                                            Intent intent = new Intent(Part56.this, Dashboard.class);
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
                            Intent intent = new Intent(Part56.this, Dashboard.class);
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
                    general.goBackDialoag(Part56.this);
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
        new AlertDialog.Builder(Part56.this)
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
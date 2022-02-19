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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Part25 extends AppCompatActivity {
    private static final Set<String> CODES_WITH_EDIT_TEXT = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("CHAD18E")));

    General general;
    ProgressDialog p;

    LinearLayout lytQuestions;
    ArrayList<String> answer_code = new ArrayList<>();

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

        String questions = general.getFile("questionnaire", Part25.this);
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

        FloatingActionButton fab = new FloatingActionButton(Part25.this);
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
                        if(answer_code.size() != 0){
                            String ans = answer_code.toString().replaceAll("[\\[\\](){}]","").trim();
                            JSONObject object_answers;
                            JSONArray array_results;
                            JSONObject object1;
                            try {
                                object_answers = new JSONObject(string_object_answers);
                                array_results = object_answers.getJSONArray("results");

                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("code", "CHAD18E");
                                jsonObject.put("answer_code", ans);
                                jsonObject.put("comment", "None");

                                for(int i=0; i<array_results.length(); i++){
                                    object1 = array_results.getJSONObject(i);
                                    if(object1.getString("code").equals("CHAD18E")){
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

                                            Toast.makeText(Part25.this, R.string.dataupdated, Toast.LENGTH_LONG).show();

                                            Intent intent = new Intent(Part25.this, Part33.class);
                                            intent.putExtra("FILENAME", filename);
                                            intent.putExtra("ANSWERS", string_object_answers);
                                            startActivity(intent);
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                            System.out.println(R.string.file_not_deleted);
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Intent intent = new Intent(Part25.this, Part33.class);
                            intent.putExtra("FILENAME", filename);
                            intent.putExtra("ANSWERS", string_object_answers);
                            startActivity(intent);
                        }
                    }else{
                        String ans = answer_code.toString().replaceAll("[\\[\\](){}]","").trim();
                        general.AddObjectToResultArray("CHAD18E", ans.trim(), Part25.this);
                        Intent intent = new Intent(Part25.this, Part33.class);
                        startActivity(intent);
                    }
                }else{
                    String ans = answer_code.toString().replaceAll("[\\[\\](){}]","").trim();
                    general.AddObjectToResultArray("CHAD18E", ans.trim(), Part25.this);
                    Intent intent = new Intent(Part25.this, Part33.class);
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

                    EditText editText = null;
                    if (lytQuestions != null) {
                        lytQuestions.addView(textView);
                    }
                    String opt = json_data.getString("options");
                    JSONArray optArray = new JSONArray(opt);
                    final Button[] button_date  = new Button[optArray.length()];

                    for(int j=0; j<optArray.length(); j++){
                        final JSONObject opt_data = optArray.getJSONObject(j);
                        final Calendar c = Calendar.getInstance();
                        button_date[j] = new Button(this);
                        button_date[j].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        button_date[j].setTextSize(18);
                        button_date[j].setText(opt_data.getString("name_en"));
                        button_date[j].setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        if (extras != null) {
                            if (extras.containsKey("FILENAME")) {
                                JSONObject object_answers = new JSONObject(string_object_answers);
                                JSONArray array_results = object_answers.getJSONArray("results");
                                for (int k = 0; k < array_results.length(); k++) {
                                    JSONObject opt_data_results = array_results.getJSONObject(k);
                                    if (opt_data_results.getString("code").equals("CHAD18E")) {
                                        String answer_code = opt_data_results.getString("answer_code");
                                        String[] elements = answer_code.split(",");

                                        try {
                                            button_date[j].setText(elements[j].trim());
                                            button_date[j].setTextColor(getResources().getColor(R.color.orange1));
                                            this.answer_code.add(elements[j].trim());
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }


                                    }
                                }
                            }else{
                                button_date[j].setTextColor(getResources().getColor(R.color.black));
                            }
                        }else{
                            button_date[j].setTextColor(getResources().getColor(R.color.black));
                        }
                        setMargins(button_date[j], 0, 25, 0, 50);
                        final int finalJ = j;
                        button_date[j].setOnClickListener(new View.OnClickListener() {
                            public void setReturnDate(int year, int month, int day) {
                                button_date[finalJ].setText(""+year+"-"+(month+1)+"-"+day);
                                answer_code.add(button_date[finalJ].getText().toString());
                            }
                            @Override
                            public void onClick(View view) {
                                Dialog datePickerDialog = new DatePickerDialog(Part25.this, new DatePickerDialog.OnDateSetListener() {
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
                        lytQuestions.addView(button_date[j]);
                        //personNames[j] = opt_data.getString("name_en");
                    }

                final Button button_clear = new Button(this);
                button_clear.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                button_clear.setTextSize(18);
                button_clear.setText(R.string.clear);
                button_clear.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                button_clear.setTextColor(getResources().getColor(R.color.black));
                setMargins(button_clear, 0, 25, 0, 50);
                button_clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        button_date[0].setText("HUDHURIO LA KWANZA");
                        button_date[1].setText("HUDHURIO LA PILI");
                        button_date[2].setText("HUDHURIO LA TATU");
                        button_date[3].setText("HUDHURIO LA NNE NA ZAIDI");
                        answer_code.clear();
                    }
                });
                lytQuestions.addView(button_clear);

            }


        }
        assert lytQuestions != null;
        if (extras != null) {
            if (extras.containsKey("FILENAME")) {
                main_layout = (ScrollView) findViewById(R.id.main_layout);//change this
                main_layout.setBackgroundColor(getResources().getColor(R.color.grey));

                lytQuestions = (LinearLayout) findViewById(R.id.lytQuestions);
                Button btn_edit = new Button(this);
                btn_edit.setText(R.string.edit);
                setMargins(btn_edit, 0, 10, 0, 10);
                btn_edit.setBackgroundResource(R.drawable.orange_corner_gradient);
                btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(answer_code.size() != 0){
                            String ans = answer_code.toString().replaceAll("[\\[\\](){}]","").trim();
                            JSONObject object_answers;
                            JSONArray array_results;
                            JSONObject object1;
                            try {
                                object_answers = new JSONObject(string_object_answers);
                                array_results = object_answers.getJSONArray("results");

                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("code", "CHAD18E");
                                jsonObject.put("answer_code", ans);
                                jsonObject.put("comment", "None");

                                for(int i=0; i<array_results.length(); i++){
                                    object1 = array_results.getJSONObject(i);
                                    if(object1.getString("code").equals("CHAD18E")){
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

                                            Toast.makeText(Part25.this, R.string.dataupdated, Toast.LENGTH_LONG).show();

                                            Intent intent = new Intent(Part25.this, Dashboard.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.putExtra("EXIT", true);
                                            startActivity(intent);
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                            System.out.println(R.string.file_not_deleted);
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Intent intent = new Intent(Part25.this, Dashboard.class);
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
                    general.goBackDialoag(Part25.this);
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
        new AlertDialog.Builder(Part25.this)
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
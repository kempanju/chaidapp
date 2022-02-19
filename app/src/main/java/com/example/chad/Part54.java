package com.example.chad;

import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Part54 extends AppCompatActivity {
    private static final Set<String> CODES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("CHAD23D")));


    General general;
    ProgressDialog p;

    LinearLayout lytQuestions;
    String answer_code = "";

    String filename = "";
    String string_object_answers = "";
    Bundle extras = null;
    ScrollView main_layout;

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

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

        String questions = general.getFile("questionnaire", Part54.this);
        try {
            createAQuestion(questions);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void createAQuestion(String questions) throws JSONException {
        JSONObject json = new JSONObject(questions);
        JSONArray questionnaire = json.getJSONArray("questionnaire");
        lytQuestions = findViewById(R.id.lytQuestions);

        FloatingActionButton fab = new FloatingActionButton(Part54.this);
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
                        if (!answer_code.equals("")) {
                            JSONObject object_answers;
                            JSONArray array_results;
                            JSONObject object1;
                            try {
                                object_answers = new JSONObject(string_object_answers);
                                array_results = object_answers.getJSONArray("results");

                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("code", "CHAD23D");
                                jsonObject.put("answer_code", answer_code);
                                jsonObject.put("comment", "None");

                                for (int i = 0; i < array_results.length(); i++) {
                                    object1 = array_results.getJSONObject(i);
                                    if (object1.getString("code").equals("CHAD23D")) {
                                        array_results.remove(i);
                                        array_results.put(jsonObject);
                                        break;
                                    }
                                }

                                object_answers.remove("results");
                                object_answers.put("results", array_results);

                                File yourDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Answers/");

                                File fdelete = new File(yourDir + "/" + filename);
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

                                            Toast.makeText(Part54.this, getString(R.string.dataupdated), Toast.LENGTH_LONG).show();

                                            string_object_answers = object_answers.toString();
                                            JSONObject obj;
                                            JSONObject object2 = new JSONObject(string_object_answers);
                                            JSONArray arr = object2.getJSONArray("results");
                                            for (int b = 0; b < arr.length(); b++) {
                                                obj = arr.getJSONObject(b);
                                                String code = obj.getString("code");
                                                if (code.equals("CHAD23E")) {
                                                    String ans_code = obj.getString("answer_code");
                                                    if (ans_code.equals("CHAD23E1")) {
                                                        Intent intent = new Intent(Part54.this, Part22.class);
                                                        intent.putExtra("FILENAME", filename);
                                                        intent.putExtra("ANSWERS", string_object_answers);
                                                        startActivity(intent);
                                                    } else {
                                                        Intent intent = new Intent(Part54.this, End.class);//ilikuwa Part7
                                                        intent.putExtra("FILENAME", filename);
                                                        intent.putExtra("ANSWERS", string_object_answers);
                                                        startActivity(intent);
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            System.out.println(getString(R.string.file_not_deleted));
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                JSONObject obj;
                                JSONObject object2 = new JSONObject(string_object_answers);
                                JSONArray arr = object2.getJSONArray("results");
                                for (int b = 0; b < arr.length(); b++) {
                                    obj = arr.getJSONObject(b);
                                    String code = obj.getString("code");
                                    if (code.equals("CHAD23E")) {
                                        String ans_code = obj.getString("answer_code");
                                        if (ans_code.equals("CHAD23E1")) {
                                            Intent intent = new Intent(Part54.this, Part22.class);
                                            intent.putExtra("FILENAME", filename);
                                            intent.putExtra("ANSWERS", string_object_answers);
                                            startActivity(intent);
                                        } else {
                                            Intent intent = new Intent(Part54.this, End.class);
                                            intent.putExtra("FILENAME", filename);
                                            intent.putExtra("ANSWERS", string_object_answers);
                                            startActivity(intent);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        general.AddObjectToResultArray("CHAD23D", answer_code, Part54.this);
                        Intent intent = new Intent(Part54.this, Part55.class);
                        startActivity(intent);
                    }
                } else {
                    general.AddObjectToResultArray("CHAD23D", answer_code, Part54.this);
                    Intent intent = new Intent(Part54.this, Part55.class);
                    startActivity(intent);
                }

            }
        });

        for (int i = 0; i < questionnaire.length(); i++) {
            JSONObject json_data = questionnaire.getJSONObject(i);
            if (CODES.contains(json_data.getString("code"))) {
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
                //final Spinner spinner = new Spinner(this);
                RadioGroup rg = new RadioGroup(Part54.this);
                RadioButton spinner;
                final EditText editText = new EditText(this);
                String opt = json_data.getString("options");
                JSONArray optArray = new JSONArray(opt);
                for (int j = 0; j < optArray.length(); j++) {
                    final JSONObject opt_data = optArray.getJSONObject(j);
                    if (opt_data.getString("code").equals("CHAD23E4")) {
                        editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
                        setMargins(editText, 0, 0, 0, 30);
                        editText.setTextSize(18);
                        editText.setBackgroundResource(R.drawable.corner_edittext);
                        editText.setHint(opt_data.getString("name_en"));
                        rg.addView(editText);
                    } else {
                        spinner = new RadioButton(this);
                        spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 90));
                        setMargins(spinner, 0, 0, 0, 10);
                        spinner.setText(opt_data.getString("name_en"));
                        if (extras != null) {
                            if (extras.containsKey("FILENAME")) {
                                JSONObject object_answers = new JSONObject(string_object_answers);
                                JSONArray array_results = object_answers.getJSONArray("results");
                                for (int k = 0; k < array_results.length(); k++) {
                                    JSONObject opt_data_results = array_results.getJSONObject(k);
                                    if (opt_data_results.getString("code").equals("CHAD23D")) {
                                        String answer_code = opt_data_results.getString("answer_code");
                                        if (opt_data.getString("code").equals(answer_code)) {
                                            spinner.setChecked(true);
                                            spinner.setTextColor(getResources().getColor(R.color.orange1));
                                            this.answer_code = opt_data.getString("code");
                                        }
                                    }
                                }
                            }
                        }//Hapa
                        spinner.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                if (((RadioButton) v).isChecked()) {
                                    try {
                                        //Toast.makeText(Part16.this, opt_data.getString("code"), Toast.LENGTH_SHORT).show();
                                        answer_code = opt_data.getString("code");
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
        if (extras != null) {
            if (extras.containsKey("FILENAME")) {
                main_layout = findViewById(R.id.main_layout);//change this
                main_layout.setBackgroundColor(getResources().getColor(R.color.grey));

                lytQuestions = findViewById(R.id.lytQuestions);
                Button btn_edit = new Button(this);
                btn_edit.setText(getString(R.string.edit));
                setMargins(btn_edit, 0, 10, 0, 10);
                btn_edit.setBackgroundResource(R.drawable.orange_corner_gradient);
                btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!answer_code.equals("")) {
                            JSONObject object_answers;
                            JSONArray array_results;
                            JSONObject object1;
                            try {
                                object_answers = new JSONObject(string_object_answers);
                                array_results = object_answers.getJSONArray("results");

                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("code", "CHAD23D");
                                jsonObject.put("answer_code", answer_code);
                                jsonObject.put("comment", "None");

                                for (int i = 0; i < array_results.length(); i++) {
                                    object1 = array_results.getJSONObject(i);
                                    if (object1.getString("code").equals("CHAD23D")) {
                                        array_results.remove(i);
                                        array_results.put(jsonObject);
                                        break;
                                    }
                                }
                                File yourDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Answers/");

                                File fdelete = new File(yourDir + "/" + filename);
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

                                            Toast.makeText(Part54.this, getString(R.string.dataupdated), Toast.LENGTH_LONG).show();

                                            Intent intent = new Intent(Part54.this, Dashboard.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.putExtra("EXIT", true);
                                            startActivity(intent);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            System.out.println(getString(R.string.file_not_deleted));
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Intent intent = new Intent(Part54.this, Dashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("EXIT", true);
                            startActivity(intent);
                        }
                    }
                });
                lytQuestions.addView(btn_edit);
            }
        } else {
            Button btn_back = new Button(this);
            btn_back.setText(getString(R.string.go_back_to_dashboard));
            btn_back.setBackgroundResource(R.drawable.cyan_corner_gradient);
            setMargins(btn_back, 0, 10, 0, 10);
            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    general.goBackDialoag(Part54.this);
                }
            });

            lytQuestions.addView(btn_back);
        }//Hapa
        assert lytQuestions != null;
        lytQuestions.addView(fab);
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
        new AlertDialog.Builder(Part54.this)
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
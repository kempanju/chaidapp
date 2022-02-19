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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Part68 extends AppCompatActivity {

    private static final Set<String> CODES_WITH_EDIT_TEXT = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "CHAD43")));

    General general;
    ProgressDialog p;

    LinearLayout lytQuestions;
    String male = "";
    String female = "";
    CharSequence other;

    String filename = "";
    String string_object_answers = "";
    Bundle extras = null;
    ScrollView main_layout;
    Calendar today = Calendar.getInstance();


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

        String questions = general.getFile("questionnaire", Part68.this);
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

        FloatingActionButton fab = new FloatingActionButton(Part68.this);
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
                        JSONObject object_answers;
                        JSONArray array_results;
                        JSONObject object1;
                        try {
                            object_answers = new JSONObject(string_object_answers);
                            array_results = object_answers.getJSONArray("results");

                            View v1 = lytQuestions.getChildAt(1);
                            View v2 = lytQuestions.getChildAt(2);
                            if (v1 instanceof EditText) {
                                male = ((EditText)v1).getText().toString();
                            }
                            if (v2 instanceof EditText) {
                                female = ((EditText)v2).getText().toString();
                            }


                            for(int i=0; i<array_results.length(); i++){
                                object1 = array_results.getJSONObject(i);
                                if(object1.getString("code").equals("CHAD43")){
                                    String temp_male = "";
                                    String temp_female = "";

                                    for(int n=0; n<array_results.length(); n++){
                                        object1 = array_results.getJSONObject(n);
                                        if(object1.getString("code").equals("CHAD43")){//change this
                                            JSONArray array = object1.getJSONArray("answer_code");
                                            JSONObject object = null;
                                            for (int m=0; m<array.length(); m++){
                                                object = array.getJSONObject(m);
                                                if(object.getString("code").equals("CHAD43A")){
                                                    temp_male = object.getString("member_no");
                                                }else{
                                                    temp_female = object.getString("member_no");
                                                }
                                            }
                                            break;
                                        }
                                    }
                                    JSONObject jsonObject2;
                                    JSONArray jsonArray = new JSONArray();

                                    if(male.equals("") && female.equals("")){
                                        jsonObject2 = new JSONObject();
                                        jsonObject2.put("code", "CHAD43A");
                                        jsonObject2.put("member_no", "");
                                        jsonArray.put(jsonObject2);

                                        jsonObject2 = new JSONObject();
                                        jsonObject2.put("code", "CHAD43B");
                                        jsonObject2.put("member_no", "");
                                        jsonArray.put(jsonObject2);

                                        JSONObject jsonObject3 = new JSONObject();
                                        jsonObject3.put("code", "CHAD43");
                                        jsonObject3.put("answer_code", jsonArray);
                                        jsonObject3.put("comment", "None");

                                        array_results.remove(i);
                                        array_results.put(jsonObject3);

                                        Filling filling = new Filling();
                                        filling.writeToFile("ResultArray", array_results.toString(), Part68.this);
                                        general.CreateMainObject2("results", array_results, Part68.this);
                                        File yourDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Answers/");

                                        File fdelete = new File(yourDir+"/"+filename);
                                        if (fdelete.exists()) {
                                            if (fdelete.delete()) {
                                                //finishDialog(filename);
                                                if(filling.saveFileToFolderContext3(filename, general.getFile("MainObject", Part68.this), Part68.this)){
                                                    Intent intent = new Intent(Part68.this, Part41.class);
                                                    intent.putExtra("FILENAME", filename);
                                                    intent.putExtra("ANSWERS", string_object_answers);
                                                    startActivity(intent);
                                                }
                                            } else {
                                                System.out.println(getString(R.string.file_not_deleted));
                                            }
                                        }

                                    }else{
                                        if(!male.equals("") || !female.equals("")){

                                            try {
                                                jsonObject2 = new JSONObject();
                                                jsonObject2.put("code", "CHAD43B");


                                                if(temp_male.equals("") && male.equals("")){
                                                    jsonObject2.put("member_no", "");
                                                }else if(!male.equals("")){
                                                    jsonObject2.put("member_no", male);
                                                }else {
                                                    jsonObject2.put("member_no", "");
                                                }

                                                jsonArray.put(jsonObject2);

                                                jsonObject2 = new JSONObject();
                                                jsonObject2.put("code", "CHAD43A");
                                                if(temp_female.equals("") && female.equals("")){
                                                    jsonObject2.put("member_no", "");
                                                }else if(!female.equals("")){
                                                    jsonObject2.put("member_no", female);
                                                }else {
                                                    jsonObject2.put("member_no", "");
                                                }

                                                jsonArray.put(jsonObject2);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            JSONObject jsonObject3 = new JSONObject();
                                            jsonObject3.put("code", "CHAD43");
                                            jsonObject3.put("answer_code", jsonArray);
                                            jsonObject3.put("comment", "None");

                                            array_results.remove(i);
                                            array_results.put(jsonObject3);

                                            object_answers.remove("results");
                                            object_answers.put("results", array_results);

                                            Filling filling = new Filling();
                                            File yourDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Answers/");

                                            File fdelete = new File(yourDir+"/"+filename);
                                            if (fdelete.exists()) {
                                                if (fdelete.delete()) {
                                                    if(filling.saveFileToFolderContext3(filename, object_answers.toString(), Part68.this)){
                                                        string_object_answers = object_answers.toString();
                                                        Intent intent = new Intent(Part68.this, Part41.class);
                                                        intent.putExtra("FILENAME", filename);
                                                        intent.putExtra("ANSWERS", string_object_answers);
                                                        startActivity(intent);
                                                    }
                                                } else {
                                                    System.out.println(getString(R.string.file_not_deleted));
                                                }
                                            }

                                        }else{
                                            Toast.makeText(Part68.this, R.string.please_type_number, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        JSONObject jsonObject;
                        JSONArray jsonArray = new JSONArray();
                        try {
                            jsonObject = new JSONObject();
                            jsonObject.put("code", "CHAD43A");
                            jsonObject.put("member_no", male);
                            jsonArray.put(jsonObject);

                            jsonObject = new JSONObject();
                            jsonObject.put("code", "CHAD43B");
                            jsonObject.put("member_no", female);
                            jsonArray.put(jsonObject);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(!male.equals("") || !female.equals("")){
                            general.AddObjectToResultArray2("CHAD43", jsonArray, Part68.this);
                            Intent intent = new Intent(Part68.this, Part41.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(Part68.this, getString(R.string.please_type_number), Toast.LENGTH_SHORT).show();
                        }
                    }

                }else{
                    JSONObject jsonObject;
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("code", "CHAD43A");
                        jsonObject.put("member_no", male);
                        jsonArray.put(jsonObject);

                        jsonObject = new JSONObject();
                        jsonObject.put("code", "CHAD43B");
                        jsonObject.put("member_no", female);
                        jsonArray.put(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(!male.equals("") || !female.equals("")){
                        general.AddObjectToResultArray2("CHAD43", jsonArray, Part68.this);
                        Intent intent = new Intent(Part68.this, Part41.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(Part68.this, getString(R.string.please_type_number), Toast.LENGTH_SHORT).show();
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
                if (lytQuestions != null) {
                    lytQuestions.addView(textView2);
                }

                String opt = json_data.getString("options");
                JSONArray optArray = new JSONArray(opt);
                EditText[] editText = new EditText[optArray.length()];
                for(int j=0; j<optArray.length(); j++){
                    JSONObject opt_data = optArray.getJSONObject(j);
                    editText[j] = new EditText(this);
                    editText[j].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    setMargins(editText[j], 0, 0, 0, 10);
                    editText[j].setTextSize(18);
                    if(opt_data.getString("code").equals("CHAD43A")){
                        JSONObject object_answers = null;
                        JSONArray array_results = null;
                        JSONObject object1 = null;
                        try {
                            object_answers = new JSONObject(string_object_answers);
                            array_results = object_answers.getJSONArray("results");

                            for(int n=0; n<array_results.length(); n++){
                                object1 = array_results.getJSONObject(n);
                                if(object1.getString("code").equals("CHAD43")){//change this
                                    JSONArray array = object1.getJSONArray("answer_code");
                                    JSONObject object = null;
                                    for (int m=0; m<array.length(); m++){
                                        object = array.getJSONObject(m);
                                        if(object.getString("code").equals("CHAD43A")){
                                            editText[j].setText(object.getString("member_no"));
                                            male = object.getString("member_no");
                                        }
                                    }
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        JSONObject object_answers = null;
                        JSONArray array_results = null;
                        JSONObject object1 = null;
                        try {
                            object_answers = new JSONObject(string_object_answers);
                            array_results = object_answers.getJSONArray("results");

                            for(int n=0; n<array_results.length(); n++){
                                object1 = array_results.getJSONObject(n);
                                if(object1.getString("code").equals("CHAD43")){//change this
                                    JSONArray array = object1.getJSONArray("answer_code");
                                    JSONObject object = null;
                                    for (int m=0; m<array.length(); m++){
                                        object = array.getJSONObject(m);
                                        if(object.getString("code").equals("CHAD43B")){
                                            editText[j].setText(object.getString("member_no"));
                                            female = object.getString("member_no");
                                        }
                                    }
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    editText[j].setHint(opt_data.getString("name_en"));
                    editText[j].setInputType(InputType.TYPE_CLASS_NUMBER);
                    editText[j].setTextColor(getResources().getColor(R.color.black));
                    final int finalJ = j;
                    editText[j].addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if(charSequence.length() != 0){
                                other = charSequence;
                                if(finalJ == 0){
                                    male = "";
                                    male = other.toString();
                                }else if(finalJ == 1){
                                    female = "";
                                    female = other.toString();
                                }
                            }

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {}
                    });
                    lytQuestions.addView(editText[j]);

                }
            }

        }

        if (extras != null) {
            if (extras.containsKey("FILENAME")) {
                main_layout = (ScrollView) findViewById(R.id.main_layout);//change this
                main_layout.setBackgroundColor(getResources().getColor(R.color.grey));

                lytQuestions = (LinearLayout) findViewById(R.id.lytQuestions);
                Button btn_edit = new Button(this);
                btn_edit.setText(getString(R.string.next));
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

                                View v1 = lytQuestions.getChildAt(1);
                                View v2 = lytQuestions.getChildAt(2);
                                if (v1 instanceof EditText) {
                                    male = ((EditText)v1).getText().toString();
                                }
                                if (v2 instanceof EditText) {
                                    female = ((EditText)v2).getText().toString();
                                }


                            for(int i=0; i<array_results.length(); i++){
                                object1 = array_results.getJSONObject(i);
                                if(object1.getString("code").equals("CHAD43")){
                                    String temp_male = "";
                                    String temp_female = "";

                                    for(int n=0; n<array_results.length(); n++){
                                        object1 = array_results.getJSONObject(n);
                                        if(object1.getString("code").equals("CHAD43")){//change this
                                            JSONArray array = object1.getJSONArray("answer_code");
                                            JSONObject object = null;
                                            for (int m=0; m<array.length(); m++){
                                                object = array.getJSONObject(m);
                                                if(object.getString("code").equals("CHAD43A")){
                                                    temp_male = object.getString("member_no");
                                                }else{
                                                    temp_female = object.getString("member_no");
                                                }
                                            }
                                            break;
                                        }
                                    }
                                    JSONObject jsonObject2;
                                    JSONArray jsonArray = new JSONArray();

                                    if(male.equals("") && female.equals("")){
                                        jsonObject2 = new JSONObject();
                                        jsonObject2.put("code", "CHAD43A");
                                        jsonObject2.put("member_no", "");
                                        jsonArray.put(jsonObject2);

                                        jsonObject2 = new JSONObject();
                                        jsonObject2.put("code", "CHAD43B");
                                        jsonObject2.put("member_no", "");
                                        jsonArray.put(jsonObject2);

                                        JSONObject jsonObject3 = new JSONObject();
                                        jsonObject3.put("code", "CHAD43");
                                        jsonObject3.put("answer_code", jsonArray);
                                        jsonObject3.put("comment", "None");

                                        array_results.remove(i);
                                        array_results.put(jsonObject3);

                                        Filling filling = new Filling();
                                        filling.writeToFile("ResultArray", array_results.toString(), Part68.this);
                                        general.CreateMainObject2("results", array_results, Part68.this);
                                        File yourDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Answers/");

                                        File fdelete = new File(yourDir+"/"+filename);
                                        if (fdelete.exists()) {
                                            if (fdelete.delete()) {
                                                //finishDialog(filename);
                                                if(filling.saveFileToFolderContext3(filename, general.getFile("MainObject", Part68.this), Part68.this)){
                                                    Intent intent = new Intent(Part68.this, Part41.class);
                                                    intent.putExtra("FILENAME", filename);
                                                    intent.putExtra("ANSWERS", string_object_answers);
                                                    startActivity(intent);
                                                }
                                                //filling.saveFileToFolderContext2(filename, general.getFile("MainObject", Part48.this), Part48.this);
                                            } else {
                                                System.out.println(getString(R.string.file_not_deleted));
                                            }
                                        }

                                    }else{
                                        if(!male.equals("") || !female.equals("")){

                                            try {
                                                jsonObject2 = new JSONObject();
                                                jsonObject2.put("code", "CHAD43A");


                                                if(temp_male.equals("") && male.equals("")){
                                                    jsonObject2.put("member_no", "");
                                                }else if(!male.equals("")){
                                                    jsonObject2.put("member_no", male);
                                                }else {
                                                    jsonObject2.put("member_no", "");
                                                }

                                                jsonArray.put(jsonObject2);

                                                jsonObject2 = new JSONObject();
                                                jsonObject2.put("code", "CHAD43B");
                                                if(temp_female.equals("") && female.equals("")){
                                                    jsonObject2.put("member_no", "");
                                                }else if(!female.equals("")){
                                                    jsonObject2.put("member_no", female);
                                                }else {
                                                    jsonObject2.put("member_no", "");
                                                }

                                                jsonArray.put(jsonObject2);

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            JSONObject jsonObject3 = new JSONObject();
                                            jsonObject3.put("code", "CHAD43");
                                            jsonObject3.put("answer_code", jsonArray);
                                            jsonObject3.put("comment", "None");

                                            array_results.remove(i);
                                            array_results.put(jsonObject3);

                                            Filling filling = new Filling();
                                            filling.writeToFile("ResultArray", array_results.toString(), Part68.this);
                                            general.CreateMainObject2("results", array_results, Part68.this);
                                            File yourDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Answers/");

                                            File fdelete = new File(yourDir+"/"+filename);
                                            if (fdelete.exists()) {
                                                if (fdelete.delete()) {
                                                    //finishDialog(filename);
                                                    if(filling.saveFileToFolderContext3(filename, general.getFile("MainObject", Part68.this), Part68.this)){
                                                        Intent intent = new Intent(Part68.this, Part41.class);
                                                        intent.putExtra("FILENAME", filename);
                                                        intent.putExtra("ANSWERS", string_object_answers);
                                                        startActivity(intent);
                                                    }
                                                    //filling.saveFileToFolderContext(filename, general.getFile("MainObject", Part48.this), Part48.this);
                                                } else {
                                                    System.out.println("file not Deleted");
                                                }
                                            }

                                        }else{
                                            Toast.makeText(Part68.this, getString(R.string.please_type_number), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                    general.goBackDialoag(Part68.this);
                }
            });
            lytQuestions.addView(btn_back);
        }//Hapa

        assert lytQuestions != null;
        lytQuestions.addView(fab);
/*        if (extras != null) {
            if (extras.containsKey("FILENAME")) {

            }else{
                lytQuestions.addView(fab);
            }
        }else{
            lytQuestions.addView(fab);
        }*/

    }

    public void finishDialog(final String filename) {
        final Filling filling = new Filling();
        AlertDialog.Builder builder = new AlertDialog.Builder(Part68.this);

        builder.setTitle("SAVE");
        builder.setMessage(getString(R.string.saving_file));

        builder.setPositiveButton(getString(R.string.save),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        filling.saveFileToFolderContext2(filename, general.getFile("MainObject", Part68.this), Part68.this);
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }

        });

        AlertDialog alert = builder.create();
        alert.show();
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
        new AlertDialog.Builder(Part68.this)
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
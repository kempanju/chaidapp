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
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Part16 extends AppCompatActivity {

    private static final Set<String> CODES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("CHAD16")));

    Filling filling;
    General general;
    ProgressDialog p;

    LinearLayout lytQuestions;

    String pregnant_woman1 = "";
    String breastfeeding_mother = "";
    String neonates = "";
    String infants = "";
    String children = "";
    String children_6_9= "";
    String adolescents_girls_10_19 = "";
    String adolescents_boys_10_19 = "";
    String female_youth_20_24 = "";
    String male_youth_20_24 = "";
    String female_15_49 = "";
    String male_15_49 = "";
    String male_above_50 = "";
    String female_above_50 = "";

    CharSequence other;
    EditText[] editText1;
    int count1 = 0;

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

        filling = new Filling();
        String questions = general.getFile("questionnaire", Part16.this);
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

        FloatingActionButton fab = new FloatingActionButton(Part16.this);
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
                            View v3 = lytQuestions.getChildAt(3);
                            View v4 = lytQuestions.getChildAt(4);
                            View v5 = lytQuestions.getChildAt(5);
                            View v6 = lytQuestions.getChildAt(6);
                            View v7 = lytQuestions.getChildAt(7);
                            View v8 = lytQuestions.getChildAt(8);
                            View v9 = lytQuestions.getChildAt(9);
                            View v10 = lytQuestions.getChildAt(10);
                            View v11 = lytQuestions.getChildAt(11);
                            View v12 = lytQuestions.getChildAt(12);
                            View v13 = lytQuestions.getChildAt(13);
                            View v14 = lytQuestions.getChildAt(14);

                            if (v1 instanceof EditText) {
                                pregnant_woman1 = ((EditText)v1).getText().toString();
                            }
                            if (v2 instanceof EditText) {
                                breastfeeding_mother = ((EditText)v2).getText().toString();
                            }
                            if (v3 instanceof EditText) {
                                neonates = ((EditText)v3).getText().toString();
                            }
                            if (v4 instanceof EditText) {
                                infants = ((EditText)v4).getText().toString();
                            }
                            if (v5 instanceof EditText) {
                                children = ((EditText)v5).getText().toString();
                            }
                            if (v6 instanceof EditText) {
                                adolescents_girls_10_19 = ((EditText)v6).getText().toString();
                            }
                            if (v7 instanceof EditText) {
                                adolescents_boys_10_19 = ((EditText)v7).getText().toString();
                            }
                            if (v8 instanceof EditText) {
                                female_youth_20_24 = ((EditText)v8).getText().toString();
                            }
                            if (v9 instanceof EditText) {
                                male_youth_20_24 = ((EditText)v9).getText().toString();
                            }
                            if (v10 instanceof EditText) {
                                female_15_49 = ((EditText)v10).getText().toString();
                            }
                            if (v11 instanceof EditText) {
                                male_15_49 = ((EditText)v11).getText().toString();
                            }
                            if (v12 instanceof EditText) {
                                male_above_50 = ((EditText)v12).getText().toString();
                            }
                            if (v13 instanceof EditText) {
                                female_above_50 = ((EditText)v13).getText().toString();
                            }

                            if (v14 instanceof EditText) {
                                children_6_9 = ((EditText)v14).getText().toString();
                            }



                            for(int i=0; i<array_results.length(); i++){
                                object1 = array_results.getJSONObject(i);
                                if(object1.getString("code").equals("CHAD16")){
                                    JSONObject jsonObject2;
                                    JSONArray jsonArray = new JSONArray();

                                    try {
                                        if(pregnant_woman1.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16A");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16A");
                                            jsonObject2.put("member_no", pregnant_woman1);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(breastfeeding_mother.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16B");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16B");
                                            jsonObject2.put("member_no", breastfeeding_mother);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(neonates.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16C");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16C");
                                            jsonObject2.put("member_no", neonates);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(infants.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16D");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16D");
                                            jsonObject2.put("member_no", infants);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(children.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16E");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16E");
                                            jsonObject2.put("member_no", children);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(adolescents_girls_10_19.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16F");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16F");
                                            jsonObject2.put("member_no", adolescents_girls_10_19);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(adolescents_boys_10_19.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16G");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16G");
                                            jsonObject2.put("member_no", adolescents_boys_10_19);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(female_youth_20_24.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16H");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16H");
                                            jsonObject2.put("member_no", female_youth_20_24);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(male_youth_20_24.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16I");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16I");
                                            jsonObject2.put("member_no", male_youth_20_24);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(female_15_49.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16J");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16J");
                                            jsonObject2.put("member_no", female_15_49);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(male_15_49.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16K");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16K");
                                            jsonObject2.put("member_no", male_15_49);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(male_above_50.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16L");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16L");
                                            jsonObject2.put("member_no", male_above_50);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(female_above_50.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16M");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16M");
                                            jsonObject2.put("member_no", female_above_50);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(children_6_9.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16N");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16N");
                                            jsonObject2.put("member_no", children_6_9);
                                            jsonArray.put(jsonObject2);
                                        }

                                        JSONObject jsonObject3 = new JSONObject();
                                        jsonObject3.put("code", "CHAD16");
                                        jsonObject3.put("answer_code", jsonArray);
                                        jsonObject3.put("comment", "None");

                                        array_results.remove(i);
                                        array_results.put(jsonObject3);

                                        object_answers.remove("results");
                                        object_answers.put("results", array_results);


                                        File yourDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Answers/");

                                        File fdelete = new File(yourDir+"/"+filename);
                                        if (fdelete.exists()) {
                                            if (fdelete.delete()) {
                                                if(filling.saveFileToFolderContext3(filename, object_answers.toString(), Part16.this)){
                                                    string_object_answers = object_answers.toString();
                                                    JSONArray array = general.getChad16();
                                                    JSONObject object;
                                                    outerloop:
                                                    for(int a=0; a<array.length(); a++){
                                                        object = array.getJSONObject(a);

                                                        if(a == 0){
                                                            if(object.getBoolean("pregnant_woman")){
                                                                Intent intent = new Intent(Part16.this, Part30.class);
                                                                intent.putExtra("FILENAME", filename);
                                                                intent.putExtra("ANSWERS", string_object_answers);
                                                                intent.putExtra("TITLE", "Pregnant woman");
                                                                startActivity(intent);
                                                            }
                                                        }
                                                        else if(a == 1){
                                                            if(object.getBoolean("breastfeeding_mother_above")){
                                                                JSONObject obj;
                                                                JSONObject object2 = new JSONObject(string_object_answers);
                                                                JSONArray arr = object2.getJSONArray("results");
                                                                for(int b=0; b<arr.length(); b++){
                                                                    obj = arr.getJSONObject(b);
                                                                    String code = obj.getString("code");
                                                                    if(code.equals("CHAD23A")){
                                                                        Intent intent = new Intent(Part16.this, Part8.class);
                                                                        intent.putExtra("FILENAME", filename);
                                                                        intent.putExtra("ANSWERS", string_object_answers);
                                                                        intent.putExtra("TITLE", "Breastfeeding mother within 42 days");
                                                                        startActivity(intent);
                                                                        break outerloop;
                                                                    }else{
                                                                        //finishDialog(filename, string_object_answers);
                                                                        //break outerloop;
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        else if(a == 2){
                                                            if(object.getBoolean("breastfeeding_mother_below")){
                                                                Intent intent = new Intent(Part16.this, Part12.class);
                                                                intent.putExtra("FILENAME", filename);
                                                                intent.putExtra("ANSWERS", string_object_answers);
                                                                intent.putExtra("TITLE", "Breastfeeding mother above 42 days");
                                                                startActivity(intent);
                                                                break;
                                                            }
                                                        }

                                                        else if(a == 3){
                                                            if(object.getBoolean("neonates")){
                                                                Intent intent = new Intent(Part16.this, Part35.class);
                                                                intent.putExtra("FILENAME", filename);
                                                                intent.putExtra("ANSWERS", string_object_answers);
                                                                intent.putExtra("TITLE", "neonates");
                                                                startActivity(intent);
                                                                break;
                                                            }
                                                        }

                                                        else if(a == 4){
                                                            if(object.getBoolean("infants")){
                                                                Intent intent = new Intent(Part16.this, Part35.class);
                                                                intent.putExtra("FILENAME", filename);
                                                                intent.putExtra("ANSWERS", string_object_answers);
                                                                intent.putExtra("TITLE", "infants");
                                                                startActivity(intent);
                                                                break;
                                                            }
                                                        }

                                                        else if(a == 5){
                                                            if(object.getBoolean("children")){
                                                                Intent intent = new Intent(Part16.this, Part35.class);
                                                                intent.putExtra("FILENAME", filename);
                                                                intent.putExtra("ANSWERS", string_object_answers);
                                                                intent.putExtra("TITLE", "children");
                                                                startActivity(intent);
                                                                break;
                                                            }
                                                        }
                                                        else{
                                                            Intent intent = new Intent(Part16.this, End.class);//change to part14
                                                            intent.putExtra("FILENAME", filename);
                                                            intent.putExtra("ANSWERS", string_object_answers);
                                                            //intent.putExtra("TITLE", "Other services");
                                                            startActivity(intent);
                                                            break;
                                                        }
                                                    }

                                                }
                                            } else {
                                                System.out.println(R.string.file_not_deleted);
                                            }
                                        }

                                    }catch (Exception e) {
                                        e.printStackTrace();
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
                            if(!pregnant_woman1.equals("")){
                                jsonObject = new JSONObject();
                                jsonObject.put("code", "CHAD16A");
                                jsonObject.put("member_no", pregnant_woman1);
                                jsonArray.put(jsonObject);
                            }
                            if(!breastfeeding_mother.equals("")){
                                jsonObject = new JSONObject();
                                jsonObject.put("code", "CHAD16B");
                                jsonObject.put("member_no", breastfeeding_mother);
                                jsonArray.put(jsonObject);
                            }
                            if(!neonates.equals("")){
                                jsonObject = new JSONObject();
                                jsonObject.put("code", "CHAD16C");
                                jsonObject.put("member_no", neonates);
                                jsonArray.put(jsonObject);
                            }
                            if(!infants.equals("")){
                                jsonObject = new JSONObject();
                                jsonObject.put("code", "CHAD16D");
                                jsonObject.put("member_no", infants);
                                jsonArray.put(jsonObject);
                            }
                            if(!children.equals("")){
                                jsonObject = new JSONObject();
                                jsonObject.put("code", "CHAD16E");
                                jsonObject.put("member_no", children);
                                jsonArray.put(jsonObject);
                            }
                            if(!adolescents_girls_10_19.equals("")){
                                jsonObject = new JSONObject();
                                jsonObject.put("code", "CHAD16F");
                                jsonObject.put("member_no", adolescents_girls_10_19);
                                jsonArray.put(jsonObject);
                            }
                            if(!adolescents_boys_10_19.equals("")){
                                jsonObject = new JSONObject();
                                jsonObject.put("code", "CHAD16G");
                                jsonObject.put("member_no", adolescents_boys_10_19);
                                jsonArray.put(jsonObject);
                            }
                            if(!female_youth_20_24.equals("")){
                                jsonObject = new JSONObject();
                                jsonObject.put("code", "CHAD16H");
                                jsonObject.put("member_no", female_youth_20_24);
                                jsonArray.put(jsonObject);
                            }
                            if(!male_youth_20_24.equals("")){
                                jsonObject = new JSONObject();
                                jsonObject.put("code", "CHAD16I");
                                jsonObject.put("member_no", male_youth_20_24);
                                jsonArray.put(jsonObject);
                            }
                            if(!female_15_49.equals("")){
                                jsonObject = new JSONObject();
                                jsonObject.put("code", "CHAD16J");
                                jsonObject.put("member_no", female_15_49);
                                jsonArray.put(jsonObject);
                            }
                            if(!male_15_49.equals("")){
                                jsonObject = new JSONObject();
                                jsonObject.put("code", "CHAD16K");
                                jsonObject.put("member_no", male_15_49);
                                jsonArray.put(jsonObject);
                            }
                            if(!male_above_50.equals("")){
                                jsonObject = new JSONObject();
                                jsonObject.put("code", "CHAD16L");
                                jsonObject.put("member_no", male_above_50);
                                jsonArray.put(jsonObject);
                            }
                            if(!female_above_50.equals("")){
                                jsonObject = new JSONObject();
                                jsonObject.put("code", "CHAD16M");
                                jsonObject.put("member_no", female_above_50);
                                jsonArray.put(jsonObject);
                            }

                            if(!children_6_9.equals("")) {
                                jsonObject = new JSONObject();
                                jsonObject.put("code", "CHAD16N");
                                jsonObject.put("member_no", children_6_9);
                                jsonArray.put(jsonObject);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        general.AddObjectToResultArray2("CHAD16", jsonArray, Part16.this);
                        Intent intent = new Intent(Part16.this, Part50.class);
                        startActivity(intent);
                    }

                }else{
                    JSONObject jsonObject;
                    JSONArray jsonArray = new JSONArray();
                    try {
                        if(!pregnant_woman1.equals("")){
                            jsonObject = new JSONObject();
                            jsonObject.put("code", "CHAD16A");
                            jsonObject.put("member_no", pregnant_woman1);
                            jsonArray.put(jsonObject);
                        }
                        if(!breastfeeding_mother.equals("")){
                            jsonObject = new JSONObject();
                            jsonObject.put("code", "CHAD16B");
                            jsonObject.put("member_no", breastfeeding_mother);
                            jsonArray.put(jsonObject);
                        }
                        if(!neonates.equals("")){
                            jsonObject = new JSONObject();
                            jsonObject.put("code", "CHAD16C");
                            jsonObject.put("member_no", neonates);
                            jsonArray.put(jsonObject);
                        }
                        if(!infants.equals("")){
                            jsonObject = new JSONObject();
                            jsonObject.put("code", "CHAD16D");
                            jsonObject.put("member_no", infants);
                            jsonArray.put(jsonObject);
                        }
                        if(!children.equals("")){
                            jsonObject = new JSONObject();
                            jsonObject.put("code", "CHAD16E");
                            jsonObject.put("member_no", children);
                            jsonArray.put(jsonObject);
                        }
                        if(!adolescents_girls_10_19.equals("")){
                            jsonObject = new JSONObject();
                            jsonObject.put("code", "CHAD16F");
                            jsonObject.put("member_no", adolescents_girls_10_19);
                            jsonArray.put(jsonObject);
                        }
                        if(!adolescents_boys_10_19.equals("")){
                            jsonObject = new JSONObject();
                            jsonObject.put("code", "CHAD16G");
                            jsonObject.put("member_no", adolescents_boys_10_19);
                            jsonArray.put(jsonObject);
                        }
                        if(!female_youth_20_24.equals("")){
                            jsonObject = new JSONObject();
                            jsonObject.put("code", "CHAD16H");
                            jsonObject.put("member_no", female_youth_20_24);
                            jsonArray.put(jsonObject);
                        }
                        if(!male_youth_20_24.equals("")){
                            jsonObject = new JSONObject();
                            jsonObject.put("code", "CHAD16I");
                            jsonObject.put("member_no", male_youth_20_24);
                            jsonArray.put(jsonObject);
                        }
                        if(!female_15_49.equals("")){
                            jsonObject = new JSONObject();
                            jsonObject.put("code", "CHAD16J");
                            jsonObject.put("member_no", female_15_49);
                            jsonArray.put(jsonObject);
                        }
                        if(!male_15_49.equals("")){
                            jsonObject = new JSONObject();
                            jsonObject.put("code", "CHAD16K");
                            jsonObject.put("member_no", male_15_49);
                            jsonArray.put(jsonObject);
                        }
                        if(!male_above_50.equals("")){
                            jsonObject = new JSONObject();
                            jsonObject.put("code", "CHAD16L");
                            jsonObject.put("member_no", male_above_50);
                            jsonArray.put(jsonObject);
                        }
                        if(!female_above_50.equals("")){
                            jsonObject = new JSONObject();
                            jsonObject.put("code", "CHAD16M");
                            jsonObject.put("member_no", female_above_50);
                            jsonArray.put(jsonObject);
                        }

                        if(!children_6_9.equals("")){
                            jsonObject = new JSONObject();
                            jsonObject.put("code", "CHAD16N");
                            jsonObject.put("member_no", children_6_9);
                            jsonArray.put(jsonObject);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                   // Log.i("jsonArray",jsonArray.toString());

                    general.AddObjectToResultArray2("CHAD16", jsonArray, Part16.this);
                    Intent intent = new Intent(Part16.this, Part50.class);
                    startActivity(intent);
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
                String opt = json_data.getString("options");
                String code = json_data.getString("code");
                JSONArray optArray = new JSONArray(opt);
                count1 = optArray.length();
                editText1 = new EditText[optArray.length()];
                for(int j=0; j<optArray.length(); j++){
                    final JSONObject opt_data = optArray.getJSONObject(j);
                    editText1[j] = new EditText(Part16.this);
                    editText1[j].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    setMargins(editText1[j], 0, 0, 0, 10);
                    editText1[j].setTextSize(18);
                    editText1[j].setInputType(InputType.TYPE_CLASS_NUMBER);
                    editText1[j].setId(View.generateViewId());
                    editText1[j].setTag(opt_data.getString("code"));
                    editText1[j].setHint(opt_data.getString("name_en"));
                    editText1[j].setTextColor(getResources().getColor(R.color.black));

                    if(opt_data.getString("code").equals("CHAD16A")){
                        JSONObject object_answers = null;
                        JSONArray array_results = null;
                        JSONObject object1 = null;
                        try {
                            object_answers = new JSONObject(string_object_answers);
                            array_results = object_answers.getJSONArray("results");

                            for(int n=0; n<array_results.length(); n++){
                                object1 = array_results.getJSONObject(n);
                                if(object1.getString("code").equals("CHAD16")){
                                    JSONArray array = object1.getJSONArray("answer_code");
                                    JSONObject object;
                                    for (int m=0; m<array.length(); m++){
                                        object = array.getJSONObject(m);
                                        if(object.getString("code").equals("CHAD16A")){
                                            editText1[j].setText(object.getString("member_no"));
                                            pregnant_woman1 = object.getString("member_no");
                                        }
                                    }
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(opt_data.getString("code").equals("CHAD16B")){
                        JSONObject object_answers;
                        JSONArray array_results;
                        JSONObject object1;
                        try {
                            object_answers = new JSONObject(string_object_answers);
                            array_results = object_answers.getJSONArray("results");

                            for(int n=0; n<array_results.length(); n++){
                                object1 = array_results.getJSONObject(n);
                                if(object1.getString("code").equals("CHAD16")){//change this
                                    JSONArray array = object1.getJSONArray("answer_code");
                                    JSONObject object = null;
                                    for (int m=0; m<array.length(); m++){
                                        object = array.getJSONObject(m);
                                        if(object.getString("code").equals("CHAD16B")){
                                            editText1[j].setText(object.getString("member_no"));
                                            breastfeeding_mother = object.getString("member_no");
                                        }
                                    }
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(opt_data.getString("code").equals("CHAD16C")){
                        JSONObject object_answers = null;
                        JSONArray array_results = null;
                        JSONObject object1 = null;
                        try {
                            object_answers = new JSONObject(string_object_answers);
                            array_results = object_answers.getJSONArray("results");

                            for(int n=0; n<array_results.length(); n++){
                                object1 = array_results.getJSONObject(n);
                                if(object1.getString("code").equals("CHAD16")){
                                    JSONArray array = object1.getJSONArray("answer_code");
                                    JSONObject object = null;
                                    for (int m=0; m<array.length(); m++){
                                        object = array.getJSONObject(m);
                                        if(object.getString("code").equals("CHAD16C")){
                                            editText1[j].setText(object.getString("member_no"));
                                            neonates = object.getString("member_no");
                                        }
                                    }
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(opt_data.getString("code").equals("CHAD16D")){
                        JSONObject object_answers;
                        JSONArray array_results;
                        JSONObject object1;
                        try {
                            object_answers = new JSONObject(string_object_answers);
                            array_results = object_answers.getJSONArray("results");

                            for(int n=0; n<array_results.length(); n++){
                                object1 = array_results.getJSONObject(n);
                                if(object1.getString("code").equals("CHAD16")){//change this
                                    JSONArray array = object1.getJSONArray("answer_code");
                                    JSONObject object = null;
                                    for (int m=0; m<array.length(); m++){
                                        object = array.getJSONObject(m);
                                        if(object.getString("code").equals("CHAD16D")){
                                            editText1[j].setText(object.getString("member_no"));
                                            infants = object.getString("member_no");
                                        }
                                    }
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(opt_data.getString("code").equals("CHAD16E")){
                        JSONObject object_answers = null;
                        JSONArray array_results = null;
                        JSONObject object1 = null;
                        try {
                            object_answers = new JSONObject(string_object_answers);
                            array_results = object_answers.getJSONArray("results");

                            for(int n=0; n<array_results.length(); n++){
                                object1 = array_results.getJSONObject(n);
                                if(object1.getString("code").equals("CHAD16")){//change this
                                    JSONArray array = object1.getJSONArray("answer_code");
                                    JSONObject object = null;
                                    for (int m=0; m<array.length(); m++){
                                        object = array.getJSONObject(m);
                                        if(object.getString("code").equals("CHAD16E")){
                                            editText1[j].setText(object.getString("member_no"));
                                            children = object.getString("member_no");
                                        }
                                    }
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(opt_data.getString("code").equals("CHAD16F")){
                        JSONObject object_answers = null;
                        JSONArray array_results = null;
                        JSONObject object1 = null;
                        try {
                            object_answers = new JSONObject(string_object_answers);
                            array_results = object_answers.getJSONArray("results");

                            for(int n=0; n<array_results.length(); n++){
                                object1 = array_results.getJSONObject(n);
                                if(object1.getString("code").equals("CHAD16")){//change this
                                    JSONArray array = object1.getJSONArray("answer_code");
                                    JSONObject object = null;
                                    for (int m=0; m<array.length(); m++){
                                        object = array.getJSONObject(m);
                                        if(object.getString("code").equals("CHAD16F")){
                                            editText1[j].setText(object.getString("member_no"));
                                            adolescents_girls_10_19 = object.getString("member_no");
                                        }
                                    }
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(opt_data.getString("code").equals("CHAD16G")){
                        JSONObject object_answers = null;
                        JSONArray array_results = null;
                        JSONObject object1 = null;
                        try {
                            object_answers = new JSONObject(string_object_answers);
                            array_results = object_answers.getJSONArray("results");

                            for(int n=0; n<array_results.length(); n++){
                                object1 = array_results.getJSONObject(n);
                                if(object1.getString("code").equals("CHAD16")){
                                    JSONArray array = object1.getJSONArray("answer_code");
                                    JSONObject object = null;
                                    for (int m=0; m<array.length(); m++){
                                        object = array.getJSONObject(m);
                                        if(object.getString("code").equals("CHAD16G")){
                                            editText1[j].setText(object.getString("member_no"));
                                            adolescents_boys_10_19 = object.getString("member_no");
                                        }
                                    }
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(opt_data.getString("code").equals("CHAD16H")){
                        JSONObject object_answers = null;
                        JSONArray array_results = null;
                        JSONObject object1 = null;
                        try {
                            object_answers = new JSONObject(string_object_answers);
                            array_results = object_answers.getJSONArray("results");

                            for(int n=0; n<array_results.length(); n++){
                                object1 = array_results.getJSONObject(n);
                                if(object1.getString("code").equals("CHAD16")){
                                    JSONArray array = object1.getJSONArray("answer_code");
                                    JSONObject object = null;
                                    for (int m=0; m<array.length(); m++){
                                        object = array.getJSONObject(m);
                                        if(object.getString("code").equals("CHAD16H")){
                                            editText1[j].setText(object.getString("member_no"));
                                            female_youth_20_24 = object.getString("member_no");
                                        }
                                    }
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(opt_data.getString("code").equals("CHAD16I")){
                        JSONObject object_answers = null;
                        JSONArray array_results = null;
                        JSONObject object1 = null;
                        try {
                            object_answers = new JSONObject(string_object_answers);
                            array_results = object_answers.getJSONArray("results");

                            for(int n=0; n<array_results.length(); n++){
                                object1 = array_results.getJSONObject(n);
                                if(object1.getString("code").equals("CHAD16")){
                                    JSONArray array = object1.getJSONArray("answer_code");
                                    JSONObject object = null;
                                    for (int m=0; m<array.length(); m++){
                                        object = array.getJSONObject(m);
                                        if(object.getString("code").equals("CHAD16I")){
                                            editText1[j].setText(object.getString("member_no"));
                                            male_youth_20_24 = object.getString("member_no");
                                        }
                                    }
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(opt_data.getString("code").equals("CHAD16J")){
                        JSONObject object_answers = null;
                        JSONArray array_results = null;
                        JSONObject object1 = null;
                        try {
                            object_answers = new JSONObject(string_object_answers);
                            array_results = object_answers.getJSONArray("results");

                            for(int n=0; n<array_results.length(); n++){
                                object1 = array_results.getJSONObject(n);
                                if(object1.getString("code").equals("CHAD16")){
                                    JSONArray array = object1.getJSONArray("answer_code");
                                    JSONObject object = null;
                                    for (int m=0; m<array.length(); m++){
                                        object = array.getJSONObject(m);
                                        if(object.getString("code").equals("CHAD16J")){
                                            editText1[j].setText(object.getString("member_no"));
                                            female_15_49 = object.getString("member_no");
                                        }
                                    }
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(opt_data.getString("code").equals("CHAD16K")){
                        JSONObject object_answers = null;
                        JSONArray array_results = null;
                        JSONObject object1 = null;
                        try {
                            object_answers = new JSONObject(string_object_answers);
                            array_results = object_answers.getJSONArray("results");

                            for(int n=0; n<array_results.length(); n++){
                                object1 = array_results.getJSONObject(n);
                                if(object1.getString("code").equals("CHAD16")){
                                    JSONArray array = object1.getJSONArray("answer_code");
                                    JSONObject object = null;
                                    for (int m=0; m<array.length(); m++){
                                        object = array.getJSONObject(m);
                                        if(object.getString("code").equals("CHAD16K")){
                                            editText1[j].setText(object.getString("member_no"));
                                            male_15_49 = object.getString("member_no");
                                        }
                                    }
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(opt_data.getString("code").equals("CHAD16L")){
                        JSONObject object_answers = null;
                        JSONArray array_results = null;
                        JSONObject object1 = null;
                        try {
                            object_answers = new JSONObject(string_object_answers);
                            array_results = object_answers.getJSONArray("results");

                            for(int n=0; n<array_results.length(); n++){
                                object1 = array_results.getJSONObject(n);
                                if(object1.getString("code").equals("CHAD16")){
                                    JSONArray array = object1.getJSONArray("answer_code");
                                    JSONObject object = null;
                                    for (int m=0; m<array.length(); m++){
                                        object = array.getJSONObject(m);
                                        if(object.getString("code").equals("CHAD16L")){
                                            editText1[j].setText(object.getString("member_no"));
                                            male_above_50 = object.getString("member_no");
                                        }
                                    }
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(opt_data.getString("code").equals("CHAD16M")){
                        JSONObject object_answers = null;
                        JSONArray array_results = null;
                        JSONObject object1 = null;
                        try {
                            object_answers = new JSONObject(string_object_answers);
                            array_results = object_answers.getJSONArray("results");

                            for(int n=0; n<array_results.length(); n++){
                                object1 = array_results.getJSONObject(n);
                                if(object1.getString("code").equals("CHAD16")){
                                    JSONArray array = object1.getJSONArray("answer_code");
                                    JSONObject object = null;
                                    for (int m=0; m<array.length(); m++){
                                        object = array.getJSONObject(m);
                                        if(object.getString("code").equals("CHAD16M")){
                                            editText1[j].setText(object.getString("member_no"));
                                            female_above_50 = object.getString("member_no");
                                        }
                                    }
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if(opt_data.getString("code").equals("CHAD16N")){
                        JSONObject object_answers = null;
                        JSONArray array_results = null;
                        JSONObject object1 = null;
                        try {
                            object_answers = new JSONObject(string_object_answers);
                            array_results = object_answers.getJSONArray("results");

                            for(int n=0; n<array_results.length(); n++){
                                object1 = array_results.getJSONObject(n);
                                if(object1.getString("code").equals("CHAD16")){
                                    JSONArray array = object1.getJSONArray("answer_code");
                                    JSONObject object = null;
                                    for (int m=0; m<array.length(); m++){
                                        object = array.getJSONObject(m);
                                        if(object.getString("code").equals("CHAD16N")){
                                            editText1[j].setText(object.getString("member_no"));
                                            children_6_9 = object.getString("member_no");
                                        }
                                    }
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    final int finalJ = j;
                    editText1[j].addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            if(charSequence.length() != 0){
                                other = charSequence;
                                if(finalJ == 0){
                                    pregnant_woman1 = "";
                                    pregnant_woman1 = other.toString();
                                }else if(finalJ == 1){
                                    breastfeeding_mother = "";
                                    breastfeeding_mother = other.toString();
                                }else if(finalJ == 2){
                                    neonates = "";
                                    neonates = other.toString();
                                }else if(finalJ == 3){
                                    infants = "";
                                    infants = other.toString();
                                }else if(finalJ == 4){
                                    children = "";
                                    children = other.toString();
                                }else if(finalJ == 5){
                                    adolescents_girls_10_19 = "";
                                    adolescents_girls_10_19 = other.toString();
                                }else if(finalJ == 6){
                                    adolescents_boys_10_19 = "";
                                    adolescents_boys_10_19 = other.toString();
                                }else if(finalJ == 7){
                                    female_youth_20_24 = "";
                                    female_youth_20_24 = other.toString();
                                }else if(finalJ == 8){
                                    male_youth_20_24 = "";
                                    male_youth_20_24 = other.toString();
                                }else if(finalJ == 9){
                                    female_15_49 = "";
                                    female_15_49 = other.toString();
                                }else if(finalJ == 10){
                                    male_15_49 = "";
                                    male_15_49 = other.toString();
                                }else if(finalJ == 11){
                                    male_above_50 = "";
                                    male_above_50 = other.toString();
                                }else if(finalJ == 12){
                                    female_above_50 = "";
                                    female_above_50 = other.toString();
                                }
                                else if(finalJ == 13){
                                    children_6_9 = "";
                                    children_6_9 = other.toString();
                                }

                            }

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {}
                    });
                    lytQuestions.addView(editText1[j]);
                }
            }


        }
        assert lytQuestions != null;
        if (extras != null) {
            if (extras.containsKey("FILENAME")) {
                main_layout = (ScrollView) findViewById(R.id.main_layout);//change this
                main_layout.setBackgroundColor(getResources().getColor(R.color.grey));

                lytQuestions = (LinearLayout) findViewById(R.id.lytQuestions);
                Button btn_edit = new Button(this);
                btn_edit.setText(R.string.next);
                setMargins(btn_edit, 0, 10, 0, 10);
                btn_edit.setBackgroundResource(R.drawable.orange_corner_gradient);
                btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JSONObject object_answers;
                        JSONArray array_results;
                        JSONObject object1;
                        try {
                            object_answers = new JSONObject(string_object_answers);
                            array_results = object_answers.getJSONArray("results");

                            View v1 = lytQuestions.getChildAt(1);
                            View v2 = lytQuestions.getChildAt(2);
                            View v3 = lytQuestions.getChildAt(3);
                            View v4 = lytQuestions.getChildAt(4);
                            View v5 = lytQuestions.getChildAt(5);
                            View v6 = lytQuestions.getChildAt(6);
                            View v7 = lytQuestions.getChildAt(7);
                            View v8 = lytQuestions.getChildAt(8);
                            View v9 = lytQuestions.getChildAt(9);
                            View v10 = lytQuestions.getChildAt(10);
                            View v11 = lytQuestions.getChildAt(11);
                            View v12 = lytQuestions.getChildAt(12);
                            View v13 = lytQuestions.getChildAt(13);
                            View v14 = lytQuestions.getChildAt(14);

                            if (v1 instanceof EditText) {
                                pregnant_woman1 = ((EditText)v1).getText().toString();
                            }
                            if (v2 instanceof EditText) {
                                breastfeeding_mother = ((EditText)v2).getText().toString();
                            }
                            if (v3 instanceof EditText) {
                                neonates = ((EditText)v3).getText().toString();
                            }
                            if (v4 instanceof EditText) {
                                infants = ((EditText)v4).getText().toString();
                            }
                            if (v5 instanceof EditText) {
                                children = ((EditText)v5).getText().toString();
                            }
                            if (v6 instanceof EditText) {
                                adolescents_girls_10_19 = ((EditText)v6).getText().toString();
                            }
                            if (v7 instanceof EditText) {
                                adolescents_boys_10_19 = ((EditText)v7).getText().toString();
                            }
                            if (v8 instanceof EditText) {
                                female_youth_20_24 = ((EditText)v8).getText().toString();
                            }
                            if (v9 instanceof EditText) {
                                male_youth_20_24 = ((EditText)v9).getText().toString();
                            }
                            if (v10 instanceof EditText) {
                                female_15_49 = ((EditText)v10).getText().toString();
                            }
                            if (v11 instanceof EditText) {
                                male_15_49 = ((EditText)v11).getText().toString();
                            }
                            if (v12 instanceof EditText) {
                                male_above_50 = ((EditText)v12).getText().toString();
                            }
                            if (v13 instanceof EditText) {
                                female_above_50 = ((EditText)v13).getText().toString();
                            }
                            if (v14 instanceof EditText) {
                                children_6_9 = ((EditText)v14).getText().toString();
                            }


                            for(int i=0; i<array_results.length(); i++){
                                object1 = array_results.getJSONObject(i);
                                if(object1.getString("code").equals("CHAD16")){
                                    JSONObject jsonObject2;
                                    JSONArray jsonArray = new JSONArray();

                                    try {
                                        if(pregnant_woman1.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16A");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16A");
                                            jsonObject2.put("member_no", pregnant_woman1);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(breastfeeding_mother.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16B");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16B");
                                            jsonObject2.put("member_no", breastfeeding_mother);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(neonates.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16C");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16C");
                                            jsonObject2.put("member_no", neonates);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(infants.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16D");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16D");
                                            jsonObject2.put("member_no", infants);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(children.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16E");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16E");
                                            jsonObject2.put("member_no", children);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(adolescents_girls_10_19.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16F");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16F");
                                            jsonObject2.put("member_no", adolescents_girls_10_19);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(adolescents_boys_10_19.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16G");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16G");
                                            jsonObject2.put("member_no", adolescents_boys_10_19);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(female_youth_20_24.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16H");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16H");
                                            jsonObject2.put("member_no", female_youth_20_24);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(male_youth_20_24.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16I");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16I");
                                            jsonObject2.put("member_no", male_youth_20_24);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(female_15_49.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16J");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16J");
                                            jsonObject2.put("member_no", female_15_49);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(male_15_49.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16K");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16K");
                                            jsonObject2.put("member_no", male_15_49);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(male_above_50.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16L");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16L");
                                            jsonObject2.put("member_no", male_above_50);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(female_above_50.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16M");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16M");
                                            jsonObject2.put("member_no", female_above_50);
                                            jsonArray.put(jsonObject2);
                                        }

                                        if(children_6_9.equals("")) {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16N");
                                            jsonObject2.put("member_no", "");
                                            jsonArray.put(jsonObject2);

                                        }else {
                                            jsonObject2 = new JSONObject();
                                            jsonObject2.put("code", "CHAD16N");
                                            jsonObject2.put("member_no", children_6_9);
                                            jsonArray.put(jsonObject2);
                                        }

                                        JSONObject jsonObject3 = new JSONObject();
                                        jsonObject3.put("code", "CHAD16");
                                        jsonObject3.put("answer_code", jsonArray);
                                        jsonObject3.put("comment", "None");

                                        array_results.remove(i);
                                        array_results.put(jsonObject3);

                                        Filling filling = new Filling();
                                        filling.writeToFile("ResultArray", array_results.toString(), Part16.this);
                                        general.CreateMainObject2("results", array_results, Part16.this);
                                        File yourDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Answers/");

                                        File fdelete = new File(yourDir+"/"+filename);
                                        if (fdelete.exists()) {
                                            if (fdelete.delete()) {
                                                filling.saveFileToFolderContext2(filename, general.getFile("MainObject", Part16.this), Part16.this);
                                            } else {
                                                System.out.println("file not Deleted");
                                            }
                                        }

                                    }catch (Exception e) {
                                        e.printStackTrace();
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
            btn_back.setText(R.string.go_back_to_dashboard);
            btn_back.setBackgroundResource(R.drawable.cyan_corner_gradient);
            setMargins(btn_back, 0, 10, 0, 10);
            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    general.goBackDialoag(Part16.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Part16.this);

        builder.setTitle(R.string.save);
        builder.setMessage(R.string.saving_file);

        builder.setPositiveButton(R.string.save,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        filling.saveFileToFolderContext2(filename, string_object_answers, Part16.this);
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
        new AlertDialog.Builder(Part16.this)
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
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Part34 extends AppCompatActivity {
    private static final Set<String> CODES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "CHAD36")));

    General general;
    ProgressDialog p;

    LinearLayout lytQuestions;
    String answer_code = "";

    String Title = "";

    JSONObject object4;
    JSONObject object5;
    JSONObject object6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part4);
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        general = new General(this);
        String questions = general.getFile("questionnaire", Part34.this);
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

        FloatingActionButton fab = new FloatingActionButton(Part34.this);
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
                general = new General(Part34.this);
                String chad16 = general.getFile("CHAD16", Part34.this);
                JSONObject json = null;
                try {
                    json = new JSONObject(chad16);
                    JSONArray arrayChad16 = json.getJSONArray("CHAD16");
                    JSONObject json_data = arrayChad16.getJSONObject(1);
                    if(json_data.getString("breastfeeding_mother_above").equals("false")){
                        JSONObject json_data1 = arrayChad16.getJSONObject(2);
                        if(json_data1.getString("breastfeeding_mother_below").equals("false")){
                            json_data = arrayChad16.getJSONObject(3);
                            if(json_data.getString("neonates").equals("false")){
                                json_data = arrayChad16.getJSONObject(4);
                                if(json_data.getString("infants").equals("false")){
                                    json_data = arrayChad16.getJSONObject(5);
                                    if(json_data.getString("children").equals("false")){
                                        //other services
                                        Intent intent = new Intent(Part34.this, Part14.class);
                                        intent.putExtra("TITLE", "Other Services");//other services
                                        startActivity(intent);
                                    }else{
                                        object4 = new JSONObject();
                                        object5 = new JSONObject();
                                        object6 = new JSONObject();

                                        arrayChad16.remove(3);
                                        arrayChad16.remove(4);
                                        arrayChad16.remove(5);

                                        object4.put("neonates", false);
                                        object5.put("infants", false);
                                        object6.put("children", false);

                                        arrayChad16.put(3, object4);
                                        arrayChad16.put(4, object5);
                                        arrayChad16.put(5, object6);

                                        JSONObject main_object = new JSONObject();
                                        main_object.put("CHAD16",arrayChad16);
                                        Filling filling = new Filling();
                                        filling.writeToFile("CHAD16", main_object.toString(), Part34.this);

                                        if(Title.equals("children")){
                                            Intent intent = new Intent(Part34.this, Part14.class);
                                            intent.putExtra("TITLE", "Other Services");
                                            startActivity(intent);
                                        }else{
                                            Intent intent = new Intent(Part34.this, Part35.class);
                                            intent.putExtra("TITLE", "children");
                                            startActivity(intent);
                                        }

                                    }

                                }else{
                                    json_data = arrayChad16.getJSONObject(5);
                                    boolean children = json_data.getBoolean("children");

                                    object4 = new JSONObject();
                                    object5 = new JSONObject();
                                    object6 = new JSONObject();

                                    arrayChad16.remove(3);
                                    arrayChad16.remove(4);
                                    arrayChad16.remove(5);

                                    object4.put("neonates", false);
                                    object5.put("infants", false);
                                    object6.put("children", children);

                                    arrayChad16.put(3, object4);
                                    arrayChad16.put(4, object5);
                                    arrayChad16.put(5, object6);

                                    JSONObject main_object = new JSONObject();
                                    main_object.put("CHAD16",arrayChad16);
                                    Filling filling = new Filling();
                                    filling.writeToFile("CHAD16", main_object.toString(), Part34.this);

                                    json_data = arrayChad16.getJSONObject(5);
                                    if(!json_data.getBoolean("children")){
                                        Intent intent = new Intent(Part34.this, Part14.class);
                                        intent.putExtra("TITLE", "Other Services");
                                        startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(Part34.this, Part35.class);
                                        intent.putExtra("TITLE", "children");
                                        startActivity(intent);
                                    }
                                }
                            }else{
                                json_data = arrayChad16.getJSONObject(4);
                                boolean infants = json_data.getBoolean("infants");
                                json_data = arrayChad16.getJSONObject(5);
                                boolean children = json_data.getBoolean("children");

                                object4 = new JSONObject();
                                object5 = new JSONObject();
                                object6 = new JSONObject();

                                arrayChad16.remove(3);
                                arrayChad16.remove(3);
                                arrayChad16.remove(3);

                                object4.put("neonates", false);
                                object5.put("infants", infants);
                                object6.put("children", children);

                                arrayChad16.put(3, object4);
                                arrayChad16.put(4, object5);
                                arrayChad16.put(5, object6);

                                JSONObject main_object = new JSONObject();
                                main_object.put("CHAD16",arrayChad16);
                                Filling filling = new Filling();
                                filling.writeToFile("CHAD16", main_object.toString(), Part34.this);

                                json_data = arrayChad16.getJSONObject(4);
                                if(json_data.getString("infants").equals("false")){
                                    json_data = arrayChad16.getJSONObject(5);
                                    if(json_data.getString("children").equals("false")){
                                        //other services
                                        Intent intent = new Intent(Part34.this, Part14.class);
                                        intent.putExtra("TITLE", "Other Services");
                                        startActivity(intent);
                                    }else{
                                        object4 = new JSONObject();
                                        object5 = new JSONObject();
                                        object6 = new JSONObject();

                                        arrayChad16.remove(3);
                                        arrayChad16.remove(4);
                                        arrayChad16.remove(5);

                                        object4.put("neonates", false);
                                        object5.put("infants", false);
                                        object6.put("children", false);

                                        arrayChad16.put(3, object4);
                                        arrayChad16.put(4, object5);
                                        arrayChad16.put(5, object6);

                                        main_object.put("CHAD16",arrayChad16);
                                        filling.writeToFile("CHAD16", main_object.toString(), Part34.this);

                                        Intent intent = new Intent(Part34.this, Part35.class);
                                        intent.putExtra("TITLE", "children");
                                        startActivity(intent);
                                    }
                                }else{
                                    json_data = arrayChad16.getJSONObject(5);
                                    boolean children2 = json_data.getBoolean("children");

                                    object4 = new JSONObject();
                                    object5 = new JSONObject();
                                    object6 = new JSONObject();


                                    arrayChad16.remove(3);
                                    arrayChad16.remove(3);
                                    arrayChad16.remove(3);

                                    object4.put("neonates", false);
                                    object5.put("infants", false);
                                    object6.put("children", children2);

                                    arrayChad16.put(3, object4);
                                    arrayChad16.put(4, object5);
                                    arrayChad16.put(5, object6);

                                    main_object.put("CHAD16",arrayChad16);
                                    filling.writeToFile("CHAD16", main_object.toString(), Part34.this);

                                    Intent intent = new Intent(Part34.this, Part35.class);
                                    intent.putExtra("TITLE", "infants");
                                    startActivity(intent);
                                }
                            }
                        }else{
                            Intent intent = new Intent(Part34.this, Part14.class);
                            startActivity(intent);
                        }

                    }else{
                        Intent intent = new Intent(Part34.this, Part9.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

                assert lytQuestions != null;
                lytQuestions.addView(textView);
            }

        }

        Button btn_back = new Button(this);
        btn_back.setText(getString(R.string.go_back_to_dashboard));
        btn_back.setBackgroundResource(R.drawable.cyan_corner_gradient);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                general.goBackDialoag(Part34.this);
            }
        });
        lytQuestions.addView(btn_back);
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
        new AlertDialog.Builder(Part34.this)
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
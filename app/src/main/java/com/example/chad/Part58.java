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
import android.text.Html;
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
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Part58 extends AppCompatActivity {
    private static final Set<String> CODES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "CHAD33F")));



    General general;
    ProgressDialog p;

    LinearLayout lytQuestions;
    ArrayList<String> answer_code = new ArrayList<>();

    String Title = "";

    JSONObject object4;
    JSONObject object5;
    JSONObject object6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part15);
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        general = new General(this);
        String questions = general.getFile("questionnaire", Part58.this);
        try {
            createAQuestion(questions);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        Title = intent.getStringExtra("TITLE");
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>"+ Title +" </font>"));
    }

    public void createAQuestion(String questions) throws JSONException {
        JSONObject json = new JSONObject(questions);
        JSONArray questionnaire = json.getJSONArray("questionnaire");
        lytQuestions = (LinearLayout) findViewById(R.id.lytQuestions);

        FloatingActionButton fab = new FloatingActionButton(Part58.this);
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
                String ans = answer_code.toString().replaceAll("[\\[\\](){}]","").trim();
                general.AddObjectToResultArray("CHAD33F", ans.trim(), Part58.this);
                String chad16 = general.getFile("CHAD16", Part58.this);
                JSONObject json = null;
                JSONObject json_neonates = null;
                JSONObject json_infants = null;
                JSONObject json_children = null;

                String n = "";
                String i = "";
                String c = "";

                try {
                    json = new JSONObject(chad16);
                    JSONArray arrayChad16 = json.getJSONArray("CHAD16");
                    json_neonates = arrayChad16.getJSONObject(3);
                    json_infants = arrayChad16.getJSONObject(4);
                    json_children = arrayChad16.getJSONObject(5);

                    n = json_neonates.getString("neonates");
                    i = json_infants.getString("infants");
                    c = json_children.getString("children");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(n.equals("true") || i.equals("true") || c.equals("true")){

                    JSONObject json_data = null;
                    JSONObject json_pregnant = null;
                    JSONObject json_breast_above = null;
                    JSONObject json_breast_below = null;
                    try {
                        json = new JSONObject(chad16);
                        JSONArray arrayChad16 = json.getJSONArray("CHAD16");
                        json_pregnant = arrayChad16.getJSONObject(0);
                        json_breast_above = arrayChad16.getJSONObject(1);
                        json_breast_below = arrayChad16.getJSONObject(2);
                        if(json_pregnant.getString("pregnant_woman").equals("false") && json_breast_above.getString("breastfeeding_mother_above").equals("false") && json_breast_below.getString("breastfeeding_mother_below").equals("false")){
                            json_data = arrayChad16.getJSONObject(3);
                            if(json_data.getString("neonates").equals("false")){
                                json_data = arrayChad16.getJSONObject(4);
                                if(json_data.getString("infants").equals("false")){
                                    json_data = arrayChad16.getJSONObject(5);
                                    if(json_data.getString("children").equals("false")){
                                        //other services
                                        Intent intent = new Intent(Part58.this, End.class);
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
                                        filling.writeToFile("CHAD16", main_object.toString(), Part58.this);

                                        if(c.equals("true")){
                                            Intent intent = new Intent(Part58.this, End.class);
                                            intent.putExtra("TITLE", "Other Services");
                                            startActivity(intent);
                                        }else{
                                            Intent intent = new Intent(Part58.this, Part35.class);
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
                                    filling.writeToFile("CHAD16", main_object.toString(), Part58.this);

                                    json_data = arrayChad16.getJSONObject(5);
                                    if(!json_data.getBoolean("children")){
                                        Intent intent = new Intent(Part58.this, End.class);
                                        intent.putExtra("TITLE", "Other Services");
                                        startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(Part58.this, Part35.class);
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
                                filling.writeToFile("CHAD16", main_object.toString(), Part58.this);

                                json_data = arrayChad16.getJSONObject(4);
                                if(json_data.getString("infants").equals("false")){
                                    json_data = arrayChad16.getJSONObject(5);
                                    if(json_data.getString("children").equals("false")){
                                        //other services
                                        Intent intent = new Intent(Part58.this, End.class);
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
                                        filling.writeToFile("CHAD16", main_object.toString(), Part58.this);

                                        Intent intent = new Intent(Part58.this, Part35.class);
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
                                    filling.writeToFile("CHAD16", main_object.toString(), Part58.this);

                                    Intent intent = new Intent(Part58.this, Part35.class);
                                    intent.putExtra("TITLE", "infants");
                                    startActivity(intent);
                                }
                            }
                        }else{
                            json_data = arrayChad16.getJSONObject(3);
                            if(json_data.getString("neonates").equals("false")){
                                json_data = arrayChad16.getJSONObject(4);
                                if(json_data.getString("infants").equals("false")){
                                    json_data = arrayChad16.getJSONObject(5);
                                    if(json_data.getString("children").equals("false")){
                                        //other services
                                        Intent intent = new Intent(Part58.this, End.class);
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
                                        filling.writeToFile("CHAD16", main_object.toString(), Part58.this);

                                        Intent intent = new Intent(Part58.this, Part35.class);
                                        intent.putExtra("TITLE", "children");
                                        startActivity(intent);

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
                                    filling.writeToFile("CHAD16", main_object.toString(), Part58.this);

                                    Intent intent = new Intent(Part58.this, Part35.class);
                                    intent.putExtra("TITLE", "infants");
                                    startActivity(intent);

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
                                filling.writeToFile("CHAD16", main_object.toString(), Part58.this);

                                Intent intent = new Intent(Part58.this, Part35.class);
                                intent.putExtra("TITLE", "neonates");
                                startActivity(intent);

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else{

                    Intent intent = new Intent(Part58.this, End.class);
                    startActivity(intent);
                }
            }
        });
        for(int i=0; i<questionnaire.length(); i++){
            JSONObject json_data = questionnaire.getJSONObject(i);
            if(CODES.contains(json_data.getString("code"))){
                final TextView textView = new TextView(this);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                setMargins(textView, 0, 0, 0, 30);
                textView.setText(json_data.getString("name_sw"));
                textView.setTextSize(18);
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                textView.setTextColor(getResources().getColor(R.color.black));
                if (lytQuestions != null) {
                    lytQuestions.addView(textView);
                }
                String opt = json_data.getString("options");
                JSONArray optArray = new JSONArray(opt);
                final CheckBox[] checkbox  = new CheckBox[optArray.length()];
                for(int j=0; j<optArray.length(); j++){
                    final JSONObject opt_data = optArray.getJSONObject(j);
                    checkbox[j] = new CheckBox(this);
                    checkbox[j].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 90));
                    setMargins(checkbox[j], 0, 0, 0, 10);
                    checkbox[j].setText(opt_data.getString("name_en"));
                    final int finalJ = j;
                    checkbox[j].setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if ( ((CheckBox)v).isChecked() ) {
                                try {
                                    //Toast.makeText(Part16.this, opt_data.getString("code"), Toast.LENGTH_SHORT).show();
                                    answer_code.add(opt_data.getString("code"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                try {
                                    String code = opt_data.getString("code");
                                    answer_code.remove(code);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    lytQuestions.addView(checkbox[j]);
                }
            }

        }
        Button btn_back = new Button(this);
        btn_back.setText(getString(R.string.go_back_to_dashboard));
        btn_back.setBackgroundResource(R.drawable.cyan_corner_gradient);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                general.goBackDialoag(Part58.this);
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
        new AlertDialog.Builder(Part58.this)
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
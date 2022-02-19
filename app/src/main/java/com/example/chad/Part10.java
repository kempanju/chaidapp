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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Part10 extends AppCompatActivity {
    private static final Set<String> CODES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("CHAD27B")));


    General general;
    ProgressDialog p;

    LinearLayout lytQuestions;
    String answer_code = "";

    String Title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part10);
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        general = new General(this);
        String questions = general.getFile("questionnaire", Part10.this);
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

        FloatingActionButton fab = new FloatingActionButton(Part10.this);
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
        setMargins(fab, 0, 20, 0
                , 10);
        fab.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(answer_code.equals("CHAD27B1")){
                    if(Title.equals("neonates") || Title.equals("infants") || Title.equals("children")){
                        general.CreateSmallObjectForMainObjectInt(Part10.this, Title, "baby_provided_immunization", 1);
                        //general.AddObjectToResultArray("CHAD27B", answer_code, Part10.this);
                        Intent intent = new Intent(Part10.this, Part11.class);
                        intent.putExtra("TITLE", Title);
                        startActivity(intent);
                    }else{
                        general.CreateSmallObjectForMainObjectInt(Part10.this, "post_delivery", "baby_provided_immunization", 1);
                        general.CreateSmallObjectForMainObjectInt(Part10.this, "child_under_five", "baby_provided_immunization", 1);
                        //general.AddObjectToResultArray("CHAD27B", answer_code, Part10.this);
                        Intent intent = new Intent(Part10.this, Part11.class);
                        intent.putExtra("TITLE", Title);
                        startActivity(intent);
                    }

                }else{
                    if(Title.equals("neonates") || Title.equals("infants") || Title.equals("children")){
                        general.CreateSmallObjectForMainObjectInt(Part10.this, Title, "baby_provided_immunization", 0);
                        //general.AddObjectToResultArray("CHAD27B", answer_code, Part10.this);
                        Intent intent = new Intent(Part10.this, Part39.class);
                        intent.putExtra("TITLE", Title);
                        startActivity(intent);
                    }else{
                        general.CreateSmallObjectForMainObjectInt(Part10.this, "post_delivery", "baby_provided_immunization", 0);
                        general.CreateSmallObjectForMainObjectInt(Part10.this, "child_under_five", "baby_provided_immunization", 0);
                        //general.AddObjectToResultArray("CHAD27B", answer_code, Part10.this);
                        Intent intent = new Intent(Part10.this, Part39.class);
                        intent.putExtra("TITLE", Title);
                        startActivity(intent);
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

                RadioGroup rg = new RadioGroup(Part10.this);
                RadioButton spinner;

                String code = "";
                String opt = json_data.getString("options");
                code = json_data.getString("code");
                JSONArray optArray = new JSONArray(opt);
                final String[] personNames = new String[optArray.length()];
                for(int j=0; j<optArray.length(); j++){
                    final JSONObject opt_data = optArray.getJSONObject(j);
                    spinner = new RadioButton(this);
                    spinner.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 90));
                    setMargins(spinner, 0, 0, 0, 10);
                    spinner.setText(opt_data.getString("name_en").toString());
                    spinner.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if ( ((RadioButton)v).isChecked() ) {
                                try {
                                    answer_code = opt_data.getString("code");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    rg.addView(spinner);
                    //personNames[j] = opt_data.getString("name_en");
                }
                lytQuestions.addView(rg);
            }
            //final Spinner spinner = new Spinner(this);
        }

        assert lytQuestions != null;
        Button btn_back = new Button(this);
        btn_back.setText(R.string.go_back_to_dashboard);
        btn_back.setBackgroundResource(R.drawable.cyan_corner_gradient);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                general.goBackDialoag(Part10.this);
            }
        });
        lytQuestions.addView(btn_back);
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
        new AlertDialog.Builder(Part10.this)
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
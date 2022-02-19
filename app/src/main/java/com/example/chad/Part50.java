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
import android.widget.TextView;
import android.widget.Toast;

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

public class Part50 extends AppCompatActivity {

    private static final Set<String> CODES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "CHAD17")));

    Filling filling;
    General general;
    ProgressDialog p;

    LinearLayout lytQuestions;

    String pregnant_woman1 = "";
    String breastfeeding_mother_within_42 = "";
    String breastfeeding_mother_above_42 = "";
    String neonates = "";
    String infants = "";
    String children = "";
    String children_6_9 = "";
    String adolescents_girls_10_19 = "";
    String adolescents_boys_10_19 = "";
    String female_youth_20_24 = "";
    String male_youth_20_24 = "";
    String female_15_49 = "";
    String male_15_49 = "";
    String male_above_50 = "";
    String female_above_50 = "";
    EditText[] editText2;
    int count2 = 0;

    CharSequence other;
    Boolean pregnant_woman = false;
    Boolean breastfeeding_mother_above = false;
    Boolean breastfeeding_mother_below = false;
    Boolean _neonates = false;
    Boolean  _infants = false;
    Boolean  _children = false;
    Boolean _adolescents_girls_10_19 = false;
    Boolean _adolescents_boys_10_19 = false;

    JSONArray jsonArray = new JSONArray();
    JSONObject main_object;
    JSONObject object1;
    JSONObject object2;
    JSONObject object3;
    JSONObject object4;
    JSONObject object5;
    JSONObject object6;

    JSONObject object7;
    JSONObject object8;
    JSONObject object9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part2);
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        general = new General(this);
        filling = new Filling();
        String questions = general.getFile("questionnaire", Part50.this);
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

        FloatingActionButton fab = new FloatingActionButton(Part50.this);
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
                if(!editText2[0].getText().toString().equals("")){
                    pregnant_woman = true;
                }
                if(!editText2[1].getText().toString().equals("")){
                    breastfeeding_mother_above = true;
                }
                if(!editText2[2].getText().toString().equals("")){
                    breastfeeding_mother_below = true;
                }
                if(!editText2[3].getText().toString().equals("")){
                    _neonates = true;
                }
                if(!editText2[4].getText().toString().equals("")){
                    _infants = true;
                }
                if(!editText2[5].getText().toString().equals("")){
                    _children = true;
                }

                if(!editText2[6].getText().toString().equals("")){
                    _adolescents_girls_10_19 = true;
                }
                if(!editText2[7].getText().toString().equals("")){
                    _adolescents_boys_10_19 = true;
                }


                try {
                    object1 = new JSONObject();
                    object2 = new JSONObject();
                    object3 = new JSONObject();
                    object4 = new JSONObject();
                    object5 = new JSONObject();
                    object6 = new JSONObject();
                    object7 = new JSONObject();
                    object8 = new JSONObject();
                    object1.put("pregnant_woman", pregnant_woman);
                    object2.put("breastfeeding_mother_above", breastfeeding_mother_above);
                    object3.put("breastfeeding_mother_below", breastfeeding_mother_below);

                    object4.put("neonates", _neonates);
                    object5.put("infants", _infants);
                    object6.put("children", _children);

                    object7.put("adolescents_girls", _adolescents_girls_10_19);
                    object8.put("adolescents_boys", _adolescents_boys_10_19);

                    jsonArray.put(object1);
                    jsonArray.put(object2);
                    jsonArray.put(object3);
                    jsonArray.put(object4);
                    jsonArray.put(object5);
                    jsonArray.put(object6);
                    jsonArray.put(object7);
                    jsonArray.put(object8);

                    main_object = new JSONObject();
                    main_object.put("CHAD16",jsonArray);
                    filling = new Filling();
                    filling.writeToFile("CHAD16", main_object.toString(), Part50.this);
                    filling.writeToFile("CHAD17", main_object.toString(), Part50.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject jsonObject;
                JSONArray jsonArray = new JSONArray();
                try {
                    if(!pregnant_woman1.equals("")){
                        jsonObject = new JSONObject();
                        jsonObject.put("code", "CHAD17A");
                        jsonObject.put("member_no", pregnant_woman1);
                        jsonArray.put(jsonObject);
                    }
                    if(!breastfeeding_mother_within_42.equals("")){
                        jsonObject = new JSONObject();
                        jsonObject.put("code", "CHAD17B");
                        jsonObject.put("member_no", breastfeeding_mother_within_42);
                        jsonArray.put(jsonObject);
                    }
                    if(!breastfeeding_mother_above_42.equals("")){
                        jsonObject = new JSONObject();
                        jsonObject.put("code", "CHAD17C");
                        jsonObject.put("member_no", breastfeeding_mother_above_42);
                        jsonArray.put(jsonObject);
                    }
                    if(!neonates.equals("")){
                        jsonObject = new JSONObject();
                        jsonObject.put("code", "CHAD17D");
                        jsonObject.put("member_no", neonates);
                        jsonArray.put(jsonObject);
                    }
                    if(!infants.equals("")){
                        jsonObject = new JSONObject();
                        jsonObject.put("code", "CHAD17E");
                        jsonObject.put("member_no", infants);
                        jsonArray.put(jsonObject);
                    }
                    if(!children.equals("")){
                        jsonObject = new JSONObject();
                        jsonObject.put("code", "CHAD17F");
                        jsonObject.put("member_no", children);
                        jsonArray.put(jsonObject);
                    }
                    if(!adolescents_girls_10_19.equals("")){
                        jsonObject = new JSONObject();
                        jsonObject.put("code", "CHAD17G");
                        jsonObject.put("member_no", adolescents_girls_10_19);
                        jsonArray.put(jsonObject);
                    }
                    if(!adolescents_boys_10_19.equals("")){
                        jsonObject = new JSONObject();
                        jsonObject.put("code", "CHAD17H");
                        jsonObject.put("member_no", adolescents_boys_10_19);
                        jsonArray.put(jsonObject);
                    }
                    if(!female_youth_20_24.equals("")){
                        jsonObject = new JSONObject();
                        jsonObject.put("code", "CHAD17I");
                        jsonObject.put("member_no", female_youth_20_24);
                        jsonArray.put(jsonObject);
                    }
                    if(!male_youth_20_24.equals("")){
                        jsonObject = new JSONObject();
                        jsonObject.put("code", "CHAD17J");
                        jsonObject.put("member_no", male_youth_20_24);
                        jsonArray.put(jsonObject);
                    }
                    if(!female_15_49.equals("")){
                        jsonObject = new JSONObject();
                        jsonObject.put("code", "CHAD17K");
                        jsonObject.put("member_no", female_15_49);
                        jsonArray.put(jsonObject);
                    }
                    if(!male_15_49.equals("")){
                        jsonObject = new JSONObject();
                        jsonObject.put("code", "CHAD17L");
                        jsonObject.put("member_no", male_15_49);
                        jsonArray.put(jsonObject);
                    }
                    if(!male_above_50.equals("")){
                        jsonObject = new JSONObject();
                        jsonObject.put("code", "CHAD17M");
                        jsonObject.put("member_no", male_above_50);
                        jsonArray.put(jsonObject);
                    }
                    if(!female_above_50.equals("")){
                        jsonObject = new JSONObject();
                        jsonObject.put("code", "CHAD17N");
                        jsonObject.put("member_no", female_above_50);
                        jsonArray.put(jsonObject);
                    }

                    if(!children_6_9.equals("")){
                        jsonObject = new JSONObject();
                        jsonObject.put("code", "CHAD17O");
                        jsonObject.put("member_no", children_6_9);
                        jsonArray.put(jsonObject);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                general.AddObjectToResultArray2("CHAD17", jsonArray, Part50.this);
                if(pregnant_woman){
                    Intent intent = new Intent(Part50.this, Part65.class);
                    intent.putExtra("TITLE", "Pregnant woman");
                    startActivity(intent);
                } else if(breastfeeding_mother_above){
                    Intent intent = new Intent(Part50.this, Part9.class);
                    intent.putExtra("TITLE", "Breastfeeding mother within 42 days");
                    startActivity(intent);
                } else if(breastfeeding_mother_below){
                    Intent intent = new Intent(Part50.this, Part12.class);
                    intent.putExtra("TITLE", "Breastfeeding mother above 42 days");
                    startActivity(intent);
                } else if(_neonates){
                    Intent intent = new Intent(Part50.this, Part35.class);
                    intent.putExtra("TITLE", "neonates");
                    startActivity(intent);
                } else if(_infants){
                    Intent intent = new Intent(Part50.this, Part35.class);
                    intent.putExtra("TITLE", "infants");
                    startActivity(intent);
                } else if(_children){
                    Intent intent = new Intent(Part50.this, Part35.class);
                    intent.putExtra("TITLE", "children");
                    startActivity(intent);
                }  /*else if(_adolescents_girls_10_19&&_adolescents_girls_10_19) {
                    // Toast.makeText(getApplicationContext(),"Selected",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(Part50.this, Part72.class);
                    intent.putExtra("TITLE", "Adolescents Details ");
                    startActivity(intent);
                }*/

                else{
                    Intent intent = new Intent(Part50.this, Part14.class);
                    intent.putExtra("TITLE", "Other services");
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
                count2 = optArray.length();
                editText2 = new EditText[optArray.length()];
                for(int j=0; j<optArray.length(); j++){
                    final JSONObject opt_data = optArray.getJSONObject(j);
                    editText2[j] = new EditText(Part50.this);
                    editText2[j].setTag(opt_data.getString("code"));
                    editText2[j].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    setMargins(editText2[j], 0, 0, 0, 10);
                    editText2[j].setTag(opt_data.getString("code"));
                    editText2[j].setInputType(InputType.TYPE_CLASS_NUMBER);
                    editText2[j].setHint(opt_data.getString("name_en"));
                    editText2[j].setTextColor(getResources().getColor(R.color.black));
                    final int finalJ = j;
                    editText2[j].addTextChangedListener(new TextWatcher() {
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
                                    breastfeeding_mother_within_42 = "";
                                    breastfeeding_mother_within_42 = other.toString();
                                }else if(finalJ == 2){
                                    breastfeeding_mother_above_42 = "";
                                    breastfeeding_mother_above_42 = other.toString();
                                } else if(finalJ == 3){
                                    neonates = "";
                                    neonates = other.toString();
                                }else if(finalJ == 4){
                                    infants = "";
                                    infants = other.toString();
                                }else if(finalJ == 5){
                                    children = "";
                                    children = other.toString();
                                }else if(finalJ == 6){
                                    adolescents_girls_10_19 = "";
                                    adolescents_girls_10_19 = other.toString();
                                }else if(finalJ == 7){
                                    adolescents_boys_10_19 = "";
                                    adolescents_boys_10_19 = other.toString();
                                }else if(finalJ == 8){
                                    female_youth_20_24 = "";
                                    female_youth_20_24 = other.toString();
                                }else if(finalJ == 9){
                                    male_youth_20_24 = "";
                                    male_youth_20_24 = other.toString();
                                }else if(finalJ == 10){
                                    female_15_49 = "";
                                    female_15_49 = other.toString();
                                }else if(finalJ == 11){
                                    male_15_49 = "";
                                    male_15_49 = other.toString();
                                }else if(finalJ == 12){
                                    male_above_50 = "";
                                    male_above_50 = other.toString();
                                }else if(finalJ == 13){
                                    female_above_50 = "";
                                    female_above_50 = other.toString();
                                }else if(finalJ == 14){
                                    children_6_9 = "";
                                    children_6_9 = other.toString();
                                }

                            }

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {}
                    });
                    lytQuestions.addView(editText2[j]);
                }

            }


        }
        Button btn_back = new Button(this);
        btn_back.setText(getString(R.string.go_back_to_dashboard));
        btn_back.setBackgroundResource(R.drawable.cyan_corner_gradient);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                general.goBackDialoag(Part50.this);
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
        new AlertDialog.Builder(Part50.this)
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
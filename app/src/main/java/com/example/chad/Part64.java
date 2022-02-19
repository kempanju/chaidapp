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
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class Part64 extends AppCompatActivity {


    General general;
    ProgressDialog p;

    String village_name = "";
    String village_id = "";
    TextView title;
    LinearLayout lytHouse;

    ListView listView;
    Button btn_upload;
    EditText search;
    ArrayAdapter<String> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_answers);
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        title = (TextView) findViewById(R.id.title);
        title.setText(R.string.select_hamlet);

        general = new General(this);
        String questions = general.getFile("questionnaire", Part64.this);
        try {
            createAQuestion(questions);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        btn_upload = (Button) findViewById(R.id.btn_upload);
        btn_upload.setVisibility(View.GONE);

        search = (EditText) findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                itemsAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void createAQuestion(String questions) throws JSONException {
        JSONObject json = new JSONObject(questions);
        String house_hold_string = json.getString("street");
        final JSONArray house_hold_array = new JSONArray(house_hold_string);
        ArrayList<String> list = new ArrayList<>();
        JSONObject obj;
        for(int i=0; i<house_hold_array.length(); i++){
            obj = house_hold_array.getJSONObject(i);
            String house_hold = obj.getString("name");
            list.add(house_hold);
        }

        itemsAdapter = new ArrayAdapter<String>(this, R.layout.listview_item, list);

        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String stringText;

                //in normal case
                stringText= ((TextView)view).getText().toString();
                try {
                    JSONObject obj2 = house_hold_array.getJSONObject(position);
                    village_name = obj2.getString("name");
                    village_id = obj2.getString("id");
                    continueDialog(stringText);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });


        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);

        FloatingActionButton fab = new FloatingActionButton(Part64.this);
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
                general.CreateSmallObjectForMainObjectInt(Part64.this,"house_hold", "street_id", Integer.parseInt(village_id));
                //general.CreateMainObjectInt("street_id", Integer.parseInt(village_id), Part64.this);
                Intent intent = new Intent(Part64.this, Part44.class);
                startActivity(intent);
            }
        });


        Button btn_back = new Button(this);
        btn_back.setText(getString(R.string.go_back_to_dashboard));
        btn_back.setBackgroundResource(R.drawable.cyan_corner_gradient);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                general.goBackDialoag(Part64.this);
            }
        });
    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public void continueDialog(String stringText) {

        general = new General(Part64.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(Part64.this);

        builder.setTitle(getString(R.string.select));
        builder.setMessage(stringText);

        builder.setPositiveButton(getString(R.string.con), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                general.CreateSmallObjectForMainObjectInt(Part64.this,"house_hold", "street_id", Integer.parseInt(village_id));
                Intent intent = new Intent(Part64.this, Part44.class);
                startActivity(intent);
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
        new AlertDialog.Builder(Part64.this)
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
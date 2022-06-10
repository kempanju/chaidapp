package com.example.chad;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ListOfAnswers extends AppCompatActivity {

    ListView listView;
    TextView title;
    Button btn_upload;
    ArrayList<String> filenames = new ArrayList<>();
    General general;
    EditText search;
    ArrayAdapter<String> itemsAdapter;

    String fileMainPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_answers);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        general = new General(ListOfAnswers.this);
        title = (TextView) findViewById(R.id.title);
        title.setVisibility(View.GONE);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        String Title = "Orodha ya taarifa";
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>"+ Title +" </font>"));
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {

            File root = new File(this.getFilesDir(), "Answers");
            fileMainPath = root.getAbsolutePath()+"/";
        } else {
            fileMainPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Answers/";    // it will return root directory of internal storage
        }
        File yourDir = new File(fileMainPath);

        if(yourDir.listFiles() != null){
            for (File f : yourDir.listFiles()) {
                if (f.isFile())
                    filenames.add(f.getName());
            }
        }else{
            Toast.makeText(ListOfAnswers.this, R.string.nodata, Toast.LENGTH_SHORT).show();
        }


        itemsAdapter = new ArrayAdapter<String>(this, R.layout.listview_item, filenames);

      listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String stringText;

                //in normal case
                stringText= ((TextView)view).getText().toString();

                editDialog(stringText);
            }

        });




        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDialog();
            }
        });

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void sendDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListOfAnswers.this);

        builder.setTitle(R.string.confirm);
        builder.setMessage(R.string.sendtosever);

        builder.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                File yourDir = new File(fileMainPath);
                File file = null;
                StringBuilder text;
                if(yourDir.listFiles() != null){
                    for (File f : yourDir.listFiles()) {
                        if (f.isFile())
                            file = new File(yourDir,f.getName());
                        text = new StringBuilder();
                        try {
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            String line;

                            while ((line = br.readLine()) != null) {
                                text.append("").append(line);
                            }
                            br.close();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        // Do your stuff
                        if(general.isNetworkAvailable(ListOfAnswers.this)){
                            try {
                                general.uploadData(ListOfAnswers.this, text.toString(), yourDir+"/"+f.getName());
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(ListOfAnswers.this, R.string.switch_mobile_data, Toast.LENGTH_SHORT).show();
                        }

                    }
                }
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

    public void editDialog(final String filename) {

        final String path =fileMainPath;
        general = new General(ListOfAnswers.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(ListOfAnswers.this);

        builder.setTitle(R.string.edit);
        builder.setMessage(filename);

        builder.setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                        // Do your stuff
                JSONObject object_answers = null;
                JSONArray array_results = null;
                JSONObject array_house_hold = null;
                String string_object_answers = general.getFile_path(path ,filename, ListOfAnswers.this);
                try {
                    object_answers = new JSONObject(string_object_answers);
                    array_house_hold = object_answers.getJSONObject("house_hold");
                    array_results = object_answers.getJSONArray("results");
                    for (int k = 0; k < array_results.length(); k++) {
                        JSONObject opt_data_results = array_results.getJSONObject(k);
                        if(opt_data_results.getString("code").equals("CHAD4")){
                        String answer_code = opt_data_results.getString("answer_code");
                            switch (answer_code) {
                                case "CHAD4A": {
                                    try{
                                        array_house_hold.getString("name");
                                        Intent intent = new Intent(ListOfAnswers.this, Part44.class);
                                        intent.putExtra("FILENAME", filename);
                                        intent.putExtra("ANSWERS", string_object_answers);
                                        startActivity(intent);
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                        Intent intent = new Intent(ListOfAnswers.this, Part46.class);
                                        intent.putExtra("FILENAME", filename);
                                        intent.putExtra("ANSWERS", string_object_answers);
                                        startActivity(intent);
                                    }
                                    break;
                                }
                                case "CHAD4B": {
                                    Intent intent = new Intent(ListOfAnswers.this, Part2.class);
                                    intent.putExtra("FILENAME", filename);
                                    intent.putExtra("ANSWERS", string_object_answers);
                                    startActivity(intent);
                                    break;
                                }
                                case "CHAD4C": {
                                    Intent intent = new Intent(ListOfAnswers.this, Part24.class);
                                    intent.putExtra("FILENAME", filename);
                                    intent.putExtra("ANSWERS", string_object_answers);
                                    startActivity(intent);
                                    break;
                                }
                                default: {
                                    break;
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }).setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                File yourDir = new File(fileMainPath);

                File fdelete = new File(yourDir+"/"+filename);
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        Toast.makeText(ListOfAnswers.this, R.string.file_delete_successful, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ListOfAnswers.this, Dashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra(getString(R.string.exit), true);
                        startActivity(intent);
                    } else {
                        System.out.println(getString(R.string.file_not_deleted));
                    }
                }
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
        new AlertDialog.Builder(ListOfAnswers.this)
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
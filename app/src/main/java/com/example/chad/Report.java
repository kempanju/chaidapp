package com.example.chad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class Report extends AppCompatActivity {

    Button btn_refferal;
    TextView today,thisMonth,total,thisWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Taarifa</font>"));

        btn_refferal = (Button) findViewById(R.id.btn_refferal);
        today = findViewById(R.id.today);
        thisMonth = findViewById(R.id.thisMonth);
        total = findViewById(R.id.total);
        thisWeek = findViewById(R.id.thisWeek);


        btn_refferal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Report.this, ReportRefferal.class);
                startActivity(intent);
            }
        });
        HttpChwReportUsers();

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
        new AlertDialog.Builder(Report.this)
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

    private void HttpChwReportUsers () {

            General gn=new General(getApplicationContext());
            String urls =gn.url + "home/chwReport";
            try {

                Log.i("data",urls);


                RequestParams params = new RequestParams();
                params.put("username", gn.getFile("username", getApplicationContext()));


                final int DEFAULT_TIMEOUT = 60 * 1000;
                AsyncHttpClient client = new AsyncHttpClient();
                client.addHeader("Authorization", "Bearer "+ gn.getFile("token", getApplicationContext()));
                client.setTimeout(DEFAULT_TIMEOUT);
                client.post(urls, params, new AsyncHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            String str = new String(responseBody, StandardCharsets.UTF_8);
                            Log.i("data",""+str);
                            JSONObject jsonObject=new JSONObject(str);
                            int total_v=jsonObject.getInt("total");
                            int this_month=jsonObject.getInt("total_month");
                            int today_v=jsonObject.getInt("total_today");
                            int week_v=jsonObject.getInt("total_week");

                            today.setText(""+today_v);
                            thisMonth.setText(""+this_month);
                            total.setText(""+total_v);
                            thisWeek.setText(""+week_v);

                        } catch (Exception e) {
                           e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        try {
                            String str = new String(responseBody, StandardCharsets.UTF_8);
                            Log.i("data",""+str);

                            Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                         e.printStackTrace();
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }


    }

}
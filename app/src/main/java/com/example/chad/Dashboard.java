package com.example.chad;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Dashboard extends AppCompatActivity {
    General general;
    ProgressDialog p;
    Filling filling;
    MediaPlayer mp;

    LinearLayout btn_questionnaire;
    LinearLayout btn_logout;
    LinearLayout btn_menu3;
    LinearLayout report;
    TextView txt_username, txt_greetings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        general = new General(Dashboard.this);
        requestPermision();
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        String dashboard = "Chaid-tool "+ general.getVersionNumber();
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>"+ dashboard +" </font>"));

        filling = new Filling();
        mp = MediaPlayer.create(this, R.raw.button);


        btn_questionnaire = (LinearLayout) findViewById(R.id.questionnaire);
        btn_logout = (LinearLayout) findViewById(R.id.logout);
        btn_menu3 = (LinearLayout) findViewById(R.id.Menu3);
        report = (LinearLayout) findViewById(R.id.report);
        txt_username = (TextView) findViewById(R.id.username);

        btn_questionnaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject MainObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();

                Filling filling = new Filling();
                filling.writeToFile("ResultArray", jsonArray.toString(), Dashboard.this);
                filling.writeToFile("MainObject", MainObject.toString(), Dashboard.this);

                Intent intent = new Intent(Dashboard.this, MainActivity.class);
                startActivity(intent);
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, Report.class);
                startActivity(intent);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog();
            }
        });

        btn_menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, ListOfAnswers.class);
                startActivity(intent);
            }
        });

        String usr = general.getFile("username", Dashboard.this);
        String questions = general.getFile("questionnaire", Dashboard.this);
        JSONObject json = null;
        try {
            json = new JSONObject(questions);
            String full_name = json.getString("full_name");
            int id = json.getInt("id");
            general.CreateMainObject("user_id", String.valueOf(id), Dashboard.this);
            if(usr.equals("")){
                loginDialog();

            }else {
                greetUser(full_name);
            }
        } catch (JSONException e) {
            loginDialog();
            e.printStackTrace();
        }




    }

    protected void loginDialog(){
        general = new General(Dashboard.this);
        //database = new Database(this);
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(Dashboard.this);
        View promptsView = li.inflate(R.layout.login_dialog, null);
        final EditText UserName = (EditText) promptsView.findViewById(R.id.UserName);
        final EditText Password = (EditText) promptsView.findViewById(R.id.Password);
        final TextView btnChangeLanguage = (TextView) promptsView.findViewById(R.id.btnChangeLanguage);

        btnChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguageDialog();
            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Dashboard.this);
        alertDialogBuilder.setView(promptsView);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.login,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                if(general.isNetworkAvailable(Dashboard.this)){
                                    updateAccesToken(UserName.getText().toString(), Password.getText().toString());
                                }else{
                                    Toast.makeText(Dashboard.this, R.string.switch_mobile_data, Toast.LENGTH_SHORT).show();
                                    loginDialog();
                                }

                            }
                        })
                .setNegativeButton(R.string.exit,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void updateAccesToken(String usernamesdata,String password) {
        p = new ProgressDialog(Dashboard.this);
        p.setMessage(this.getResources().getString(R.string.verifying_credentials));
        p.setIndeterminate(false);
        p.setCancelable(false);
        general = new General(Dashboard.this);
        try {
            String urls = general.url+"login";

            RequestParams params = new RequestParams();
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("username", usernamesdata);
            jsonObject.put("password", password);

            StringEntity entity = new StringEntity(jsonObject.toString());


            final int DEFAULT_TIMEOUT = 20 * 1000;
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(DEFAULT_TIMEOUT);
            client.post(getApplicationContext(), urls, entity, "application/json", new AsyncHttpResponseHandler() {

                        @Override
                        public void onStart() {
                            super.onStart();
                            try {
                                p.show();
                            }catch (Exception e){

                            }
                        }
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                p.hide();

                            }catch (Exception e){

                            }
                            try {
                                String resuldata = new String(responseBody, "UTF-8");
                                Log.d("splashAccessToken: ", resuldata);

                                JSONObject jsonObject = new JSONObject(resuldata);

                                String access_token =jsonObject.getString("access_token");
                                String username=jsonObject.getString("username");
                                greetUser(username);

                                Filling filling = new Filling();
                                filling.writeToFile("token", access_token, Dashboard.this);
                                filling.writeToFile("username", username, Dashboard.this);
                                userDetails();

                            } catch (Exception e) {
                                Toast.makeText(Dashboard.this, R.string.please_try_again, Toast.LENGTH_LONG).show();
                                loginDialog();
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            p.hide();
                            try {
                                String resuldata = new String(responseBody, "UTF-8");
                                Toast.makeText(Dashboard.this, R.string.failed, Toast.LENGTH_LONG).show();
                                loginDialog();

                            } catch (Exception e) {
                                Toast.makeText(Dashboard.this, R.string.please_try_again, Toast.LENGTH_LONG).show();
                                loginDialog();
                                e.printStackTrace();

                            }
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void greetUser(String username) {
        txt_username = (TextView) findViewById(R.id.username);
        txt_greetings = (TextView) findViewById(R.id.greetings);
        txt_username.setText(username.toString().toUpperCase().charAt(0)+username.toString().substring(1,username.toString().length()));
        txt_greetings.setText(Dashboard.this.getResources().getString(R.string.welcome));
    }

    private void userDetails() {
        general = new General(Dashboard.this);
        p = new ProgressDialog(Dashboard.this);
        p.setMessage(this.getResources().getString(R.string.get_questionnaire));
        p.setIndeterminate(false);
        p.setCancelable(false);
        String urls = general.url+"home/initialData";
        RequestParams params = new RequestParams();
        params.put("username", general.getFile("username", Dashboard.this));

        final int DEFAULT_TIMEOUT = 260 * 1000;
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer "+ general.getFile("token", Dashboard.this));
        client.setTimeout(DEFAULT_TIMEOUT);
        client.post(urls, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        p.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        p.hide();
                        try {
                            String response = new String(responseBody, "UTF-8");

                            JSONObject json_data_info = new JSONObject(response);
                            Filling filling = new Filling();
                            filling.writeToFile("questionnaire", json_data_info.toString(), Dashboard.this);
                            //Log.i("responselogin: ", "response:" + String.valueOf(response));
                            playButtonSound();


                        } catch (Exception e) {
                            //finish();
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        p.hide();
                        Log.i("chaid", "fail");
                        try {
                            String response = new String(responseBody, "UTF-8");

                            Log.d("responselogin: ", "response:" + String.valueOf(response));
                            Toast.makeText(Dashboard.this, R.string.failtogetquestionaire, Toast.LENGTH_LONG).show();
                            loginDialog();

                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                    }
                }
        );
    }

    private void playButtonSound() {
        try {
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
                mp = MediaPlayer.create(Dashboard.this, R.raw.button);
            } mp.start();
        } catch(Exception e) { e.printStackTrace(); }
    }

    public void requestPermision(){
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.VIBRATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.SEND_SMS};
        if(!hasPermissions(getApplicationContext(), PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public void CreateObjectForMain(){
        JSONObject object = new JSONObject();
        general = new General(Dashboard.this);
        general.CreateMainObjectObject("post_delivery", object, Dashboard.this);
        general.CreateMainObjectObject("pregnant_woman", object, Dashboard.this);
        general.CreateMainObjectObject("child_under_five", object, Dashboard.this);
    }

    public void logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);

        builder.setTitle(getString(R.string.confirm));
        builder.setMessage(R.string.logout_warning);

        builder.setPositiveButton(getString(R.string.logout), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                filling.writeToFile("token", "", Dashboard.this);
                filling.writeToFile("username", "", Dashboard.this);
                finish();
            }

        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }

        }).setNeutralButton(R.string.quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
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
        new AlertDialog.Builder(Dashboard.this)
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
package com.example.chad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class General {
    //Database database;
    Context context;
    ProgressDialog p;

    public General(Context context){
        this.context = context;
    }
    //public String url = "http://chaid.mkapafoundation.or.tz/api/";
    public String url = "http://172.20.10.2:9090/api/";

    public String version_number = "5.3";

    public boolean isNetworkAvailable(Context ctx){
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo ni = cm.getActiveNetworkInfo();

        return (ni != null && ni.isConnected());
    }
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddress = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddress.equals("");
        } catch (Exception e) {
            return true;
        }
    }

    public String getFile(String  filename,Context context){
        Filling filling = new Filling();
        return filling.readFromFile(filename, context);
    }

    public String getFile_path(String path, String  filename,Context context){
        Filling filling = new Filling();
        return filling.readFromFile(path, filename, context);
    }

    public String getVersionNumber(){
        return this.version_number;
    }

    public void saveData(final Context context) throws UnsupportedEncodingException {
        p = new ProgressDialog(context);
        p.setMessage("Saving Data...");
        p.setIndeterminate(false);
        p.setCancelable(false);
        String urls = url+"home/chadSaveData";

        Calendar today = Calendar.getInstance();
        today.clear(Calendar.HOUR); today.clear(Calendar.MINUTE); today.clear(Calendar.SECOND);
        final Date todayDate = today.getTime();

        final Filling filling = new Filling();

        final int DEFAULT_TIMEOUT = 600 * 1000;
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer "+ getFile("token", context));
        StringEntity entity = new StringEntity(getFile("MainObject", context));

        ///Log.i("CHAIDDATA",entity.toString());

        client.setTimeout(DEFAULT_TIMEOUT);

        client.post(context, urls, entity, "application/json", new AsyncHttpResponseHandler() {

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
                        p.hide();
                        try {
                            String resuldata = new String(responseBody, "UTF-8");
                            Log.d("splashAccessToken: ", resuldata);
                            Toast.makeText(context, "Data sent", Toast.LENGTH_SHORT).show();
                            userDetails(context);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        p.hide();
                        try {
                            String resuldata = new String(responseBody, "UTF-8");
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                            //filling.writeToFile(todayDate + " " + respondent + " " + house_hold_id, getFile("MainObject", context), context);
                            filling.saveFileToFolderContext(System.currentTimeMillis()+" "+todayDate, getFile("MainObject", context), context);
                        } catch (Exception e) {
                            Toast.makeText(context, "Failed to save file", Toast.LENGTH_SHORT).show();
                            saveDialog();
                            //filling.writeToFile(todayDate + " " + respondent + " " + house_hold_id, getFile("MainObject", context), context);
                            //filling.saveFileToFolderContext(System.currentTimeMillis()+" "+todayDate, getFile("MainObject", context), context);
                            e.printStackTrace();

                        }
                    }
                }
        );
    }

    public void uploadData(final Context context, String data, final String filePath) throws UnsupportedEncodingException {
        p = new ProgressDialog(context);
        p.setMessage(context.getResources().getString(R.string.sending_data));
        p.setIndeterminate(false);
        p.setCancelable(false);
        String urls = url+"home/chadSaveData";

        Log.i("CHAIDDATA",data);


        final int DEFAULT_TIMEOUT = 600 * 1000;
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer "+ getFile("token", context));
        StringEntity entity = new StringEntity(data);


        client.setTimeout(DEFAULT_TIMEOUT);
        client.post(context, urls, entity, "application/json", new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        p.show();
                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        p.hide();
                        try {
                            String resuldata = new String(responseBody, "UTF-8");
                            Log.d("splashAccessToken: ", resuldata);
                            Toast.makeText(context, context.getResources().getString(R.string.data_sent), Toast.LENGTH_SHORT).show();
                            File fdelete = new File(filePath);
                            if (fdelete.exists()) {
                                if (fdelete.delete()) {
                                    Toast.makeText(context, context.getResources().getString(R.string.file_delete_successful) + filePath, Toast.LENGTH_SHORT).show();
                                } else {
                                    System.out.println(context.getResources().getString(R.string.file_not_deleted) + filePath);
                                }
                            }
                            Intent intent = new Intent(context, Dashboard.class);
                            context.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        p.hide();
                        try {
                            String resuldata = new String(responseBody, "UTF-8");
                            if(statusCode == 401){

                            }else{
                                Toast.makeText(context, statusCode +" - "+error+" - "+ context.getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                            Calendar today = Calendar.getInstance();
                            today.clear(Calendar.HOUR); today.clear(Calendar.MINUTE); today.clear(Calendar.SECOND);
                            Date todayDate = today.getTime();

                            //JSONObject jsonObject = new JSONObject(resuldata);
                            JSONObject object = new JSONObject(getFile("MainObject", context));
                            String respondent = object.getString("respondent");
                            JSONObject house_hold = object.getJSONObject("house_hold");
                            int house_hold_id  = house_hold.getInt("house_hold_id");
                            Filling filling = new Filling();
                            filling.saveFileToFolder(todayDate + " " + respondent + " " + house_hold_id, getFile("MainObject", context));
                            Intent intent = new Intent(context, Dashboard.class);
                            context.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                }
        );
    }

    public void CreateObjectForMain(Context context){
        JSONObject object = new JSONObject();
        CreateMainObjectObject("post_delivery", object, context);
        CreateMainObjectObject("pregnant_woman", object, context);
        CreateMainObjectObject("child_under_five", object, context);
        CreateMainObjectObject("house_hold", object, context);
        CreateMainObjectObject("neonates", object, context);
        CreateMainObjectObject("infants", object, context);
        CreateMainObjectObject("children", object, context);
    }
    public void goBackDialoag(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(context.getResources().getString(R.string.confirm));
        builder.setMessage(R.string.Areyousuregoback);

        builder.setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, Dashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                context.startActivity(intent);
            }

        }).setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }

        });

/*        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // I do not need any action here you might
                dialog.dismiss();
            }
        });*/

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void userDetails(final Context context) {
        p = new ProgressDialog(context);
        p.setMessage(context.getResources().getString(R.string.get_questionnaire));
        p.setIndeterminate(false);
        p.setCancelable(false);
        String urls = url+"home/initialData";

        RequestParams params = new RequestParams();
        params.put("username", getFile("username", context));

        final int DEFAULT_TIMEOUT = 600 * 1000;
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer "+ getFile("token", context));
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
                            filling.writeToFile("questionnaire", json_data_info.toString(), context);
                            //Log.i("responselogin: ", "response:" + String.valueOf(response));
                            Intent intent = new Intent(context, Dashboard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("EXIT", true);
                            context.startActivity(intent);

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

                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                    }
                }
        );
    }

    public void getRefferalList(final Context context) throws UnsupportedEncodingException {
        p = new ProgressDialog(context);
        p.setMessage(context.getResources().getString(R.string.get_referrals));
        p.setIndeterminate(false);
        p.setCancelable(false);
        String urls = url+"home/getReferralListUser";

        final int DEFAULT_TIMEOUT = 120 * 1000;
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", "Bearer "+ getFile("token", context));
        RequestParams params = new RequestParams();


        params.put("username", getFile("username", context));


        client.setTimeout(DEFAULT_TIMEOUT);
        client.post(context, urls, params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        p.show();
                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        p.hide();
                        try {
                            String resuldata = new String(responseBody, "UTF-8");
                            Log.d("splashAccessToken: ", resuldata);
                            //Toast.makeText(context, resuldata, Toast.LENGTH_LONG).show();
                            JSONArray json_data_info = new JSONArray(resuldata);
                            Filling filling = new Filling();
                            filling.writeToFile("referral", json_data_info.toString(), context);

                            ((Activity) context).finish();
                            Intent intent = new Intent(context, ReportRefferal.class);
                            context.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        p.hide();
                        try {
                            String resuldata = new String(responseBody, "UTF-8");
                            Toast.makeText(context, context.getResources().getString(R.string.please_try_again)+ statusCode, Toast.LENGTH_LONG).show();
/*
                            Calendar today = Calendar.getInstance();
                            today.clear(Calendar.HOUR); today.clear(Calendar.MINUTE); today.clear(Calendar.SECOND);
                            Date todayDate = today.getTime();

                            //JSONObject jsonObject = new JSONObject(resuldata);
                            JSONObject object = new JSONObject(getFile("MainObject", context));
                            String respondent = object.getString("respondent");
                            JSONObject house_hold = object.getJSONObject("house_hold");
                            int house_hold_id  = house_hold.getInt("house_hold_id");
                            Filling filling = new Filling();
                            filling.saveFileToFolder(todayDate + " " + respondent + " " + house_hold_id, getFile("MainObject", context));
                            Intent intent = new Intent(context, Dashboard.class);
                            context.startActivity(intent);*/
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                }
        );
    }
    private void saveDialog() {
        Calendar today = Calendar.getInstance();
        final Date todayDate = today.getTime();
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.save_dialog, null);

        final EditText filename = promptsView.findViewById(R.id.filename);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle(context.getResources().getString(R.string.save_file));
        alertDialogBuilder.setIcon(context.getDrawable(R.drawable.survey));
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.save),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                if(!filename.getText().toString().equals("")){
                                    Filling filling = new Filling();
                                    filling.saveFileToFolderContext(filename.getText().toString()+" "+todayDate, getFile("MainObject", context), context);

                                }
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public JSONArray getChad16(){
        JSONArray array = null;
        String chad16 = getFile("CHAD16", context);
        try {
            JSONObject object = new JSONObject(chad16);
            array = object.getJSONArray("CHAD16");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

    public void CreateMainObject(String Qcode, String Acode, Context context){
        try {
            JSONObject MainObject = new JSONObject(getFile("MainObject", context));
            MainObject.put(Qcode, Acode);
            Filling filling = new Filling();
            filling.writeToFile("MainObject", MainObject.toString(), context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CreateMainObjectInt(String Qcode, int Acode, Context context){
        try {
            JSONObject MainObject = new JSONObject(getFile("MainObject", context));
            MainObject.put(Qcode, Acode);
            Filling filling = new Filling();
            filling.writeToFile("MainObject", MainObject.toString(), context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CreateMainObject2(String Qcode, JSONArray Acode, Context context){
        try {
            JSONObject MainObject = new JSONObject(getFile("MainObject", context));
            MainObject.put(Qcode, Acode);
            Filling filling = new Filling();
            filling.writeToFile("MainObject", MainObject.toString(), context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CreateMainObjectObject(String Qcode, JSONObject Acode, Context context){
        JSONObject MainObject;
        try {
            if(getFile("MainObject", context).equals("")){
                MainObject = new JSONObject();
                MainObject.put(Qcode, Acode);
                Filling filling = new Filling();
                filling.writeToFile("MainObject", MainObject.toString(), context);
            }else{
                MainObject = new JSONObject(getFile("MainObject", context));
                MainObject.put(Qcode, Acode);
                Filling filling = new Filling();
                filling.writeToFile("MainObject", MainObject.toString(), context);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String AddObjectToResultArray(String Qcode, String Acode, Context context){
        String array = "";
        JSONArray jsonArray;
        try {
            if(getFile("ResultArray", context).equals("")){
                jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", Qcode);
                jsonObject.put("answer_code", Acode);
                jsonObject.put("comment", "None");
                jsonArray.put(jsonObject);
                Filling filling = new Filling();
                filling.writeToFile("ResultArray", jsonArray.toString(), context);
            }else{
                jsonArray = new JSONArray(getFile("ResultArray", context));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code", Qcode);
                jsonObject.put("answer_code", Acode);
                jsonObject.put("comment", "None");
                jsonArray.put(jsonObject);
                Filling filling = new Filling();
                filling.writeToFile("ResultArray", jsonArray.toString(), context);
            }
            array = jsonArray.toString();


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

    public void AddObjectToResultArray2(String Qcode, JSONArray Acode, Context context){
        try {
            JSONArray jsonArray = new JSONArray(getFile("ResultArray", context));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", Qcode);
            jsonObject.put("answer_code", Acode);
            jsonObject.put("comment", "None");
            jsonArray.put(jsonObject);
            Filling filling = new Filling();
            filling.writeToFile("ResultArray", jsonArray.toString(), context);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CreateSmallObjectForMainObjectInt(Context context, String object_name, String key, int value){
        JSONObject object;
        try {
            object = new JSONObject(getFile("MainObject", context));
            JSONObject post_delivery = object.getJSONObject(object_name);
            post_delivery.put(key, value);
            CreateMainObjectObject(object_name, post_delivery, context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CreateSmallObjectForMainObjectString(Context context, String object_name, String key, String value){
        JSONObject object;
        try {
            object = new JSONObject(getFile("MainObject", context));
            JSONObject jsonObject = object.getJSONObject(object_name);
            jsonObject.put(key, value);
            CreateMainObjectObject(object_name, jsonObject, context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void createFabBlockString(Context context, Bundle extras, String filename, final Class<? extends Activity> next_activity, String answer_code, String string_object_answers, String sub_object, String item) {
        if (extras != null) {
            if (extras.containsKey("FILENAME")) {
                if(!answer_code.equals("")){
                    JSONObject object_answers = null;
                    try {
                        object_answers = new JSONObject(string_object_answers);
                        //Do something here
                        JSONObject obj = object_answers.getJSONObject(sub_object);
                        obj.remove(item);
                        obj.put(item, answer_code);

                        object_answers.remove(sub_object);
                        object_answers.put(sub_object, obj);

                        File yourDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Answers/");

                        File fdelete = new File(yourDir+"/"+filename);
                        if (fdelete.exists()) {
                            if (fdelete.delete()) {
                                //saveFileToFolderContext direct
                                try {
                                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Answers/";    // it will return root directory of internal storage
                                    File root = new File(path);
                                    if (!root.exists()) {
                                        root.mkdirs();       // create folder if not exist
                                    }
                                    File file = new File(path + filename);
                                    if (!file.exists()) {
                                        file.createNewFile();  // create file if not exist
                                    }
                                    BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
                                    buf.append(object_answers.toString());
                                    buf.close();
                                    Toast.makeText(context, context.getResources().getString(R.string.dataupdated), Toast.LENGTH_LONG).show();

                                    string_object_answers = object_answers.toString();
                                    Intent intent = new Intent(context, next_activity);
                                    if(next_activity == Dashboard.class){
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("EXIT", true);
                                    }
                                    intent.putExtra("FILENAME", filename);
                                    intent.putExtra("ANSWERS", string_object_answers);
                                    context.startActivity(intent);
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                    System.out.println(context.getResources().getString(R.string.file_not_deleted));
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Intent intent = new Intent(context, next_activity);
                    intent.putExtra("FILENAME", filename);
                    intent.putExtra("ANSWERS", string_object_answers);
                    context.startActivity(intent);
                }
            }else{
                if(!answer_code.equals("")){
                    CreateSmallObjectForMainObjectString(context, sub_object, item, answer_code);
                    Intent intent = new Intent(context, next_activity);
                    context.startActivity(intent);
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.please_enter_answer), Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            if(!answer_code.equals("")){
                CreateSmallObjectForMainObjectString(context, sub_object, item, answer_code);
                Intent intent = new Intent(context, next_activity);
                context.startActivity(intent);
            }else{
                Toast.makeText(context, "Please enter an answer to continue", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void createFabBlockInt(Context context, Bundle extras, String filename, final Class<? extends Activity> next_activity, String answer_code, String string_object_answers, String sub_object, String item) {

        if (extras != null) {
            if (extras.containsKey("FILENAME")) {
                if(!answer_code.equals("")){
                    JSONObject object_answers = null;
                    try {
                        object_answers = new JSONObject(string_object_answers);
                        //Do something here
                        JSONObject obj = object_answers.getJSONObject(sub_object);
                        obj.remove(item);
                        obj.put(item, Integer.parseInt(answer_code));

                        object_answers.remove(sub_object);
                        object_answers.put(sub_object, obj);

                        File yourDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Answers/");

                        File fdelete = new File(yourDir+"/"+filename);
                        if (fdelete.exists()) {
                            if (fdelete.delete()) {
                                //saveFileToFolderContext direct
                                try {
                                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Answers/";    // it will return root directory of internal storage
                                    File root = new File(path);
                                    if (!root.exists()) {
                                        root.mkdirs();       // create folder if not exist
                                    }
                                    File file = new File(path + filename);
                                    if (!file.exists()) {
                                        file.createNewFile();  // create file if not exist
                                    }
                                    BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
                                    buf.append(object_answers.toString());
                                    buf.close();
                                    Toast.makeText(context, "Data updated", Toast.LENGTH_LONG).show();

                                    string_object_answers = object_answers.toString();
                                    Intent intent = new Intent(context, next_activity);
                                    intent.putExtra("FILENAME", filename);
                                    intent.putExtra("ANSWERS", string_object_answers);
                                    context.startActivity(intent);
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                    System.out.println("file not Deleted");
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Intent intent = new Intent(context, next_activity);
                    intent.putExtra("FILENAME", filename);
                    intent.putExtra("ANSWERS", string_object_answers);
                    context.startActivity(intent);
                }
            }else{
                if(!answer_code.equals("")){
                    CreateSmallObjectForMainObjectInt(context, sub_object, item, Integer.parseInt(answer_code));
                    Intent intent = new Intent(context, next_activity);
                    context.startActivity(intent);
                }else{
                    Toast.makeText(context, "Please enter an answer to continue", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            if(!answer_code.equals("")){
                CreateSmallObjectForMainObjectInt(context, sub_object, item, Integer.parseInt(answer_code));
                Intent intent = new Intent(context, next_activity);
                context.startActivity(intent);
            }else{
                Toast.makeText(context, "Please enter an answer to continue", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private <T> Iterable<T> iterate(final Iterator<T> i){
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return i;
            }
        };
    }

    public void createFabBlockInt(Context context, Bundle extras, String filename, final Class<? extends Activity> next_activity, String answer_code, String string_object_answers, JSONArray elements) {
        String sub_object = "";
        String item = "";
        JSONObject object;

        if (extras != null) {
            if (extras.containsKey("FILENAME")) {
                if(!answer_code.equals("")){
                    JSONObject object_answers = null;
                    try {
                        for(int a=0; a<elements.length(); a++){
                            object = elements.getJSONObject(a);
                            if(a == 0){
                                sub_object = "post_delivery";
                                item = object.getString(sub_object);
                                object_answers = new JSONObject(string_object_answers);
                                //Do something here
                                JSONObject obj = object_answers.getJSONObject(sub_object);
                                obj.remove(item);
                                obj.put(item, Integer.parseInt(answer_code));

                                object_answers.remove(sub_object);
                                object_answers.put(sub_object, obj);

                                File yourDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Answers/");

                                File fdelete = new File(yourDir+"/"+filename);
                                if (fdelete.exists()) {
                                    if (fdelete.delete()) {
                                        //saveFileToFolderContext direct
                                        try {
                                            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Answers/";    // it will return root directory of internal storage
                                            File root = new File(path);
                                            if (!root.exists()) {
                                                root.mkdirs();       // create folder if not exist
                                            }
                                            File file = new File(path + filename);
                                            if (!file.exists()) {
                                                file.createNewFile();  // create file if not exist
                                            }
                                            BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
                                            buf.append(object_answers.toString());
                                            buf.close();
                                            Toast.makeText(context, "Data updated", Toast.LENGTH_LONG).show();

                                            string_object_answers = object_answers.toString();
                                            Intent intent = new Intent(context, next_activity);
                                            intent.putExtra("FILENAME", filename);
                                            intent.putExtra("ANSWERS", string_object_answers);
                                            context.startActivity(intent);
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                            System.out.println("file not Deleted");
                                        }
                                    }
                                }
                            }
                            if(a == 1){
                                sub_object = "child_under_five";
                                item = object.getString(sub_object);
                                object_answers = new JSONObject(string_object_answers);
                                //Do something here
                                JSONObject obj = object_answers.getJSONObject(sub_object);
                                obj.remove(item);
                                obj.put(item, Integer.parseInt(answer_code));

                                object_answers.remove(sub_object);
                                object_answers.put(sub_object, obj);

                                File yourDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Answers/");

                                File fdelete = new File(yourDir+"/"+filename);
                                if (fdelete.exists()) {
                                    if (fdelete.delete()) {
                                        //saveFileToFolderContext direct
                                        try {
                                            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Answers/";    // it will return root directory of internal storage
                                            File root = new File(path);
                                            if (!root.exists()) {
                                                root.mkdirs();       // create folder if not exist
                                            }
                                            File file = new File(path + filename);
                                            if (!file.exists()) {
                                                file.createNewFile();  // create file if not exist
                                            }
                                            BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
                                            buf.append(object_answers.toString());
                                            buf.close();
                                            Toast.makeText(context, "Data updated", Toast.LENGTH_LONG).show();

                                            string_object_answers = object_answers.toString();
                                            Intent intent = new Intent(context, next_activity);
                                            intent.putExtra("FILENAME", filename);
                                            intent.putExtra("ANSWERS", string_object_answers);
                                            context.startActivity(intent);
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                            System.out.println("file not Deleted");
                                        }
                                    }
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Intent intent = new Intent(context, next_activity);
                    intent.putExtra("FILENAME", filename);
                    intent.putExtra("ANSWERS", string_object_answers);
                    context.startActivity(intent);
                }
            }else{
                if(!answer_code.equals("")){
                    CreateSmallObjectForMainObjectInt(context, sub_object, item, Integer.parseInt(answer_code));
                    Intent intent = new Intent(context, next_activity);
                    context.startActivity(intent);
                }else{
                    Toast.makeText(context, "Please enter an answer to continue", Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            if(!answer_code.equals("")){
                CreateSmallObjectForMainObjectInt(context, sub_object, item, Integer.parseInt(answer_code));
                Intent intent = new Intent(context, next_activity);
                context.startActivity(intent);
            }else{
                Toast.makeText(context, "Please enter an answer to continue", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void createEditBlockString(final Context context, final String filename, final String answer_code, final String string_object_answers, final String sub_object, final String item) {
        JSONObject object_answers = null;
        if(!answer_code.equals("")){
            try {
                object_answers = new JSONObject(string_object_answers);
                //Do something here
                JSONObject obj = object_answers.getJSONObject(sub_object);
                obj.remove(item);
                obj.put(item, answer_code);

                object_answers.remove(sub_object);
                object_answers.put(sub_object, obj);

                File yourDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Answers/");

                File fdelete = new File(yourDir+"/"+filename);
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        Filling filling = new Filling();
                        filling.saveFileToFolderContext(filename, object_answers.toString(), context);
                    } else {
                        System.out.println("file not Deleted");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            Intent intent = new Intent(context, Dashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            context.startActivity(intent);
        }
    }

    public void createEditBlockInt(final Context context, final String filename, final String answer_code, final String string_object_answers, final String sub_object, final String item) {

        JSONObject object_answers = null;
        if(!answer_code.equals("")){
            try {
                object_answers = new JSONObject(string_object_answers);
                //Do something here
                JSONObject obj = object_answers.getJSONObject(sub_object);
                obj.remove(item);
                obj.put(item, Integer.parseInt(answer_code));

                object_answers.remove(sub_object);
                object_answers.put(sub_object, obj);

                File yourDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Answers/");

                File fdelete = new File(yourDir+"/"+filename);
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        Filling filling = new Filling();
                        filling.saveFileToFolderContext(filename, object_answers.toString(), context);
                    } else {
                        System.out.println("file not Deleted");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            Intent intent = new Intent(context, Dashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            context.startActivity(intent);
        }
    }

    public void createEditBlockInt(final Context context, final String filename, final String answer_code, String string_object_answers, JSONArray elements) {
        String sub_object = "";
        String item = "";
        JSONObject object;

        JSONObject object_answers = null;
        if(!answer_code.equals("")){
            try {
                for(int a=0; a<elements.length(); a++){
                    object = elements.getJSONObject(a);
                    if(a == 0){
                        sub_object = "post_delivery";
                        item = object.getString(sub_object);
                        object_answers = new JSONObject(string_object_answers);
                        //Do something here
                        JSONObject obj = object_answers.getJSONObject(sub_object);
                        obj.remove(item);
                        obj.put(item, Integer.parseInt(answer_code));

                        object_answers.remove(sub_object);
                        object_answers.put(sub_object, obj);

                        File yourDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Answers/");

                        File fdelete = new File(yourDir+"/"+filename);
                        if (fdelete.exists()) {
                            if (fdelete.delete()) {
                                //saveFileToFolderContext direct
                                try {
                                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Answers/";    // it will return root directory of internal storage
                                    File root = new File(path);
                                    if (!root.exists()) {
                                        root.mkdirs();       // create folder if not exist
                                    }
                                    File file = new File(path + filename);
                                    if (!file.exists()) {
                                        file.createNewFile();  // create file if not exist
                                    }
                                    BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
                                    buf.append(object_answers.toString());
                                    buf.close();
                                    Toast.makeText(context, "Data updated", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(context, Dashboard.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("EXIT", true);
                                    context.startActivity(intent);
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                    System.out.println("file not Deleted");
                                }
                            }
                        }
                    }
                    if(a == 1){
                        sub_object = "child_under_five";
                        item = object.getString(sub_object);
                        object_answers = new JSONObject(string_object_answers);
                        //Do something here
                        JSONObject obj = object_answers.getJSONObject(sub_object);
                        obj.remove(item);
                        obj.put(item, Integer.parseInt(answer_code));

                        object_answers.remove(sub_object);
                        object_answers.put(sub_object, obj);

                        File yourDir = new File(Environment.getExternalStorageDirectory().getPath() + "/Answers/");

                        File fdelete = new File(yourDir+"/"+filename);
                        if (fdelete.exists()) {
                            if (fdelete.delete()) {
                                //saveFileToFolderContext direct
                                try {
                                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Answers/";    // it will return root directory of internal storage
                                    File root = new File(path);
                                    if (!root.exists()) {
                                        root.mkdirs();       // create folder if not exist
                                    }
                                    File file = new File(path + filename);
                                    if (!file.exists()) {
                                        file.createNewFile();  // create file if not exist
                                    }
                                    BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
                                    buf.append(object_answers.toString());
                                    buf.close();
                                    Toast.makeText(context, "Data updated", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(context, Dashboard.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("EXIT", true);
                                    context.startActivity(intent);
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                    System.out.println("file not Deleted");
                                }
                            }
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            Intent intent = new Intent(context, Dashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            context.startActivity(intent);
        }
    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    protected void loginDialog(final Context context){
        //database = new Database(this);
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.login_dialog, null);
        final EditText UserName = (EditText) promptsView.findViewById(R.id.UserName);
        final EditText Password = (EditText) promptsView.findViewById(R.id.Password);
        final TextView btnChangeLanguage = (TextView) promptsView.findViewById(R.id.btnChangeLanguage);

        btnChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changeLanguageDialog();
            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setView(promptsView);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.login,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                if(isNetworkAvailable(context)){
                                    updateAccesToken(UserName.getText().toString(), Password.getText().toString());
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.switch_mobile_data), Toast.LENGTH_SHORT).show();
                                    loginDialog(context);
                                }

                            }
                        })
                .setNegativeButton(R.string.exit,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void updateAccesToken(String usernamesdata,String password) {
        p = new ProgressDialog(context);
        p.setMessage(context.getResources().getString(R.string.verifying_credentials));
        p.setIndeterminate(false);
        p.setCancelable(false);
        try {
            String urls = this.url+"login";

            RequestParams params = new RequestParams();
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("username", usernamesdata);
            jsonObject.put("password", password);

            StringEntity entity = new StringEntity(jsonObject.toString());


            final int DEFAULT_TIMEOUT = 60 * 1000;
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(DEFAULT_TIMEOUT);
            client.post(context, urls, entity, "application/json", new AsyncHttpResponseHandler() {

                        @Override
                        public void onStart() {
                            super.onStart();
                            p.show();
                        }
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            p.hide();
                            try {
                                String resuldata = new String(responseBody, "UTF-8");
                                Log.d("splashAccessToken: ", resuldata);

                                JSONObject jsonObject = new JSONObject(resuldata);

                                String access_token =jsonObject.getString("access_token");
                                String username=jsonObject.getString("username");

                                Filling filling = new Filling();
                                filling.writeToFile("token", access_token, context);
                                filling.writeToFile("username", username, context);

                            } catch (Exception e) {
                                Toast.makeText(context, context.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                                loginDialog(context);
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            p.hide();
                            try {
                                String resuldata = new String(responseBody, "UTF-8");
                                Toast.makeText(context, context.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                                loginDialog(context);

                            } catch (Exception e) {
                                Toast.makeText(context, context.getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                                loginDialog(context);
                                e.printStackTrace();

                            }
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

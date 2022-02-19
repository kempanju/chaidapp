package com.example.chad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Filling {
    public void writeToFile(String filename, String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFromFile(String path, String filename, Context context) {

        String ret = "";

        try {
            //InputStream inputStream = context.openFileInput(path + filename);
            FileInputStream fis = new FileInputStream (new File(path + filename));

            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ( (receiveString = bufferedReader.readLine()) != null ) {
                stringBuilder.append("").append(receiveString);
            }

            fis.close();
            ret = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public String readFromFile(String filename, Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
             //   Log.i("CHAIDDATA",ret);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public void saveFileToFolder(String filename, String data){
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Answers/";    // it will return root directory of internal storage
            File root = new File(path);
            if (!root.exists()) {
                root.mkdirs();       // create folder if not exist
            }
            File file = new File(path + filename);
            if (!file.exists()) {
                file.createNewFile();   // create file if not exist
            }
            BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
            buf.append(data);
            buf.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFileToFolderContext(String filename, String data, Context context){
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
            buf.append(data);
            buf.close();
            Toast.makeText(context, context.getResources().getString(R.string.datasaved), Toast.LENGTH_LONG).show();
            //((Activity) context).finish();
            Intent intent = new Intent(context, Dashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            context.startActivity(intent);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFileToFolderContext2(String filename, String data, Context context){
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
            buf.append(data);
            buf.close();
            Toast.makeText(context, context.getResources().getString(R.string.dataupdated), Toast.LENGTH_LONG).show();
            //((Activity) context).finish();
            Intent intent = new Intent(context, Dashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            context.startActivity(intent);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean saveFileToFolderContext3(String filename, String data, Context context){

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
            buf.append(data);
            buf.close();
            Toast.makeText(context, context.getResources().getString(R.string.dataupdated), Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}

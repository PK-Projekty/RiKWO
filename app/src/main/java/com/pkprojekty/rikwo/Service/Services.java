package com.pkprojekty.rikwo.Service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;

import com.pkprojekty.rikwo.UI.LocalizationFragment;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class Services extends Service {

    private Handler handler = new Handler();
    public static final String KEY_TASK_DESC = "key_task_desc";

    private String freq;
    private String directory;
    private Timer timer = null;
    private long s;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        if(timer!=null){
            timer.cancel();
        }else {
            timer = new Timer();
        }
        SharedPreferences preferences = getSharedPreferences("Preference", MODE_PRIVATE);
        freq = preferences.getString("Frequency","");
        directory = preferences.getString("Directory","");

//        Data data = new Data.Builder()
//                .putString(KEY_TASK_DESC, "Hey im sending the work data")
//                .build();
//
//        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(NotificationWorker.class)
//                .setInputData(data)
//                .build();

        //ustawienie odpowiedniego czasu w zależności od opcji czasu wykonywania kopii
        if(freq.isEmpty())
            Toast.makeText(getApplicationContext(), "Nie ustawiono częstotliwości wykonywania kopii", Toast.LENGTH_SHORT).show();
        else{
            if(freq.equals("Codziennie"))
                s=1;
            if(freq.equals("Co tydzień"))
                s=7;
            if(freq.equals("Co miesiąc"))
                s=30;
            if(freq.equals("Nigdy"))
                s=0;

            timer.scheduleAtFixedRate(new createBackup(), 0, s*2*1000);
            System.out.println(freq);
            System.out.println(directory);
            //s*24*60*60*1000
        }
    }


    //EXAMPLE
    class createBackup extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Wykonano kopie", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    class sendEmail extends TimerTask {

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i("Send email", "");
                    String[] TO = {""};
                    String[] CC = {""};
                    //backup file name and location
                    String smsFileName = "";
                    String callFileName = "";
                    File smsLocation = new File(directory, smsFileName);
                    File callLocation = new File(directory, callFileName);
                    Uri pathSMS = Uri.fromFile(smsLocation);
                    Uri pathCall = Uri.fromFile(callLocation);
                    //sending mail without user interaction
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:"))
                            .setType("text/plain")
                            .putExtra(Intent.EXTRA_EMAIL, TO)
                            .putExtra(Intent.EXTRA_CC, CC)
                            .putExtra(Intent.EXTRA_SUBJECT, "Kopia zapasowa")
                            .putExtra(Intent.EXTRA_TEXT, "Kopia zapasowa z aplikacji")
                            .putExtra(Intent.EXTRA_STREAM, pathSMS)
                            .putExtra(Intent.EXTRA_STREAM, pathCall);
                    try{
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        //finish();
                        Log.i("Finished sending email...","");
                    }
                    catch (android.content.ActivityNotFoundException ex)
                    {
                        Toast.makeText(getApplicationContext(), "There is no email client installed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void EmailButton() {
        Log.i("Send email", "");
        String[] TO = {""};
        String[] CC = {""};
        //backup file name and location
        String smsFileName = "";
        String callFileName = "";
        File smsLocation = new File(directory, smsFileName);
        File callLocation = new File(directory, callFileName);
        Uri pathSMS = Uri.fromFile(smsLocation);
        Uri pathCall = Uri.fromFile(callLocation);
        //sending mail without user interaction
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"))
                .setType("text/plain")
                .putExtra(Intent.EXTRA_EMAIL, TO)
                .putExtra(Intent.EXTRA_CC, CC)
                .putExtra(Intent.EXTRA_SUBJECT, "Kopia zapasowa")
                .putExtra(Intent.EXTRA_TEXT, "Kopia zapasowa z aplikacji")
                .putExtra(Intent.EXTRA_STREAM, pathSMS)
                .putExtra(Intent.EXTRA_STREAM, pathCall);
        try{
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            //finish();
            Log.i("Finished sending email...","");
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            Toast.makeText(getApplicationContext(), "There is no email client installed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
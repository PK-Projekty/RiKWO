package com.pkprojekty.rikwo.Service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;

import com.pkprojekty.rikwo.UI.LocalizationFragment;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class Services extends Service {

    private Handler handler = new Handler();
    public static final String KEY_TASK_DESC = "key_task_desc";

    private String freq = new LocalizationFragment().getFreq();
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
        Data data = new Data.Builder()
                .putString(KEY_TASK_DESC, "Hey im sending the work data")
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInputData(data)
                .build();

        //ustawienie odpowiedniego czasu w zależności od opcji czasu wykonywania kopii
        if(freq.equals("Codziennie"))
            s=1;
        if(freq.equals("Co tydzień"))
            s=7;
        if(freq.equals("Co miesiąc"))
            s=30;
        if(freq.equals("Nigdy"))
            s=0;
        timer.scheduleAtFixedRate(toast(), 0, s*2*1000);
        System.out.println(freq);
        //s*24*60*60*1000
    }


    //EXAMPLE
//    class createBackup extends TimerTask {
//        @Override
//        public void run() {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(getApplicationContext(),"Wykonano kopie", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }

//    }

    public TimerTask toast(){
        Toast.makeText(getApplicationContext(), "Wykonano kopie", Toast.LENGTH_SHORT).show();
        return null;
    }
    public void EmailButton() {
        Log.i("Send email", "");
        String[] TO = {""};
        String[] CC = {""};
        //backup file name and location
        String filename = "";
        File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
        Uri path = Uri.fromFile(filelocation);
        //sending mail without user interaction
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"))
                .setType("text/plain")
                .putExtra(Intent.EXTRA_EMAIL, TO)
                .putExtra(Intent.EXTRA_CC, CC)
                .putExtra(Intent.EXTRA_SUBJECT, "Kopia zapasowa")
                .putExtra(Intent.EXTRA_TEXT, "Kopia zapasowa z aplikacji")
                .putExtra(Intent.EXTRA_STREAM, path);
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
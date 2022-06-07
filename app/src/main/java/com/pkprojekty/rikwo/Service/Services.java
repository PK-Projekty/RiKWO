package com.pkprojekty.rikwo.Service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

import com.pkprojekty.rikwo.CallLog.BackupCallLog;
import com.pkprojekty.rikwo.Entities.CallData;
import com.pkprojekty.rikwo.Entities.SmsData;
import com.pkprojekty.rikwo.Sms.BackupSms;
import com.pkprojekty.rikwo.Xml.FileHandler;

import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Services extends Service {

    private Handler handler = new Handler();
    public static final String KEY_TASK_DESC = "key_task_desc";

    private String freq,directory, localization, uriTree;
    private boolean smsSwitchState, callLogSwitchState;
    private Timer timer = null;
    private DocumentFile smsFile;
    private DocumentFile callsFile;
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
        localization = preferences.getString("Localization","");
        uriTree = preferences.getString("uriTree","");
        callLogSwitchState = preferences.getBoolean("switchBackupCallLogUse",false);
        smsSwitchState = preferences.getBoolean("switchBackupSmsUse", false);

        System.out.print(callLogSwitchState);
        System.out.print(smsSwitchState);


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

//            if(localization.equals("Local"))
//                timer.scheduleAtFixedRate(new createLocalBackup(), 0, s*2*1000);
            if(localization.equals("E-mail"))
                timer.scheduleAtFixedRate(new sendEmail(), 0, s*2*1000);
//            if(localization.equals("Chmura"))
//                timer.scheduleAtFixedRate(new createBackupToCloud(), 0, s*2*1000);
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
                    //backup();
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
                    backup();
                    Log.i("Send email", "");
                    String[] TO = {""};
                    String[] CC = {""};
                    //backup file name and location
                    String smsFileName = smsFile.toString();
                    String callFileName = callsFile.toString();
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
                    smsFile.delete();
                    callsFile.delete();
                }
            });
        }
    }

    class createBackupToCloud extends TimerTask{

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    backup();

                }
            });
        }
    }

    private void backup(){
        FileHandler fh = new FileHandler(getApplicationContext());
        System.out.println("Tworzenie kopii zapasowej w wybranym katalogu: "+uriTree);
        ZoneId z = ZoneId.of("Europe/Warsaw");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        if (smsSwitchState) {
            ZonedDateTime zdt = ZonedDateTime.now(z);
            String smsFilename = zdt.format(formatter) + "-sms.xml";
            smsFile = Objects.requireNonNull(DocumentFile.fromTreeUri(getApplicationContext(), Uri.parse(uriTree))).createFile("text/xml", smsFilename);
            BackupSms backupSms = new BackupSms(getApplicationContext());
            List<List<SmsData>> smsData = backupSms.getAllSms();
            fh.storeSmsInXml(smsData, smsFile);
        }
        if (callLogSwitchState) {
            ZonedDateTime zdt = ZonedDateTime.now(z);
            String callsFilename = zdt.format(formatter) + "-calls.xml";
            callsFile = Objects.requireNonNull(DocumentFile.fromTreeUri(getApplicationContext(), Uri.parse(uriTree))).createFile("text/xml", callsFilename);
            BackupCallLog backupCallLog = new BackupCallLog(getApplicationContext());
            List<CallData> callData = backupCallLog.getAllCalls();
            fh.storeCallLogInXml(callData, callsFile);
        }
    }

}
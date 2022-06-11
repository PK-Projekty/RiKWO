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
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Services extends Service {

    private Handler handler = new Handler();
    public static final String KEY_TASK_DESC = "key_task_desc";

    private String freq,directory, localization, uriTree, email, password;
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
        restoreSettings();

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

            if(localization.equals("Local"))
                timer.scheduleAtFixedRate(new createLocalBackup(), 0, s*10*1000);
//            if(localization.equals("E-mail"))
//                timer.scheduleAtFixedRate(new sendEmail(), 0, s*2*1000);
//            if(localization.equals("Chmura"))
//                timer.scheduleAtFixedRate(new createBackupToCloud(), 0, s*2*1000);
            //s*24*60*60*1000
            //s*2*1000
        }
    }


    //EXAMPLE
    class createLocalBackup extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    restoreSettings();
                    backup();
                    Toast.makeText(getApplicationContext(),"Wykonano kopie", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void restoreSettings(){
        SharedPreferences preferences = getSharedPreferences("Preference", MODE_PRIVATE);
        freq = preferences.getString("Frequency","");
        directory = preferences.getString("Directory","");
        localization = preferences.getString("Localization","");
        uriTree = preferences.getString("uriTree","");
        callLogSwitchState = preferences.getBoolean("switchBackupCallLogUse",false);
        smsSwitchState = preferences.getBoolean("switchBackupSmsUse", false);
        email = preferences.getString("Email", "");
        password = preferences.getString("Password","");
    }

    class sendEmail extends TimerTask {

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    backup();
                    Log.i("Send email", "");
                    String[] TO = {"thehuberts11@gmail.com"};
                    String[] CC = {""};
                    //backup file name and location
                    String smsFileName = smsFile.toString();
                    String callFileName = callsFile.toString();
                    File smsLocation = new File(directory, smsFileName);
                    File callLocation = new File(directory, callFileName);
                    Uri pathSMS = Uri.fromFile(smsLocation);
                    Uri pathCall = Uri.fromFile(callLocation);
                    //sending mail without user interaction
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"))
                            .setType("message/rfc822")
                            .putExtra(Intent.EXTRA_EMAIL, TO)
                            .putExtra(Intent.EXTRA_CC, CC)
                            .putExtra(Intent.EXTRA_SUBJECT, "Kopia zapasowa")
                            .putExtra(Intent.EXTRA_TEXT, "Kopia zapasowa z aplikacji")
                            .putExtra(Intent.EXTRA_STREAM, pathSMS)
                            .putExtra(Intent.EXTRA_STREAM, pathCall);
                    try{
                        startActivity(Intent.createChooser(emailIntent, "Wybierz klienta poczty"));
//                        finish();
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
                    restoreSettings();
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

    class Mail extends TimerTask {

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String email = "thehuberts11@gmail.com";
                    String subject = "Kopia TEST";
                    String message = "Test wysylania wiadomosci";
                    SendMail sm = new SendMail(getApplicationContext(), email, subject, message);
                    sm.execute();
                }
            });
        }
    }

//    private class SendMail extends AsyncTask<Message, String, String> {
//        @Override
//        protected String doInBackground(Message... messages) {
//            try{
//                //When success
//                Transport.send(messages[0]);
//                return "Success";
//            }
//            catch (MessagingException e){
//                e.printStackTrace();
//                return "Error";
//            }
//        }
        //initialize progress dialog
//    }

}
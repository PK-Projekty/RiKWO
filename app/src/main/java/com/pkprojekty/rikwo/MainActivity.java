package com.pkprojekty.rikwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.pkprojekty.rikwo.CallLog.BackupCallLog;
import com.pkprojekty.rikwo.Entities.CallData;
import com.pkprojekty.rikwo.Sms.BackupSms;
import com.pkprojekty.rikwo.Entities.SmsData;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_TASK_DESC = "key_task_desc";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean ReadSmsPermissionGranted = false;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_SMS}, 1);
        } else {
            /* do nothing */
            /* permission is granted */
            ReadSmsPermissionGranted = true;

        }

        boolean ReadCallLogsPermissionGranted = false;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_CALL_LOG}, 1);
        } else {
            /* do nothing */
            /* permission is granted */
            ReadCallLogsPermissionGranted = true;
        }


        if (ReadSmsPermissionGranted && ReadCallLogsPermissionGranted) {
            backup();
        }
    }

    /* And a method to override */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            // READ_SMS permission toast
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No Permission granted", Toast.LENGTH_SHORT).show();
            }
            // READ_CALL_LOG permission toast
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No Permission granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void backup() {
        BackupSms backupSms =  new BackupSms(this);

        List<List<SmsData>> smsData = backupSms.getAllSms();

        int countMessagesInDraft = backupSms.countMessagesInDraft();
        int countMessagesInInbox = backupSms.countMessagesInInbox();
        int countMessagesInOutbox = backupSms.countMessagesInOutbox();
        int countMessagesInSent = backupSms.countMessagesInSent();

        TextView textView1 = findViewById(R.id.textView1);
        String txt1 = textView1.getText() + " " + countMessagesInDraft;
        textView1.setText(txt1);
        TextView textView2 = findViewById(R.id.textView2);
        String txt2 = textView2.getText() + " " + countMessagesInInbox;
        textView2.setText(txt2);
        TextView textView3 = findViewById(R.id.textView3);
        String txt3 = textView3.getText() + " " + countMessagesInOutbox;
        textView3.setText(txt3);
        TextView textView4 = findViewById(R.id.textView4);
        String txt4 = textView4.getText() + " " + countMessagesInSent;
        textView4.setText(txt4);

        BackupCallLog backupCallLog = new BackupCallLog(this);

        List<CallData> callData = backupCallLog.getAllCalls();

        String countCallsInCallLog = backupCallLog.countCallLog();

        TextView textView5 = findViewById(R.id.textView5);
        String txt5 = textView5.getText() + " " + countCallsInCallLog;
        textView5.setText(txt5);

    }

    public void EmailButton () {
        Log.i("Send email", "");
        String[] TO = {""};
        String[] CC = {""};
        //backup file name and location
        String filename = "";
        File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
        Uri path = Uri.fromFile(filelocation);
        //sending mail without user interaction
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Kopia zapasowa");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Kopia zapasowa z aplikacji");
        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
        try{
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...","");
        }
        catch (android.content.ActivityNotFoundException ex)
        {
            Toast.makeText(MainActivity.this, "There is no email client installed.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
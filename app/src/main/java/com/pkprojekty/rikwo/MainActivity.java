package com.pkprojekty.rikwo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.pkprojekty.rikwo.sms.BackupSms;
import com.pkprojekty.rikwo.sms.SmsData;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_SMS)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_SMS}, 1);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_SMS}, 1);
            }
        } else {
            /* do nothing */
            /* permission is granted */
            backup();
        }
    }

    /* And a method to override */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "No Permission granted", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void backup() {
        BackupSms backupSms =  new BackupSms(this);

        List<List<SmsData>> smsData = backupSms.getAllSms();

        int countMessagesInDraft = backupSms.countMessagesInDraft();
        int countMessagesInInbox = backupSms.countMessagesInInbox();
        int countMessagesInOutbox = backupSms.countMessagesInOutbox();
        int countMessagesInSent = backupSms.countMessagesInSent();

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(String.valueOf(countMessagesInSent));
    }
}
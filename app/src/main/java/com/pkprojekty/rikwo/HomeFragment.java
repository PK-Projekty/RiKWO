package com.pkprojekty.rikwo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.pkprojekty.rikwo.CallLog.BackupCallLog;
import com.pkprojekty.rikwo.CallLog.RestoreCallLog;
import com.pkprojekty.rikwo.Entities.CallData;
import com.pkprojekty.rikwo.Entities.SmsData;
import com.pkprojekty.rikwo.Sms.BackupSms;
import com.pkprojekty.rikwo.Sms.RestoreSms;
import com.pkprojekty.rikwo.Xml.FileHandler;

import java.io.File;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private Context homeContext;

    boolean ReadSmsPermissionGranted = false;
    boolean ReadCallLogsPermissionGranted = false;
    boolean WriteCallLogsPermissionGranted = false;
    boolean ReadExternalStoragePermissionGranted = false;
    boolean WriteExternalStoragePermissionGranted = false;

    // READ_SMS permission
    private ActivityResultLauncher<String> requestReadSmsPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    if (ContextCompat.checkSelfPermission(homeContext, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(homeContext, "Permission granted", Toast.LENGTH_SHORT).show(); }
                } else { Toast.makeText(homeContext, "No Permission granted", Toast.LENGTH_SHORT).show(); }
            });
    // READ_CALL_LOG permission
    private ActivityResultLauncher<String> requestReadCallLogPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    if (ContextCompat.checkSelfPermission(homeContext, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(homeContext, "Permission granted", Toast.LENGTH_SHORT).show(); }
                } else { Toast.makeText(homeContext, "No Permission granted", Toast.LENGTH_SHORT).show(); }
            });
    // WRITE_CALL_LOG permission
    private ActivityResultLauncher<String> requestWriteCallLogPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    if (ContextCompat.checkSelfPermission(homeContext, Manifest.permission.WRITE_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(homeContext, "Permission granted", Toast.LENGTH_SHORT).show(); }
                } else { Toast.makeText(homeContext, "No Permission granted", Toast.LENGTH_SHORT).show(); }
            });
    // READ_EXTERNAL_STORAGE permission
    private ActivityResultLauncher<String> requestReadExternalStoragePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    if (ContextCompat.checkSelfPermission(homeContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(homeContext, "Permission granted", Toast.LENGTH_SHORT).show(); }
                } else { Toast.makeText(homeContext, "No Permission granted", Toast.LENGTH_SHORT).show(); }
            });
    // WRITE_EXTERNAL_STORAGE permission
    private ActivityResultLauncher<String> requestWriteExternalStoragePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    if (ContextCompat.checkSelfPermission(homeContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(homeContext, "Permission granted", Toast.LENGTH_SHORT).show(); }
                } else { Toast.makeText(homeContext, "No Permission granted", Toast.LENGTH_SHORT).show(); }
            });

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeContext=context;
        // READ_SMS permission
        if (ActivityCompat.checkSelfPermission(homeContext, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            ReadSmsPermissionGranted = true;
        } else { requestReadSmsPermissionLauncher.launch(Manifest.permission.READ_SMS); }
        // READ_CALL_LOG permission
        if (ActivityCompat.checkSelfPermission(homeContext, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            ReadCallLogsPermissionGranted = true;
        } else { requestReadCallLogPermissionLauncher.launch(Manifest.permission.READ_CALL_LOG); }
        // WRITE_CALL_LOG permission
        if (ActivityCompat.checkSelfPermission(homeContext, Manifest.permission.WRITE_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            WriteCallLogsPermissionGranted = true;
        } else { requestWriteCallLogPermissionLauncher.launch(Manifest.permission.WRITE_CALL_LOG); }
        // READ_EXTERNAL_STORAGE permission
        if (ActivityCompat.checkSelfPermission(homeContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            ReadExternalStoragePermissionGranted = true;
        } else { requestReadExternalStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE); }
        // WRITE_EXTERNAL_STORAGE permission
        if (ActivityCompat.checkSelfPermission(homeContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            WriteExternalStoragePermissionGranted = true;
        } else { requestWriteExternalStoragePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE); }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView textView1 = view.findViewById(R.id.textViewSmsCount);
        if (ReadSmsPermissionGranted) { showCountofMessages(view, textView1); }
        else { textView1.setText("Brak uprawnień odczytu do api SMS"); }

        if (ReadSmsPermissionGranted &&
            ReadCallLogsPermissionGranted &&
            ReadExternalStoragePermissionGranted &&
            WriteExternalStoragePermissionGranted) { backup(view); }

        if (WriteCallLogsPermissionGranted &&
            ReadExternalStoragePermissionGranted &&
            WriteExternalStoragePermissionGranted) { restore(); }

        return view;
    }

    public void showCountofMessages (View view, TextView textView) {
        BackupSms backupSms =  new BackupSms(homeContext);
        int countMessagesInDraft = backupSms.countMessagesInDraft();
        int countMessagesInInbox = backupSms.countMessagesInInbox();
        int countMessagesInOutbox = backupSms.countMessagesInOutbox();
        int countMessagesInSent = backupSms.countMessagesInSent();
        int overallCount = countMessagesInDraft + countMessagesInInbox + countMessagesInOutbox + countMessagesInSent;
        String text = "Posiadasz łącznie " + overallCount + " wiadomości sms";
        textView.setText(text);
    }

    public void backup(View view) {
        BackupSms backupSms =  new BackupSms(homeContext);

        List<List<SmsData>> smsData = backupSms.getAllSms();

        BackupCallLog backupCallLog = new BackupCallLog(homeContext);
        List<CallData> callData = backupCallLog.getAllCalls();
//        String countCallsInCallLog = backupCallLog.countCallLog();
//        TextView textView5 = view.findViewById(R.id.textView5);
//        String txt5 = textView5.getText() + " " + countCallsInCallLog;
//        textView5.setText(txt5);

        FileHandler fh = new FileHandler(homeContext);
        //fh.storeSmsInXml(smsData);
        //fh.storeCallLogInXml(callData);
    }

    public void restore() {
        FileHandler fh = new FileHandler(homeContext);

        // /storage/emulated/0/Documents/20220428151028-sms.xml
        String SmsFileName = "20220428151028-sms.xml";
        File smsXml = new File(
                Environment.getExternalStorageDirectory() + "/Documents/", SmsFileName
        );
        List<List<SmsData>> smsDataLists = fh.restoreSmsFromXml(smsXml);
        RestoreSms restoreSms = new RestoreSms(homeContext);
        if (! homeContext.getPackageName().equals(Telephony.Sms.getDefaultSmsPackage(homeContext))) {
            System.out.println("Na czas przywracania wiadomości ustaw ta aplikacje jako domyslna");
            System.out.println("Następnie uruchom przywracanie wiadomości sms ponownie");
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS);
            startActivity(intent);
        }
        restoreSms.setAllSms(smsDataLists);
        if (homeContext.getPackageName().equals(Telephony.Sms.getDefaultSmsPackage(homeContext)))
        {
            System.out.println("Wiadomości przywrócone, ustaw pierwotną aplikację jako domyślną");
            Intent intent = new Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS);
            startActivity(intent);
        }

        // /storage/emulated/0/Documents/20220428171724-calls.xml
        String CallLogFileName = "20220428171724-calls.xml";
        File callLogXml = new File(
                Environment.getExternalStorageDirectory() + "/Documents/",
                CallLogFileName
        );
        List<CallData> callDataList = fh.restoreCallLogFromXml(callLogXml);
        RestoreCallLog restoreCallLog = new RestoreCallLog(homeContext);
        restoreCallLog.setAllCallLog(callDataList);
        System.out.println("Rejestr połączeń przywrócony");
        //restoreCallLog.deleteAllCallLog();
        for (CallData callData : callDataList) {
            System.out.println(callData.Number);
        }

    }

}
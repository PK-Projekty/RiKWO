package com.pkprojekty.rikwo;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.pkprojekty.rikwo.CallLog.BackupCallLog;
import com.pkprojekty.rikwo.CallLog.RestoreCallLog;
import com.pkprojekty.rikwo.Entities.CallData;
import com.pkprojekty.rikwo.Entities.SmsData;
import com.pkprojekty.rikwo.Sms.BackupSms;
import com.pkprojekty.rikwo.Sms.RestoreSms;
import com.pkprojekty.rikwo.Xml.FileHandler;

import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private Context homeContext;
    private Uri uriTree = Uri.EMPTY;

    private boolean ReadSmsPermissionGranted;
    private boolean ReadCallLogsPermissionGranted;
    private boolean WriteCallLogsPermissionGranted;
    private boolean ReadExternalStoragePermissionGranted;
    private boolean WriteExternalStoragePermissionGranted;

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
        if (Uri.EMPTY.equals(uriTree)) { restoreChoosedDirectoryFromAppPreferences(); }
        // READ_SMS permission
        ReadSmsPermissionGranted = false;
        if (ActivityCompat.checkSelfPermission(homeContext, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            ReadSmsPermissionGranted = true;
        } else { requestReadSmsPermissionLauncher.launch(Manifest.permission.READ_SMS); }
        // READ_CALL_LOG permission
        ReadCallLogsPermissionGranted = false;
        if (ActivityCompat.checkSelfPermission(homeContext, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            ReadCallLogsPermissionGranted = true;
        } else { requestReadCallLogPermissionLauncher.launch(Manifest.permission.READ_CALL_LOG); }
        // WRITE_CALL_LOG permission
        WriteCallLogsPermissionGranted = false;
        if (ActivityCompat.checkSelfPermission(homeContext, Manifest.permission.WRITE_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            WriteCallLogsPermissionGranted = true;
        } else { requestWriteCallLogPermissionLauncher.launch(Manifest.permission.WRITE_CALL_LOG); }
        // READ_EXTERNAL_STORAGE permission
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
        ReadExternalStoragePermissionGranted = false;
            if (ActivityCompat.checkSelfPermission(homeContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                ReadExternalStoragePermissionGranted = true;
            } else { requestReadExternalStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE); }
        } else { ReadExternalStoragePermissionGranted = uriTree != null; }
        // WRITE_EXTERNAL_STORAGE permission
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            WriteExternalStoragePermissionGranted = false;
            if (ActivityCompat.checkSelfPermission(homeContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                WriteExternalStoragePermissionGranted = true;
            } else { requestWriteExternalStoragePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE); }
        } else { WriteExternalStoragePermissionGranted = uriTree != null; }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        restoreChoosedDirectoryFromAppPreferences();

        TextView textViewSmsCount = view.findViewById(R.id.textViewSmsCount);
        if (ReadSmsPermissionGranted) { showCountofMessages(textViewSmsCount); }
        else { textViewSmsCount.setText(getResources().getString(R.string.textViewSmsCount)); }

        TextView textViewCallLogCount = view.findViewById(R.id.textViewCallLogCount);
        if (ReadCallLogsPermissionGranted) { showCountofCallLog(textViewCallLogCount); }
        else { textViewCallLogCount.setText(getResources().getString(R.string.textViewCallLogCount)); }

        Button buttonHomeMakeBackupNow = view.findViewById(R.id.buttonHomeMakeBackupNow);

        SwitchMaterial switchBackupSms = view.findViewById(R.id.switchBackupSms);
        SwitchMaterial switchBackupCallLog = view.findViewById(R.id.switchBackupCallLog);

        restoreSwitchStateFromAppPreferences(switchBackupSms, "switchBackupSms");
        switchBackupSms.setOnCheckedChangeListener((compoundButton, b) -> {
            storeSwitchStateInAppPreferences(switchBackupSms, "switchBackupSms");
            if (b) { buttonHomeMakeBackupNow.setEnabled(true); }
            else { if (!switchBackupCallLog.isChecked()) { buttonHomeMakeBackupNow.setEnabled(false); } }
        });

        restoreSwitchStateFromAppPreferences(switchBackupCallLog,"switchBackupCallLog");
        switchBackupCallLog.setOnCheckedChangeListener((compoundButton, b) -> {
            storeSwitchStateInAppPreferences(switchBackupCallLog, "switchBackupCallLog");
            if (b) { buttonHomeMakeBackupNow.setEnabled(true); }
            else { if (!switchBackupSms.isChecked()) { buttonHomeMakeBackupNow.setEnabled(false); } }
        });

        buttonHomeMakeBackupNow.setEnabled(!Uri.EMPTY.equals(uriTree) || switchBackupSms.isChecked() || switchBackupCallLog.isChecked());

        buttonHomeMakeBackupNow.setOnClickListener(view1 -> {
            if (ReadSmsPermissionGranted &&
                ReadCallLogsPermissionGranted &&
                ReadExternalStoragePermissionGranted &&
                WriteExternalStoragePermissionGranted) { backup(switchBackupSms.isChecked(), switchBackupCallLog.isChecked()); }
        });

//        if (WriteCallLogsPermissionGranted &&
//            ReadExternalStoragePermissionGranted &&
//            WriteExternalStoragePermissionGranted) { restore(); }

        return view;
    }

    private void restoreSwitchStateFromAppPreferences(SwitchMaterial switchMaterial, String key) {
        SharedPreferences preferences = requireActivity().getPreferences(MODE_PRIVATE);
        if (preferences.contains(key)) {
            boolean switchState = preferences.getBoolean(key,false);
            switchMaterial.setChecked(switchState);
        }
    }

    private void storeSwitchStateInAppPreferences(SwitchMaterial switchMaterial, String key) {
        SharedPreferences preferences = requireActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key,switchMaterial.isChecked());
        editor.apply();
    }

    public void showCountofMessages (TextView textView) {
        BackupSms backupSms =  new BackupSms(homeContext);
        int countMessagesInDraft = backupSms.countMessagesInDraft();
        int countMessagesInInbox = backupSms.countMessagesInInbox();
        int countMessagesInOutbox = backupSms.countMessagesInOutbox();
        int countMessagesInSent = backupSms.countMessagesInSent();
        int overallCount = countMessagesInDraft + countMessagesInInbox + countMessagesInOutbox + countMessagesInSent;
        String pluralMessage = (overallCount >= 2) ? "wiadomości" : "wiadomość";
        String text = "Posiadasz łącznie " + overallCount + " " + pluralMessage + " sms";
        textView.setText(text);
    }
    public void showCountofCallLog (TextView textView) {
        BackupCallLog backupCallLog = new BackupCallLog(homeContext);
        int countCallLog = backupCallLog.countCallLog();
        String pluralEntry = (countCallLog >= 2) ? "wpisy" : "wpis";
        String text = "Posiadasz łącznie " + countCallLog + " " + pluralEntry + " w rejestrze połączeń";
        textView.setText(text);
    }

    private void restoreChoosedDirectoryFromAppPreferences() {
        SharedPreferences preferences = requireActivity().getPreferences(MODE_PRIVATE);
        if (preferences.contains("uriTree")) {
            uriTree = Uri.parse(preferences.getString("uriTree",""));
        }
        System.out.println("Wybrany katalog: "+uriTree);
    }

    public void backup(boolean smsSwitchState, boolean callLogSwitchState) {
        FileHandler fh = new FileHandler(homeContext);
        System.out.println("Tworzenie kopii zapasowej w wybranym katalogu: "+uriTree);
        ZoneId z = ZoneId.of("Europe/Warsaw");
        //ZonedDateTime zdt = ZonedDateTime.now(z);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        if (smsSwitchState) {
            ZonedDateTime zdt = ZonedDateTime.now(z);
            String smsFilename = zdt.format(formatter) + "-sms.xml";
            DocumentFile smsFile = Objects.requireNonNull(DocumentFile.fromTreeUri(homeContext, uriTree)).createFile("text/xml", smsFilename);
            BackupSms backupSms = new BackupSms(homeContext);
            List<List<SmsData>> smsData = backupSms.getAllSms();
            fh.storeSmsInXml(smsData, smsFile);
        }
        if (callLogSwitchState) {
            ZonedDateTime zdt = ZonedDateTime.now(z);
            String callsFilename = zdt.format(formatter) + "-calls.xml";
            DocumentFile callsFile = Objects.requireNonNull(DocumentFile.fromTreeUri(homeContext, uriTree)).createFile("text/xml", callsFilename);
            BackupCallLog backupCallLog = new BackupCallLog(homeContext);
            List<CallData> callData = backupCallLog.getAllCalls();
            fh.storeCallLogInXml(callData, callsFile);
        }
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
        //restoreSms.setAllSms(smsDataLists);
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
        //restoreCallLog.setAllCallLog(callDataList);
        System.out.println("Rejestr połączeń przywrócony");
        //restoreCallLog.deleteAllCallLog();
        for (CallData callData : callDataList) {
            System.out.println(callData.Number);
        }

    }

}
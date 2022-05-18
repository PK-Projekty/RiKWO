package com.pkprojekty.rikwo.UI;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.pkprojekty.rikwo.CallLog.BackupCallLog;
import com.pkprojekty.rikwo.Entities.CallData;
import com.pkprojekty.rikwo.Entities.SmsData;
import com.pkprojekty.rikwo.Permissions.Permissions;
import com.pkprojekty.rikwo.R;
import com.pkprojekty.rikwo.Sms.BackupSms;
import com.pkprojekty.rikwo.Xml.FileHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class BackupFragment extends Fragment {
    Permissions permissions = new Permissions(this);

    private Context backupContext;
    private Uri uriTree = Uri.EMPTY;

    MutableLiveData<Boolean> ReadSmsPermissionGranted = new MutableLiveData<>();
    MutableLiveData<Boolean> ReadCallLogsPermissionGranted = new MutableLiveData<>();
    MutableLiveData<Boolean> ReadExternalStoragePermissionGranted = new MutableLiveData<>();
    MutableLiveData<Boolean> WriteExternalStoragePermissionGranted = new MutableLiveData<>();

    public BackupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        backupContext=context;
        System.out.println("BackupFragment onAttach HIT");

        // ask for all permissions needed by app
        permissions.askForAllPermissions();

        // read from app preferences location uri choosed by user
        restoreChoosedDirectoryFromAppPreferences();

        //check app permissions
        checkPermissions();
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("HomeFragment onResume HIT");

        // read from app preferences location uri choosed by user
        restoreChoosedDirectoryFromAppPreferences();

        //check app permissions
        checkPermissions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_backup, container, false);

        // read from app preferences location uri choosed by user
        restoreChoosedDirectoryFromAppPreferences();

        //check app permissions
        checkPermissions();

        // section: make backup now
        makeBackup(view);

        return view;
    }

    public void makeBackup(View view) {
        // show to user current choosed backup location and allow user to choose other backup location
        TextView textViewBackupLocationAbout = view.findViewById(R.id.textViewBackupLocationAbout);
        Button buttonBackupChooseLocation = view.findViewById(R.id.buttonBackupChooseLocation);
        if (uriTree != Uri.EMPTY) {
            if (ReadExternalStoragePermissionGranted.getValue() == null) { ReadExternalStoragePermissionGranted.setValue(false); }
            if (WriteExternalStoragePermissionGranted.getValue() == null) { WriteExternalStoragePermissionGranted.setValue(false); }
            if (!ReadExternalStoragePermissionGranted.getValue() && !WriteExternalStoragePermissionGranted.getValue()) {
                String[] topDir = uriTree.toString().split("%3A");
                String dir = topDir[topDir.length-1].replace("%2F","/");
                String backupLocationAbout = "Lokalizacja: "+dir;
                backupLocationAbout += "\nBrak dostępu do pamięci masowej!\n\nProszę zweryfikować uprawnienia dostępu do pamięci masowej, lub wskazać inną lokalizację dla kopii zapasowych.";
                textViewBackupLocationAbout.setText(backupLocationAbout);
                String btnTxt = "Wskaż inny katalog";
                buttonBackupChooseLocation.setText(btnTxt);
                buttonBackupChooseLocation.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_backupFragment_to_localizationFragment));
            }
        } else {
            ReadExternalStoragePermissionGranted.setValue(false);
            WriteExternalStoragePermissionGranted.setValue(false);
            String backupLocationAbout = textViewBackupLocationAbout.getText() + "\n\nWybierz katalog, w którym chcesz przechowywać kopie zapasowe sms i rejestru połączeń.";
            textViewBackupLocationAbout.setText(backupLocationAbout);
        }
        if (ReadExternalStoragePermissionGranted.getValue() == null) { ReadExternalStoragePermissionGranted.setValue(false); }
        if (WriteExternalStoragePermissionGranted.getValue() == null) { WriteExternalStoragePermissionGranted.setValue(false); }
        if (ReadExternalStoragePermissionGranted.getValue() && WriteExternalStoragePermissionGranted.getValue()) {
            String[] topDir = uriTree.toString().split("%3A");
            String dir = topDir[topDir.length-1].replace("%2F","/");
            String backupLocationAbout = "Lokalizacja: "+dir;
            textViewBackupLocationAbout.setText(backupLocationAbout);
            String btnTxt = "Wskaż inny katalog";
            buttonBackupChooseLocation.setText(btnTxt);
            buttonBackupChooseLocation.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_backupFragment_to_localizationFragment));
        }

        // show to user how many sms he has
        TextView textViewBackupSmsAbout = view.findViewById(R.id.textViewBackupSmsAbout);
        if (ReadSmsPermissionGranted.getValue() == null) { ReadSmsPermissionGranted.setValue(false); }
        if (ReadSmsPermissionGranted.getValue()) { showCountofMessages(textViewBackupSmsAbout); }
        else { textViewBackupSmsAbout.setText(getResources().getString(R.string.textViewBackupSmsAbout)); }

        // show to user how many entries in the call log he has
        TextView textViewBackupCallLogAbout = view.findViewById(R.id.textViewBackupCallLogAbout);
        if (ReadCallLogsPermissionGranted.getValue() == null) { ReadCallLogsPermissionGranted.setValue(false); }
        if (ReadCallLogsPermissionGranted.getValue()) { showCountofCallLog(textViewBackupCallLogAbout); }
        else { textViewBackupCallLogAbout.setText(getResources().getString(R.string.textViewBackupCallLogAbout)); }

        // allow user to include sms or / and call log in backup
        Button buttonBackupDoItNow = view.findViewById(R.id.buttonBackupDoItNow);
        SwitchMaterial switchBackupSmsUse = view.findViewById(R.id.switchBackupSmsUse);
        SwitchMaterial switchBackupCallLogUse = view.findViewById(R.id.switchBackupCallLogUse);
        // restore state of sms switch
        restoreSwitchStateFromAppPreferences("switchBackupSmsUse",switchBackupSmsUse);
        //restore state of call log switch
        restoreSwitchStateFromAppPreferences("switchBackupCallLogUse",switchBackupCallLogUse);
        switchBackupSmsUse.setOnCheckedChangeListener((compoundButton, b) -> {
            if (ReadSmsPermissionGranted.getValue() && !Uri.EMPTY.equals(uriTree)) {
                storeSwitchStateInAppPreferences("switchBackupSmsUse", switchBackupSmsUse);
            }
            if (b) { buttonBackupDoItNow.setEnabled(true); }
            else { if (!switchBackupCallLogUse.isChecked()) { buttonBackupDoItNow.setEnabled(false); } }
        });
        switchBackupCallLogUse.setOnCheckedChangeListener((compoundButton, b) -> {
            if (ReadCallLogsPermissionGranted.getValue() && !Uri.EMPTY.equals(uriTree)) {
                storeSwitchStateInAppPreferences("switchBackupCallLogUse", switchBackupCallLogUse);
            }
            if (b) { buttonBackupDoItNow.setEnabled(true); }
            else { if (!switchBackupSmsUse.isChecked()) { buttonBackupDoItNow.setEnabled(false); } }
        });
        // updating state of sms switch which depends on read sms permission
        ReadSmsPermissionGranted.observe(requireActivity(), aBoolean -> {
            if (ReadSmsPermissionGranted.getValue() && !Uri.EMPTY.equals(uriTree)) {
                restoreSwitchStateFromAppPreferences("switchBackupSmsUse",switchBackupSmsUse);
                switchBackupSmsUse.setEnabled(true);
            } else {
                switchBackupSmsUse.setEnabled(false);
                switchBackupSmsUse.setChecked(false);
            }
        });
        // updating state of call log switch which depends on read call log permission
        ReadCallLogsPermissionGranted.observe(requireActivity(), aBoolean -> {
            if (ReadCallLogsPermissionGranted.getValue() && !Uri.EMPTY.equals(uriTree)) {
                restoreSwitchStateFromAppPreferences("switchBackupCallLogUse",switchBackupCallLogUse);
                switchBackupCallLogUse.setEnabled(true);
            } else {
                switchBackupCallLogUse.setEnabled(false);
                switchBackupCallLogUse.setChecked(false);
            }
        });
        // allow user to backup what he included
        if (switchBackupSmsUse.isChecked() && !Uri.EMPTY.equals(uriTree)) {
            buttonBackupDoItNow.setEnabled(true);
        } else buttonBackupDoItNow.setEnabled(switchBackupCallLogUse.isChecked() && !Uri.EMPTY.equals(uriTree));
        buttonBackupDoItNow.setOnClickListener(view1 -> {
            if (ReadExternalStoragePermissionGranted.getValue() == null) { ReadExternalStoragePermissionGranted.setValue(false); }
            if (WriteExternalStoragePermissionGranted.getValue() == null) { WriteExternalStoragePermissionGranted.setValue(false); }
            if ((ReadSmsPermissionGranted.getValue() || ReadCallLogsPermissionGranted.getValue()) &&
                    ReadExternalStoragePermissionGranted.getValue() &&
                    WriteExternalStoragePermissionGranted.getValue()) { backup(switchBackupSmsUse.isChecked(), switchBackupCallLogUse.isChecked()); }
        });
    }

    public void backup(boolean smsSwitchState, boolean callLogSwitchState) {
        FileHandler fh = new FileHandler(backupContext);
        System.out.println("Tworzenie kopii zapasowej w wybranym katalogu: "+uriTree);
        ZoneId z = ZoneId.of("Europe/Warsaw");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        if (smsSwitchState) {
            ZonedDateTime zdt = ZonedDateTime.now(z);
            String smsFilename = zdt.format(formatter) + "-sms.xml";
            DocumentFile smsFile = Objects.requireNonNull(DocumentFile.fromTreeUri(backupContext, uriTree)).createFile("text/xml", smsFilename);
            BackupSms backupSms = new BackupSms(backupContext);
            List<List<SmsData>> smsData = backupSms.getAllSms();
            fh.storeSmsInXml(smsData, smsFile);
        }
        if (callLogSwitchState) {
            ZonedDateTime zdt = ZonedDateTime.now(z);
            String callsFilename = zdt.format(formatter) + "-calls.xml";
            DocumentFile callsFile = Objects.requireNonNull(DocumentFile.fromTreeUri(backupContext, uriTree)).createFile("text/xml", callsFilename);
            BackupCallLog backupCallLog = new BackupCallLog(backupContext);
            List<CallData> callData = backupCallLog.getAllCalls();
            fh.storeCallLogInXml(callData, callsFile);
        }
    }

    private void restoreSwitchStateFromAppPreferences(String key, SwitchMaterial switchMaterial) {
        SharedPreferences preferences = requireActivity().getPreferences(MODE_PRIVATE);
        if (preferences.contains(key)) {
            boolean switchState = preferences.getBoolean(key,false);
            switchMaterial.setChecked(switchState);
        }
    }

    private void storeSwitchStateInAppPreferences(String key, SwitchMaterial switchMaterial) {
        SharedPreferences preferences = requireActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key,switchMaterial.isChecked());
        editor.apply();
    }

    public void showCountofMessages (TextView textView) {
        BackupSms backupSms =  new BackupSms(backupContext);
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
        BackupCallLog backupCallLog = new BackupCallLog(backupContext);
        int countCallLog = backupCallLog.countCallLog();
        String pluralEntry = (countCallLog >= 2) ? "wpisy" : "wpis";
        String text = "Posiadasz łącznie " + countCallLog + " " + pluralEntry + " w rejestrze połączeń";
        textView.setText(text);
    }

    public void checkPermissions() {
        // check for all permissions needed by app
        ReadSmsPermissionGranted.setValue(permissions.hasReadSmsPermission());
        ReadCallLogsPermissionGranted.setValue(permissions.hasReadCallLogsPermission());
        if (uriTree != Uri.EMPTY) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                ReadExternalStoragePermissionGranted.setValue(permissions.hasReadExternalStoragePermission() && Objects.requireNonNull(DocumentFile.fromTreeUri(backupContext, uriTree)).canRead());
            } else {
                ReadExternalStoragePermissionGranted.setValue(Objects.requireNonNull(DocumentFile.fromTreeUri(backupContext, uriTree)).canRead());
            }
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                WriteExternalStoragePermissionGranted.setValue(permissions.hasWriteExternalStoragePermission() && Objects.requireNonNull(DocumentFile.fromTreeUri(backupContext, uriTree)).canWrite());
            } else {
                WriteExternalStoragePermissionGranted.setValue(Objects.requireNonNull(DocumentFile.fromTreeUri(backupContext, uriTree)).canWrite());
            }
        } else {
            ReadExternalStoragePermissionGranted.setValue(false);
            WriteExternalStoragePermissionGranted.setValue(false);
        }
    }

    private void restoreChoosedDirectoryFromAppPreferences() {
        SharedPreferences preferences = requireActivity().getPreferences(MODE_PRIVATE);
        if (preferences.contains("uriTree")) {
            uriTree = Uri.parse(preferences.getString("uriTree",""));
        }
        System.out.println("Wybrany katalog: "+uriTree);
    }

}
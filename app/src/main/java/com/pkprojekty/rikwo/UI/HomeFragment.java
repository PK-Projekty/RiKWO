package com.pkprojekty.rikwo.UI;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
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

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    Permissions permissions = new Permissions(this);

    private Context homeContext;
    private Uri uriTree = Uri.EMPTY;
    MutableLiveData<Uri> choosedSmsBackupFile = new MutableLiveData<>();
    MutableLiveData<Uri> choosedCallLogBackupFile = new MutableLiveData<>();

    MutableLiveData<Boolean> ReadSmsPermissionGranted = new MutableLiveData<>();
    MutableLiveData<Boolean> ReadCallLogsPermissionGranted = new MutableLiveData<>();
    MutableLiveData<Boolean> WriteCallLogsPermissionGranted = new MutableLiveData<>();
    MutableLiveData<Boolean> ReadExternalStoragePermissionGranted = new MutableLiveData<>();
    MutableLiveData<Boolean> WriteExternalStoragePermissionGranted = new MutableLiveData<>();
    MutableLiveData<Boolean> IsDefaultSmsApp = new MutableLiveData<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        homeContext=context;
        System.out.println("HomeFragment onAttach HIT");

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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        System.out.println("HomeFragment onCreateView HIT");

        // read from app preferences location uri choosed by user
        restoreChoosedDirectoryFromAppPreferences();

        //check app permissions
        checkPermissions();

        // section: About app permissions
        aboutPermissions(view);

        // section: About last backup
        aboutLastBackup(view);

        // section: About sms, and call log -> backup now
        backupNow(view);

        return view;
    }

    public void checkPermissions() {
        // check if app is set as default app for sms
        // it should be true, when user wants to restore sms from backup
        IsDefaultSmsApp.setValue(permissions.isDefaultSmsApp());

        // check for all permissions needed by app
        ReadSmsPermissionGranted.setValue(permissions.hasReadSmsPermission());
        ReadCallLogsPermissionGranted.setValue(permissions.hasReadCallLogsPermission());
        WriteCallLogsPermissionGranted.setValue(permissions.hasWriteCallLogsPermission());

        if (uriTree != Uri.EMPTY) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                ReadExternalStoragePermissionGranted.setValue(permissions.hasReadExternalStoragePermission() && Objects.requireNonNull(DocumentFile.fromTreeUri(homeContext, uriTree)).canRead());
            } else {
                ReadExternalStoragePermissionGranted.setValue(Objects.requireNonNull(DocumentFile.fromTreeUri(homeContext, uriTree)).canRead());
            }
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                WriteExternalStoragePermissionGranted.setValue(permissions.hasWriteExternalStoragePermission() && Objects.requireNonNull(DocumentFile.fromTreeUri(homeContext, uriTree)).canWrite());
            } else {
                WriteExternalStoragePermissionGranted.setValue(Objects.requireNonNull(DocumentFile.fromTreeUri(homeContext, uriTree)).canWrite());
            }
        } else {
            ReadExternalStoragePermissionGranted.setValue(false);
            WriteExternalStoragePermissionGranted.setValue(false);
        }

    }

    public void aboutPermissions(View view) {
        // set gui element for default sms app
        IsDefaultSmsApp.observe(requireActivity(), aBoolean -> {
            TextView textViewHomePermissionsAboutDefaultSmsApp = view.findViewById(R.id.textViewHomePermissionsAboutDefaultSmsApp);
            if (IsDefaultSmsApp.getValue() == null) { IsDefaultSmsApp.setValue(false); }
            String homePermissionsAboutDefaultSmsAppText = getResources().getString(R.string.textViewHomePermissionsAboutDefaultSmsApp)+": "+((IsDefaultSmsApp.getValue())?"TAK":"NIE");
            textViewHomePermissionsAboutDefaultSmsApp.setText(homePermissionsAboutDefaultSmsAppText);
            System.out.println("DEFAULT SMS APP: " + IsDefaultSmsApp.getValue());
        });

        // set gui element for read sms permission
        ReadSmsPermissionGranted.observe(requireActivity(), aBoolean -> {
            TextView textViewHomePermissionsAboutSmsAccess = view.findViewById(R.id.textViewHomePermissionsAboutSmsAccess);
            if (ReadSmsPermissionGranted.getValue() == null) { ReadSmsPermissionGranted.setValue(false); }
            String homePermissionsAboutSmsAccessText = getResources().getString(R.string.textViewHomePermissionsAboutSmsAccess)+": "+((ReadSmsPermissionGranted.getValue())?"TAK":"NIE");
            textViewHomePermissionsAboutSmsAccess.setText(homePermissionsAboutSmsAccessText);
            System.out.println("READ SMS: " + ReadSmsPermissionGranted.getValue());
        });

        // set gui element for read call log permission
        ReadCallLogsPermissionGranted.observe(requireActivity(), aBoolean -> {
            TextView textViewHomePermissionsAboutCallLogAccess = view.findViewById(R.id.textViewHomePermissionsAboutCallLogAccess);
            if (ReadCallLogsPermissionGranted.getValue() == null) { ReadCallLogsPermissionGranted.setValue(false); }
            if (WriteCallLogsPermissionGranted.getValue() == null) { WriteCallLogsPermissionGranted.setValue(false); }
            String homePermissionsAboutCallLogAccessText = getResources().getString(R.string.textViewHomePermissionsAboutCallLogAccess)+": "+((ReadCallLogsPermissionGranted.getValue()&&WriteCallLogsPermissionGranted.getValue()) ? "TAK" : "NIE");
            textViewHomePermissionsAboutCallLogAccess.setText(homePermissionsAboutCallLogAccessText);
            System.out.println("READ CALL LOG: " + ReadCallLogsPermissionGranted.getValue());
        });

        // set gui element for write call log permission
        WriteCallLogsPermissionGranted.observe(requireActivity(), aBoolean -> {
            TextView textViewHomePermissionsAboutCallLogAccess = view.findViewById(R.id.textViewHomePermissionsAboutCallLogAccess);
            if (ReadCallLogsPermissionGranted.getValue() == null) { ReadCallLogsPermissionGranted.setValue(false); }
            if (WriteCallLogsPermissionGranted.getValue() == null) { WriteCallLogsPermissionGranted.setValue(false); }
            String homePermissionsAboutCallLogAccessText = getResources().getString(R.string.textViewHomePermissionsAboutCallLogAccess)+": "+((ReadCallLogsPermissionGranted.getValue()&&WriteCallLogsPermissionGranted.getValue()) ? "TAK" : "NIE");
            textViewHomePermissionsAboutCallLogAccess.setText(homePermissionsAboutCallLogAccessText);
            System.out.println("WRITE CALL LOG: " + WriteCallLogsPermissionGranted.getValue());
        });

        // set gui element for read external storage permission
        ReadExternalStoragePermissionGranted.observe(requireActivity(), aBoolean -> {
            TextView textViewHomePermissionsAboutBackupLocation = view.findViewById(R.id.textViewHomePermissionsAboutBackupLocation);
            if (ReadExternalStoragePermissionGranted.getValue() == null) { ReadExternalStoragePermissionGranted.setValue(false); }
            if (WriteExternalStoragePermissionGranted.getValue() == null) { WriteExternalStoragePermissionGranted.setValue(false); }
            String homePermissionsAboutBackupLocationText = getResources().getString(R.string.textViewHomePermissionsAboutBackupLocation)+": "+((ReadExternalStoragePermissionGranted.getValue() && WriteExternalStoragePermissionGranted.getValue()) ? "TAK" : "NIE");
            textViewHomePermissionsAboutBackupLocation.setText(homePermissionsAboutBackupLocationText);
        });

        // set gui element for write external storage permission
        WriteExternalStoragePermissionGranted.observe(requireActivity(), aBoolean -> {
            TextView textViewHomePermissionsAboutBackupLocation = view.findViewById(R.id.textViewHomePermissionsAboutBackupLocation);
            if (ReadExternalStoragePermissionGranted.getValue() == null) { ReadExternalStoragePermissionGranted.setValue(false); }
            if (WriteExternalStoragePermissionGranted.getValue() == null) { WriteExternalStoragePermissionGranted.setValue(false); }
            String homePermissionsAboutBackupLocationText = getResources().getString(R.string.textViewHomePermissionsAboutBackupLocation)+": "+((ReadExternalStoragePermissionGranted.getValue() && WriteExternalStoragePermissionGranted.getValue()) ? "TAK" : "NIE");
            textViewHomePermissionsAboutBackupLocation.setText(homePermissionsAboutBackupLocationText);
        });

        // set gui element for navigate user to permissions detail screen
        Button buttonHomePermissionsDetails = view.findViewById(R.id.buttonHomePermissionsDetails);
        buttonHomePermissionsDetails.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_permissionsFragment));
    }
    public void aboutLastBackup(View view) {
        // initialize all gui elements needed by this section
        TextView textViewHomeLastBackupLocalizationAbout = view.findViewById(R.id.textViewHomeLastBackupLocalizationAbout);
        TextView textViewHomeLastSmsBackupAbout = view.findViewById(R.id.textViewHomeLastSmsBackupAbout);
        TextView textViewHomeLastCallLogBackupAbout = view.findViewById(R.id.textViewHomeLastCallLogBackupAbout);
        Button buttonHomeRestoreBackupNow = view.findViewById(R.id.buttonHomeRestoreBackupNow);

        // check if location has been choosed, and app has permissions to use it
        if (uriTree != Uri.EMPTY) {
            if (ReadExternalStoragePermissionGranted.getValue() == null) { ReadExternalStoragePermissionGranted.setValue(false); }
            if (WriteExternalStoragePermissionGranted.getValue() == null) { WriteExternalStoragePermissionGranted.setValue(false); }
            if (!ReadExternalStoragePermissionGranted.getValue() && !WriteExternalStoragePermissionGranted.getValue()) {
                String[] topDir = uriTree.toString().split("%3A");
                String dir = topDir[topDir.length-1].replace("%2F","/");
                String lastBackupLocalizationAbout = "Lokalizacja: "+dir;
                lastBackupLocalizationAbout += "\nBrak dostępu do pamięci masowej!\n\nProszę zweryfikować uprawnienia dostępu do pamięci masowej, lub wskazać inną lokalizację dla kopii zapasowych.";
                textViewHomeLastSmsBackupAbout.setVisibility(View.GONE);
                textViewHomeLastCallLogBackupAbout.setVisibility(View.GONE);
                buttonHomeRestoreBackupNow.setVisibility(View.GONE);
                textViewHomeLastBackupLocalizationAbout.setText(lastBackupLocalizationAbout);
            }
        } else {
            // even if app has needed storage permissions, when choosed location is null
            // we are set those permissions to false, and set gui elements for asking user to choose location again
            ReadExternalStoragePermissionGranted.setValue(false);
            WriteExternalStoragePermissionGranted.setValue(false);
            String lastBackupLocalizationAbout = textViewHomeLastBackupLocalizationAbout.getText()+"\n\nWybierz katalog, w którym chcesz przechowywać kopie zapasowe sms i rejestru połączeń.";
            textViewHomeLastBackupLocalizationAbout.setText(lastBackupLocalizationAbout);
            textViewHomeLastSmsBackupAbout.setVisibility(View.GONE);
            textViewHomeLastCallLogBackupAbout.setVisibility(View.GONE);
            String btnTxt = "Wybierz katalog";
            buttonHomeRestoreBackupNow.setText(btnTxt);
            buttonHomeRestoreBackupNow.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_localizationFragment));
        }
        // if app has needed storage permissions and based from above choosed location is not null
        // we are set gui element for display information about last backup in choosed location
        if (ReadExternalStoragePermissionGranted.getValue() == null) { ReadExternalStoragePermissionGranted.setValue(false); }
        if (WriteExternalStoragePermissionGranted.getValue() == null) { WriteExternalStoragePermissionGranted.setValue(false); }
        if (ReadExternalStoragePermissionGranted.getValue() && WriteExternalStoragePermissionGranted.getValue()) {
            textViewHomeLastSmsBackupAbout.setVisibility(View.VISIBLE);
            textViewHomeLastCallLogBackupAbout.setVisibility(View.VISIBLE);
            buttonHomeRestoreBackupNow.setVisibility(View.VISIBLE);
            List<Uri> fileUriList = listDirectory(uriTree);
            lastBackupInChoosedDirectory(fileUriList);
            String[] topDir = uriTree.toString().split("%3A");
            String dir = topDir[topDir.length-1].replace("%2F","/");
            String lastBackupLocalizationAbout = "Lokalizacja: "+dir;
            textViewHomeLastBackupLocalizationAbout.setText(lastBackupLocalizationAbout);
            // set gui element for displaying information about last sms backup in choosed location
            if (choosedSmsBackupFile.getValue() == Uri.EMPTY) {
                String text = "We wskazanej lokalizacji nie ma kopii zapasowych wiadomości sms.";
                textViewHomeLastSmsBackupAbout.setText(text);
            } else {
                // assumption: in choosed location there is at least one sms backup file
                choosedSmsBackupFile.observe(requireActivity(), uri -> {
                    String backupFileName = Objects.requireNonNull(DocumentFile.fromSingleUri(homeContext, Objects.requireNonNull(choosedSmsBackupFile.getValue()))).getName();
                    long epoch = Objects.requireNonNull(DocumentFile.fromSingleUri(homeContext, choosedSmsBackupFile.getValue())).lastModified();
                    String backupLastModified = convertEpochIntoHumanReadableDatetime(epoch);
                    FileHandler fh = new FileHandler(homeContext);
                    int entries = fh.countEntriesInSmsXml(homeContext, choosedSmsBackupFile.getValue());
                    String pluralEntry = (entries >= 2 || entries == 0) ? "smsów" : "sms";
                    String text =
                            "Kopia zapasowa: " + backupFileName +
                                    "\nData ostatniej modyfikacji: " + backupLastModified +
                                    "\nWskazana kopia składa się z " + entries + " " + pluralEntry;
                    textViewHomeLastSmsBackupAbout.setText(text);
                });
            }
            // set gui element for displaying information about last call log backup in choosed location
            if (choosedCallLogBackupFile.getValue() == Uri.EMPTY) {
                String text = "We wskazanej lokalizacji nie ma kopii zapasowych rejestru połączeń.";
                textViewHomeLastCallLogBackupAbout.setText(text);
            } else {
                // assumption: in choosed location there is at least one call log backup file
                choosedCallLogBackupFile.observe(requireActivity(), uri -> {
                    String backupFileName = Objects.requireNonNull(DocumentFile.fromSingleUri(homeContext, Objects.requireNonNull(choosedCallLogBackupFile.getValue()))).getName();
                    long epoch = Objects.requireNonNull(DocumentFile.fromSingleUri(homeContext, choosedCallLogBackupFile.getValue())).lastModified();
                    String backupLastModified = convertEpochIntoHumanReadableDatetime(epoch);
                    FileHandler fh = new FileHandler(homeContext);
                    int entries = fh.countEntriesInCallLogXml(homeContext, choosedCallLogBackupFile.getValue());
                    String pluralEntry = (entries >= 2 || entries == 0) ? "połączeń" : "połączenia";
                    String text =
                            "Kopia zapasowa: " + backupFileName +
                                    "\nData ostatniej modyfikacji: " + backupLastModified +
                                    "\nWskazana kopia składa się z " + entries + " " + pluralEntry;
                    textViewHomeLastCallLogBackupAbout.setText(text);
                });
            }

            // set gui elements for asking user to choose location again, because in current choosed location there are no backup files
            if (choosedSmsBackupFile.getValue() == Uri.EMPTY && choosedCallLogBackupFile.getValue() == Uri.EMPTY) {
                String btnTxt = "Wybierz inny katalog";
                buttonHomeRestoreBackupNow.setText(btnTxt);
                buttonHomeRestoreBackupNow.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_localizationFragment));
            } else {
                // set gui element for navigate user to restore backup screen
                buttonHomeRestoreBackupNow.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_restoreFragment));
            }
        }
    }

    public void backupNow(View view) {
        // initialize all gui elements needed by this section
        TextView textViewSmsCount = view.findViewById(R.id.textViewSmsCount);
        if (ReadSmsPermissionGranted.getValue() == null) { ReadSmsPermissionGranted.setValue(false); }
        if (ReadSmsPermissionGranted.getValue()) { showCountofMessages(textViewSmsCount); }
        else { textViewSmsCount.setText(getResources().getString(R.string.textViewSmsCount)); }
        TextView textViewCallLogCount = view.findViewById(R.id.textViewCallLogCount);
        if (ReadCallLogsPermissionGranted.getValue() == null) { ReadCallLogsPermissionGranted.setValue(false); }
        if (ReadCallLogsPermissionGranted.getValue()) { showCountofCallLog(textViewCallLogCount); }
        else { textViewCallLogCount.setText(getResources().getString(R.string.textViewCallLogCount)); }
        Button buttonHomeMakeBackupNow = view.findViewById(R.id.buttonHomeMakeBackupNow);
        SwitchMaterial switchBackupSms = view.findViewById(R.id.switchBackupSms);
        SwitchMaterial switchBackupCallLog = view.findViewById(R.id.switchBackupCallLog);
        // restore state of sms switch
        restoreSwitchStateFromAppPreferences("switchBackupSms",switchBackupSms);
        //restore state of call log switch
        restoreSwitchStateFromAppPreferences("switchBackupCallLog",switchBackupCallLog);
        switchBackupSms.setOnCheckedChangeListener((compoundButton, b) -> {
            storeSwitchStateInAppPreferences( "switchBackupSms",switchBackupSms);
            if (b) { buttonHomeMakeBackupNow.setEnabled(true); }
            else { if (!switchBackupCallLog.isChecked()) { buttonHomeMakeBackupNow.setEnabled(false); } }
        });
        switchBackupCallLog.setOnCheckedChangeListener((compoundButton, b) -> {
            storeSwitchStateInAppPreferences("switchBackupCallLog",switchBackupCallLog);
            if (b) { buttonHomeMakeBackupNow.setEnabled(true); }
            else { if (!switchBackupSms.isChecked()) { buttonHomeMakeBackupNow.setEnabled(false); } }
        });
        // updating state of sms switch which depends on read sms permission
        ReadSmsPermissionGranted.observe(requireActivity(), aBoolean -> switchBackupSms.setEnabled(ReadSmsPermissionGranted.getValue() && !Uri.EMPTY.equals(choosedSmsBackupFile.getValue())));
        // updating state of call log switch which depends on read call log permission
        ReadCallLogsPermissionGranted.observe(requireActivity(), aBoolean -> switchBackupCallLog.setEnabled(ReadCallLogsPermissionGranted.getValue() && !Uri.EMPTY.equals(choosedCallLogBackupFile.getValue())));
        // make backup now button available, when:
        // - sms switch is on
        // - choosed sms backup is available -> choosedSmsBackupFile
        // - call log switch is on
        // - choosed call log backup is available -> choosedCallLogBackupFile
        System.out.println("loc: "+!Uri.EMPTY.equals(choosedSmsBackupFile.getValue()));
        System.out.println("loc: "+!Uri.EMPTY.equals(choosedCallLogBackupFile.getValue()));
        if (switchBackupSms.isChecked() && !Uri.EMPTY.equals(choosedSmsBackupFile.getValue())) {
            buttonHomeMakeBackupNow.setEnabled(true);
        } else buttonHomeMakeBackupNow.setEnabled(switchBackupCallLog.isChecked() && !Uri.EMPTY.equals(choosedCallLogBackupFile.getValue()));
        buttonHomeMakeBackupNow.setOnClickListener(view1 -> {
            if (ReadExternalStoragePermissionGranted.getValue() == null) { ReadExternalStoragePermissionGranted.setValue(false); }
            if (WriteExternalStoragePermissionGranted.getValue() == null) { WriteExternalStoragePermissionGranted.setValue(false); }
            if (ReadSmsPermissionGranted.getValue() &&
                    ReadCallLogsPermissionGranted.getValue() &&
                    ReadExternalStoragePermissionGranted.getValue() &&
                    WriteExternalStoragePermissionGranted.getValue()) { backup(switchBackupSms.isChecked(), switchBackupCallLog.isChecked()); }
        });

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
        List<Uri> fileUriList = listDirectory(uriTree);
        lastBackupInChoosedDirectory(fileUriList);
    }

    public List<Uri> listDirectory(Uri uriTree) {
        Uri uriFolder = DocumentsContract.buildChildDocumentsUriUsingTree(uriTree, DocumentsContract.getTreeDocumentId(uriTree));
        List<Uri> uriList = new ArrayList<>();
        try (Cursor cursor = requireActivity().getContentResolver().query(uriFolder,
                new String[]{DocumentsContract.Document.COLUMN_DOCUMENT_ID},
                null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Uri uriFile = DocumentsContract.buildDocumentUriUsingTree(uriTree, cursor.getString(0));
                    if (Objects.requireNonNull(DocumentFile.fromSingleUri(homeContext, uriFile)).isFile()) {
                        uriList.add(uriFile);
                    } else if (Objects.requireNonNull(DocumentFile.fromSingleUri(homeContext, uriFile)).isDirectory()) {
                        System.out.println("To jest katalog! " + uriFile);
                    }
                    System.out.println(uriFile);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            // TODO: handle error
        }
        return uriList;
    }

    private void lastBackupInChoosedDirectory(List<Uri> listOfFilesUri) {
        String fileName;
        String backupType;
        long lastModified;
        long tmpSmsLastModified = 0;
        long tmpCallLogLastModified = 0;
        Uri latestSmsBackupFile = Uri.EMPTY;
        Uri latestCallLogBackupFile = Uri.EMPTY;
        for (Uri uriFile : listOfFilesUri) {
            lastModified = Objects.requireNonNull(DocumentFile.fromSingleUri(homeContext, uriFile)).lastModified();
            fileName = Objects.requireNonNull(DocumentFile.fromSingleUri(homeContext, uriFile)).getName();
            backupType = Objects.requireNonNull(fileName).split("-")[1];
            if (backupType.equals("sms.xml")) {
                if (Uri.EMPTY.equals(latestSmsBackupFile)) {
                    tmpSmsLastModified = lastModified;
                    latestSmsBackupFile = uriFile;
                } else {
                    if (lastModified >= tmpSmsLastModified) {
                        tmpSmsLastModified = lastModified;
                        latestSmsBackupFile = uriFile;
                    }
                }
            }
            if (backupType.equals("calls.xml")) {
                if (Uri.EMPTY.equals(latestSmsBackupFile)) {
                    tmpCallLogLastModified = lastModified;
                    latestCallLogBackupFile = uriFile;
                } else {
                    if (lastModified >= tmpCallLogLastModified) {
                        tmpCallLogLastModified = lastModified;
                        latestCallLogBackupFile = uriFile;
                    }
                }
            }
        }
        choosedSmsBackupFile.setValue(latestSmsBackupFile);
        choosedCallLogBackupFile.setValue(latestCallLogBackupFile);
    }

    private String convertEpochIntoHumanReadableDatetime(Long epoch) {
        String humanReadableDatetime;
        Instant instant = Instant.ofEpochMilli(epoch);
        ZoneId z = ZoneId.of("Europe/Warsaw");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        ZonedDateTime datetime = instant.atZone(z);
        humanReadableDatetime = datetime.format(formatter);
        return humanReadableDatetime;
    }

}
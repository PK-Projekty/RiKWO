package com.pkprojekty.rikwo.UI;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
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
import androidx.appcompat.app.AlertDialog;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.pkprojekty.rikwo.CallLog.RestoreCallLog;
import com.pkprojekty.rikwo.Entities.CallData;
import com.pkprojekty.rikwo.Entities.SmsData;
import com.pkprojekty.rikwo.Permissions.Permissions;
import com.pkprojekty.rikwo.R;
import com.pkprojekty.rikwo.Sms.RestoreSms;
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
public class RestoreFragment extends Fragment {
    Permissions permissions = new Permissions(this);

    private Context restoreContext;
    private Uri uriTree = Uri.EMPTY;

    MutableLiveData<Uri> choosedSmsBackupFile = new MutableLiveData<>();
    MutableLiveData<Uri> choosedCallLogBackupFile = new MutableLiveData<>();

    private final ActivityResultLauncher<Intent> openSmsBackupFileLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                requireActivity();
                if (result.getResultCode() == Activity.RESULT_OK) {
                    System.out.println("Obecny plik: " + choosedSmsBackupFile);
                    //choosedSmsBackupFile = result.getData().getData();
                    String fileName = null;
                    if (result.getData() != null) {
                        fileName = Objects.requireNonNull(DocumentFile.fromSingleUri(restoreContext, result.getData().getData())).getName();
                    }
                    System.out.println("Nazwa wybranego pliku to: "+fileName);
                    if (fileName != null) {
                        if (fileName.split("-")[1].equals("sms.xml")) {
                            choosedSmsBackupFile.setValue(result.getData().getData());
                        } else {
                            //Toast.makeText(restoreContext, "Wskazana kopia zapasowa "+fileName+" nie zawiera wiadomości sms. Spróbuj ponownie", Toast.LENGTH_SHORT).show();
                            String title = "Nieprawidłowa kopia zapasowa";
                            String additionalInfo;
                            if (fileName.split("-")[1].equals("calls.xml")) {
                                additionalInfo = "\n\nWygląda na to, że przez pomyłkę wskazano kopię zapasową\nrejestru połączeń.";
                            } else {
                                additionalInfo = "\n\nWygląda na to, że przez pomyłkę wskazano plik niebędący kopią zapasową.";
                            }
                            String message = "Wskazany przez Ciebie plik: "+fileName+"\nnie jest prawidłową kopią zapasową wiadomości sms."+" "+additionalInfo;
                            new AlertDialog.Builder(restoreContext)
                                .setTitle(title)
                                .setMessage(message)
                                .setPositiveButton("Spróbuj ponownie", null).create().show();
                        }
                    }
                    System.out.println("Nowy plik: " + choosedSmsBackupFile);
                }
            });
    private final ActivityResultLauncher<Intent> openCallLogBackupFileLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                requireActivity();
                if (result.getResultCode() == Activity.RESULT_OK) {
                    System.out.println("Obecny plik: " + choosedCallLogBackupFile);
                    //choosedSmsBackupFile = result.getData().getData();
                    String fileName = null;
                    if (result.getData() != null) {
                        fileName = Objects.requireNonNull(DocumentFile.fromSingleUri(restoreContext, result.getData().getData())).getName();
                    }
                    System.out.println("Nazwa wybranego pliku to: "+fileName);
                    if (fileName != null) {
                        if (fileName.split("-")[1].equals("calls.xml")) {
                            choosedCallLogBackupFile.setValue(result.getData().getData());
                        } else {
                            //Toast.makeText(restoreContext, "Wskazana kopia zapasowa "+fileName+" nie zawiera wiadomości sms. Spróbuj ponownie", Toast.LENGTH_SHORT).show();
                            String title = "Nieprawidłowa kopia zapasowa";
                            String additionalInfo;
                            if (fileName.split("-")[1].equals("sms.xml")) {
                                additionalInfo = "\n\nWygląda na to, że przez pomyłkę wskazano kopię zapasową\nwiadomości sms.";
                            } else {
                                additionalInfo = "\n\nWygląda na to, że przez pomyłkę wskazano plik niebędący kopią zapasową.";
                            }
                            String message = "Wskazany przez Ciebie plik: "+fileName+"\nnie jest prawidłową kopią zapasową rejestru połączeń."+" "+additionalInfo;
                            new AlertDialog.Builder(restoreContext)
                                    .setTitle(title)
                                    .setMessage(message)
                                    .setPositiveButton("Spróbuj ponownie", null).create().show();
                        }
                    }
                    System.out.println("Nowy plik: " + choosedCallLogBackupFile);
                }
            });

    MutableLiveData<Boolean> IsDefaultSmsApp = new MutableLiveData<>();

    String defaultSmsApp = "";

    MutableLiveData<Boolean> WriteCallLogsPermissionGranted = new MutableLiveData<>();
    MutableLiveData<Boolean> ReadExternalStoragePermissionGranted = new MutableLiveData<>();

    private final ActivityResultLauncher<Intent> changeToDefaultSmsAppLauncher =
        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (permissions.isDefaultSmsApp()) {
                IsDefaultSmsApp.setValue(true);
                restoreSms();
                Toast.makeText(restoreContext, "Set as Default SMS app", Toast.LENGTH_SHORT).show();
                System.out.println("PRZYWRACAMY DOMYŚLNĄ APLIKACJĘ SMS");
                String title = "Domyślna aplikacja SMS";
                String message = "Aplikacja jest ustawiona jako domyślna dla wiadomości sms. Proszę zmienić domyślną aplikację dla wiadomości sms na dotychczas używaną." +
                        "\n\nPo kliknięciu kontynuuj zostaniesz poproszony o zmianę obecnie domyślnej aplikacji dla wiadomości sms." +
                        "\n\nPo kliknięciu zrezygnuj nie zostaniesz poproszony o zmianę domyślnej aplikacji dla wiadomości sms.";
                new AlertDialog.Builder(restoreContext)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("Kontynuuj", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    RoleManager roleManager = restoreContext.getSystemService(RoleManager.class);
                                    if (roleManager.isRoleAvailable(RoleManager.ROLE_SMS)) {
                                        if (roleManager.isRoleHeld(RoleManager.ROLE_SMS)) {
                                            System.out.println("ELO");
                                            Intent roleRequestIntent = new Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS);
                                            startActivity(roleRequestIntent);
                                        } else {
                                            Intent roleRequestIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS);
                                            changeToDefaultSmsAppLauncher.launch(roleRequestIntent);
                                        }
                                    }
                                } else {
                                    Intent setSmsAppIntent =
                                            new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                                    setSmsAppIntent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                                            defaultSmsApp);
                                    changeToDefaultSmsAppLauncher.launch(setSmsAppIntent);
                                }
                            }
                        })
                        .setNegativeButton("Zrezygnuj", null).create().show();
            } else {
                IsDefaultSmsApp.setValue(false);
                Toast.makeText(restoreContext, "Not set as Default SMS app", Toast.LENGTH_SHORT).show();
            }
        });

    public RestoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        restoreContext = context;
        System.out.println("RestoreFragment onAttach HIT");

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
        System.out.println("RestoreFragment onResume HIT");

        // read from app preferences location uri choosed by user
        restoreChoosedDirectoryFromAppPreferences();

        //check app permissions
        checkPermissions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restore, container, false);
        System.out.println("RestoreFragment onCreateView HIT");

        // read from app preferences location uri choosed by user
        restoreChoosedDirectoryFromAppPreferences();

        //check app permissions
        checkPermissions();

        // section: Restore backup
        restoreBackup(view);

        return view;
    }

    public void checkPermissions() {
        // check if app is set as default app for sms
        // it should be true, when user wants to restore sms from backup
        IsDefaultSmsApp.setValue(permissions.isDefaultSmsApp());

        // ONLY FOR <= ANDROID P:
        // get current default sms app package name before setting this app as default sms app
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            System.out.println("DEFAULT SMS APP: " + Telephony.Sms.getDefaultSmsPackage(restoreContext));
            if (!permissions.isDefaultSmsApp()) {
                defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(restoreContext);
                storeDefaultSmsAppNameInAppPreferences(defaultSmsApp);
            }
            IsDefaultSmsApp.setValue(permissions.isDefaultSmsApp());
            System.out.println("CURRENT DEFAULT SMS APP: " + Telephony.Sms.getDefaultSmsPackage(restoreContext));
            System.out.println("IS DEFAULT APP: " + IsDefaultSmsApp.getValue());
        }

        // check for all permissions needed by app
        WriteCallLogsPermissionGranted.setValue(permissions.hasWriteCallLogsPermission());
        if (uriTree != Uri.EMPTY) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                ReadExternalStoragePermissionGranted.setValue(permissions.hasReadExternalStoragePermission() && Objects.requireNonNull(DocumentFile.fromTreeUri(restoreContext, uriTree)).canRead());
            } else {
                ReadExternalStoragePermissionGranted.setValue(Objects.requireNonNull(DocumentFile.fromTreeUri(restoreContext, uriTree)).canRead());
            }
        } else {
            ReadExternalStoragePermissionGranted.setValue(false);
        }
    }

    private void storeDefaultSmsAppNameInAppPreferences(String value) {
        SharedPreferences preferences = requireActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("defaultSmsApp", value);
        editor.apply();
    }

    public void restoreBackup(View view) {
        // initialize all gui elements needed by this section
        SwitchMaterial switchRestoreIncludeSmsFile = view.findViewById(R.id.switchRestoreIncludeSmsFile);
        TextView textViewRestoreSmsFileAbout = view.findViewById(R.id.textViewRestoreSmsFileAbout);
        Button buttonRestoreChooseOtherSmsFile = view.findViewById(R.id.buttonRestoreChooseOtherSmsFile);
        SwitchMaterial switchRestoreIncludeCallLogFile = view.findViewById(R.id.switchRestoreIncludeCallLogFile);
        TextView textViewRestoreCallLogFileAbout = view.findViewById(R.id.textViewRestoreCallLogFileAbout);
        Button buttonRestoreChooseOtherCallLogFile = view.findViewById(R.id.buttonRestoreChooseOtherCallLogFile);
        Button buttonRestoreBegin = view.findViewById(R.id.buttonRestoreBegin);
        // update gui elements when user choose sms backup
        choosedSmsBackupFile.observe(requireActivity(), uri -> {
            switchRestoreIncludeSmsFile.setChecked(true);
            switchRestoreIncludeSmsFile.setEnabled(true);
            String backupFileName = Objects.requireNonNull(DocumentFile.fromSingleUri(restoreContext, Objects.requireNonNull(choosedSmsBackupFile.getValue()))).getName();
            long epoch = Objects.requireNonNull(DocumentFile.fromSingleUri(restoreContext, choosedSmsBackupFile.getValue())).lastModified();
            String backupLastModified = convertEpochIntoHumanReadableDatetime(epoch);
            FileHandler fh = new FileHandler(restoreContext);
            int entries = fh.countEntriesInSmsXml(restoreContext, choosedSmsBackupFile.getValue());
            String pluralEntry = (entries >= 2 || entries == 0) ? "smsów" : "sms";
            String text =
                    "Kopia zapasowa: "+backupFileName+
                            "\nData ostatniej modyfikacji: "+backupLastModified+
                            "\nWskazana kopia składa się z "+entries+" "+pluralEntry;
            textViewRestoreSmsFileAbout.setText(text);
            String btnTxt = "Wskaż inną kopię wiadomości";
            buttonRestoreChooseOtherSmsFile.setText(btnTxt);
        });
        // update gui elements when user choose call log backup
        choosedCallLogBackupFile.observe(requireActivity(), uri -> {
            if (WriteCallLogsPermissionGranted.getValue() == null) {
                WriteCallLogsPermissionGranted.setValue(false);
            }
            if (WriteCallLogsPermissionGranted.getValue()) {
                switchRestoreIncludeCallLogFile.setChecked(true);
                switchRestoreIncludeCallLogFile.setEnabled(true);
            }
            String backupFileName = Objects.requireNonNull(DocumentFile.fromSingleUri(restoreContext, Objects.requireNonNull(choosedCallLogBackupFile.getValue()))).getName();
            long epoch = Objects.requireNonNull(DocumentFile.fromSingleUri(restoreContext, choosedCallLogBackupFile.getValue())).lastModified();
            String backupLastModified = convertEpochIntoHumanReadableDatetime(epoch);
            FileHandler fh = new FileHandler(restoreContext);
            int entries = fh.countEntriesInCallLogXml(restoreContext,choosedCallLogBackupFile.getValue());
            String pluralEntry = (entries >= 2 || entries == 0) ? "połączeń" : "połączenia";
            String text =
                    "Kopia zapasowa: "+backupFileName+
                            "\nData ostatniej modyfikacji: "+backupLastModified+
                            "\nWskazana kopia składa się z "+entries+" "+pluralEntry;
            textViewRestoreCallLogFileAbout.setText(text);
            String btnTxt = "Wskaż inną kopię rejestru";
            buttonRestoreChooseOtherCallLogFile.setText(btnTxt);
        });
        // check if location has been choosed, and app has permissions to use it
        if (uriTree != Uri.EMPTY) {
            if (ReadExternalStoragePermissionGranted.getValue() == null) {
                ReadExternalStoragePermissionGranted.setValue(false);
            }
            if (!ReadExternalStoragePermissionGranted.getValue()) {
                String textViewRestoreSmsFileAboutText = "\nBrak dostępu do kopii zapasowej wiadomości sms!\n\nProszę zweryfikować uprawnienia dostępu do pamięci masowej, lub wskazać kopię wiadomości.";
                textViewRestoreSmsFileAbout.setText(textViewRestoreSmsFileAboutText);
                switchRestoreIncludeSmsFile.setEnabled(false);
                switchRestoreIncludeSmsFile.setChecked(false);
                String textViewRestoreCallLogFileAboutText = "\nBrak dostępu do kopii zapasowej rejestru połączeń!\n\nProszę zweryfikować uprawnienia dostępu do pamięci masowej, lub wskazać kopię rejestru.";
                textViewRestoreCallLogFileAbout.setText(textViewRestoreCallLogFileAboutText);
                switchRestoreIncludeCallLogFile.setEnabled(false);
                switchRestoreIncludeCallLogFile.setChecked(false);
            }
        } else {
            // even if app has needed storage permissions, when choosed location is null
            // we are set those permissions to false, and set gui elements for asking user to choose location again
            ReadExternalStoragePermissionGranted.setValue(false);
            String textViewRestoreSmsFileAboutText = "\nNie wskazano lokalizacji dla kopii zapasowych!\n\nOpcjonalnie możesz wskazać kopię wiadomości.";
            textViewRestoreSmsFileAbout.setText(textViewRestoreSmsFileAboutText);
            switchRestoreIncludeSmsFile.setChecked(false);
            switchRestoreIncludeSmsFile.setEnabled(false);
            String textViewRestoreCallLogFileAboutText = "\nNie wskazano lokalizacji dla kopii zapasowych!\n\nOpcjonalnie możesz wskazać kopię rejestru.";
            switchRestoreIncludeCallLogFile.setChecked(false);
            switchRestoreIncludeCallLogFile.setEnabled(false);
            textViewRestoreCallLogFileAbout.setText(textViewRestoreCallLogFileAboutText);
        }
        // if app has needed storage permissions and choosed location is not null
        // we are set gui element for display information about last backup in choosed location
        if (ReadExternalStoragePermissionGranted.getValue() == null) {
            ReadExternalStoragePermissionGranted.setValue(false);
        }
        if (ReadExternalStoragePermissionGranted.getValue()) {
            List<Uri> fileUriList = listDirectory(uriTree);
            lastBackupInChoosedDirectory(fileUriList);
            // set gui element for displaying information about last sms backup in choosed location
            if (choosedSmsBackupFile.getValue() == Uri.EMPTY) {
                String text = "We wskazanej lokalizacji nie ma kopii zapasowych wiadomości sms.";
                textViewRestoreSmsFileAbout.setText(text);
                switchRestoreIncludeSmsFile.setChecked(false);
                switchRestoreIncludeSmsFile.setEnabled(false);
            } else {
                // assumption: in choosed location there is at least one sms backup file
                String backupFileName = Objects.requireNonNull(DocumentFile.fromSingleUri(restoreContext, Objects.requireNonNull(choosedSmsBackupFile.getValue()))).getName();
                long epoch = Objects.requireNonNull(DocumentFile.fromSingleUri(restoreContext, choosedSmsBackupFile.getValue())).lastModified();
                String backupLastModified = convertEpochIntoHumanReadableDatetime(epoch);
                FileHandler fh = new FileHandler(restoreContext);
                int entries = fh.countEntriesInSmsXml(restoreContext, choosedSmsBackupFile.getValue());
                String pluralEntry = (entries >= 2 || entries == 0) ? "smsów" : "sms";
                String text =
                        "Kopia zapasowa: "+backupFileName+
                                "\nData ostatniej modyfikacji: "+backupLastModified+
                                "\nWskazana kopia składa się z "+entries+" "+pluralEntry;
                textViewRestoreSmsFileAbout.setText(text);
                String btnTxt = "Wskaż inną kopię wiadomości";
                buttonRestoreChooseOtherSmsFile.setText(btnTxt);
            }
            // set gui element for displaying information about last call log backup in choosed location
            if (choosedCallLogBackupFile.getValue() == Uri.EMPTY) {
                String text = "We wskazanej lokalizacji nie ma kopii zapasowych rejestru połączeń.";
                textViewRestoreCallLogFileAbout.setText(text);
                switchRestoreIncludeCallLogFile.setChecked(false);
                switchRestoreIncludeCallLogFile.setEnabled(false);
            } else {
                // assumption: in choosed location there is at least one call log backup file
                if (WriteCallLogsPermissionGranted.getValue() == null) {
                    WriteCallLogsPermissionGranted.setValue(false);
                }
                if (WriteCallLogsPermissionGranted.getValue()) {
                    switchRestoreIncludeCallLogFile.setChecked(true);
                    switchRestoreIncludeCallLogFile.setEnabled(true);
                }
                String backupFileName = Objects.requireNonNull(DocumentFile.fromSingleUri(restoreContext, Objects.requireNonNull(choosedCallLogBackupFile.getValue()))).getName();
                long epoch = Objects.requireNonNull(DocumentFile.fromSingleUri(restoreContext, choosedCallLogBackupFile.getValue())).lastModified();
                String backupLastModified = convertEpochIntoHumanReadableDatetime(epoch);
                FileHandler fh = new FileHandler(restoreContext);
                int entries = fh.countEntriesInCallLogXml(restoreContext,choosedCallLogBackupFile.getValue());
                String pluralEntry = (entries >= 2 || entries == 0) ? "połączeń" : "połączenia";
                String text =
                        "Kopia zapasowa: "+backupFileName+
                                "\nData ostatniej modyfikacji: "+backupLastModified+
                                "\nWskazana kopia składa się z "+entries+" "+pluralEntry;
                textViewRestoreCallLogFileAbout.setText(text);
                String btnTxt = "Wskaż inną kopię rejestru";
                buttonRestoreChooseOtherCallLogFile.setText(btnTxt);
            }
        }
        // set gui elements for asking user to choose other backup
        buttonRestoreChooseOtherSmsFile.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/xml");
            if (Uri.EMPTY.equals(uriTree)) { intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uriTree); }
            openSmsBackupFileLauncher.launch(intent);
        });
        buttonRestoreChooseOtherCallLogFile.setOnClickListener(view12 -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/xml");
            if (Uri.EMPTY.equals(uriTree)) { intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uriTree); }
            openCallLogBackupFileLauncher.launch(intent);
        });
        // set gui element for restore backup now
        // make backup now button available, when:
        // - sms switch is on
        // - choosed sms backup is available -> choosedSmsBackupFile
        // - call log switch is on
        // - choosed call log backup is available -> choosedCallLogBackupFile
        System.out.println("loc: "+!Uri.EMPTY.equals(choosedSmsBackupFile.getValue()));
        System.out.println("loc: "+!Uri.EMPTY.equals(choosedCallLogBackupFile.getValue()));
        if (switchRestoreIncludeSmsFile.isChecked() && !Uri.EMPTY.equals(choosedSmsBackupFile.getValue())) {
            buttonRestoreBegin.setEnabled(true);
        } else buttonRestoreBegin.setEnabled(switchRestoreIncludeCallLogFile.isChecked() && !Uri.EMPTY.equals(choosedCallLogBackupFile.getValue()));
        // enable restore when at least one of those switches is checked
        switchRestoreIncludeSmsFile.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) { buttonRestoreBegin.setEnabled(true); }
            else { if (!switchRestoreIncludeSmsFile.isChecked() && !switchRestoreIncludeCallLogFile.isChecked()) { buttonRestoreBegin.setEnabled(false); } }
        });
        switchRestoreIncludeCallLogFile.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) { buttonRestoreBegin.setEnabled(true); }
            else { if (!switchRestoreIncludeSmsFile.isChecked() && !switchRestoreIncludeCallLogFile.isChecked()) { buttonRestoreBegin.setEnabled(false); } }
        });
        buttonRestoreBegin.setOnClickListener(view13 -> {
            if (switchRestoreIncludeSmsFile.isChecked()) {
                ReadExternalStoragePermissionGranted.setValue(Objects.requireNonNull(DocumentFile.fromSingleUri(restoreContext, choosedSmsBackupFile.getValue())).canRead());
            } else if (switchRestoreIncludeCallLogFile.isChecked()) {
                ReadExternalStoragePermissionGranted.setValue(Objects.requireNonNull(DocumentFile.fromSingleUri(restoreContext, choosedCallLogBackupFile.getValue())).canRead());
            } else {
                ReadExternalStoragePermissionGranted.setValue(Objects.requireNonNull(DocumentFile.fromSingleUri(restoreContext, choosedCallLogBackupFile.getValue())).canRead() && Objects.requireNonNull(DocumentFile.fromSingleUri(restoreContext, choosedSmsBackupFile.getValue())).canRead());
            }
            if (ReadExternalStoragePermissionGranted.getValue()) { restore(switchRestoreIncludeSmsFile.isChecked(), switchRestoreIncludeCallLogFile.isChecked()); }
        });

    }

    private void restoreChoosedDirectoryFromAppPreferences() {
        SharedPreferences preferences = requireActivity().getPreferences(MODE_PRIVATE);
        if (preferences.contains("uriTree")) {
            uriTree = Uri.parse(preferences.getString("uriTree", ""));
        }
        System.out.println("Wybrany katalog: " + uriTree);
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
                    if (Objects.requireNonNull(DocumentFile.fromSingleUri(restoreContext, uriFile)).isFile()) {
                        uriList.add(uriFile);
                    } else if (Objects.requireNonNull(DocumentFile.fromSingleUri(restoreContext, uriFile)).isDirectory()) {
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
            lastModified = Objects.requireNonNull(DocumentFile.fromSingleUri(restoreContext, uriFile)).lastModified();
            fileName = Objects.requireNonNull(DocumentFile.fromSingleUri(restoreContext, uriFile)).getName();
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

    public void restore(boolean smsSwitchState, boolean callLogSwitchState) {
        System.out.println("Przywracanie kopii zapasowej: "+uriTree);
        if (smsSwitchState) {
            if (!permissions.isDefaultSmsApp()) {
                askForDefaultSmsApp();
            } else {
                restoreSms();
                System.out.println("PRZYWRACAMY DOMYŚLNĄ APLIKACJĘ SMS");
                String title = "Domyślna aplikacja SMS";
                String message = "Aplikacja jest ustawiona jako domyślna dla wiadomości sms. Proszę zmienić domyślną aplikację dla wiadomości sms na dotychczas używaną." +
                        "\n\nPo kliknięciu kontynuuj zostaniesz poproszony o zmianę obecnie domyślnej aplikacji dla wiadomości sms." +
                        "\n\nPo kliknięciu zrezygnuj nie zostaniesz poproszony o zmianę domyślnej aplikacji dla wiadomości sms.";
                new AlertDialog.Builder(restoreContext)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("Kontynuuj", (dialogInterface, i) -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                RoleManager roleManager = restoreContext.getSystemService(RoleManager.class);
                                if (roleManager.isRoleAvailable(RoleManager.ROLE_SMS)) {
                                    if (roleManager.isRoleHeld(RoleManager.ROLE_SMS)) {
                                        System.out.println("ELO");
                                        Intent roleRequestIntent = new Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS);
                                        startActivity(roleRequestIntent);
                                    } else {
                                        Intent roleRequestIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS);
                                        changeToDefaultSmsAppLauncher.launch(roleRequestIntent);
                                    }
                                }
                            } else {
                                Intent setSmsAppIntent =
                                        new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                                setSmsAppIntent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                                        defaultSmsApp);
                                changeToDefaultSmsAppLauncher.launch(setSmsAppIntent);
                            }
                        })
                        .setNegativeButton("Zrezygnuj", null).create().show();
            }
        }
        if (callLogSwitchState) {
            restoreCallLog();
        }
    }

    private void restoreSms() {
        if (restoreContext.getPackageName().equals(Telephony.Sms.getDefaultSmsPackage(restoreContext))) {
            FileHandler fh = new FileHandler(restoreContext);
            List<List<SmsData>> smsDataLists = fh.restoreSmsFromXml(restoreContext, choosedSmsBackupFile.getValue());
            RestoreSms restoreSms = new RestoreSms(restoreContext);
            restoreSms.deleteAllSms();
            restoreSms.setAllSms(smsDataLists);
        }
    }

    private void restoreCallLog() {
        FileHandler fh = new FileHandler(restoreContext);
        List<CallData> callDataList = fh.restoreCallLogFromXml(restoreContext, choosedCallLogBackupFile.getValue());
        RestoreCallLog restoreCallLog = new RestoreCallLog(restoreContext);
        restoreCallLog.deleteAllCallLog();
        restoreCallLog.setAllCallLog(callDataList);
    }

    private final DialogInterface.OnClickListener showDialogChangeDefaultSmsAppListner =
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        RoleManager roleManager;
                        roleManager = restoreContext.getSystemService(RoleManager.class);
                        if (roleManager.isRoleAvailable(RoleManager.ROLE_SMS)) {
                            if (roleManager.isRoleHeld(RoleManager.ROLE_SMS)) {
                                Intent roleRequestIntent = new Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS);
                                startActivity(roleRequestIntent);
                            } else {
                                Intent roleRequestIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_SMS);
                                changeToDefaultSmsAppLauncher.launch(roleRequestIntent);
                            }
                        }
                    } else {
                        Intent setSmsAppIntent =
                                new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                        setSmsAppIntent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                                requireActivity().getPackageName());
                        changeToDefaultSmsAppLauncher.launch(setSmsAppIntent);
                    }
                }
            };

    public void askForDefaultSmsApp() {
        System.out.println("ZMIENIAMY DOMYŚLNĄ APLIKACJĘ SMS");
        String title = "Domyślna aplikacja SMS";
        String message = "Aby można było przywrócić kopię zapasową niezbędne jest tymczasowe ustawienie aplikacji jako domyślnej dla wiadomości sms." +
                "\n\nPo kliknięciu kontynuuj zostaniesz poproszony o ustawienie tej aplikacji jako domyślnej dla wiadomości sms." +
                "\n\nMożesz także zrezygnować co spowoduje, że kopia zapasowa wiadomości sms nie zostanie przywrócona.";
        new AlertDialog.Builder(restoreContext)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Kontynuuj", showDialogChangeDefaultSmsAppListner)
                .setNegativeButton("Zrezygnuj", null).create().show();

    }
}
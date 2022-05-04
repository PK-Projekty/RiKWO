package com.pkprojekty.rikwo;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.pkprojekty.rikwo.CallLog.RestoreCallLog;
import com.pkprojekty.rikwo.Entities.CallData;
import com.pkprojekty.rikwo.Entities.SmsData;
import com.pkprojekty.rikwo.Sms.RestoreSms;
import com.pkprojekty.rikwo.Xml.FileHandler;

import java.io.File;
import java.text.ChoiceFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestoreFragment extends Fragment {
    private Context restoreContext;
    private Uri uriTree = Uri.EMPTY;
    //private Uri choosedSmsBackupFile = Uri.EMPTY;
    //private Uri choosedCallLogBackupFile = Uri.EMPTY;

    MutableLiveData<Uri> choosedSmsBackupFile = new MutableLiveData<>();
    MutableLiveData<Uri> choosedCallLogBackupFile = new MutableLiveData<>();

    private ActivityResultLauncher<Intent> openSmsBackupFileLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == requireActivity().RESULT_OK) {
                    if (result != null) {
                        System.out.println("Obecny plik: " + choosedSmsBackupFile);
                        //choosedSmsBackupFile = result.getData().getData();
                        String fileName = DocumentFile.fromSingleUri(restoreContext,result.getData().getData()).getName();
                        System.out.println("Nazwa wybranego pliku to: "+fileName);
                        if (fileName.split("-")[1].equals("sms.xml")) {
                            choosedSmsBackupFile.setValue(result.getData().getData());
                        } else {
                            //Toast.makeText(restoreContext, "Wskazana kopia zapasowa "+fileName+" nie zawiera wiadomości sms. Spróbuj ponownie", Toast.LENGTH_SHORT).show();
                            String title = "Nieprawidłowa kopia zapasowa";
                            String additionalInfo = "";
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
                        System.out.println("Nowy plik: " + choosedSmsBackupFile);
                    }
                }
            });
    private ActivityResultLauncher<Intent> openCallLogBackupFileLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == requireActivity().RESULT_OK) {
                    if (result != null) {
                        System.out.println("Obecny plik: " + choosedCallLogBackupFile);
                        //choosedSmsBackupFile = result.getData().getData();
                        String fileName = DocumentFile.fromSingleUri(restoreContext,result.getData().getData()).getName();
                        System.out.println("Nazwa wybranego pliku to: "+fileName);
                        if (fileName.split("-")[1].equals("calls.xml")) {
                            choosedCallLogBackupFile.setValue(result.getData().getData());
                        } else {
                            //Toast.makeText(restoreContext, "Wskazana kopia zapasowa "+fileName+" nie zawiera wiadomości sms. Spróbuj ponownie", Toast.LENGTH_SHORT).show();
                            String title = "Nieprawidłowa kopia zapasowa";
                            String additionalInfo = "";
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
                        System.out.println("Nowy plik: " + choosedCallLogBackupFile);
                    }
                }
            });

    private boolean WriteCallLogsPermissionGranted;
    private boolean ReadExternalStoragePermissionGranted;
    private boolean WriteExternalStoragePermissionGranted;

    // WRITE_CALL_LOG permission
    private ActivityResultLauncher<String> requestWriteCallLogPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    if (ContextCompat.checkSelfPermission(restoreContext, Manifest.permission.WRITE_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(restoreContext, "Permission granted", Toast.LENGTH_SHORT).show(); }
                } else { Toast.makeText(restoreContext, "No Permission granted", Toast.LENGTH_SHORT).show(); }
            });
    // READ_EXTERNAL_STORAGE permission
    private ActivityResultLauncher<String> requestReadExternalStoragePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    if (ContextCompat.checkSelfPermission(restoreContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(restoreContext, "Permission granted", Toast.LENGTH_SHORT).show(); }
                } else { Toast.makeText(restoreContext, "No Permission granted", Toast.LENGTH_SHORT).show(); }
            });
    // WRITE_EXTERNAL_STORAGE permission
    private ActivityResultLauncher<String> requestWriteExternalStoragePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    if (ContextCompat.checkSelfPermission(restoreContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(restoreContext, "Permission granted", Toast.LENGTH_SHORT).show(); }
                } else { Toast.makeText(restoreContext, "No Permission granted", Toast.LENGTH_SHORT).show(); }
            });

    public RestoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        restoreContext = context;

        if (Uri.EMPTY.equals(uriTree)) { restoreChoosedDirectoryFromAppPreferences(); }

        // WRITE_CALL_LOG permission
        WriteCallLogsPermissionGranted = false;
        if (ActivityCompat.checkSelfPermission(restoreContext, Manifest.permission.WRITE_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            WriteCallLogsPermissionGranted = true;
        } else { requestWriteCallLogPermissionLauncher.launch(Manifest.permission.WRITE_CALL_LOG); }
        // READ_EXTERNAL_STORAGE permission
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            ReadExternalStoragePermissionGranted = false;
            if (ActivityCompat.checkSelfPermission(restoreContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                ReadExternalStoragePermissionGranted = true;
            } else { requestReadExternalStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE); }
        } else { ReadExternalStoragePermissionGranted = uriTree != null; }
        // WRITE_EXTERNAL_STORAGE permission
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            WriteExternalStoragePermissionGranted = false;
            if (ActivityCompat.checkSelfPermission(restoreContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                WriteExternalStoragePermissionGranted = true;
            } else { requestWriteExternalStoragePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE); }
        } else { WriteExternalStoragePermissionGranted = uriTree != null; }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restore, container, false);

        restoreChoosedDirectoryFromAppPreferences();
        List<Uri> fileUriList = listDirectory(uriTree);
        lastBackupInChoosedDirectory(fileUriList);

        SwitchMaterial switchRestoreIncludeSmsFile = view.findViewById(R.id.switchRestoreIncludeSmsFile);
        TextView textViewRestoreSmsFileAbout = view.findViewById(R.id.textViewRestoreSmsFileAbout);
        Button buttonRestoreChooseOtherSmsFile = view.findViewById(R.id.buttonRestoreChooseOtherSmsFile);
        if (Uri.EMPTY.equals(choosedSmsBackupFile)) {
            switchRestoreIncludeSmsFile.setEnabled(false);
        } else {
            switchRestoreIncludeSmsFile.setEnabled(true);
            switchRestoreIncludeSmsFile.setChecked(true);
            choosedSmsBackupFile.observe(requireActivity(), new Observer<Uri>(){
                @Override
                public void onChanged(Uri uri) {
                    String backupFileName = DocumentFile.fromSingleUri(restoreContext,choosedSmsBackupFile.getValue()).getName();
                    long epoch = DocumentFile.fromSingleUri(restoreContext,choosedSmsBackupFile.getValue()).lastModified();
                    String backupLastModified = convertEpochIntoHumanReadableDatetime(epoch);
                    FileHandler fh = new FileHandler(restoreContext);
                    int entries = fh.countEntriesInSmsXml(restoreContext, choosedSmsBackupFile.getValue());
                    String pluralEntry = (entries >= 2 || entries == 0) ? "smsów" : "sms";
                    String text =
                            "Kopia zapasowa: "+backupFileName+
                                    "\nData ostatniej modyfikacji: "+backupLastModified+
                                    "\nWskazana kopia składa się z "+entries+" "+pluralEntry;
                    textViewRestoreSmsFileAbout.setText(text);
                    buttonRestoreChooseOtherSmsFile.setText("Wskaż inną kopię wiadomości");
                }
            });
        }
        buttonRestoreChooseOtherSmsFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/xml");
                if (Uri.EMPTY.equals(uriTree)) { intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uriTree); }
                //Launch activity to get result
                openSmsBackupFileLauncher.launch(intent);
            }
        });

        SwitchMaterial switchRestoreIncludeCallLogFile = view.findViewById(R.id.switchRestoreIncludeCallLogFile);
        TextView textViewRestoreCallLogFileAbout = view.findViewById(R.id.textViewRestoreCallLogFileAbout);
        Button buttonRestoreChooseOtherCallLogFile = view.findViewById(R.id.buttonRestoreChooseOtherCallLogFile);
        if (Uri.EMPTY.equals(choosedCallLogBackupFile)) {
            switchRestoreIncludeCallLogFile.setEnabled(false);
        } else {
            switchRestoreIncludeCallLogFile.setEnabled(true);
            switchRestoreIncludeCallLogFile.setChecked(true);
            choosedCallLogBackupFile.observe(requireActivity(), new Observer<Uri>() {
                @Override
                public void onChanged(Uri uri) {
                    String backupFileName = DocumentFile.fromSingleUri(restoreContext,choosedCallLogBackupFile.getValue()).getName();
                    long epoch = DocumentFile.fromSingleUri(restoreContext,choosedCallLogBackupFile.getValue()).lastModified();
                    String backupLastModified = convertEpochIntoHumanReadableDatetime(epoch);
                    FileHandler fh = new FileHandler(restoreContext);
                    int entries = fh.countEntriesInCallLogXml(restoreContext,choosedCallLogBackupFile.getValue());
                    String pluralEntry = (entries >= 2 || entries == 0) ? "połączeń" : "połączenia";
                    String text =
                            "Kopia zapasowa: "+backupFileName+
                                    "\nData ostatniej modyfikacji: "+backupLastModified+
                                    "\nWskazana kopia składa się z "+entries+" "+pluralEntry;
                    textViewRestoreCallLogFileAbout.setText(text);
                    buttonRestoreChooseOtherCallLogFile.setText("Wskaż inną kopię rejestru");
                }
            });
        }
        buttonRestoreChooseOtherCallLogFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/xml");
                if (Uri.EMPTY.equals(uriTree)) { intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uriTree); }
                //Launch activity to get result
                openCallLogBackupFileLauncher.launch(intent);
            }
        });


//        if (WriteCallLogsPermissionGranted &&
//            ReadExternalStoragePermissionGranted &&
//            WriteExternalStoragePermissionGranted) { restore(); }

        return view;
    }

    private void restoreChoosedDirectoryFromAppPreferences() {
        SharedPreferences preferences = requireActivity().getPreferences(MODE_PRIVATE);
        if (preferences.contains("uriTree")) {
            uriTree = Uri.parse(preferences.getString("uriTree", ""));
        }
        System.out.println("Wybrany katalog: " + uriTree);
    }

    public List<Uri> listDirectory(Uri uriTree) {
        // the uri from which we query the files
        Uri uriFolder = DocumentsContract.buildChildDocumentsUriUsingTree(uriTree, DocumentsContract.getTreeDocumentId(uriTree));

        List<Uri> uriList = new ArrayList<>();
        Cursor cursor = null;
        try {
            // let's query the files
            cursor = requireActivity().getContentResolver().query(uriFolder,
                    new String[]{DocumentsContract.Document.COLUMN_DOCUMENT_ID},
                    null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // build the uri for the file
                    Uri uriFile = DocumentsContract.buildDocumentUriUsingTree(uriTree, cursor.getString(0));
                    //add to the list
                    if (DocumentFile.fromSingleUri(restoreContext,uriFile).isFile()) {
                        uriList.add(uriFile);
                    } else if (DocumentFile.fromSingleUri(restoreContext,uriFile).isDirectory()) {
                        System.out.println("To jest katalog! "+uriFile);
                    }
                    System.out.println(uriFile);
                } while (cursor.moveToNext());
                //System.out.println("--------------------");
            }
        } catch (Exception e) {
            // TODO: handle error
        } finally {
            if (cursor!=null) cursor.close();
        }
        return uriList;
    }

    private void lastBackupInChoosedDirectory(List<Uri> listOfFilesUri) {
        DateTimeFormatter dtf  = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        ZoneId z = ZoneId.of("Europe/Warsaw");

        String fileName;
        String backupType;
        String creationDatePartFromFilename;
        LocalDateTime creationDate;
        long lastModified;

        long tmpSmsLastModified = 0;
        long tmpCallLogLastModified = 0;

        Uri latestSmsBackupFile = Uri.EMPTY;
        Uri latestCallLogBackupFile = Uri.EMPTY;

        for (Uri uriFile : listOfFilesUri) {
            // Last modified date from metadata
            lastModified = DocumentFile.fromSingleUri(restoreContext, uriFile).lastModified();
            //System.out.println(lastModified);
            // What to do when dates: lastModified, and creation are different?
            // Could this be a case in which backups are malformed?
            // Perhaps when user done backup, he would restore it on different time zone whatsoever
            // Nie wiem czemu mnie wzieło na pisanie w innym języku niż jedynym słusznym...
            // creation date from filename
            fileName = DocumentFile.fromSingleUri(restoreContext, uriFile).getName();
            backupType = fileName.split("-")[1];
            //System.out.println(backupType);
            //creationDatePartFromFilename = fileName.split("-")[0];
            //System.out.println(creationDatePartFromFilename);
            //creationDate = LocalDateTime.parse(creationDatePartFromFilename, dtf);
            //System.out.println(creationDate.atZone(z).toInstant().toEpochMilli());
            if (backupType.equals("sms.xml")) {
                if (Uri.EMPTY.equals(latestSmsBackupFile)) {
                    tmpSmsLastModified = lastModified;
                    latestSmsBackupFile = uriFile;
                    //System.out.println("Pierwszy plik z kopią zapasową wiadomości sms: "+tmpSmsLastModified);
                } else {
                    if (lastModified >= tmpSmsLastModified) {
                        tmpSmsLastModified = lastModified;
                        latestSmsBackupFile = uriFile;
                        //System.out.println("Znaleziono nowszą kopię wiadomości sms: "+tmpSmsLastModified);
                    }
                }
            }
            if (backupType.equals("calls.xml")) {
                if (Uri.EMPTY.equals(latestSmsBackupFile)) {
                    tmpCallLogLastModified = lastModified;
                    latestCallLogBackupFile = uriFile;
                    //System.out.println("Pierwszy plik z kopią zapasową rejestru połączeń: "+tmpCallLogLastModified);
                } else {
                    if (lastModified >= tmpCallLogLastModified) {
                        tmpCallLogLastModified = lastModified;
                        latestCallLogBackupFile = uriFile;
                        //System.out.println("Znaleziono nowszą kopię rejestru połączeń: "+tmpCallLogLastModified);
                    }
                }
            }
            //System.out.println(tmpSmsLastModified + ", " + tmpCallLogLastModified);
        }
        //System.out.println("Najnowsza kopia zapasowa wiadomości sms: " + latestSmsBackupFile);
        //System.out.println("Najnowsza kopia zapasowa rejestru połączeń: " + latestCallLogBackupFile);
        //choosedSmsBackupFile = latestSmsBackupFile;
        choosedSmsBackupFile.setValue(latestSmsBackupFile);
        //choosedCallLogBackupFile = latestCallLogBackupFile;
        choosedCallLogBackupFile.setValue(latestCallLogBackupFile);
    }

    private String convertEpochIntoHumanReadableDatetime(Long epoch) {
        String humanReadableDatetime = "";
        // DocumentFile.fromSingleUri(restoreContext,choosedCallLogBackupFile).lastModified();
        Instant instant = Instant.ofEpochMilli(epoch);
        ZoneId z = ZoneId.of("Europe/Warsaw");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        ZonedDateTime datetime = instant.atZone(z);
        humanReadableDatetime = datetime.format(formatter);
        return humanReadableDatetime;
    }

    public void restore() {
        FileHandler fh = new FileHandler(restoreContext);

        // /storage/emulated/0/Documents/20220428151028-sms.xml
        String SmsFileName = "20220428151028-sms.xml";
        File smsXml = new File(
                Environment.getExternalStorageDirectory() + "/Documents/", SmsFileName
        );
        List<List<SmsData>> smsDataLists = fh.restoreSmsFromXml(smsXml);
        RestoreSms restoreSms = new RestoreSms(restoreContext);
        if (! restoreContext.getPackageName().equals(Telephony.Sms.getDefaultSmsPackage(restoreContext))) {
            System.out.println("Na czas przywracania wiadomości ustaw ta aplikacje jako domyslna");
            System.out.println("Następnie uruchom przywracanie wiadomości sms ponownie");
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS);
            startActivity(intent);
        }
        //restoreSms.setAllSms(smsDataLists);
        if (restoreContext.getPackageName().equals(Telephony.Sms.getDefaultSmsPackage(restoreContext)))
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
        RestoreCallLog restoreCallLog = new RestoreCallLog(restoreContext);
        //restoreCallLog.setAllCallLog(callDataList);
        System.out.println("Rejestr połączeń przywrócony");
        //restoreCallLog.deleteAllCallLog();
        for (CallData callData : callDataList) {
            System.out.println(callData.Number);
        }
    }

}
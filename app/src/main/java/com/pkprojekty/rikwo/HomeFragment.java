package com.pkprojekty.rikwo;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.pkprojekty.rikwo.CallLog.BackupCallLog;
import com.pkprojekty.rikwo.Entities.CallData;
import com.pkprojekty.rikwo.Entities.SmsData;
import com.pkprojekty.rikwo.Sms.BackupSms;
import com.pkprojekty.rikwo.Xml.FileHandler;

import java.time.Instant;
import java.time.LocalDateTime;
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
    private Context homeContext;
    private Uri uriTree = Uri.EMPTY;
    MutableLiveData<Uri> choosedSmsBackupFile = new MutableLiveData<>();
    MutableLiveData<Uri> choosedCallLogBackupFile = new MutableLiveData<>();

    MutableLiveData<Boolean> ReadSmsPermissionGranted = new MutableLiveData<>();
    MutableLiveData<Boolean> ReadCallLogsPermissionGranted = new MutableLiveData<>();
    MutableLiveData<Boolean> WriteCallLogsPermissionGranted = new MutableLiveData<>();
    MutableLiveData<Boolean> ReadExternalStoragePermissionGranted = new MutableLiveData<>();
    MutableLiveData<Boolean> WriteExternalStoragePermissionGranted = new MutableLiveData<>();
    MutableLiveData<Boolean> ReadWriteExternalStoragePermissionGranted = new MutableLiveData<>();
    MutableLiveData<Boolean> IsDefaultSmsApp = new MutableLiveData<>();


    // READ_SMS permission
    private ActivityResultLauncher<String> requestReadSmsPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    if (ContextCompat.checkSelfPermission(homeContext, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                        ReadSmsPermissionGranted.setValue(true);
                        Toast.makeText(homeContext, "Permission granted", Toast.LENGTH_SHORT).show(); }
                } else { Toast.makeText(homeContext, "No Permission granted", Toast.LENGTH_SHORT).show(); }
            });
    // READ_CALL_LOG permission
    private ActivityResultLauncher<String> requestReadCallLogPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    if (ContextCompat.checkSelfPermission(homeContext, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                        ReadCallLogsPermissionGranted.setValue(true);
                        Toast.makeText(homeContext, "Permission granted", Toast.LENGTH_SHORT).show(); }
                } else { Toast.makeText(homeContext, "No Permission granted", Toast.LENGTH_SHORT).show(); }
            });
    // WRITE_CALL_LOG permission
    private ActivityResultLauncher<String> requestWriteCallLogPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    if (ContextCompat.checkSelfPermission(homeContext, Manifest.permission.WRITE_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                        WriteCallLogsPermissionGranted.setValue(true);
                        Toast.makeText(homeContext, "Permission granted", Toast.LENGTH_SHORT).show(); }
                    if (ContextCompat.checkSelfPermission(homeContext, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                        ReadCallLogsPermissionGranted.setValue(true);
                        Toast.makeText(homeContext, "Permission granted", Toast.LENGTH_SHORT).show(); }
                } else { Toast.makeText(homeContext, "No Permission granted", Toast.LENGTH_SHORT).show(); }
            });
    // READ_EXTERNAL_STORAGE permission
    private ActivityResultLauncher<String> requestReadExternalStoragePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    if (ContextCompat.checkSelfPermission(homeContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        ReadExternalStoragePermissionGranted.setValue(true);
                        Toast.makeText(homeContext, "Permission granted", Toast.LENGTH_SHORT).show(); }
                } else { Toast.makeText(homeContext, "No Permission granted", Toast.LENGTH_SHORT).show(); }
            });
    // WRITE_EXTERNAL_STORAGE permission
    private ActivityResultLauncher<String> requestWriteExternalStoragePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    if (ContextCompat.checkSelfPermission(homeContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        WriteExternalStoragePermissionGranted.setValue(true);
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
        //ReadSmsPermissionGranted = false;
        ReadSmsPermissionGranted.setValue(false);
        if (ActivityCompat.checkSelfPermission(homeContext, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            //ReadSmsPermissionGranted = true;
            ReadSmsPermissionGranted.setValue(true);
        } else {
            // requestReadSmsPermissionLauncher.launch(Manifest.permission.READ_SMS);
            String title = "Uprawnienia aplikacji";
            String message = "Aby można było wykonać kopię zapasową niezbędne są uprawnienia do odczytu wiadomości sms." +
                    "\n\nPo kliknięciu kontynuuj zostaniesz poproszony o nadanie uprawnienia do odczytu wiadomości sms dla tej aplikacji." +
                    "\n\nMożesz także zrezygnować co spowoduje, że kopia zapasowa wiadomości sms nie zostanie wykonana.";
            new AlertDialog.Builder(homeContext)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Kontynuuj", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            requestReadSmsPermissionLauncher.launch(Manifest.permission.READ_SMS);
                        }
                    })
                    .setNegativeButton("Zrezygnuj",null).create().show();
        }
        // READ_CALL_LOG permission
        //ReadCallLogsPermissionGranted = false;
        ReadCallLogsPermissionGranted.setValue(false);
        if (ActivityCompat.checkSelfPermission(homeContext, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            //ReadCallLogsPermissionGranted = true;
            ReadCallLogsPermissionGranted.setValue(true);
        } else {
            // requestReadCallLogPermissionLauncher.launch(Manifest.permission.READ_CALL_LOG);
            String title = "Uprawnienia aplikacji";
            String message = "Aby można było wykonać kopię zapasową niezbędne są uprawnienia do odczytu rejestru połączeń." +
                    "\n\nPo kliknięciu kontynuuj zostaniesz poproszony o nadanie uprawnienia do odczytu rejestru połączeń dla tej aplikacji." +
                    "\n\nMożesz także zrezygnować co spowoduje, że kopia zapasowa rejestru połączeń nie zostanie wykonana.";
            new AlertDialog.Builder(homeContext)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Kontynuuj", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            requestReadCallLogPermissionLauncher.launch(Manifest.permission.READ_CALL_LOG);
                        }
                    })
                    .setNegativeButton("Zrezygnuj",null).create().show();
        }
        // WRITE_CALL_LOG permission
        WriteCallLogsPermissionGranted.setValue(false);
        if (ActivityCompat.checkSelfPermission(homeContext, Manifest.permission.WRITE_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            WriteCallLogsPermissionGranted.setValue(true);
        } else {
            // requestWriteCallLogPermissionLauncher.launch(Manifest.permission.WRITE_CALL_LOG);
            String title = "Uprawnienia aplikacji";
            String message = "Aby można było wykonać kopię zapasową niezbędne są uprawnienia do zapisu rejestru połączeń." +
                    "\n\nPo kliknięciu kontynuuj zostaniesz poproszony o nadanie uprawnienia do zapisu rejestru połączeń dla tej aplikacji." +
                    "\n\nMożesz także zrezygnować co spowoduje, że kopia zapasowa rejestru połączeń nie zostanie przywrócona.";
            new AlertDialog.Builder(homeContext)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Kontynuuj", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            requestWriteCallLogPermissionLauncher.launch(Manifest.permission.WRITE_CALL_LOG);
                        }
                    })
                    .setNegativeButton("Zrezygnuj",null).create().show();
        }
        // READ_EXTERNAL_STORAGE permission
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
        ReadExternalStoragePermissionGranted.setValue(false);
            if (ActivityCompat.checkSelfPermission(homeContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                ReadExternalStoragePermissionGranted.setValue(true);
            } else {
                // requestReadExternalStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                String title = "Uprawnienia aplikacji";
                String message = "Aby można było wykonać kopię zapasową niezbędne są uprawnienia do odczytu z pamięci masowej." +
                        "\n\nPo kliknięciu kontynuuj zostaniesz poproszony o nadanie uprawnienia do odczytu z pamięci masowej dla tej aplikacji." +
                        "\n\nMożesz także zrezygnować co spowoduje, że kopia zapasowa nie zostanie wykonana.";
                new AlertDialog.Builder(homeContext)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("Kontynuuj", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                requestReadExternalStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                            }
                        })
                        .setNegativeButton("Zrezygnuj",null).create().show();
            }
        } else {
            //System.out.println("CAN READ: "+DocumentFile.fromTreeUri(homeContext,uriTree).canRead());
            //ReadExternalStoragePermissionGranted.setValue(uriTree != null);
            if (uriTree != Uri.EMPTY) {
                System.out.println("CAN READ: " + DocumentFile.fromTreeUri(homeContext, uriTree).canRead());
                ReadExternalStoragePermissionGranted.setValue(DocumentFile.fromTreeUri(homeContext, uriTree).canRead());
            } else {
                ReadExternalStoragePermissionGranted.setValue(false);
            }
        }
        // WRITE_EXTERNAL_STORAGE permission
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            WriteExternalStoragePermissionGranted.setValue(false);
            if (ActivityCompat.checkSelfPermission(homeContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                WriteExternalStoragePermissionGranted.setValue(true);
            } else {
                // requestWriteExternalStoragePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                String title = "Uprawnienia aplikacji";
                String message = "Aby można było wykonać kopię zapasową niezbędne są uprawnienia do zapisu w pamięci masowej." +
                        "\n\nPo kliknięciu kontynuuj zostaniesz poproszony o nadanie uprawnienia do zapisu w pamięci masowej dla tej aplikacji." +
                        "\n\nMożesz także zrezygnować co spowoduje, że kopia zapasowa nie zostanie wykonana.";
                new AlertDialog.Builder(homeContext)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("Kontynuuj", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                requestReadExternalStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                            }
                        })
                        .setNegativeButton("Zrezygnuj",null).create().show();
            }

        } else {
            //WriteExternalStoragePermissionGranted.setValue(uriTree != null);
            if (uriTree != Uri.EMPTY) {
                System.out.println("CAN WRITE: " + DocumentFile.fromTreeUri(homeContext, uriTree).canWrite());
                WriteExternalStoragePermissionGranted.setValue(DocumentFile.fromTreeUri(homeContext, uriTree).canWrite());
            } else {
                WriteExternalStoragePermissionGranted.setValue(false);
            }
        }



    }

    @Override
    public void onResume() {
        super.onResume();
        IsDefaultSmsApp.setValue(homeContext.getPackageName().equals(Telephony.Sms.getDefaultSmsPackage(homeContext)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        IsDefaultSmsApp.setValue(homeContext.getPackageName().equals(Telephony.Sms.getDefaultSmsPackage(homeContext)));

        restoreChoosedDirectoryFromAppPreferences();

        TextView textViewHomeLastBackupLocalizationAbout = view.findViewById(R.id.textViewHomeLastBackupLocalizationAbout);
        TextView textViewHomeLastSmsBackupAbout = view.findViewById(R.id.textViewHomeLastSmsBackupAbout);
        TextView textViewHomeLastCallLogBackupAbout = view.findViewById(R.id.textViewHomeLastCallLogBackupAbout);
        Button buttonHomeRestoreBackupNow = view.findViewById(R.id.buttonHomeRestoreBackupNow);

        if (uriTree != Uri.EMPTY) {
            ReadExternalStoragePermissionGranted.setValue(DocumentFile.fromTreeUri(homeContext, uriTree).canRead());
            WriteExternalStoragePermissionGranted.setValue(DocumentFile.fromTreeUri(homeContext, uriTree).canWrite());
            if (!ReadExternalStoragePermissionGranted.getValue() && !WriteExternalStoragePermissionGranted.getValue()) {
                String[] topDir = uriTree.toString().split("%3A");
                String dir = topDir[topDir.length-1].replace("%2F","/");
                String lastBackupLocalizationAbout = "Lokalizacja: "+dir;
                //textViewHomeLastBackupLocalizationAbout.setText(lastBackupLocalizationAbout);
                lastBackupLocalizationAbout += "\nBrak dostępu do pamięci masowej!\n\nProszę zweryfikować uprawnienia dostępu do pamięci masowej, lub wskazać inną lokalizację dla kopii zapasowych.";
                textViewHomeLastSmsBackupAbout.setVisibility(View.GONE);
                textViewHomeLastCallLogBackupAbout.setVisibility(View.GONE);
                buttonHomeRestoreBackupNow.setVisibility(View.GONE);
                textViewHomeLastBackupLocalizationAbout.setText(lastBackupLocalizationAbout);
            }
        } else {
            ReadExternalStoragePermissionGranted.setValue(false);
            WriteExternalStoragePermissionGranted.setValue(false);
            String lastBackupLocalizationAbout = textViewHomeLastBackupLocalizationAbout.getText()+"\n\nWybierz katalog, w którym chcesz przechowywać kopie zapasowe wiadomości sms i rejestru połączeń.";
            textViewHomeLastBackupLocalizationAbout.setText(lastBackupLocalizationAbout);
            textViewHomeLastSmsBackupAbout.setVisibility(View.GONE);
            textViewHomeLastCallLogBackupAbout.setVisibility(View.GONE);
            buttonHomeRestoreBackupNow.setText("Wybierz katalog");
            buttonHomeRestoreBackupNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_localizationFragment);
                }
            });
        }

        //

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

            choosedSmsBackupFile.observe(requireActivity(), new Observer<Uri>() {
                @Override
                public void onChanged(Uri uri) {
                    String backupFileName = DocumentFile.fromSingleUri(homeContext, choosedSmsBackupFile.getValue()).getName();
                    long epoch = DocumentFile.fromSingleUri(homeContext, choosedSmsBackupFile.getValue()).lastModified();
                    String backupLastModified = convertEpochIntoHumanReadableDatetime(epoch);
                    FileHandler fh = new FileHandler(homeContext);
                    int entries = fh.countEntriesInSmsXml(homeContext, choosedSmsBackupFile.getValue());
                    String pluralEntry = (entries >= 2 || entries == 0) ? "smsów" : "sms";
                    String text =
                            "Kopia zapasowa: " + backupFileName +
                                    "\nData ostatniej modyfikacji: " + backupLastModified +
                                    "\nWskazana kopia składa się z " + entries + " " + pluralEntry;
                    textViewHomeLastSmsBackupAbout.setText(text);
                }
            });

            choosedCallLogBackupFile.observe(requireActivity(), new Observer<Uri>() {
                @Override
                public void onChanged(Uri uri) {
                    String backupFileName = DocumentFile.fromSingleUri(homeContext, choosedCallLogBackupFile.getValue()).getName();
                    long epoch = DocumentFile.fromSingleUri(homeContext, choosedCallLogBackupFile.getValue()).lastModified();
                    String backupLastModified = convertEpochIntoHumanReadableDatetime(epoch);
                    FileHandler fh = new FileHandler(homeContext);
                    int entries = fh.countEntriesInCallLogXml(homeContext, choosedCallLogBackupFile.getValue());
                    String pluralEntry = (entries >= 2 || entries == 0) ? "połączeń" : "połączenia";
                    String text =
                            "Kopia zapasowa: " + backupFileName +
                                    "\nData ostatniej modyfikacji: " + backupLastModified +
                                    "\nWskazana kopia składa się z " + entries + " " + pluralEntry;
                    textViewHomeLastCallLogBackupAbout.setText(text);
                }
            });

            buttonHomeRestoreBackupNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_restoreFragment);
                }
            });
        }

        TextView textViewSmsCount = view.findViewById(R.id.textViewSmsCount);
        //if (ReadSmsPermissionGranted) { showCountofMessages(textViewSmsCount); }
        if (ReadSmsPermissionGranted.getValue()) { showCountofMessages(textViewSmsCount); }
        else { textViewSmsCount.setText(getResources().getString(R.string.textViewSmsCount)); }

        TextView textViewCallLogCount = view.findViewById(R.id.textViewCallLogCount);
        if (ReadCallLogsPermissionGranted.getValue()) { showCountofCallLog(textViewCallLogCount); }
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
            //if (ReadSmsPermissionGranted &&
            if (ReadSmsPermissionGranted.getValue() &&
                ReadCallLogsPermissionGranted.getValue() &&
                ReadExternalStoragePermissionGranted.getValue() &&
                WriteExternalStoragePermissionGranted.getValue()) { backup(switchBackupSms.isChecked(), switchBackupCallLog.isChecked()); }
        });

        ReadSmsPermissionGranted.observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (ReadSmsPermissionGranted.getValue()) {
                    switchBackupSms.setChecked(true);
                    switchBackupSms.setEnabled(true);
                } else {
                    switchBackupSms.setChecked(false);
                    switchBackupSms.setEnabled(false);
                }
            }
        });
        ReadCallLogsPermissionGranted.observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (ReadCallLogsPermissionGranted.getValue()) {
                    switchBackupCallLog.setChecked(true);
                    switchBackupCallLog.setEnabled(true);
                } else {
                    switchBackupCallLog.setChecked(false);
                    switchBackupCallLog.setEnabled(false);
                }
            }
        });

        IsDefaultSmsApp.observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                TextView textViewHomePermissionsAboutDefaultSmsApp = view.findViewById(R.id.textViewHomePermissionsAboutDefaultSmsApp);
                String homePermissionsAboutDefaultSmsAppText = getResources().getString(R.string.textViewHomePermissionsAboutDefaultSmsApp)+": "+((IsDefaultSmsApp.getValue())?"TAK":"NIE");
                textViewHomePermissionsAboutDefaultSmsApp.setText(homePermissionsAboutDefaultSmsAppText);
            }
        });

        ReadSmsPermissionGranted.observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                TextView textViewHomePermissionsAboutSmsAccess = view.findViewById(R.id.textViewHomePermissionsAboutSmsAccess);
                String homePermissionsAboutSmsAccessText = getResources().getString(R.string.textViewHomePermissionsAboutSmsAccess)+": "+((ReadSmsPermissionGranted.getValue())?"TAK":"NIE");
                textViewHomePermissionsAboutSmsAccess.setText(homePermissionsAboutSmsAccessText);
            }
        });

        ReadCallLogsPermissionGranted.observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                TextView textViewHomePermissionsAboutCallLogAccess = view.findViewById(R.id.textViewHomePermissionsAboutCallLogAccess);
                String homePermissionsAboutCallLogAccessText = getResources().getString(R.string.textViewHomePermissionsAboutCallLogAccess)+": "+((ReadCallLogsPermissionGranted.getValue()&&WriteCallLogsPermissionGranted.getValue()) ? "TAK" : "NIE");
                textViewHomePermissionsAboutCallLogAccess.setText(homePermissionsAboutCallLogAccessText);
            }
        });

        WriteCallLogsPermissionGranted.observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                TextView textViewHomePermissionsAboutCallLogAccess = view.findViewById(R.id.textViewHomePermissionsAboutCallLogAccess);
                String homePermissionsAboutCallLogAccessText = getResources().getString(R.string.textViewHomePermissionsAboutCallLogAccess)+": "+((ReadCallLogsPermissionGranted.getValue()&&WriteCallLogsPermissionGranted.getValue()) ? "TAK" : "NIE");
                textViewHomePermissionsAboutCallLogAccess.setText(homePermissionsAboutCallLogAccessText);
            }
        });

        ReadWriteExternalStoragePermissionGranted.setValue(ReadExternalStoragePermissionGranted.getValue() && WriteExternalStoragePermissionGranted.getValue());
        ReadWriteExternalStoragePermissionGranted.observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (ReadWriteExternalStoragePermissionGranted.getValue() && (ReadSmsPermissionGranted.getValue()||ReadCallLogsPermissionGranted.getValue())) {
                    buttonHomeMakeBackupNow.setEnabled(true);
                } else {
                    buttonHomeMakeBackupNow.setEnabled(false);
                }
            }
        });

        TextView textViewHomePermissionsAboutDefaultSmsApp = view.findViewById(R.id.textViewHomePermissionsAboutDefaultSmsApp);
        String homePermissionsAboutDefaultSmsAppText = getResources().getString(R.string.textViewHomePermissionsAboutDefaultSmsApp)+": "+((IsDefaultSmsApp.getValue())?"TAK":"NIE");
        textViewHomePermissionsAboutDefaultSmsApp.setText(homePermissionsAboutDefaultSmsAppText);

        TextView textViewHomePermissionsAboutSmsAccess = view.findViewById(R.id.textViewHomePermissionsAboutSmsAccess);
        String homePermissionsAboutSmsAccessText = getResources().getString(R.string.textViewHomePermissionsAboutSmsAccess)+": "+((ReadSmsPermissionGranted.getValue())?"TAK":"NIE");
        textViewHomePermissionsAboutSmsAccess.setText(homePermissionsAboutSmsAccessText);

        TextView textViewHomePermissionsAboutCallLogAccess = view.findViewById(R.id.textViewHomePermissionsAboutCallLogAccess);
        String homePermissionsAboutCallLogAccessText = getResources().getString(R.string.textViewHomePermissionsAboutCallLogAccess)+": "+((ReadCallLogsPermissionGranted.getValue()&&WriteCallLogsPermissionGranted.getValue()) ? "TAK" : "NIE");
        textViewHomePermissionsAboutCallLogAccess.setText(homePermissionsAboutCallLogAccessText);

        TextView textViewHomePermissionsAboutBackupLocation = view.findViewById(R.id.textViewHomePermissionsAboutBackupLocation);
        String homePermissionsAboutBackupLocationText = getResources().getString(R.string.textViewHomePermissionsAboutBackupLocation)+": "+((ReadWriteExternalStoragePermissionGranted.getValue()) ? "TAK" : "NIE");
        textViewHomePermissionsAboutBackupLocation.setText(homePermissionsAboutBackupLocationText);
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

        List<Uri> fileUriList = listDirectory(uriTree);
        lastBackupInChoosedDirectory(fileUriList);
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
                    if (DocumentFile.fromSingleUri(homeContext,uriFile).isFile()) {
                        uriList.add(uriFile);
                    } else if (DocumentFile.fromSingleUri(homeContext,uriFile).isDirectory()) {
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
            lastModified = DocumentFile.fromSingleUri(homeContext, uriFile).lastModified();
            //System.out.println(lastModified);
            // What to do when dates: lastModified, and creation are different?
            // Could this be a case in which backups are malformed?
            // Perhaps when user done backup, he would restore it on different time zone whatsoever
            // Nie wiem czemu mnie wzieło na pisanie w innym języku niż jedynym słusznym...
            // creation date from filename
            fileName = DocumentFile.fromSingleUri(homeContext, uriFile).getName();
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

//    public void restore() {
//        FileHandler fh = new FileHandler(homeContext);
//
//        // /storage/emulated/0/Documents/20220428151028-sms.xml
//        String SmsFileName = "20220428151028-sms.xml";
//        File smsXml = new File(
//                Environment.getExternalStorageDirectory() + "/Documents/", SmsFileName
//        );
//        List<List<SmsData>> smsDataLists = fh.restoreSmsFromXml(smsXml);
//        RestoreSms restoreSms = new RestoreSms(homeContext);
//        if (! homeContext.getPackageName().equals(Telephony.Sms.getDefaultSmsPackage(homeContext))) {
//            System.out.println("Na czas przywracania wiadomości ustaw ta aplikacje jako domyslna");
//            System.out.println("Następnie uruchom przywracanie wiadomości sms ponownie");
//            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS);
//            startActivity(intent);
//        }
//        //restoreSms.setAllSms(smsDataLists);
//        if (homeContext.getPackageName().equals(Telephony.Sms.getDefaultSmsPackage(homeContext)))
//        {
//            System.out.println("Wiadomości przywrócone, ustaw pierwotną aplikację jako domyślną");
//            Intent intent = new Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS);
//            startActivity(intent);
//        }
//
//        // /storage/emulated/0/Documents/20220428171724-calls.xml
//        String CallLogFileName = "20220428171724-calls.xml";
//        File callLogXml = new File(
//                Environment.getExternalStorageDirectory() + "/Documents/",
//                CallLogFileName
//        );
//        List<CallData> callDataList = fh.restoreCallLogFromXml(callLogXml);
//        RestoreCallLog restoreCallLog = new RestoreCallLog(homeContext);
//        //restoreCallLog.setAllCallLog(callDataList);
//        System.out.println("Rejestr połączeń przywrócony");
//        //restoreCallLog.deleteAllCallLog();
//        for (CallData callData : callDataList) {
//            System.out.println(callData.Number);
//        }
//
//    }

}
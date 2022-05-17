package com.pkprojekty.rikwo.Permissions;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Telephony;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;


public class Permissions {
    private final Fragment fragment;

    public Permissions(Fragment fragment) {
        this.fragment = fragment;

        // READ_SMS permission
        this.requestReadSmsPermissionLauncher =
                fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        if (hasReadSmsPermission()) {
                            ReadSmsPermissionGranted = true;
                            Toast.makeText(fragment.getContext(), "Permission granted", Toast.LENGTH_SHORT).show(); }
                    } else { Toast.makeText(fragment.getContext(), "No Permission granted", Toast.LENGTH_SHORT).show(); }
                });

        // READ_CALL_LOG permission
        this.requestReadCallLogPermissionLauncher =
                fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        if (hasReadCallLogsPermission()) {
                            ReadCallLogsPermissionGranted = true;
                            Toast.makeText(fragment.getContext(), "Permission granted", Toast.LENGTH_SHORT).show(); }
                    } else { Toast.makeText(fragment.getContext(), "No Permission granted", Toast.LENGTH_SHORT).show(); }
                });

        // WRITE_CALL_LOG permission
        this.requestWriteCallLogPermissionLauncher =
                fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        if (hasWriteCallLogsPermission()) {
                            WriteCallLogsPermissionGranted = true;
                            Toast.makeText(fragment.getContext(), "Permission granted", Toast.LENGTH_SHORT).show(); }
                    } else { Toast.makeText(fragment.getContext(), "No Permission granted", Toast.LENGTH_SHORT).show(); }
                });

        // READ_EXTERNAL_STORAGE permission
        this.requestReadExternalStoragePermissionLauncher =
                fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        if (hasReadExternalStoragePermission()) {
                            ReadExternalStoragePermissionGranted = true;
                            Toast.makeText(fragment.getContext(), "Permission granted", Toast.LENGTH_SHORT).show(); }
                    } else { Toast.makeText(fragment.getContext(), "No Permission granted", Toast.LENGTH_SHORT).show(); }
                });

        // WRITE_EXTERNAL_STORAGE permission
        this.requestWriteExternalStoragePermissionLauncher =
                fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        if (hasWriteExternalStoragePermission()) {
                            WriteExternalStoragePermissionGranted = true;
                            Toast.makeText(fragment.getContext(), "Permission granted", Toast.LENGTH_SHORT).show(); }
                    } else { Toast.makeText(fragment.getContext(), "No Permission granted", Toast.LENGTH_SHORT).show(); }
                });

        // All needed permissions
        this.requestAllPermissionsLauncher =
                fragment.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    if (result.containsValue(false)) {
                        Toast.makeText(fragment.getContext(), "Not all permission granted", Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < requestedAllPermissions.size(); i++) {
                            System.out.println(requestedAllPermissions.get(i) + ": " + result.get(requestedAllPermissions.get(i)));
                        }
                    } else {
                        Toast.makeText(fragment.getContext(), "All permissions granted", Toast.LENGTH_SHORT).show();
                    }
                });

    }
// ----------------------------------------
    public boolean isDefaultSmsApp() {
        return fragment.requireActivity().getPackageName().equals(
                Telephony.Sms.getDefaultSmsPackage(fragment.requireContext())
        );
    }

// ----------------------------------------
    private boolean ReadSmsPermissionGranted;

    private ActivityResultLauncher<String> requestReadSmsPermissionLauncher;

    public boolean hasReadSmsPermission() {
        return ActivityCompat.checkSelfPermission(
                fragment.requireContext(), Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private final DialogInterface.OnClickListener showDialogReadSmsPermissionListner =
            (dialogInterface, i) -> requestReadSmsPermissionLauncher.launch(
                    Manifest.permission.READ_SMS
            );

    public void askForReadSmsPermission() {
        if (!hasReadSmsPermission()) {
            if (fragment.shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
                String title = "Uprawnienia aplikacji";
                String message = "Aby można było wykonać kopię zapasową niezbędne są uprawnienia do odczytu wiadomości sms." +
                        "\n\nPo kliknięciu kontynuuj zostaniesz poproszony o nadanie uprawnienia do odczytu wiadomości sms dla tej aplikacji." +
                        "\n\nMożesz także zrezygnować co spowoduje, że kopia zapasowa wiadomości sms nie zostanie wykonana.";
                new AlertDialog.Builder(fragment.requireContext())
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("Kontynuuj", showDialogReadSmsPermissionListner)
                        .setNegativeButton("Zrezygnuj", null).create().show();
            } else {
                requestReadSmsPermissionLauncher.launch(Manifest.permission.READ_SMS);
            }
        } else {
            if (!ReadSmsPermissionGranted) {
                ReadSmsPermissionGranted = true;
            }
        }
    }

// ----------------------------------------
    private boolean ReadCallLogsPermissionGranted;

    private ActivityResultLauncher<String> requestReadCallLogPermissionLauncher;

    public boolean hasReadCallLogsPermission() {
        return ActivityCompat.checkSelfPermission(
                fragment.requireContext(), Manifest.permission.READ_CALL_LOG
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private final DialogInterface.OnClickListener showDialogReadCallLogsPermissionListner =
            (dialogInterface, i) -> requestReadCallLogPermissionLauncher.launch(
                    Manifest.permission.READ_CALL_LOG
            );

    public void askForReadCallLogPermission() {
        if (!hasReadCallLogsPermission()) {
            if (fragment.shouldShowRequestPermissionRationale(Manifest.permission.READ_CALL_LOG)) {
                String title = "Uprawnienia aplikacji";
                String message = "Aby można było wykonać kopię zapasową niezbędne są uprawnienia do odczytu rejestru połączeń." +
                        "\n\nPo kliknięciu kontynuuj zostaniesz poproszony o nadanie uprawnienia do odczytu rejestru połączeń dla tej aplikacji." +
                        "\n\nMożesz także zrezygnować co spowoduje, że kopia zapasowa rejestru połączeń nie zostanie wykonana.";
                new AlertDialog.Builder(fragment.requireContext())
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("Kontynuuj", showDialogReadCallLogsPermissionListner)
                        .setNegativeButton("Zrezygnuj",null).create().show();
            } else {
                requestReadCallLogPermissionLauncher.launch(Manifest.permission.READ_CALL_LOG);
            }
        } else {
            if (!ReadCallLogsPermissionGranted) {
                ReadCallLogsPermissionGranted = true;
            }
        }
    }

// ----------------------------------------
    private boolean WriteCallLogsPermissionGranted;

    private ActivityResultLauncher<String> requestWriteCallLogPermissionLauncher;

    public boolean hasWriteCallLogsPermission() {
        return ActivityCompat.checkSelfPermission(
                fragment.requireContext(), Manifest.permission.WRITE_CALL_LOG
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private final DialogInterface.OnClickListener showDialogWriteCallLogsPermissionListner =
            (dialogInterface, i) -> requestWriteCallLogPermissionLauncher.launch(
                    Manifest.permission.WRITE_CALL_LOG
            );

    public void askForWriteCallLogPermission() {
        if (!hasWriteCallLogsPermission()) {
            if (fragment.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CALL_LOG)) {
                String title = "Uprawnienia aplikacji";
                String message = "Aby można było wykonać kopię zapasową niezbędne są uprawnienia do odczytu rejestru połączeń." +
                        "\n\nPo kliknięciu kontynuuj zostaniesz poproszony o nadanie uprawnienia do odczytu rejestru połączeń dla tej aplikacji." +
                        "\n\nMożesz także zrezygnować co spowoduje, że kopia zapasowa rejestru połączeń nie zostanie wykonana.";
                new AlertDialog.Builder(fragment.requireContext())
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("Kontynuuj", showDialogWriteCallLogsPermissionListner)
                        .setNegativeButton("Zrezygnuj",null).create().show();
            } else {
                requestWriteCallLogPermissionLauncher.launch(Manifest.permission.WRITE_CALL_LOG);
            }
        } else {
            if (!WriteCallLogsPermissionGranted) {
                WriteCallLogsPermissionGranted = true;
            }
        }
    }

// ----------------------------------------
    private boolean ReadExternalStoragePermissionGranted;

    private ActivityResultLauncher<String> requestReadExternalStoragePermissionLauncher;

    public boolean hasReadExternalStoragePermission() {
        return ActivityCompat.checkSelfPermission(
                fragment.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private final DialogInterface.OnClickListener showDialogReadExternalStoragePermissionListner =
            (dialogInterface, i) -> requestReadExternalStoragePermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE
            );

    public void askForReadExternalStoragePermission() {
        if (!hasReadExternalStoragePermission()) {
            if (fragment.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                String title = "Uprawnienia aplikacji";
                String message = "Aby można było wykonać kopię zapasową niezbędne są uprawnienia do odczytu z pamięci masowej." +
                        "\n\nPo kliknięciu kontynuuj zostaniesz poproszony o nadanie uprawnienia do odczytu z pamięci masowej dla tej aplikacji." +
                        "\n\nMożesz także zrezygnować co spowoduje, że kopia zapasowa nie zostanie wykonana.";
                new AlertDialog.Builder(fragment.requireContext())
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("Kontynuuj", showDialogReadExternalStoragePermissionListner)
                        .setNegativeButton("Zrezygnuj", null).create().show();
            } else {
            requestReadExternalStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else {
            if (!ReadExternalStoragePermissionGranted) {
                ReadExternalStoragePermissionGranted = true;
            }
        }
    }

// ----------------------------------------
    private boolean WriteExternalStoragePermissionGranted;

    private ActivityResultLauncher<String> requestWriteExternalStoragePermissionLauncher;

    public boolean hasWriteExternalStoragePermission() {
        return ActivityCompat.checkSelfPermission(
                fragment.requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private final DialogInterface.OnClickListener showDialogWriteExternalStoragePermissionListner =
            (dialogInterface, i) -> requestWriteExternalStoragePermissionLauncher.launch(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            );

    public void askForWriteExternalStoragePermission() {
        if (!hasWriteExternalStoragePermission()) {
            if (fragment.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                String title = "Uprawnienia aplikacji";
                String message = "Aby można było wykonać kopię zapasową niezbędne są uprawnienia do zapisu w pamięci masowej." +
                        "\n\nPo kliknięciu kontynuuj zostaniesz poproszony o nadanie uprawnienia do zapisu w pamięci masowej dla tej aplikacji." +
                        "\n\nMożesz także zrezygnować co spowoduje, że kopia zapasowa nie zostanie wykonana.";
                new AlertDialog.Builder(fragment.requireContext())
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("Kontynuuj", showDialogWriteExternalStoragePermissionListner)
                        .setNegativeButton("Zrezygnuj", null).create().show();
            } else {
                requestWriteExternalStoragePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        } else {
            if (!WriteExternalStoragePermissionGranted) {
                WriteExternalStoragePermissionGranted = true;
            }
        }
    }

// ----------------------------------------
    // potrzebne uprawnienia to:
    // - odczyt zapis pamięć masowa
    // - odczyt zapis sms
    // - odczyt zapis rejestru połączeń
    private ActivityResultLauncher<String[]> requestAllPermissionsLauncher;

    private final List<String> requestedAllPermissions = new ArrayList<>();
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_SMS,
//            Manifest.permission.READ_CALL_LOG
//            Manifest.permission.WRITE_CALL_LOG

    private final DialogInterface.OnClickListener showDialogAllPermissionsListner =
            (dialogInterface, i) -> requestAllPermissionsLauncher.launch(requestedAllPermissions.toArray(new String[0]));

    public void askForAllPermissions() {
        boolean shouldShowReadSmsPermissionRationale = false;
        boolean shouldShowReadCallLogPermissionRationale = false;
        boolean shouldShowWriteCallLogPermissionRationale = false;
        boolean shouldShowReadExternalStoragePermissionRationale = false;
        boolean shouldShowWriteExternalStoragePermissionRationale = false;
        if (!hasReadSmsPermission())  {
            shouldShowReadSmsPermissionRationale = fragment.shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS);
            requestedAllPermissions.add(Manifest.permission.READ_SMS);
        }
        if (!hasReadCallLogsPermission())  {
            shouldShowReadCallLogPermissionRationale = fragment.shouldShowRequestPermissionRationale(Manifest.permission.READ_CALL_LOG);
            requestedAllPermissions.add(Manifest.permission.READ_CALL_LOG);
        }
        if (!hasWriteCallLogsPermission())  {
            shouldShowWriteCallLogPermissionRationale = fragment.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CALL_LOG);
            requestedAllPermissions.add(Manifest.permission.WRITE_CALL_LOG);
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            if (!hasReadExternalStoragePermission()) {
                shouldShowReadExternalStoragePermissionRationale = fragment.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                requestedAllPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (!hasWriteExternalStoragePermission()) {
                shouldShowWriteExternalStoragePermissionRationale = fragment.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                requestedAllPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
        if (!requestedAllPermissions.isEmpty()) {
            String permissionsRationaleMessage = "W celu wykonywania i przywracania kopii zapasowych wiadomości sms i rejestru połączeń aplikacja potrzebuje dostępu do: \n";
            if (shouldShowReadSmsPermissionRationale) {
                permissionsRationaleMessage += " - wiadomości sms\n";
            }
            if (shouldShowReadCallLogPermissionRationale || shouldShowWriteCallLogPermissionRationale) {
                permissionsRationaleMessage += " - rejestru połączeń\n";
            }
            if (shouldShowReadExternalStoragePermissionRationale || shouldShowWriteExternalStoragePermissionRationale) {
                permissionsRationaleMessage += " - pamięci wewnętrznej telefonu\n";
            }
            if (shouldShowReadSmsPermissionRationale ||
                shouldShowReadCallLogPermissionRationale ||
                shouldShowWriteCallLogPermissionRationale ||
                shouldShowReadExternalStoragePermissionRationale ||
                shouldShowWriteExternalStoragePermissionRationale) {
                String title = "Uprawnienia aplikacji";
                new AlertDialog.Builder(fragment.requireContext())
                        .setTitle(title)
                        .setMessage(permissionsRationaleMessage)
                        .setPositiveButton("Kontynuuj", showDialogAllPermissionsListner)
                        .setNegativeButton("Zrezygnuj", null).create().show();
            } else {
                requestAllPermissionsLauncher.launch(requestedAllPermissions.toArray(new String[0]));
            }
        }
    }

}

// https://developer.android.com/training/permissions/requesting
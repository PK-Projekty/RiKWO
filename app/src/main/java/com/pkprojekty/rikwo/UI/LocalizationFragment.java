package com.pkprojekty.rikwo.UI;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.pkprojekty.rikwo.Permissions.Permissions;
import com.pkprojekty.rikwo.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocalizationFragment extends Fragment {
    Permissions permissions = new Permissions(this);

    private Context localizationContext;
    private Uri uriTree = Uri.EMPTY;

    MutableLiveData<String> currentChoosedDir = new MutableLiveData<>();
    // https://stackoverflow.com/questions/14457711/android-listening-for-variable-changes

    MutableLiveData<Boolean> ReadExternalStoragePermissionGranted = new MutableLiveData<>();
    MutableLiveData<Boolean> WriteExternalStoragePermissionGranted = new MutableLiveData<>();

    ActivityResultLauncher<Intent> chooseLocalDirectoryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Uri uri = Objects.requireNonNull(result.getData()).getData();
                    if (uri != null) {
                        System.out.println("Przed zachowaniem uprawnień: "+ requireActivity().getContentResolver().getPersistedUriPermissions());
                        requireActivity().getContentResolver().takePersistableUriPermission(uri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION |
                                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        System.out.println("Po zachowaniu uprawnień: " + requireActivity().getContentResolver().getPersistedUriPermissions());
                        uriTree = result.getData().getData();
                        storeChoosedDirectoryInAppPreferences(uriTree);
                        String[] topDir = uriTree.toString().split("%3A");
                        String dir = topDir[topDir.length-1].replace("%2F","/");
                        System.out.println(dir);
                        if (dir.isEmpty()) {
                            currentChoosedDir.setValue(getResources().getString(R.string.textViewCurrentlyChoosedLocalDirectory));
                        } else {
                            currentChoosedDir.setValue("Obecnie wybrany katalog to: " + dir);
                        }
                    }
                }
            });

    public LocalizationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        localizationContext = context;
        System.out.println("LocalizationFragment onAttach HIT");

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
        System.out.println("LocalizationFragment onResume HIT");

        // read from app preferences location uri choosed by user
        restoreChoosedDirectoryFromAppPreferences();

        //check app permissions
        checkPermissions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_localization, container, false);
        System.out.println("LocalizationFragment onCreateView HIT");

        // read from app preferences location uri choosed by user
        restoreChoosedDirectoryFromAppPreferences();

        //check app permissions
        checkPermissions();

        // section: Restore backup
        chooseLocalBackupLocation(view);

        return view;
    }

    public void checkPermissions() {
        if (uriTree != Uri.EMPTY) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                if (DocumentFile.fromTreeUri(localizationContext, uriTree) != null) {
                    boolean canBeRead = Objects.requireNonNull(DocumentFile.fromTreeUri(localizationContext, uriTree)).canRead();
                    if (permissions.hasReadExternalStoragePermission() && canBeRead) {
                        ReadExternalStoragePermissionGranted.setValue(true);
                    }
                }
            } else {
                boolean canBeRead = Objects.requireNonNull(DocumentFile.fromTreeUri(localizationContext, uriTree)).canRead();
                ReadExternalStoragePermissionGranted.setValue(canBeRead);
            }
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                if (DocumentFile.fromTreeUri(localizationContext, uriTree) != null) {
                    boolean canBeWrite = Objects.requireNonNull(DocumentFile.fromTreeUri(localizationContext, uriTree)).canWrite();
                    if (permissions.hasWriteExternalStoragePermission() && canBeWrite) {
                        WriteExternalStoragePermissionGranted.setValue(true);
                    }
                }
            } else {
                boolean canBeWrite = Objects.requireNonNull(DocumentFile.fromTreeUri(localizationContext, uriTree)).canWrite();
                WriteExternalStoragePermissionGranted.setValue(canBeWrite);
            }
        } else {
            ReadExternalStoragePermissionGranted.setValue(false);
            WriteExternalStoragePermissionGranted.setValue(false);
        }
    }

    private void chooseLocalBackupLocation(View view) {
        TextView textViewCurrentlyChoosedLocalDirectory = view.findViewById(R.id.textViewCurrentlyChoosedLocalDirecotry);
        System.out.println(uriTree);
        String[] topDir = uriTree.toString().split("%3A");
        String dir = topDir[topDir.length-1].replace("%2F","/");
        System.out.println(dir);
        if (dir.isEmpty()) {
            currentChoosedDir.setValue(getResources().getString(R.string.textViewCurrentlyChoosedLocalDirectory));
        } else {
            currentChoosedDir.setValue("Obecnie wybrany katalog to: " + dir);
        }
        currentChoosedDir.observe(requireActivity(), textViewCurrentlyChoosedLocalDirectory::setText);

        SwitchMaterial switchBackupLocalizationLocal = view.findViewById(R.id.switchBackupLocalizationLocal);
        if (ReadExternalStoragePermissionGranted.getValue() == null) {
            ReadExternalStoragePermissionGranted.setValue(false);
        }
        if (WriteExternalStoragePermissionGranted.getValue() == null) {
            WriteExternalStoragePermissionGranted.setValue(false);
        }
        if (ReadExternalStoragePermissionGranted.getValue() && WriteExternalStoragePermissionGranted.getValue()) {
            restoreSwitchStateFromAppPreferences(switchBackupLocalizationLocal);
        }
        Button buttonChooseBackupLocalDirectory = view.findViewById(R.id.buttonChooseBackupLocalDirectory);
        buttonChooseBackupLocalDirectory.setEnabled(switchBackupLocalizationLocal.isChecked());
        buttonChooseBackupLocalDirectory.setOnClickListener(view1 -> {
                    Uri initialDirectoryForBackupFiles = Uri.parse("/storage/emulated/0/Documents/");
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, initialDirectoryForBackupFiles);
                    chooseLocalDirectoryLauncher.launch(intent);
                }
        );
        switchBackupLocalizationLocal.setOnCheckedChangeListener((compoundButton, b) -> {
            storeSwitchStateInAppPreferences(switchBackupLocalizationLocal);
            buttonChooseBackupLocalDirectory.setEnabled(switchBackupLocalizationLocal.isChecked());
        });
    }

    private void restoreChoosedDirectoryFromAppPreferences() {
        SharedPreferences preferences = requireActivity().getPreferences(MODE_PRIVATE);
        if (preferences.contains("uriTree")) {
            uriTree = Uri.parse(preferences.getString("uriTree",""));
        }
    }

    private void storeChoosedDirectoryInAppPreferences(Uri value) {
        SharedPreferences preferences = requireActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("uriTree", String.valueOf(value));
        editor.apply();
    }

    private void restoreSwitchStateFromAppPreferences(SwitchMaterial switchMaterial) {
        SharedPreferences preferences = requireActivity().getPreferences(MODE_PRIVATE);
        if (preferences.contains("switchBackupLocalizationLocal")) {
            boolean switchState = preferences.getBoolean("switchBackupLocalizationLocal",false);
            switchMaterial.setChecked(switchState);
        }
    }

    private void storeSwitchStateInAppPreferences(SwitchMaterial switchMaterial) {
        SharedPreferences preferences = requireActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("switchBackupLocalizationLocal",switchMaterial.isChecked());
        editor.apply();
    }


}
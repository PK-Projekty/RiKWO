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

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.pkprojekty.rikwo.Permissions.Permissions;
import com.pkprojekty.rikwo.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class PermissionsFragment extends Fragment {
    Permissions permissions = new Permissions(this);

    private Context permissionsContext;
    private Uri uriTree = Uri.EMPTY;

    MutableLiveData<Boolean> ReadSmsPermissionGranted = new MutableLiveData<>();
    MutableLiveData<Boolean> ReadCallLogsPermissionGranted = new MutableLiveData<>();
    MutableLiveData<Boolean> WriteCallLogsPermissionGranted = new MutableLiveData<>();
    MutableLiveData<Boolean> ReadExternalStoragePermissionGranted = new MutableLiveData<>();
    MutableLiveData<Boolean> WriteExternalStoragePermissionGranted = new MutableLiveData<>();
    MutableLiveData<Boolean> IsDefaultSmsApp = new MutableLiveData<>();

    public PermissionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        permissionsContext=context;
        System.out.println("PermissionsFragment onAttach HIT");

        // read from app preferences location uri choosed by user
        restoreChoosedDirectoryFromAppPreferences();

        //check app permissions
        checkPermissions();
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("PermissionsFragment onResume HIT");

        // read from app preferences location uri choosed by user
        restoreChoosedDirectoryFromAppPreferences();

        //check app permissions
        checkPermissions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_permissions, container, false);
        System.out.println("PermissionsFragment onCreateView HIT");

        // read from app preferences location uri choosed by user
        restoreChoosedDirectoryFromAppPreferences();

        //check app permissions
        checkPermissions();

        // section: sms permission
        updateSmsSwitchesState(view);

        // section: call log permission
        updateCallLogSwitchesState(view);

        // section: external memory permission
        updateMemorySwitchesState(view);


        return view;
    }

    public void updateSmsSwitchesState(View view) {
        SwitchMaterial switchPermissionsSmsRead = view.findViewById(R.id.switchPermissionsSmsRead);
        switchPermissionsSmsRead.setEnabled(false);
        ReadSmsPermissionGranted.observe(requireActivity(), aBoolean -> {
            if (aBoolean) {
                switchPermissionsSmsRead.setChecked(true);
                switchPermissionsSmsRead.setEnabled(false);
            } else {
                switchPermissionsSmsRead.setChecked(false);
                switchPermissionsSmsRead.setEnabled(true);
            }
        });
        switchPermissionsSmsRead.setOnClickListener(view1 -> {
            if (ReadSmsPermissionGranted.getValue() == null) { ReadSmsPermissionGranted.setValue(false); }
            if (!ReadSmsPermissionGranted.getValue()) {
                permissions.askForReadSmsPermission();
            }
        });
        switchPermissionsSmsRead.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!permissions.hasReadSmsPermission()) {
                switchPermissionsSmsRead.setChecked(false);
            }
        });
        SwitchMaterial switchPermissionsSmsDefault = view.findViewById(R.id.switchPermissionsSmsDefault);
        switchPermissionsSmsDefault.setEnabled(false);
        IsDefaultSmsApp.observe(requireActivity(), aBoolean -> {
            if (aBoolean) {
                switchPermissionsSmsDefault.setChecked(true);
                switchPermissionsSmsDefault.setEnabled(true);
            } else {
                switchPermissionsSmsDefault.setChecked(false);
                switchPermissionsSmsDefault.setEnabled(false);
            }
        });
        switchPermissionsSmsDefault.setOnClickListener(view12 -> {
            if (IsDefaultSmsApp.getValue() == null) { IsDefaultSmsApp.setValue(false); }
            if (IsDefaultSmsApp.getValue()) {
                String defaultSmsApp = restoreDefaultSmsAppNameFromAppPreferences();
                permissions.askForDefaultSmsApp(defaultSmsApp);
            }
        });
        switchPermissionsSmsDefault.setOnCheckedChangeListener((compoundButton, b) -> switchPermissionsSmsDefault.setChecked(permissions.isDefaultSmsApp()));
    }

    private String restoreDefaultSmsAppNameFromAppPreferences() {
        SharedPreferences preferences = requireActivity().getPreferences(MODE_PRIVATE);
        String defaultSmsAppName = "";
        if (preferences.contains("defaultSmsApp")) {
            defaultSmsAppName = preferences.getString("defaultSmsApp","");
        }
        return defaultSmsAppName;
    }

    public void updateCallLogSwitchesState(View view) {
        SwitchMaterial switchPermissionsCallLogRead = view.findViewById(R.id.switchPermissionsCallLogRead);
        ReadCallLogsPermissionGranted.observe(requireActivity(), aBoolean -> {
            if (aBoolean) {
                switchPermissionsCallLogRead.setChecked(true);
                switchPermissionsCallLogRead.setEnabled(false);
            } else {
                switchPermissionsCallLogRead.setChecked(false);
                switchPermissionsCallLogRead.setEnabled(true);
            }
        });
        switchPermissionsCallLogRead.setOnClickListener(view1 -> {
            if (ReadCallLogsPermissionGranted.getValue() == null) { ReadCallLogsPermissionGranted.setValue(false); }
            if (!ReadCallLogsPermissionGranted.getValue()) {
                permissions.askForReadCallLogPermission();
            }
        });
        switchPermissionsCallLogRead.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!permissions.hasReadCallLogsPermission()) {
                switchPermissionsCallLogRead.setChecked(false);
            }
        });
        SwitchMaterial switchPermissionsCallLogWrite = view.findViewById(R.id.switchPermissionsCallLogWrite);
        WriteCallLogsPermissionGranted.observe(requireActivity(), aBoolean -> {
            if (aBoolean) {
                switchPermissionsCallLogWrite.setChecked(true);
                switchPermissionsCallLogWrite.setEnabled(false);
            } else {
                switchPermissionsCallLogWrite.setChecked(false);
                switchPermissionsCallLogWrite.setEnabled(true);
            }
        });
        switchPermissionsCallLogWrite.setOnClickListener(view12 -> {
            if (WriteCallLogsPermissionGranted.getValue() == null) { WriteCallLogsPermissionGranted.setValue(false); }
            if (!WriteCallLogsPermissionGranted.getValue()) {
                permissions.askForWriteCallLogPermission();
            }
        });
        switchPermissionsCallLogWrite.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!permissions.hasWriteCallLogsPermission()) {
                switchPermissionsCallLogWrite.setChecked(false);
            }
        });
    }

    public void updateMemorySwitchesState(View view) {
        SwitchMaterial switchPermissionsMemoryRead = view.findViewById(R.id.switchPermissionsMemoryRead);
        ReadExternalStoragePermissionGranted.observe(requireActivity(), aBoolean -> {
            if (aBoolean) {
                switchPermissionsMemoryRead.setChecked(true);
                switchPermissionsMemoryRead.setEnabled(false);
            } else {
                switchPermissionsMemoryRead.setChecked(false);
                switchPermissionsMemoryRead.setEnabled(true);
            }
        });
        switchPermissionsMemoryRead.setOnClickListener(view1 -> {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                if (ReadExternalStoragePermissionGranted.getValue() == null) { ReadExternalStoragePermissionGranted.setValue(false);}
                if (!ReadExternalStoragePermissionGranted.getValue()) {
                    // Brak uprawnień odczytu do pamięci telefonu
                    if (!permissions.hasReadExternalStoragePermission()) {
                        permissions.askForReadExternalStoragePermission();
                    }
                    if (uriTree != Uri.EMPTY) {
                        // Brak uprawnień odczytu do wskazanej lokalizacji
                        if (!Objects.requireNonNull(DocumentFile.fromTreeUri(permissionsContext, uriTree)).canRead()) {
                            Navigation.findNavController(view1).navigate(R.id.action_permissionsFragment_to_localizationFragment);
                        }
                    } else {
                        // Lokalizacja kopii zapasowych nie została wskazana
                        Navigation.findNavController(view1).navigate(R.id.action_permissionsFragment_to_localizationFragment);
                    }
                }
            } else {
                Navigation.findNavController(view1).navigate(R.id.action_permissionsFragment_to_localizationFragment);
            }
        });
        switchPermissionsMemoryRead.setOnCheckedChangeListener((compoundButton, b) -> {
            if (uriTree != Uri.EMPTY) {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                    if (!permissions.hasReadExternalStoragePermission() && !Objects.requireNonNull(DocumentFile.fromTreeUri(permissionsContext, uriTree)).canRead()) {
                        switchPermissionsMemoryRead.setChecked(false);
                    }
                } else {
                    if (!Objects.requireNonNull(DocumentFile.fromTreeUri(permissionsContext, uriTree)).canRead()) {
                        switchPermissionsMemoryRead.setChecked(false);
                    }
                }
            }
        });
        SwitchMaterial switchPermissionsMemoryWrite = view.findViewById(R.id.switchPermissionsMemoryWrite);
        WriteExternalStoragePermissionGranted.observe(requireActivity(), aBoolean -> {
            if (aBoolean) {
                switchPermissionsMemoryWrite.setChecked(true);
                switchPermissionsMemoryWrite.setEnabled(false);
            } else {
                switchPermissionsMemoryWrite.setChecked(false);
                switchPermissionsMemoryWrite.setEnabled(true);
            }
        });
        switchPermissionsMemoryWrite.setOnClickListener(view12 -> {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                if (WriteExternalStoragePermissionGranted.getValue() == null) { WriteExternalStoragePermissionGranted.setValue(false); }
                if (!WriteExternalStoragePermissionGranted.getValue()) {
                    // Brak uprawnień zapisu do pamięci telefonu
                    if (!permissions.hasWriteExternalStoragePermission()) {
                        permissions.askForWriteExternalStoragePermission();
                    }
                    if (uriTree != Uri.EMPTY) {
                        // Brak uprawnień zapisu do wskazanej lokalizacji
                        if (!Objects.requireNonNull(DocumentFile.fromTreeUri(permissionsContext, uriTree)).canWrite()) {
                            Navigation.findNavController(view12).navigate(R.id.action_permissionsFragment_to_localizationFragment);
                        }
                    } else {
                        // Lokalizacja kopii zapasowych nie została wskazana
                        Navigation.findNavController(view12).navigate(R.id.action_permissionsFragment_to_localizationFragment);
                    }
                }
            } else {
                Navigation.findNavController(view12).navigate(R.id.action_permissionsFragment_to_localizationFragment);
            }
        });
        switchPermissionsMemoryWrite.setOnCheckedChangeListener((compoundButton, b) -> {
            if (uriTree != Uri.EMPTY) {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    if (!permissions.hasWriteExternalStoragePermission() && !Objects.requireNonNull(DocumentFile.fromTreeUri(permissionsContext, uriTree)).canWrite()) {
                        switchPermissionsMemoryWrite.setChecked(false);
                    }
                } else {
                    if (!Objects.requireNonNull(DocumentFile.fromTreeUri(permissionsContext, uriTree)).canWrite()) {
                        switchPermissionsMemoryWrite.setChecked(false);
                    }
                }
            }
        });
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
                ReadExternalStoragePermissionGranted.setValue(permissions.hasReadExternalStoragePermission() && Objects.requireNonNull(DocumentFile.fromTreeUri(permissionsContext, uriTree)).canRead());
            } else {
                ReadExternalStoragePermissionGranted.setValue(Objects.requireNonNull(DocumentFile.fromTreeUri(permissionsContext, uriTree)).canRead());
            }
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                WriteExternalStoragePermissionGranted.setValue(permissions.hasWriteExternalStoragePermission() && Objects.requireNonNull(DocumentFile.fromTreeUri(permissionsContext, uriTree)).canWrite());
            } else {
                WriteExternalStoragePermissionGranted.setValue(Objects.requireNonNull(DocumentFile.fromTreeUri(permissionsContext, uriTree)).canWrite());
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
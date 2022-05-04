package com.pkprojekty.rikwo;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocalizationFragment extends Fragment {
    private Uri uriTree = Uri.EMPTY;
    MutableLiveData<String> currentChoosedDir = new MutableLiveData<>();
    // https://stackoverflow.com/questions/14457711/android-listening-for-variable-changes

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
                            currentChoosedDir.setValue(getResources().getString(R.string.textViewSmsCount));
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_localization, container, false);

        restoreChoosedDirectoryFromAppPreferences();

        SwitchMaterial switchBackupLocalizationLocal = view.findViewById(R.id.switchBackupLocalizationLocal);
        restoreSwitchStateFromAppPreferences(switchBackupLocalizationLocal);
        switchBackupLocalizationLocal.setOnCheckedChangeListener((compoundButton, b) -> storeSwitchStateInAppPreferences(switchBackupLocalizationLocal));

        TextView textViewCurrentlyChoosedLocalDirectory = view.findViewById(R.id.textViewCurrentlyChoosedLocalDirecotry);
        System.out.println(uriTree);
        String[] topDir = uriTree.toString().split("%3A");
        String dir = topDir[topDir.length-1].replace("%2F","/");
        System.out.println(dir);
        if (dir.isEmpty()) {
            switchBackupLocalizationLocal.setEnabled(false);
            currentChoosedDir.setValue(getResources().getString(R.string.textViewSmsCount));
        } else {
            switchBackupLocalizationLocal.setEnabled(true);
            currentChoosedDir.setValue("Obecnie wybrany katalog to: " + dir);
        }
        currentChoosedDir.observe(requireActivity(), text -> textViewCurrentlyChoosedLocalDirectory.setText(text));

        Button buttonChooseBackupLocalDirectory = view.findViewById(R.id.buttonChooseBackupLocalDirectory);
        buttonChooseBackupLocalDirectory.setOnClickListener(view1 -> {
                    Uri initialDirectoryForBackupFiles = Uri.parse("/storage/emulated/0/Documents/");
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, initialDirectoryForBackupFiles);
                    chooseLocalDirectoryLauncher.launch(intent);
                }
        );

        return view;
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
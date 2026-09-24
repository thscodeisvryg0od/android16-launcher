package com.example.launcher16;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final SettingsManager sm = new SettingsManager(this);

        Switch glanceSwitch = findViewById(R.id.glanceSwitch);
        Switch addIconsSwitch = findViewById(R.id.addIconsSwitch);
        Switch labelsSwitch = findViewById(R.id.labelsSwitch);
        RadioButton col4 = findViewById(R.id.col4);
        RadioButton col5 = findViewById(R.id.col5);
        RadioButton col6 = findViewById(R.id.col6);
        RadioButton szSmall = findViewById(R.id.sizeSmall);
        RadioButton szMedium = findViewById(R.id.sizeMedium);
        RadioButton szLarge = findViewById(R.id.sizeLarge);
        Spinner langSpinner = findViewById(R.id.langSpinner);

        glanceSwitch.setChecked(sm.getAtAGlance());
        addIconsSwitch.setChecked(sm.getAddIcons());
        labelsSwitch.setChecked(sm.getShowLabels());

        int cols = sm.getColumns();
        if (cols == 4) col4.setChecked(true);
        else if (cols == 6) col6.setChecked(true);
        else col5.setChecked(true);

        int size = sm.getIconSize();
        if (size <= 60) szSmall.setChecked(true);
        else if (size >= 88) szLarge.setChecked(true);
        else szMedium.setChecked(true);

        // Language spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langSpinner.setAdapter(adapter);

        String savedLang = sm.getLanguage();
        int langPos = 0;
        if ("tr".equals(savedLang)) langPos = 1;
        else if ("system".equals(savedLang)) langPos = 2;
        final int finalLangPos = langPos;
        langSpinner.post(() -> langSpinner.setSelection(finalLangPos, false));

        Button save = findViewById(R.id.saveBtn);
        save.setOnClickListener(v -> {
            sm.setAtAGlance(glanceSwitch.isChecked());
            sm.setAddIcons(addIconsSwitch.isChecked());
            sm.setShowLabels(labelsSwitch.isChecked());
            sm.setColumns(col4.isChecked() ? 4 : (col6.isChecked() ? 6 : 5));
            sm.setIconSize(szSmall.isChecked() ? 56 : (szLarge.isChecked() ? 88 : 72));

            String[] langCodes = {"en", "tr", "system"};
            int pos = langSpinner.getSelectedItemPosition();
            if (pos >= 0 && pos < langCodes.length) {
                sm.setLanguage(langCodes[pos]);
            }

            Toast.makeText(SettingsActivity.this,
                    "Settings saved", Toast.LENGTH_SHORT).show();

            // Return to home screen
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(home);
            finish();
        });
    }
}
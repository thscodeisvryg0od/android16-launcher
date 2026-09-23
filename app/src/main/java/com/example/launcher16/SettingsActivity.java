package com.example.launcher16;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SettingsManager sm = new SettingsManager(this);

        // At a Glance
        Switch glanceSwitch = findViewById(R.id.glanceSwitch);
        glanceSwitch.setChecked(sm.getAtAGlance());

        // Add icons
        Switch addIconsSwitch = findViewById(R.id.addIconsSwitch);
        addIconsSwitch.setChecked(sm.getAddIcons());

        // Labels
        Switch labelsSwitch = findViewById(R.id.labelsSwitch);
        labelsSwitch.setChecked(sm.getShowLabels());

        // Columns
        RadioButton col4 = findViewById(R.id.col4);
        RadioButton col5 = findViewById(R.id.col5);
        RadioButton col6 = findViewById(R.id.col6);
        int cols = sm.getColumns();
        if (cols == 4) col4.setChecked(true);
        else if (cols == 6) col6.setChecked(true);
        else col5.setChecked(true);

        // Icon size
        RadioButton szSmall = findViewById(R.id.sizeSmall);
        RadioButton szMedium = findViewById(R.id.sizeMedium);
        RadioButton szLarge = findViewById(R.id.sizeLarge);
        int size = sm.getIconSize();
        if (size <= 60) szSmall.setChecked(true);
        else if (size >= 88) szLarge.setChecked(true);
        else szMedium.setChecked(true);

        // Language spinner — SET INITIAL SELECTION
        Spinner langSpinner = findViewById(R.id.langSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langSpinner.setAdapter(adapter);

        String savedLang = sm.getLanguage();
        int langPos = 0;
        if (savedLang.equals("tr")) langPos = 1;
        else if (savedLang.equals("system")) langPos = 2;
        langSpinner.setSelection(langPos);

        // Save button
        Button save = findViewById(R.id.saveBtn);
        save.setOnClickListener(v -> {
            sm.setAtAGlance(glanceSwitch.isChecked());
            sm.setAddIcons(addIconsSwitch.isChecked());
            sm.setShowLabels(labelsSwitch.isChecked());
            sm.setColumns(col4.isChecked() ? 4 : (col6.isChecked() ? 6 : 5));
            sm.setIconSize(szSmall.isChecked() ? 56 : (szLarge.isChecked() ? 88 : 72));

            String[] langCodes = {"en", "tr", "system"};
            int pos = langSpinner.getSelectedItemPosition();
            if (pos >= 0 && pos < langCodes.length) sm.setLanguage(langCodes[pos]);

            // Restart activity to apply language
            android.content.Intent intent = getIntent();
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });
    }
}
package com.example.launcher16;

import android.content.Intent;
import android.os.Bundle;
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
        Switch clockSwitch = findViewById(R.id.clockSwitch);
        Switch addIconsSwitch = findViewById(R.id.addIconsSwitch);
        Switch newsSwitch = findViewById(R.id.newsSwitch);
        Switch labelsSwitch = findViewById(R.id.labelsSwitch);
        Switch hiddenSwitch = findViewById(R.id.hiddenSwitch);
        RadioButton col4 = findViewById(R.id.col4);
        RadioButton col5 = findViewById(R.id.col5);
        RadioButton col6 = findViewById(R.id.col6);
        RadioButton szSmall = findViewById(R.id.sizeSmall);
        RadioButton szMedium = findViewById(R.id.sizeMedium);
        RadioButton szLarge = findViewById(R.id.sizeLarge);
        RadioButton dock3 = findViewById(R.id.dock3);
        RadioButton dock4 = findViewById(R.id.dock4);
        RadioButton dock5 = findViewById(R.id.dock5);
        Spinner langSpinner = findViewById(R.id.langSpinner);

        glanceSwitch.setChecked(sm.getAtAGlance());
        clockSwitch.setChecked(sm.getShowClock());
        addIconsSwitch.setChecked(sm.getAddIcons());
        newsSwitch.setChecked(sm.getNewsSwipe());
        labelsSwitch.setChecked(sm.getShowLabels());
        hiddenSwitch.setChecked(sm.getShowHidden());

        int cols = sm.getColumns();
        if (cols == 4) col4.setChecked(true);
        else if (cols == 6) col6.setChecked(true);
        else col5.setChecked(true);

        int size = sm.getIconSize();
        if (size <= 60) szSmall.setChecked(true);
        else if (size >= 88) szLarge.setChecked(true);
        else szMedium.setChecked(true);

        int dc = sm.getDockCount();
        if (dc == 3) dock3.setChecked(true);
        else if (dc == 4) dock4.setChecked(true);
        else dock5.setChecked(true);

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
            sm.setShowClock(clockSwitch.isChecked());
            sm.setAddIcons(addIconsSwitch.isChecked());
            sm.setNewsSwipe(newsSwitch.isChecked());
            sm.setShowLabels(labelsSwitch.isChecked());
            sm.setShowHidden(hiddenSwitch.isChecked());
            sm.setColumns(col4.isChecked() ? 4 : (col6.isChecked() ? 6 : 5));
            sm.setIconSize(szSmall.isChecked() ? 56 : (szLarge.isChecked() ? 88 : 72));
            sm.setDockCount(dock3.isChecked() ? 3 : (dock4.isChecked() ? 4 : 5));

            String[] langCodes = {"en", "tr", "system"};
            int pos = langSpinner.getSelectedItemPosition();
            if (pos >= 0 && pos < langCodes.length) {
                sm.setLanguage(langCodes[pos]);
            }

            Toast.makeText(SettingsActivity.this,
                    getString(R.string.settings_saved), Toast.LENGTH_SHORT).show();

            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(home);
            finish();
        });
    }
}
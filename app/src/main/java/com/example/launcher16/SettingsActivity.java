package com.example.launcher16;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Switch;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SettingsManager sm = new SettingsManager(this);

        RadioButton col4 = findViewById(R.id.col4);
        RadioButton col5 = findViewById(R.id.col5);
        RadioButton col6 = findViewById(R.id.col6);
        int cols = sm.getColumns();
        if (cols == 4) col4.setChecked(true);
        else if (cols == 6) col6.setChecked(true);
        else col5.setChecked(true);

        RadioButton sizeSmall = findViewById(R.id.sizeSmall);
        RadioButton sizeMedium = findViewById(R.id.sizeMedium);
        RadioButton sizeLarge = findViewById(R.id.sizeLarge);
        int size = sm.getIconSize();
        if (size <= 56) sizeSmall.setChecked(true);
        else if (size >= 88) sizeLarge.setChecked(true);
        else sizeMedium.setChecked(true);

        Switch showLabels = findViewById(R.id.showLabels);
        showLabels.setChecked(sm.getShowLabels());

        Button save = findViewById(R.id.saveBtn);
        save.setOnClickListener(v -> {
            int newCols = col4.isChecked() ? 4 : (col6.isChecked() ? 6 : 5);
            sm.setColumns(newCols);

            int newSize = sizeSmall.isChecked() ? 56 : (sizeLarge.isChecked() ? 88 : 72);
            sm.setIconSize(newSize);

            sm.setShowLabels(showLabels.isChecked());

            finish();
        });
    }
}

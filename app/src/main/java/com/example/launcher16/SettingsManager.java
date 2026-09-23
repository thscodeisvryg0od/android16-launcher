package com.example.launcher16;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    private static final String PREFS = "launcher16_prefs";
    private static final String KEY_COLUMNS = "columns";
    private static final String KEY_ICON_SIZE = "icon_size";
    private static final String KEY_SHOW_LABELS = "show_labels";

    private final SharedPreferences prefs;

    public SettingsManager(Context ctx) {
        prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public int getColumns() { return prefs.getInt(KEY_COLUMNS, 5); }
    public void setColumns(int v) { prefs.edit().putInt(KEY_COLUMNS, v).apply(); }

    public int getIconSize() { return prefs.getInt(KEY_ICON_SIZE, 72); }
    public void setIconSize(int v) { prefs.edit().putInt(KEY_ICON_SIZE, v).apply(); }

    public boolean getShowLabels() { return prefs.getBoolean(KEY_SHOW_LABELS, true); }
    public void setShowLabels(boolean v) { prefs.edit().putBoolean(KEY_SHOW_LABELS, v).apply(); }
}

package com.example.launcher16;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    private static final String PREFS = "pulse_prefs";

    private static final String KEY_COLUMNS = "columns";
    private static final String KEY_ICON_SIZE = "icon_size";
    private static final String KEY_SHOW_LABELS = "show_labels";
    private static final String KEY_AT_GLANCE = "at_a_glance";
    private static final String KEY_ADD_ICONS = "add_icons";
    private static final String KEY_LANGUAGE = "language";

    private final SharedPreferences prefs;

    public SettingsManager(Context ctx) {
        prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public int getColumns() { return prefs.getInt(KEY_COLUMNS, 5); }
    public void setColumns(int v) { prefs.edit().putInt(KEY_COLUMNS, v).commit(); }

    public int getIconSize() { return prefs.getInt(KEY_ICON_SIZE, 72); }
    public void setIconSize(int v) { prefs.edit().putInt(KEY_ICON_SIZE, v).commit(); }

    public boolean getShowLabels() { return prefs.getBoolean(KEY_SHOW_LABELS, true); }
    public void setShowLabels(boolean v) { prefs.edit().putBoolean(KEY_SHOW_LABELS, v).commit(); }

    public boolean getAtAGlance() { return prefs.getBoolean(KEY_AT_GLANCE, true); }
    public void setAtAGlance(boolean v) { prefs.edit().putBoolean(KEY_AT_GLANCE, v).commit(); }

    public boolean getAddIcons() { return prefs.getBoolean(KEY_ADD_ICONS, false); }
    public void setAddIcons(boolean v) { prefs.edit().putBoolean(KEY_ADD_ICONS, v).commit(); }

    public String getLanguage() { return prefs.getString(KEY_LANGUAGE, "en"); }
    public void setLanguage(String v) { prefs.edit().putString(KEY_LANGUAGE, v).commit(); }
}
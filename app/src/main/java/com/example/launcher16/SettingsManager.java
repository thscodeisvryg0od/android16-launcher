package com.example.launcher16;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class SettingsManager {
    private static final String PREFS = "pulse_prefs";

    private static final String KEY_COLUMNS = "columns";
    private static final String KEY_ICON_SIZE = "icon_size";
    private static final String KEY_SHOW_LABELS = "show_labels";
    private static final String KEY_AT_GLANCE = "at_a_glance";
    private static final String KEY_ADD_ICONS = "add_icons";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_DOCK_COUNT = "dock_count";
    private static final String KEY_NEWS_SWIPE = "news_swipe";
    private static final String KEY_SHOW_CLOCK = "show_clock";
    private static final String KEY_SHOW_HIDDEN = "show_hidden";
    private static final String KEY_HIDDEN_APPS = "hidden_apps";

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

    public int getDockCount() { return prefs.getInt(KEY_DOCK_COUNT, 5); }
    public void setDockCount(int v) { prefs.edit().putInt(KEY_DOCK_COUNT, v).commit(); }

    public boolean getNewsSwipe() { return prefs.getBoolean(KEY_NEWS_SWIPE, false); }
    public void setNewsSwipe(boolean v) { prefs.edit().putBoolean(KEY_NEWS_SWIPE, v).commit(); }

    public boolean getShowClock() { return prefs.getBoolean(KEY_SHOW_CLOCK, true); }
    public void setShowClock(boolean v) { prefs.edit().putBoolean(KEY_SHOW_CLOCK, v).commit(); }

    public boolean getShowHidden() { return prefs.getBoolean(KEY_SHOW_HIDDEN, false); }
    public void setShowHidden(boolean v) { prefs.edit().putBoolean(KEY_SHOW_HIDDEN, v).commit(); }

    public Set<String> getHiddenApps() {
        return prefs.getStringSet(KEY_HIDDEN_APPS, new HashSet<>());
    }

    public void hideApp(String pkg) {
        Set<String> set = new HashSet<>(getHiddenApps());
        set.add(pkg);
        prefs.edit().putStringSet(KEY_HIDDEN_APPS, set).commit();
    }

    public void unhideApp(String pkg) {
        Set<String> set = new HashSet<>(getHiddenApps());
        set.remove(pkg);
        prefs.edit().putStringSet(KEY_HIDDEN_APPS, set).commit();
    }

    public boolean isHidden(String pkg) {
        return getHiddenApps().contains(pkg);
    }
}
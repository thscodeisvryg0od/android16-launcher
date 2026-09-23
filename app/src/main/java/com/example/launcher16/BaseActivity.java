package com.example.launcher16;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class BaseActivity extends Activity {

    @Override
    protected void attachBaseContext(Context newBase) {
        SettingsManager sm = new SettingsManager(newBase);
        String lang = sm.getLanguage();
        if (lang == null || lang.equals("system")) {
            super.attachBaseContext(newBase);
            return;
        }
        try {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.setLocale(locale);
            Context localized = newBase.createConfigurationContext(config);
            super.attachBaseContext(localized);
        } catch (Exception e) {
            super.attachBaseContext(newBase);
        }
    }
}

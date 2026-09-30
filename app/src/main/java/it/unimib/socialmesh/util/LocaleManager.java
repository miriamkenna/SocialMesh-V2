package it.unimib.socialmesh.util;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

public class LocaleManager {
    private Resources resources;
    private Context ct;
    public LocaleManager(Context ctx)
    {
        this.ct = ctx;
        this.resources = ct.getResources();
    }
    public void updateResource(String code){
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Resources resources = ct.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
    }

    public String getCurrentLanguage() {
        Configuration config = resources.getConfiguration();
        return config.locale.getLanguage();
    }
}
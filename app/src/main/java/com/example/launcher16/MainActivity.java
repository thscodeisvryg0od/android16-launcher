package com.example.launcher16;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.launcher16.icons.DockView;
import com.example.launcher16.icons.SearchBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private GridView appGrid;
    private AppAdapter adapter;
    private final List<AppInfo> appList = new ArrayList<>();
    private final List<AppInfo> allApps = new ArrayList<>();
    private SearchBarView searchBar;
    private DockView dockView;
    private SettingsManager settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = new SettingsManager(this);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER,
                WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(12), dp(60), dp(12), dp(24));

        // Arama çubuğu
        searchBar = new SearchBarView(this);
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(56));
        sp.bottomMargin = dp(24);
        root.addView(searchBar, sp);

        // Uzun bas → Ayarlar
        searchBar.setOnLongClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        });

        // Yazı değişince filtrele
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {
                filterApps(s.toString());
            }
        });

        // Enter'a bas → ilk sonucu aç
        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (!appList.isEmpty()) {
                AppInfo app = appList.get(0);
                Intent i = getPackageManager().getLaunchIntentForPackage(app.packageName);
                if (i != null) startActivity(i);
            }
            return true;
        });

        // Grid
        appGrid = new GridView(this);
        appGrid.setNumColumns(settings.getColumns());
        appGrid.setVerticalSpacing(dp(28));
        appGrid.setHorizontalSpacing(dp(4));
        appGrid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        appGrid.setSelector(android.R.color.transparent);
        LinearLayout.LayoutParams gp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
        root.addView(appGrid, gp);

        // Dock
        dockView = new DockView(this);
        LinearLayout.LayoutParams dp2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(88));
        dp2.topMargin = dp(16);
        root.addView(dockView, dp2);

        setContentView(root);

        loadApps();
        adapter = new AppAdapter(this, appList, settings.getIconSize(), settings.getShowLabels());
        appGrid.setAdapter(adapter);
        appGrid.setOnItemClickListener((parent, view, position, id) -> {
            AppInfo app = appList.get(position);
            Intent i = getPackageManager().getLaunchIntentForPackage(app.packageName);
            if (i != null) startActivity(i);
        });

        appGrid.setOnItemLongClickListener((parent, view, position, id) -> {
            AppInfo app = appList.get(position);
            Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.setData(Uri.parse("package:" + app.packageName));
            startActivity(i);
            return true;
        });

        dockView.setApps(getDockApps(), app -> {
            Intent i = getPackageManager().getLaunchIntentForPackage(app.packageName);
            if (i != null) startActivity(i);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ayarlar değişmiş olabilir, grid'i yeniden yapılandır
        if (adapter != null) {
            appGrid.setNumColumns(settings.getColumns());
            adapter = new AppAdapter(this, appList, settings.getIconSize(), settings.getShowLabels());
            appGrid.setAdapter(adapter);
        }
    }

    private void loadApps() {
        PackageManager pm = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolved = pm.queryIntentActivities(mainIntent, 0);

        allApps.clear();
        for (ResolveInfo ri : resolved) {
            if (ri.activityInfo.packageName.equals(getPackageName())) continue;
            AppInfo info = new AppInfo();
            info.label = ri.loadLabel(pm).toString();
            info.packageName = ri.activityInfo.packageName;
            info.icon = ri.loadIcon(pm);
            allApps.add(info);
        }

        appList.clear();
        appList.addAll(allApps);
    }

    private void filterApps(String query) {
        appList.clear();
        if (query.trim().isEmpty()) {
            appList.addAll(allApps);
        } else {
            String lower = query.toLowerCase().trim();
            for (AppInfo a : allApps) {
                if (a.label.toLowerCase().contains(lower)) {
                    appList.add(a);
                }
            }
        }
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    private List<AppInfo> getDockApps() {
        List<AppInfo> dock = new ArrayList<>();
        String[] favorites = {
                "com.android.dialer", "com.android.mms",
                "com.android.chrome", "com.android.camera2",
                "com.google.android.apps.messaging"
        };
        for (String pkg : favorites) {
            for (AppInfo a : allApps) {
                if (a.packageName.equals(pkg) && !dock.contains(a)) {
                    dock.add(a);
                    break;
                }
            }
            if (dock.size() >= 5) break;
        }
        if (dock.isEmpty()) {
            dock.addAll(allApps.subList(0, Math.min(5, allApps.size())));
        }
        return dock;
    }

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density);
    }
}
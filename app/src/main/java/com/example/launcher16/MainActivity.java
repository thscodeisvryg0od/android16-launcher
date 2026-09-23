package com.example.launcher16;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
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
    private SearchBarView searchBar;
    private DockView dockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER,
                WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(60), dp(16), dp(24));

        searchBar = new SearchBarView(this);
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(56));
        sp.bottomMargin = dp(24);
        root.addView(searchBar, sp);

        appGrid = new GridView(this);
        appGrid.setNumColumns(4);
        appGrid.setVerticalSpacing(dp(20));
        appGrid.setHorizontalSpacing(dp(8));
        appGrid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        appGrid.setSelector(android.R.color.transparent);
        LinearLayout.LayoutParams gp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
        root.addView(appGrid, gp);

        dockView = new DockView(this);
        LinearLayout.LayoutParams dp2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(88));
        dp2.topMargin = dp(16);
        root.addView(dockView, dp2);

        setContentView(root);

        loadApps();
        adapter = new AppAdapter(this, appList);
        appGrid.setAdapter(adapter);
        appGrid.setOnItemClickListener((parent, view, position, id) -> {
            AppInfo app = appList.get(position);
            Intent i = getPackageManager().getLaunchIntentForPackage(app.packageName);
            if (i != null) startActivity(i);
        });

        dockView.setApps(getDockApps(), app -> {
            Intent i = getPackageManager().getLaunchIntentForPackage(app.packageName);
            if (i != null) startActivity(i);
        });
    }

    private void loadApps() {
        PackageManager pm = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolved = pm.queryIntentActivities(mainIntent, 0);
        appList.clear();
        for (ResolveInfo ri : resolved) {
            AppInfo info = new AppInfo();
            info.label = ri.loadLabel(pm).toString();
            info.packageName = ri.activityInfo.packageName;
            info.icon = ri.loadIcon(pm);
            appList.add(info);
        }
    }

    private List<AppInfo> getDockApps() {
        List<AppInfo> dock = new ArrayList<>();
        String[] favorites = {
                "com.android.dialer", "com.android.mms",
                "com.android.chrome", "com.android.camera2"
        };
        for (String pkg : favorites) {
            for (AppInfo a : appList) {
                if (a.packageName.equals(pkg)) { dock.add(a); break; }
            }
        }
        if (dock.isEmpty()) {
            dock.addAll(appList.subList(0, Math.min(4, appList.size())));
        }
        return dock;
    }

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density);
    }
}

package com.example.launcher16;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.launcher16.icons.AtAGlanceView;
import com.example.launcher16.icons.DockView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private final List<AppInfo> allApps = new ArrayList<>();
    private DockView dockView;
    private SettingsManager settings;
    private GestureDetector gesture;

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

        FrameLayout root = new FrameLayout(this);
        root.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(16), dp(60), dp(16), dp(24));

        // At a Glance
        if (settings.getAtAGlance()) {
            AtAGlanceView glance = new AtAGlanceView(this);
            LinearLayout.LayoutParams gp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, dp(72));
            gp.bottomMargin = dp(8);
            content.addView(glance, gp);
        }

        // Spacer
        View spacer = new View(this);
        content.addView(spacer, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));

        // Dock
        dockView = new DockView(this);
        LinearLayout.LayoutParams dp2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(88));
        dp2.topMargin = dp(16);
        content.addView(dockView, dp2);

        root.addView(content);
        setContentView(root);

        loadApps();

        dockView.setApps(getDockApps(), app -> {
            Intent i = getPackageManager().getLaunchIntentForPackage(app.packageName);
            if (i != null) startActivity(i);
        });

        // Swipe up → App Drawer
        gesture = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float vx, float vy) {
                if (e1 != null && e2 != null && (e1.getY() - e2.getY()) > 120) {
                    startActivity(new Intent(MainActivity.this, AppDrawerActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                }
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                showHomeMenu();
            }
        });

        root.setOnTouchListener((v, ev) -> gesture.onTouchEvent(ev));
    }

    @Override
    public void onBackPressed() {
        // Home screen - do nothing
    }

    private void showHomeMenu() {
        String[] options = {
                getString(R.string.menu_wallpaper),
                getString(R.string.menu_widgets),
                getString(R.string.menu_apps),
                getString(R.string.menu_settings)
        };

        new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert)
                .setItems(options, (d, which) -> {
                    switch (which) {
                        case 0:
                            try {
                                Intent wp = new Intent(Intent.ACTION_SET_WALLPAPER);
                                startActivity(Intent.createChooser(wp, "Wallpaper"));
                            } catch (Exception ignored) {}
                            break;
                        case 1:
                            startActivity(new Intent(MainActivity.this, AppDrawerActivity.class));
                            break;
                        case 2:
                            startActivity(new Intent(MainActivity.this, AppDrawerActivity.class));
                            break;
                        case 3:
                            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                            break;
                    }
                })
                .show();
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

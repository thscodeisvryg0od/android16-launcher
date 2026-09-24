package com.example.launcher16;

import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Insets;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.launcher16.icons.AtAGlanceView;
import com.example.launcher16.icons.DockView;
import com.example.launcher16.icons.SearchBarView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private final List<AppInfo> allApps = new ArrayList<>();
    private DockView dockView;
    private SettingsManager settings;
    private LinearLayout content;

    private float downY = 0f;
    private float downX = 0f;
    private boolean longPressFired = false;
    private boolean swipeConsumed = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable longPressRunnable;

    private static final int LONG_PRESS_MS = 500;

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

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        // Initial padding — will be updated by insets listener
        content.setPadding(dp(16), dp(56), dp(16), dp(16));

        if (settings.getAtAGlance()) {
            AtAGlanceView glance = new AtAGlanceView(this);
            LinearLayout.LayoutParams gp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, dp(72));
            content.addView(glance, gp);
        }

        View spacer = new View(this);
        content.addView(spacer, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));

        dockView = new DockView(this);
        LinearLayout.LayoutParams dp1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(72));
        dp1.bottomMargin = dp(8);
        content.addView(dockView, dp1);

        SearchBarView searchBar = new SearchBarView(this);
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(52));
        content.addView(searchBar, sp);

        searchBar.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com")));
            } catch (Exception ignored) {}
        });

        root.addView(content);
        setContentView(root);

        // Apply system bar insets to avoid overlap
        root.setOnApplyWindowInsetsListener((v, insets) -> {
            int top, bottom;
            if (Build.VERSION.SDK_INT >= 30) {
                Insets bars = insets.getInsets(WindowInsets.Type.systemBars());
                top = bars.top;
                bottom = bars.bottom;
            } else {
                top = insets.getSystemWindowInsetTop();
                bottom = insets.getSystemWindowInsetBottom();
            }
            content.setPadding(dp(16), top + dp(12), dp(16), bottom + dp(8));
            return insets;
        });
        root.requestApplyInsets();

        loadApps();

        dockView.setApps(getDockApps(), app -> {
            Intent i = getPackageManager().getLaunchIntentForPackage(app.packageName);
            if (i != null) startActivity(i);
        });
    }

    // ===== GESTURE HANDLING =====

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                downX = ev.getX();
                longPressFired = false;
                swipeConsumed = false;
                longPressRunnable = this::showHomeMenu;
                handler.postDelayed(longPressRunnable, LONG_PRESS_MS);
                break;

            case MotionEvent.ACTION_MOVE:
                float dy = ev.getY() - downY;
                float dx = Math.abs(ev.getX() - downX);
                if (Math.abs(dy) > 40 || dx > 40) {
                    handler.removeCallbacks(longPressRunnable);
                }
                // Swipe down → notification panel
                if (!swipeConsumed && dy > 180 && dx < 150) {
                    swipeConsumed = true;
                    openNotificationPanel();
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
                handler.removeCallbacks(longPressRunnable);
                float upDy = ev.getY() - downY;
                float upDx = ev.getX() - downX;
                // Swipe up → App Drawer
                if (!longPressFired && !swipeConsumed
                        && upDy < -150 && Math.abs(upDx) < 200) {
                    startActivity(new Intent(this, AppDrawerActivity.class));
                    return true;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                handler.removeCallbacks(longPressRunnable);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() { /* Home screen — do nothing */ }

    // ===== LONG-PRESS MENU =====

    private void showHomeMenu() {
        longPressFired = true;
        String[] options = {
                getString(R.string.menu_wallpaper),
                getString(R.string.menu_widgets),
                getString(R.string.menu_settings)
        };
        new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert)
                .setItems(options, (d, which) -> {
                    switch (which) {
                        case 0: openWallpaperPicker(); break;
                        case 1: openWidgetPicker(); break;
                        case 2:
                            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                            break;
                    }
                })
                .show();
    }

    private void openWallpaperPicker() {
        try {
            Intent wp = new Intent(Intent.ACTION_SET_WALLPAPER);
            startActivity(Intent.createChooser(wp, "Wallpaper"));
        } catch (Exception e) {
            android.widget.Toast.makeText(this, "Wallpaper not available",
                    android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    private void openWidgetPicker() {
        // Try multiple intents until one works
        Intent[] attempts = new Intent[] {
                new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK),
                new Intent("android.appwidget.action.APPWIDGET_PICK"),
                new Intent("android.appwidget.action.APPWIDGET_CONFIGURE")
        };
        for (Intent intent : attempts) {
            try {
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                    return;
                }
            } catch (Exception ignored) {}
        }
        android.widget.Toast.makeText(this,
                "Widget picker not available on this device.\nComing in v6.",
                android.widget.Toast.LENGTH_LONG).show();
    }

    private void openNotificationPanel() {
        try {
            Object service = getSystemService("statusbar");
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method expand;
            if (Build.VERSION.SDK_INT >= 17) {
                expand = statusBarManager.getMethod("expandNotificationsPanel");
            } else {
                expand = statusBarManager.getMethod("expand");
            }
            expand.invoke(service);
        } catch (Exception e) {
            // Fallback: open quick settings tile if possible
            try {
                Object service = getSystemService("statusbar");
                Class<?> cls = Class.forName("android.app.StatusBarManager");
                Method m = cls.getMethod("expandSettingsPanel");
                m.invoke(service);
            } catch (Exception ignored) {}
        }
    }

    // ===== APP LOADING =====

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
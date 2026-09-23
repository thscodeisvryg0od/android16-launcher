package com.example.launcher16;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class AppDrawerActivity extends BaseActivity {

    private GridView grid;
    private AppGridAdapter adapter;
    private final List<AppInfo> allApps = new ArrayList<>();
    private final List<AppInfo> filtered = new ArrayList<>();
    private SettingsManager settings;

    private float downY = 0f;
    private boolean dismissFired = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable dismissRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = new SettingsManager(this);

        getWindow().setStatusBarColor(0xFF1A1A1A);
        getWindow().setNavigationBarColor(0xFF1A1A1A);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(0xFF1A1A1A);
        root.setPadding(dp(12), dp(48), dp(12), dp(16));

        EditText search = new EditText(this);
        search.setHint(R.string.search_hint);
        search.setHintTextColor(0xFF888888);
        search.setTextColor(0xFFFFFFFF);
        search.setSingleLine(true);
        search.setBackgroundColor(0xFF2A2A2A);
        search.setPadding(dp(20), dp(12), dp(20), dp(12));
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(52));
        sp.bottomMargin = dp(16);
        root.addView(search, sp);

        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {
                filter(s.toString());
            }
        });

        grid = new GridView(this);
        grid.setNumColumns(settings.getColumns());
        grid.setVerticalSpacing(dp(20));
        grid.setHorizontalSpacing(dp(4));
        grid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        grid.setSelector(android.R.color.transparent);
        root.addView(grid, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));

        setContentView(root);

        loadApps();
        adapter = new AppGridAdapter(this, filtered,
                settings.getIconSize(), settings.getShowLabels());
        grid.setAdapter(adapter);
        grid.setOnItemClickListener((parent, view, position, id) -> {
            AppInfo app = filtered.get(position);
            Intent i = getPackageManager().getLaunchIntentForPackage(app.packageName);
            if (i != null) startActivity(i);
        });
        grid.setOnItemLongClickListener((parent, view, position, id) -> {
            AppInfo app = filtered.get(position);
            Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.setData(Uri.parse("package:" + app.packageName));
            startActivity(i);
            return true;
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                dismissFired = false;
                dismissRunnable = () -> { /* no auto dismiss */ };
                break;

            case MotionEvent.ACTION_UP:
                float dy = ev.getY() - downY;
                if (!dismissFired && dy > 200) {
                    dismissFired = true;
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
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
        filtered.clear();
        filtered.addAll(allApps);
    }

    private void filter(String q) {
        filtered.clear();
        if (q.trim().isEmpty()) {
            filtered.addAll(allApps);
        } else {
            String lower = q.toLowerCase().trim();
            for (AppInfo a : allApps) {
                if (a.label.toLowerCase().contains(lower)) filtered.add(a);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density);
    }
}
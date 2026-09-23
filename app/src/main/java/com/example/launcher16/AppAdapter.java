package com.example.launcher16;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.launcher16.icons.AppIconView;

import java.util.List;

public class AppAdapter extends BaseAdapter {

    private final Context ctx;
    private final List<AppInfo> apps;
    private final int iconSizeDp;
    private final boolean showLabels;

    public AppAdapter(Context ctx, List<AppInfo> apps, int iconSizeDp, boolean showLabels) {
        this.ctx = ctx;
        this.apps = apps;
        this.iconSizeDp = iconSizeDp;
        this.showLabels = showLabels;
    }

    @Override public int getCount() { return apps.size(); }
    @Override public Object getItem(int i) { return apps.get(i); }
    @Override public long getItemId(int i) { return i; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppInfo app = apps.get(position);

        LinearLayout container = new LinearLayout(ctx);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setGravity(Gravity.CENTER);
        container.setPadding(dp(2), dp(4), dp(2), dp(4));

        AppIconView iconView = new AppIconView(ctx);
        iconView.setIcon(app.icon);
        iconView.setLayoutParams(new LinearLayout.LayoutParams(dp(iconSizeDp), dp(iconSizeDp)));
        container.addView(iconView);

        if (showLabels) {
            TextView tv = new TextView(ctx);
            tv.setText(app.label);
            tv.setTextSize(11f);
            tv.setTextColor(0xFFFFFFFF);
            tv.setGravity(Gravity.CENTER);
            tv.setMaxLines(1);
            tv.setEllipsize(android.text.TextUtils.TruncateAt.END);
            tv.setShadowLayer(4f, 0f, 1f, Color.BLACK);
            tv.setPadding(dp(2), dp(8), dp(2), 0);
            container.addView(tv);
        }

        return container;
    }

    private int dp(int v) {
        return (int) (v * ctx.getResources().getDisplayMetrics().density);
    }
}
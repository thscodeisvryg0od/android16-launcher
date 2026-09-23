package com.example.launcher16;

import android.content.Context;
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

    public AppAdapter(Context ctx, List<AppInfo> apps) {
        this.ctx = ctx;
        this.apps = apps;
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
        container.setPadding(4, 8, 4, 8);

        AppIconView iconView = new AppIconView(ctx);
        iconView.setIcon(app.icon);
        iconView.setLayoutParams(new LinearLayout.LayoutParams(dp(56), dp(56)));
        container.addView(iconView);

        TextView tv = new TextView(ctx);
        tv.setText(app.label);
        tv.setTextSize(11f);
        tv.setTextColor(0xFFFFFFFF);
        tv.setGravity(Gravity.CENTER);
        tv.setMaxLines(1);
        tv.setPadding(0, dp(6), 0, 0);
        container.addView(tv);

        return container;
    }

    private int dp(int v) {
        return (int) (v * ctx.getResources().getDisplayMetrics().density);
    }
}

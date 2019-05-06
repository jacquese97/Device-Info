package com.ytheekshana.deviceinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.BatteryManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

public class tabBattery extends Fragment {
    private TextView txtBatteryLeveldis, txtBatteryStatusdis, txtPowerSourcedis, txtBatteryHealthdis, txtTechnologydis, txtTemperaturedis, txtBatteryVoltagedis;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabbattery, container, false);
        LinearLayout llayout = rootView.findViewById(R.id.llayout);

        try {
            int textDisColor = MainActivity.themeColor;
            int lineColor = GetDetails.getThemeColor(Objects.requireNonNull(getContext()), R.attr.colorButtonNormal);

            IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Context batteryContext = Objects.requireNonNull(getActivity()).getApplicationContext();
            batteryContext.registerReceiver(mBroadcastReceiver, iFilter);

            TextView txtBatteryHealth = new TextView(getContext());
            txtBatteryHealthdis = new TextView(getContext());
            View v = new View(getContext());
            v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            v.setBackgroundColor(lineColor);
            txtBatteryHealth.setText(R.string.Health);
            txtBatteryHealth.setTypeface(null, Typeface.BOLD);
            txtBatteryHealth.setTextSize(16);
            txtBatteryHealthdis.setPadding(0, 0, 0, 15);
            txtBatteryHealthdis.setTextColor(textDisColor);
            txtBatteryHealthdis.setTextSize(16);
            txtBatteryHealthdis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            txtBatteryHealth.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llayout.addView(txtBatteryHealth);
            llayout.addView(txtBatteryHealthdis);
            llayout.addView(v);

            TextView txtBatteryLevel = new TextView(getContext());
            txtBatteryLeveldis = new TextView(getContext());
            View v1 = new View(getContext());
            v1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            v1.setBackgroundColor(lineColor);
            txtBatteryLevel.setText(R.string.Level);
            txtBatteryLevel.setTypeface(null, Typeface.BOLD);
            txtBatteryLevel.setTextSize(16);
            txtBatteryLeveldis.setPadding(0, 0, 0, 15);
            txtBatteryLeveldis.setTextColor(textDisColor);
            txtBatteryLeveldis.setTextSize(16);
            txtBatteryLeveldis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            txtBatteryLevel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llayout.addView(txtBatteryLevel);
            llayout.addView(txtBatteryLeveldis);
            llayout.addView(v1);

            TextView txtBatteryStatus = new TextView(getContext());
            txtBatteryStatusdis = new TextView(getContext());
            View v2 = new View(getContext());
            v2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            v2.setBackgroundColor(lineColor);
            txtBatteryStatus.setText(R.string.Status);
            txtBatteryStatus.setTypeface(null, Typeface.BOLD);
            txtBatteryStatus.setTextSize(16);
            txtBatteryStatus.setPadding(0, 15, 0, 0);
            txtBatteryStatusdis.setPadding(0, 0, 0, 15);
            txtBatteryStatusdis.setTextColor(textDisColor);
            txtBatteryStatusdis.setTextSize(16);
            txtBatteryStatusdis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            txtBatteryStatus.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llayout.addView(txtBatteryStatus);
            llayout.addView(txtBatteryStatusdis);
            llayout.addView(v2);

            TextView txtPowerSource = new TextView(getContext());
            txtPowerSourcedis = new TextView(getContext());
            View v3 = new View(getContext());
            v3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            v3.setBackgroundColor(lineColor);
            txtPowerSource.setText(R.string.PowerSource);
            txtPowerSource.setTypeface(null, Typeface.BOLD);
            txtPowerSource.setTextSize(16);
            txtPowerSource.setPadding(0, 15, 0, 0);
            txtPowerSourcedis.setPadding(0, 0, 0, 15);
            txtPowerSourcedis.setTextColor(textDisColor);
            txtPowerSourcedis.setTextSize(16);
            txtPowerSourcedis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            txtBatteryStatus.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llayout.addView(txtPowerSource);
            llayout.addView(txtPowerSourcedis);
            llayout.addView(v3);

            TextView txtTechnology = new TextView(getContext());
            txtTechnologydis = new TextView(getContext());
            View v4 = new View(getContext());
            v4.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            v4.setBackgroundColor(lineColor);
            txtTechnology.setText(R.string.Technology);
            txtTechnology.setTypeface(null, Typeface.BOLD);
            txtTechnology.setTextSize(16);
            txtTechnology.setPadding(0, 15, 0, 0);
            txtTechnologydis.setPadding(0, 0, 0, 15);
            txtTechnologydis.setTextColor(textDisColor);
            txtTechnologydis.setTextSize(16);
            txtTechnologydis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            txtTechnology.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llayout.addView(txtTechnology);
            llayout.addView(txtTechnologydis);
            llayout.addView(v4);

            TextView txtTemperature = new TextView(getContext());
            txtTemperaturedis = new TextView(getContext());
            View v5 = new View(getContext());
            v5.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            v5.setBackgroundColor(lineColor);
            txtTemperature.setText(R.string.Temperature);
            txtTemperature.setTypeface(null, Typeface.BOLD);
            txtTemperature.setTextSize(16);
            txtTemperature.setPadding(0, 15, 0, 0);
            txtTemperaturedis.setPadding(0, 0, 0, 15);
            txtTemperaturedis.setTextColor(textDisColor);
            txtTemperaturedis.setTextSize(16);
            txtTemperaturedis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            txtTemperature.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llayout.addView(txtTemperature);
            llayout.addView(txtTemperaturedis);
            llayout.addView(v5);

            TextView txtBatteryVoltage = new TextView(getContext());
            txtBatteryVoltagedis = new TextView(getContext());
            View v6 = new View(getContext());
            v6.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            v6.setBackgroundColor(lineColor);
            txtBatteryVoltage.setText(R.string.Voltage);
            txtBatteryVoltage.setTypeface(null, Typeface.BOLD);
            txtBatteryVoltage.setTextSize(16);
            txtBatteryVoltage.setPadding(0, 15, 0, 0);
            txtBatteryVoltagedis.setPadding(0, 0, 0, 15);
            txtBatteryVoltagedis.setTextColor(textDisColor);
            txtBatteryVoltagedis.setTextSize(16);
            txtBatteryVoltagedis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            txtBatteryVoltage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llayout.addView(txtBatteryVoltage);
            llayout.addView(txtBatteryVoltagedis);
            llayout.addView(v6);

            TextView txtBatteryCapacity = new TextView(getContext());
            TextView txtBatteryCapacitydis = new TextView(getContext());
            View v7 = new View(getContext());
            v7.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            v7.setBackgroundColor(lineColor);
            txtBatteryCapacity.setText(R.string.Capacity);
            txtBatteryCapacity.setTypeface(null, Typeface.BOLD);
            txtBatteryCapacity.setTextSize(16);
            txtBatteryCapacity.setPadding(0, 15, 0, 0);
            txtBatteryCapacitydis.setPadding(0, 0, 0, 15);
            txtBatteryCapacitydis.setTextColor(textDisColor);
            txtBatteryCapacitydis.setTextSize(16);
            String BatCap = SplashActivity.batteryCapacity + " mAh";
            txtBatteryCapacitydis.setText(BatCap);
            txtBatteryCapacitydis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            txtBatteryCapacity.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llayout.addView(txtBatteryCapacity);
            llayout.addView(txtBatteryCapacitydis);
            llayout.addView(v7);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return rootView;
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {

                int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int batteryVoltage = intent.getIntExtra("voltage", 0);
                int batteryTemperature = (intent.getIntExtra("temperature", 0)) / 10;
                int batteryStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                int batteryPowerSource = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                int batteryHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
                String batteryTechnology = Objects.requireNonNull(intent.getExtras()).getString(BatteryManager.EXTRA_TECHNOLOGY);

                String battery_level = batteryLevel + "%";
                txtBatteryLeveldis.setText(battery_level);
                txtBatteryStatusdis.setText(GetDetails.getBatteryStatus(batteryStatus,context));
                txtPowerSourcedis.setText(GetDetails.getBatteryPowerSource(batteryPowerSource,context));
                txtBatteryHealthdis.setText(GetDetails.getBatteryHealth(batteryHealth,context));
                txtTechnologydis.setText(batteryTechnology);

                String battemp = batteryTemperature + " \u2103";
                String batvol = batteryVoltage + " mV";
                txtTemperaturedis.setText(battemp);
                txtBatteryVoltagedis.setText(batvol);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
}

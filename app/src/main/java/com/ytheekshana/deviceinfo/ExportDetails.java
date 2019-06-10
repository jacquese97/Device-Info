package com.ytheekshana.deviceinfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.ImageFormat;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Size;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.ytheekshana.deviceinfo.models.ThermalInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

class ExportDetails {

    private static void createTextFile(String content, Context context) {
        try {
            File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
            if (!root.exists()) {
                root.mkdirs();
            }
            File file = new File(root, context.getString(R.string.device_info_report) + ".txt");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            writer.append(content);
            writer.flush();
            writer.close();

            /*FileWriter writer = new FileWriter(file);
            writer.append(content);
            writer.flush();
            writer.close();*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean exportReport(Context context) {

        boolean isReportCreated = false;
        try {
            Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            int batteryLevel = Objects.requireNonNull(intent).getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int batteryVoltage = intent.getIntExtra("voltage", 0);
            int batteryTemperature = (intent.getIntExtra("temperature", 0)) / 10;
            int batteryStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            int batteryPowerSource = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            int batteryHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
            String batteryTechnology = Objects.requireNonNull(intent.getExtras()).getString(BatteryManager.EXTRA_TECHNOLOGY);
            String battemp;
            if (MainActivity.isCelsius) {
                battemp = batteryTemperature + " \u2103";
            } else {
                battemp = String.format(GetDetails.getLocale(context), "%.1f", GetDetails.toFahrenheit((double) batteryTemperature)) + " \u2109";
            }


            String brightnessLevel = (Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS) * 100) / 255 + "%";
            String brightnessMode = "";
            if (Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                brightnessMode = context.getString(R.string.adaptive);
            } else if (Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL) {
                brightnessMode = context.getString(R.string.manual);
            }
            String screenTimeout = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT) / 1000 + " Seconds";
            Locale locale = GetDetails.getLocale(context);

            SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            List<Sensor> deviceSensors = Objects.requireNonNull(mSensorManager).getSensorList(Sensor.TYPE_ALL);

            String seperator = "--------------------------------------------------------\n";
            String seperatorSmall = "------------------\n";
            @SuppressLint({"HardwareIds", "InlinedApi"})
            String content = context.getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME + "\n" +
                    context.getString(R.string.date_created) + " " + Calendar.getInstance().getTime().toString() + "\n\n" +
                    context.getString(R.string.device) + "\n" + seperator +
                    context.getString(R.string.DeviceName) + " : " + SplashActivity.deviceName + "\n" +
                    context.getString(R.string.Model) + " : " + Build.MODEL + "\n" +
                    context.getString(R.string.Manufacturer) + " : " + Build.MANUFACTURER + "\n" +
                    context.getString(R.string.device) + " : " + Build.DEVICE + "\n" +
                    context.getString(R.string.Board) + " : " + Build.BOARD + "\n" +
                    context.getString(R.string.Hardware) + " : " + Build.HARDWARE + "\n" +
                    context.getString(R.string.Brand) + " : " + Build.BRAND + "\n" +
                    context.getString(R.string.BuildFingerprint) + " : " + Build.FINGERPRINT + "\n" +
                    context.getString(R.string.usbHost) + " : " + SplashActivity.usbHost + "\n\n\n" +

                    context.getString(R.string.system) + "\n" + seperator +
                    context.getString(R.string.android_version) + " : " + Build.VERSION.RELEASE + "\n" +
                    context.getString(R.string.version_name) + " : " + GetDetails.GetOSName(Build.VERSION.SDK_INT, context) + "\n" +
                    context.getString(R.string.released_date) + " : " + GetDetails.GetOSReleaseDate(context) + "\n" +
                    context.getString(R.string.CodeName) + " : " + GetDetails.GetOSNameAdvanced(context) + "\n" +
                    context.getString(R.string.APILevel) + " : " + Build.VERSION.SDK_INT + "\n" +
                    context.getString(R.string.SecurityPatchLevel) + " : " + ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ? Build.VERSION.SECURITY_PATCH : "Not Available") + "\n" +
                    context.getString(R.string.Bootloader) + " : " + Build.BOOTLOADER + "\n" +
                    context.getString(R.string.BuildNumber) + " : " + Build.DISPLAY + "\n" +
                    context.getString(R.string.Baseband) + " : " + Build.getRadioVersion() + "\n" +
                    context.getString(R.string.java_vm) + " : " + SplashActivity.androidRuntime + "\n" +
                    context.getString(R.string.Kernel) + " : " + SplashActivity.kernelVersion + "\n" +
                    context.getString(R.string.Language) + " : " + Locale.getDefault().getDisplayLanguage() + " (" + Locale.getDefault().toString() + ")" + "\n" +
                    context.getString(R.string.OpenGL) + " : " + SplashActivity.glVersion + "\n" +
                    context.getString(R.string.RootAccess) + " : " + (SplashActivity.rootedStatus ? context.getString(R.string.yes) : context.getString(R.string.no)) + "\n" +
                    context.getString(R.string.SELinux) + " : " + SplashActivity.selinuxMode + "\n\n\n" +

                    context.getString(R.string.cpu) + "\n" + seperator +
                    context.getString(R.string.Processor) + " : " + SplashActivity.processorName + "\n" +
                    context.getString(R.string.ABIs) + " : " + SplashActivity.cpuABIs + "\n" +
                    context.getString(R.string.CPUHardware) + " : " + SplashActivity.processorHardware + "\n" +
                    context.getString(R.string.CPUGovernor) + " : " + SplashActivity.cpuGovernor + "\n" +
                    context.getString(R.string.Cores) + " : " + Runtime.getRuntime().availableProcessors() + "\n" +
                    context.getString(R.string.CPUFrequency) + " : " + String.format(Locale.US, "%.0f", SplashActivity.cpuMinFreq) + " MHz - " + String.format(Locale.US, "%.0f", SplashActivity.cpuMaxFreq) + " MHz" + "\n" +
                    context.getString(R.string.GPURenderer) + " : " + SplashActivity.gpuRenderer + "\n" +
                    context.getString(R.string.GPUVendor) + " : " + SplashActivity.gpuVendor + "\n" +
                    context.getString(R.string.GPUVersion) + " : " + SplashActivity.gpuVersion + "\n\n\n" +

                    context.getString(R.string.battery) + "\n" + seperator +
                    context.getString(R.string.Health) + " : " + GetDetails.getBatteryHealth(batteryHealth, context) + "\n" +
                    context.getString(R.string.Level) + " : " + batteryLevel + "%" + "\n" +
                    context.getString(R.string.Status) + " : " + GetDetails.getBatteryStatus(batteryStatus, context) + "\n" +
                    context.getString(R.string.PowerSource) + " : " + GetDetails.getBatteryPowerSource(batteryPowerSource, context) + "\n" +
                    context.getString(R.string.Technology) + " : " + batteryTechnology + "\n" +
                    context.getString(R.string.Temperature) + " : " + battemp + "\n" +
                    context.getString(R.string.Voltage) + " : " + batteryVoltage + " mV" + "\n" +
                    context.getString(R.string.Capacity) + " : " + SplashActivity.batteryCapacity + " mAh" + "\n\n\n" +

                    context.getString(R.string.display) + "\n" + seperator +
                    context.getString(R.string.Resolution) + " : " + SplashActivity.displayWidth + " x " + SplashActivity.displayHeight + " " + context.getString(R.string.pixels) + "\n" +
                    context.getString(R.string.Density) + " : " + SplashActivity.displayDensityDPI + " " + context.getString(R.string.dpi) + " (" + SplashActivity.displaySize + ")" + "\n" +
                    context.getString(R.string.FontScale) + " : " + SplashActivity.fontSize + "\n" +
                    context.getString(R.string.PhysicalSize) + " : " + SplashActivity.displayPhysicalSize + " " + context.getString(R.string.inches) + "\n" +
                    context.getString(R.string.RefreshRate) + " : " + SplashActivity.displayRefreshRate + " " + context.getString(R.string.hz) + "\n" +
                    context.getString(R.string.brightnessLevel) + " : " + brightnessLevel + "\n" +
                    context.getString(R.string.brightnessMode) + " : " + brightnessMode + "\n" +
                    context.getString(R.string.screenTimeout) + " : " + screenTimeout + "\n" +
                    context.getString(R.string.Orientation) + " : " + SplashActivity.displayOrientation + "\n\n\n" +

                    context.getString(R.string.memory) + "\n" + seperator +
                    context.getString(R.string.RAM) + "\n" + seperatorSmall +
                    context.getString(R.string.total) + " : " + String.format(locale, "%.1f", SplashActivity.totalRam) + "MB" + "\n" +
                    context.getString(R.string.available) + " : " + String.format(locale, "%.1f", SplashActivity.availableRam) + "MB" + "\n" +
                    context.getString(R.string.used) + " : " + String.format(locale, "%.1f", SplashActivity.usedRam) + "MB" + "\n" +
                    context.getString(R.string.used_percentage) + " : " + (int) SplashActivity.usedRamPercentage + "%" + "\n\n" +

                    context.getString(R.string.systemStorage) + "\n" + seperatorSmall +
                    context.getString(R.string.total) + " : " + String.format(locale, "%.2f", SplashActivity.totalRom) + "GB" + "\n" +
                    context.getString(R.string.available) + " : " + String.format(locale, "%.2f", SplashActivity.availableRom) + "GB" + "\n" +
                    context.getString(R.string.used) + " : " + String.format(locale, "%.2f", SplashActivity.usedRom) + "GB" + "\n" +
                    context.getString(R.string.used_percentage) + " : " + (int) SplashActivity.usedRomPercentage + "%" + "\n\n" +

                    context.getString(R.string.internalStorage) + "\n" + seperatorSmall +
                    context.getString(R.string.total) + " : " + String.format(locale, "%.2f", SplashActivity.totalInternalStorage) + "GB" + "\n" +
                    context.getString(R.string.available) + " : " + String.format(locale, "%.2f", SplashActivity.availableInternalStorage) + "GB" + "\n" +
                    context.getString(R.string.used) + " : " + String.format(locale, "%.2f", SplashActivity.usedInternalStorage) + "GB" + "\n" +
                    context.getString(R.string.used_percentage) + " : " + (int) SplashActivity.usedInternalPercentage + "%" + "\n\n";

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && ContextCompat.getExternalFilesDirs(context, null).length >= 2) {
                String externalStorageData = context.getString(R.string.externalStorage) + "\n" + seperatorSmall +
                        context.getString(R.string.total) + " : " + String.format(locale, "%.2f", SplashActivity.totalExternalStorage) + "GB" + "\n" +
                        context.getString(R.string.available) + " : " + String.format(locale, "%.2f", SplashActivity.availableExternalStorage) + "GB" + "\n" +
                        context.getString(R.string.used) + " : " + String.format(locale, "%.2f", SplashActivity.usedExternalStorage) + "GB" + "\n" +
                        context.getString(R.string.used_percentage) + " : " + (int) SplashActivity.usedExternalPercentage + "%" + "\n\n";
                content = content + externalStorageData;
            }

            content = content + "\n" + context.getString(R.string.camera) + "\n" + seperator;

            CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            StringBuilder cameraContent = new StringBuilder();
            for (final String cameraId : Objects.requireNonNull(cameraManager).getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);

                StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                Size[] sizes = Objects.requireNonNull(streamConfigurationMap).getOutputSizes(ImageFormat.JPEG);
                Integer lensFacing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                String lens = "";
                if (lensFacing != null) {
                    switch (lensFacing) {
                        case CameraCharacteristics.LENS_FACING_BACK:
                            lens = context.getString(R.string.back);
                            break;
                        case CameraCharacteristics.LENS_FACING_FRONT:
                            lens = context.getString(R.string.front);
                            break;
                        case CameraCharacteristics.LENS_FACING_EXTERNAL:
                            lens = context.getString(R.string.external);
                            break;
                        default:
                            lens = context.getString(R.string.unknown);
                            break;
                    }
                }
                float[] flenths = cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
                cameraContent.append(context.getString(R.string.camera))
                        .append(" - ").append(cameraId).append("\n").append(seperatorSmall)
                        .append(context.getString(R.string.position)).append(" : ").append(lens).append("\n")
                        .append(context.getString(R.string.mega_pixels)).append(" : ").append(GetDetails.getCameraMP(sizes)).append("\n")
                        .append(context.getString(R.string.Resolution)).append(" : ").append(GetDetails.getCameraResolution(sizes)).append("\n")
                        .append(context.getString(R.string.focal_length)).append(" : ").append(Objects.requireNonNull(flenths)[0]).append("mm").append("\n\n");
            }

            content = content + cameraContent.toString();

            ArrayList<ThermalInfo> exportThermalList = GetDetails.loadThermal();
            if (exportThermalList != null && !exportThermalList.isEmpty()) {
                content = content + "\n" + context.getString(R.string.thermal) + "\n" + seperator;
                StringBuilder thermalContent = new StringBuilder();
                for (ThermalInfo ti : exportThermalList) {
                    thermalContent.append(ti.getThermalName()).append(" - ").append(ti.getThermalValue()).append("\n");
                }
                content = content + thermalContent.toString();
            }

            content = content + "\n\n" + context.getString(R.string.sensors) + "\n" + seperator;
            StringBuilder sensorContent = new StringBuilder();
            for (Sensor sensor : deviceSensors) {
                sensorContent.append(sensor.getName()).append("\n").append(seperatorSmall)
                        .append(context.getString(R.string.vendor)).append(" : ").append(sensor.getVendor()).append("\n")
                        .append(context.getString(R.string.type)).append(" : ").append(GetDetails.GetSensorType(sensor.getType(), context)).append("\n")
                        .append(context.getString(R.string.power)).append(" : ").append(sensor.getPower()).append("mA").append("\n\n");
            }
            content = content + sensorContent.toString();

            createTextFile(content, context);
            isReportCreated = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isReportCreated;
    }

    static void export(Context context, View viewSnackbar) {
        if (exportReport(context)) {
            Snackbar snackbar = Snackbar.make(viewSnackbar, context.getString(R.string.document_generated), Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(context.getString(R.string.open), view -> {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + context.getString(R.string.device_info_report) + ".txt");
                Uri path = FileProvider.getUriForFile(context, "com.ytheekshana.deviceinfo", file);
                Intent openTextFile = new Intent(Intent.ACTION_VIEW);
                openTextFile.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                openTextFile.setDataAndType(path, "text/html");
                try {
                    context.startActivity(openTextFile);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            SnackbarHelper.configSnackbar(context, snackbar);
            snackbar.show();
        } else {
            Snackbar snackbar = Snackbar.make(viewSnackbar, context.getString(R.string.something_went_wrong), Snackbar.LENGTH_LONG);
            SnackbarHelper.configSnackbar(context, snackbar);
            snackbar.show();
        }
    }
}

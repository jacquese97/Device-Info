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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

class ExportDetails {

    private static void createTextFile(String content) {
        try {
            File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
            if (!root.exists()) {
                root.mkdirs();
            }
            File file = new File(root, "Device Info Report.txt");
            FileWriter writer = new FileWriter(file);
            writer.append(content);
            writer.flush();
            writer.close();
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

            String brightnessLevel = (Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS) * 100) / 255 + "%";
            String brightnessMode = "";
            if (Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                brightnessMode = "Adaptive";
            } else if (Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL) {
                brightnessMode = "Manual";
            }
            String screenTimeout = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT) / 1000 + " Seconds";

            SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            List<Sensor> deviceSensors = Objects.requireNonNull(mSensorManager).getSensorList(Sensor.TYPE_ALL);

            String seperator = "--------------------------------------------------------\n";
            String seperatorSmall = "------------------\n";
            @SuppressLint({"HardwareIds", "InlinedApi"})
            String content = "Device Info " + BuildConfig.VERSION_NAME + "\n" +
                    "Date Created " + Calendar.getInstance().getTime().toString() + "\n\n" +
                    "Device\n" + seperator +
                    "Device Name : " + SplashActivity.deviceName + "\n" +
                    "Model : " + Build.MODEL + "\n" +
                    "Manufacturer : " + Build.MANUFACTURER + "\n" +
                    "Device : " + Build.DEVICE + "\n" +
                    "Board : " + Build.BOARD + "\n" +
                    "Hardware : " + Build.HARDWARE + "\n" +
                    "Brand : " + Build.BRAND + "\n" +
                    "Build Fingerprint : " + Build.FINGERPRINT + "\n" +
                    "USB Host : " + SplashActivity.usbHost + "\n\n\n" +

                    "System\n" + seperator +
                    "Android Version : " + Build.VERSION.RELEASE + "\n" +
                    "Version Name : " + GetDetails.GetOSName(Build.VERSION.SDK_INT) + "\n" +
                    "Release Date : " + GetDetails.GetOSReleaseDate() + "\n" +
                    "Code Name : " + GetDetails.GetOSNameAdvanced() + "\n" +
                    "API Level : " + Build.VERSION.SDK_INT + "\n" +
                    "Security Patch : " + ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ? Build.VERSION.SECURITY_PATCH : "Not Available") + "\n" +
                    "Bootloader : " + Build.BOOTLOADER + "\n" +
                    "Build Number : " + Build.DISPLAY + "\n" +
                    "Baseband : " + Build.getRadioVersion() + "\n" +
                    "Java VM : " + SplashActivity.androidRuntime + "\n" +
                    "Kernel : " + SplashActivity.kernelVersion + "\n" +
                    "OpenGL ES : " + SplashActivity.glVersion + "\n" +
                    "Root Access : " + (SplashActivity.rootedStatus ? "Yes" : "No") + "\n" +
                    "SELinux : " + SplashActivity.selinuxMode + "\n\n\n" +

                    "CPU\n" + seperator +
                    "Processor : " + SplashActivity.processorName + "\n" +
                    "Supported ABIs : " + SplashActivity.cpuABIs + "\n" +
                    "CPU Hardware : " + SplashActivity.processorHardware + "\n" +
                    "Governor : " + SplashActivity.cpuGovernor + "\n" +
                    "Cores : " + Runtime.getRuntime().availableProcessors() + "\n" +
                    "CPU Frequency : " + String.format(Locale.US, "%.0f", SplashActivity.cpuMinFreq) + " MHz - " + String.format(Locale.US, "%.0f", SplashActivity.cpuMaxFreq) + " MHz" + "\n" +
                    "GPU Renderer : " + SplashActivity.gpuRenderer + "\n" +
                    "GPU Vendor : " + SplashActivity.gpuVendor + "\n" +
                    "GPU Version : " + SplashActivity.gpuVersion + "\n\n\n" +

                    "Battery\n" + seperator +
                    "Health : " + GetDetails.getBatteryHealth(batteryHealth) + "\n" +
                    "Level : " + batteryLevel + "%" + "\n" +
                    "Status : " + GetDetails.getBatteryStatus(batteryStatus) + "\n" +
                    "Power Source : " + GetDetails.getBatteryPowerSource(batteryPowerSource) + "\n" +
                    "Technology : " + batteryTechnology + "\n" +
                    "Temperature : " + batteryTemperature + " \u2103" + "\n" +
                    "Voltage : " + batteryVoltage + " mV" + "\n" +
                    "Capacity : " + SplashActivity.batteryCapacity + " mAh" + "\n\n\n" +

                    "Display\n" + seperator +
                    "Resolution : " + SplashActivity.displayWidth + " x " + SplashActivity.displayHeight + " Pixels" + "\n" +
                    "Density : " + SplashActivity.displayDensityDPI + " dpi (" + SplashActivity.displaySize + ")" + "\n" +
                    "Font Scale : " + SplashActivity.fontSize + "\n" +
                    "Physical Size : " + SplashActivity.displayPhysicalSize + " inches" + "\n" +
                    "Refresh Rate : " + SplashActivity.displayRefreshRate + " Hz" + "\n" +
                    "Brightness Level : " + brightnessLevel + "\n" +
                    "Brightness Mode : " + brightnessMode + "\n" +
                    "Screen Timeout : " + screenTimeout + "\n" +
                    "Orientation : " + SplashActivity.displayOrientation + "\n\n\n" +

                    "Memory\n" + seperator +
                    "RAM\n" + seperatorSmall +
                    "Total : " + String.format(Locale.US, "%.1f", SplashActivity.totalRam) + "MB" + "\n" +
                    "Available : " + String.format(Locale.US, "%.1f", SplashActivity.availableRam) + "MB" + "\n" +
                    "Used : " + String.format(Locale.US, "%.1f", SplashActivity.usedRam) + "MB" + "\n" +
                    "Used Percentage : " + (int) SplashActivity.usedRamPercentage + "%" + "\n\n" +

                    "System Storage\n" + seperatorSmall +
                    "Total : " + String.format(Locale.US, "%.2f", SplashActivity.totalRom) + "GB" + "\n" +
                    "Available : " + String.format(Locale.US, "%.2f", SplashActivity.availableRom) + "GB" + "\n" +
                    "Used : " + String.format(Locale.US, "%.2f", SplashActivity.usedRom) + "GB" + "\n" +
                    "Used Percentage : " + (int) SplashActivity.usedRomPercentage + "%" + "\n\n" +

                    "Internal Storage\n" + seperatorSmall +
                    "Total : " + String.format(Locale.US, "%.2f", SplashActivity.totalInternalStorage) + "GB" + "\n" +
                    "Available : " + String.format(Locale.US, "%.2f", SplashActivity.availableInternalStorage) + "GB" + "\n" +
                    "Used : " + String.format(Locale.US, "%.2f", SplashActivity.usedInternalStorage) + "GB" + "\n" +
                    "Used Percentage : " + (int) SplashActivity.usedInternalPercentage + "%" + "\n\n";

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && ContextCompat.getExternalFilesDirs(context, null).length >= 2) {
                String externalStorageData = "External Storage\n" + seperatorSmall +
                        "Total : " + String.format(Locale.US, "%.2f", SplashActivity.totalExternalStorage) + "GB" + "\n" +
                        "Available : " + String.format(Locale.US, "%.2f", SplashActivity.availableExternalStorage) + "GB" + "\n" +
                        "Used : " + String.format(Locale.US, "%.2f", SplashActivity.usedExternalStorage) + "GB" + "\n" +
                        "Used Percentage : " + (int) SplashActivity.usedExternalPercentage + "%" + "\n\n";
                content = content + externalStorageData;
            }

            content = content + "\nCamera\n" + seperator;

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
                            lens = "Back";
                            break;
                        case CameraCharacteristics.LENS_FACING_FRONT:
                            lens = "Front";
                            break;
                        case CameraCharacteristics.LENS_FACING_EXTERNAL:
                            lens = "External";
                            break;
                        default:
                            lens = "Unknown";
                            break;
                    }
                }
                float[] flenths = cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
                cameraContent.append("Camera - ").append(cameraId).append("\n").append(seperatorSmall)
                        .append("Position : ").append(lens).append("\n")
                        .append("Mega Pixels : ").append(GetDetails.getCameraMP(sizes)).append("\n")
                        .append("Resolution : ").append(GetDetails.getCameraResolution(sizes)).append("\n")
                        .append("Focul Length : ").append(Objects.requireNonNull(flenths)[0]).append("mm").append("\n\n");
            }

            content = content + cameraContent.toString();

            ArrayList<ThermalInfo> exportThermalList = GetDetails.loadThermal();
            if (exportThermalList != null && !exportThermalList.isEmpty()) {
                content = content + "\nThermal\n" + seperator;
                StringBuilder thermalContent = new StringBuilder();
                for (ThermalInfo ti : exportThermalList) {
                    thermalContent.append(ti.getThermalName()).append(" - ").append(ti.getThermalValue()).append("\n");
                }
                content = content + thermalContent.toString();
            }

            content = content + "\n\nSensors\n" + seperator;
            StringBuilder sensorContent = new StringBuilder();
            for (Sensor sensor : deviceSensors) {
                sensorContent.append(sensor.getName()).append("\n").append(seperatorSmall)
                        .append("Vendor : ").append(sensor.getVendor()).append("\n")
                        .append("Type : ").append(GetDetails.GetSensorType(sensor.getType())).append("\n")
                        .append("Power : ").append(sensor.getPower()).append("mA").append("\n\n");
            }
            content = content + sensorContent.toString();

            createTextFile(content);
            isReportCreated = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isReportCreated;
    }

    static void export(Context context, View viewSnackbar) {
        if (exportReport(context)) {
            Snackbar snackbar = Snackbar.make(viewSnackbar, "Document generated in Downloads folder", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Open", view -> {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/Device Info Report.txt");
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
            Snackbar snackbar = Snackbar.make(viewSnackbar, "Something went wrong", Snackbar.LENGTH_LONG);
            SnackbarHelper.configSnackbar(context, snackbar);
            snackbar.show();
        }
    }
}

package com.ytheekshana.deviceinfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Size;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.ytheekshana.deviceinfo.models.ThermalInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

public class GetDetails {

    @ColorInt
    public static int getThemeColor(@NonNull final Context context, @AttrRes final int attributeColor) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(attributeColor, value, true);
        return value.data;
    }

    static String GetFromBuildProp(String PropKey) {
        Process p;
        String propvalue = "";
        try {
            p = new ProcessBuilder("/system/bin/getprop", PropKey).redirectErrorStream(true).start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                propvalue = line;
            }
            p.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propvalue;
    }

    public static String GetOSNameAdvanced(Context context) {
        String OSName;
        switch (Build.VERSION.SDK_INT) {
            case 21:
                OSName = "Lollipop";
                break;
            case 22:
                OSName = "Lollipop MR1";
                break;
            case 23:
                OSName = "Marshmallow";
                break;
            case 24:
                OSName = "Nougat";
                break;
            case 25:
                OSName = "Nougat MR1";
                break;
            case 26:
                OSName = "Oreo";
                break;
            case 27:
                OSName = "Oreo MR1";
                break;
            case 28:
                OSName = "Pie";
                break;
            default:
                OSName = context.getString(R.string.unknown);
        }
        return OSName;
    }

    public static String GetOSReleaseDate(Context context) {
        String OSReleaseDate = "";
        switch (Build.VERSION.SDK_INT) {
            case 11:
            case 12:
            case 13:
                OSReleaseDate = "2011-02-22";
                break;
            case 14:
            case 15:
                OSReleaseDate = "2011-10-18";
                break;
            case 16:
            case 17:
            case 18:
                OSReleaseDate = "2012-07-09";
                break;
            case 19:
                OSReleaseDate = "2013-10-31";
                break;
            case 21:
            case 22:
                OSReleaseDate = "2014-11-12";
                break;
            case 23:
                OSReleaseDate = "2015-10-05";
                break;
            case 24:
            case 25:
                OSReleaseDate = "2016-08-22";
                break;
            case 26:
            case 27:
                OSReleaseDate = "2017-08-21";
                break;
            case 28:
                OSReleaseDate = "2018-08-09";
                break;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", getLocale(context));
        SimpleDateFormat dateFormatInput = new SimpleDateFormat("yyyy-mm-dd", getLocale(context));
        Date date = null;
        try {
            date = dateFormatInput.parse(OSReleaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat.format(date);
    }

    public static String GetOSName(int sdk, Context context) {
        String OSName;
        switch (sdk) {
            case 11:
            case 12:
            case 13:
                OSName = "HoneyComb";
                break;
            case 14:
            case 15:
                OSName = "Ice Cream Sandwich";
                break;
            case 16:
            case 17:
            case 18:
                OSName = "Jelly Bean";
                break;
            case 19:
                OSName = "KitKat";
                break;
            case 21:
            case 22:
                OSName = "Lollipop";
                break;
            case 23:
                OSName = "Marshmallow";
                break;
            case 24:
            case 25:
                OSName = "Nougat";
                break;
            case 26:
            case 27:
                OSName = "Oreo";
                break;
            case 28:
                OSName = "Pie";
                break;
            default:
                OSName = context.getString(R.string.unknown);
        }
        return OSName;
    }

    static String getProcessor(Context context) {
        String Final = "";
        try {
            StringBuilder sb = new StringBuilder();
            if (new File("/proc/cpuinfo").exists()) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(new File("/proc/cpuinfo")));
                    String aLine;
                    while ((aLine = br.readLine()) != null) {
                        String _append = aLine + "ndeviceinfo";
                        sb.append(_append);
                    }
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String[] cpuinfo = sb.toString().split(":");
                for (int a = 0; a < cpuinfo.length; a++) {
                    if (cpuinfo[a].toLowerCase().contains("processor")) {
                        int getlastindex = cpuinfo[a + 1].indexOf("ndeviceinfo");
                        Final = cpuinfo[a + 1].substring(1, getlastindex);
                        break;
                    }
                }
                if (Final.equals("0") || Final.equals("")) {
                    for (int a = 0; a < cpuinfo.length; a++) {
                        if (cpuinfo[a].contains("model name")) {
                            int getlastindex = cpuinfo[a + 1].indexOf("ndeviceinfo");
                            Final = cpuinfo[a + 1].substring(1, getlastindex);
                            break;
                        }
                    }
                }
                if (Final.equals("") || Final.equals("0")) {
                    Final = context.getString(R.string.unknown);
                }
            } else {
                Final = context.getString(R.string.unknown);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Final;
    }

    static String getProcessorHardware(Context context) {
        String Final = "";
        try {
            StringBuilder sb = new StringBuilder();
            if (new File("/proc/cpuinfo").exists()) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(new File("/proc/cpuinfo")));
                    String aLine;
                    while ((aLine = br.readLine()) != null) {
                        String _append = aLine + "ndeviceinfo";
                        sb.append(_append);
                    }
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String[] cpuinfo = sb.toString().split(":");
                for (int a = 0; a < cpuinfo.length; a++) {
                    if (cpuinfo[a].toLowerCase().contains("hardware")) {
                        int getlastindex = cpuinfo[a + 1].indexOf("ndeviceinfo");
                        Final = cpuinfo[a + 1].substring(1, getlastindex);
                        break;
                    } else {
                        Final = context.getString(R.string.unknown);
                    }
                }
            } else {
                Final = context.getString(R.string.unknown);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Final;
    }

    static String getCPUGoverner() {
        String aLine = "";
        if (new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor").exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor")));
                aLine = br.readLine();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return aLine;
    }

    public static String getTime(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }
        return String.format(Locale.US, "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    static String getDisplaySize(Activity activity) {
        double x = 0, y = 0;
        int mWidthPixels, mHeightPixels;
        try {
            WindowManager windowManager = activity.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getMetrics(displayMetrics);
            Point realSize = new Point();
            Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
            mWidthPixels = realSize.x;
            mHeightPixels = realSize.y;
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            x = Math.pow(mWidthPixels / dm.xdpi, 2);
            y = Math.pow(mHeightPixels / dm.ydpi, 2);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return String.format(Locale.US, "%.2f", Math.sqrt(x + y));
    }

    public static String PhoneType(int gettype) {
        String Type = "";
        switch (gettype) {
            case TelephonyManager.PHONE_TYPE_CDMA:
                Type = "CDMA";
                break;
            case TelephonyManager.PHONE_TYPE_GSM:
                Type = "GSM";
                break;
            case TelephonyManager.PHONE_TYPE_NONE:
                Type = "None";
                break;
        }
        return Type;
    }

    public static String NetworkType(int gettype) {
        String Type;
        switch (gettype) {
            case TelephonyManager.NETWORK_TYPE_CDMA:
                Type = "CDMA";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                Type = "EDGE";
                break;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                Type = "GPRS";
                break;
            case TelephonyManager.NETWORK_TYPE_GSM:
                Type = "GSM";
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                Type = "HSDPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                Type = "HSPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                Type = "HSPAP";
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                Type = "HSUPA";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                Type = "LTE";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                Type = "UMTS";
                break;
            default:
                Type = "Not Available";
                break;
        }
        return Type;
    }

    public static String getBatteryStatus(int batstatus, Context context) {
        String batstatusdis;
        if (batstatus == BatteryManager.BATTERY_STATUS_CHARGING) {
            batstatusdis = context.getString(R.string.charging);
        } else if (batstatus == BatteryManager.BATTERY_STATUS_DISCHARGING) {
            batstatusdis = context.getString(R.string.discharging);
        } else if (batstatus == BatteryManager.BATTERY_STATUS_FULL) {
            batstatusdis = context.getString(R.string.battery_full);
        } else if (batstatus == BatteryManager.BATTERY_STATUS_UNKNOWN) {
            batstatusdis = context.getString(R.string.unknown);
        } else if (batstatus == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
            batstatusdis = context.getString(R.string.not_charging);
        } else {
            batstatusdis = context.getString(R.string.not_available);
        }
        return batstatusdis;
    }

    public static String getBatteryPowerSource(int batpowersource, Context context) {
        String batpowersourcedis;
        if (batpowersource == BatteryManager.BATTERY_PLUGGED_USB) {
            batpowersourcedis = context.getString(R.string.usb_port);
        } else if (batpowersource == BatteryManager.BATTERY_PLUGGED_AC) {
            batpowersourcedis = context.getString(R.string.ac);
        } else {
            batpowersourcedis = context.getString(R.string.battery);
        }
        return batpowersourcedis;
    }

    public static String getBatteryHealth(int bathealth, Context context) {
        String bathealthdis;
        if (bathealth == BatteryManager.BATTERY_HEALTH_COLD) {
            bathealthdis = context.getString(R.string.cold);
        } else if (bathealth == BatteryManager.BATTERY_HEALTH_DEAD) {
            bathealthdis = context.getString(R.string.dead);
        } else if (bathealth == BatteryManager.BATTERY_HEALTH_GOOD) {
            bathealthdis = context.getString(R.string.good);
        } else if (bathealth == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {
            bathealthdis = context.getString(R.string.over_voltage);
        } else if (bathealth == BatteryManager.BATTERY_HEALTH_OVERHEAT) {
            bathealthdis = context.getString(R.string.overheat);
        } else if (bathealth == BatteryManager.BATTERY_HEALTH_UNKNOWN) {
            bathealthdis = context.getString(R.string.unknown);
        } else if (bathealth == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) {
            bathealthdis = context.getString(R.string.failure);
        } else {
            bathealthdis = context.getString(R.string.not_available);
        }
        return bathealthdis;
    }

    static boolean isRooted() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys") || canExecuteCommand("/system/xbin/which su") || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su");
    }

    private static boolean canExecuteCommand(String command) {
        try {
            int exitValue = Runtime.getRuntime().exec(command).waitFor();
            return exitValue == 0;
        } catch (Exception e) {
            return false;
        }
    }

    static String getWifiMacAddress() {
        try {
            String interfaceName = "wlan0";
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (!intf.getName().equalsIgnoreCase(interfaceName)) {
                    continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) {
                    return "";
                }
                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) {
                    buf.append(String.format("%02X:", aMac));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                return buf.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    @SuppressLint("HardwareIds")
    static String getBluetoothMac(Context context) {
        String result = "";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                result = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        "bluetooth_address");
            } else {
                BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
                result = (bta != null) ? bta.getAddress() : "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    static int getBatteryCapacity(Context context) {
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";
        try {
            Object mPowerProfile = Class.forName(POWER_PROFILE_CLASS).getConstructor(Context.class).newInstance(context);
            batteryCapacity = (Double) Class.forName(POWER_PROFILE_CLASS).getMethod("getAveragePower", java.lang.String.class).invoke(mPowerProfile, "battery.capacity");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return (int) batteryCapacity;
    }

    public static String[] getStorageDirectories(Context context) {
        String[] storageDirectories;
        String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
        List<String> results = new ArrayList<>();
        File[] externalDirs = context.getExternalFilesDirs(null);
        for (File file : externalDirs) {
            String path = file.getPath().split("/Android")[0];
            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Environment.isExternalStorageRemovable(file))
                    || rawSecondaryStoragesStr != null && rawSecondaryStoragesStr.contains(path)) {
                results.add(path);
            }
        }
        storageDirectories = results.toArray(new String[0]);
        return storageDirectories;
    }

    public static Double getAndroidVersion(int sdk) {
        double Version;
        switch (sdk) {
            case 10:
                Version = 2.3;
                break;
            case 11:
                Version = 3.0;
                break;
            case 12:
                Version = 3.1;
                break;
            case 13:
                Version = 3.2;
                break;
            case 14:
            case 15:
                Version = 4.0;
                break;
            case 16:
                Version = 4.1;
                break;
            case 17:
                Version = 4.2;
                break;
            case 18:
                Version = 4.3;
                break;
            case 19:
                Version = 4.4;
                break;
            case 21:
                Version = 5.0;
                break;
            case 22:
                Version = 5.1;
                break;
            case 23:
                Version = 6.0;
                break;
            case 24:
                Version = 7.0;
                break;
            case 25:
                Version = 7.1;
                break;
            case 26:
                Version = 8.0;
                break;
            case 27:
                Version = 8.1;
                break;
            case 28:
                Version = 9.0;
                break;
            default:
                Version = 0.0;
                break;
        }
        return Version;
    }

    static String GetSELinuxMode(Context context) {
        StringBuilder output = new StringBuilder();
        Process p;
        try {
            p = Runtime.getRuntime().exec("getenforce");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return context.getString(R.string.not_supported);
        }
        String response = output.toString();
        if ("Enforcing".equals(response)) {
            return context.getString(R.string.enforcing);
        } else if ("Permissive".equals(response)) {
            return context.getString(R.string.permissive);
        } else {
            return context.getString(R.string.unable_to_determine);
        }
    }

    public static String GetSensorType(int type, Context context) {
        String stype;
        switch (type) {
            case 1:
                stype = context.getString(R.string.sensor_accelerometer);
                break;
            case 2:
                stype = context.getString(R.string.sensor_magnetic_field);
                break;
            case 3:
                stype = context.getString(R.string.sensor_orientation);
                break;
            case 4:
                stype = context.getString(R.string.sensor_gyroscope);
                break;
            case 5:
                stype = context.getString(R.string.sensor_light);
                break;
            case 6:
                stype = context.getString(R.string.sensor_pressure);
                break;
            case 7:
                stype = context.getString(R.string.sensor_temperature);
                break;
            case 8:
                stype = context.getString(R.string.sensor_proximity);
                break;
            case 9:
                stype = context.getString(R.string.sensor_gravity);
                break;
            case 10:
                stype = context.getString(R.string.sensor_linear_acceleration);
                break;
            case 11:
                stype = context.getString(R.string.sensor_rotation_vector);
                break;
            case 12:
                stype = context.getString(R.string.sensor_relative_humidity);
                break;
            case 13:
                stype = context.getString(R.string.sensor_ambient_temperature);
                break;
            case 14:
                stype = context.getString(R.string.sensor_magnetic_field_uncalibrated);
                break;
            case 15:
                stype = context.getString(R.string.sensor_game_rotation_vector);
                break;
            case 16:
                stype = context.getString(R.string.sensor_gyroscope_uncalibrated);
                break;
            case 17:
                stype = context.getString(R.string.sensor_significant_motion);
                break;
            case 18:
                stype = context.getString(R.string.sensor_step_detector);
                break;
            case 19:
                stype = context.getString(R.string.sensor_step_counter);
                break;
            case 20:
                stype = context.getString(R.string.sensor_geomagnetic_rotation_vector);
                break;
            case 21:
                stype = context.getString(R.string.sensor_heart_rate);
                break;
            case 22:
                stype = context.getString(R.string.sensor_tilt_detector);
                break;
            case 23:
                stype = context.getString(R.string.sensor_wake_gesture);
                break;
            case 24:
                stype = context.getString(R.string.sensor_glance_gesture);
                break;
            case 25:
                stype = context.getString(R.string.sensor_pickup_gesture);
                break;
            case 26:
                stype = context.getString(R.string.sensor_wrist_tilt_detector);
                break;
            case 27:
                stype = context.getString(R.string.sensor_device_orientation);
                break;
            case 28:
                stype = context.getString(R.string.sensor_pose_6dof);
                break;
            case 29:
                stype = context.getString(R.string.sensor_stationary_detect);
                break;
            case 30:
                stype = context.getString(R.string.sensor_motion_detect);
                break;
            case 31:
                stype = context.getString(R.string.sensor_heart_beat);
                break;
            case 32:
                stype = context.getString(R.string.sensor_dynamic_sensor_meta);
                break;
            case 33:
                stype = context.getString(R.string.sensor_additional_info);
                break;
            case 34:
                stype = context.getString(R.string.sensor_low_latency_offbody_detect);
                break;
            case 35:
                stype = context.getString(R.string.sensor_accelerometer_uncalibrated);
                break;
            default:
                stype = context.getString(R.string.unknown);
                break;
        }
        return stype;
    }

    public static int getDarkColor(Context context, int color) {
        List<String> colorThemeColor = Arrays.asList(context.getResources().getStringArray(R.array.accent_colors));
        List<String> colorThemeColorDark = Arrays.asList(context.getResources().getStringArray(R.array.accent_colors_700));
        String getHex = String.format("#%02x%02x%02x", Color.red(color), Color.green(color), Color.blue(color));
        return Color.parseColor(colorThemeColorDark.get(colorThemeColor.indexOf(getHex)));
    }

    static int getDarkColor2(Context context, int color) {
        List<String> colorThemeColor = Arrays.asList(context.getResources().getStringArray(R.array.accent_colors));
        List<String> colorThemeColor2 = Arrays.asList(context.getResources().getStringArray(R.array.accent_colors_2));
        String getHex = String.format("#%02x%02x%02x", Color.red(color), Color.green(color), Color.blue(color));
        return Color.parseColor(colorThemeColor2.get(colorThemeColor.indexOf(getHex)));
    }

    static int adjustAlpha(@ColorInt int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static void copy(File src, File dst) {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getKeyName(String name, Context context) {
        String keyName;
        switch (name) {
            case "android.colorCorrection.availableAberrationModes":
                keyName = context.getString(R.string.camera_aberration_modes);
                break;
            case "android.control.aeAvailableAntibandingModes":
                keyName = context.getString(R.string.camera_antibanding_modes);
                break;
            case "android.control.aeAvailableModes":
                keyName = context.getString(R.string.camera_auto_exposure_modes);
                break;
            case "android.control.aeAvailableTargetFpsRanges":
                keyName = context.getString(R.string.camera_target_fps_ranges);
                break;
            case "android.control.aeCompensationRange":
                keyName = context.getString(R.string.camera_compensation_range);
                break;
            case "android.control.aeCompensationStep":
                keyName = context.getString(R.string.camera_compensation_step);
                break;
            case "android.control.aeLockAvailable":
                keyName = context.getString(R.string.camera_auto_exposure_lock);
                break;
            case "android.control.afAvailableModes":
                keyName = context.getString(R.string.camera_autofocus_modes);
                break;
            case "android.control.availableEffects":
                keyName = context.getString(R.string.camera_effects);
                break;
            case "android.control.availableModes":
                keyName = context.getString(R.string.camera_available_modes);
                break;
            case "android.control.availableSceneModes":
                keyName = context.getString(R.string.camera_scene_modes);
                break;
            case "android.control.availableVideoStabilizationModes":
                keyName = context.getString(R.string.camera_video_stabilization_modes);
                break;
            case "android.control.awbAvailableModes":
                keyName = context.getString(R.string.camera_auto_white_balance_modes);
                break;
            case "android.control.awbLockAvailable":
                keyName = context.getString(R.string.camera_auto_white_balance_lock);
                break;
            case "android.control.maxRegionsAe":
                keyName = context.getString(R.string.camera_maximum_auto_exposure_regions);
                break;
            case "android.control.maxRegionsAf":
                keyName = context.getString(R.string.camera_maximum_auto_focus_regions);
                break;
            case "android.control.maxRegionsAwb":
                keyName = context.getString(R.string.camera_maximum_auto_white_balance_regions);
                break;
            case "android.edge.availableEdgeModes":
                keyName = context.getString(R.string.camera_edge_modes);
                break;
            case "android.flash.info.available":
                keyName = context.getString(R.string.camera_flash_available);
                break;
            case "android.hotPixel.availableHotPixelModes":
                keyName = context.getString(R.string.camera_hot_pixel_modes);
                break;
            case "android.info.supportedHardwareLevel":
                keyName = context.getString(R.string.camera_hardware_level);
                break;
            case "android.jpeg.availableThumbnailSizes":
                keyName = context.getString(R.string.camera_thumbnail_sizes);
                break;
            case "android.lens.facing":
                keyName = context.getString(R.string.camera_lens_placement);
                break;
            case "android.lens.info.availableApertures":
                keyName = context.getString(R.string.camera_apertures);
                break;
            case "android.lens.info.availableFilterDensities":
                keyName = context.getString(R.string.camera_filter_densities);
                break;
            case "android.lens.info.availableFocalLengths":
                keyName = context.getString(R.string.camera_focal_lengths);
                break;
            case "android.lens.info.availableOpticalStabilization":
                keyName = context.getString(R.string.camera_optical_stabilization);
                break;
            case "android.lens.info.focusDistanceCalibration":
                keyName = context.getString(R.string.camera_focus_distance_calibration);
                break;
            case "android.lens.info.hyperfocalDistance":
                keyName = context.getString(R.string.camera_hyperfocal_distance);
                break;
            case "android.lens.info.minimumFocusDistance":
                keyName = context.getString(R.string.camera_minimum_focus_distance);
                break;
            case "android.noiseReduction.availableNoiseReductionModes":
                keyName = context.getString(R.string.camera_noise_reduction_modes);
                break;
            case "android.request.availableCapabilities":
                keyName = context.getString(R.string.camera_capabilities);
                break;
            case "android.request.maxNumInputStreams":
                keyName = context.getString(R.string.camera_maximum_input_streams);
                break;
            case "android.request.maxNumOutputProc":
                keyName = context.getString(R.string.camera_maximum_output_streams);
                break;
            case "android.request.maxNumOutputProcStalling":
                keyName = context.getString(R.string.camera_maximum_output_streams_stalling);
                break;
            case "android.request.maxNumOutputRaw":
                keyName = context.getString(R.string.camera_maximum_raw_output_streams);
                break;
            case "android.request.partialResultCount":
                keyName = context.getString(R.string.camera_partial_results);
                break;
            case "android.request.pipelineMaxDepth":
                keyName = context.getString(R.string.camera_maximum_pipeline_depth);
                break;
            case "android.scaler.availableMaxDigitalZoom":
                keyName = context.getString(R.string.camera_maximum_digital_zoom);
                break;
            case "android.scaler.croppingType":
                keyName = context.getString(R.string.camera_cropping_type);
                break;
            case "android.scaler.streamConfigurationMap":
                keyName = context.getString(R.string.camera_supported_resolutions);
                break;
            case "android.sensor.availableTestPatternModes":
                keyName = context.getString(R.string.camera_test_pattern_modes);
                break;
            case "android.sensor.blackLevelPattern":
                keyName = context.getString(R.string.camera_black_level_pattern);
                break;
            case "android.sensor.info.activeArraySize":
                keyName = context.getString(R.string.camera_active_array_size);
                break;
            case "android.sensor.info.colorFilterArrangement":
                keyName = context.getString(R.string.camera_color_filter_arrangement);
                break;
            case "android.sensor.info.exposureTimeRange":
                keyName = context.getString(R.string.camera_exposure_time_range);
                break;
            case "android.sensor.info.maxFrameDuration":
                keyName = context.getString(R.string.camera_maximum_frame_duration);
                break;
            case "android.sensor.info.physicalSize":
                keyName = context.getString(R.string.camera_sensor_size);
                break;
            case "android.sensor.info.pixelArraySize":
                keyName = context.getString(R.string.camera_pixel_array_size);
                break;
            case "android.sensor.info.preCorrectionActiveArraySize":
                keyName = context.getString(R.string.camera_pre_correction_active_array_size);
                break;
            case "android.sensor.info.sensitivityRange":
                keyName = context.getString(R.string.camera_sensitivity_range);
                break;
            case "android.sensor.info.timestampSource":
                keyName = context.getString(R.string.camera_timestamp_source);
                break;
            case "android.sensor.info.whiteLevel":
                keyName = context.getString(R.string.camera_white_level);
                break;
            case "android.sensor.maxAnalogSensitivity":
                keyName = context.getString(R.string.camera_maximum_analog_sensitivity);
                break;
            case "android.sensor.orientation":
                keyName = context.getString(R.string.camera_orientation);
                break;
            case "android.shading.availableModes":
                keyName = context.getString(R.string.camera_shading_modes);
                break;
            case "android.statistics.info.availableFaceDetectModes":
                keyName = context.getString(R.string.camera_face_detection_modes);
                break;
            case "android.statistics.info.availableHotPixelMapModes":
                keyName = context.getString(R.string.camera_hot_pixel_map_modes);
                break;
            case "android.statistics.info.availableLensShadingMapModes":
                keyName = context.getString(R.string.camera_lens_shading_map_modes);
                break;
            case "android.statistics.info.maxFaceCount":
                keyName = context.getString(R.string.camera_maximum_face_detectable);
                break;
            case "android.sync.maxLatency":
                keyName = context.getString(R.string.camera_maximum_latency);
                break;
            case "android.tonemap.availableToneMapModes":
                keyName = context.getString(R.string.camera_tone_map_modes);
                break;
            case "android.tonemap.maxCurvePoints":
                keyName = context.getString(R.string.camera_maximum_curve_points);
                break;
            default:
                keyName = name;
                break;
        }
        return keyName;
    }

    public static String getCameraMP(Size[] size) {
        String finalCameraRes = getMP(size[0], 1);
        int maxSize = size[0].getHeight() * size[0].getWidth();
        for (Size camSize : size) {
            int tempMax = camSize.getHeight() * camSize.getWidth();
            if (tempMax > maxSize) {
                maxSize = tempMax;
                finalCameraRes = getMP(camSize, 1);
            }
        }
        return finalCameraRes;
    }

    public static String getCameraResolution(Size[] size) {
        Size first = size[0];
        if (size.length > 1) {
            Size second = size[size.length - 1];
            if (first.getWidth() > second.getWidth()) {
                return first.getWidth() + "x" + first.getHeight();
            } else {
                return second.getWidth() + "x" + second.getHeight();
            }
        } else {
            return first.getWidth() + "x" + first.getHeight();
        }
    }

    public static String getMP(Size size, int decimalPlaces) {
        float mp = (size.getWidth() * size.getHeight()) / 1000000f;
        if (decimalPlaces == 1) {
            return String.format(Locale.US, "%.1f", mp) + " MP";
        } else if (decimalPlaces == 2) {
            return String.format(Locale.US, "%.2f", mp) + " MP";
        } else {
            return String.format(Locale.US, "%.2f", mp) + " MP";
        }
    }

    private static String getFormattedTemp(String zoneValue) {
        double finalTemp;
        int val = Integer.parseInt(zoneValue.trim());
        if (val >= 10000) {
            finalTemp = val / 1000.0;
        } else if (val >= 1000) {
            finalTemp = val / 100.0;
        } else if (val > 100) {
            finalTemp = val / 10.0;
        } else {
            finalTemp = val;
        }
        finalTemp = Math.abs(finalTemp);
        return new DecimalFormat("##.#").format(finalTemp) + " \u2103";
    }

    public static ArrayList<ThermalInfo> loadThermal() {
        ArrayList<ThermalInfo> thermalList = new ArrayList<>();
        File dir = new File("/sys/devices/virtual/thermal/");
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    File tempFileValue = new File(file.getAbsolutePath() + "/temp");
                    File tempFileName = new File(file.getAbsolutePath() + "/type");
                    BufferedReader bufferedReaderValue = new BufferedReader(new FileReader(tempFileValue));
                    BufferedReader bufferedReaderName = new BufferedReader(new FileReader(tempFileName));
                    String lineName = bufferedReaderName.readLine();
                    String lineValue = bufferedReaderValue.readLine();
                    if (!lineValue.trim().equals("0")) {
                        thermalList.add(new ThermalInfo(lineName, GetDetails.getFormattedTemp(lineValue)));
                    }
                    bufferedReaderName.close();
                    bufferedReaderValue.close();
                } catch (IOException ignored) {
                }
            }
        }
        return thermalList;
    }

    public static Locale getLocale(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        return locale;
    }

}

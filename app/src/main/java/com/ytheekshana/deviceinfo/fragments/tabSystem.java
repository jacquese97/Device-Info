package com.ytheekshana.deviceinfo.fragments;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ytheekshana.deviceinfo.GetDetails;
import com.ytheekshana.deviceinfo.MainActivity;
import com.ytheekshana.deviceinfo.R;
import com.ytheekshana.deviceinfo.SplashActivity;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.Locale;
import java.util.Objects;

public class tabSystem extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabsystem, container, false);
        LinearLayout llayout = rootView.findViewById(R.id.llayout);
        ImageView imgAndroidLogo = rootView.findViewById(R.id.imgAndroidLogo);
        TextView txtAndroidVersionNumber = rootView.findViewById(R.id.txtAndroidVersionNumber);
        TextView txtAndroidVersionName = rootView.findViewById(R.id.txtAndroidVersionName);
        TextView txtAndroidVersionDate = rootView.findViewById(R.id.txtAndroidVersionDate);
        TextView txtRootStatus = rootView.findViewById(R.id.txtRootStatus);

        try {
            int textDisColor = MainActivity.themeColor;
            int lineColor = GetDetails.getThemeColor(Objects.requireNonNull(getContext()), R.attr.colorButtonNormal);
            CardView cardviewRam = rootView.findViewById(R.id.cardviewSystem);
            cardviewRam.setCardBackgroundColor(MainActivity.themeColor);

            switch (Build.VERSION.SDK_INT) {
                case 21:
                case 22:
                    imgAndroidLogo.setImageResource(R.drawable.lollipop);
                    break;
                case 23:
                    imgAndroidLogo.setImageResource(R.drawable.marshmallow);
                    break;
                case 24:
                case 25:
                    imgAndroidLogo.setImageResource(R.drawable.nougat);
                    break;
                case 26:
                case 27:
                    imgAndroidLogo.setImageResource(R.drawable.oreo);
                    break;
                case 28:
                    imgAndroidLogo.setImageResource(R.drawable.pie);
                    break;
            }
            String Aversion = getString(R.string.android) + " " + Build.VERSION.RELEASE;
            txtAndroidVersionNumber.setText(Aversion);
            txtAndroidVersionName.setText(GetDetails.GetOSName(Build.VERSION.SDK_INT, getContext()).toUpperCase());
            String AReleaseDate = getString(R.string.released) + " : " + GetDetails.GetOSReleaseDate(getContext());
            txtAndroidVersionDate.setText(AReleaseDate);
            if (SplashActivity.rootedStatus) {
                txtRootStatus.setText(R.string.rooted);
            } else {
                txtRootStatus.setText(R.string.not_rooted);
            }

            TextView txtAndroidName = new TextView(getContext());
            TextView txtAndroidNamedis = new TextView(getContext());
            View v1 = new View(getContext());
            v1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            v1.setBackgroundColor(lineColor);
            txtAndroidName.setText(R.string.CodeName);
            txtAndroidName.setTypeface(null, Typeface.BOLD);
            txtAndroidName.setTextSize(16);
            txtAndroidName.setPadding(0, 15, 0, 0);
            txtAndroidNamedis.setPadding(0, 0, 0, 15);
            txtAndroidNamedis.setTextColor(textDisColor);
            txtAndroidNamedis.setTextSize(16);
            txtAndroidNamedis.setText(GetDetails.GetOSNameAdvanced(getContext()));
            txtAndroidNamedis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            txtAndroidName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llayout.addView(txtAndroidName);
            llayout.addView(txtAndroidNamedis);
            llayout.addView(v1);

            TextView txtAPILevel = new TextView(getContext());
            TextView txtAPILeveldis = new TextView(getContext());
            View v2 = new View(getContext());
            v2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            v2.setBackgroundColor(lineColor);
            txtAPILevel.setText(R.string.APILevel);
            txtAPILevel.setTypeface(null, Typeface.BOLD);
            txtAPILevel.setTextSize(16);
            txtAPILevel.setPadding(0, 15, 0, 0);
            txtAPILeveldis.setPadding(0, 0, 0, 15);
            txtAPILeveldis.setTextColor(textDisColor);
            txtAPILeveldis.setTextSize(16);
            txtAPILeveldis.setText(String.valueOf(Build.VERSION.SDK_INT));
            txtAPILeveldis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            txtAPILevel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llayout.addView(txtAPILevel);
            llayout.addView(txtAPILeveldis);
            llayout.addView(v2);

            if (Build.VERSION.SDK_INT >= 23) {
                TextView txtSecurityPatch = new TextView(getContext());
                TextView txtSecurityPatchdis = new TextView(getContext());
                View v3 = new View(getContext());
                v3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
                v3.setBackgroundColor(lineColor);
                txtSecurityPatch.setText(R.string.SecurityPatchLevel);
                txtSecurityPatch.setTypeface(null, Typeface.BOLD);
                txtSecurityPatch.setTextSize(16);
                txtSecurityPatch.setPadding(0, 15, 0, 0);
                txtSecurityPatchdis.setPadding(0, 0, 0, 15);
                txtSecurityPatchdis.setTextColor(textDisColor);
                txtSecurityPatchdis.setTextSize(16);
                txtSecurityPatchdis.setText(Build.VERSION.SECURITY_PATCH);
                txtSecurityPatchdis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                txtSecurityPatch.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                llayout.addView(txtSecurityPatch);
                llayout.addView(txtSecurityPatchdis);
                llayout.addView(v3);
            }

            TextView txtBootloader = new TextView(getContext());
            TextView txtBootloaderdis = new TextView(getContext());
            View v4 = new View(getContext());
            v4.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            v4.setBackgroundColor(lineColor);
            txtBootloader.setText(R.string.Bootloader);
            txtBootloader.setTypeface(null, Typeface.BOLD);
            txtBootloader.setTextSize(16);
            txtBootloader.setPadding(0, 15, 0, 0);
            txtBootloaderdis.setPadding(0, 0, 0, 15);
            txtBootloaderdis.setTextColor(textDisColor);
            txtBootloaderdis.setTextSize(16);
            txtBootloaderdis.setText(Build.BOOTLOADER);
            txtBootloaderdis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            txtBootloader.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llayout.addView(txtBootloader);
            llayout.addView(txtBootloaderdis);
            llayout.addView(v4);

            TextView txtBuildNumber = new TextView(getContext());
            TextView txtBuildNumberdis = new TextView(getContext());
            View v5 = new View(getContext());
            v5.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            v5.setBackgroundColor(lineColor);
            txtBuildNumber.setText(R.string.BuildNumber);
            txtBuildNumber.setTypeface(null, Typeface.BOLD);
            txtBuildNumber.setTextSize(16);
            txtBuildNumber.setPadding(0, 15, 0, 0);
            txtBuildNumberdis.setPadding(0, 0, 0, 15);
            txtBuildNumberdis.setTextColor(textDisColor);
            txtBuildNumberdis.setTextSize(16);
            txtBuildNumberdis.setText(Build.DISPLAY);
            txtBuildNumberdis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            txtBuildNumber.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llayout.addView(txtBuildNumber);
            llayout.addView(txtBuildNumberdis);
            llayout.addView(v5);

            TextView txtRadioVersion = new TextView(getContext());
            TextView txtRadioVersiondis = new TextView(getContext());
            View v6 = new View(getContext());
            v6.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            v6.setBackgroundColor(lineColor);
            txtRadioVersion.setText(R.string.Baseband);
            txtRadioVersion.setTypeface(null, Typeface.BOLD);
            txtRadioVersion.setTextSize(16);
            txtRadioVersion.setPadding(0, 15, 0, 0);
            txtRadioVersiondis.setPadding(0, 0, 0, 15);
            txtRadioVersiondis.setTextColor(textDisColor);
            txtRadioVersiondis.setTextSize(16);
            txtRadioVersiondis.setText(Build.getRadioVersion());
            txtRadioVersiondis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            txtRadioVersion.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llayout.addView(txtRadioVersion);
            llayout.addView(txtRadioVersiondis);
            llayout.addView(v6);

            TextView txtAndroidRuntime = new TextView(getContext());
            TextView txtAndroidRuntimedis = new TextView(getContext());
            View v7 = new View(getContext());
            v7.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            v7.setBackgroundColor(lineColor);
            txtAndroidRuntime.setText(R.string.java_vm);
            txtAndroidRuntime.setTypeface(null, Typeface.BOLD);
            txtAndroidRuntime.setTextSize(16);
            txtAndroidRuntime.setPadding(0, 15, 0, 0);
            txtAndroidRuntimedis.setPadding(0, 0, 0, 15);
            txtAndroidRuntimedis.setTextColor(textDisColor);
            txtAndroidRuntimedis.setTextSize(16);
            txtAndroidRuntimedis.setText(SplashActivity.androidRuntime);
            txtAndroidRuntimedis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            txtAndroidRuntime.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llayout.addView(txtAndroidRuntime);
            llayout.addView(txtAndroidRuntimedis);
            llayout.addView(v7);

            TextView txtKernelVersion = new TextView(getContext());
            TextView txtKernelVersiondis = new TextView(getContext());
            View v8 = new View(getContext());
            v8.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            v8.setBackgroundColor(lineColor);
            txtKernelVersion.setText(R.string.Kernel);
            txtKernelVersion.setTypeface(null, Typeface.BOLD);
            txtKernelVersion.setTextSize(16);
            txtKernelVersion.setPadding(0, 15, 0, 0);
            txtKernelVersiondis.setPadding(0, 0, 0, 15);
            txtKernelVersiondis.setTextColor(textDisColor);
            txtKernelVersiondis.setTextSize(16);
            txtKernelVersiondis.setText(SplashActivity.kernelVersion);
            txtKernelVersiondis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            txtKernelVersion.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llayout.addView(txtKernelVersion);
            llayout.addView(txtKernelVersiondis);
            llayout.addView(v8);

            TextView txtLanguage = new TextView(getContext());
            TextView txtLanguagedis = new TextView(getContext());
            View v9 = new View(getContext());
            v9.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            v9.setBackgroundColor(lineColor);
            txtLanguage.setText(R.string.Language);
            txtLanguage.setTypeface(null, Typeface.BOLD);
            txtLanguage.setTextSize(16);
            txtLanguage.setPadding(0, 15, 0, 0);
            txtLanguagedis.setPadding(0, 0, 0, 15);
            txtLanguagedis.setTextColor(textDisColor);
            txtLanguagedis.setTextSize(16);
            String language = Locale.getDefault().getDisplayLanguage() + " (" + Locale.getDefault().toString() + ")";
            txtLanguagedis.setText(language);
            txtLanguagedis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            txtLanguage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llayout.addView(txtLanguage);
            llayout.addView(txtLanguagedis);
            llayout.addView(v9);

            TextView txtOpenGLES = new TextView(getContext());
            TextView txtOpenGLESdis = new TextView(getContext());
            View v10 = new View(getContext());
            v10.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            v10.setBackgroundColor(lineColor);
            txtOpenGLES.setText(R.string.OpenGL);
            txtOpenGLES.setTypeface(null, Typeface.BOLD);
            txtOpenGLES.setTextSize(16);
            txtOpenGLES.setPadding(0, 15, 0, 0);
            txtOpenGLESdis.setPadding(0, 0, 0, 15);
            txtOpenGLESdis.setTextColor(textDisColor);
            txtOpenGLESdis.setTextSize(16);
            txtOpenGLESdis.setText(SplashActivity.glVersion);
            txtOpenGLESdis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            txtOpenGLES.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llayout.addView(txtOpenGLES);
            llayout.addView(txtOpenGLESdis);
            llayout.addView(v10);

            TextView txtRootAccess = new TextView(getContext());
            TextView txtRootAccessdis = new TextView(getContext());
            View v12 = new View(getContext());
            v12.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            v12.setBackgroundColor(lineColor);
            txtRootAccess.setText(R.string.RootAccess);
            txtRootAccess.setTypeface(null, Typeface.BOLD);
            txtRootAccess.setTextSize(16);
            txtRootAccess.setPadding(0, 15, 0, 0);
            txtRootAccessdis.setPadding(0, 0, 0, 15);
            txtRootAccessdis.setTextColor(textDisColor);
            txtRootAccessdis.setTextSize(16);
            txtRootAccessdis.setText(SplashActivity.rootedStatus ? getString(R.string.yes) : getString(R.string.no));
            txtRootAccessdis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            txtRootAccess.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llayout.addView(txtRootAccess);
            llayout.addView(txtRootAccessdis);
            llayout.addView(v12);

            TextView txtSelinux = new TextView(getContext());
            TextView txtSelinuxdis = new TextView(getContext());
            View v14 = new View(getContext());
            v14.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            v14.setBackgroundColor(lineColor);
            txtSelinux.setText(R.string.SELinux);
            txtSelinux.setTypeface(null, Typeface.BOLD);
            txtSelinux.setTextSize(16);
            txtSelinux.setPadding(0, 15, 0, 0);
            txtSelinuxdis.setPadding(0, 0, 0, 15);
            txtSelinuxdis.setTextColor(textDisColor);
            txtSelinuxdis.setTextSize(16);
            txtSelinuxdis.setText(SplashActivity.selinuxMode);
            txtSelinuxdis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            txtSelinux.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llayout.addView(txtSelinux);
            llayout.addView(txtSelinuxdis);
            llayout.addView(v14);

            TextView txtSystemUptime = new TextView(getContext());
            final TextView txtSystemUptimedis = new TextView(getContext());
            View v13 = new View(getContext());
            v13.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            v13.setBackgroundColor(lineColor);
            txtSystemUptime.setText(R.string.Uptime);
            txtSystemUptime.setTypeface(null, Typeface.BOLD);
            txtSystemUptime.setTextSize(16);
            txtSystemUptime.setPadding(0, 15, 0, 0);
            txtSystemUptimedis.setPadding(0, 0, 0, 15);
            txtSystemUptimedis.setTextColor(textDisColor);
            txtSystemUptimedis.setTextSize(16);
            txtSystemUptimedis.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            txtSystemUptime.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            llayout.addView(txtSystemUptime);
            llayout.addView(txtSystemUptimedis);
            llayout.addView(v13);

            final Handler h = new Handler();
            h.postDelayed(new Runnable() {
                Long uptime;

                @Override
                public void run() {
                    uptime = SystemClock.elapsedRealtime();
                    txtSystemUptimedis.setText(GetDetails.getTime(uptime));
                    h.postDelayed(this, 1000);
                }
            }, 1000);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rootView;
    }
}


package com.ytheekshana.deviceinfo.tests;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.ytheekshana.deviceinfo.GetDetails;
import com.ytheekshana.deviceinfo.R;

import java.util.ArrayList;
import java.util.Objects;

public class LoudSpeakerTestActivity extends AppCompatActivity {
    Context context;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editPrefs;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            int themeId = sharedPrefs.getInt("ThemeBar", R.style.AppTheme);
            int themeColor = sharedPrefs.getInt("accent_color_dialog", Color.parseColor("#2196f3"));
            int themeColorDark = GetDetails.getDarkColor(this, themeColor);
            setTheme(themeId);

            if (sharedPrefs.getInt("ThemeBar", 0) != R.style.AppThemeDark) {
                Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(themeColor));
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getWindow().setStatusBarColor(themeColorDark);
            }
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
            ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(getString(R.string.app_name), icon, themeColor);
            setTaskDescription(taskDescription);

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_test_loudspeaker);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

            context = this;
            sharedPrefs = getSharedPreferences("tests", MODE_PRIVATE);
            editPrefs = sharedPrefs.edit();

            ImageButton imgbtn_failed = findViewById(R.id.imgbtn_failed);
            ImageButton imgbtn_success = findViewById(R.id.imgbtn_success);
            imgbtn_failed.setOnClickListener(v -> {
                editPrefs.putInt("loudspeaker_test_status", 0);
                editPrefs.apply();
                editPrefs.commit();
                finish();
            });
            imgbtn_success.setOnClickListener(v -> {
                editPrefs.putInt("loudspeaker_test_status", 1);
                editPrefs.apply();
                editPrefs.commit();
                finish();
            });

            mediaPlayer = new MediaPlayer();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Permissions.check(context, Manifest.permission.READ_EXTERNAL_STORAGE, null, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        testLoudspeaker();
                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                        finish();
                    }
                });
            } else {
                testLoudspeaker();
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void testLoudspeaker() {
        try {
            Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            mediaPlayer.setDataSource(this, defaultRingtoneUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        super.onPause();
    }
}

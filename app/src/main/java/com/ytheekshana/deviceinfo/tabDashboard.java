package com.ytheekshana.deviceinfo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.snackbar.Snackbar;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class tabDashboard extends Fragment {
    private TextView txtUsedRam, txtFreeRam, txtBatteryPerce, txtBatteryStatus, txtSensorCount, txtAppCount, txtBatteryTitle;
    private ProgressBar progressBarRom, progressBarInStorage, progressBarExStorage, progressBarBattery;
    private int startRAM, startROM, startInStorage, startExStorage, batlevel;
    private ArcProgress arcProgressRam;
    private LineChart lineChartRam;
    private float usedRam = 0;
    private Handler handlerChart, handlerRam;
    private Runnable runnableChart;
    private RecyclerView recyclerCPU;
    private ArrayList<CPUCoreInfo> cpuCoreList, cpuCoreList2;
    private CPUCoreAdapter cpuCoreAdapter;
    private ScheduledExecutorService scheduledExecutorService;
    private Context batteryContext;

    @Override
    public void onDestroyView() {
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdownNow();
        }
        super.onDestroyView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabdashboard, container, false);

        try {
            ImageView imgROM = rootView.findViewById(R.id.imageROM);
            ImageView imgInStorage = rootView.findViewById(R.id.imageInStorage);
            ImageView imgExStorage = rootView.findViewById(R.id.imageExStorage);
            ImageView imgBattery = rootView.findViewById(R.id.imageBattery);

            ImageView imgSensor = rootView.findViewById(R.id.imageSensor);
            ImageView imgApps = rootView.findViewById(R.id.imageApps);

            ColorFilter accentFilter = new LightingColorFilter(Color.BLACK, MainActivity.themeColor);
            imgROM.setColorFilter(accentFilter);
            imgInStorage.setColorFilter(accentFilter);
            imgExStorage.setColorFilter(accentFilter);
            imgBattery.setColorFilter(accentFilter);
            imgSensor.setColorFilter(accentFilter);
            imgApps.setColorFilter(accentFilter);

            CardView cardviewRam = rootView.findViewById(R.id.cardviewRam);
            cardviewRam.setCardBackgroundColor(MainActivity.themeColor);

            final CardView cardRom = rootView.findViewById(R.id.cardviewRom);
            final CardView cardInternalStorage = rootView.findViewById(R.id.cardviewInStorage);
            final CardView cardExternalStorage = rootView.findViewById(R.id.cardviewExStorage);
            final CardView cardBattery = rootView.findViewById(R.id.cardviewBattery);
            final CardView cardSensor = rootView.findViewById(R.id.cardviewSensor);
            final CardView cardApps = rootView.findViewById(R.id.cardviewApp);

            txtUsedRam = rootView.findViewById(R.id.txtUsedRam);
            txtFreeRam = rootView.findViewById(R.id.txtFreeRam);
            TextView txtTotalRam = rootView.findViewById(R.id.txtTotalRam);
            TextView txtRomPerce = rootView.findViewById(R.id.txtROMPerc);
            TextView txtRomStatus = rootView.findViewById(R.id.txtROMStatus);
            txtBatteryPerce = rootView.findViewById(R.id.txtBatteryPerc);
            txtBatteryStatus = rootView.findViewById(R.id.txtBatteryStatus);
            txtBatteryTitle = rootView.findViewById(R.id.txtBatteryTitle);
            TextView txtInStoragePerce = rootView.findViewById(R.id.txtInStoragePerc);
            TextView txtInStorageStatus = rootView.findViewById(R.id.txtInStorageStatus);
            TextView txtExStoragePerce = rootView.findViewById(R.id.txtExStoragePerc);
            TextView txtExStorageStatus = rootView.findViewById(R.id.txtExStorageStatus);
            txtSensorCount = rootView.findViewById(R.id.txtSensorCount);
            txtAppCount = rootView.findViewById(R.id.txtAppCount);

            lineChartRam = rootView.findViewById(R.id.lineChartRam);
            lineChartRam.setDrawGridBackground(false);
            lineChartRam.getDescription().setEnabled(false);
            lineChartRam.setBackgroundColor(Color.TRANSPARENT);
            LineData data = new LineData();
            data.setValueTextColor(Color.WHITE);
            lineChartRam.setData(data);
            lineChartRam.getLegend().setEnabled(false);
            lineChartRam.setTouchEnabled(false);

            XAxis xl = lineChartRam.getXAxis();
            xl.setEnabled(false);

            YAxis leftAxis = lineChartRam.getAxisLeft();
            leftAxis.setEnabled(false);

            YAxis rightAxis = lineChartRam.getAxisRight();
            rightAxis.setLabelCount(3);
            rightAxis.setTextColor(Color.WHITE);
            rightAxis.setDrawGridLines(false);
            rightAxis.setTextSize(9);

            arcProgressRam = rootView.findViewById(R.id.arcProgressRam);
            arcProgressRam.setUnfinishedStrokeColor(MainActivity.themeColorDark);

            progressBarRom = rootView.findViewById(R.id.progressRom);
            DrawableCompat.setTint(progressBarRom.getProgressDrawable(), MainActivity.themeColor);

            progressBarBattery = rootView.findViewById(R.id.progressBattery);
            DrawableCompat.setTint(progressBarBattery.getProgressDrawable(), MainActivity.themeColor);

            progressBarInStorage = rootView.findViewById(R.id.progressInStorage);
            DrawableCompat.setTint(progressBarInStorage.getProgressDrawable(), MainActivity.themeColor);

            progressBarExStorage = rootView.findViewById(R.id.progressExStorage);
            DrawableCompat.setTint(progressBarExStorage.getProgressDrawable(), MainActivity.themeColor);

            startRAM = (int) SplashActivity.usedRamPercentage;
            txtUsedRam.setText(String.valueOf((int) SplashActivity.usedRam));
            txtFreeRam.setText(String.valueOf((int) SplashActivity.availableRam));
            usedRam = (float) SplashActivity.usedRam;

            String totalRamSpan = "RAM - " + (int) SplashActivity.totalRam + " MB Total";
            SpannableString ssTotalRam = new SpannableString(totalRamSpan);
            ssTotalRam.setSpan(new RelativeSizeSpan(0.7f), totalRamSpan.length() - 8, totalRamSpan.length(), 0); // set size
            txtTotalRam.setText(ssTotalRam);

            startROM = (int) SplashActivity.usedRomPercentage;
            String setRom = "Free: " + String.format(Locale.US, "%.1f", SplashActivity.availableRom) + " GB,  Total: " + String.format(Locale.US, "%.1f", SplashActivity.totalRom) + " GB";
            txtRomStatus.setText(setRom);
            String storage_percentageRom = (int) SplashActivity.usedRomPercentage + "%";
            txtRomPerce.setText(storage_percentageRom);

            startInStorage = (int) SplashActivity.usedInternalPercentage;
            String setInStorage = "Free: " + String.format(Locale.US, "%.1f", SplashActivity.availableInternalStorage) + " GB,  Total: " + String.format(Locale.US, "%.1f", SplashActivity.totalInternalStorage) + " GB";
            txtInStorageStatus.setText(setInStorage);
            String in_storage_percentage = (int) SplashActivity.usedInternalPercentage + "%";
            txtInStoragePerce.setText(in_storage_percentage);

            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED) && ContextCompat.getExternalFilesDirs(Objects.requireNonNull(getContext()), null).length >= 2) {
                cardExternalStorage.setVisibility(View.VISIBLE);
                startExStorage = (int) SplashActivity.usedExternalPercentage;
                String setExStorage = "Free: " + String.format(Locale.US, "%.1f", SplashActivity.availableExternalStorage) + " GB,  Total: " + String.format(Locale.US, "%.1f", SplashActivity.totalExternalStorage) + " GB";
                txtExStorageStatus.setText(setExStorage);
                String ex_storage_percentage = (int) SplashActivity.usedExternalPercentage + "%";
                txtExStoragePerce.setText(ex_storage_percentage);
            } else {
                cardExternalStorage.setVisibility(View.GONE);
            }

            final MemoryInfo memoryInfo = new MemoryInfo(getContext());
            handlerRam = new Handler();
            Runnable runnable = new Runnable() {
                public void run() {
                    memoryInfo.Ram();
                    arcProgressRam.setProgress((int) memoryInfo.getUsedRamPercentage());
                    usedRam = (float) memoryInfo.getUsedRam();
                    SplashActivity.usedRam = memoryInfo.getUsedRam();
                    txtUsedRam.setText(String.valueOf((int) memoryInfo.getUsedRam()));
                    txtFreeRam.setText(String.valueOf((int) memoryInfo.getAvailableRam()));
                    handlerRam.postDelayed(this, 1000);
                }
            };
            handlerRam.postDelayed(runnable, 1000);

            handlerChart = new Handler();
            runnableChart = new Runnable() {
                public void run() {
                    addEntry();
                    handlerChart.postDelayed(this, 1000);
                }
            };
            handlerChart.postDelayed(runnableChart, 0);

            recyclerCPU = rootView.findViewById(R.id.recyclerCPU);
            recyclerCPU.setItemAnimator(null);
            loadCPUCore();
            int columns;
            if (Runtime.getRuntime().availableProcessors() == 2) {
                columns = 2;
            } else {
                columns = 4;
            }
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), columns);
            cpuCoreAdapter = new CPUCoreAdapter(getContext(), cpuCoreList);
            recyclerCPU.setLayoutManager(layoutManager);
            recyclerCPU.setAdapter(cpuCoreAdapter);

            if (cpuCoreList.isEmpty()) {
                Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(R.id.cordmain), "No Thermal Data", Snackbar.LENGTH_INDEFINITE);
                SnackbarHelper.configSnackbar(getContext(), snackbar);
                snackbar.show();
            } else {
                cpuCoreList2 = new ArrayList<>();
                scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
                scheduledExecutorService.scheduleAtFixedRate(() -> {
                    loadCPUCore();
                    cpuCoreList2 = cpuCoreList;
                    recyclerCPU.post(() ->
                            cpuCoreAdapter.updateCPUCoreListItems(cpuCoreList2)
                    );
                }, 1, 2, TimeUnit.SECONDS);
            }


            final com.ytheekshana.deviceinfo.BounceInterpolator bounceInterpolator = new com.ytheekshana.deviceinfo.BounceInterpolator(0.2, 20);
            cardRom.setOnClickListener(v -> {
                Animation animRom = AnimationUtils.loadAnimation(getContext(), R.anim.bounce_dash);
                animRom.setInterpolator(bounceInterpolator);
                cardRom.startAnimation(animRom);
            });
            cardInternalStorage.setOnClickListener(v -> {
                Animation animInSto = AnimationUtils.loadAnimation(getContext(), R.anim.bounce_dash);
                animInSto.setInterpolator(bounceInterpolator);
                cardInternalStorage.startAnimation(animInSto);
            });
            cardExternalStorage.setOnClickListener(v -> {
                Animation animExSto = AnimationUtils.loadAnimation(getContext(), R.anim.bounce_dash);
                animExSto.setInterpolator(bounceInterpolator);
                cardExternalStorage.startAnimation(animExSto);
            });
            cardBattery.setOnClickListener(v -> {
                Animation animBattery = AnimationUtils.loadAnimation(getContext(), R.anim.bounce_dash);
                animBattery.setInterpolator(bounceInterpolator);
                cardBattery.startAnimation(animBattery);
            });
            cardSensor.setOnClickListener(v -> {
                Animation animSensor = AnimationUtils.loadAnimation(getContext(), R.anim.bounce_dash);
                animSensor.setInterpolator(bounceInterpolator);
                cardSensor.startAnimation(animSensor);
            });
            cardApps.setOnClickListener(v -> {
                Animation animApps = AnimationUtils.loadAnimation(getContext(), R.anim.bounce_dash);
                animApps.setInterpolator(bounceInterpolator);
                cardApps.startAnimation(animApps);
            });

        } catch (Exception ignored) {
        }

        return rootView;
    }

    private BroadcastReceiver batteryBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            batlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int batstatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            int voltage = intent.getIntExtra("voltage", 0);
            int temperature = (intent.getIntExtra("temperature", 0)) / 10;
            String setBattery = "Voltage: " + voltage + "mV,  Temperature: " + temperature + " \u2103";
            txtBatteryStatus.setText(setBattery);
            String battery_percentage = batlevel + "%";
            txtBatteryPerce.setText(battery_percentage);
            if (batstatus == BatteryManager.BATTERY_STATUS_CHARGING) {
                progressBarBattery.setIndeterminate(true);
                DrawableCompat.setTint(progressBarBattery.getIndeterminateDrawable(), MainActivity.themeColor);
                txtBatteryTitle.setText(R.string.DashBatteryCharging);
            } else {
                progressBarBattery.setIndeterminate(false);
                txtBatteryTitle.setText(R.string.DashBattery);
            }
            progressBarBattery.setProgress(batlevel * 10);

        }
    };

    private void animateTextView(int finalValue, final TextView textview) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, finalValue);
        valueAnimator.setDuration(800);
        valueAnimator.addUpdateListener(valueAnimator1 -> textview.setText(valueAnimator1.getAnimatedValue().toString()));
        valueAnimator.start();
    }

    private void addEntry() {
        LineData data = lineChartRam.getData();
        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            data.addEntry(new Entry(set.getEntryCount(), usedRam), 0);
            data.notifyDataChanged();
            lineChartRam.notifyDataSetChanged();
            lineChartRam.setVisibleXRangeMaximum(20);
            lineChartRam.moveViewToX(data.getEntryCount());
        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set.setColor(Color.WHITE);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setLineWidth(2f);
        set.setDrawValues(false);
        return set;
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter batteryIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryContext = Objects.requireNonNull(getActivity()).getApplicationContext();
        batteryContext.registerReceiver(batteryBroadcastReceiver, batteryIntentFilter);

        Intent batteryIntent = batteryContext.registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (batteryIntent != null) {
            batlevel = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        }

        if (runnableChart != null) {
            handlerChart.postDelayed(runnableChart, 0);
        }
        if (recyclerCPU != null) {
            LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.recycler_layout_animation);
            recyclerCPU.setLayoutAnimation(controller);
            recyclerCPU.scheduleLayoutAnimation();
        }
        if (arcProgressRam != null) {
            ObjectAnimator arcProgressAnimatorRAM = ObjectAnimator.ofInt(arcProgressRam, "progress", 0, startRAM);
            arcProgressAnimatorRAM.setDuration(800);
            arcProgressAnimatorRAM.setInterpolator(new DecelerateInterpolator());
            arcProgressAnimatorRAM.start();
        }
        if (progressBarRom != null) {
            ObjectAnimator progressAnimatorROM = ObjectAnimator.ofInt(progressBarRom, "progress", 0, startROM * 10);
            progressAnimatorROM.setDuration(800);
            progressAnimatorROM.start();
        }

        if (progressBarInStorage != null) {
            ObjectAnimator progressAnimatorInStorage = ObjectAnimator.ofInt(progressBarInStorage, "progress", 0, startInStorage * 10);
            progressAnimatorInStorage.setDuration(800);
            progressAnimatorInStorage.start();
        }
        if (progressBarExStorage != null) {
            ObjectAnimator progressAnimatorExStorage = ObjectAnimator.ofInt(progressBarExStorage, "progress", 0, startExStorage * 10);
            progressAnimatorExStorage.setDuration(800);
            progressAnimatorExStorage.start();
        }
        if (progressBarBattery != null) {
            ObjectAnimator progressAnimatorBattery = ObjectAnimator.ofInt(progressBarBattery, "progress", 0, batlevel * 10);
            progressAnimatorBattery.setDuration(800);
            progressAnimatorBattery.start();
        }
        if (txtAppCount != null) {
            animateTextView(SplashActivity.numberOfInstalledApps, txtAppCount);
        }
        if (txtSensorCount != null) {
            animateTextView(SplashActivity.numberOfSensors, txtSensorCount);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (runnableChart != null) {
            handlerChart.removeCallbacks(runnableChart);
        }
        if (batteryBroadcastReceiver != null) {
            batteryContext.unregisterReceiver(batteryBroadcastReceiver);
        }
    }

    private void loadCPUCore() {
        cpuCoreList = new ArrayList<>();
        for (int corecount = 0; corecount < Runtime.getRuntime().availableProcessors(); corecount++) {
            try {
                double currentFreq;
                RandomAccessFile readerCurFreq;
                readerCurFreq = new RandomAccessFile("/sys/devices/system/cpu/cpu" + corecount + "/cpufreq/scaling_cur_freq", "r");
                String curfreg = readerCurFreq.readLine();
                currentFreq = Double.parseDouble(curfreg) / 1000;
                readerCurFreq.close();
                cpuCoreList.add(new CPUCoreInfo("Core " + corecount, (int) currentFreq + " Mhz"));

            } catch (Exception ex) {
                cpuCoreList.add(new CPUCoreInfo("Core " + corecount, "Idle"));
            }
        }
    }
}



package com.ytheekshana.deviceinfo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class tabSensor extends Fragment {

    private String getcount;
    private TextView sensor_count;
    private Thread loadSensors;
    private SwipeRefreshLayout swipesensorlist;
    private RecyclerView recyclerSensors;
    Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.tabsensor, container, false);
        recyclerSensors = rootView.findViewById(R.id.recyclerSensors);
        sensor_count = rootView.findViewById(R.id.sensor_count);
        swipesensorlist = rootView.findViewById(R.id.swipesensorlist);
        swipesensorlist.setColorSchemeColors(MainActivity.themeColor);
        swipesensorlist.setOnRefreshListener(() -> new Thread(loadSensors).start());

        getcount = SplashActivity.numberOfSensors + " " + getString(R.string.sensors_are_available);

        loadSensors = new Thread() {
            @Override
            public void run() {
                swipesensorlist.post(() -> {
                    if (!swipesensorlist.isRefreshing()) {
                        swipesensorlist.setRefreshing(true);
                    }
                });

                final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                final RecyclerView.Adapter sensorAdapter = new SensorAdapter(context, getAllSensors());
                recyclerSensors.post(() -> {
                    recyclerSensors.setLayoutManager(layoutManager);
                    recyclerSensors.setAdapter(sensorAdapter);
                });
                sensor_count.post(() -> sensor_count.setText(getcount));
                swipesensorlist.post(() -> {
                    if (swipesensorlist.isRefreshing()) {
                        swipesensorlist.setRefreshing(false);
                    }
                });
            }
        };
        loadSensors.start();
        return rootView;
    }

    private ArrayList<SensorInfo> getAllSensors() {

        ArrayList<SensorInfo> allsensors = new ArrayList<>();
        SensorManager mSensorManager = (SensorManager) Objects.requireNonNull(context.getSystemService(Context.SENSOR_SERVICE));
        List<Sensor> deviceSensors = Objects.requireNonNull(mSensorManager).getSensorList(Sensor.TYPE_ALL);

        for (Sensor s : deviceSensors) {
            String sensorName = s.getName();
            String sensorVendor = getString(R.string.vendor) + " : " + s.getVendor();
            String sensorType = getString(R.string.type) + " : " + GetDetails.GetSensorType(s.getType(), context);
            String wakup = s.isWakeUpSensor() ? getString(R.string.yes) : getString(R.string.no);
            String wakeUpType = getString(R.string.wake_up_sensor) + " : " + wakup;
            String sensorPower = getString(R.string.power) + " : " + s.getPower() + "mA";
            allsensors.add(new SensorInfo(sensorName, sensorVendor, sensorType, wakeUpType, sensorPower));
        }
        return allsensors;
    }
}
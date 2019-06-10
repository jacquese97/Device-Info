package com.ytheekshana.deviceinfo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ytheekshana.deviceinfo.adapters.ThermalAdapter;
import com.ytheekshana.deviceinfo.GetDetails;
import com.ytheekshana.deviceinfo.models.ThermalInfo;
import com.ytheekshana.deviceinfo.R;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class tabThermal extends Fragment {
    private Context context;
    private ArrayList<ThermalInfo> thermalList2;
    private ThermalAdapter thermalAdapter;
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        context = null;
        super.onDetach();
    }

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
        View rootView = inflater.inflate(R.layout.tabthermal, container, false);

        try {
            final RecyclerView recyclerThermal = rootView.findViewById(R.id.recyclerThermal);
            recyclerThermal.setItemAnimator(null);
            ArrayList<ThermalInfo> thermalList = GetDetails.loadThermal();

            GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
            thermalAdapter = new ThermalAdapter(context, thermalList);
            recyclerThermal.setLayoutManager(layoutManager);
            recyclerThermal.setAdapter(thermalAdapter);

            CardView cardNoThermal = rootView.findViewById(R.id.cardNoThermal);
            if (thermalList.isEmpty()) {
                cardNoThermal.setVisibility(View.VISIBLE);
            } else {

                thermalList2 = new ArrayList<>();
                scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
                scheduledExecutorService.scheduleAtFixedRate(() -> {
                    thermalList2 = GetDetails.loadThermal();
                    recyclerThermal.post(() ->
                            thermalAdapter.updateThermalListItems(thermalList2)
                    );
                }, 1, 2, TimeUnit.SECONDS);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rootView;
    }
}

package com.ytheekshana.deviceinfo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ytheekshana.deviceinfo.models.ThermalInfo;
import com.ytheekshana.deviceinfo.R;
import com.ytheekshana.deviceinfo.diffcallbacks.ThermalDiffCallback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ThermalAdapter extends RecyclerView.Adapter<ThermalAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ThermalInfo> mDataSet;

    public ThermalAdapter(Context context, ArrayList<ThermalInfo> list) {
        mContext = context;
        mDataSet = list;
    }

    public void updateThermalListItems(ArrayList<ThermalInfo> thermal) {
        final ThermalDiffCallback diffCallback = new ThermalDiffCallback(this.mDataSet, thermal);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.mDataSet.clear();
        this.mDataSet.addAll(thermal);
        diffResult.dispatchUpdatesTo(this);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtThermalName;
        TextView txtThermalValue;

        ViewHolder(View view) {
            super(view);
            txtThermalName = view.findViewById(R.id.txtThermalName);
            txtThermalValue = view.findViewById(R.id.txtThermalValue);
        }
    }

    @NonNull
    @Override
    public ThermalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.thermal_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String thermalName = mDataSet.get(position).getThermalName();
        final String thermalValue = mDataSet.get(position).getThermalValue();

        holder.txtThermalName.setText(thermalName);
        holder.txtThermalValue.setText(thermalValue);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
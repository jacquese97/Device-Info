package com.ytheekshana.deviceinfo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ytheekshana.deviceinfo.diffcallbacks.CPUCoreDiffCallback;
import com.ytheekshana.deviceinfo.MainActivity;
import com.ytheekshana.deviceinfo.models.CPUCoreInfo;
import com.ytheekshana.deviceinfo.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CPUCoreAdapter extends RecyclerView.Adapter<CPUCoreAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<CPUCoreInfo> mDataSet;

    public CPUCoreAdapter(Context context, ArrayList<CPUCoreInfo> list) {
        mContext = context;
        mDataSet = list;
    }

    public void updateCPUCoreListItems(ArrayList<CPUCoreInfo> cpucore) {
        final CPUCoreDiffCallback diffCallback = new CPUCoreDiffCallback(this.mDataSet, cpucore);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.mDataSet.clear();
        this.mDataSet.addAll(cpucore);
        diffResult.dispatchUpdatesTo(this);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCoreName;
        TextView txtCoreValue;
        CardView cardCPUCoreList;

        ViewHolder(View view) {
            super(view);
            txtCoreName = view.findViewById(R.id.txtCoreName);
            txtCoreValue = view.findViewById(R.id.txtCoreValue);
            cardCPUCoreList = view.findViewById(R.id.cardCPUCoreList);
        }
    }

    @NonNull
    @Override
    public CPUCoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cpucore_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String coreName = mDataSet.get(position).getCoreName();
        final String coreValue = mDataSet.get(position).getCoreValue();

        holder.txtCoreName.setText(coreName);
        holder.txtCoreValue.setText(coreValue);
        holder.cardCPUCoreList.setCardBackgroundColor(MainActivity.themeColor);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
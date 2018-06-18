package com.ytheekshana.deviceinfo;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.Locale;
import java.util.Objects;

public class tabMemory extends Fragment {

    int TextDisColor, LineColor;
    TextView txtRamTotal, txtRamFree, txtRamUsed, txtRomTotal, txtRomFree, txtRomUsed,
            txtInStorageTotal, txtInStorageFree, txtInStorageUsed,
            txtExStorageTotal, txtExStorageFree, txtExStorageUsed;
    DonutProgress progressRam, progressRom, progressInStorage, progressExStorage;
    Double ARam, URam, TRam, FreeSto, TotalSto, UsedSto, UsedPerc;
    CardView cardExStorage;
    int UPerc;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabmemory, container, false);

        TextDisColor = GetDetails.getThemeColor(Objects.requireNonNull(getContext()), R.attr.colorAccent);
        LineColor = GetDetails.getThemeColor(Objects.requireNonNull(getContext()), R.attr.colorButtonNormal);

        try {

            txtRamTotal = rootView.findViewById(R.id.txtRamFree);
            txtRamFree = rootView.findViewById(R.id.txtRamUsed);
            txtRamUsed = rootView.findViewById(R.id.txtRamTotal);
            progressRam = rootView.findViewById(R.id.progressRam);

            txtRomTotal = rootView.findViewById(R.id.txtRomFree);
            txtRomFree = rootView.findViewById(R.id.txtRomUsed);
            txtRomUsed = rootView.findViewById(R.id.txtRomTotal);
            progressRom = rootView.findViewById(R.id.progressRom);

            txtInStorageTotal = rootView.findViewById(R.id.txtInStorageTotal);
            txtInStorageFree = rootView.findViewById(R.id.txtInStorageFree);
            txtInStorageUsed = rootView.findViewById(R.id.txtInStorageUsed);
            progressInStorage = rootView.findViewById(R.id.progressInStorage);

            txtExStorageTotal = rootView.findViewById(R.id.txtExStorageTotal);
            txtExStorageFree = rootView.findViewById(R.id.txtExStorageFree);
            txtExStorageUsed = rootView.findViewById(R.id.txtExStorageUsed);
            progressExStorage = rootView.findViewById(R.id.progressExStorage);
            cardExStorage = rootView.findViewById(R.id.cardExStorageInfo);

            final Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
                        ActivityManager activityManager = (ActivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ACTIVITY_SERVICE);
                        if (activityManager != null) {
                            activityManager.getMemoryInfo(mi);
                        }
                        ARam = (double) (mi.availMem / 1024 / 1024);
                        TRam = (double) (mi.totalMem / 1024 / 1024);
                        URam = TRam - ARam;
                        UPerc = (int) (((URam) * 100) / TRam);
                        String TRAM = "Total\t\t\t\t\t" + Double.toString(TRam) + " MB";
                        String ARAM = "Free\t\t\t\t\t\t" + Double.toString(ARam) + " MB";
                        String URAM = "Used\t\t\t\t\t" + Double.toString(URam) + " MB";
                        txtRamTotal.setText(TRAM);
                        txtRamUsed.setText(URAM);
                        txtRamFree.setText(ARAM);
                        progressRam.setProgress(UPerc);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    h.postDelayed(this, 1000);
                }
            }, 0);

            StatFs statROM = new StatFs(Environment.getRootDirectory().getAbsolutePath());
            FreeSto = (((double) statROM.getBlockSizeLong() * (double) statROM.getAvailableBlocksLong()) / 1024 / 1024 / 1024);
            TotalSto = ((double) statROM.getBlockSizeLong() * (double) statROM.getBlockCountLong()) / 1024 / 1024 / 1024;
            UsedSto = TotalSto - FreeSto;
            UsedPerc = ((UsedSto * 100) / TotalSto);

            String TROM = "Total\t\t\t\t\t" + String.format(Locale.US, "%.2f", TotalSto) + " GB";
            String AROM = "Free\t\t\t\t\t\t" + String.format(Locale.US, "%.2f", FreeSto) + " GB";
            String UROM = "Used\t\t\t\t\t" + String.format(Locale.US, "%.2f", UsedSto) + " GB";
            txtRomTotal.setText(TROM);
            txtRomFree.setText(UROM);
            txtRomUsed.setText(AROM);
            progressRom.setProgress(UsedPerc.intValue());

            StatFs statIN = new StatFs(Environment.getExternalStorageDirectory().getPath());
            FreeSto = (((double) statIN.getBlockSizeLong() * (double) statIN.getAvailableBlocksLong()) / 1024 / 1024 / 1024);
            TotalSto = ((double) statIN.getBlockSizeLong() * (double) statIN.getBlockCountLong()) / 1024 / 1024 / 1024;
            UsedSto = TotalSto - FreeSto;
            UsedPerc = ((UsedSto * 100) / TotalSto);

            String TINS = "Total\t\t\t\t\t" + String.format(Locale.US, "%.2f", TotalSto) + " GB";
            String AINS = "Free\t\t\t\t\t\t" + String.format(Locale.US, "%.2f", FreeSto) + " GB";
            String UINS = "Used\t\t\t\t\t" + String.format(Locale.US, "%.2f", UsedSto) + " GB";
            txtInStorageTotal.setText(TINS);
            txtInStorageUsed.setText(UINS);
            txtInStorageFree.setText(AINS);
            progressInStorage.setProgress(UsedPerc.intValue());

            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED) && ContextCompat.getExternalFilesDirs(getContext(), null).length >= 2) {
                cardExStorage.setVisibility(View.VISIBLE);
                StatFs statEX = new StatFs(GetDetails.getStorageDirectories(Objects.requireNonNull(getContext()))[0]);
                FreeSto = (((double) statEX.getBlockSizeLong() * (double) statEX.getAvailableBlocksLong()) / 1024 / 1024 / 1024);
                TotalSto = ((double) statEX.getBlockSizeLong() * (double) statEX.getBlockCountLong()) / 1024 / 1024 / 1024;
                UsedSto = TotalSto - FreeSto;
                UsedPerc = ((UsedSto * 100) / TotalSto);

                String TEXS = "Total\t\t\t\t\t" + String.format(Locale.US, "%.2f", TotalSto) + " GB";
                String AEXS = "Free\t\t\t\t\t\t" + String.format(Locale.US, "%.2f", FreeSto) + " GB";
                String UEXS = "Used\t\t\t\t\t" + String.format(Locale.US, "%.2f", UsedSto) + " GB";
                txtExStorageTotal.setText(TEXS);
                txtExStorageUsed.setText(UEXS);
                txtExStorageFree.setText(AEXS);
                progressExStorage.setProgress(UsedPerc.intValue());
            } else {
                cardExStorage.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return rootView;
    }
}

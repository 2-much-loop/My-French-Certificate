package com.maximeg.my_french_certificate.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maximeg.my_french_certificate.R;
import com.maximeg.my_french_certificate.adapters.QRCodesAdapter;
import com.maximeg.my_french_certificate.models.QRCode;
import com.maximeg.my_french_certificate.sqlite.QRDbHelper;

import java.util.List;

public class QRCodesFragment extends Fragment{
    private List<QRCode> qrCodeList;
    private QRDbHelper dbHelper;
    private QRCodesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qr_codes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_qr);

        dbHelper = new QRDbHelper(getContext());

        qrCodeList = dbHelper.getQRCodes();

        adapter = new QRCodesAdapter(getContext(), qrCodeList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();

        if(dbHelper != null && adapter != null){
            qrCodeList = dbHelper.getQRCodes();
            adapter.notify(qrCodeList);
        }
    }
}

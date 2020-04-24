package com.maximeg.my_french_certificate.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.maximeg.my_french_certificate.R;
import com.maximeg.my_french_certificate.activities.QRActivity;
import com.maximeg.my_french_certificate.models.QRCode;
import com.maximeg.my_french_certificate.sqlite.QRContract;

import java.util.List;

public class QRCodesAdapter extends RecyclerView.Adapter<QRCodesAdapter.ViewHolder>{
    private Context context;
    private List<QRCode> qrCodeList;

    public QRCodesAdapter(Context context, List<QRCode> qrCodesList) {
        this.context = context;
        this.qrCodeList = qrCodesList;
    }

    public void notify(List<QRCode> list){
        this.qrCodeList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View qrView = inflater.inflate(R.layout.row_qr, parent, false);

        return new ViewHolder(qrView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final QRCode code = qrCodeList.get(position);

        holder.name.setText(code.getFirstName() + " " + code.getLastName());
        holder.date.setText(context.getString(R.string.period_for) + code.getDate() + context.getString(R.string.period_starting) + code.getTime());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QRActivity.class);
                intent.putExtra(QRContract.COLUMN_NAME_FILE_NAME, code.getFileName());
                intent.putExtra(QRContract._ID, code.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return qrCodeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public MaterialTextView name;
        public MaterialTextView date;
        public MaterialCardView card;

        public ViewHolder(View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.row_card);
            name = itemView.findViewById(R.id.row_name);
            date = itemView.findViewById(R.id.row_date);
        }
    }
}

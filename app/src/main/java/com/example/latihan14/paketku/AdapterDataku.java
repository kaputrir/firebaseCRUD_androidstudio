package com.example.latihan14.paketku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.latihan14.R;

import java.util.List;

public class AdapterDataku extends RecyclerView.Adapter<AdapterDataku.ViewHolder> {
    Context context;
    List<Dataku> list;

    OnCallBack onCallBack;

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    public AdapterDataku(Context context, List<Dataku> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_data_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewNim.setText(list.get(position).getIsiNim());
        holder.textViewNama.setText(list.get(position).getIsiNama());

        holder.tblDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onTblDelete(list.get(position));
            }
        });

        holder.tblEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBack.onTblEdit(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNim, textViewNama;
        ImageButton tblEdit, tblDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNim = itemView.findViewById(R.id.text_viem_nim);
            textViewNama = itemView.findViewById(R.id.text_view_nama);
            tblEdit = itemView.findViewById(R.id.tmblEdit);
            tblDelete = itemView.findViewById(R.id.tmblDelete);
        }
    }

    public interface OnCallBack{
        void onTblDelete(Dataku dataku);
        void onTblEdit(Dataku dataku);

    }
}

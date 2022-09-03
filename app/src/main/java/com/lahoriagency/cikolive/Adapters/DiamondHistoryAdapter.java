package com.lahoriagency.cikolive.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.lahoriagency.cikolive.Classes.DiamondHistory;
import com.lahoriagency.cikolive.R;

import java.util.ArrayList;
import java.util.List;

public class DiamondHistoryAdapter extends RecyclerView.Adapter<DiamondHistoryAdapter.ItemHolder> {

    List<DiamondHistory> list = new ArrayList<>();

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diamond_history, parent, false);
        return new ItemHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.setItems(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateItems(List<DiamondHistory> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private LinearLayout lout;
        private TextView tvDate,tvType,tvDiamondCount;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_Date_Diamond);
            tvType = itemView.findViewById(R.id.tv_type_diamond);
            tvDiamondCount = itemView.findViewById(R.id.tv_diamond_count);

        }

        public void setItems(int position) {
            DiamondHistory model = list.get(position);


            tvDiamondCount.setText(String.valueOf(model.getCount()));
            tvDate.setText(model.getDate());
            tvType.setText(model.getFrom());

        }


    }
}

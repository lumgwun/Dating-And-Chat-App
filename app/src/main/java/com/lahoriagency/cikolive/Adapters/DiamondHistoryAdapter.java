package com.lahoriagency.cikolive.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.retry.dimdim.R;
import com.retry.dimdim.databinding.ItemDiamondHistoryBinding;
import com.retry.dimdim.modals.DiamondHistory;

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

        ItemDiamondHistoryBinding binding;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }

        public void setItems(int position) {
            DiamondHistory model = list.get(position);


            binding.tvDiamondCount.setText(String.valueOf(model.getCount()));
            binding.tvDate.setText(model.getDate());
            binding.tvType.setText(model.getFrom());

        }


    }
}

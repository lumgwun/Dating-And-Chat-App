package com.lahoriagency.cikolive.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.retry.dimdim.R;
import com.retry.dimdim.databinding.ItemPurchaseDiamondBinding;
import com.retry.dimdim.modals.PurchaseDiamond;
import com.retry.dimdim.utils.Const;

import java.util.ArrayList;
import java.util.List;

public class PurchaseDiamondAdapter extends RecyclerView.Adapter<PurchaseDiamondAdapter.ItemHolder> {

    List<PurchaseDiamond> list = new ArrayList<>();

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase_diamond, parent, false);
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

    public void updateItems(List<PurchaseDiamond> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        ItemPurchaseDiamondBinding binding;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }

        public void setItems(int position) {
            PurchaseDiamond model = list.get(position);


            binding.tvCount.setText(String.valueOf(model.getCount()));
            binding.btnPrice.setText(Const.CURRENCY + " " + model.getPrice());


        }


    }
}

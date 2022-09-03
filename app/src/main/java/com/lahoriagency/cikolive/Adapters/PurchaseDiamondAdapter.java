package com.lahoriagency.cikolive.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;


import com.lahoriagency.cikolive.Classes.PurchaseDiamond;
import com.lahoriagency.cikolive.R;
import com.lahoriagency.cikolive.Utils.Const;

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
        TextView tvCount;
        private AppCompatButton btnPrice;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            btnPrice = itemView.findViewById(R.id.btn_get_price);
            tvCount = itemView.findViewById(R.id.tv_get_count);

        }

        public void setItems(int position) {
            PurchaseDiamond model = list.get(position);


            tvCount.setText(String.valueOf(model.getCount()));
            btnPrice.setText(Const.CURRENCY + " " + model.getPrice());


        }


    }
}

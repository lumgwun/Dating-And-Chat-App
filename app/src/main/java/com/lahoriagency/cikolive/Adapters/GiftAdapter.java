package com.lahoriagency.cikolive.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lahoriagency.cikolive.Classes.Gift;
import com.lahoriagency.cikolive.R;


import java.util.ArrayList;
import java.util.List;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ItemHolder> {

    List<Gift> list = new ArrayList<>();

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gift, parent, false);
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

    public void updateItems(List<Gift> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        TextView tvCount;
        private ImageView giftImage;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            giftImage = (ImageView) itemView.findViewById(R.id.giftImg);
            tvCount = itemView.findViewById(R.id.tv_gift_count);

        }

        public void setItems(int position) {
            Gift model = list.get(position);

            Glide.with(itemView.getContext())
                    .load(model.getImage())
                    .placeholder(R.drawable.img_heart_gold)
                    .into(giftImage);
            tvCount.setText(String.valueOf(model.getCount()));


        }


    }
}

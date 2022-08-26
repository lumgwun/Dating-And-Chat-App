package com.lahoriagency.cikolive.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lahoriagency.cikolive.R;


import java.util.ArrayList;
import java.util.List;

public class ProfileVideoAdapter extends RecyclerView.Adapter<ProfileVideoAdapter.ItemHolder> {

    List<String> list = new ArrayList<>();
    OnItemClick onItemClick;

    public OnItemClick getOnItemClick() {
        return onItemClick;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
    public interface OnItemClick {
        void onClick();
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_gallery, parent, false);
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

    public void updateItems(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView1,imageView2;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_Video);
            imageView1 = (ImageView) itemView.findViewById(R.id.img_Video);
            imageView2 = (ImageView) itemView.findViewById(R.id.img_V_down);
        }

        public void setItems(int position) {


            Glide.with(itemView.getContext()).load(list.get(position)).into(imageView1);


        }


    }
}

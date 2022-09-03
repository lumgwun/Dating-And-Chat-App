package com.lahoriagency.cikolive.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lahoriagency.cikolive.Classes.Notification;
import com.lahoriagency.cikolive.R;


import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ItemHolder> {

    List<Notification> list = new ArrayList<>();

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
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

    public void updateItems(List<Notification> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tvHeadLine,tvDescription,tvTime;



        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgYYYT);
            tvHeadLine = itemView.findViewById(R.id.tv_headLineY);
            tvDescription = itemView.findViewById(R.id.tv_descrip);
            tvTime = itemView.findViewById(R.id.tv_timeY);


        }

        public void setItems(int position) {
            Notification model = list.get(position);


            tvHeadLine.setText(String.valueOf(model.getHeadLine()));
            tvDescription.setText(String.valueOf(model.getDescription()));
            tvTime.setText(String.valueOf(model.getTime()));
            Glide.with(itemView.getContext()).load(model.getImage()).into(img);

        }


    }
}

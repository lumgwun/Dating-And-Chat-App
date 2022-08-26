package com.lahoriagency.cikolive.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lahoriagency.cikolive.Classes.ChatsItem;
import com.lahoriagency.cikolive.R;

import java.util.ArrayList;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ItemHolder>  {

    OnItemClick onItemClick;

    public OnItemClick getOnItemClick() {
        return onItemClick;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    List<ChatsItem> list = new ArrayList<>();

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chats, parent, false);
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

    public void updateItems(List<ChatsItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public interface OnItemClick {
        void onClick();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        ImageView ivImage,imgNewMassage;
        TextView tvName,tvAge,tvMassage,tvTime;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = (ImageView) itemView.findViewById(R.id.img_chat);
            ivImage = (ImageView) itemView.findViewById(R.id.img_new_chat_massage);
            tvName = (TextView) itemView.findViewById(R.id.tv_chat_name);
            tvAge = itemView.findViewById(R.id.tv_chat_age);
            tvMassage = itemView.findViewById(R.id.tv_chat_massage);
            tvTime = (TextView) itemView.findViewById(R.id.tv_chat_time);
        }

        public void setItems(int position) {
            ChatsItem model = list.get(position);

            if (model.isSeen()) {
                imgNewMassage.setVisibility(View.GONE);
            }
            tvName.setText(String.valueOf(model.getName()));
            tvAge.setText(String.valueOf(model.getAge()));
            tvMassage.setText(String.valueOf(model.getMessage()));
            tvTime.setText(String.valueOf(model.getTime()));
            Glide.with(itemView.getContext()).load(model.getImage()).into(ivImage);


            itemView.setOnClickListener(view -> onItemClick.onClick());

        }


    }
}

package com.lahoriagency.cikolive.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lahoriagency.cikolive.R;

import java.util.ArrayList;
import java.util.List;

public class FindMatchAdapter extends RecyclerView.Adapter<FindMatchAdapter.ItemHolder> {

    List<String> list = new ArrayList<>();

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_image, parent, false);
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
        private ImageView img;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgProfImg);

        }

        public void setItems(int position) {

            Glide.with(itemView.getContext()).load(list.get(position)).into(img);


        }


    }
}

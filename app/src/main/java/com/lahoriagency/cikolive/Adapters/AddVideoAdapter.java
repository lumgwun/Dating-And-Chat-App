package com.lahoriagency.cikolive.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lahoriagency.cikolive.R;

import java.util.ArrayList;
import java.util.List;

public class AddVideoAdapter extends RecyclerView.Adapter<AddVideoAdapter.ItemHolder> {

    List<String> list = new ArrayList<>();

    @NonNull
    @Override
    public AddVideoAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_videos, parent, false);
        return new AddVideoAdapter.ItemHolder(view);

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
        private LinearLayout lout;
        private TextView tvName;
        private ImageView btnDelete,btnAdd,img;



        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.videoToBeAdded);
            btnDelete = itemView.findViewById(R.id.btn_deleteProfVideo);
            btnAdd = itemView.findViewById(R.id.btn_addVideo);

        }

        public void setItems(int position) {
            if (position < 4) {

                Glide.with(itemView.getContext()).load(list.get(position)).into(img);
                btnDelete.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.GONE);
            }


        }


    }
}

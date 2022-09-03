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

public class AddImageAdapter extends RecyclerView.Adapter<AddImageAdapter.ItemHolder> {

    List<String> list = new ArrayList<>();

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_images, parent, false);
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
        private LinearLayout lout;
        private TextView tvName;
        private ImageView btnDelete,btnAdd,img;



        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgToBeAdded);
            btnDelete = itemView.findViewById(R.id.btn_deleteImage);
            btnAdd = itemView.findViewById(R.id.btn_addImage);

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

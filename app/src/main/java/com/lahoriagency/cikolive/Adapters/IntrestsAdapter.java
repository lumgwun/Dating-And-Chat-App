package com.lahoriagency.cikolive.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.lahoriagency.cikolive.R;

import java.util.ArrayList;
import java.util.List;

public class IntrestsAdapter extends RecyclerView.Adapter<IntrestsAdapter.ItemHolder> {

    List<String> list = new ArrayList<>();
    private OnItemsClickListener callback;

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_intrests_chips, parent, false);
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
    public void renewItems(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.list.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(String item) {
        this.list.add(item);
        notifyDataSetChanged();
    }
    public void setCallback(OnItemsClickListener callback) {
        this.callback = callback;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView txtName;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.textv_name);
            cardView = itemView.findViewById(R.id.cardViewName);

        }

        public void setItems(int position) {
            txtName.setText(String.valueOf(list.get(position)));


        }


    }
    public interface OnItemsClickListener{
        void onItemClick(int position);
        void onItemClick(View view, int position);
    }
}

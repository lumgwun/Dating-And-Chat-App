package com.lahoriagency.cikolive.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lahoriagency.cikolive.R;


public class ImageIndicatorAdapter extends RecyclerView.Adapter<ImageIndicatorAdapter.ItemHolder> {

    int count;
    int index;

    public ImageIndicatorAdapter(int index) {
        this.index = index;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_indicator, parent, false);
        int height = 10;
        int width = parent.getMeasuredWidth() / count;


        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(width, height);
        params.setMargins(0, 0, 4, 0);
        view.setLayoutParams(params);
        return new ItemHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.setItems(position);
    }

    @Override
    public int getItemCount() {
        return count;
    }

    public void updateColor(int index) {
        this.index = index;
        notifyDataSetChanged();
    }

    public void updateItems(int count) {
        this.count = count;
        notifyDataSetChanged();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private View indicator;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            indicator = itemView.findViewById(R.id.indicatorN);

        }

        public void setItems(int position) {
            if (position == index) {

                indicator.setBackgroundTintList(ContextCompat.getColorStateList(itemView.getContext(), R.color.white));

            } else {
                indicator.setBackgroundTintList(ContextCompat.getColorStateList(itemView.getContext(), R.color.white_30));

            }

        }


    }
}

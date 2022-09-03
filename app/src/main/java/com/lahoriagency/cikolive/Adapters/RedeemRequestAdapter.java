package com.lahoriagency.cikolive.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


import com.lahoriagency.cikolive.Classes.RedeemRequest;
import com.lahoriagency.cikolive.R;

import java.util.ArrayList;
import java.util.List;

public class RedeemRequestAdapter extends RecyclerView.Adapter<RedeemRequestAdapter.ItemHolder> {

    List<RedeemRequest> list = new ArrayList<>();

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_redeem_request, parent, false);
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

    public void updateItems(List<RedeemRequest> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView tvDiamondCount,tvRqId,tvDate,tvType,tvAmount;
        private LinearLayout louCompleted;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvDiamondCount = itemView.findViewById(R.id.tv_diamond_count);
            tvRqId = itemView.findViewById(R.id.tv_rq_id);
            tvDate = itemView.findViewById(R.id.tv_Date);
            tvType = itemView.findViewById(R.id.tv_type);
            tvAmount = itemView.findViewById(R.id.btn_search1);
            louCompleted = itemView.findViewById(R.id.lou_completed);

        }

        public void setItems(int position) {
            RedeemRequest model = list.get(position);

            tvDiamondCount.setText(String.valueOf(model.getDiamondCount()));
            tvRqId.setText(String.valueOf(model.getId()));
            tvDate.setText(model.getDate());
            if (model.getType() == 1) {

                tvType.setText(R.string.completed);
                tvType.setBackgroundTintList(itemView.getContext().getColorStateList(R.color.green_22));
                tvType.setTextColor(itemView.getContext().getColor(R.color.green));
                louCompleted.setVisibility(View.VISIBLE);
                tvAmount.setText(model.getAmount());


            } else if (model.getType() == 0) {

                tvType.setText(R.string.processing);
                tvType.setBackgroundTintList(itemView.getContext().getColorStateList(R.color.orange_20));
                tvType.setTextColor(itemView.getContext().getColor(R.color.orange));
                louCompleted.setVisibility(View.GONE);

            }

        }


    }
}

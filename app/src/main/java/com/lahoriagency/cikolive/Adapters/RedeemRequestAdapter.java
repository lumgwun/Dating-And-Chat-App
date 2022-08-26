package com.lahoriagency.cikolive.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.retry.dimdim.R;
import com.retry.dimdim.databinding.ItemRedeemRequestBinding;
import com.retry.dimdim.modals.RedeemRequest;

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

        ItemRedeemRequestBinding binding;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }

        public void setItems(int position) {
            RedeemRequest model = list.get(position);


            binding.tvDiamondCount.setText(String.valueOf(model.getDiamondCount()));
            binding.tvRqId.setText(String.valueOf(model.getId()));
            binding.tvDate.setText(model.getDate());
            if (model.getType() == 1) {

                binding.tvType.setText(R.string.completed);
                binding.tvType.setBackgroundTintList(itemView.getContext().getColorStateList(R.color.green_22));
                binding.tvType.setTextColor(itemView.getContext().getColor(R.color.green));
                binding.louCompleted.setVisibility(View.VISIBLE);
                binding.tvAmount.setText(model.getAmount());


            } else if (model.getType() == 0) {

                binding.tvType.setText(R.string.processing);
                binding.tvType.setBackgroundTintList(itemView.getContext().getColorStateList(R.color.orange_20));
                binding.tvType.setTextColor(itemView.getContext().getColor(R.color.orange));
                binding.louCompleted.setVisibility(View.GONE);

            }

        }


    }
}

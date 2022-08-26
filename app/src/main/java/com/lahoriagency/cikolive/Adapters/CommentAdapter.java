package com.lahoriagency.cikolive.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.retry.dimdim.R;
import com.retry.dimdim.databinding.ItemCommentBinding;
import com.retry.dimdim.modals.Comments;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ItemHolder> {

    List<Comments> list = new ArrayList<>();

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
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

    public void updateItems(List<Comments> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        ItemCommentBinding binding;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }

        public void setItems(int position) {

            Comments model = list.get(position);


            if (model.getId() == 1) {
                binding.tvComment.setVisibility(View.VISIBLE);
                binding.tvComment.setText(String.valueOf(model.getComment()));

            } else if (model.getId() == 2) {
                binding.imgGift.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext()).load(model.getGift()).into(binding.imgGift);

            }

            Glide.with(itemView.getContext()).load(model.getImage()).into(binding.img);
            binding.tvName.setText(model.getName());
            binding.tvPalce.setText(model.getPlace());
            binding.tvAge.setText(String.valueOf(model.getAge()));


        }


    }
}

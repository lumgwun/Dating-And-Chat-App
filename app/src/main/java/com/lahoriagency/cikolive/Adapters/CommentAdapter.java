package com.lahoriagency.cikolive.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lahoriagency.cikolive.Classes.Comments;
import com.lahoriagency.cikolive.R;
import com.lahoriagency.cikolive.Utils.SessionManager;

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
        private TextView tvComment,tvName,tvPalce,tvAge;
        private ImageView imgGift,pic;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvComment = itemView.findViewById(R.id.tv_comment_chatter);
            tvName = itemView.findViewById(R.id.tv_name_of_Chatter);
            tvPalce = itemView.findViewById(R.id.tv_palce_chatter);
            tvAge = itemView.findViewById(R.id.t_age_of_chatter);
            imgGift = itemView.findViewById(R.id.img_gift_chatter);
            pic = itemView.findViewById(R.id.imgChatter);


        }

        public void setItems(int position) {

            Comments model = list.get(position);


            if (model.getId() == 1) {
                tvComment.setVisibility(View.VISIBLE);
                tvComment.setText(String.valueOf(model.getComment()));

            } else if (model.getId() == 2) {
                imgGift.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext()).load(model.getGift()).into(imgGift);

            }

            Glide.with(itemView.getContext()).load(model.getImage()).into(pic);
            tvName.setText(model.getName());
            tvPalce.setText(model.getPlace());
            tvAge.setText(String.valueOf(model.getAge()));


        }


    }
}

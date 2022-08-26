package com.lahoriagency.cikolive.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.retry.dimdim.R;
import com.retry.dimdim.activities.ProfileActivity;
import com.retry.dimdim.activities.UserLiveActivity;
import com.retry.dimdim.databinding.ItemExploreProfileLongBinding;
import com.retry.dimdim.databinding.ItemExploreProfileShortBinding;
import com.retry.dimdim.modals.ModelItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ExploreProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int LONG = 1;
    public static final int SHORT = 2;

    List<ModelItem> list = new ArrayList<>();
    int longIndex = 3;
    int shortIndex = 1;
    int type;

    public ExploreProfileAdapter(int type) {
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View viewShort = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explore_profile_short, parent, false);
        View viewLong = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explore_profile_long, parent, false);

        if (viewType == LONG) {

            return new ItemHolderLong(viewLong);


        } else if (viewType == SHORT) {
            return new ItemHolderShort(viewShort);
        }
        return new ItemHolderLong(viewLong);

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == LONG) {

            ItemHolderLong vhLong = (ItemHolderLong) holder;
            vhLong.setDataLong(position);

        } else if (holder.getItemViewType() == SHORT) {
            ItemHolderShort vhShort = (ItemHolderShort) holder;
            vhShort.setDataShort(position);
        }
    }

    @Override
    public int getItemViewType(int position) {


        if (list.get(position).isShort()) {
            return SHORT;
        } else return LONG;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateItems(List<ModelItem> list) {
        longIndex = 0;
        shortIndex = 1;
        this.list = list;


        for (int i = 0; i < list.size(); i++) {
            if (i == longIndex) {
                longIndex = longIndex + 2;
                list.get(i).setShort(false);

            }
            if (i == shortIndex) {
                shortIndex = shortIndex + 2;
                list.get(i).setShort(true);

            }


        }
        notifyDataSetChanged();

    }



    public class ItemHolderShort extends RecyclerView.ViewHolder {

        ItemExploreProfileShortBinding binding;

        public ItemHolderShort(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }


        public void setDataShort(int position) {

            ModelItem model = list.get(position);


            if (type == 1) {
                binding.loutLive.setVisibility(View.GONE);
            } else {
                binding.loutLive.setVisibility(View.VISIBLE);

            }
            Glide.with(itemView.getContext()).load(model.getActorImage()).into(binding.img);
            binding.tvName.setText(model.getActorName());
            binding.tvLocation.setText(model.getLocation());
            binding.tvDiamondRate.setText(String.valueOf(model.getDiamond()));
            binding.tvAge.setText(String.valueOf(model.getAge()));


            binding.getRoot().setOnClickListener(view -> {
                if (type == 1) {

                    itemView.getContext().startActivity(new Intent(itemView.getContext(), ProfileActivity.class));
                } else if (type == 2) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), UserLiveActivity.class));

                }

            });
        }
    }


    public class ItemHolderLong extends RecyclerView.ViewHolder {

        ItemExploreProfileLongBinding binding;

        public ItemHolderLong(@NonNull @NotNull View view1) {
            super(view1);
            binding = DataBindingUtil.bind(view1);
        }

        private void setDataLong(int position) {

            ModelItem model = list.get(position);


            if (type == 1) {
                binding.loutLive.setVisibility(View.GONE);
            } else {
                binding.loutLive.setVisibility(View.VISIBLE);

            }
            Glide.with(itemView.getContext()).load(model.getActorImage()).into(binding.img);
            binding.tvName.setText(model.getActorName());
            binding.tvLocation.setText(model.getLocation());
            binding.tvDiamondRate.setText(String.valueOf(model.getDiamond()));
            binding.tvAge.setText(String.valueOf(model.getAge()));


            binding.getRoot().setOnClickListener(view -> {
                if (type == 1) {

                    itemView.getContext().startActivity(new Intent(itemView.getContext(), ProfileActivity.class));
                } else if (type == 2) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), UserLiveActivity.class));

                }
            });

        }
    }
}


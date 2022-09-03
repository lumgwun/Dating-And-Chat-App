package com.lahoriagency.cikolive.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lahoriagency.cikolive.Classes.ModelItem;
import com.lahoriagency.cikolive.ProfileActivity;
import com.lahoriagency.cikolive.R;
import com.lahoriagency.cikolive.UserLiveActivity;

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
        private ImageView img;
        private TextView tvName,tvLocation,tvAge,tvDiamondRate,livCount;
        private FrameLayout loutLive;


        public ItemHolderShort(@NonNull @NotNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgCik);
            tvName = itemView.findViewById(R.id.tv_nameBabe);
            tvLocation = itemView.findViewById(R.id.tv_loc_user);
            tvAge = itemView.findViewById(R.id.tv_short_age);
            tvDiamondRate = itemView.findViewById(R.id.tv_diamond_short_rate);
            tvAge = itemView.findViewById(R.id.loutCountry);
            livCount = itemView.findViewById(R.id.tv_live_count);
            loutLive = itemView.findViewById(R.id.short_lout_live);
        }


        public void setDataShort(int position) {

            ModelItem model = list.get(position);


            if (type == 1) {
                loutLive.setVisibility(View.GONE);
            } else {
                loutLive.setVisibility(View.VISIBLE);

            }
            Glide.with(itemView.getContext()).load(model.getActorImage()).into(img);
            tvName.setText(model.getActorName());
            tvLocation.setText(model.getLocation());
            tvDiamondRate.setText(String.valueOf(model.getDiamond()));
            tvAge.setText(String.valueOf(model.getAge()));


            itemView.setOnClickListener(view -> {
                if (type == 1) {

                    itemView.getContext().startActivity(new Intent(itemView.getContext(), ProfileActivity.class));
                } else if (type == 2) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), UserLiveActivity.class));

                }

            });
        }
    }


    public class ItemHolderLong extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tvName,tvLocation,tvAge,tvDiamondRate,time,liveCount;
        private FrameLayout loutLive;


        public ItemHolderLong(@NonNull @NotNull View view1) {
            super(view1);
            img = itemView.findViewById(R.id.long_userPic);
            tvName = itemView.findViewById(R.id.long_name);
            tvLocation = itemView.findViewById(R.id.tv_long_loc);
            tvAge = itemView.findViewById(R.id.long_tv_age);
            tvDiamondRate = itemView.findViewById(R.id.tv_diamond34_);
            tvAge = itemView.findViewById(R.id.long_tv_age);
            time = itemView.findViewById(R.id.txtV_time);
            liveCount = itemView.findViewById(R.id.tv_live_count);
            loutLive = itemView.findViewById(R.id.long_lout_live);
        }

        private void setDataLong(int position) {

            ModelItem model = list.get(position);


            if (type == 1) {
                loutLive.setVisibility(View.GONE);
            } else {
                loutLive.setVisibility(View.VISIBLE);

            }
            Glide.with(itemView.getContext()).load(model.getActorImage()).into(img);
            tvName.setText(model.getActorName());
            tvLocation.setText(model.getLocation());
            tvDiamondRate.setText(String.valueOf(model.getDiamond()));
            tvAge.setText(String.valueOf(model.getAge()));


            loutLive.setOnClickListener(view -> {
                if (type == 1) {

                    itemView.getContext().startActivity(new Intent(itemView.getContext(), ProfileActivity.class));
                } else if (type == 2) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), UserLiveActivity.class));

                }
            });

        }
    }
}


package com.lahoriagency.cikolive.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.lahoriagency.cikolive.Classes.RedeemRequest;
import com.lahoriagency.cikolive.R;

import java.util.ArrayList;
import java.util.List;

public class RedeemRequestAdapter extends RecyclerView.Adapter<RedeemRequestAdapter.ItemHolder> {

    List<RedeemRequest> list = new ArrayList<>();
    private Context mcontext;
    int resources;
    private String from, date;
    FragmentActivity activity;
    int diamondCount;
    private OnItemsClickListener listener;

    public RedeemRequestAdapter(ArrayList<RedeemRequest> recyclerDataArrayList, Context mcontext) {
        this.list = recyclerDataArrayList;
        this.mcontext = mcontext;
    }

    public RedeemRequestAdapter(Context context, int resources, ArrayList<RedeemRequest> diamondHistories) {
        this.list = diamondHistories;
        this.mcontext = context;
        this.resources = resources;

    }
    public void setWhenClickListener(OnItemsClickListener listener){
        this.listener = listener;
    }
    public RedeemRequestAdapter(Context context, ArrayList<RedeemRequest> diamondHistories) {
        this.list = diamondHistories;
        this.mcontext = context;

    }

    public RedeemRequestAdapter(FragmentActivity activity, ArrayList<RedeemRequest> diamondHistories) {
        this.list = diamondHistories;
        this.activity = activity;
    }

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

            tvDiamondCount.setText(String.valueOf(model.getRr_Count()));
            tvRqId.setText(String.valueOf(model.getRr_Id()));
            tvDate.setText(model.getRr_Date());
            if (model.getRr_Type() == 1) {

                tvType.setText(R.string.completed);
                tvType.setBackgroundTintList(itemView.getContext().getColorStateList(R.color.green_22));
                tvType.setTextColor(itemView.getContext().getColor(R.color.green));
                louCompleted.setVisibility(View.VISIBLE);
                tvAmount.setText(model.getRr_Amount());


            } else if (model.getRr_Type() == 0) {

                tvType.setText(R.string.processing);
                tvType.setBackgroundTintList(itemView.getContext().getColorStateList(R.color.orange_20));
                tvType.setTextColor(itemView.getContext().getColor(R.color.orange));
                louCompleted.setVisibility(View.GONE);

            }

        }


    }
    public interface OnItemsClickListener{
        void onItemClick(RedeemRequest redeemRequest);
    }
}

package com.lahoriagency.cikolive.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.lahoriagency.cikolive.Classes.DiamondTransfer;
import com.lahoriagency.cikolive.R;

import java.text.MessageFormat;
import java.util.ArrayList;

public class DiamondHisAdapter extends RecyclerView.Adapter<DiamondHisAdapter.RecyclerViewHolder> {

    private ArrayList<DiamondTransfer> diamondHistories;
    private Context mcontext;
    int resources;
    private String from, date;
    FragmentActivity activity;
    int diamondCount;
    private OnItemsClickListener listener;

    public DiamondHisAdapter(ArrayList<DiamondTransfer> recyclerDataArrayList, Context mcontext) {
        this.diamondHistories = recyclerDataArrayList;
        this.mcontext = mcontext;
    }

    public DiamondHisAdapter(Context context, int resources, ArrayList<DiamondTransfer> diamondHistories) {
        this.diamondHistories = diamondHistories;
        this.mcontext = context;
        this.resources = resources;

    }
    public void setWhenClickListener(OnItemsClickListener listener){
        this.listener = listener;
    }
    public DiamondHisAdapter(Context context, ArrayList<DiamondTransfer> diamondHistories) {
        this.diamondHistories = diamondHistories;
        this.mcontext = context;

    }

    public DiamondHisAdapter(FragmentActivity activity, ArrayList<DiamondTransfer> diamondHistories) {
        this.diamondHistories = diamondHistories;
        this.activity = activity;
    }

    @NonNull
    @Override
    public DiamondHisAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_d_history, parent, false);
        return new DiamondHisAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiamondHisAdapter.RecyclerViewHolder holder, int position) {
        DiamondTransfer recyclerData = diamondHistories.get(position);
        if(recyclerData !=null){
            from=recyclerData.getdH_From();
            diamondCount=recyclerData.getdH_Count();
            date=recyclerData.getdH_Date();
        }
        holder.txtDiamondFrom.setText(MessageFormat.format("From:{0}", from));
        holder.txtDate.setText(MessageFormat.format("Date:{0}",date));
        holder.txtDCount.setText(MessageFormat.format("Count:{0}", diamondCount));

    }
    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return (null != diamondHistories ? diamondHistories.size() : 0);
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDiamondFrom;
        private TextView txtDate;
        private TextView txtDCount;


        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDiamondFrom = itemView.findViewById(R.id.dh_from);
            txtDCount = itemView.findViewById(R.id.dh_cOUNt);
            txtDate = itemView.findViewById(R.id.dh_Date);

        }
    }
    public interface OnItemsClickListener{
        void onItemClick(DiamondTransfer diamondTransfer);
    }
}

package com.lahoriagency.cikolive.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.lahoriagency.cikolive.Classes.TimeLine;
import com.lahoriagency.cikolive.R;

import java.text.MessageFormat;
import java.util.ArrayList;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.RecyclerViewHolder> {

    private ArrayList<TimeLine> timeLineArrayList;
    private Context mcontext;
    int resources;
    FragmentActivity activity;
    private OnItemsClickListener listener;

    public TimelineAdapter(ArrayList<TimeLine> recyclerDataArrayList, Context mcontext) {
        this.timeLineArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
    }

    public TimelineAdapter(Context context, int resources, ArrayList<TimeLine> timeLineArrayList) {
        this.timeLineArrayList = timeLineArrayList;
        this.mcontext = context;
        this.resources = resources;

    }
    public void setWhenClickListener(OnItemsClickListener listener){
        this.listener = listener;
    }
    public TimelineAdapter(Context context, ArrayList<TimeLine> timeLineArrayList) {
        this.timeLineArrayList = timeLineArrayList;
        this.mcontext = context;

    }

    public TimelineAdapter(FragmentActivity activity, ArrayList<TimeLine> timeLineArrayList) {
        this.timeLineArrayList = timeLineArrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_list_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        TimeLine recyclerData = timeLineArrayList.get(position);
        holder.txtTimelineType.setText(MessageFormat.format("Type:{0}", recyclerData.getTimelineType()));
        holder.txtDetails.setText(MessageFormat.format("Details:{0}", recyclerData.getTimelineDetails()));
        holder.txtTime.setText(MessageFormat.format("Time:{0}", recyclerData.getTimelineTime()));

    }
    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return (null != timeLineArrayList ? timeLineArrayList.size() : 0);
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTimelineType;
        private TextView txtDetails;
        private final ImageView userPicture;
        private TextView txtTime;


        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTimelineType = itemView.findViewById(R.id.timelineType);
            txtTime = itemView.findViewById(R.id.time_timeline);
            txtDetails = itemView.findViewById(R.id.timeline_Details);
            userPicture = itemView.findViewById(R.id.img_account4);

        }
    }
    public interface OnItemsClickListener{
        void onItemClick(TimeLine timeLine);
    }
}

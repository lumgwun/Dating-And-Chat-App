package com.lahoriagency.cikolive.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lahoriagency.cikolive.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AddVideoAdapter extends RecyclerView.Adapter<AddVideoAdapter.ItemHolder> {

    List<String> list = new ArrayList<>();
    ArrayList<String> mValues;
    Context mContext;
    protected ItemListener mListener;
    int position;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;

    @NonNull
    @Override
    public AddVideoAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_videos, parent, false);
        return new AddVideoAdapter.ItemHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ItemHolder holder, int position) {
        String video = mValues.get(position);
        holder.setItems(position);


        try{
            holder.videoView.setMediaController(mediaControls);
            holder.videoView.setVideoURI(Uri.parse(video));
            holder.videoView.getLayoutParams().height = getScreenWidth(mContext) * 9 /16;
        } catch (Exception e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        holder.videoView.requestFocus();
        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressDialog.dismiss();
                holder.videoView.start();
            }
        });
        holder.videoView.getLayoutParams().height = getScreenWidth(mContext) * 9 /16;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateItems(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout lout;
        private TextView tvName;
        private ImageView btnDelete,btnAdd;
        VideoView videoView;



        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoToBeAdded);
            btnDelete = itemView.findViewById(R.id.btn_deleteProfVideo);
            btnAdd = itemView.findViewById(R.id.btn_addVideo);

        }


        public void setItems(int position) {
            if (position < 10) {
                btnDelete.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.GONE);
            }


        }


        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(position);
            }
        }
    }
    public static int getScreenWidth(Context c) {
        int screenWidth = 0; // this is part of the class not the method
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }
    public interface ItemListener {
        void onItemClick(int position);
    }
}

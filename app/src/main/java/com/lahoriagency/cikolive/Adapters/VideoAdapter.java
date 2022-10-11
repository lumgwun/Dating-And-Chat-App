package com.lahoriagency.cikolive.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lahoriagency.cikolive.CreateProfileActivity;
import com.lahoriagency.cikolive.R;

import android.widget.MediaController;
import android.media.MediaPlayer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    ArrayList<String> mValues;
    ArrayList<Uri> uriVideo;
    Context mContext;
    protected VideoListener mListener;
    int position;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;

    public VideoAdapter(Context context, ArrayList<String>  values, VideoListener videoListener) {

        mValues = values;
        mContext = context;
        mListener= videoListener;
    }
    public VideoAdapter(Context context) {
        mContext = context;
        mediaControls = new MediaController(mContext);
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("Profile Video");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public VideoAdapter(CreateProfileActivity context, ArrayList<Uri> mArrayUri, VideoListener videoListener) {
        uriVideo = mArrayUri;
        mContext = context;
        mListener=videoListener;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public VideoView videoView;

        public ViewHolder(View v) {

            super(v);
            v.setOnClickListener(this);
            videoView = (VideoView) v.findViewById(R.id.videoProfPix);

        }


        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onVideoClick(position);
            }
        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.video_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        String video = mValues.get(position);
        Uri videoUri = uriVideo.get(position);


        try{
            holder.videoView.setMediaController(mediaControls);
            holder.videoView.setVideoURI(Uri.parse(video));
            holder.videoView.setVideoURI(videoUri);
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
        return (null != mValues ? mValues.size() : 0);
    }


    public interface VideoListener {
        void onVideoClick(int position);
    }
    public static int getScreenWidth(Context c) {
        int screenWidth = 0; // this is part of the class not the method
        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;

        return screenWidth;
    }
}

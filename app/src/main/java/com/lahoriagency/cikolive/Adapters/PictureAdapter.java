package com.lahoriagency.cikolive.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lahoriagency.cikolive.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {

    List<String> mValues;
    ArrayList<String> mValuesArray;
    Context mContext;
    protected ItemListener mListener;
    int position;

    public PictureAdapter(Context context, List<String> values, ItemListener itemListener) {

        mValues = values;
        mContext = context;
        mListener=itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView pictureView;

        public ViewHolder(View v) {

            super(v);
            v.setOnClickListener(this);
            pictureView = (ImageView) v.findViewById(R.id.imageProfPix);

        }


        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onPictureClick(position);
            }
        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.picture_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        String picture = mValues.get(position);
        Picasso.get().load(picture).into(holder.pictureView);
    }

    @Override
    public int getItemCount() {
        return (null != mValues ? mValues.size() : 0);
    }


    public interface ItemListener {
        void onPictureClick(int position);
    }
}

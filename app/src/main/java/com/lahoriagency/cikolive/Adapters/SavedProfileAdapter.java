package com.lahoriagency.cikolive.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.R;
import com.lahoriagency.cikolive.SavedProfileActivity;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SavedProfileAdapter extends RecyclerView.Adapter<SavedProfileAdapter.ItemHolder> {

    List<SavedProfile> list = new ArrayList<>();
    private Context context;
    private int profileID;
    private String age,name,location,status,lastSeen;
    private Uri profilePicture;

    public SavedProfileAdapter(SavedProfileActivity savedProfileActivity, ArrayList<SavedProfile> savedProfiles) {
        this.context = savedProfileActivity;
        this.list = savedProfiles;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_profile, parent, false);
        return new ItemHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.setItems(position);

        final SavedProfile savedProfile = list.get(position);
        if(savedProfile !=null){
            profileID =savedProfile.getSavedProfID();
            age=savedProfile.getAge();
            name=savedProfile.getName();
            location=savedProfile.getLocation();
            status=savedProfile.getStatus();
            lastSeen=savedProfile.getLastSeen();
            profilePicture=savedProfile.getImage();

        }
        holder.names.setText(name);
        holder.age.setText(age);
        holder.location.setText(location);
        //holder.lastSeen.setText(lastSeen);
        holder.status.setText(status);
        Glide.with(context).load(profilePicture).into(holder.soPix);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateItems(List<SavedProfile> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public  View mView;
        public  TextView age;
        public  TextView location;
        public  TextView status;
        public  TextView names,lastSeen;
        public CircleImageView soPix;
        private CardView rootLayout;
        private ImageView imgStatus;

        public ItemHolder(View view) {
            super(view);
            mView = view;
            age = (TextView) view.findViewById(R.id.textv_age);
            imgStatus = view.findViewById(R.id.imgO);
            location = (TextView) view.findViewById(R.id.tv_location);
            status = (TextView) view.findViewById(R.id.text_info);
            names = (TextView) view.findViewById(R.id.textv_name);
            //lastSeen = (TextView) view.findViewById(R.id.textv_name);
            rootLayout = view.findViewById(R.id.cardProfiles);
            soPix = (CircleImageView) view.findViewById(R.id.imgSavedUser);
        }

        public void setItems(int position) {
            SavedProfile model = list.get(position);

        }


    }
}

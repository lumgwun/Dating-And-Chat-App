package com.lahoriagency.cikolive.Adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.lahoriagency.cikolive.Classes.ImageLoader;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.UserProfileInfo;
import com.lahoriagency.cikolive.ProfileActivity;
import com.lahoriagency.cikolive.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SavedProfArrayAdapter extends ArrayAdapter<SavedProfile> {
    public static final String EXTRA_USER_PROFILE = "987";
    public static final String EXTRA_SWIPE_VIEW_SOURCE = "420";
    private ImageLoader imageLoader;
    private FragmentActivity fragmentActivity;
    Context context;
    private String names,lastSeen;
    private int distance;
    private int diamond;
    private SavedProfile profile;
    private UserProfileInfo userProfileInfo;
    List<SavedProfile> list = new ArrayList<>();

    public SavedProfArrayAdapter(Context context, ArrayList<SavedProfile> savedProfiles) {
        super(context, 0);
        this.context = context;
        this.list = savedProfiles;
    }

    public SavedProfArrayAdapter(Context context, List<SavedProfile> profileArrayList) {
        super(context, 0);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View contentView, @NonNull ViewGroup parent) {
        SavedProfArrayAdapter.ViewHolder holder;

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());

            contentView = inflater.inflate(R.layout.saved_prof_item, parent, false);
            holder = new SavedProfArrayAdapter.ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (SavedProfArrayAdapter.ViewHolder) contentView.getTag();
        }

        profile = getItem(position);
        if(profile !=null){
            diamond=profile.getDefaultDiamond();
            userProfileInfo=profile.getSavedPUserProfileInfo();
            names=profile.getSavedPName();
            lastSeen=profile.getSavedPLastSeen();

        }
        if(userProfileInfo !=null){
            distance=userProfileInfo.getDistance();
        }

        holder.name.setText(String.format("%s, %d", profile.getSavedPName()+""+lastSeen));
        holder.distanceAway.setText(distance+""+"away");
        holder.diamond.setText("Default "+diamond+""+"Diamonds");
        holder.relativeLayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                View cardView = fragmentActivity.findViewById(R.id.user_card_prof);
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(fragmentActivity,
                                Pair.create(cardView, "user_swipe_image_transition"));
                fragmentActivity.startActivity(getBundledIntent(profile), options.toBundle());
            }
        });
        Glide.with(context).load(profile.getSavedPImage()).into(holder.image);


        return contentView;
    }

    public SavedProfile getProfileByUserId(int userId) {
        for (int i = 0; i < getCount(); i++) {
            if (getItem(i).getSavedProfQBID() == userId) {
                return getItem(i);
            }
        }
        return null;
    }

    private Intent getBundledIntent(SavedProfile profile) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra(EXTRA_USER_PROFILE, (Parcelable) profile);
        intent.putExtra(EXTRA_SWIPE_VIEW_SOURCE, true);
        return intent;
    }

    private static class ViewHolder {
        public CardView cardView;
        public TextView name;
        public TextView diamond,distanceAway;
        public RelativeLayout relativeLayout;
        public CircleImageView image;

        ViewHolder(View view) {
            cardView = view.findViewById(R.id.user_card_prof);
            name = view.findViewById(R.id.userN);
            distanceAway = view.findViewById(R.id._distanceAway);
            relativeLayout = view.findViewById(R.id.Rel_L);
            diamond = view.findViewById(R.id.diamonds_default);
            image =  view.findViewById(R.id.user_prof_image);
        }
    }
}

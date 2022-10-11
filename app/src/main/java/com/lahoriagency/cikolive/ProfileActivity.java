package com.lahoriagency.cikolive;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.transition.Fade;
import android.transition.Transition;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.github.clans.fab.FloatingActionButton;
import com.github.ybq.parallaxviewpager.ParallaxViewPager;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Adapters.PictureAdapter;
import com.lahoriagency.cikolive.Adapters.UserSwipeProfileAdapter;
import com.lahoriagency.cikolive.Adapters.VideoAdapter;
import com.lahoriagency.cikolive.Classes.AppE;
import com.lahoriagency.cikolive.Classes.ImageLoader;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.SpacesItemDecoration;
import com.lahoriagency.cikolive.Classes.UserProfileInfo;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;


public class ProfileActivity extends AppCompatActivity implements  PictureAdapter.ItemListener, VideoAdapter.VideoListener {
    private boolean swipeViewSource;
    private ParallaxViewPager parallaxViewPager;
    private ImageLoader imageLoader;
    private FloatingActionButton fab;
    private CardView profileImageCard;
    private UserProfileInfo matchedProfileInfo,myProfileInfo;
    private static final String APPLICATION_ID = QUICKBLOX_APP_ID;   //QUICKBLOX_APP_ID
    private static final String AUTH_KEY = QUICKBLOX_AUTH_KEY;
    private static final String AUTH_SECRET = QUICKBLOX_SECRET_KEY;
    private static final String ACCOUNT_KEY = QUICKBLOX_ACCT_KEY;
    private static final String SERVER_URL = "";
    private QBUser cloudUser;
    private  QBUser currentUser;
    SharedPreferences userPreferences;
    private  SavedProfile savedProfile;
    private String userName,password,profileName;
    private int profileID;
    private static final String PREF_NAME = "Ciko";
    Gson gson2,gson3,gson,gson1;
    String json2,json3, json,json1;
    private Bundle bundle;
    private RecyclerView recyclerViewPic,recyclerViewVideos;
    private int mMargin=5;
    private int NUM_COLUMNS=3;
    private PictureAdapter pictureAdapter;
    private List<String> pictureArrayList;
    private ArrayList<String> videoArrayList;
    private PictureAdapter.ItemListener pictureListener;
    private VideoAdapter videoAdapter;
    private VideoAdapter.VideoListener videoListener;
    private double chatDiamondAmt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_profile);
        QBSettings.getInstance().init(this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        setTitle("Main Selection Arena");
        bundle= new Bundle();
        imageLoader = AppE.getImageLoader();
        matchedProfileInfo = new UserProfileInfo();
        myProfileInfo = new UserProfileInfo();
        currentUser= new QBUser();
        pictureArrayList= new ArrayList<>();
        videoArrayList= new ArrayList<>();
        gson = new Gson();
        gson= new Gson();
        gson1= new Gson();
        gson2= new Gson();
        gson3= new Gson();
        savedProfile= new SavedProfile();
        userPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = userPreferences.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = userPreferences.getString("LastQBUserUsed", "");
        currentUser = gson1.fromJson(json1, QBUser.class);
        json2 = userPreferences.getString("LastUserProfileInfoUsed", "");
        myProfileInfo = gson2.fromJson(json2, UserProfileInfo.class);
        profileID = userPreferences.getInt("SAVED_PROFILE_ID", 0);
        userName = userPreferences.getString("SAVED_PROFILE_EMAIL", "");
        password = userPreferences.getString("SAVED_PROFILE_PASSWORD", "");
        profileName = userPreferences.getString("SAVED_PROFILE_NAME", "");
        TextView description = findViewById(R.id.profile_description);
        TextView userDistance = findViewById(R.id.profile_about_distance);
        profileImageCard = findViewById(R.id.user_swipe_card_view);
        recyclerViewPic = findViewById(R.id.prof_pix_recy);
        recyclerViewVideos = findViewById(R.id.prof_video_recy);
        bundle=getIntent().getExtras();


        if(bundle !=null){
            matchedProfileInfo =bundle.getParcelable("UserProfileInfo");
        }
        if(matchedProfileInfo !=null){
            try {
                videoArrayList= matchedProfileInfo.getVideoLinks();
                pictureArrayList= matchedProfileInfo.getPhotoLinks();
                chatDiamondAmt=matchedProfileInfo.getChatDiamondAmount();
                swipeViewSource = getIntent().getExtras().getBoolean(UserSwipeProfileAdapter.EXTRA_SWIPE_VIEW_SOURCE);
                matchedProfileInfo = (UserProfileInfo) getIntent().getExtras().getParcelable(UserSwipeProfileAdapter.EXTRA_USER_PROFILE);
            } catch (NullPointerException e) {
                System.out.println("Oops!");
            }

        }
        for(int i=0;i<10;i++)
        {
            pictureArrayList.add("Item - "+i);
        }

        for(int i=0;i<10;i++)
        {
            videoArrayList.add("Item - "+i);
        }
        parallaxViewPager = findViewById(R.id.parallax_viewpager);
        TextView profileUsername = findViewById(R.id.profile_name);
        if(matchedProfileInfo !=null){
            profileUsername.setText(matchedProfileInfo.getName() + ", " + matchedProfileInfo.getAge());
            userDistance.setText(matchedProfileInfo.getDistance() + " miles away"+","+chatDiamondAmt+""+"Diamonds required");
            description.setText(matchedProfileInfo.getDescription());
            recyclerViewVideos.addItemDecoration(new SpacesItemDecoration(mMargin));
            recyclerViewVideos.setLayoutManager(new GridLayoutManager(ProfileActivity.this, NUM_COLUMNS));
            videoAdapter = new VideoAdapter(ProfileActivity.this,videoArrayList, videoListener);
            recyclerViewVideos.setItemAnimator(new DefaultItemAnimator());
            recyclerViewVideos.setAdapter(videoAdapter);
            recyclerViewVideos.setNestedScrollingEnabled(false);
            recyclerViewVideos.setClickable(true);

            recyclerViewPic.addItemDecoration(new SpacesItemDecoration(mMargin));
            recyclerViewPic.setLayoutManager(new GridLayoutManager(ProfileActivity.this, NUM_COLUMNS));

            pictureAdapter = new PictureAdapter(ProfileActivity.this, pictureArrayList,pictureListener);
            recyclerViewPic.setItemAnimator(new DefaultItemAnimator());
            recyclerViewPic.setAdapter(pictureAdapter);
            recyclerViewPic.setNestedScrollingEnabled(false);
            recyclerViewPic.setClickable(true);

        }

        if (!swipeViewSource) {
            TextView matchValue = findViewById(R.id.profile_match_text_view);
            if(matchedProfileInfo !=null){
                matchValue.setText("match in " + matchedProfileInfo.getMatchValue() + "%!");

            }

            matchValue.setVisibility(View.VISIBLE);
            ImageView profileMatchHeart = findViewById(R.id.profile_match_heart);
            profileMatchHeart.setVisibility(View.VISIBLE);
        }
        fab = findViewById(R.id.profileFab);
        if (!swipeViewSource)
            fab.setVisibility(View.VISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();

            }
        });

        getWindow().getSharedElementEnterTransition()
                .addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {
                        ObjectAnimator animator = ObjectAnimator
                                .ofFloat(profileImageCard, "radius", 0);
                        animator.setDuration(250);
                        animator.start();
                    }

                    @Override
                    public void onTransitionEnd(@NonNull Transition transition) {
                        if (swipeViewSource)
                            showFab();
                    }

                    @Override
                    public void onTransitionCancel(@NonNull Transition transition) {

                    }

                    @Override
                    public void onTransitionPause(@NonNull Transition transition) {

                    }

                    @Override
                    public void onTransitionResume(@NonNull Transition transition) {

                    }
                });

        Fade fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);

        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        initViewPager(matchedProfileInfo);
    }

    private void initViewPager(final UserProfileInfo userProfileInfo) {
        PagerAdapter adapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object obj) {
                container.removeView((View) obj);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = View.inflate(container.getContext(), R.layout.parallax_viewpager_item, null);
                ImageView imageView = (ImageView) view.findViewById(R.id.item_img);
                imageLoader.downloadImage(userProfileInfo.getPhotoLinks().get(position), imageView);
                container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                return view;
            }

            @Override
            public int getCount() {
                try {
                    return (null != userProfileInfo.getPhotoLinks() ? userProfileInfo.getPhotoLinks().size() : 0);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                return 0;
            }
        };
        try {
            parallaxViewPager.setAdapter(adapter);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }


    @SuppressLint("RestrictedApi")
    private void showFab() {
        fab.animate().cancel();
        fab.setScaleX(0f);
        fab.setScaleY(0f);
        fab.setAlpha(0f);
        fab.setVisibility(View.VISIBLE);
        fab.animate().setDuration(200).scaleX(1).scaleY(1).alpha(1)
                .setInterpolator(new LinearOutSlowInInterpolator());
    }

    @SuppressLint("RestrictedApi")
    private void hideFab() {
        fab.animate().cancel();
        fab.animate().setDuration(200).scaleX(0).scaleY(0).alpha(0)
                .setInterpolator(new LinearOutSlowInInterpolator());
        fab.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
        if (swipeViewSource)
            hideFab();

    }

    @Override
    public void onPictureClick(int position) {
        Bundle bundle= new Bundle();
        pictureArrayList= new ArrayList<>();
        if(matchedProfileInfo !=null){
            try {
                pictureArrayList= matchedProfileInfo.getPhotoLinks();
                swipeViewSource = getIntent().getExtras().getBoolean(UserSwipeProfileAdapter.EXTRA_SWIPE_VIEW_SOURCE);
                matchedProfileInfo = (UserProfileInfo) getIntent().getExtras().getParcelable(UserSwipeProfileAdapter.EXTRA_USER_PROFILE);
            } catch (NullPointerException e) {
                System.out.println("Oops!");
            }

        }
        Intent chatIntent = new Intent(ProfileActivity.this, MediaSliderAct.class);
        bundle.putStringArrayList("Pictures", (ArrayList<String>) pictureArrayList);
        bundle.putString("mediaFileType","image");
        chatIntent.putExtras(bundle);
        chatIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(chatIntent);

    }
    @Override
    public void onVideoClick(int position) {
        Bundle bundle= new Bundle();
        videoArrayList= new ArrayList<>();
        if(matchedProfileInfo !=null){
            try {
                videoArrayList= matchedProfileInfo.getVideoLinks();
                chatDiamondAmt=matchedProfileInfo.getChatDiamondAmount();
                swipeViewSource = getIntent().getExtras().getBoolean(UserSwipeProfileAdapter.EXTRA_SWIPE_VIEW_SOURCE);
                matchedProfileInfo = (UserProfileInfo) getIntent().getExtras().getParcelable(UserSwipeProfileAdapter.EXTRA_USER_PROFILE);
            } catch (NullPointerException e) {
                System.out.println("Oops!");
            }

        }
        Intent chatIntent = new Intent(ProfileActivity.this, MediaSliderAct.class);
        bundle.putString("mediaFileType","image");
        bundle.putStringArrayList("Videos", (ArrayList<String>) videoArrayList);
        chatIntent.putExtras(bundle);
        chatIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(chatIntent);

    }
}
package com.lahoriagency.cikolive.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.lahoriagency.cikolive.Adapters.UserSwipeProfileAdapter;
import com.lahoriagency.cikolive.Classes.AppE;
import com.lahoriagency.cikolive.Classes.BaseAsyncTask22;

import com.lahoriagency.cikolive.Classes.PushUtils;
import com.lahoriagency.cikolive.Classes.AppServerUser;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.SwipeUserRequest;
import com.lahoriagency.cikolive.Classes.UserProfileInfo;
import com.lahoriagency.cikolive.Classes.UserProfileInfoHolder;
import com.lahoriagency.cikolive.Classes.UserProfileInfoModel;
import com.lahoriagency.cikolive.Classes.UserProfileInfoReply;
import com.lahoriagency.cikolive.Classes.UserSwipeReply;
import com.lahoriagency.cikolive.ContentAct;
import com.lahoriagency.cikolive.Interfaces.ServerMethodsConsts;
import com.lahoriagency.cikolive.MainActivity;
import com.lahoriagency.cikolive.R;
import com.quickblox.users.model.QBUser;

import java.util.Objects;

import link.fls.swipestack.SwipeStack;

import static android.content.Context.MODE_PRIVATE;

@SuppressWarnings("deprecation")
public class SwipeFragment extends Fragment implements SwipeStack.SwipeStackListener {

    private SwipeStack mSwipeStack;
    private LinearLayout swipeDislikeButtonLayout;
    private LinearLayout swipeLikeButtonLayout;
    private UserSwipeProfileAdapter userSwipeProfileAdapter;
    private OnMatchCreated onMatchCreatedListener;

    private GetSwipeUsers getSwipeUsers;
    private SwipeUser swipeUser;
    //private MyPreferences preferences;
    private SharedPreferences userPreferences;
    private QBUser qbUser;
    private int qbUserID,userProfileInfoID,matchedValue;
    private long qbUserIDLong;
    private UserProfileInfo profile;
    private String name;

    public SwipeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewRoot = inflater.inflate(R.layout.fragment_swipe, container, false);
        //MainActivity mainActivity = (MainActivity) getContext();
        //ContentAct contentAct = (ContentAct) getContext();
        qbUser= new QBUser();
        userPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("LastQBUserUsed", MODE_PRIVATE);
        Gson gson = new Gson();

        String json = userPreferences.getString("LastQBUserUsed", "");
        qbUser = gson.fromJson(json, QBUser.class);
        if(qbUser !=null){
            qbUserID=qbUser.getId();
        }
        getSwipeUsers = new GetSwipeUsers(ServerMethodsConsts.USERSTOSWIPE + "/" + qbUserID);
        userSwipeProfileAdapter = new UserSwipeProfileAdapter(getContext());
        swipeDislikeButtonLayout = viewRoot.findViewById(R.id.swipe_dislike_button_layout);
        swipeLikeButtonLayout = viewRoot.findViewById(R.id.swipe_like_button_layout);

        checkUserList();

        mSwipeStack = viewRoot.findViewById(R.id.swipeStack);
        mSwipeStack.setAdapter(userSwipeProfileAdapter);
        mSwipeStack.setListener(this);

        swipeDislikeButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeStack.swipeTopViewToLeft();
            }
        });
        swipeLikeButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeStack.swipeTopViewToRight();
            }
        });
        setup(viewRoot);
        return viewRoot;
    }

    private void setup(View viewRoot) {
        //preferences = AppE.getPreferences();
       //FacebookSdk.sdkInitialize(getApplicationContext());


    }

    private void checkUserList() {
        userPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("LastQBUserUsed", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = userPreferences.getString("LastQBUserUsed", "");
        qbUser = gson.fromJson(json, QBUser.class);
        if(qbUser !=null){
            qbUserID=qbUser.getId();
        }
        if (userSwipeProfileAdapter.isEmpty()) {
            swipeDislikeButtonLayout.setVisibility(View.INVISIBLE);
            swipeLikeButtonLayout.setVisibility(View.INVISIBLE);
            getSwipeUsers = new GetSwipeUsers(ServerMethodsConsts.USERSTOSWIPE + "/" + qbUserID);
            getSwipeUsers.execute();
        }
    }

    @Override
    public void onViewSwipedToLeft(int position) {
        userPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("LastQBUserUsed", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = userPreferences.getString("LastQBUserUsed", "");
        qbUser = gson.fromJson(json, QBUser.class);
        if(qbUser !=null){
            qbUserID=qbUser.getId();
        }
        if(qbUserID>0){
            qbUserIDLong=qbUserID;
        }
        profile = userSwipeProfileAdapter.getItem(position);
        if(profile !=null){
            userProfileInfoID=profile.getUserProfInfoID();
            matchedValue=profile.getMatchValue();
            name=profile.getName();
        }
        SwipeUserRequest swipeUserRequest = new SwipeUserRequest(qbUserIDLong, userProfileInfoID,
                0, name, matchedValue);
        swipeUser = new SwipeUser(ServerMethodsConsts.SWIPED, swipeUserRequest);
        swipeUser.setHttpMethod("POST");
        swipeUser.execute();
    }

    @Override
    public void onViewSwipedToRight(int position) {
        userPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("LastQBUserUsed", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = userPreferences.getString("LastQBUserUsed", "");
        qbUser = gson.fromJson(json, QBUser.class);
        if(qbUser !=null){
            qbUserID=qbUser.getId();
        }
        if(qbUserID>0){
            qbUserIDLong=qbUserID;
        }
        profile = userSwipeProfileAdapter.getItem(position);
        if(profile !=null){
            userProfileInfoID=profile.getUserProfInfoID();
            matchedValue=profile.getMatchValue();
            name=profile.getName();
        }

        SwipeUserRequest swipeUserRequest = new SwipeUserRequest(qbUserIDLong, userProfileInfoID,
                1, name, matchedValue);
        swipeUser = new SwipeUser(ServerMethodsConsts.SWIPED, swipeUserRequest);
        swipeUser.setHttpMethod("POST");
        swipeUser.execute();
    }

    @Override
    public void onStackEmpty() {
        checkUserList();
    }

    private class GetSwipeUsers extends BaseAsyncTask22<Void> {

        public GetSwipeUsers(String urn) {
            super(urn, null);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (result != null) {
                    UserProfileInfoReply userProfileInfoReply = AppE.getGson().fromJson(result, UserProfileInfoReply.class);
                    if (userProfileInfoReply.isStatusOkay() && userProfileInfoReply.getUsersProfileInfo() != null) {
                        for (UserProfileInfoModel model : userProfileInfoReply.getUsersProfileInfo()) {
                            UserProfileInfo profile = new UserProfileInfo(model);
                            userSwipeProfileAdapter.add(profile);
                        }
                        userSwipeProfileAdapter.notifyDataSetChanged();
                        if (userSwipeProfileAdapter.getCount() > 0) {
                            swipeDislikeButtonLayout.setVisibility(View.VISIBLE);
                            swipeLikeButtonLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            if (result != null) {
                UserSwipeReply userSwipeReply = AppE.getGson().fromJson(result, UserSwipeReply.class);
                if (userSwipeReply.isMatch()) {
                    MainActivity mainActivity = (MainActivity) getContext();
                    profile = userSwipeProfileAdapter.getProfileByUserId(userSwipeReply.getUserId());
                    UserProfileInfoHolder.getInstance().putProfileInfo(profile);
                    if (mainActivity != null) {
                        ((DialogsFragment) Objects.requireNonNull(mainActivity.getContentFragment().getFragmentForPosition(2))).createNewDialog(userSwipeReply.getRecipientQuickBloxId(), userSwipeReply.getMatchValue());
                    }
                    PushUtils.sendPushAboutNewPair(userSwipeReply.getRecipientQuickBloxId());
                    onMatchCreatedListener.showMatchDialog(profile, false);
                }
            }
        }

    }
    private class SwipeUser extends BaseAsyncTask22<SwipeUserRequest> {

        public SwipeUser(String urn, SwipeUserRequest params) {
            super(urn, params);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                UserSwipeReply userSwipeReply = AppE.getGson().fromJson(result, UserSwipeReply.class);
                if (userSwipeReply.isMatch()) {
                    MainActivity mainActivity = (MainActivity) getContext();
                    UserProfileInfo userProfile = userSwipeProfileAdapter.getProfileByUserId(userSwipeReply.getUserId());
                    UserProfileInfoHolder.getInstance().putProfileInfo(userProfile);
                    if (mainActivity != null) {
                        ((DialogsFragment) mainActivity.getContentFragment().getFragmentForPosition(2)).createNewDialog(userSwipeReply.getRecipientQuickBloxId(), userSwipeReply.getMatchValue());
                    }
                    PushUtils.sendPushAboutNewPair(userSwipeReply.getRecipientQuickBloxId());
                    onMatchCreatedListener.showMatchDialog(userProfile, false);
                }
            }
        }
    }

    public void setOnMatchCreatedListener(OnMatchCreated onMatchCreatedListener) {
        this.onMatchCreatedListener = onMatchCreatedListener;
    }

    public interface OnMatchCreated {
        void showMatchDialog(UserProfileInfo userProfileInfo, boolean fromQueue);
        void showMatchDialog(SavedProfile savedProfile, boolean fromQueue);
        void showMatchDialog(AppServerUser appServerUser, boolean fromQueue);
    }

}

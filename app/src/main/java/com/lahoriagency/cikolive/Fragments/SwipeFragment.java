package com.lahoriagency.cikolive.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.lahoriagency.cikolive.Adapters.UserSwipeProfileAdapter;
import com.lahoriagency.cikolive.Classes.AppChat;
import com.lahoriagency.cikolive.Classes.BaseAsyncTask22;
import com.lahoriagency.cikolive.Classes.MyPreferences;
import com.lahoriagency.cikolive.Classes.PushUtils;
import com.lahoriagency.cikolive.Classes.QBUser;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.SwipeUserRequest;
import com.lahoriagency.cikolive.Classes.UserProfileInfo;
import com.lahoriagency.cikolive.Classes.UserProfileInfoHolder;
import com.lahoriagency.cikolive.Classes.UserProfileInfoModel;
import com.lahoriagency.cikolive.Classes.UserProfileInfoReply;
import com.lahoriagency.cikolive.Classes.UserSwipeReply;
import com.lahoriagency.cikolive.Interfaces.ServerMethodsConsts;
import com.lahoriagency.cikolive.MainActivity;
import com.lahoriagency.cikolive.R;

import link.fls.swipestack.SwipeStack;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SwipeFragment extends Fragment implements SwipeStack.SwipeStackListener {

    private SwipeStack mSwipeStack;
    private LinearLayout swipeDislikeButtonLayout;
    private LinearLayout swipeLikeButtonLayout;
    private UserSwipeProfileAdapter userSwipeProfileAdapter;
    private OnMatchCreated onMatchCreatedListener;

    private GetSwipeUsers getSwipeUsers;
    private SwipeUser swipeUser;
    private MyPreferences preferences;

    public SwipeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewRoot = inflater.inflate(R.layout.fragment_swipe, container, false);
        setup(viewRoot);
        return viewRoot;
    }

    private void setup(View viewRoot) {
        preferences = AppChat.getPreferences();

        getSwipeUsers = new GetSwipeUsers(ServerMethodsConsts.USERSTOSWIPE + "/" + preferences.getUserId());

        userSwipeProfileAdapter = new UserSwipeProfileAdapter(AppChat.getAppContext(), getActivity());
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
    }

    private void checkUserList() {
        if (userSwipeProfileAdapter.isEmpty()) {
            swipeDislikeButtonLayout.setVisibility(View.INVISIBLE);
            swipeLikeButtonLayout.setVisibility(View.INVISIBLE);
            getSwipeUsers = new GetSwipeUsers(ServerMethodsConsts.USERSTOSWIPE + "/" + preferences.getUserId());
            getSwipeUsers.execute();
        }
    }

    @Override
    public void onViewSwipedToLeft(int position) {
        UserProfileInfo profile = userSwipeProfileAdapter.getItem(position);
        SwipeUserRequest swipeUserRequest = new SwipeUserRequest(preferences.getUserId(), profile.getUserProfInfoID(),
                0, profile.getName(), profile.getMatchValue());
        swipeUser = new SwipeUser(ServerMethodsConsts.SWIPED, swipeUserRequest);
        swipeUser.setHttpMethod("POST");
        swipeUser.execute();
    }

    @Override
    public void onViewSwipedToRight(int position) {
        UserProfileInfo profile = userSwipeProfileAdapter.getItem(position);
        SwipeUserRequest swipeUserRequest = new SwipeUserRequest(preferences.getUserId(), profile.getUserProfInfoID(),
                1, profile.getName(), profile.getMatchValue());
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
                    UserProfileInfoReply userProfileInfoReply = AppChat.getGson().fromJson(result, UserProfileInfoReply.class);
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
                Toast.makeText(getApplicationContext(), "error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            if (result != null) {
                UserSwipeReply userSwipeReply = AppChat.getGson().fromJson(result, UserSwipeReply.class);
                if (userSwipeReply.isMatch()) {
                    MainActivity mainActivity = (MainActivity) getContext();
                    UserProfileInfo userProfile = userSwipeProfileAdapter.getProfileByUserId(userSwipeReply.getUserId());
                    UserProfileInfoHolder.getInstance().putProfileInfo(userProfile);
                    ((DialogsFragment) mainActivity.getContentFragment().getFragmentForPosition(2)).createNewDialog(userSwipeReply.getRecipientQuickBloxId(), userSwipeReply.getMatchValue());
                    PushUtils.sendPushAboutNewPair(userSwipeReply.getRecipientQuickBloxId());
                    onMatchCreatedListener.showMatchDialog(userProfile, false);
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
                UserSwipeReply userSwipeReply = AppChat.getGson().fromJson(result, UserSwipeReply.class);
                if (userSwipeReply.isMatch()) {
                    MainActivity mainActivity = (MainActivity) getContext();
                    UserProfileInfo userProfile = userSwipeProfileAdapter.getProfileByUserId(userSwipeReply.getUserId());
                    UserProfileInfoHolder.getInstance().putProfileInfo(userProfile);
                    ((DialogsFragment) mainActivity.getContentFragment().getFragmentForPosition(2)).createNewDialog(userSwipeReply.getRecipientQuickBloxId(), userSwipeReply.getMatchValue());
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
        void showMatchDialog(QBUser qbUser, boolean fromQueue);
    }

}

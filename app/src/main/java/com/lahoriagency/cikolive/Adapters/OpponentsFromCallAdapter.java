package com.lahoriagency.cikolive.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import com.lahoriagency.cikolive.Classes.AppChat;
import com.lahoriagency.cikolive.Classes.QBRTCSessionUtils;
import com.lahoriagency.cikolive.Conference.AppConference;
import com.lahoriagency.cikolive.R;
import com.lahoriagency.cikolive.Conference.BaseConversationFragment;
import com.quickblox.conference.ConferenceSession;
import com.quickblox.conference.view.QBConferenceSurfaceView;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.quickblox.videochat.webrtc.view.QBRTCSurfaceView;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecation")
public class OpponentsFromCallAdapter extends RecyclerView.Adapter<OpponentsFromCallAdapter.ViewHolder> {

    private static final String TAG = OpponentsFromCallAdapter.class.getSimpleName();
    private  int itemHeight;
    private  int itemWidth;

    private Context context;
    private BaseConversationFragment baseConversationFragment;
    private List<QBUser> opponents;
    private LayoutInflater inflater;
    private OnAdapterEventListener adapterListener;

    private int layoutHeight;
    private ConferenceSession session;



    public OpponentsFromCallAdapter(Context context, BaseConversationFragment baseConversationFragment, List<QBUser> users, int width, int height) {
        this.context = context;
        this.baseConversationFragment = baseConversationFragment;
        this.opponents = users;
        this.inflater = LayoutInflater.from(context);
        itemWidth = width;
        itemHeight = height;
        Log.d(TAG, "item width=" + itemWidth + ", item height=" + itemHeight);
    }

    public void setAdapterListener(OnAdapterEventListener adapterListener) {
        this.adapterListener = adapterListener;
    }
    public OpponentsFromCallAdapter(Context context, ConferenceSession session, List<QBUser> users, int layoutHeight) {
        this.context = context;
        this.session = session;
        this.opponents = users;
        this.inflater = LayoutInflater.from(context);
        this.layoutHeight = layoutHeight;
    }

    public void clearOpponents(List<QBUser> opponents) {
        opponents.clear();
    }




    public void updateUserFullName(QBUser user) {
        int position = opponents.indexOf(user);
        QBUser userInList = opponents.get(position);
        userInList.setFullName(user.getFullName());
        notifyItemChanged(position);
    }


    @Override
    public int getItemCount() {
        return opponents.size();
    }

    public Integer getItem(int position) {
        return opponents.get(position).getId();
    }

    public List<QBUser> getOpponents() {
        return opponents;
    }

    public void removeItem(int index) {
        opponents.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, opponents.size());
    }

    public void replaceUsers(int position, QBUser qbUser) {
        opponents.set(position, qbUser);
        notifyItemChanged(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.list_item_opponent_from_call, null);
        v.findViewById(R.id.innerLayout).setLayoutParams(new FrameLayout.LayoutParams(itemWidth, itemHeight));

        ViewHolder vh = new ViewHolder(v);
        vh.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                int position = vh.getAdapterPosition();
                Integer userID = getItem(position);
                adapterListener.onToggleButtonItemClick(userID, isChecked);
            }
        });

        vh.flItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = vh.getAdapterPosition();
                Integer userID = getItem(position);
                adapterListener.onOpponentViewItemClick(userID);
            }
        });
        vh.setListener(new ViewHolder.ViewHolderClickListener() {
            @Override
            public void onShowOpponent(int callerId) {
                adapterListener.onItemClick(callerId);
            }
        });
        vh.showOpponentView(true);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final QBUser user = opponents.get(position);
        int userID = user.getId();
        holder.opponentsName.setText(user.getFullName());
        holder.opponentName.setText(user.getFullName());

        holder.getOpponentView().setId(user.getId());
        holder.setUserId(userID);
        String username = TextUtils.isEmpty(user.getFullName()) ? user.getLogin() : user.getFullName();
        QBUser currentUser = ((AppConference) context.getApplicationContext()).getSharedPrefsHelper().getQbUser();
        if (Objects.equals(userID, currentUser.getId())) {
            holder.opponentName.setText(R.string.you);
        } else {
            holder.opponentName.setText(username);
        }

        boolean isAudioEnabled = true;
        if (session.getMediaStreamManager() != null) {
            try {
                isAudioEnabled = session.getMediaStreamManager().getAudioTrack(userID).enabled();
            } catch (Exception e) {
                if (e.getMessage() != null) {
                    Log.d(TAG, e.getMessage());
                }
            }
            holder.toggleButton.setChecked(isAudioEnabled);
        }

        holder.innerLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, getItemHeight()));
        holder.getSurfaceView().setId(user.getId());
        holder.setUserId(userID);

        if (isAudioEnabled) {
            holder.ivMutedOpponentIndicator.setVisibility(View.GONE);
        } else {
            holder.ivMutedOpponentIndicator.setVisibility(View.VISIBLE);
        }

        QBRTCTypes.QBRTCConnectionState state = baseConversationFragment.getConnectionState(userID);
        if (state != null) {
            Log.d(TAG, "State ordinal= " + state.ordinal());
            holder.setStatus(context.getString(QBRTCSessionUtils.getStatusDescriptionResource(state)));
        }
        if (position == (opponents.size() - 1)) {
            adapterListener.OnBindLastViewHolder(holder, position);
        }
    }
    private int getItemHeight() {
        int itemsCount = opponents.size();
        int itemHeight = layoutHeight;

        if (itemsCount >= 2 && itemsCount <= 6) {
            itemHeight = layoutHeight / 2;
        }
        if (itemsCount >= 7 && itemsCount <= 9) {
            itemHeight = layoutHeight / 3;
        }
        if (itemsCount >= 10 && itemsCount <= 12) {
            itemHeight = layoutHeight / 4;
        }

        return itemHeight;
    }
    public void add(QBUser item) {
        if (session.getCurrentUserID().equals(item.getId())) {
            opponents.add(0, item);
        } else {
            opponents.add(item);
        }
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("deprecation")
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView opponentsName;
        TextView connectionStatus;
        QBRTCSurfaceView opponentView;
        ProgressBar progressBar;
        private int userId;
        private ViewHolderClickListener viewHolderClickListener;
        FrameLayout flItemContainer;
        RelativeLayout innerLayout;
        ToggleButton toggleButton;
        TextView opponentName;
        QBConferenceSurfaceView opponentViewC;
        ProgressBar progressBarC;
        ImageView ivMutedOpponentIndicator;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            opponentsName = (TextView) itemView.findViewById(R.id.opponentName);
            connectionStatus = (TextView) itemView.findViewById(R.id.connectionStatus);
            opponentView = (QBRTCSurfaceView) itemView.findViewById(R.id.opponentView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar_adapter);

            flItemContainer = itemView.findViewById(R.id.con_FrameLa);
            toggleButton = itemView.findViewById(R.id.opponent_toggle_mic);
            opponentName = itemView.findViewById(R.id.con_oppName);
            opponentViewC = itemView.findViewById(R.id.con_oppView);
            progressBar = itemView.findViewById(R.id.progress_bar_c);
            innerLayout = itemView.findViewById(R.id.con_Layout);
            ivMutedOpponentIndicator = itemView.findViewById(R.id.iv_muted_indicator);
        }

        private void setListener(ViewHolderClickListener viewHolderClickListener) {
            this.viewHolderClickListener = viewHolderClickListener;
        }

        public void setStatus(String status) {
            connectionStatus.setText(status);
        }

        public void setUserName(String userName) {
            opponentsName.setText(userName);
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getUserId() {
            return userId;
        }

        public ProgressBar getProgressBar() {
            return progressBar;
        }

        public QBRTCSurfaceView getOpponentView() {
            return opponentView;
        }

        public void showOpponentView(boolean show) {
            Log.d("UsersAdapter", "show? " + show);
            opponentView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        public QBConferenceSurfaceView getSurfaceView() {
            return opponentViewC;
        }

        @Override
        public void onClick(View v) {
            viewHolderClickListener.onShowOpponent(getAdapterPosition());
        }

        public interface ViewHolderClickListener {
            void onShowOpponent(int callerId);
        }
    }

    public interface OnAdapterEventListener {
        void OnBindLastViewHolder(ViewHolder holder, int position);

        void onItemClick(int position);
        void onToggleButtonItemClick(Integer userID, boolean isChecked);

        void onOpponentViewItemClick(Integer userID);
    }

}

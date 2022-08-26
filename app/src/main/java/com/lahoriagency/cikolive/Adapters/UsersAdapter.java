package com.lahoriagency.cikolive.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lahoriagency.cikolive.Classes.ResourceUtils;
import com.lahoriagency.cikolive.Classes.UiUtils;
import com.lahoriagency.cikolive.R;
import com.quickblox.chat.QBChatService;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("deprecation")
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private Context context;
    List<QBUser> usersList;
    List<QBUser> selectedUsers;
    protected QBUser currentUser;
    private SelectedItemsCountChangedListener selectedItemsCountChangedListener;


    public UsersAdapter(Context context, List<QBUser> users) {
        this.context = context;
        currentUser = QBChatService.getInstance().getUser();
        usersList = users;
        this.selectedUsers = new ArrayList<>();
    }

    public void addNewList(List<QBUser> users) {
        usersList.clear();
        usersList.addAll(users);
        for (QBUser user : users) {
            if (isUserMe(user)) {
                usersList.remove(user);
            }
        }
        notifyDataSetChanged();
    }

    public void addUsers(List<QBUser> users) {
        for (QBUser user : users) {
            if (!usersList.contains(user)) {
                usersList.add(user);
            }
        }
        notifyDataSetChanged();
    }


    public void removeUsers(List<QBUser> users) {
        for (QBUser user : users) {
            usersList.remove(user);
        }
        notifyDataSetChanged();
    }

    public void clearList() {
        usersList.clear();
        notifyDataSetChanged();
    }
    public QBUser getItem(int position) {
        return usersList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return (null != usersList ? usersList.size() : 0);
    }

    private boolean isUserMe(QBUser user) {
        return currentUser != null && currentUser.getId().equals(user.getId());
    }

    protected boolean isAvailableForSelection(QBUser user) {
        return currentUser == null || !currentUser.getId().equals(user.getId());
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView opponentIcon;
        TextView opponentName;
        LinearLayout rootLayout;

        //CircleImageView userImageView;
        //TextView loginTextView;
        CheckBox userCheckBox;
        TextView userAvatarTitle;

        public ViewHolder(@NonNull View view) {
            super(view);
            opponentIcon = view.findViewById(R.id.image_opponent_icon);
            opponentName = view.findViewById(R.id.opponents_name);
            rootLayout = view.findViewById(R.id.root_layout);
            userAvatarTitle = view.findViewById(R.id.text_user_avatar_title);
            userCheckBox = view.findViewById(R.id.checkbox_user);


            //userImageView = view.findViewById(R.id.image_user);
            //loginTextView = view.findViewById(R.id.text_user_login);


        }
    }


    public List<QBUser> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedItemsCountsChangedListener(SelectedItemsCountChangedListener selectedItemsCountChanged) {
        if (selectedItemsCountChanged != null) {
            this.selectedItemsCountChangedListener = selectedItemsCountChanged;
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_opponents_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QBUser user = usersList.get(position);
        holder.opponentName.setText(user.getFullName());
        if (selectedUsers.contains(user)) {
            holder.rootLayout.setBackgroundResource(R.color.background_color_selected_user_item);
            holder.opponentIcon.setBackgroundDrawable(
                    UiUtils.getColoredCircleDrawable(context.getResources().getColor(R.color.icon_background_color_selected_user)));
            holder.opponentIcon.setImageResource(R.drawable.ic_all_cus);
        } else {
            holder.rootLayout.setBackgroundResource(R.color.background_color_normal_user_item);
            holder.opponentIcon.setBackgroundDrawable(UiUtils.getColorCircleDrawable(user.getId().hashCode()));
            holder.opponentIcon.setImageResource(R.drawable.ic_admin_user);
        }
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSelection(user);
                selectedItemsCountChangedListener.onCountSelectedItemsChanged(selectedUsers.size());
            }
        });


        String userName = TextUtils.isEmpty(user.getFullName()) ? user.getLogin() : user.getFullName();

        if (isUserMe(user)) {
            holder.opponentName.setText(context.getString(R.string.placeholder_username_you, userName));
            holder.userCheckBox.setVisibility(View.GONE);
        } else {
            holder.opponentName.setText(userName);
            //holder.userCheckBox.setVisibility(View.VISIBLE);
        }

        if (isAvailableForSelection(user)) {
            holder.opponentName.setTextColor(ResourceUtils.getColor(R.color.text_color_black));
            holder.userCheckBox.setVisibility(View.VISIBLE);
        } else {
            holder.opponentName.setTextColor(ResourceUtils.getColor(R.color.text_color_medium_grey));
            holder.userCheckBox.setVisibility(View.GONE);

        }
        boolean containsUser = selectedUsers.contains(user);
        holder.userCheckBox.setChecked(containsUser);

        if (containsUser) {
            holder.rootLayout.setBackgroundColor(ResourceUtils.getColor(R.color.selected_list_item_color));
        } else {
            holder.rootLayout.setBackgroundColor(ResourceUtils.getColor(android.R.color.transparent));
        }




        if (!TextUtils.isEmpty(user.getFullName())) {
            String avatarTitle = String.valueOf(user.getFullName().charAt(0)).toUpperCase();
            holder.userAvatarTitle.setText(avatarTitle);
        }
    }


    public void updateUsersList(List<QBUser> usersList) {
        this.usersList = usersList;
        notifyDataSetChanged();
    }

    private void toggleSelection(QBUser qbUser) {
        if (selectedUsers.contains(qbUser)){
            selectedUsers.remove(qbUser);
        } else {
            selectedUsers.add(qbUser);
        }
        notifyDataSetChanged();
    }



    public interface SelectedItemsCountChangedListener {
        void onCountSelectedItemsChanged(Integer count);
    }
}

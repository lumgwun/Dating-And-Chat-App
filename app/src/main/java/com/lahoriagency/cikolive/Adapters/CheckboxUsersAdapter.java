package com.lahoriagency.cikolive.Adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CheckboxUsersAdapter extends UsersAdapter implements ListAdapter {

    private List<Integer> initiallySelectedUsers;
    private Set<QBUser> selectedUsers;
    List<QBUser> usersList;

    public CheckboxUsersAdapter(Context context, List<QBUser> users) {
        super(context, users);
        selectedUsers = new HashSet<>();
        initiallySelectedUsers = new ArrayList<>();
    }


    public void onItemClicked(int position, View convertView, ViewGroup parent) {
        QBUser user = getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();

        if (!isAvailableForSelection(user)) {
            return;
        }

        holder.userCheckBox.setChecked(!holder.userCheckBox.isChecked());
        if (holder.userCheckBox.isChecked()) {
            selectedUsers.add(user);
        } else {
            selectedUsers.remove(user);
        }
        notifyDataSetChanged();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public QBUser getItem(int position) {
        return usersList.get(position);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    protected boolean isAvailableForSelection(QBUser user) {
        return super.isAvailableForSelection(user) && !initiallySelectedUsers.contains(user.getId());
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }
}

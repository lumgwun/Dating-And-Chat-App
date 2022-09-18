package com.lahoriagency.cikolive.Classes;

import com.quickblox.users.model.QBUser;

public class FriendList {
    private QBUser user;
    private boolean isSelected = false;


    public FriendList(QBUser user, boolean isSelected) {
        this.user = user;
        this.isSelected = isSelected;
    }

    public QBUser getUser() {
        return user;
    }

    public void setUser(QBUser user) {
        this.user = user;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

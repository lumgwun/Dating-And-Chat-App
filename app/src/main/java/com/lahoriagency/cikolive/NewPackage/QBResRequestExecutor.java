package com.lahoriagency.cikolive.NewPackage;

import android.content.Context;

import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class QBResRequestExecutor {
    private static QBResRequestExecutor instance;
    Context _context;
    public QBResRequestExecutor(Context context) {
        this._context = context;
    }

    public QBResRequestExecutor() {
        super();
    }

    public static synchronized QBResRequestExecutor getInstance() {
        if (instance == null) {
            instance = new QBResRequestExecutor();
        }

        return instance;
    }
    private String TAG = QBResRequestExecutor.class.getSimpleName();

    public void signUpNewUser(final QBUser newQbUser, final QBEntityCallback<QBUser> callback) {
        QBUsers.signUp(newQbUser).performAsync(callback);
    }

    public void signInUser(final QBUser currentQbUser, final QBEntityCallback<QBUser> callback) {
        QBUsers.signIn(currentQbUser).performAsync(callback);
    }

    public void deleteCurrentUser(int currentQbUserID, QBEntityCallback<Void> callback) {
        QBUsers.deleteUser(currentQbUserID).performAsync(callback);
    }

    public void loadUsersByTag(final String tag, final QBEntityCallback<ArrayList<QBUser>> callback) {
        QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder();
        List<String> tags = new LinkedList<>();
        tags.add(tag);

        QBUsers.getUsersByTags(tags, requestBuilder).performAsync(callback);
    }

    public void loadUsersByIds(final Collection<Integer> usersIDs, final QBEntityCallback<ArrayList<QBUser>> callback) {
        QBUsers.getUsersByIDs(usersIDs, null).performAsync(callback);
    }
}

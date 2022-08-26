package com.lahoriagency.cikolive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Adapters.UserAdapter2;
import com.lahoriagency.cikolive.Classes.ChatHelper;
import com.lahoriagency.cikolive.Classes.DialogsManager;
import com.lahoriagency.cikolive.Classes.PreferencesManager;
import com.lahoriagency.cikolive.Classes.QbUsersHolder;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.ToastUtils;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

public class ChatInfoAct extends BaseActivity {
    private static final String TAG = ChatInfoAct.class.getSimpleName();
    private static final String EXTRA_DIALOG = "extra_dialog";
    private QBUser cloudUser;
    private String userName,password;
    private PreferencesManager preferencesManager;
    SharedPreferences userPreferences;
    Gson gson, gson1;
    int profileID;
    private FloatingActionButton fab;
    SharedPreferences sharedPref;
    private SavedProfile savedProfile;
    private static final String PREF_NAME = "Ciko";
    String json, json1, json2;
    private QBUser qbUser;

    private ListView usersListView;
    private QBChatDialog qbDialog;
    private UserAdapter2 userAdapter;
    private SystemMessagesListener systemMessagesListener = new SystemMessagesListener();
    private DialogsManager dialogsManager = new DialogsManager();
    private QBSystemMessagesManager systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();

    public static void start(Context context, QBChatDialog qbDialog) {
        Intent intent = new Intent(context, ChatInfoAct.class);
        intent.putExtra(EXTRA_DIALOG, qbDialog);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chat_info);
        qbDialog = (QBChatDialog) getIntent().getSerializableExtra(EXTRA_DIALOG);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(qbDialog.getName());
            getSupportActionBar().setSubtitle(getString(R.string.chat_info_subtitle, String.valueOf(qbDialog.getOccupants().size())));
        }
        usersListView = findViewById(R.id.list_chat_info_users);
        List<Integer> userIds = qbDialog.getOccupants();
        List<QBUser> users = QbUsersHolder.getInstance().getUsersByIds(userIds);
        userAdapter = new UserAdapter2(this, users);
        usersListView = findViewById(R.id.list_chat_info_users);
        usersListView.setAdapter(userAdapter);
        getDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        systemMessagesManager.removeSystemMessageListener(systemMessagesListener);
    }

    @Override
    public void onResumeFinished() {
        systemMessagesManager.addSystemMessageListener(systemMessagesListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_chat_info_action_add_people) {
            updateDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void getDialog() {
        String dialogID = qbDialog.getDialogId();
        ChatHelper.getInstance().getDialogById(dialogID, new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                qbDialog = qbChatDialog;
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setSubtitle(getString(R.string.chat_info_subtitle, String.valueOf(qbDialog.getOccupants().size())));
                }
                buildUserList();
            }

            @Override
            public void onError(QBResponseException e) {
                ToastUtils.shortToast(e.getMessage());
                finish();
            }
        });
    }

    private void buildUserList() {
        List<Integer> userIds = qbDialog.getOccupants();
        if (QbUsersHolder.getInstance().hasAllUsers(userIds)) {
            List<QBUser> users = QbUsersHolder.getInstance().getUsersByIds(userIds);
            userAdapter.clearList();
            userAdapter.addUsers(users);
        } else {
            ChatHelper.getInstance().getUsersFromDialog(qbDialog, new QBEntityCallback<ArrayList<QBUser>>() {
                @Override
                public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                    if (qbUsers != null) {
                        QbUsersHolder.getInstance().putUsers(qbUsers);
                        userAdapter.addUsers(qbUsers);
                    }
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.d(TAG, e.getMessage());
                }
            });
        }
    }

    private void updateDialog() {
        showProgressDialog(R.string.dlg_updating);
        Log.d(TAG, "Starting Dialog Update");
        ChatHelper.getInstance().getDialogById(qbDialog.getDialogId(), new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog updatedChatDialog, Bundle bundle) {
                Log.d(TAG, "Update Dialog Successful: " + updatedChatDialog.getDialogId());
                qbDialog = updatedChatDialog;
                hideProgressDialog();
                SelectUsersActivity.startForResult(ChatInfoAct.this, ChatAct.REQUEST_CODE_SELECT_PEOPLE, updatedChatDialog);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(TAG, "Dialog Loading Error: " + e.getMessage());
                hideProgressDialog();
                //showErrorSnackbar(R.string.select_users_get_dialog_error, e, null);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult with resultCode: $resultCode requestCode: $requestCode");
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ChatAct.REQUEST_CODE_SELECT_PEOPLE && data != null) {
                showProgressDialog(R.string.chat_info_updating);
                final List<QBUser> selectedUsers = (ArrayList<QBUser>) data.getSerializableExtra(SelectUsersActivity.EXTRA_QB_USERS);
                List<Integer> existingOccupants = qbDialog.getOccupants();
                final List<Integer> newUserIds = new ArrayList<>();

                for (QBUser user : selectedUsers) {
                    if (!existingOccupants.contains(user.getId())) {
                        newUserIds.add(user.getId());
                    }
                }

                ChatHelper.getInstance().getDialogById(qbDialog.getDialogId(), new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        qbDialog = qbChatDialog;
                        dialogsManager.sendMessageAddedUsers(qbChatDialog, newUserIds);
                        dialogsManager.sendSystemMessageAddedUser(systemMessagesManager, qbChatDialog, newUserIds);
                        updateDialog(selectedUsers);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        hideProgressDialog();
                        //showErrorSnackbar(R.string.update_dialog_error, e, null);
                    }
                });
            }
        }
    }

    private void updateDialog(final List<QBUser> selectedUsers) {
        ChatHelper.getInstance().updateDialogUsers(qbDialog, selectedUsers, new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                qbDialog = qbChatDialog;
                hideProgressDialog();
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                hideProgressDialog();
                /*showErrorSnackbar(R.string.chat_info_add_people_error, e, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDialog(selectedUsers);
                    }
                });*/
            }
        });
    }

    private class SystemMessagesListener implements QBSystemMessageListener {
        @Override
        public void processMessage(QBChatMessage qbChatMessage) {
            Log.d(TAG, "System Message Received: " + qbChatMessage.getId());
            if (qbChatMessage.getDialogId().equals(qbDialog.getDialogId())) {
                getDialog();
            }
        }

        @Override
        public void processError(QBChatException e, QBChatMessage qbChatMessage) {
            Log.d(TAG, "System Messages Error: " + e.getMessage() + "With MessageID: " + qbChatMessage.getId());
        }
    }
}
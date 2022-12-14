package com.lahoriagency.cikolive.Conference;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.lahoriagency.cikolive.Adapters.UserAdapter2;
import com.lahoriagency.cikolive.NewPackage.ConfChatAct;
import com.lahoriagency.cikolive.NewPackage.ToastUtilsCon;
import com.lahoriagency.cikolive.R;
import com.lahoriagency.cikolive.SelectUsersActivity;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
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

public class ChatInfoAct extends BaseActCon {
    private static final String TAG = ChatInfoAct.class.getSimpleName();
    private static final String EXTRA_DIALOG = "extra_dialog";

    private QBChatDialog qbDialog;
    private UserAdapter2 userAdapter;
    private SystemMessagesListener systemMessagesListener = new SystemMessagesListener();
    private QBSystemMessagesManager systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();

    public static void start(Context context, String dialogID) {
        Intent intent = new Intent(context, ChatInfoAct.class);
        intent.putExtra(EXTRA_DIALOG, dialogID);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chat_info_con);
        String dialogID = getIntent().getStringExtra(EXTRA_DIALOG);
        qbDialog = getQBDialogsHolder().getDialogById(dialogID);
        setTitleSubtitle();

        ListView usersListView = findViewById(R.id.list_chat_info_users);
        List<Integer> userIds = qbDialog.getOccupants();
        List<QBUser> users = getQBUsersHolder().getUsersByIds(userIds);
        userAdapter = new UserAdapter2(this, users);
        usersListView = findViewById(R.id.list_chat_info_users);
        usersListView.setAdapter(userAdapter);
        getDialog();
    }
    private void setTitleSubtitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(qbDialog.getName());

            String subtitle;
            if (qbDialog.getOccupants().size() != 1) {
                subtitle = getString(R.string.chat_info_subtitle_plural, String.valueOf(qbDialog.getOccupants().size()));
            } else {
                subtitle = getString(R.string.chat_info_subtitle_singular, "1");
            }

            getSupportActionBar().setSubtitle(subtitle);
        }
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
        getMenuInflater().inflate(R.menu.menu_act_chat_info, menu);
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
        QBRestChatService.getChatDialogById(dialogID).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                qbDialog = qbChatDialog;
                setTitleSubtitle();
                buildUserList();
            }

            @Override
            public void onError(QBResponseException e) {
                ToastUtilsCon.shortToast(ChatInfoAct.this, e.getMessage());
                finish();
            }
        });
    }

    private void buildUserList() {
        List<Integer> userIds = qbDialog.getOccupants();
        if (getQBUsersHolder().hasAllUsers(userIds)) {
            List<QBUser> users = getQBUsersHolder().getUsersByIds(userIds);
            userAdapter.clearList();
            userAdapter.addUsers(users);
        } else {
            getChatHelper().getUsersFromDialog(qbDialog, new QBEntityCallback<ArrayList<QBUser>>() {
                @Override
                public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                    if (qbUsers != null) {
                        getQBUsersHolder().putUsers(qbUsers);
                        userAdapter.addUsers(qbUsers);
                    }
                }

                @Override
                public void onError(QBResponseException e) {
                    if (e.getMessage() != null) {
                        Log.d(TAG, e.getMessage());
                    }
                }
            });
        }
    }

    private void updateDialog() {
        showProgressDialog(R.string.dlg_loading);
        Log.d(TAG, "Starting Dialog Update");
        QBRestChatService.getChatDialogById(qbDialog.getDialogId()).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog updatedChatDialog, Bundle bundle) {
                Log.d(TAG, "Update Dialog Successful: " + updatedChatDialog.getDialogId());
                qbDialog = updatedChatDialog;
                hideProgressDialog();
                SelectUsersActivity.startForResult(ChatInfoAct.this, ConfChatAct.REQUEST_CODE_SELECT_PEOPLE, updatedChatDialog);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(TAG, "Dialog Loading Error: " + e.getMessage());
                hideProgressDialog();
                showErrorSnackbar(R.string.select_users_get_dialog_error, e, null);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult with resultCode: $resultCode requestCode: $requestCode");
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ConfChatAct.REQUEST_CODE_SELECT_PEOPLE && data != null) {
                showProgressDialog(R.string.chat_info_updating);
                final List<QBUser> selectedUsers = (ArrayList<QBUser>) data.getSerializableExtra(SelectUsersActivity.EXTRA_QB_USERS);
                List<Integer> existingOccupants = qbDialog.getOccupants();
                final List<Integer> newUserIds = new ArrayList<>();

                if (selectedUsers != null) {
                    for (QBUser user : selectedUsers) {
                        if (!existingOccupants.contains(user.getId())) {
                            newUserIds.add(user.getId());
                        }
                    }
                }

                QBRestChatService.getChatDialogById(qbDialog.getDialogId()).performAsync(new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        qbDialog = qbChatDialog;
                        getDialogsManager().sendMessageAddedUsers(qbChatDialog, newUserIds);
                        getDialogsManager().sendSystemMessageAddedUser(systemMessagesManager, qbChatDialog, newUserIds);
                        updateDialog(selectedUsers);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        hideProgressDialog();
                        showErrorSnackbar(R.string.update_dialog_error, e, null);
                    }
                });
            }
        }
    }

    private void updateDialog(final List<QBUser> selectedUsers) {
        QBUser currentUser = getChatHelper().getCurrentUser();
        getChatHelper().updateDialogUsers(currentUser, qbDialog, selectedUsers, new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                qbDialog = qbChatDialog;
                hideProgressDialog();
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                hideProgressDialog();
                showErrorSnackbar(R.string.chat_info_add_people_error, e, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDialog(selectedUsers);
                    }
                });
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
package com.lahoriagency.cikolive.NewPackage;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.lahoriagency.cikolive.Adapters.ChatAdapter33;
import com.lahoriagency.cikolive.AttachmentImageActivity;
import com.lahoriagency.cikolive.BaseActNew;
import com.lahoriagency.cikolive.CallActivity;
import com.lahoriagency.cikolive.Classes.AttachmentPreviewAdapter;
import com.lahoriagency.cikolive.Classes.AttachmentPreviewAdapterView;
import com.lahoriagency.cikolive.Classes.CallService;
import com.lahoriagency.cikolive.Classes.ChatHelper;
import com.lahoriagency.cikolive.Classes.ImagePickHelper;
import com.lahoriagency.cikolive.Classes.QbChatDialogMessageListenerImp;
import com.lahoriagency.cikolive.Classes.QbDialogHolder;
import com.lahoriagency.cikolive.Classes.QbDialogUtils;
import com.lahoriagency.cikolive.Classes.QbUsersDbManager;
import com.lahoriagency.cikolive.Classes.VerboseQbChatConnectionListener;
import com.lahoriagency.cikolive.Classes.WebRtcSessionManager;
import com.lahoriagency.cikolive.Fragments.ProgressDialogFragment;
import com.lahoriagency.cikolive.Interfaces.Consts;
import com.lahoriagency.cikolive.Interfaces.OnImagePickedListener;
import com.lahoriagency.cikolive.Interfaces.PaginationHistoryListener;
import com.lahoriagency.cikolive.ListUsersActivity;
import com.lahoriagency.cikolive.R;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.ui.kit.chatmessage.adapter.listeners.QBChatAttachClickListener;
import com.quickblox.users.model.QBUser;
import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ChatActivity extends BaseActNew implements OnImagePickedListener, View.OnClickListener {
    public static final String EXTRA_DIALOG_ID = "dialogId";
    public static final String userId = "userId";
    private static final String TAG = ChatActivity.class.getSimpleName();
    private static final int REQUEST_CODE_ATTACHMENT = 721;
    private static final int REQUEST_CODE_SELECT_PEOPLE = 752;
    protected List<QBChatMessage> messagesList;
    private ProgressBar progressBar;
    private EditText messageEditText;
    private LinearLayout attachmentPreviewContainerLayout;
    private Snackbar snackbar;
    private ChatAdapterNew chatAdapter;

    private RecyclerView chatMessagesRecyclerView;
    private AttachmentPreviewAdapter attachmentPreviewAdapter;
    private ConnectionListener chatConnectionListener;
    private ImageAttachClickListener imageAttachClickListener;
    private QBChatDialog qbChatDialog;
    private ArrayList<QBChatMessage> unShownMessages;
    private int skipPagination = 0;
    private ChatMessageListener chatMessageListener;
    private boolean checkAdapterInit;
    private TextView tv_title;
    private ImageView iv_firsticon;

    private FloatingActionButton videoButton;
    private Integer qbUserId;

    private boolean isRunForCall;

    private QBUser currentUser;
    private QbUsersDbManager dbManager;

    private WebRtcSessionManager webRtcSessionManager;
    SharedPrefsHelper sharedPrefsHelper;


    public static void start(Context context, boolean isRunForCall) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, isRunForCall);
        context.startActivity(intent);
    }

    public static void startForResult(Activity activity, int code, QBChatDialog dialogId, Integer id) {
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_DIALOG_ID, dialogId);
        intent.putExtra(ChatActivity.userId, id);
        activity.startActivityForResult(intent, code);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setStatusbarColor();
        sharedPrefsHelper = SharedPrefsHelper.getInstance();


        Log.v(TAG, "onCreate ChatActivity on Thread ID = " + Thread.currentThread().getId());

        if((QBChatDialog) getIntent().getSerializableExtra(EXTRA_DIALOG_ID) != null)
        {
            qbChatDialog = (QBChatDialog) getIntent().getSerializableExtra(EXTRA_DIALOG_ID);
            qbUserId = getIntent().getIntExtra(userId, 0);

            Log.v(TAG, "deserialized dialog = " + qbChatDialog);
            qbChatDialog.initForChat(QBChatService.getInstance());

            initViews();
            setUpAppBar();
            initMessagesRecyclerView();

            chatMessageListener = new ChatMessageListener();

            qbChatDialog.addMessageListener(chatMessageListener);

            initChatConnectionListener();

            initChat();

            //OnlineOflineAction("1");
        }



        else  {
            initFields();

            if (isRunForCall && webRtcSessionManager.getCurrentSession() != null) {
                CallActivity.start(ChatActivity.this, true);
            }
        }





    }

    private void initFields() {
        Bundle extras = getIntent().getExtras();
        sharedPrefsHelper = SharedPrefsHelper.getInstance();
        if (extras != null) {
            isRunForCall = extras.getBoolean(Consts.EXTRA_IS_STARTED_FOR_CALL);
        }

        currentUser = sharedPrefsHelper.getQbUser();
        dbManager = QbUsersDbManager.getInstance(getApplicationContext());
        webRtcSessionManager = WebRtcSessionManager.getInstance(getApplicationContext());
    }


    private void setUpAppBar() {

        iv_firsticon.setImageResource(R.drawable.ic_left_arrow);
        iv_firsticon.setVisibility(View.VISIBLE);

        iv_firsticon.setOnClickListener(this);
    }

    @Override
    protected void initUI() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (qbChatDialog != null) {
            outState.putString(EXTRA_DIALOG_ID, qbChatDialog.getDialogId());
        }
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (qbChatDialog == null) {
            qbChatDialog = QbDialogHolder.getInstance().getChatDialogById(savedInstanceState.getString(EXTRA_DIALOG_ID));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if((QBChatDialog) getIntent().getSerializableExtra(EXTRA_DIALOG_ID) != null)
        {
            addChatMessagesAdapterListeners();
            ChatHelper.getInstance().addConnectionListener(chatConnectionListener);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if((QBChatDialog) getIntent().getSerializableExtra(EXTRA_DIALOG_ID) != null) {
            removeChatMessagesAdapterListeners();
            ChatHelper.getInstance().removeConnectionListener(chatConnectionListener);
        }
    }

    @Override
    public void onBackPressed() {
        releaseChat();
        sendDialogId();
        //OnlineOflineAction("0");

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_chat33, menu);

        MenuItem menuItemLeave = menu.findItem(R.id.menu_chat_action_leave);
        MenuItem menuItemAdd = menu.findItem(R.id.menu_chat_action_add);
        MenuItem menuItemDelete = menu.findItem(R.id.menu_chat_action_delete);
        if (qbChatDialog.getType() == QBDialogType.PRIVATE) {
            menuItemLeave.setVisible(false);
            menuItemAdd.setVisible(false);
        } else {
            menuItemDelete.setVisible(false);
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendDialogId() {
        Intent result = new Intent();
        result.putExtra(EXTRA_DIALOG_ID, qbChatDialog.getDialogId());
        setResult(RESULT_OK, result);
    }


    private void leaveGroupChat() {
        ProgressDialogFragment.show(getSupportFragmentManager());
        ChatHelper.getInstance().exitFromDialog(qbChatDialog, new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbDialog, Bundle bundle) {
                ProgressDialogFragment.hide(getSupportFragmentManager());
                QbDialogHolder.getInstance().deleteDialog(qbDialog);
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                ProgressDialogFragment.hide(getSupportFragmentManager());
                showErrorSnackbar(R.string.error_leave_chat, e, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        leaveGroupChat();
                    }
                });
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_PEOPLE) {
                ArrayList<QBUser> selectedUsers = (ArrayList<QBUser>) data.getSerializableExtra(
                        ListUsersActivity.EXTRA_QB_USERS);

                updateDialog(selectedUsers);
            }
        }
    }

    @Override
    public void onImagePicked(int requestCode, File file) {
        switch (requestCode) {
            case REQUEST_CODE_ATTACHMENT:
                attachmentPreviewAdapter.add(file);
                break;
        }
    }

    @Override
    public void onImagePickError(int requestCode, Exception e) {
        showErrorSnackbar(0, e, null);
    }

    @Override
    public void onImagePickClosed(int requestCode) {
        // ignore
    }

    @Override
    protected View getSnackbarAnchorView() {
        return findViewById(R.id.list_chat_messages);
    }

    public void onSendChatClick(View view) {
        int totalAttachmentsCount = attachmentPreviewAdapter.getCount();
        Collection<QBAttachment> uploadedAttachments = attachmentPreviewAdapter.getUploadedAttachments();
        if (!uploadedAttachments.isEmpty()) {
            if (uploadedAttachments.size() == totalAttachmentsCount) {
                for (QBAttachment attachment : uploadedAttachments) {
                    sendChatMessage(null, attachment);
                }
            } else {
                Toaster.shortToast(R.string.chat_wait_for_attachments_to_upload);
            }
        }

        String text = messageEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            sendChatMessage(text, null);
        }
    }

    public void onAttachmentsClick(View view) {
        new ImagePickHelper().pickAnImage(this, REQUEST_CODE_ATTACHMENT);
    }

    public void showMessage(QBChatMessage message) {
        if (isAdapterConnected()) {
            chatAdapter.add(message);
            scrollMessageListDown();
        } else {
            delayShowMessage(message);
        }
    }

    private boolean isAdapterConnected() {
        return checkAdapterInit;
    }

    private void delayShowMessage(QBChatMessage message) {
        if (unShownMessages == null) {
            unShownMessages = new ArrayList<>();
        }
        unShownMessages.add(message);
    }

    private void initViews() {
        //  actionBar.setDisplayHomeAsUpEnabled(true);

        messageEditText = _findViewById(R.id.edit_chat_message);
        iv_firsticon = findViewById(R.id.iv_1sticon);
        tv_title = findViewById(R.id.tv_title);
        progressBar = _findViewById(R.id.progress_chat);

        videoButton = findViewById(R.id.video_floating_button);
        videoButton.setOnClickListener(this);

        attachmentPreviewContainerLayout = _findViewById(R.id.layout_attachment_preview_container);

        attachmentPreviewAdapter = new AttachmentPreviewAdapter(this,
                new AttachmentPreviewAdapter.OnAttachmentCountChangedListener() {
                    @Override
                    public void onAttachmentCountChanged(int count) {
                        attachmentPreviewContainerLayout.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
                    }
                },
                new AttachmentPreviewAdapter.OnAttachmentUploadErrorListener() {
                    @Override
                    public void onAttachmentUploadError(QBResponseException e) {
                        showErrorSnackbar(0, e, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onAttachmentsClick(v);
                            }
                        });
                    }
                });

        AttachmentPreviewAdapterView previewAdapterView = _findViewById(R.id.adapter_view_attachment_preview);
        previewAdapterView.setAdapter(attachmentPreviewAdapter);
    }

    private void initMessagesRecyclerView() {
        chatMessagesRecyclerView = findViewById(R.id.list_chat_messages);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        chatMessagesRecyclerView.setLayoutManager(layoutManager);

        messagesList = new ArrayList<>();
        chatAdapter = new ChatAdapterNew(this, qbChatDialog, messagesList);
        chatAdapter.setPaginationHistoryListener(new PaginationListener());
        chatMessagesRecyclerView.addItemDecoration(
                new StickyRecyclerHeadersDecoration(chatAdapter));

        chatMessagesRecyclerView.setAdapter(chatAdapter);
        imageAttachClickListener = new ImageAttachClickListener();
    }

    private void sendChatMessage(String text, QBAttachment attachment) {
        QBChatMessage chatMessage = new QBChatMessage();
        if (attachment != null) {
            chatMessage.addAttachment(attachment);
        } else {
            chatMessage.setBody(text);
        }
        chatMessage.setSaveToHistory(true);
        chatMessage.setDateSent(System.currentTimeMillis() / 1000);
        chatMessage.setMarkable(true);

        if (!QBDialogType.PRIVATE.equals(qbChatDialog.getType()) && !qbChatDialog.isJoined()) {
            Toaster.shortToast("You're still joining a group chat, please wait a bit");
            return;
        }

        try {
            qbChatDialog.sendMessage(chatMessage);

            if (QBDialogType.PRIVATE.equals(qbChatDialog.getType())) {
                showMessage(chatMessage);
            }

            if (attachment != null) {
                //sendNotification("", "");
                attachmentPreviewAdapter.remove(attachment);
            } else {
                //sendNotification(text, "");
                messageEditText.setText("");
            }
        } catch (SmackException.NotConnectedException e) {
            Log.w(TAG, e);
            Toaster.shortToast("Can't send a message, You are not connected to chat");
        }
    }

    private void initChat() {
        switch (qbChatDialog.getType()) {
            case GROUP:
            case PUBLIC_GROUP:
                joinGroupChat();
                break;

            case PRIVATE:
                loadDialogUsers();
                break;

            default:
                Toaster.shortToast(String.format("%s %s", getString(R.string.chat_unsupported_type), qbChatDialog.getType().name()));
                finish();
                break;
        }
    }

    private void joinGroupChat() {
        progressBar.setVisibility(View.VISIBLE);
        ChatHelper.getInstance().join(qbChatDialog, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle b) {
                if (snackbar != null) {
                    snackbar.dismiss();
                }
                loadDialogUsers();
            }

            @Override
            public void onError(QBResponseException e) {
                progressBar.setVisibility(View.GONE);
                snackbar = showErrorSnackbar(R.string.connection_error, e, null);
            }
        });
    }

    private void leaveGroupDialog() {
        try {
            ChatHelper.getInstance().leaveChatDialog(qbChatDialog);
        } catch (XMPPException | SmackException.NotConnectedException e) {
            Log.w(TAG, e);
        }
    }

    private void releaseChat() {
        qbChatDialog.removeMessageListrener(chatMessageListener);
        if (!QBDialogType.PRIVATE.equals(qbChatDialog.getType())) {
            leaveGroupDialog();
        }
    }

    private void updateDialog(final ArrayList<QBUser> selectedUsers) {
        ChatHelper.getInstance().updateDialogUsers(qbChatDialog, selectedUsers,
                new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog dialog, Bundle args) {
                        qbChatDialog = dialog;
                        loadDialogUsers();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        showErrorSnackbar(R.string.chat_info_add_people_error, e,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        updateDialog(selectedUsers);
                                    }
                                });
                    }
                }
        );
    }

    private void loadDialogUsers() {
        ChatHelper.getInstance().getUsersFromDialog(qbChatDialog, new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> users, Bundle bundle) {
                setChatNameToActionBar();
                loadChatHistory();
            }

            @Override
            public void onError(QBResponseException e) {
                showErrorSnackbar(R.string.chat_load_users_error, e,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadDialogUsers();
                            }
                        });
            }
        });
    }

    private void setChatNameToActionBar() {
        String chatName = QbDialogUtils.getDialogName(qbChatDialog);
        tv_title.setText(chatName);
    }

    private void loadChatHistory() {
        ChatHelper.getInstance().loadChatHistory(qbChatDialog, skipPagination, new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> messages, Bundle args) {
                // The newest messages should be in the end of list,
                // so we need to reverse list to show messages in the right order
                Collections.reverse(messages);
                if (!checkAdapterInit) {
                    checkAdapterInit = true;
                    chatAdapter.addList(messages);
                    addDelayedMessagesToAdapter();
                } else {
                    chatAdapter.addToList(messages);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(QBResponseException e) {
                progressBar.setVisibility(View.GONE);
                skipPagination -= ChatHelper.CHAT_HISTORY_ITEMS_PER_PAGE;
                snackbar = showErrorSnackbar(R.string.connection_error, e, null);
            }
        });
        skipPagination += ChatHelper.CHAT_HISTORY_ITEMS_PER_PAGE;
    }

    private void addDelayedMessagesToAdapter() {
        if (unShownMessages != null && !unShownMessages.isEmpty()) {
            List<QBChatMessage> chatList = chatAdapter.getList();
            for (QBChatMessage message : unShownMessages) {
                if (!chatList.contains(message)) {
                    chatAdapter.add(message);
                }
            }
        }
    }

    private void scrollMessageListDown() {
        chatMessagesRecyclerView.scrollToPosition(messagesList.size() - 1);
    }

    private void deleteChat() {
        ChatHelper.getInstance().deleteDialog(qbChatDialog, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                showErrorSnackbar(R.string.dialogs_deletion_error, e,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteChat();
                            }
                        });
            }
        });
    }

    private void initChatConnectionListener() {
        chatConnectionListener = new VerboseQbChatConnectionListener(getSnackbarAnchorView()) {
            @Override
            public void reconnectionSuccessful() {
                super.reconnectionSuccessful();
                skipPagination = 0;
                switch (qbChatDialog.getType()) {
                    case GROUP:
                        checkAdapterInit = false;
                        // Join active room if we're in Group Chat
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                joinGroupChat();
                            }
                        });
                        break;
                }
            }
        };
    }

    private void addChatMessagesAdapterListeners() {
        chatAdapter.setAttachImageClickListener(imageAttachClickListener);
    }

    private void removeChatMessagesAdapterListeners() {
        chatAdapter.removeAttachImageClickListener(imageAttachClickListener);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_1sticon:
                onBackPressed();
                break;

            case R.id.video_floating_button:
                if (isLoggedInChat()) {
                    startCall(true);
                }
//                if (checker.lacksPermissions(Consts.PERMISSIONS)) {
//                    startPermissionsActivity(false);
//                }
                break;
        }
    }


    private void startCall(boolean isVideoCall) {
//        if (opponentsAdapter.getSelectedItems().size() > Consts.MAX_OPPONENTS_COUNT) {
//            Toaster.longToast(String.format(getString(R.string.error_max_opponents_count),
//                    Consts.MAX_OPPONENTS_COUNT));
//            return;
//        }
//
//        if (opponentsAdapter.getSelectedItems().size() == 0) {
//            Toast.makeText(this, "Please select at least one friend", Toast.LENGTH_SHORT).show();
//            return;
//        }


        Log.d(TAG, "startCall()");
        ArrayList<Integer> opponentsList = new ArrayList<>();


        opponentsList.add(qbUserId);


        QBRTCTypes.QBConferenceType conferenceType = isVideoCall
                ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;

        QBRTCClient qbrtcClient = QBRTCClient.getInstance(getApplicationContext());

        QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);

        WebRtcSessionManager.getInstance(this).setCurrentSession(newQbRtcSession);

        //PushNotificationSender.sendPushMessage(opponentsList, currentUser.getFullName());

        CallActivity.start(this, false);
        Log.d(TAG, "conferenceType = " + conferenceType);
    }

    private class ChatMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
            showMessage(qbChatMessage);
        }
    }

    private class ImageAttachClickListener implements QBChatAttachClickListener {

        @Override
        public void onLinkClicked(QBAttachment qbAttachment, int position) {
            AttachmentImageActivity.start(ChatActivity.this, qbAttachment.getUrl());
        }
    }

    private class PaginationListener implements PaginationHistoryListener {

        @Override
        public void downloadMore() {
            Log.w(TAG, "downloadMore");
            loadChatHistory();
        }
    }

//    private void sendNotification(String text, String attachemnt) {
//
//        if (CommonUtil.isNetworkConnected(ChatActivity.this)) {
//            RetrofitApi.getApi();
//            ApiService apiService = RetrofitApi.retrofit.create(ApiService.class);
//
//            apiService.SendMessage(qbChatDialog.getUserId() + "", qbChatDialog.getRecipientId() + "", text).enqueue(new Callback<Map>() {
//                @Override
//                public void onResponse(Call<Map> call, Response<Map> response) {
//                    if (response.isSuccessful()) {
//
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Map> call, Throwable t) {
//
//                }
//            });
//
//
//        }
//
//
//    }


//    private void OnlineOflineAction(String status) {
//
//        if (CommonUtil.isNetworkConnected(ChatActivity.this)) {
//            RetrofitApi.getApi();
//            ApiService apiService = RetrofitApi.retrofit.create(ApiService.class);
//            apiService.GoOnlineOfline(status, CommonUtil.getUserid(ChatActivity.this)).enqueue(new Callback<Map>() {
//                @Override
//                public void onResponse(Call<Map> call, Response<Map> response) {
//                    if (response.isSuccessful()) {
//
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Map> call, Throwable t) {
//
//                }
//            });
//
//        }
//
//    }

    private void setStatusbarColor() {
        Window window = ChatActivity.this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(ChatActivity.this, R.color.color_blue_accent));
    }


    private boolean isLoggedInChat() {
        if (!QBChatService.getInstance().isLoggedIn()) {
            Toaster.shortToast(R.string.dlg_signal_error);
            tryReLoginToChat();
            return false;
        }
        return true;
    }

    private void tryReLoginToChat() {
        sharedPrefsHelper = SharedPrefsHelper.getInstance();
        if (sharedPrefsHelper.hasQbUser()) {
            QBUser qbUser = sharedPrefsHelper.getQbUser();
            CallServiceNew.start(this, qbUser);
        }

    }
}
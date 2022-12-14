package com.lahoriagency.cikolive;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.lahoriagency.cikolive.Adapters.ChatAdapter;
import com.lahoriagency.cikolive.Adapters.ChatAdapter33;
import com.lahoriagency.cikolive.Adapters.ChatsAdapter;
import com.lahoriagency.cikolive.Classes.AttachmentPreviewAdapter;
import com.lahoriagency.cikolive.Classes.AttachmentPreviewAdapterView;
import com.lahoriagency.cikolive.Classes.ChatHelper;
import com.lahoriagency.cikolive.Classes.ChatsItem;
import com.lahoriagency.cikolive.Classes.CircularProgressIndicator;
import com.lahoriagency.cikolive.Classes.DialogsManager;
import com.lahoriagency.cikolive.Classes.ImagePickHelper;
import com.lahoriagency.cikolive.Classes.LoginService;
import com.lahoriagency.cikolive.Classes.ModelItem;
import com.lahoriagency.cikolive.Classes.PreferencesManager;
import com.lahoriagency.cikolive.Classes.QBResRequestExecutor;
import com.lahoriagency.cikolive.Classes.QbChatDialogMessageListenerImp;
import com.lahoriagency.cikolive.Classes.QbDialogHolder;
import com.lahoriagency.cikolive.Classes.QbDialogUtils;
import com.lahoriagency.cikolive.Classes.QbUsersHolder;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.SharedPrefsHelper;
import com.lahoriagency.cikolive.Classes.StrokedTextView;
import com.lahoriagency.cikolive.Classes.SystemPermissionHelper;
import com.lahoriagency.cikolive.Classes.ToastUtils;
import com.lahoriagency.cikolive.Classes.VerboseQbChatConnectionListener;
import com.lahoriagency.cikolive.Interfaces.AttachClickListener;
import com.lahoriagency.cikolive.Interfaces.MessageLongClickListener;
import com.lahoriagency.cikolive.Interfaces.OnImagePickedListener;
import com.lahoriagency.cikolive.Interfaces.PaginationHistoryListener;
import com.lahoriagency.cikolive.Utils.Const;
import com.lahoriagency.cikolive.Utils.SessionManager;
import com.lahoriagency.cikolive.Conference.OpponentsActivity;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBMessageStatusesManager;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogTypingListener;
import com.quickblox.chat.listeners.QBMessageStatusListener;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.DiscussionHistory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;


@SuppressWarnings("deprecation")
public class ChatAct extends BaseActivity implements OnImagePickedListener , Runnable, View.OnClickListener,QBMessageStatusListener, DialogsManager.ManagingDialogsCallbacks {
    private static boolean isNewDialog;
    private static int code=908;
    List<ChatsItem> chatsList = new ArrayList<>();

    private RecyclerView recyclerView;


    private static final String TAG = ChatAct.class.getSimpleName();
    private static final int REQUEST_CODE_ATTACHMENT = 721;

    private static final String PROPERTY_SAVE_TO_HISTORY = "save_to_history";
    public static final String EXTRA_DIALOG_ID = "dialogId";
    public static final String EXTRA_DELETE_DIALOG = "deleteDialog";
    public static final String EXTRA_DIALOG_DATA = "dialogData";
    public static final int REQUEST_CODE_SELECT_PEOPLE = 752;
    public static final String PROPERTY_FORWARD_USER_NAME = "origin_sender_name";
    public static final String EXTRA_IS_NEW_DIALOG = "isNewDialog";
    public static final String IS_IN_BACKGROUND = "is_in_background";

    public static final String ORDER_RULE = "order";
    public static final String ORDER_VALUE_UPDATED_AT = "desc string updated_at";

    public static final long TYPING_STATUS_DELAY = 2000;
    public static final long TYPING_STATUS_INACTIVITY_DELAY = 10000;
    private static final long SEND_TYPING_STATUS_DELAY = 3000;
    public static final int MAX_ATTACHMENTS_COUNT = 1;
    public static final int MAX_MESSAGE_SYMBOLS_LENGTH = 1000;



    private ProgressBar progressBar;

    private StickyListHeadersListView messagesListView;
    private EditText messageEditText;

    private RelativeLayout emptyChatLayout;
    private TextView emptyChatMatchText;
    private TextView emptyChatTimeText;
    private CircleImageView emptyChatCircleImageView;
    private StrokedTextView emptyChatMatchValueText;
    private CircularProgressIndicator emptyChatIndicator;

    private LinearLayout attachmentPreviewContainerLayout;
    private Snackbar snackbar;

    private ChatAdapter chatAdapter;
    private ChatsAdapter chatsAdapter22;
    private ChatAdapter33 chatAdapter33;
    private AttachmentPreviewAdapter attachmentPreviewAdapter;
    private ConnectionListener chatConnectionListener;

    private QBChatDialog qbChatDialog;
    private ArrayList<QBChatMessage> unShownMessages;
    private ChatMessageListener chatMessageListener;

    private View.OnClickListener openProfileActivityOnClickListener;
    private int skipPagination = 0;

    private ImageView attachmentBtnChat;
    private TextView typingStatus;
    private QBUser currentUser;

    private RecyclerView chatMessagesRecyclerView;

    private RelativeLayout sendMessageContainer;

    private ImageAttachClickListener imageAttachClickListener;
    private VideoAttachClickListener videoAttachClickListener;
    private QBSystemMessagesManager systemMessagesManager;

    private List<QBChatMessage> messagesList;

    private Boolean checkAdapterInit = false;
    private QBResRequestExecutor requestExecutor = new QBResRequestExecutor();

    private FileAttachClickListener fileAttachClickListener;
    private MessageLongClickListenerImpl messageLongClickListener;
    private QBMessageStatusesManager qbMessageStatusesManager;
    private DialogsManager dialogsManager = new DialogsManager();
    private Bundle userExtras;
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

    private static final int REQUEST_CONFERENCE_PERMISSION_CODE = 175;
    private static final int REQUEST_STREAM_PERMISSION_CODE = 176;
    private static final int PERMISSIONS_FOR_SAVE_FILE_IMAGE_REQUEST = 1010;


    public static final String EXTRA_IS_OPEN_FROM_CALL = "isOpenFromCall";

    public static final long CHANGE_CONFERENCE_ROOM_DELAY = 10000;
    public static final int MAX_CONFERENCE_OPPONENTS_ALLOWED = 12;
    ChipNavigationBar chipNavigationBar;
    Runnable runnable;
    Handler handler;
    SessionManager sessionManager;
    AnimationDrawable loadingAnimation;
    private static final int SPLASH_DELAY = 1500;
    private ModelItem modelItem;
    private static final String APPLICATION_ID = QUICKBLOX_APP_ID;   //QUICKBLOX_APP_ID
    private static final String AUTH_KEY = QUICKBLOX_AUTH_KEY;
    private static final String AUTH_SECRET = QUICKBLOX_SECRET_KEY;
    private static final String ACCOUNT_KEY = QUICKBLOX_ACCT_KEY;
    private static final String SERVER_URL = "";

    private SystemMessagesListener systemMessagesListener = new SystemMessagesListener();

    public static void startForResult(AppCompatActivity activity, int code, QBChatDialog dialogId) {
        Intent intent = new Intent(activity, ChatAct.class);
        intent.putExtra(ChatAct.EXTRA_DIALOG_ID, dialogId);
        activity.startActivityForResult(intent, code);
    }
    @Override
    public void onClick(View v) {
        handler.removeCallbacks(this);
        run();
    }

    public static void startForResult(AppCompatActivity activity, int code, QBChatDialog dialogId, boolean isNewDialog) {
        Intent intent = new Intent(activity, ChatAct.class);
        intent.putExtra(ChatAct.EXTRA_DIALOG_ID, dialogId);
        intent.putExtra(ChatAct.EXTRA_IS_NEW_DIALOG, isNewDialog);
        activity.startActivityForResult(intent, code);
    }



    public static void startForResult(Activity activity, int code, QBChatDialog dialogId) {
        Intent intent = new Intent(activity, ChatAct.class);
        intent.putExtra(ChatAct.EXTRA_DIALOG_ID, dialogId);
        activity.startActivityForResult(intent, code);
    }

    public static void startForResult(Activity activity, int code, QBChatDialog dialogId, boolean isNewDialog) {
        Intent intent = new Intent(activity, ChatAct.class);
        intent.putExtra(ChatAct.EXTRA_DIALOG_ID, dialogId);
        intent.putExtra(ChatAct.EXTRA_IS_NEW_DIALOG, isNewDialog);
        activity.startActivityForResult(intent, code);
    }

    public static void startForResultFromCall(CallActivity callActivity, int requestCodeOpenConversationChat, String dialogID, boolean b) {
        Intent intent = new Intent(callActivity, ChatAct.class);
        intent.putExtra(ChatAct.EXTRA_DIALOG_ID, requestCodeOpenConversationChat);
        intent.putExtra(ChatAct.EXTRA_IS_NEW_DIALOG, isNewDialog);
        callActivity.startActivityForResult(intent, code);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chat);
        setTitle("Chats Activity");
        checkInternetConnection();
        FirebaseApp.initializeApp(this);
        QBSettings.getInstance().init(this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        userExtras= new Bundle();
        cloudUser= new QBUser();
        modelItem= new ModelItem();
        savedProfile= new SavedProfile();
        FloatingActionButton diaFab = findViewById(R.id.fab_dialog);
        chipNavigationBar = findViewById(R.id.bottom_nav_barC);
        chipNavigationBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        chipNavigationBar.setOnItemSelectedListener
                (new ChipNavigationBar.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int i) {
                        //Fragment fragment = null;
                        switch (i){
                            case R.id.chatHome:
                                Intent myIntent = new Intent(ChatAct.this, ChatAct.class);
                                overridePendingTransition(R.anim.slide_in_right,
                                        R.anim.slide_out_left);
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(myIntent);

                            case R.id.chat_Timelines:

                                Intent chat = new Intent(ChatAct.this, MyTimeLinesAct.class);
                                overridePendingTransition(R.anim.slide_in_right,
                                        R.anim.slide_out_left);
                                chat.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                chat.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(chat);


                            case R.id.cOpponents:

                                Intent shop = new Intent(ChatAct.this, OpponentsActivity.class);
                                overridePendingTransition(R.anim.slide_in_right,
                                        R.anim.slide_out_left);
                                shop.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                shop.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(shop);


                            case R.id.chat_settings:
                                Intent helpIntent = new Intent(ChatAct.this, SettingsActivity.class);
                                overridePendingTransition(R.anim.slide_in_right,
                                        R.anim.slide_out_left);
                                helpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                helpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(helpIntent);
                        }
                    }
                });


        SharedPrefsHelper.getInstance().delete(IS_IN_BACKGROUND);
        Log.v(TAG, "onCreate ChatActivity on Thread ID = " + Thread.currentThread().getId());
        userPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        gson = new Gson();
        diaFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogsActivity.start(ChatAct.this);
            }
        });
        //fm.beginTransaction().add(R.id.main_content, fbLoginFragment).commit();
        profileID = userPreferences.getInt("PROFILE_ID", 0);
        userName = userPreferences.getString("PROFILE_USERNAME", "");
        password = userPreferences.getString("PROFILE_PASSWORD", "");

        userExtras=getIntent().getExtras();
        if(userExtras !=null){
            cloudUser=userExtras.getParcelable("QBUser");
            savedProfile=userExtras.getParcelable("SavedProfile");
        }else {
            currentUser = SharedPrefsHelper.getInstance().getQbUser();
        }
        if(savedProfile !=null){
            modelItem=savedProfile.getSavedPModelItem();
        }

        if (ChatHelper.getCurrentUser() != null) {
            currentUser = ChatHelper.getCurrentUser();
        } else {
            Log.e(TAG, "Finishing " + TAG + ". Current user is null");
            finish();
        }

        if (!ChatHelper.getInstance().isLogged()) {
            reloginToChat();
        }else {
            restoreChatSession();
        }

        qbChatDialog = (QBChatDialog) getIntent().getSerializableExtra(EXTRA_DIALOG_ID);
        Log.v(TAG, "Deserialized dialog = " + qbChatDialog);

        try {
            qbChatDialog.initForChat(QBChatService.getInstance());
        } catch (IllegalStateException e) {
            Log.v(TAG, "initForChat error. Error message is : " + e.getMessage());
            Log.e(TAG, "Finishing " + TAG + ". Unable to init chat");
            finish();
        }
        qbChatDialog.addMessageListener(chatMessageListener);
        qbChatDialog.addIsTypingListener(new TypingStatusListener());

        initViews();
        initMessagesRecyclerView();
        initChatConnectionListener();
        initChat();
        init();
    }
    private void init() {

        sessionManager = new SessionManager(this);
        runnable = () -> {

            if (Boolean.TRUE.equals(sessionManager.getBooleanValue(Const.IS_LOGGED_IN))) {
                startActivity(new Intent(ChatAct.this, MainActivity.class));


            } else {
                startActivity(new Intent(ChatAct.this, CreateProfileActivity.class));
            }
            finish();
        };

        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnable, 1000);



    }
    @Override
    public void run() {
        loadingAnimation.stop();
        if (sharedPrefsHelper.hasQbUser()) {
            LoginService.start(ChatAct.this, sharedPrefsHelper.getQbUser());
            OpponentsActivity.start(ChatAct.this);
            restoreChatSession();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingAnimation.stop();
                    SignInActivity.start(ChatAct.this);
                    finish();
                }
            }, SPLASH_DELAY);
        }
        this.finish();

    }
    private void restoreChatSession() {
        if (ChatHelper.getInstance().isLogged()) {
            DialogsActivity.start(this);
            finish();
        } else {
            QBUser currentUser = getUserFromSession();
            if (currentUser == null) {
                SignInActivity.start(this);
                finish();
            } else {
                loginToChat(currentUser);
            }
        }
    }
    private QBUser getUserFromSession() {
        QBUser user = SharedPrefsHelper.getInstance().getQbUser();
        QBSessionManager qbSessionManager = QBSessionManager.getInstance();
        if (qbSessionManager.getSessionParameters() == null || user == null) {
            ChatHelper.getInstance().destroy();
            return null;
        }
        Integer userId = qbSessionManager.getSessionParameters().getUserId();
        user.setId(userId);
        return user;
    }

    private void loginToChat(final QBUser user) {
        showProgressDialog(R.string.dlg_restoring_chat_session);

        ChatHelper.getInstance().loginToChat(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                Log.v(TAG, "Chat login onSuccess()");
                hideProgressDialog();
                DialogsActivity.start(ChatAct.this);
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                if (e.getMessage().equals("You have already logged in chat")) {
                    loginToChat(user);
                } else {
                    hideProgressDialog();
                    Log.w(TAG, "Chat login onError(): " + e);
                    showErrorSnackbar(R.string.error_recreate_session, e,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    loginToChat(user);
                                }
                            });
                }
            }
        });
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
            try {
                qbChatDialog = QbDialogHolder.getInstance().getChatDialogById(savedInstanceState.getString(EXTRA_DIALOG_ID));
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }

    @Override
    public void onResumeFinished() {
        if (ChatHelper.getInstance().isLogged()) {
            if (qbChatDialog == null) {
                qbChatDialog = (QBChatDialog) getIntent().getSerializableExtra(EXTRA_DIALOG_ID);
            }
            returnToChat();
        } else {
            reloginToChat();
        }
    }

    public void reloginToChat() {
        showProgressDialog(R.string.dlg_login);
        ChatHelper.getInstance().loginToChat(SharedPrefsHelper.getInstance().getQbUser(), new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                returnToChat();
                hideProgressDialog();
            }

            @Override
            public void onError(QBResponseException e) {
                hideProgressDialog();
               /* showErrorSnackbar(R.string.reconnect_failed, e, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reloginToChat();
                    }
                });*/
            }
        });
    }

    private void returnToChat() {
        qbChatDialog.initForChat(QBChatService.getInstance());
        if (!qbChatDialog.isJoined()) {
            ChatHelper.getInstance().join(qbChatDialog, new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void unused, Bundle bundle) {
                    // Loading unread messages received in background
                    if (SharedPrefsHelper.getInstance().get(IS_IN_BACKGROUND, false)) {
                        progressBar.setVisibility(View.VISIBLE);
                        skipPagination = 0;
                        checkAdapterInit = false;
                        loadChatHistory();
                    }

                    sendMessageContainer.setVisibility(View.VISIBLE);

                    returnListeners();
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.e(TAG, "Join Dialog Exception: " + e.getMessage());
                    Toast.makeText(ChatAct.this, R.string.error_joining_chat, Toast.LENGTH_LONG).show();
                    /*showErrorSnackbar(R.string.error_joining_chat, e, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            returnToChat();
                        }
                    });*/
                    returnToChat();
                }
            });
        }
    }

    private void returnListeners() {
        qbChatDialog.addIsTypingListener(new TypingStatusListener());

        dialogsManager.addManagingDialogsCallbackListener(this);

        try {
            systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();
            systemMessagesManager.addSystemMessageListener(systemMessagesListener);
            qbMessageStatusesManager = QBChatService.getInstance().getMessageStatusesManager();
            qbMessageStatusesManager.addMessageStatusListener(this);
        } catch (Exception e) {
            if (e.getMessage() != null) {
                Log.d(TAG, e.getMessage());
            }

            /*showErrorSnackbar(R.string.error_getting_chat_service, e, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnListeners();
                }
            });*/
        }

        chatAdapter.setAttachImageClickListener(imageAttachClickListener);
        chatAdapter.setAttachVideoClickListener(videoAttachClickListener);
        chatAdapter.setAttachFileClickListener(fileAttachClickListener);
        chatAdapter.setMessageLongClickListener(messageLongClickListener);
        ChatHelper.getInstance().addConnectionListener(chatConnectionListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        chatAdapter.removeClickListeners();
        ChatHelper.getInstance().removeConnectionListener(chatConnectionListener);
        if (qbMessageStatusesManager != null) {
            qbMessageStatusesManager.removeMessageStatusListener(this);
        }
        SharedPrefsHelper.getInstance().save(IS_IN_BACKGROUND, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (systemMessagesManager != null) {
            systemMessagesManager.removeSystemMessageListener(systemMessagesListener);
        }
        qbChatDialog.removeMessageListrener(chatMessageListener);
        dialogsManager.removeManagingDialogsCallbackListener(this);
        SharedPrefsHelper.getInstance().delete(IS_IN_BACKGROUND);
    }

    @Override
    public void onBackPressed() {
        qbChatDialog.removeMessageListrener(chatMessageListener);
        sendDialogId();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_chat, menu);
        MenuItem menuItemInfo = menu.findItem(R.id.menu_chat_action_info);
        MenuItem menuItemLeave = menu.findItem(R.id.menu_chat_action_leave);
        MenuItem menuItemDelete = menu.findItem(R.id.menu_chat_action_delete);
        menuItemDelete.setVisible(true);

        switch (qbChatDialog.getType()) {
            case GROUP:
                menuItemDelete.setVisible(false);
                break;
            case PRIVATE:
                menuItemInfo.setVisible(false);
                menuItemLeave.setVisible(false);
                break;
            case PUBLIC_GROUP:
                menuItemInfo.setVisible(false);
                menuItemLeave.setVisible(false);
                menuItemDelete.setVisible(false);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_chat_action_info:
                updateDialog();
                return true;

            case R.id.menu_chat_action_leave:
                openAlertDialogLeaveGroupChat();
                return true;

            case R.id.menu_chat_action_delete:
                openAlertDialogDeletePrivateChat();
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openAlertDialogLeaveGroupChat() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatAct.this, R.style.AlertDialogStyle);
        alertDialogBuilder.setTitle(getString(R.string.dlg_leave_group_dialog));
        alertDialogBuilder.setMessage(getString(R.string.dlg_leave_group_question));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(getString(R.string.dlg_leave), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                leaveGroupChat();
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.dlg_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialogBuilder.create();
        alertDialogBuilder.show();
    }

    private void openAlertDialogDeletePrivateChat() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatAct.this, R.style.AlertDialogStyle);
        alertDialogBuilder.setTitle(getString(R.string.dlg_delete_private_dialog));
        alertDialogBuilder.setMessage(getString(R.string.dlg_delete_private_question));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(getString(R.string.dlg_delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteChat();
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.dlg_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialogBuilder.create();
        alertDialogBuilder.show();
    }

    private void showPopupMenu(boolean isIncomingMessageClicked, View view, final QBChatMessage chatMessage) {
        PopupMenu popupMenu = new PopupMenu(this, view);

        popupMenu.getMenuInflater().inflate(R.menu.menu_message_longclick, popupMenu.getMenu());
        popupMenu.setGravity(Gravity.RIGHT);

        if (isIncomingMessageClicked || qbChatDialog.getType() != QBDialogType.GROUP) {
            popupMenu.getMenu().removeItem(R.id.menu_message_delivered_to);
            popupMenu.getMenu().removeItem(R.id.menu_message_viewed_by);
            popupMenu.setGravity(Gravity.LEFT);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_message_forward:
                        Log.d(TAG, "Forward Message");
                        startForwardingMessage(chatMessage);
                        break;
                    case R.id.menu_message_delivered_to:
                        Log.d(TAG, "Delivered by");
                        showDeliveredToScreen(chatMessage);
                        break;
                    case R.id.menu_message_viewed_by:
                        Log.d(TAG, "Viewed by");
                        showViewedByScreen(chatMessage);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void showFilePopup(int itemViewType, final QBAttachment attachment, View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_file_popup, popupMenu.getMenu());

        if (itemViewType == ChatAdapter.TYPE_TEXT_RIGHT || itemViewType == ChatAdapter.TYPE_ATTACH_RIGHT) {
            popupMenu.setGravity(Gravity.RIGHT);
        } else if (itemViewType == ChatAdapter.TYPE_TEXT_LEFT || itemViewType == ChatAdapter.TYPE_ATTACH_LEFT) {
            popupMenu.setGravity(Gravity.LEFT);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_file_save) {
                    saveFileToStorage(attachment);
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void saveFileToStorage(QBAttachment attachment) {
        File file = new File(getApplication().getFilesDir(), attachment.getName());
        String url = QBFile.getPrivateUrlForUID(attachment.getId());
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, file.getName());
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.allowScanningByMediaScanner();
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager != null) {
            manager.enqueue(request);
        }
    }

    private void startForwardingMessage(QBChatMessage message) {
        ForwardToActivity.start(this, message);
    }

    private void showDeliveredToScreen(QBChatMessage message) {
        MessageInfoAct.start(this, message, MessageInfoAct.MESSAGE_INFO_DELIVERED_TO);
    }

    private void showViewedByScreen(QBChatMessage message) {
        MessageInfoAct.start(this, message, MessageInfoAct.MESSAGE_INFO_READ_BY);
    }

    private void updateDialog() {
        showProgressDialog(R.string.dlg_loading);
        Log.d(TAG, "Starting Dialog Update");
        ChatHelper.getInstance().getDialogById(qbChatDialog.getDialogId(), new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog updatedChatDialog, Bundle bundle) {
                Log.d(TAG, "Update Dialog Successful: " + updatedChatDialog.getDialogId());
                qbChatDialog = updatedChatDialog;
                hideProgressDialog();
                ChatInfoAct.start(ChatAct.this, qbChatDialog);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(TAG, "Update Dialog Error: " + e.getMessage());
                hideProgressDialog();
                //showErrorSnackbar(R.string.select_users_get_dialog_error, e, null);
            }
        });
    }

    private void sendDialogId() {
        Intent intent = new Intent().putExtra(EXTRA_DIALOG_ID, qbChatDialog.getDialogId());
        setResult(Activity.RESULT_OK, intent);
    }

    private void leaveGroupChat() {
        showProgressDialog(R.string.dlg_loading);
        dialogsManager.sendMessageLeftUser(qbChatDialog);

        if (systemMessagesManager != null) {
            dialogsManager.sendSystemMessageLeftUser(systemMessagesManager, qbChatDialog);
        }

        try {
            // Its a hack to give the Chat Server more time to process the message and deliver them
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Leaving Dialog");
        ChatHelper.getInstance().exitFromDialog(qbChatDialog, new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbDialog, Bundle bundle) {
                Log.d(TAG, "Leaving Dialog Successful: " + qbDialog.getDialogId());
                hideProgressDialog();
                QbDialogHolder.getInstance().deleteDialog(qbDialog);
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(TAG, "Leaving Dialog Error: " + e.getMessage());
                hideProgressDialog();
                /*showErrorSnackbar(R.string.error_leave_chat, e, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        leaveGroupChat();
                    }
                });*/
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult with resultCode: " + resultCode + " requestCode: " + requestCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_PEOPLE && data != null) {
                progressBar.setVisibility(View.VISIBLE);
                final ArrayList<QBUser> selectedUsers = (ArrayList<QBUser>) data.getSerializableExtra(
                        SelectUsersActivity.EXTRA_QB_USERS);
                List<Integer> existingOccupantsIds = qbChatDialog.getOccupants();
                final List<Integer> newUsersIds = new ArrayList<>();

                for (QBUser user : selectedUsers) {
                    if (!existingOccupantsIds.contains(user.getId())) {
                        newUsersIds.add(user.getId());
                    }
                }

                ChatHelper.getInstance().getDialogById(qbChatDialog.getDialogId(), new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        progressBar.setVisibility(View.GONE);
                        dialogsManager.sendMessageAddedUsers(qbChatDialog, newUsersIds);

                        if (systemMessagesManager != null) {
                            dialogsManager.sendSystemMessageAddedUser(systemMessagesManager, qbChatDialog, newUsersIds);
                        }

                        if (qbChatDialog != null) {
                            ChatAct.this.qbChatDialog = qbChatDialog;
                        }
                        updateDialog(selectedUsers);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        progressBar.setVisibility(View.GONE);
                        //showErrorSnackbar(R.string.update_dialog_error, e, null);
                    }
                });
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SystemPermissionHelper.PERMISSIONS_FOR_SAVE_FILE_IMAGE_REQUEST && grantResults.length > 0 && grantResults[0] != -1) {
            openImagePicker();
        }
    }

    @Override
    public void onImagePicked(int requestCode, File file) {
        switch (requestCode) {
            case REQUEST_CODE_ATTACHMENT:
                SystemPermissionHelper permissionsHelper = new SystemPermissionHelper(this);
                if (permissionsHelper.isSaveImagePermissionGranted()) {
                    attachmentPreviewAdapter.add(file);
                } else {
                    permissionsHelper.requestPermissionsForSaveFileImage();
                }
                break;
        }
    }

    @Override
    public void onImagePickError(int requestCode, Exception e) {
        Log.d(TAG, e.getMessage());
        /*showErrorSnackbar(0, e, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });*/
    }

    @Override
    public void onImagePickClosed(int ignored) {

    }

    public void onSendChatClick(View view) {
        try {
            qbChatDialog.sendStopTypingNotification();
        } catch (XMPPException | SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        int totalAttachmentsCount = attachmentPreviewAdapter.getCount();
        Collection<QBAttachment> uploadedAttachments = attachmentPreviewAdapter.getUploadedAttachments();
        if (!uploadedAttachments.isEmpty()) {
            if (uploadedAttachments.size() == totalAttachmentsCount) {
                for (QBAttachment attachment : uploadedAttachments) {
                    sendChatMessage(null, attachment);
                }
            } else {
                ToastUtils.shortToast(R.string.chat_wait_for_attachments_to_upload);
            }
        }

        String text = messageEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            if (text.length() > MAX_MESSAGE_SYMBOLS_LENGTH) {
                text = text.substring(0, MAX_MESSAGE_SYMBOLS_LENGTH);
            }
            sendChatMessage(text, null);
        }
    }

    public void showMessage(QBChatMessage message) {
        if (isAdapterConnected()) {
            chatAdapter.addMessage(message);
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        typingStatus = findViewById(R.id.tv_typing_status);

        messageEditText = findViewById(R.id.et_chat_message);
        messageEditText.addTextChangedListener(new TextInputWatcher());

        progressBar = findViewById(R.id.progress_chat);
        attachmentPreviewContainerLayout = findViewById(R.id.ll_attachment_preview_container);

        sendMessageContainer = findViewById(R.id.rl_chat_send_container);

        attachmentBtnChat = findViewById(R.id.iv_chat_attachment);
        attachmentBtnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (attachmentPreviewAdapter.getCount() >= MAX_ATTACHMENTS_COUNT) {
                    ToastUtils.shortToast(R.string.error_attachment_count);
                } else {
                    openImagePicker();
                    openBottomSheet(v);
                }
            }
        });

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
                        /*showErrorSnackbar(0, e, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openImagePicker();
                            }
                        });*/
                    }
                });


        AttachmentPreviewAdapterView previewAdapterView = findViewById(R.id.adapter_attachment_preview);
        previewAdapterView.setAdapter(attachmentPreviewAdapter);
    }
    private  void openBottomSheet(View view) {
        Context context=view.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        view = inflater.inflate (R.layout.chat_act_bottom_layout, null);
        ImageView closeBottomLayout = view.findViewById(R.id.can_bottom_close);
        final Dialog mBottomSheetDialog = new Dialog (context, R.style.Theme_Design_BottomSheetDialog);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable (true);
        mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
        mBottomSheetDialog.show ();


        View finalView = view;
        closeBottomLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Clicked Close",Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
                finalView.setVisibility(View.GONE);
            }
        });

        AttachmentPreviewAdapterView previewAdapterView = findViewById(R.id.adapter_attachment_preview);
        previewAdapterView.setAdapter(attachmentPreviewAdapter);

    }


    private void openImagePicker() {
        SystemPermissionHelper permissionHelper = new SystemPermissionHelper(this);
        if (permissionHelper.isSaveImagePermissionGranted()) {
            new ImagePickHelper().pickAnImage(this, REQUEST_CODE_ATTACHMENT);
        } else {
            permissionHelper.requestPermissionsForSaveFileImage();
        }
    }

    private void initMessagesRecyclerView() {
        chatMessagesRecyclerView = findViewById(R.id.rv_chat_messages);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        chatMessagesRecyclerView.setLayoutManager(layoutManager);

        messagesList = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, qbChatDialog, messagesList);
        chatAdapter.setPaginationHistoryListener(new PaginationListener());
        //chatMessagesRecyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(chatAdapter));
        //chatMessagesRecyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(chatAdapter));
        chatMessagesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(chatMessagesRecyclerView);
        chatMessagesRecyclerView.setAdapter(chatAdapter);
        imageAttachClickListener = new ImageAttachClickListener();
        videoAttachClickListener = new VideoAttachClickListener();
        fileAttachClickListener = new FileAttachClickListener();
        messageLongClickListener = new MessageLongClickListenerImpl();
    }
    public static boolean isOnline(ConnectivityManager cm) {
        @SuppressLint("MissingPermission") NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
    public boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        @SuppressLint("MissingPermission") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public boolean checkInternetConnection() {
        boolean hasInternetConnection = hasInternetConnection();
        if (!hasInternetConnection) {
            showWarningDialog("Internet connection failed");
        }

        return hasInternetConnection;
    }
    public void showWarningDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.button_ok, null);
        builder.show();
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    private void sendChatMessage(final String text, final QBAttachment attachment) {
        if (ChatHelper.getInstance().isLogged()) {
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
                try {
                    qbChatDialog.join(new DiscussionHistory());
                } catch (XMPPException | SmackException e) {
                    Log.d(TAG, e.getMessage());
                }
                ToastUtils.shortToast(R.string.chat_still_joining);
                return;
            }

            try {
                Log.d(TAG, "Sending Message with ID: " + chatMessage.getId());
                qbChatDialog.sendMessage(chatMessage);

                if (QBDialogType.PRIVATE.equals(qbChatDialog.getType())) {
                    showMessage(chatMessage);
                }

                if (attachment != null) {
                    attachmentPreviewAdapter.remove(attachment);
                } else {
                    messageEditText.setText("");
                }
            } catch (SmackException.NotConnectedException e) {
                Log.w(TAG, e.getMessage());
                ToastUtils.shortToast(R.string.chat_error_send_message);
            }
        } else {
            showProgressDialog(R.string.dlg_login);
            Log.d(TAG, "Relogin to Chat");
            ChatHelper.getInstance().loginToChat(currentUser, new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid, Bundle bundle) {
                    Log.d(TAG, "Relogin Successfull");
                    sendChatMessage(text, attachment);
                    hideProgressDialog();
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.d(TAG, "Relogin Error: " + e.getMessage());
                    hideProgressDialog();
                    ToastUtils.shortToast(R.string.chat_send_message_error);
                }
            });
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
                sendMessageContainer.setVisibility(View.VISIBLE);
                break;

            default:
                ToastUtils.shortToast(String.format("%s %s", getString(R.string.chat_unsupported_type), qbChatDialog.getType().name()));
                Log.e(TAG, "Finishing " + TAG + ". Unsupported chat type");
                finish();
                break;
        }
    }

    private void joinGroupChat() {
        progressBar.setVisibility(View.VISIBLE);
        ChatHelper.getInstance().join(qbChatDialog, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle b) {
                Log.d(TAG, "Joined to Dialog Successful");
                notifyUsersAboutCreatingDialog();
                hideProgressDialog();
                sendMessageContainer.setVisibility(View.VISIBLE);
                loadDialogUsers();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(TAG, "Joining Dialog Error:" + e.getMessage());
            }
        });
    }

    private void notifyUsersAboutCreatingDialog() {
        if (getIntent().getBooleanExtra(EXTRA_IS_NEW_DIALOG, false)) {
            dialogsManager.sendMessageCreatedDialog(qbChatDialog);
            getIntent().removeExtra(EXTRA_IS_NEW_DIALOG);
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
                        hideProgressDialog();
                        /*showErrorSnackbar(R.string.chat_info_add_people_error, e,
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        updateDialog(selectedUsers);
                                    }
                                });*/
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
                progressBar.setVisibility(View.GONE);
               /* showErrorSnackbar(R.string.chat_load_users_error, e,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadDialogUsers();
                            }
                        });*/
            }
        });
    }

    private void setChatNameToActionBar() {
        String chatName = QbDialogUtils.getDialogName(qbChatDialog);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(chatName);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }
    }

    private void loadChatHistory() {
        ChatHelper.getInstance().loadChatHistory(qbChatDialog, skipPagination, new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> messages, Bundle args) {
                // The newest messages should be in the end of list,
                // so we need to reverse list to show messages in the right order
                Collections.reverse(messages);
                if (checkAdapterInit) {
                    chatAdapter.addMessages(messages);
                } else {
                    checkAdapterInit = true;
                    chatAdapter.setMessages(messages);
                    addDelayedMessagesToAdapter();
                }
                if (skipPagination == 0) {
                    scrollMessageListDown();
                }
                skipPagination += ChatHelper.CHAT_HISTORY_ITEMS_PER_PAGE;
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(TAG, "Loading Dialog History Error: " + e.getMessage());
                progressBar.setVisibility(View.GONE);
                //showErrorSnackbar(R.string.connection_error, e, null);
            }
        });
    }

    private void addDelayedMessagesToAdapter() {
        if (unShownMessages != null && !unShownMessages.isEmpty()) {
            List<QBChatMessage> chatList = chatAdapter.getMessages();
            for (QBChatMessage message : unShownMessages) {
                if (!chatList.contains(message)) {
                    chatAdapter.addMessage(message);
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
                Log.d(TAG, "Chat Deleted");
                QbDialogHolder.getInstance().deleteDialog(qbChatDialog);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
               /* showErrorSnackbar(R.string.dialogs_deletion_error, e,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteChat();
                            }
                        });*/
            }
        });
    }

    private void initChatConnectionListener() {
        View rootView = findViewById(R.id.rv_chat_messages);
        chatConnectionListener = new VerboseQbChatConnectionListener(rootView) {
            @Override
            public void reconnectionSuccessful() {
                super.reconnectionSuccessful();
                Log.d(TAG, "Reconnection Successful");
                skipPagination = 0;
                switch (qbChatDialog.getType()) {
                    case PUBLIC_GROUP:
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

            @Override
            public void reconnectionFailed(Exception error) {
                super.reconnectionFailed(error);
                /*showErrorSnackbar(R.string.reconnect_failed, error, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reloginToChat();
                    }
                });*/
            }
        };
    }

    @Override
    public void processMessageDelivered(String messageID, String dialogID, Integer userID) {
        if (qbChatDialog.getDialogId().equals(dialogID) && userID != null) {
            chatAdapter.updateStatusDelivered(messageID, userID);
        }
    }

    @Override
    public void processMessageRead(String messageID, String dialogID, Integer userID) {
        if (qbChatDialog.getDialogId().equals(dialogID) && userID != null) {
            chatAdapter.updateStatusRead(messageID, userID);
        }
    }

    @Override
    public void onDialogCreated(QBChatDialog chatDialog) {

    }

    @Override
    public void onDialogUpdated(String chatDialog) {

    }

    @Override
    public void onNewDialogLoaded(QBChatDialog chatDialog) {

    }

    private class ChatMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
            Log.d(TAG, "Processing Received Message: " + qbChatMessage.getBody());
            showMessage(qbChatMessage);
        }
    }

    private class SystemMessagesListener implements QBSystemMessageListener {
        @Override
        public void processMessage(final QBChatMessage qbChatMessage) {
            Log.d(TAG, "System Message Received: " + qbChatMessage.getId());
            dialogsManager.onSystemMessageReceived(qbChatMessage);
        }

        @Override
        public void processError(QBChatException e, QBChatMessage qbChatMessage) {
            Log.d(TAG, "System Messages Error: " + e.getMessage() + "With MessageID: " + qbChatMessage.getId());
        }
    }

    private class ImageAttachClickListener implements AttachClickListener {

        @Override
        public void onAttachmentClicked(int itemViewType, View view, QBAttachment attachment) {
            if (attachment != null) {
                String url = QBFile.getPrivateUrlForUID(attachment.getId());
                AttachmentImageActivity.start(ChatAct.this, url);
            }
        }
    }

    private class VideoAttachClickListener implements AttachClickListener {

        @Override
        public void onAttachmentClicked(int itemViewType, View view, QBAttachment attachment) {
            if (attachment != null) {
                String url = QBFile.getPrivateUrlForUID(attachment.getId());
                AttachVideoAct.start(ChatAct.this, attachment.getName(), url);
            }
        }
    }

    private class FileAttachClickListener implements AttachClickListener {

        @Override
        public void onAttachmentClicked(int itemViewType, View view, QBAttachment attachment) {
            if (attachment != null) {
                showFilePopup(itemViewType, attachment, view);
            }
        }
    }

    private class MessageLongClickListenerImpl implements MessageLongClickListener {
        @SuppressLint("MissingPermission")
        @Override
        public void onMessageLongClicked(int itemViewType, View view, QBChatMessage qbChatMessage) {
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                vibrator.vibrate(80);
            }
            if (qbChatMessage != null) {
                if (itemViewType == ChatAdapter.TYPE_TEXT_RIGHT || itemViewType == ChatAdapter.TYPE_ATTACH_RIGHT) {
                    Log.d(TAG, "Outgoing message LongClicked");
                    showPopupMenu(false, view, qbChatMessage);
                } else if (itemViewType == ChatAdapter.TYPE_TEXT_LEFT || itemViewType == ChatAdapter.TYPE_ATTACH_LEFT) {
                    Log.d(TAG, "Incoming message LongClicked");
                    showPopupMenu(true, view, qbChatMessage);
                }
            }
        }
    }

    private class PaginationListener implements PaginationHistoryListener {
        @Override
        public void downloadMore() {
            Log.w(TAG, "Download More");
            loadChatHistory();
        }
    }

    private class TypingStatusListener implements QBChatDialogTypingListener {
        private ArrayList<String> currentTypingUserNames = new ArrayList<>();
        private HashMap<Integer, Timer> usersTimerMap = new HashMap<>();

        @Override
        public void processUserIsTyping(final String dialogID, final Integer userID) {
            Integer currentUserID = currentUser.getId();
            if (dialogID != null && dialogID.equals(qbChatDialog.getDialogId()) && userID != null && !userID.equals(currentUserID)) {
                updateTypingInactivityTimer(dialogID, userID);
                QBUser user = QbUsersHolder.getInstance().getUserById(userID);
                if (user != null && user.getFullName() != null) {
                    addUserToTypingList(user);
                } else {
                    Log.d(TAG, "Loading unknown typing user with ID: " + userID);
                    QBUsers.getUser(userID).performAsync(new QBEntityCallback<QBUser>() {
                        @Override
                        public void onSuccess(QBUser qbUser, Bundle bundle) {
                            Log.d(TAG, "User " + qbUser.getId() + " Loaded from Server");
                            QbUsersHolder.getInstance().putUser(qbUser);
                            addUserToTypingList(qbUser);
                        }

                        @Override
                        public void onError(QBResponseException e) {
                            Log.d(TAG, "Loading User Error: " + e.getMessage());
                        }
                    });
                }
            }
        }

        private void addUserToTypingList(QBUser user) {
            String userName = TextUtils.isEmpty(user.getFullName()) ? user.getLogin() : user.getFullName();
            if (!TextUtils.isEmpty(userName) && !currentTypingUserNames.contains(userName) && usersTimerMap.containsKey(user.getId())) {
                currentTypingUserNames.add(userName);
            }
            typingStatus.setText(makeStringFromNames());
            typingStatus.setVisibility(View.VISIBLE);
        }

        @Override
        public void processUserStopTyping(String dialogID, Integer userID) {
            Integer currentUserID = currentUser.getId();
            if (dialogID != null && dialogID.equals(qbChatDialog.getDialogId()) && userID != null && !userID.equals(currentUserID)) {
                stopInactivityTimer(userID);
                QBUser user = QbUsersHolder.getInstance().getUserById(userID);
                if (user != null && user.getFullName() != null) {
                    removeUserFromTypingList(user);
                }
            }
        }

        private void removeUserFromTypingList(QBUser user) {
            String userName = user.getFullName();
            if (!TextUtils.isEmpty(userName)) {
                currentTypingUserNames.remove(userName);
            }
            typingStatus.setText(makeStringFromNames());
            if (TextUtils.isEmpty(makeStringFromNames())) {
                typingStatus.setVisibility(View.GONE);
            }
        }

        private void updateTypingInactivityTimer(final String dialogID, final Integer userID) {
            stopInactivityTimer(userID);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Log.d("Typing Status", "User with ID " + userID + " Did not refresh typing status. Processing stop typing");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            processUserStopTyping(dialogID, userID);
                        }
                    });
                }
            }, TYPING_STATUS_INACTIVITY_DELAY);
            usersTimerMap.put(userID, timer);
        }

        private void stopInactivityTimer(Integer userID) {
            if (usersTimerMap.get(userID) != null) {
                try {
                    usersTimerMap.get(userID).cancel();
                } catch (NullPointerException ignored) {

                } finally {
                    usersTimerMap.remove(userID);
                }
            }
        }

        private String makeStringFromNames() {
            String result = "";
            int usersCount = currentTypingUserNames.size();
            if (usersCount == 1) {
                String firstUser = currentTypingUserNames.get(0);

                if (firstUser.length() <= 20) {
                    result = firstUser + " " + getString(R.string.typing_postfix_singular);
                } else {
                    result = firstUser.subSequence(0, 19).toString() +
                            getString(R.string.typing_ellipsis) +
                            " " + getString(R.string.typing_postfix_singular);
                }

            } else if (usersCount == 2) {
                String firstUser = currentTypingUserNames.get(0);
                String secondUser = currentTypingUserNames.get(1);

                if ((firstUser + secondUser).length() > 20) {
                    if (firstUser.length() >= 10) {
                        firstUser = firstUser.subSequence(0, 9).toString() + getString(R.string.typing_ellipsis);
                    }

                    if (secondUser.length() >= 10) {
                        secondUser = secondUser.subSequence(0, 9).toString() + getString(R.string.typing_ellipsis);
                    }
                }
                result = firstUser + " and " + secondUser + " " + getString(R.string.typing_postfix_plural);

            } else if (usersCount > 2) {
                String firstUser = currentTypingUserNames.get(0);
                String secondUser = currentTypingUserNames.get(1);
                String thirdUser = currentTypingUserNames.get(2);

                if ((firstUser + secondUser + thirdUser).length() <= 20) {
                    result = firstUser + ", " + secondUser + " and " + thirdUser + " " + getString(R.string.typing_postfix_plural);
                } else if ((firstUser + secondUser).length() <= 20) {
                    result = firstUser + ", " + secondUser + " and " + (currentTypingUserNames.size() - 2) + " more " + getString(R.string.typing_postfix_plural);
                } else {
                    if (firstUser.length() >= 10) {
                        firstUser = firstUser.subSequence(0, 9).toString() + getString(R.string.typing_ellipsis);
                    }
                    if (secondUser.length() >= 10) {
                        secondUser = secondUser.subSequence(0, 9).toString() + getString(R.string.typing_ellipsis);
                    }
                    result = firstUser + ", " + secondUser +
                            " and " + (currentTypingUserNames.size() - 2) + " more " + getString(R.string.typing_postfix_plural);
                }
            }
            return result;
        }
    }

    private class TextInputWatcher implements TextWatcher {
        private Timer timer = new Timer();
        private long lastSendTime = 0;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (SystemClock.uptimeMillis() - lastSendTime > SEND_TYPING_STATUS_DELAY) {
                lastSendTime = SystemClock.uptimeMillis();
                try {
                    qbChatDialog.sendIsTypingNotification();
                } catch (XMPPException | SmackException.NotConnectedException e) {
                    Log.d(TAG, e.getMessage());
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        qbChatDialog.sendStopTypingNotification();
                    } catch (XMPPException | SmackException.NotConnectedException e) {
                        Log.d(TAG, e.getMessage());
                    }
                }
            }, TYPING_STATUS_DELAY);
        }
    }


}
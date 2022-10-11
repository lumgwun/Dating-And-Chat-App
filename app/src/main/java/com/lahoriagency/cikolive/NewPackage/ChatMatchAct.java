package com.lahoriagency.cikolive.NewPackage;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.lahoriagency.cikolive.Adapters.OpponentsFromCallAdapter;
import com.lahoriagency.cikolive.Adapters.SavedProfileAdapter;
import com.lahoriagency.cikolive.Adapters.UserSwipeProfileAdapter;
import com.lahoriagency.cikolive.AppSupportAct;
import com.lahoriagency.cikolive.AttachmentImageActivity;
import com.lahoriagency.cikolive.BaseActNew;
import com.lahoriagency.cikolive.CallActivity;
import com.lahoriagency.cikolive.Classes.AttachmentPreviewAdapter;
import com.lahoriagency.cikolive.Classes.AttachmentPreviewAdapterView;
import com.lahoriagency.cikolive.Classes.ChatHelper;
import com.lahoriagency.cikolive.Classes.Diamond;
import com.lahoriagency.cikolive.Classes.ImagePickHelper;
import com.lahoriagency.cikolive.Classes.PushUtils;
import com.lahoriagency.cikolive.Classes.QbChatDialogMessageListenerImp;
import com.lahoriagency.cikolive.Classes.QbDialogHolder;
import com.lahoriagency.cikolive.Classes.QbDialogUtils;
import com.lahoriagency.cikolive.Classes.QbUsersDbManager;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.SpringInterpolator;
import com.lahoriagency.cikolive.Classes.StrokedTextView;
import com.lahoriagency.cikolive.Classes.UserProfileInfo;
import com.lahoriagency.cikolive.Classes.UserProfileInfoHolder;
import com.lahoriagency.cikolive.Classes.VerboseQbChatConnectionListener;
import com.lahoriagency.cikolive.Classes.WebRtcSessionManager;
import com.lahoriagency.cikolive.Conference.AppConference;
import com.lahoriagency.cikolive.Conference.OpponentsActivity;
import com.lahoriagency.cikolive.DataBase.SavedProfileDAO;
import com.lahoriagency.cikolive.DataBase.UserProfileInfoDAO;
import com.lahoriagency.cikolive.Fragments.ProgressDialogFragment;
import com.lahoriagency.cikolive.Interfaces.Consts;
import com.lahoriagency.cikolive.Interfaces.OnImagePickedListener;
import com.lahoriagency.cikolive.Interfaces.PaginationHistoryListener;
import com.lahoriagency.cikolive.ListUsersActivity;
import com.lahoriagency.cikolive.MainActivity;
import com.lahoriagency.cikolive.ProfileActivity;
import com.lahoriagency.cikolive.R;
import com.lahoriagency.cikolive.SettingsActivity;
import com.melnykov.fab.FloatingActionButton;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.listeners.QBMessageStatusListener;
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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static com.lahoriagency.cikolive.Adapters.UserSwipeProfileAdapter.EXTRA_SWIPE_VIEW_SOURCE;
import static com.lahoriagency.cikolive.Adapters.UserSwipeProfileAdapter.EXTRA_USER_PROFILE;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;

@SuppressWarnings("deprecation")
public class ChatMatchAct extends BaseActNew implements OnImagePickedListener, QBMessageStatusListener, View.OnClickListener,SavedProfileAdapter.OnItemsClickListener {
    public static final String EXTRA_DIALOG_ID = "dialogId";
    public static final String userId = "userId";
    private static final String TAG = ChatMatchAct.class.getSimpleName();
    private static final int REQUEST_CODE_ATTACHMENT = 721;
    private static final int REQUEST_CODE_SELECT_PEOPLE = 752;

    private static final String PROPERTY_SAVE_TO_HISTORY = "save_to_history";
    public static final String EXTRA_DELETE_DIALOG = "deleteDialog";
    public static final String EXTRA_DIALOG_DATA = "dialogData";
    protected List<QBChatMessage> messagesList;
    private ProgressBar progressBar;
    private EditText messageEditText;
    private LinearLayout attachmentPreviewContainerLayout;
    private Snackbar snackbar;
    private ChatAdapterNew chatAdapter;
    private static final String APPLICATION_ID = QUICKBLOX_APP_ID;   //QUICKBLOX_APP_ID
    private static final String AUTH_KEY = QUICKBLOX_AUTH_KEY;
    private static final String AUTH_SECRET = QUICKBLOX_SECRET_KEY;
    private static final String ACCOUNT_KEY = QUICKBLOX_ACCT_KEY;
    private static final String SERVER_URL = "";

    private RecyclerView chatMessagesRecyclerView;
    private StickyListHeadersListView messagesListView;
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
    int diamondCount,diamondID;
    private int collections,qbUserID;

    private WebRtcSessionManager webRtcSessionManager;
    SharedPrefsHelper sharedPrefsHelper;
    private static final String PREF_NAME = "Ciko";
    private ChipNavigationBar chipNavigationBar;
    FloatingActionButton fabNew;
    private SavedProfile savedProfile;
    private  Diamond diamond;
    Gson gson, gson1,gson2;
    String json, json1, json2;
    private QBUser qbUser;
    SharedPreferences sharedPref;
    private double liveAmt;
    private int liveDuration;
    private Bundle extras;
    FloatingActionButton fabVideo;
    private View.OnClickListener openProfileActivityOnClickListener;
    private RelativeLayout emptyChatLayout;
    private TextView emptyChatMatchText;
    private TextView emptyChatTimeText;
    private CircleImageView emptyChatCircleImageView;
    private StrokedTextView emptyChatMatchValueText;
    private CircularProgressIndicator emptyChatIndicator;
    private Date date;
    long diff,minutes;
    private OpponentsFromCallAdapter fromCallAdapter;
    private String chatName;
    private SwipyRefreshLayout refreshLayout;
    private RecyclerView chatersRecylerView;
    private UserSwipeProfileAdapter userSwipeProfileAdapter;
    private ArrayList<UserProfileInfo> userArrayList;
    private List<SavedProfile> profileArrayList;
    private List<SavedProfile> profileAgeArrayList;
    private UserProfileInfoDAO userProfileInfoDAO;
    private SavedProfileDAO savedProfileDAO;
    private  SavedProfile chatersSavedProf;
    private String gender;
    private SavedProfileAdapter savedProfAdapter;
    private UserProfileInfo userProfileInfo;

    public static void start(Context context, boolean isRunForCall) {
        Intent intent = new Intent(context, ChatMatchAct.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(Consts.EXTRA_IS_STARTED_FOR_CALL, isRunForCall);
        context.startActivity(intent);
    }
    public static void startForResult(Activity activity, int code, QBChatDialog dialogId) {
        Intent intent = new Intent(activity, ChatMatchAct.class);
        intent.putExtra(ChatMatchAct.EXTRA_DIALOG_ID, dialogId);
        activity.startActivityForResult(intent, code);
    }


    public static void startForResult(Activity activity, int code, QBChatDialog dialogId, Integer id) {
        Intent intent = new Intent(activity, ChatMatchAct.class);
        intent.putExtra(ChatMatchAct.EXTRA_DIALOG_ID, dialogId);
        intent.putExtra(ChatMatchAct.userId, id);
        activity.startActivityForResult(intent, code);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_match_chat);
        setTitle("Live Chat");
        setStatusbarColor();
        extras= new Bundle();
        date= new Date();
        userProfileInfo= new UserProfileInfo();
        userArrayList= new ArrayList<>();
        profileArrayList= new ArrayList<>();
        savedProfileDAO= new SavedProfileDAO(this);
        userProfileInfoDAO= new UserProfileInfoDAO(this);
        QBSettings.getInstance().init(this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        sharedPrefsHelper = SharedPrefsHelper.getInstance();
        chipNavigationBar = findViewById(R.id.bottom_chatA);
        com.google.android.material.floatingactionbutton.FloatingActionButton floatingActionButton = findViewById(R.id.fabChatGoHome);

        Log.v(TAG, "onCreate ChatActivity on Thread ID = " + Thread.currentThread().getId());
        extras = getIntent().getExtras();
        savedProfile= new SavedProfile();
        gson= new Gson();
        currentUser= new QBUser();
        gson1= new Gson();
        gson2= new Gson();
        qbUser= new QBUser();
        diamond= new Diamond();
        sharedPref= getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = sharedPref.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = sharedPref.getString("LastQBUserUsed", "");
        qbUser = gson1.fromJson(json1, QBUser.class);
        sharedPrefsHelper = SharedPrefsHelper.getInstance();
        messageEditText = _findViewById(R.id.edit_chat_message);
        iv_firsticon = findViewById(R.id.iv_1sticon);
        tv_title = findViewById(R.id.tv_title);
        progressBar = _findViewById(R.id.progress_chat);
        emptyChatLayout = _findViewById(R.id.empty_chat_layout);
        emptyChatMatchText = _findViewById(R.id.empty_chat_layout_match_text);
        emptyChatTimeText = _findViewById(R.id.empty_chat_layout_match_time);
        emptyChatCircleImageView = _findViewById(R.id.empty_chat_layout_circle_image_view);
        emptyChatMatchValueText = _findViewById(R.id.empty_chat_layout_match_value);
        emptyChatIndicator = _findViewById(R.id.empty_chat_layout_indicator);
        refreshLayout = findViewById(R.id.match_refresh_l);
        chatersRecylerView = findViewById(R.id.list_chaters);

        if(savedProfile !=null){
            diamond=savedProfile.getSavedPDiamond();

        }
        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                loadUsersFromQb();
            }
        });

        refreshLayout.setColorSchemeResources(R.color.color_new_blue, R.color.random_color_2, R.color.random_color_3, R.color.random_color_7);


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



        if(qbUser !=null){
            qbUserID=qbUser.getFileId();
        }
        if(diamond !=null){
            diamondCount=diamond.getDiamondCount();
            diamondID=diamond.getDiamondWalletID();
            collections=diamond.getDiamondCollections();

        }
        if (extras != null) {
            isRunForCall = extras.getBoolean(Consts.EXTRA_IS_STARTED_FOR_CALL);
            liveAmt=extras.getDouble("LiveAmount");
            liveDuration=extras.getInt("LiveDuration");
        }
        currentUser = qbUser;
        dbManager = QbUsersDbManager.getInstance(getApplicationContext());
        webRtcSessionManager = WebRtcSessionManager.getInstance(getApplicationContext());


        if((QBChatDialog) getIntent().getSerializableExtra(EXTRA_DIALOG_ID) != null) {
            qbChatDialog = (QBChatDialog) getIntent().getSerializableExtra(EXTRA_DIALOG_ID);
            qbUserId = getIntent().getIntExtra(userId, 0);
            Log.v(TAG, "deserialized dialog = " + qbChatDialog);
            qbChatDialog.initForChat(QBChatService.getInstance());
            QBChatDialog qbChatDialog= new QBChatDialog();
            initViews();
            setUpAppBar();
            initMessagesRecyclerView();
            date=qbChatDialog.getCreatedAt();
            if(date !=null){
                diff = System.currentTimeMillis() - date.getTime();

            }
            if(diff>0){
                minutes = Math.round(diff / (60 * 1000));

            }
            if(minutes==liveDuration){

            }

            chatMessageListener = new ChatMessageListener();

            qbChatDialog.addMessageListener(chatMessageListener);

            initChatConnectionListener();

            initChat();

            //OnlineOflineAction("1");
        }



        else  {
            initFields();

            if (isRunForCall && webRtcSessionManager.getCurrentSession() != null) {
                CallActivity.start(ChatMatchAct.this, true);
            }
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ChatMatchAct.this, MainActivity.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(myIntent);

            }
        });
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
                            case R.id.chatA_Home:
                                Intent myIntent = new Intent(ChatMatchAct.this, MainActivity.class);
                                overridePendingTransition(R.anim.slide_in_right,
                                        R.anim.slide_out_left);
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(myIntent);

                            case R.id.opponent_Chat:

                                Intent chat = new Intent(ChatMatchAct.this, OpponentsActivity.class);
                                overridePendingTransition(R.anim.slide_in_right,
                                        R.anim.slide_out_left);
                                chat.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                chat.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(chat);


                            case R.id.chatSet:

                                Intent shop = new Intent(ChatMatchAct.this, SettingsActivity.class);
                                overridePendingTransition(R.anim.slide_in_right,
                                        R.anim.slide_out_left);
                                shop.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                shop.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(shop);

                            case R.id.chatSupport:

                                Intent pIntent = new Intent(ChatMatchAct.this, AppSupportAct.class);
                                overridePendingTransition(R.anim.slide_in_right,
                                        R.anim.slide_out_left);
                                pIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                pIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(pIntent);

                        }
                        /*getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container,
                                        fragment).commit();*/
                    }
                });






    }
    public void loadUsersFromQb() {
        chatersRecylerView = findViewById(R.id.list_chaters);
        userArrayList= new ArrayList<>();
        profileAgeArrayList= new ArrayList<>();
        savedProfile= new SavedProfile();
        gson= new Gson();
        currentUser= new QBUser();
        gson1= new Gson();
        gson2= new Gson();
        qbUser= new QBUser();
        diamond= new Diamond();
        sharedPref= getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = sharedPref.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        userProfileInfoDAO= new UserProfileInfoDAO(this);
        if(savedProfile !=null){
            gender=savedProfile.getSavedPLookingFor();
        }
        savedProfileDAO= new SavedProfileDAO(this);
        profileArrayList=savedProfileDAO.getAllSavedProfsForGender(gender);
        chatersRecylerView.setLayoutManager(new LinearLayoutManager(ChatMatchAct.this, LinearLayoutManager.HORIZONTAL, false));
        Collections.shuffle(profileArrayList, new Random(System.currentTimeMillis()));
        savedProfAdapter = new SavedProfileAdapter(ChatMatchAct.this, profileArrayList);
        chatersRecylerView.setItemAnimator(new DefaultItemAnimator());
        chatersRecylerView.setNestedScrollingEnabled(false);
        chatersRecylerView.setClickable(true);
        chatersRecylerView.setAdapter(savedProfAdapter);
    }
    public void showMessage(QBChatMessage message) {
        messageEditText = _findViewById(R.id.edit_chat_message);
        iv_firsticon = findViewById(R.id.iv_1sticon);
        tv_title = findViewById(R.id.tv_title);
        progressBar = _findViewById(R.id.progress_chat);
        emptyChatLayout = _findViewById(R.id.empty_chat_layout);
        emptyChatMatchText = _findViewById(R.id.empty_chat_layout_match_text);
        emptyChatTimeText = _findViewById(R.id.empty_chat_layout_match_time);
        emptyChatCircleImageView = _findViewById(R.id.empty_chat_layout_circle_image_view);
        emptyChatMatchValueText = _findViewById(R.id.empty_chat_layout_match_value);
        emptyChatIndicator = _findViewById(R.id.empty_chat_layout_indicator);

        if (emptyChatLayout.getVisibility() == View.VISIBLE)
            emptyChatLayout.setVisibility(View.GONE);
        if (chatAdapter != null) {
            chatAdapter.add(message);
            scrollMessageListDown();
        } else {
            if (unShownMessages == null) {
                unShownMessages = new ArrayList<>();
            }
            unShownMessages.add(message);
        }
        if (isAdapterConnected()) {
            chatAdapter.add(message);
            scrollMessageListDown();
        } else {
            delayShowMessage(message);
        }
    }

    private void initFields() {
        messageEditText = _findViewById(R.id.edit_chat_message);
        iv_firsticon = findViewById(R.id.iv_1sticon);
        tv_title = findViewById(R.id.tv_title);
        progressBar = _findViewById(R.id.progress_chat);
        emptyChatLayout = _findViewById(R.id.empty_chat_layout);
        emptyChatMatchText = _findViewById(R.id.empty_chat_layout_match_text);
        emptyChatTimeText = _findViewById(R.id.empty_chat_layout_match_time);
        emptyChatCircleImageView = _findViewById(R.id.empty_chat_layout_circle_image_view);
        emptyChatMatchValueText = _findViewById(R.id.empty_chat_layout_match_value);
        emptyChatIndicator = _findViewById(R.id.empty_chat_layout_indicator);

        extras = getIntent().getExtras();
        savedProfile= new SavedProfile();
        gson= new Gson();
        gson1= new Gson();
        gson2= new Gson();
        qbUser= new QBUser();
        diamond= new Diamond();
        sharedPref= getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = sharedPref.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = sharedPref.getString("LastQBUserUsed", "");
        qbUser = gson1.fromJson(json1, QBUser.class);
        sharedPrefsHelper = SharedPrefsHelper.getInstance();
        if(savedProfile !=null){
            diamond=savedProfile.getSavedPDiamond();

        }
        if(qbUser !=null){
            qbUserID=qbUser.getFileId();
        }
        if(diamond !=null){
            diamondCount=diamond.getDiamondCount();
            diamondID=diamond.getDiamondWalletID();
            collections=diamond.getDiamondCollections();

        }
        if (extras != null) {
            isRunForCall = extras.getBoolean(Consts.EXTRA_IS_STARTED_FOR_CALL);
            liveAmt=extras.getDouble("LiveAmount");
            liveDuration=extras.getInt("LiveDuration");
        }
        currentUser = qbUser;
        dbManager = QbUsersDbManager.getInstance(this);
        webRtcSessionManager = WebRtcSessionManager.getInstance(this);
    }


    private void setUpAppBar() {
        iv_firsticon = findViewById(R.id.iv_1sticon);
        iv_firsticon.setImageResource(R.drawable.ic_left_arrow);
        iv_firsticon.setVisibility(View.VISIBLE);

        iv_firsticon.setOnClickListener(this);
    }
    private void initViews() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //messagesListView = _findViewById(R.id.list_chat_messages);

        messageEditText = _findViewById(R.id.edit_chat_message);
        iv_firsticon = findViewById(R.id.iv_1sticon);
        tv_title = findViewById(R.id.tv_title);
        progressBar = _findViewById(R.id.progress_chat);
        emptyChatLayout = _findViewById(R.id.empty_chat_layout);
        emptyChatMatchText = _findViewById(R.id.empty_chat_layout_match_text);
        emptyChatTimeText = _findViewById(R.id.empty_chat_layout_match_time);
        emptyChatCircleImageView = _findViewById(R.id.empty_chat_layout_circle_image_view);
        emptyChatMatchValueText = _findViewById(R.id.empty_chat_layout_match_value);
        emptyChatIndicator = _findViewById(R.id.empty_chat_layout_indicator);

        fabVideo = findViewById(R.id.video_floating_button);
        fabVideo.setOnClickListener(this);

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

        openProfileActivityOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatMatchAct.this, ProfileActivity.class);
                UserProfileInfo userProfileInfo = UserProfileInfoHolder.getInstance().getProfileInfo(qbChatDialog.getRecipientId());
                userProfileInfo.setMatchValue(qbChatDialog.getCustomData().getInteger("matchValue"));
                intent.putExtra(EXTRA_USER_PROFILE, userProfileInfo);
                intent.putExtra(EXTRA_SWIPE_VIEW_SOURCE, false);
                startActivity(intent);
            }
        };
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
        if(qbChatDialog !=null){
            if (qbChatDialog.getType() == QBDialogType.PRIVATE) {
                menuItemLeave.setVisible(false);
                menuItemAdd.setVisible(false);
            } else {
                menuItemDelete.setVisible(false);
            }

        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_chat_action_info:

                return true;
            case R.id.menu_chat_action_add:
                // do something
                return true;
            case R.id.menu_chat_action_leave:
                leaveGroupChat();
                return true;
            case R.id.menu_chat_payment:
                // do something
                return true;
            case R.id.menu_chat_action_delete:
                deleteChat();
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    private void sendDialogId() {
        if(qbChatDialog !=null){
            Intent result = new Intent();
            result.putExtra(EXTRA_DIALOG_ID, qbChatDialog.getDialogId());
            setResult(RESULT_OK, result);

        }

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


    private boolean isAdapterConnected() {
        return checkAdapterInit;
    }

    private void delayShowMessage(QBChatMessage message) {
        if (unShownMessages == null) {
            unShownMessages = new ArrayList<>();
        }
        unShownMessages.add(message);
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
    private void setChatNameToActionBar() {
        final ActionBar abar = getSupportActionBar();
        if(qbChatDialog !=null){
            chatName = QbDialogUtils.getDialogName(qbChatDialog);

        }else {
            showEmptyChatView();
        }

        String chatName = QbDialogUtils.getDialogName(qbChatDialog);
        tv_title.setText(chatName);
        View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar_chat, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        viewActionBar.findViewById(R.id.actionbar_layout).setOnClickListener(openProfileActivityOnClickListener);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText(QbDialogUtils.getDialogName(qbChatDialog));
        CircleImageView actionBarImageView = viewActionBar.findViewById(R.id.actionbar_imageview);

        String photoLink = null;
        UserProfileInfo userProfileInfo = UserProfileInfoHolder.getInstance().getProfileInfo(qbChatDialog.getRecipientId());
        if (userProfileInfo != null)
            photoLink = UserProfileInfoHolder.getInstance().getProfileInfo(qbChatDialog.getRecipientId()).getPhotoLinks().get(0);
        else {
            photoLink = QbDialogUtils.getQBUserPhotos(qbChatDialog).get(0);
        }
        AppConference.getImageLoader().downloadImage(photoLink, actionBarImageView);
        if (abar != null) {
            abar.setCustomView(viewActionBar, params);
        }
        if (abar != null) {
            abar.setDisplayShowCustomEnabled(true);
        }
        if (abar != null) {
            abar.setDisplayShowTitleEnabled(false);
        }
        if (abar != null) {
            abar.setDisplayHomeAsUpEnabled(true);
        }
        if (abar != null) {
            abar.setIcon(R.color.transparent);
        }
        if (abar != null) {
            abar.setHomeButtonEnabled(true);
        }
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(getResources().getColor(R.color.primary_purple), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    private void showEmptyChatView() {
        String photoLink = UserProfileInfoHolder.getInstance().getProfileInfo(qbChatDialog.getRecipientId()).getPhotoLinks().get(0);
        emptyChatMatchText.setText(String.format("%s %s", getString(R.string.chat_matched_with_message), QbDialogUtils.getDialogName(qbChatDialog)));
        emptyChatTimeText.setText(TimeUtils.getTimeSpan(qbChatDialog.getCreatedAt()));
        AppConference.getImageLoader().downloadImage(photoLink, emptyChatCircleImageView);
        emptyChatCircleImageView.setOnClickListener(openProfileActivityOnClickListener);
        emptyChatIndicator.setProgress(qbChatDialog.getCustomData().getInteger("matchValue"), true);
        emptyChatMatchValueText.setText(MessageFormat.format("{0}%", qbChatDialog.getCustomData().getInteger("matchValue").toString()));

        Animation emptyChatAnim = AnimationUtils.loadAnimation(ChatMatchAct.this, R.anim.empty_chat_elements);
        emptyChatAnim.setInterpolator(new SpringInterpolator(0.4f));
        emptyChatLayout.setVisibility(View.VISIBLE);
        emptyChatLayout.startAnimation(emptyChatAnim);
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
            PushUtils.sendPushAboutMessage(qbChatDialog, text);
            showMessage(chatMessage);

            if (attachment != null) {
                attachmentPreviewAdapter.remove(attachment);
            } else {
                messageEditText.setText("");
            }
        } catch (SmackException.NotConnectedException e) {
            Log.w(TAG, e);
            Toaster.shortToast("Can't send a message, You are not connected to chat");
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
       if (fromCallAdapter.getItemCount() > Consts.MAX_OPPONENTS_COUNT) {
            Toaster.longToast(String.format(getString(R.string.error_max_opponents_count),
                    Consts.MAX_OPPONENTS_COUNT));
            return;
        }

        if (fromCallAdapter.getItemCount() == 0) {
            Toast.makeText(this, "Please select at least one friend", Toast.LENGTH_SHORT).show();
            return;
        }


        Log.d(TAG, "startCall()");
        ArrayList<Integer> opponentsList = new ArrayList<>();


        opponentsList.add(qbUserId);

        QBRTCTypes.QBConferenceType conferenceType = isVideoCall
                ? QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO
                : QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_AUDIO;

        QBRTCClient qbrtcClient = QBRTCClient.getInstance(getApplicationContext());

        QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);

        WebRtcSessionManager.getInstance(this).setCurrentSession(newQbRtcSession);
        PushUtils.sendPushAboutMessage(qbChatDialog, currentUser.getFullName());

        //PushNotificationSender.sendPushMessage(opponentsList, currentUser.getFullName());

        CallActivity.start(this, false);
        Log.d(TAG, "conferenceType = " + conferenceType);
    }

    @Override
    public void processMessageDelivered(String s, String s1, Integer integer) {

    }

    @Override
    public void processMessageRead(String s, String s1, Integer integer) {

    }

    @Override
    public void onProfileClick(SavedProfile profile) {
        userProfileInfo= new UserProfileInfo();
        Intent intent = new Intent(ChatMatchAct.this, ProfileActivity.class);
        if(profile !=null){
            userProfileInfo = profile.getSavedPUserProfileInfo();

        }
        userProfileInfo.setMatchValue(qbChatDialog.getCustomData().getInteger("matchValue"));
        intent.putExtra(EXTRA_USER_PROFILE, userProfileInfo);
        intent.putExtra(EXTRA_SWIPE_VIEW_SOURCE, false);
        startActivity(intent);

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
            AttachmentImageActivity.start(ChatMatchAct.this, qbAttachment.getUrl());
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
        Window window = ChatMatchAct.this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(ChatMatchAct.this, R.color.color_blue_accent));
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
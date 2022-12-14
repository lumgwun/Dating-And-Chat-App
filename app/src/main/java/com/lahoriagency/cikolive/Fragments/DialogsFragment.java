package com.lahoriagency.cikolive.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Adapters.DialogAdapter44;
import com.lahoriagency.cikolive.Adapters.DiamondHisAdapter;
import com.lahoriagency.cikolive.Adapters.HorizontalListDialogsRecyclerViewAdapter;
import com.lahoriagency.cikolive.ChatAct;
import com.lahoriagency.cikolive.Classes.AppChat;
import com.lahoriagency.cikolive.Classes.AppE;
import com.lahoriagency.cikolive.Classes.BaseAsyncTask22;
import com.lahoriagency.cikolive.Classes.ChatHelper;
import com.lahoriagency.cikolive.Classes.DialogsManager;
import com.lahoriagency.cikolive.Classes.DiamondTransfer;
import com.lahoriagency.cikolive.Classes.GooglePlayServicesHelper;
import com.lahoriagency.cikolive.Classes.ItemTouchHelperCallback;
import com.lahoriagency.cikolive.Classes.QbChatDialogMessageListenerImp;
import com.lahoriagency.cikolive.Classes.QbDialogHolder;
import com.lahoriagency.cikolive.Classes.QbEntityCallbackImpl;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.UserProfileInfo;
import com.lahoriagency.cikolive.Classes.UserProfileInfoHolder;
import com.lahoriagency.cikolive.Classes.UserProfileInfoModel;
import com.lahoriagency.cikolive.Classes.UserProfileInfoReply;
import com.lahoriagency.cikolive.Classes.UserProfileInfoRequest;
import com.lahoriagency.cikolive.DataBase.DiamondHisDAO;
import com.lahoriagency.cikolive.Interfaces.GcmConsts;
import com.lahoriagency.cikolive.Interfaces.ServerMethodsConsts;
import com.lahoriagency.cikolive.R;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogCustomData;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;

@SuppressWarnings("deprecation")
public class DialogsFragment extends Fragment implements DialogsManager.ManagingDialogsCallbacks,DiamondHisAdapter.OnItemsClickListener {
    private static final String TAG = DialogsFragment.class.getSimpleName();
    public static final int REQUEST_DIALOG_ID_FOR_UPDATE = 165;

    private SwipyRefreshLayout setOnRefreshListener;
    private NestedScrollView nestedScrollView;
    private RelativeLayout noDialogsLayout;
    private RelativeLayout matchedDialogsHorizontalLayout;
    private TextView matchedDialogsHorizontalText;
    private QBRequestGetBuilder requestBuilder;

    private BroadcastReceiver pushBroadcastReceiver;
    private GooglePlayServicesHelper googlePlayServicesHelper;
    private DialogAdapter44 dialogsAdapter;
    private HorizontalListDialogsRecyclerViewAdapter horizontalDialogsAdapter;
    private RecyclerView dialogsRecyclerView,diamondRecylerV;
    private RecyclerView horizontalDialogsRecyclerView;
    private QBChatDialogMessageListener allDialogsMessagesListener;
    private SystemMessagesListener systemMessagesListener;
    private QBSystemMessagesManager systemMessagesManager;
    private QBIncomingMessagesManager incomingMessagesManager;
    private DialogsManager dialogsManager;

    private int skipRecords = 0;
    private boolean openDialogFromSwiping;
    private static final String APPLICATION_ID = QUICKBLOX_APP_ID;   //QUICKBLOX_APP_ID
    private static final String AUTH_KEY = QUICKBLOX_AUTH_KEY;
    private static final String AUTH_SECRET = QUICKBLOX_SECRET_KEY;
    private static final String ACCOUNT_KEY = QUICKBLOX_ACCT_KEY;
    private static final String SERVER_URL = "";
    private UserProfileInfoReply userProfileInfoReply;
    Gson gson2,gson,gson3,gson1;
    String json2,json3,json1,json, name;
    private  UserProfileInfo userProfileInfo;
    private SavedProfile savedProfile;
    private static final String PREF_NAME = "Ciko";
    private QBUser currentUser;
    private SharedPreferences userPreferences;
    private UserProfileInfoRequest userDialogInfoModel;
    private DiamondHisAdapter diamondHisAdapter;
    private ArrayList<DiamondTransfer>diamondTransfers;
    private ArrayList<DiamondTransfer>diamondTransfersTo;
    private ArrayList<DiamondTransfer>totalDiamondTransfers;
    private DiamondHisDAO diamondHisDAO;
    private int profID;
    private String toCollector,dialogID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googlePlayServicesHelper = new GooglePlayServicesHelper();
        pushBroadcastReceiver = new PushBroadcastReceiver();
        userProfileInfoReply= new UserProfileInfoReply();
        gson= new Gson();
        gson1= new Gson();
        gson2= new Gson();
        gson3= new Gson();
        currentUser= new QBUser();
        userProfileInfo= new UserProfileInfo();
        savedProfile= new SavedProfile();
        FirebaseApp.initializeApp(Objects.requireNonNull(getContext()));
        QBSettings.getInstance().init(getContext(), APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        userPreferences = getContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = userPreferences.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = userPreferences.getString("LastQBUserUsed", "");
        currentUser = gson1.fromJson(json1, QBUser.class);
        json2 = userPreferences.getString("LastUserProfileInfoUsed", "");
        userProfileInfo = gson2.fromJson(json2, UserProfileInfo.class);

        json3 = userPreferences.getString("LastUserProfileInfoReplyUsed", "");
        userProfileInfoReply = gson3.fromJson(json3, UserProfileInfoReply.class);

        allDialogsMessagesListener = new AllDialogsMessageListener();
        systemMessagesListener = new SystemMessagesListener();
        dialogsManager = new DialogsManager();


        registerQbChatListeners();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_dialogs, container, false);
        googlePlayServicesHelper = new GooglePlayServicesHelper();
        pushBroadcastReceiver = new PushBroadcastReceiver();
        userProfileInfoReply= new UserProfileInfoReply();
        gson= new Gson();
        gson1= new Gson();
        gson2= new Gson();
        gson3= new Gson();
        currentUser= new QBUser();
        userProfileInfo= new UserProfileInfo();
        savedProfile= new SavedProfile();
        FirebaseApp.initializeApp(Objects.requireNonNull(getContext()));
        QBSettings.getInstance().init(getContext(), APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        userPreferences = getContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = userPreferences.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = userPreferences.getString("LastQBUserUsed", "");
        currentUser = gson1.fromJson(json1, QBUser.class);
        json2 = userPreferences.getString("LastUserProfileInfoUsed", "");
        userProfileInfo = gson2.fromJson(json2, UserProfileInfo.class);

        json3 = userPreferences.getString("LastUserProfileInfoReplyUsed", "");
        userProfileInfoReply = gson3.fromJson(json3, UserProfileInfoReply.class);

        allDialogsMessagesListener = new AllDialogsMessageListener();
        systemMessagesListener = new SystemMessagesListener();
        dialogsManager = new DialogsManager();


        registerQbChatListeners();
        initUi(viewRoot);
        initHorizontalRecyclerView(viewRoot);
        return viewRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadDialogsFromQb(QbDialogHolder.getInstance().getDialogs().size() > 0, true);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            boolean deleteDialog = data.getExtras().getBoolean(ChatAct.EXTRA_DELETE_DIALOG, false);
            if(deleteDialog) {
                QBChatDialog qbChatDialog = (QBChatDialog) data.getExtras().getSerializable(ChatAct.EXTRA_DIALOG_DATA);
                dialogsManager.sendSystemMessageAboutDeletingDialog(systemMessagesManager, qbChatDialog);
                QbDialogHolder.getInstance().deleteDialog(qbChatDialog.getDialogId());
                updateDialogsAdapter();
            }

            if (requestCode == REQUEST_DIALOG_ID_FOR_UPDATE) {
                if (data != null) {
                    String dialogId = data.getStringExtra(ChatAct.EXTRA_DIALOG_ID);
                    loadUpdatedDialog(dialogId);
                } else {
                    updateDialogsList();
                }
            }
        } else {
            updateDialogsAdapter();
        }
    }

    private void initHorizontalRecyclerView(View viewRoot){
        diamondTransfers=new ArrayList<>();
        diamondTransfersTo= new ArrayList<>();
        totalDiamondTransfers=new ArrayList<>();
        diamondHisDAO= new DiamondHisDAO(getContext());
        googlePlayServicesHelper = new GooglePlayServicesHelper();
        pushBroadcastReceiver = new PushBroadcastReceiver();
        userProfileInfoReply= new UserProfileInfoReply();
        gson= new Gson();
        gson1= new Gson();
        gson2= new Gson();
        gson3= new Gson();
        currentUser= new QBUser();
        userProfileInfo= new UserProfileInfo();
        savedProfile= new SavedProfile();
        FirebaseApp.initializeApp(Objects.requireNonNull(getContext()));
        QBSettings.getInstance().init(getContext(), APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        userPreferences = getContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = userPreferences.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = userPreferences.getString("LastQBUserUsed", "");
        currentUser = gson1.fromJson(json1, QBUser.class);
        json2 = userPreferences.getString("LastUserProfileInfoUsed", "");
        userProfileInfo = gson2.fromJson(json2, UserProfileInfo.class);

        json3 = userPreferences.getString("LastUserProfileInfoReplyUsed", "");
        userProfileInfoReply = gson3.fromJson(json3, UserProfileInfoReply.class);

        allDialogsMessagesListener = new AllDialogsMessageListener();
        systemMessagesListener = new SystemMessagesListener();
        dialogsManager = new DialogsManager();
        if(savedProfile !=null){
            profID=savedProfile.getSavedProfID();

        }


        registerQbChatListeners();
        LinearLayoutManager layoutManager = new LinearLayoutManager(AppE.getAppContext(), LinearLayoutManager.HORIZONTAL, false);
        horizontalDialogsRecyclerView = viewRoot.findViewById(R.id.list_dialogs_profile_recyclerview);
        horizontalDialogsRecyclerView.setLayoutManager(layoutManager);
        horizontalDialogsAdapter = new HorizontalListDialogsRecyclerViewAdapter(getActivity(), new ArrayList<>(QbDialogHolder.getInstance().getDialogs().values()) );
        horizontalDialogsAdapter.setHasStableIds(true);
        horizontalDialogsRecyclerView.setAdapter(horizontalDialogsAdapter);
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(horizontalDialogsAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(horizontalDialogsRecyclerView);

        diamondTransfers=diamondHisDAO.getDiamondHisByProfID(profID);
        diamondTransfersTo=diamondHisDAO.getDiamondHisTo(profID);
        totalDiamondTransfers.addAll(diamondTransfers);
        totalDiamondTransfers.addAll(diamondTransfersTo);

        LinearLayoutManager layoutDiamond = new LinearLayoutManager(AppE.getAppContext(), LinearLayoutManager.HORIZONTAL, false);
        dialogsRecyclerView.setLayoutManager(layoutDiamond);
        diamondHisAdapter = new DiamondHisAdapter(getActivity(), totalDiamondTransfers);
        diamondHisAdapter.setHasStableIds(true);
        dialogsRecyclerView.setAdapter(diamondHisAdapter);

    }

    private void loadUpdatedDialog(final String dialogId) {
        ChatHelper.getInstance().getDialogById(dialogId, new QbEntityCallbackImpl<QBChatDialog>() {
            @Override
            public void onSuccess(final QBChatDialog result, Bundle bundle) {
                QbDialogHolder.getInstance().addDialog(result);
                ChatHelper.getInstance().getUsersFromDialog(result, new QbEntityCallbackImpl<ArrayList<QBUser>>() {
                    @Override
                    public void onSuccess(ArrayList<QBUser> users, Bundle bundle) {
                        horizontalDialogsAdapter.addToList(result);
                        updateDialogsAdapter();
                    }
                });
            }
            @Override
            public void onError(QBResponseException e) {
            }
        });
    }

    private void getUsersFromDialog(final QBChatDialog chatDialog) {
        QbDialogHolder.getInstance().addDialog(chatDialog);
        ChatHelper.getInstance().getUsersFromDialog(chatDialog, new QbEntityCallbackImpl<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> users, Bundle bundle) {
                horizontalDialogsAdapter.addToList(chatDialog);
                updateDialogsAdapter();
            }
        });
    }

    private void updateDialogsList() {
        requestBuilder.setSkip(skipRecords = 0);
        loadDialogsFromQb(true, true);
    }

    private void initUi(View view) {
        dialogsRecyclerView = view.findViewById(R.id.dialogs_recycler_view);
        setOnRefreshListener =  view.findViewById(R.id.swipy_refresh_layout);
        ArrayList<QBChatDialog> dialogs = new ArrayList<>(QbDialogHolder.getInstance().getDialogs().values());
        dialogsAdapter = new DialogAdapter44(getContext(), dialogs);

        //dialogsAdapter = new DialogsAdapter(new ArrayList<>(QbDialogHolder.getInstance().getDialogs().values()), getActivity());

        dialogsAdapter.setHasStableIds(true);
        dialogsRecyclerView.setAdapter(dialogsAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(AppE.getAppContext(), LinearLayoutManager.VERTICAL, false);
        dialogsRecyclerView.setLayoutManager(layoutManager);
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(dialogsAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(dialogsRecyclerView);

        requestBuilder = new QBRequestGetBuilder();

        setOnRefreshListener.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                requestBuilder.setSkip(skipRecords += ChatHelper.DIALOG_ITEMS_PER_PAGE);
                loadDialogsFromQb(true, false);
            }
        });

        noDialogsLayout = view.findViewById(R.id.no_dialogs_layout);
        nestedScrollView = view.findViewById(R.id.dialogs_scroll_view);
        matchedDialogsHorizontalLayout = view.findViewById(R.id.dialogs_matched_users_list);
        matchedDialogsHorizontalText = view.findViewById(R.id.dialogs_matched_users_text);
    }

    private void registerQbChatListeners() {
        incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
        systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();

        if (incomingMessagesManager != null) {
            incomingMessagesManager.addDialogMessageListener(allDialogsMessagesListener != null
                    ? allDialogsMessagesListener : new AllDialogsMessageListener());
        }

        if (systemMessagesManager != null) {
            systemMessagesManager.addSystemMessageListener(systemMessagesListener != null
                    ? systemMessagesListener : new SystemMessagesListener());
        }

        dialogsManager.addManagingDialogsCallbackListener(this);
    }

    private void unregisterQbChatListeners() {
        if (incomingMessagesManager != null) {
            incomingMessagesManager.removeDialogMessageListrener(allDialogsMessagesListener);
        }

        if (systemMessagesManager != null) {
            systemMessagesManager.removeSystemMessageListener(systemMessagesListener);
        }

        dialogsManager.removeManagingDialogsCallbackListener(this);
    }

    public void createNewDialog(int recipientId, int matchValue) {
        QBChatDialog dialog = DialogUtils.buildPrivateDialog(recipientId);
        QBDialogCustomData dialogCustomData = new QBDialogCustomData("DialogMatchValue");
        dialogCustomData.putInteger("matchValue", matchValue);
        dialog.setCustomData(dialogCustomData);
        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog result, Bundle params) {
                Log.d("Chat created", result.toString());
                dialogsManager.sendSystemMessageAboutCreatingDialog(systemMessagesManager, result);
                getUsersFromDialog(result);

                if (openDialogFromSwiping){
                    openDialogFromSwiping = false;
                    ChatAct.startForResult(getActivity(), REQUEST_DIALOG_ID_FOR_UPDATE, result);
                }
            }

            @Override
            public void onError(QBResponseException responseException) {
            }
        });
    }

    private void loadDialogsFromQb(final boolean silentUpdate, final boolean clearDialogHolder) {
//        if (!silentUpdate) {
//        }

        ChatHelper.getInstance().getDialogs(requestBuilder, new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> dialogs, Bundle bundle) {
                setOnRefreshListener.setRefreshing(false);

                if (clearDialogHolder) {
                    QbDialogHolder.getInstance().clear();
                }
                QbDialogHolder.getInstance().addDialogs(dialogs);
                updateDialogsAdapter();

                if (dialogs.size() > 0) {
                    String recipientsIds = getRecipientsIds(dialogs);
                    getUserDialogInfo(recipientsIds);
                }
            }

            @Override
            public void onError(QBResponseException e) {
                setOnRefreshListener.setRefreshing(false);
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getRecipientsIds(ArrayList<QBChatDialog> dialogs) {
        StringBuilder builder = new StringBuilder();
        String delimeter = "";
        for (int i = 0; i < dialogs.size(); i++) {
            builder.append(delimeter);
            delimeter = ";";
            builder.append(dialogs.get(i).getRecipientId());
        }
        return builder.toString();
    }

    private void getUserDialogInfo(String userIds) {
        userProfileInfoReply= new UserProfileInfoReply();
        gson= new Gson();
        gson1= new Gson();
        gson2= new Gson();
        gson3= new Gson();
        currentUser= new QBUser();
        userProfileInfo= new UserProfileInfo();
        savedProfile= new SavedProfile();
        //FirebaseApp.initializeApp(Objects.requireNonNull(getContext()));
        QBSettings.getInstance().init(getContext(), APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        userPreferences = getContext().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = userPreferences.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = userPreferences.getString("LastQBUserUsed", "");
        currentUser = gson1.fromJson(json1, QBUser.class);
        json2 = userPreferences.getString("LastUserProfileInfoUsed", "");
        userProfileInfo = gson2.fromJson(json2, UserProfileInfo.class);

        json3 = userPreferences.getString("LastUserProfileInfoRequestUsed", "");
        userDialogInfoModel = gson3.fromJson(json3, UserProfileInfoRequest.class);
        userDialogInfoModel = new UserProfileInfoRequest(AppE.getPreferences().getUserId(), userIds);
        GetUsersProfileInfo baseAsyncTask = new GetUsersProfileInfo(ServerMethodsConsts.USERSPROFILEINFO, userDialogInfoModel);
        baseAsyncTask.setHttpMethod("POST");
        baseAsyncTask.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        //googlePlayServicesHelper = new GooglePlayServicesHelper();
        /*if(googlePlayServicesHelper !=null){
            googlePlayServicesHelper.checkPlayServicesAvailable();

        }*/

        /*LocalBroadcastManager.getInstance(AppE.getAppContext()).registerReceiver(pushBroadcastReceiver,
                new IntentFilter(GcmConsts.ACTION_NEW_GCM_EVENT));*/
    }

    @Override
    public void onPause() {
        super.onPause();
        //LocalBroadcastManager.getInstance(AppE.getAppContext()).unregisterReceiver(pushBroadcastReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterQbChatListeners();
    }

    private void updateDialogsAdapter() {
        List<QBChatDialog> emptyDialogs = new ArrayList<>();
        List<QBChatDialog> startedDialogs = new ArrayList<>();
        for (QBChatDialog dialog : QbDialogHolder.getInstance().getDialogs().values()) {
            if (dialog.getLastMessage() != null || dialog.getLastMessageDateSent() > 0)
                startedDialogs.add(dialog);
            else
                emptyDialogs.add(0, dialog);
        }

        if (emptyDialogs.size() == 0 && startedDialogs.size() > 0) {
            matchedDialogsHorizontalText.setVisibility(View.GONE);
            matchedDialogsHorizontalLayout.setVisibility(View.GONE);
        } else {
            matchedDialogsHorizontalLayout.setVisibility(View.VISIBLE);
            matchedDialogsHorizontalText.setVisibility(View.VISIBLE);
        }

        if (emptyDialogs.size() > 0 || startedDialogs.size() > 0) {
            noDialogsLayout.setVisibility(View.INVISIBLE);
            nestedScrollView.setVisibility(View.VISIBLE);
        } else {
            nestedScrollView.setVisibility(View.INVISIBLE);
            noDialogsLayout.setVisibility(View.VISIBLE);
        }

        horizontalDialogsAdapter.updateList(emptyDialogs);
        dialogsAdapter.updateList(startedDialogs);
    }

    public boolean isOpenDialogFromSwiping() {
        return openDialogFromSwiping;
    }

    public void setOpenDialogFromSwiping(boolean openDialogFromSwiping) {
        this.openDialogFromSwiping = openDialogFromSwiping;
    }

    @Override
    public void onDialogCreated(QBChatDialog chatDialog) {
        updateDialogsAdapter();
    }

    @Override
    public void onDialogUpdated(String chatDialog) {
        updateDialogsAdapter();
    }

    @Override
    public void onNewDialogLoaded(QBChatDialog chatDialog) {
        updateDialogsAdapter();
    }

    //@Override
    public void onDialogDeleted(String chatDialog) {
        updateDialogsAdapter();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDiamondItemClick(DiamondTransfer diamondTransfer) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_d);
        bottomSheetDialog.show();

        LinearLayoutCompat linearLayoutCompat = bottomSheetDialog.findViewById(R.id.bs_layout);
        ImageView imgClose=bottomSheetDialog.findViewById(R.id.btn_bs_dialog_close);
        RecyclerView recyclerView = bottomSheetDialog.findViewById(R.id._h_rv);
        diamondTransfers=new ArrayList<>();
        diamondTransfersTo= new ArrayList<>();
        diamondHisDAO= new DiamondHisDAO(getContext());
        if(diamondTransfer !=null){
            dialogID=diamondTransfer.getdH_Dialog_ID();
        }
        diamondTransfers=diamondHisDAO.getDiamondHisByDialogID(dialogID);

        LinearLayoutManager layoutDiamond = new LinearLayoutManager(AppE.getAppContext(), LinearLayoutManager.VERTICAL, false);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutDiamond);
        }
        diamondHisAdapter = new DiamondHisAdapter(getActivity(), diamondTransfers);
        diamondHisAdapter.setHasStableIds(true);
        recyclerView.setAdapter(diamondHisAdapter);
        if (imgClose != null) {
            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.dismiss();

                }
            });
        }

    }

    private class PushBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(GcmConsts.EXTRA_GCM_MESSAGE);
            Log.v(TAG, "Received broadcast " + intent.getAction() + " with data: " + message);
            requestBuilder.setSkip(skipRecords = 0);
            loadDialogsFromQb(true, true);
        }
    }

    private class SystemMessagesListener implements QBSystemMessageListener {
        @Override
        public void processMessage(final QBChatMessage qbChatMessage) {
            dialogsManager.onSystemMessageReceived(qbChatMessage);
        }

        @Override
        public void processError(QBChatException e, QBChatMessage qbChatMessage) {

        }
    }

    private class AllDialogsMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(final String dialogId, final QBChatMessage qbChatMessage, Integer senderId) {
            if (!senderId.equals(ChatHelper.getCurrentUser().getId())) {
                dialogsManager.onGlobalMessageReceived(dialogId, qbChatMessage);
            }
        }
    }

    private class GetUsersProfileInfo extends BaseAsyncTask22<UserProfileInfoRequest> {

        public GetUsersProfileInfo(String urn, UserProfileInfoRequest params) {
            super(urn, params);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            userProfileInfoReply = AppE.getGson().fromJson(result, UserProfileInfoReply.class);
            if (userProfileInfoReply.isStatusOkay()) {
                for (UserProfileInfoModel model : userProfileInfoReply.getUsersProfileInfo()) {
                    UserProfileInfo profileInfo = new UserProfileInfo(model);
                    UserProfileInfoHolder.getInstance().putProfileInfo(profileInfo);
                }
            }
        }
    }
}

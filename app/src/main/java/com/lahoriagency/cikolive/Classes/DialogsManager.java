package com.lahoriagency.cikolive.Classes;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.lahoriagency.cikolive.Conference.AppConference;
import com.lahoriagency.cikolive.R;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogCustomData;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smackx.muc.DiscussionHistory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@SuppressWarnings("deprecation")
public class DialogsManager {
    private static final String TAG = DialogsManager.class.getSimpleName();

    private static final String PROPERTY_NEW_OCCUPANTS_IDS = "new_occupants_ids";
    public static final String PROPERTY_NOTIFICATION_TYPE = "notification_type";
    private static final String CREATING_DIALOG = "1";
    private static final String OCCUPANTS_ADDED = "2";
    private static final String OCCUPANT_LEFT = "3";

    public static final String PROPERTY_OCCUPANTS_IDS = "occupants_ids";
    public static final String PROPERTY_DIALOG_TYPE = "dialog_type";
    public static final String PROPERTY_DIALOG_NAME = "dialog_name";
    public static final String PROPERTY_MATCH_VALUE = "match_value";
    public static final String DELETING_DIALOG = "deleting_dialog";

    public static final String PROPERTY_CONVERSATION_ID = "conference_id";

    public static final String START_CONFERENCE = "4";
    public static final String START_STREAM = "5";

    private Context context;
    private Set<DialogsCallbacks> dialogsCallbacks = new CopyOnWriteArraySet<>();



    public DialogsManager(Context context) {
        this.context = context;
    }

    private Set<ManagingDialogsCallbacks> managingDialogsCallbackListener = new CopyOnWriteArraySet<>();

    public DialogsManager() {
        super();
    }

    private boolean isMessageCreatedDialog(QBChatMessage message) {
        return CREATING_DIALOG.equals(message.getProperty(PROPERTY_NOTIFICATION_TYPE));
    }

    private boolean isMessageAddedUser(QBChatMessage message) {
        return OCCUPANTS_ADDED.equals(message.getProperty(PROPERTY_NOTIFICATION_TYPE));
    }

    private boolean isMessageLeftUser(QBChatMessage message) {
        return OCCUPANT_LEFT.equals(message.getProperty(PROPERTY_NOTIFICATION_TYPE));
    }
    private boolean isMessageCreatingDialog(QBChatMessage systemMessage){
        return CREATING_DIALOG.equals(systemMessage.getProperty(PROPERTY_NOTIFICATION_TYPE));
    }

    private boolean isMessageDeletingDialog(QBChatMessage systemMessage) {
        return DELETING_DIALOG.equals(systemMessage.getProperty(PROPERTY_NOTIFICATION_TYPE));
    }

    private QBChatMessage buildMessageCreatedGroupDialog(QBChatDialog dialog) {
        QBChatMessage qbChatMessage = new QBChatMessage();
        qbChatMessage.setDialogId(dialog.getDialogId());
        qbChatMessage.setProperty(PROPERTY_OCCUPANTS_IDS, QbDialogUtils.getOccupantsIdsStringFromList(dialog.getOccupants()));
        qbChatMessage.setProperty(PROPERTY_DIALOG_TYPE, String.valueOf(dialog.getType().getCode()));
        qbChatMessage.setProperty(PROPERTY_DIALOG_NAME, String.valueOf(dialog.getName()));
        qbChatMessage.setProperty(PROPERTY_NOTIFICATION_TYPE, CREATING_DIALOG);
        qbChatMessage.setDateSent(System.currentTimeMillis() / 1000);
        qbChatMessage.setBody(AppChat.getInstance().getString(R.string.new_chat_created, getCurrentUserName(), dialog.getName()));
        qbChatMessage.setSaveToHistory(true);
        qbChatMessage.setMarkable(true);
        return qbChatMessage;
    }

    private QBChatMessage buildMessageAddedUsers(QBChatDialog dialog, String userIds, String usersNames) {
        QBChatMessage qbChatMessage = new QBChatMessage();
        qbChatMessage.setDialogId(dialog.getDialogId());
        qbChatMessage.setProperty(PROPERTY_NOTIFICATION_TYPE, OCCUPANTS_ADDED);
        qbChatMessage.setProperty(PROPERTY_NEW_OCCUPANTS_IDS, userIds);
        qbChatMessage.setBody(AppChat.getInstance().getString(R.string.occupant_added, getCurrentUserName(), usersNames));
        qbChatMessage.setSaveToHistory(true);
        qbChatMessage.setMarkable(true);
        return qbChatMessage;
    }

    private QBChatMessage buildMessageLeftUser(QBChatDialog dialog) {
        QBChatMessage qbChatMessage = new QBChatMessage();
        qbChatMessage.setDialogId(dialog.getDialogId());
        qbChatMessage.setProperty(PROPERTY_NOTIFICATION_TYPE, OCCUPANT_LEFT);
        qbChatMessage.setBody(AppChat.getInstance().getString(R.string.occupant_left, getCurrentUserName()));
        qbChatMessage.setSaveToHistory(true);
        qbChatMessage.setMarkable(true);
        return qbChatMessage;
    }

    private QBChatDialog buildChatDialogFromNotificationMessage(QBChatMessage qbChatMessage) {
        QBChatDialog chatDialog = new QBChatDialog();
        chatDialog.setDialogId(qbChatMessage.getDialogId());
        chatDialog.setUnreadMessageCount(0);
        return chatDialog;
    }

    private String getCurrentUserName() {
        QBUser currentUser = QBChatService.getInstance().getUser();
        return TextUtils.isEmpty(currentUser.getFullName()) ? currentUser.getLogin() : currentUser.getFullName();
    }
    private QBChatMessage buildMessageConferenceStarted(QBChatDialog dialog) {
        QBChatMessage qbChatMessage = new QBChatMessage();
        qbChatMessage.setDialogId(dialog.getDialogId());
        qbChatMessage.setProperty(PROPERTY_NOTIFICATION_TYPE, START_CONFERENCE);
        qbChatMessage.setProperty(PROPERTY_CONVERSATION_ID, dialog.getDialogId());
        qbChatMessage.setBody(context.getResources().getString(R.string.chat_conversation_started, context.getString(R.string.chat_conference)));
        qbChatMessage.setSaveToHistory(true);
        qbChatMessage.setMarkable(true);
        return qbChatMessage;
    }

    ////// Sending Notification Messages


    public void sendMessageAddedUsers(final QBChatDialog dialog, final List<Integer> newUsersIds) {
        QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder(ChatHelper.USERS_PER_PAGE, 1);
        loadNewUsersByIDs(newUsersIds, requestBuilder, new ArrayList<QBUser>(), new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                String usersIds = QbDialogUtils.getOccupantsIdsStringFromList(newUsersIds);
                if (!newUsersIds.isEmpty() && qbUsers != null) {
                    String usersNames = QbDialogUtils.getOccupantsNamesStringFromList(qbUsers);
                    QBChatMessage messageUsersAdded = buildMessageAddedUsers(dialog, usersIds, usersNames);

                    Log.d(TAG, "Sending Notification Message to Opponents about Adding Occupants");
                    sendMessageHandleJoining(dialog, messageUsersAdded);
                }
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(TAG, "Failed to load users to send message. " + e.getMessage());
                ToastUtils.shortToast("Failed to load users to send message");
            }
        });
    }


    public void sendMessageLeftUser(QBChatDialog dialog) {
        QBChatMessage messageLeftUser = buildMessageLeftUser(dialog);

        Log.d(TAG, "Sending Notification Message to Opponents about User Left to dialog : " + dialog.getName());
        sendMessageHandleJoining(dialog, messageLeftUser);
    }

    private void sendMessageHandleJoining(final QBChatDialog dialog, final QBChatMessage qbChatMessage) {
        if (dialog.isJoined()) {
            try {
                Log.d(TAG, "Sending Notification Message");
                dialog.sendMessage(qbChatMessage);
            } catch (IllegalStateException | SmackException.NotConnectedException e) {
                Log.d(TAG, "Sending Notification Message Error: " + e.getMessage());
            }
        } else {
            ChatHelper.getInstance().join(dialog, new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid, Bundle bundle) {
                    sendMessageHandleJoining(dialog, qbChatMessage);
                }

                @Override
                public void onError(QBResponseException e) {
                    ToastUtils.shortToast(e.getMessage());
                    Log.d(TAG, "Joining error: " + e.getMessage());
                }
            });

            ((AppConference) context).getChatHelper().join(dialog, new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid, Bundle bundle) {
                    sendMessageHandleJoining(dialog, qbChatMessage);
                }

                @Override
                public void onError(QBResponseException e) {
                    ToastUtils.shortToast(e.getMessage());
                    Log.d(TAG, "Joining error: " + e.getMessage());
                }
            });
        }
    }

    public void sendMessageCreatedDialog(QBChatDialog dialog) {
        QBChatMessage messageCreatingDialog = buildMessageCreatedGroupDialog(dialog);

        Log.d(TAG, "Sending Notification Message about Creating Group Dialog");
        sendMessageHandleJoining(dialog, messageCreatingDialog);
    }




    public void sendMessageStartedConference(QBChatDialog dialog) {
        QBChatMessage messageStartedConference = buildMessageConferenceStarted(dialog);

        Log.d(TAG, "Sending Notification Message about Conference Started: " + dialog.getName());
        sendMessageHandleJoining(dialog, messageStartedConference);
    }

    public void sendMessageStartedStream(QBChatDialog dialog, String streamID) {
        QBChatMessage messageStartedStream = buildMessageStreamStarted(dialog, streamID);

        Log.d(TAG, "Sending Notification Message about Stream Started: " + dialog.getName());
        sendMessageHandleJoining(dialog, messageStartedStream);
    }









    ////// Sending System Messages

    public void sendSystemMessageAboutCreatingDialog(QBSystemMessagesManager systemMessagesManager, QBChatDialog dialog) {
        QBChatMessage messageCreatingDialog = buildMessageCreatedGroupDialog(dialog);
        prepareSystemMessage(systemMessagesManager, messageCreatingDialog, dialog.getOccupants());
    }

    public void sendSystemMessageAddedUser(final QBSystemMessagesManager systemMessagesManager, final QBChatDialog dialog, final List<Integer> newUsersIds) {
        QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder(ChatHelper.USERS_PER_PAGE, 1);
        loadNewUsersForSystemMsgsByIDs(newUsersIds, requestBuilder, new ArrayList<QBUser>(), new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                String usersIds = QbDialogUtils.getOccupantsIdsStringFromList(newUsersIds);
                if (!newUsersIds.isEmpty() && qbUsers != null) {
                    String usersNames = QbDialogUtils.getOccupantsNamesStringFromList(qbUsers);

                    QBChatMessage messageUsersAdded = buildMessageAddedUsers(dialog, usersIds, usersNames);
                    prepareSystemMessage(systemMessagesManager, messageUsersAdded, dialog.getOccupants());

                    QBChatMessage messageDialogCreated = buildMessageCreatedGroupDialog(dialog);
                    prepareSystemMessage(systemMessagesManager, messageDialogCreated, newUsersIds);
                }
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(TAG, "Failed to load users to send system message. " + e.getMessage());
                ToastUtils.shortToast("Failed to load users to send system message");
            }
        });
    }

    public void sendSystemMessageLeftUser(final QBSystemMessagesManager systemMessagesManager, final QBChatDialog dialog) {
        QBChatMessage messageLeftUser = buildMessageLeftUser(dialog);
        prepareSystemMessage(systemMessagesManager, messageLeftUser, dialog.getOccupants());
    }

    private void loadNewUsersForSystemMsgsByIDs(final Collection<Integer> userIDs, final QBPagedRequestBuilder requestBuilder,
                                                final ArrayList<QBUser> alreadyLoadedUsers, final QBEntityCallback<ArrayList<QBUser>> callback) {
        QBUsers.getUsersByIDs(userIDs, requestBuilder).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                if (qbUsers != null) {
                    QbUsersHolder.getInstance().putUsers(qbUsers);
                    alreadyLoadedUsers.addAll(qbUsers);

                    if (bundle != null) {
                        int totalPages = (int) bundle.get(ChatHelper.TOTAL_PAGES_BUNDLE_PARAM);
                        int currentPage = (int) bundle.get(ChatHelper.CURRENT_PAGE_BUNDLE_PARAM);
                        if (totalPages > currentPage) {
                            requestBuilder.setPage(currentPage + 1);
                            loadNewUsersForSystemMsgsByIDs(userIDs, requestBuilder, alreadyLoadedUsers, callback);
                        } else {
                            callback.onSuccess(alreadyLoadedUsers, bundle);
                        }
                    }
                }
            }

            @Override
            public void onError(QBResponseException e) {
                callback.onError(e);
            }
        });
    }

    private void prepareSystemMessage(QBSystemMessagesManager systemMessagesManager, QBChatMessage message, List<Integer> occupants) {
        message.setSaveToHistory(false);
        message.setMarkable(false);

        try {
            for (Integer opponentID : occupants) {
                if (!opponentID.equals(QBChatService.getInstance().getUser().getId())) {
                    message.setRecipientId(opponentID);
                    Log.d(TAG, "Sending System Message to " + opponentID);
                    systemMessagesManager.sendSystemMessage(message);
                }
            }
        } catch (SmackException.NotConnectedException e) {
            Log.d(TAG, "Sending System Message Error: " + e.getMessage());
            e.printStackTrace();
        }
    }




    private void loadUsersFromDialog(QBChatDialog chatDialog) {
        ChatHelper.getInstance().getUsersFromDialog(chatDialog, new QbEntityCallbackImpl<ArrayList<QBUser>>());
    }

    private void notifyListenersDialogCreated(final QBChatDialog chatDialog) {
        for (ManagingDialogsCallbacks listener : getManagingDialogsCallbackListeners()) {
            listener.onDialogCreated(chatDialog);
        }
    }

    private void notifyListenersDialogUpdated(final String dialogId) {
        for (ManagingDialogsCallbacks listener : getManagingDialogsCallbackListeners()) {
            listener.onDialogUpdated(dialogId);
        }
    }

    private void notifyListenersNewDialogLoaded(final QBChatDialog chatDialog) {
        for (ManagingDialogsCallbacks listener : getManagingDialogsCallbackListeners()) {
            listener.onNewDialogLoaded(chatDialog);
        }
    }

    public void addManagingDialogsCallbackListener(ManagingDialogsCallbacks listener) {
        if (listener != null) {
            managingDialogsCallbackListener.add(listener);
        }
    }

    public void removeManagingDialogsCallbackListener(ManagingDialogsCallbacks listener) {
        managingDialogsCallbackListener.remove(listener);
    }

    private Collection<ManagingDialogsCallbacks> getManagingDialogsCallbackListeners() {
        return Collections.unmodifiableCollection(managingDialogsCallbackListener);
    }

    public interface ManagingDialogsCallbacks {

        void onDialogCreated(QBChatDialog chatDialog);

        void onDialogUpdated(String chatDialog);

        void onNewDialogLoaded(QBChatDialog chatDialog);
    }

    private QBChatMessage buildSystemMessageAboutCreatingDialog(QBChatDialog dialog){
        QBChatMessage qbChatMessage = new QBChatMessage();
        qbChatMessage.setDialogId(dialog.getDialogId());
        qbChatMessage.setProperty(PROPERTY_OCCUPANTS_IDS, QbDialogUtils.getOccupantsIdsStringFromList(dialog.getOccupants()));
        qbChatMessage.setProperty(PROPERTY_DIALOG_TYPE, String.valueOf(dialog.getType().getCode()));
        qbChatMessage.setProperty(PROPERTY_DIALOG_NAME, String.valueOf(dialog.getName()));
        qbChatMessage.setProperty(PROPERTY_NOTIFICATION_TYPE, CREATING_DIALOG);
        qbChatMessage.setProperty(PROPERTY_MATCH_VALUE, String.valueOf(dialog.getCustomData().getInteger("matchValue")));
        return qbChatMessage;
    }


    private QBChatMessage buildSystemMessageAboutDeletingDialog(QBChatDialog dialog) {
        QBChatMessage qbChatMessage = new QBChatMessage();
        qbChatMessage.setDialogId(dialog.getDialogId());
        qbChatMessage.setProperty(PROPERTY_OCCUPANTS_IDS, QbDialogUtils.getOccupantsIdsStringFromList(dialog.getOccupants()));
        qbChatMessage.setProperty(PROPERTY_DIALOG_TYPE, String.valueOf(dialog.getType().getCode()));
        qbChatMessage.setProperty(PROPERTY_NOTIFICATION_TYPE, DELETING_DIALOG);

        return qbChatMessage;
    }

    private QBChatDialog buildChatDialogFromSystemMessage(QBChatMessage qbChatMessage){
        QBChatDialog chatDialog = new QBChatDialog();
        chatDialog.setDialogId(qbChatMessage.getDialogId());
        chatDialog.setOccupantsIds(QbDialogUtils.getOccupantsIdsListFromString((String) qbChatMessage.getProperty(PROPERTY_OCCUPANTS_IDS)));
        chatDialog.setType(QBDialogType.parseByCode(Integer.parseInt(qbChatMessage.getProperty(PROPERTY_DIALOG_TYPE).toString())));
        chatDialog.setName(qbChatMessage.getProperty(PROPERTY_DIALOG_NAME).toString());
        chatDialog.setUnreadMessageCount(0);
        QBDialogCustomData customData = new QBDialogCustomData("DialogMatchValue");
        customData.putInteger("matchValue", Integer.parseInt(qbChatMessage.getProperty(PROPERTY_MATCH_VALUE).toString()));
        chatDialog.setCustomData(customData);
        return chatDialog;
    }


    public void sendSystemMessageAboutDeletingDialog(QBSystemMessagesManager systemMessagesManager, QBChatDialog dialog) {
        QBChatMessage systemMessageDeletingDialog = buildSystemMessageAboutDeletingDialog(dialog);
        try {
            for(Integer recipientId: dialog.getOccupants()) {
                if (!recipientId.equals(QBChatService.getInstance().getUser().getId())) {
                    systemMessageDeletingDialog.setRecipientId(recipientId);
                    systemMessagesManager.sendSystemMessage(systemMessageDeletingDialog);
                }
            }
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }
    public void onGlobalMessageReceived(String dialogId, final QBChatMessage chatMessage) {
        Log.d(TAG, "Global Message Received: " + chatMessage.getId());
        if (isMessageCreatedDialog(chatMessage) && !((AppConference) context).getQBDialogsHolder().hasDialogWithId(dialogId)) {
            loadNewDialogByNotificationMessage(chatMessage);
        }

        if (isMessageAddedUser(chatMessage) || isMessageLeftUser(chatMessage)) {
            if (((AppConference) context).getQBDialogsHolder().hasDialogWithId(dialogId)) {
                notifyListenersDialogUpdated(dialogId);
            } else {
                loadNewDialogByNotificationMessage(chatMessage);
            }
        }

        if (chatMessage.isMarkable()) {
            if (((AppConference) context).getQBDialogsHolder().hasDialogWithId(dialogId)) {
                ((AppConference) context).getQBDialogsHolder().updateDialog(dialogId, chatMessage);
                notifyListenersDialogUpdated(dialogId);
            } else {
                QBRestChatService.getChatDialogById(dialogId).performAsync(new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        Log.d(TAG, "Loading Dialog Successful");
                        ((AppConference) context).getChatHelper().getUsersFromDialog(qbChatDialog, new QBEntityCallbackImpl<>());
                        ((AppConference) context).getQBDialogsHolder().addDialog(qbChatDialog);
                        notifyListenersNewDialogLoaded(qbChatDialog);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.d(TAG, "Loading Dialog Error: " + e.getMessage());
                    }
                });
            }
        }
        if (chatMessage.isMarkable()) {
            if (isMessageCreatedDialog(chatMessage) && !QbDialogHolder.getInstance().hasDialogWithId(dialogId)) {
                loadNewDialogByNotificationMessage(chatMessage);
            }

            if (isMessageAddedUser(chatMessage) || isMessageLeftUser(chatMessage)) {
                if (QbDialogHolder.getInstance().hasDialogWithId(dialogId)) {
                    notifyListenersDialogUpdated(dialogId);
                } else {
                    loadNewDialogByNotificationMessage(chatMessage);
                }
            }
            if (QbDialogHolder.getInstance().hasDialogWithId(dialogId)) {
                QbDialogHolder.getInstance().updateDialog(dialogId, chatMessage);
                notifyListenersDialogUpdated(dialogId);
            } else {
                ChatHelper.getInstance().getDialogById(dialogId, new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        Log.d(TAG, "Loading Dialog Successful");
                        loadUsersFromDialog(qbChatDialog);
                        QbDialogHolder.getInstance().addDialog(qbChatDialog);
                        notifyListenersNewDialogLoaded(qbChatDialog);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.d(TAG, "Loading Dialog Error: " + e.getMessage());
                    }
                });
            }
        }
        if (chatMessage.getBody() != null && chatMessage.isMarkable()) {
            if (QbDialogHolder.getInstance().hasDialogWithId(dialogId)) {
                QbDialogHolder.getInstance().updateDialog(dialogId, chatMessage);
                notifyListenersDialogUpdated(dialogId);
            } else {
                ChatHelper.getInstance().getDialogById(dialogId, new QbEntityCallbackImpl<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog chatDialog, Bundle bundle) {
                        loadUsersFromDialog(chatDialog);
                        QbDialogHolder.getInstance().addDialog(chatDialog);
                        notifyListenersNewDialogLoaded(chatDialog);
                    }
                });
            }
        }
    }



    public void onSystemMessageReceived(QBChatMessage systemMessage){
        if (isMessageCreatingDialog(systemMessage)) {
            QBChatDialog chatDialog = buildChatDialogFromSystemMessage(systemMessage);
            chatDialog.initForChat(QBChatService.getInstance());
            QbDialogHolder.getInstance().addDialog(chatDialog);
            notifyListenersDialogCreated(chatDialog);
        } else if (isMessageDeletingDialog(systemMessage)) {
            QbDialogHolder.getInstance().deleteDialog(systemMessage.getDialogId());
            notifyListenersDialogDeleted(systemMessage.getDialogId());
        }
        Log.d(TAG, "System Message Received: " + systemMessage.getBody() + " Notification Type: " + systemMessage.getProperty(PROPERTY_NOTIFICATION_TYPE));
        onGlobalMessageReceived(systemMessage.getDialogId(), systemMessage);
    }



    private void notifyListenersDialogDeleted(final String chatDialog) {
        for (ManagingDialogsCallbacks listener : getManagingDialogsCallbackListeners()) {
            //listener.onDialogDeleted(chatDialog);
        }
    }
    private QBChatMessage buildMessageStreamStarted(QBChatDialog dialog, String streamID) {
        QBChatMessage qbChatMessage = new QBChatMessage();
        qbChatMessage.setDialogId(dialog.getDialogId());
        qbChatMessage.setProperty(PROPERTY_NOTIFICATION_TYPE, START_STREAM);
        qbChatMessage.setProperty(PROPERTY_CONVERSATION_ID, streamID);
        qbChatMessage.setBody(context.getResources().getString(R.string.chat_conversation_started, context.getString(R.string.chat_stream)));
        qbChatMessage.setSaveToHistory(true);
        qbChatMessage.setMarkable(true);
        return qbChatMessage;
    }



    private void loadNewUsersByIDs(final Collection<Integer> userIDs, final QBPagedRequestBuilder requestBuilder,
                                   final ArrayList<QBUser> alreadyLoadedUsers, final QBEntityCallback<ArrayList<QBUser>> callback) {
        QBUsers.getUsersByIDs(userIDs, requestBuilder).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                if (qbUsers != null) {
                    QbUsersHolder.getInstance().putUsers(qbUsers);
                    alreadyLoadedUsers.addAll(qbUsers);

                    if (bundle != null) {
                        int totalPages = (int) bundle.get(ChatHelper.TOTAL_PAGES_BUNDLE_PARAM);
                        int currentPage = (int) bundle.get(ChatHelper.CURRENT_PAGE_BUNDLE_PARAM);
                        if (totalPages > currentPage) {
                            requestBuilder.setPage(currentPage + 1);
                            loadNewUsersByIDs(userIDs, requestBuilder, alreadyLoadedUsers, callback);
                        } else {
                            callback.onSuccess(alreadyLoadedUsers, bundle);
                        }
                    }
                }
            }

            @Override
            public void onError(QBResponseException e) {
                callback.onError(e);
            }
        });
    }



    ////// Sending System Messages



    ////// Message Receivers

    private void loadNewDialogByNotificationMessage(QBChatMessage chatMessage) {
        QBChatDialog chatDialog = buildChatDialogFromNotificationMessage(chatMessage);
        QBRestChatService.getChatDialogById(chatDialog.getDialogId()).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(final QBChatDialog qbChatDialog, Bundle bundle) {
                Log.d(TAG, "Loading Dialog Successful");
                qbChatDialog.initForChat(QBChatService.getInstance());

                DiscussionHistory history = new DiscussionHistory();
                history.setMaxStanzas(0);

                qbChatDialog.join(history, new QBEntityCallbackImpl() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                        ((AppConference) context).getQBDialogsHolder().addDialog(qbChatDialog);
                        notifyListenersDialogCreated(qbChatDialog);
                    }
                });
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d(TAG, "Loading Dialog Error: " + e.getMessage());
            }
        });
    }




    public void addDialogsCallbacks(DialogsCallbacks listener) {
        if (listener != null) {
            dialogsCallbacks.add(listener);
        }
    }

    public void removeDialogsCallbacks(DialogsCallbacks listener) {
        dialogsCallbacks.remove(listener);
    }

    private Collection<DialogsCallbacks> getDialogsCallbacks() {
        return Collections.unmodifiableCollection(dialogsCallbacks);
    }

    public interface DialogsCallbacks {

        void onDialogCreated(QBChatDialog chatDialog);

        void onDialogUpdated(String chatDialog);

        void onNewDialogLoaded(QBChatDialog chatDialog);
    }



}

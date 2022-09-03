package com.lahoriagency.cikolive.Classes;

import android.Manifest;

public interface Consts {
    int ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS = 422;

    int MAX_OPPONENTS_COUNT = 6;
    int MAX_LOGIN_LENGTH = 15;
    int MAX_FULLNAME_LENGTH = 20;
    String EXTRA_FCM_MESSAGE = "message";
    String ACTION_NEW_FCM_EVENT = "new-push-event";
    String EMPTY_FCM_MESSAGE = "empty message";

    String EXTRA_QB_USER = "qb_user";
    String EXTRA_USER_ID = "user_id";
    String EXTRA_USER_LOGIN = "user_login";
    String EXTRA_USER_PASSWORD = "user_password";
    String EXTRA_PENDING_INTENT = "pending_Intent";
    String EXTRA_CONTEXT = "context";
    String EXTRA_OPPONENTS_LIST = "opponents_list";
    String EXTRA_CONFERENCE_TYPE = "conference_type";

    String EXTRA_IS_INCOMING_CALL = "conversation_reason";

    String EXTRA_LOGIN_RESULT = "login_result";
    String EXTRA_LOGIN_ERROR_MESSAGE = "login_error_message";
    int EXTRA_LOGIN_RESULT_CODE = 1002;

    String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};


    String EXTRA_ROOM_ID = "17094";
    String EXTRA_ROOM_TITLE = "Room Tittle";
    String EXTRA_DIALOG_ID = "876";
    String EXTRA_DIALOG_OCCUPANTS = "9";
    String EXTRA_AS_LISTENER = "8";
    String PREF_MIC_ENABLED = "MIC ENABLED";
}

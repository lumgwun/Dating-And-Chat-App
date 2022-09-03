package com.lahoriagency.cikolive.Interfaces;

import android.Manifest;

import com.lahoriagency.cikolive.Classes.ResourceUtils;
import com.lahoriagency.cikolive.R;

public interface Consts {
    String SAMPLE_CONFIG_FILE_NAME = "sample_config.json";

    int PREFERRED_IMAGE_SIZE_PREVIEW = ResourceUtils.getDimen(R.dimen.chat_attachment_preview_size);
    int PREFERRED_IMAGE_SIZE_FULL = ResourceUtils.dpToPx(320);
    String QB_USER_PASSWORD = "qb_user_password";
    String PREF_SWAP_CAM_TOGGLE_CHECKED = "pref_swap_cam_toggle_checked";
    String PREF_SCREEN_SHARING_TOGGLE_CHECKED = "pref_screen_sharing_toggle_checked";
    String PREF_CAM_ENABLED = "pref_cam_enabled";
    String PREF_MIC_ENABLED = "pref_mic_enabled";

    String EXTRA_DIALOG_ID = "dialog_id";
    String EXTRA_CERTAIN_DIALOG_ID = "certain_dialog_id";
    String EXTRA_ROOM_ID = "room_id";
    String EXTRA_ROOM_TITLE = "room_title";
    String EXTRA_DIALOG_OCCUPANTS = "dialog_occupants";
    String EXTRA_AS_LISTENER = "as_listener";
    String EXTRA_SETTINGS_TYPE = "extra_settings_type";



    String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
}

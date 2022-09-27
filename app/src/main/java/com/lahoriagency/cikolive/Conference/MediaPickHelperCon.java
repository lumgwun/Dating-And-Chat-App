package com.lahoriagency.cikolive.Conference;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.lahoriagency.cikolive.Fragments.MediaPickHelperFragment;
import com.lahoriagency.cikolive.Fragments.MediaSourcePickDialogFragment;

public class MediaPickHelperCon {
    public static void pickAnImage(FragmentActivity activity, int requestCode) {
        MediaPickHelperFragment mediaPickHelperFragment = MediaPickHelperFragment.start(activity, requestCode);
        showImageSourcePickerDialog(activity.getSupportFragmentManager(), mediaPickHelperFragment);
    }

    private static void showImageSourcePickerDialog(FragmentManager fm, MediaPickHelperFragment fragment) {
        MediaSourcePickDialogFragment.show(fm,
                new MediaSourcePickDialogFragment.LoggableActivityImageSourcePickedListener(fragment));
    }
}

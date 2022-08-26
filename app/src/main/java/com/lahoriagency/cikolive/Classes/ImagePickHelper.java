package com.lahoriagency.cikolive.Classes;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.lahoriagency.cikolive.Fragments.MediaPickHelperFragment;
import com.lahoriagency.cikolive.Fragments.MediaSourcePickDialogFragment;

public class ImagePickHelper {
    public void pickAnImage(FragmentActivity activity, int requestCode) {
        MediaPickHelperFragment mediaPickHelperFragment = MediaPickHelperFragment.start(activity, requestCode);
        showImageSourcePickerDialog(activity.getSupportFragmentManager(), mediaPickHelperFragment);
    }

    private void showImageSourcePickerDialog(FragmentManager fm, MediaPickHelperFragment fragment) {
        MediaSourcePickDialogFragment.show(fm,
                new MediaSourcePickDialogFragment.ImageSourcePickedListener(fragment));
    }
}

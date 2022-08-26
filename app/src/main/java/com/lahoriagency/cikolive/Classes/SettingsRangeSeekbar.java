package com.lahoriagency.cikolive.Classes;


import android.content.Context;
import android.util.AttributeSet;

import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.lahoriagency.cikolive.R;

public class SettingsRangeSeekbar extends CrystalRangeSeekbar {

    private final int THUMB_SIZE = (int) getResources().getDimension(R.dimen._22sdp);

    public SettingsRangeSeekbar(Context context) {
        super(context);
    }

    public SettingsRangeSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SettingsRangeSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected float getThumbHeight() {
        return THUMB_SIZE;
    }

    @Override
    protected float getThumbWidth() {
        return THUMB_SIZE;
    }
}

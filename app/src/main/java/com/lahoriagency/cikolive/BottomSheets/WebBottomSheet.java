package com.lahoriagency.cikolive.BottomSheets;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lahoriagency.cikolive.R;
import com.lahoriagency.cikolive.Utils.Const;


public class WebBottomSheet extends BottomSheetDialogFragment {
    String heading;
    View v;
    ImageView btnClose;
    TextView txtHeader;
    WebView webView;
    LinearLayout bottomSheetLinear;
    private CoordinatorLayout bottomSheetCoord;

    public WebBottomSheet(String heading) {
        this.heading = heading;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getDialog() != null) {

            getDialog().setOnShowListener(dialog -> {

                BottomSheetDialog d = (BottomSheetDialog) dialog;
                bottomSheetLinear = (LinearLayout) d.findViewById(R.id.bottomSheet_child);
                bottomSheetCoord = (CoordinatorLayout) d.findViewById(R.id.bottomSheet_Coord);
                if (bottomSheetLinear != null) {
                    BottomSheetBehavior.from(bottomSheetLinear)
                            .setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            });

        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = LayoutInflater.from(getActivity()).inflate(R.layout.bottomsheet_web, container, false);
        txtHeader = v.findViewById(R.id.tv_headingT);
        btnClose = v.findViewById(R.id.btn_closeT);
        webView = v.findViewById(R.id.webViewT);
        bottomSheetLinear = v.findViewById(R.id.bottomSheet_child);

        txtHeader.setText(heading);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(Const.URL);

        btnClose.setOnClickListener(view -> dismiss());
        return v;
    }



}

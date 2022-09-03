package com.lahoriagency.cikolive.BottomSheets;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lahoriagency.cikolive.R;


public class ReportBottomSheet extends BottomSheetDialogFragment {

    WebBottomSheet webBottomSheet;
    ImageView btnClose;
    TextView btnPrivacy;
    WebView webView;
    AppCompatButton btnReport;
    EditText et_reason,et_heart;
    View v;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getDialog() != null) {

            getDialog().setOnShowListener(dialog -> {

                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);

                // Right here!
                BottomSheetBehavior.from(bottomSheet)
                        .setState(BottomSheetBehavior.STATE_EXPANDED);
            });

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = LayoutInflater.from(getActivity()).inflate(R.layout.bottomsheet_report, container, false);
        btnReport = v.findViewById(R.id.btn_reportT);
        btnPrivacy = v.findViewById(R.id.btn_privacy);
        btnClose = v.findViewById(R.id.btn_closeW);
        et_reason = v.findViewById(R.id.et_reason);
        et_heart = v.findViewById(R.id.et_heart);
        webBottomSheet = new WebBottomSheet(getActivity().getResources().getString(R.string.privacy_policy));
        btnReport.setOnClickListener(view -> dismiss());
        btnClose.setOnClickListener(view -> dismiss());
        btnPrivacy.setOnClickListener(view -> webBottomSheet.show(getActivity().getSupportFragmentManager(), webBottomSheet.getClass().getSimpleName()));
        return v;
    }


}

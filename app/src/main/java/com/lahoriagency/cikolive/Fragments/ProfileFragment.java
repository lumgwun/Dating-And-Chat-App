package com.lahoriagency.cikolive.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import com.lahoriagency.cikolive.BottomSheets.WebBottomSheet;
import com.lahoriagency.cikolive.BuildConfig;
import com.lahoriagency.cikolive.CreateProfileActivity;
import com.lahoriagency.cikolive.HostMainActivity;
import com.lahoriagency.cikolive.PurchaseDiamondActivity;
import com.lahoriagency.cikolive.R;
import com.lahoriagency.cikolive.SavedProfileActivity;
import com.lahoriagency.cikolive.SplashActivity;
import com.lahoriagency.cikolive.Utils.Const;
import com.lahoriagency.cikolive.Utils.SessionManager;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class ProfileFragment extends Fragment {


    Dialog deleteDialog;
    Dialog dialogLogout;

    SessionManager sessionManager;
    WebBottomSheet webBottomSheet;
    boolean isHost;
    private TextView tvHost,txtLoc,txtChatterName;
    private ImageView btn_edit,btnMenu;
    private LinearLayout loutDeleteAccount,loutLogOut,loutWallet,loutPolicy,loutShare,loutTerms,loutHost,loutSavedProfiles;

    @Override
    public void onResume() {
        super.onResume();

        sessionManager = new SessionManager(getActivity());
        isHost = sessionManager.getBooleanValue(Const.IS_HOST);

        if (isHost) {

            tvHost.setText(getActivity().getResources().getString(R.string.host_dashboard));
        } else {

            tvHost.setText(getActivity().getResources().getString(R.string.be_a_host));

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_profile, container, false);

        loutWallet = rootView.findViewById(R.id.lout_chat_wallet);
        txtChatterName = rootView.findViewById(R.id.chatter_name);
        txtLoc = rootView.findViewById(R.id.chatter_location);
        btn_edit = rootView.findViewById(R.id.chat_btn_edit);
        loutSavedProfiles = rootView.findViewById(R.id.lout_user_saved_profiles);
        loutShare = rootView.findViewById(R.id.lout_share_chat);
        loutHost = rootView.findViewById(R.id.lout_host_man);
        loutTerms = rootView.findViewById(R.id.lout_terms_);
        loutPolicy = rootView.findViewById(R.id.lout_policy_);

        loutLogOut = rootView.findViewById(R.id.lout_logOut_user);
        loutDeleteAccount = rootView.findViewById(R.id.lout_delete_account33);
        btnMenu = rootView.findViewById(R.id.btn_menuuu);


        init();
        listeners();

        return rootView;
    }

    private void init() {
        isHost = sessionManager.getBooleanValue(Const.IS_HOST);

        sessionManager = new SessionManager(getActivity());
        if (isHost) {

            tvHost.setText(getActivity().getResources().getString(R.string.host_dashboard));
        } else {

            tvHost.setText(getActivity().getResources().getString(R.string.be_a_host));

        }

        createDeleteDialog();
        createLogoutDialog();

    }

    private void listeners() {
        loutSavedProfiles.setOnClickListener(view -> startActivity(new Intent(getActivity(), SavedProfileActivity.class)));
        loutShare.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });

        loutWallet.setOnClickListener(view -> startActivity(new Intent(getActivity(), PurchaseDiamondActivity.class)));

        loutDeleteAccount.setOnClickListener(view -> deleteDialog.show());
        loutLogOut.setOnClickListener(view -> dialogLogout.show());

        loutHost.setOnClickListener(view -> {
            isHost = sessionManager.getBooleanValue(Const.IS_HOST);

            if (isHost) {

                startActivity(new Intent(getActivity(), HostMainActivity.class));
            } else {

                startActivity(new Intent(getActivity(), CreateProfileActivity.class));
            }
        });


        loutTerms.setOnClickListener(view -> {
            webBottomSheet = new WebBottomSheet(getResources().getString(R.string.terms_of_use));
            if (!webBottomSheet.isAdded()) {
                webBottomSheet.show(getChildFragmentManager(), webBottomSheet.getClass().getSimpleName());
            }

        });

        loutPolicy.setOnClickListener(view -> {
            webBottomSheet = new WebBottomSheet(getResources().getString(R.string.privacy_policy));
            if (!webBottomSheet.isAdded()) {
                webBottomSheet.show(getChildFragmentManager(), webBottomSheet.getClass().getSimpleName());
            }


        });
    }


    private void createDeleteDialog() {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_delete_account, null);
        TextView btnCancel = v.findViewById(R.id.btn_cancel_delete);
        LinearLayout btnYes = v.findViewById(R.id.btn_delete_yes);
        deleteDialog = new Dialog(getActivity());
        deleteDialog.setContentView(v);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(deleteDialog.getWindow().getAttributes());
        layoutParams.height = WRAP_CONTENT;
        layoutParams.width = WRAP_CONTENT;

        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        deleteDialog.getWindow().setAttributes(layoutParams);
        deleteDialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;

        btnCancel.setOnClickListener(view1 -> deleteDialog.dismiss());
        btnYes.setOnClickListener(view1 -> {
            deleteDialog.dismiss();
            deleteAccount();

        });
    }

    private void createLogoutDialog() {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_log_out, null);
        dialogLogout = new Dialog(getActivity());
        dialogLogout.setContentView(v);
        TextView btnCancel = dialogLogout.findViewById(R.id.btn_cancel_Logout);
        LinearLayout btnYes = dialogLogout.findViewById(R.id.btn_yes_logOut);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogLogout.getWindow().getAttributes());
        layoutParams.height = WRAP_CONTENT;
        layoutParams.width = WRAP_CONTENT;

        dialogLogout.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialogLogout.getWindow().setAttributes(layoutParams);
        dialogLogout.getWindow().getAttributes().windowAnimations = R.style.animationdialog;

        btnCancel.setOnClickListener(view1 -> dialogLogout.dismiss());
        btnYes.setOnClickListener(view1 -> {
            dialogLogout.dismiss();
            deleteAccount();

        });
    }

    private void deleteAccount() {
        sessionManager.saveBooleanValue(Const.IS_HOST, false);
        sessionManager.saveBooleanValue(Const.IS_LOGGED_IN, false);
        startActivity(new Intent(getActivity(), SplashActivity.class));
        getActivity().finishAffinity();

    }


}
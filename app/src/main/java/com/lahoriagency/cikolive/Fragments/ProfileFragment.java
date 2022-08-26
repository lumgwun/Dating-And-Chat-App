package com.lahoriagency.cikolive.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.retry.dimdim.BuildConfig;
import com.retry.dimdim.R;
import com.retry.dimdim.activities.CreateProfileActivity;
import com.retry.dimdim.activities.HostMainActivity;
import com.retry.dimdim.activities.PurchaseDiamondActivity;
import com.retry.dimdim.activities.SavedProfileActivity;
import com.retry.dimdim.activities.SplashActivity;
import com.retry.dimdim.bottomsheets.WebBottomSheet;
import com.retry.dimdim.databinding.DialogDeleteAccountBinding;
import com.retry.dimdim.databinding.DialogLogOutBinding;
import com.retry.dimdim.databinding.FragmentProfileBinding;
import com.retry.dimdim.utils.Const;
import com.retry.dimdim.utils.SessionManager;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    Dialog deleteDialog;
    Dialog dialogLogout;
    DialogDeleteAccountBinding dialogDeleteBinding;
    DialogLogOutBinding dialogLogOutBinding;
    SessionManager sessionManager;
    WebBottomSheet webBottomSheet;
    boolean isHost;

    @Override
    public void onResume() {
        super.onResume();

        sessionManager = new SessionManager(getActivity());
        isHost = sessionManager.getBooleanValue(Const.IS_HOST);

        if (isHost) {

            binding.tvHost.setText(getActivity().getResources().getString(R.string.host_dashboard));
        } else {

            binding.tvHost.setText(getActivity().getResources().getString(R.string.be_a_host));

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        // Inflate the layout for this fragment
        init();
        listeners();

        return binding.getRoot();
    }

    private void init() {
        isHost = sessionManager.getBooleanValue(Const.IS_HOST);

        sessionManager = new SessionManager(getActivity());
        if (isHost) {

            binding.tvHost.setText(getActivity().getResources().getString(R.string.host_dashboard));
        } else {

            binding.tvHost.setText(getActivity().getResources().getString(R.string.be_a_host));

        }

        createDeleteDialog();
        createLogoutDialog();

    }

    private void listeners() {
        binding.loutSavedProfiles.setOnClickListener(view -> startActivity(new Intent(getActivity(), SavedProfileActivity.class)));
        binding.loutShare.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });

        binding.loutWallet.setOnClickListener(view -> startActivity(new Intent(getActivity(), PurchaseDiamondActivity.class)));

        binding.loutDeleteAccount.setOnClickListener(view -> deleteDialog.show());
        binding.loutLogOut.setOnClickListener(view -> dialogLogout.show());

        binding.loutHost.setOnClickListener(view -> {
            isHost = sessionManager.getBooleanValue(Const.IS_HOST);

            if (isHost) {

                startActivity(new Intent(getActivity(), HostMainActivity.class));
            } else {

                startActivity(new Intent(getActivity(), CreateProfileActivity.class));
            }
        });


        binding.loutTerms.setOnClickListener(view -> {
            webBottomSheet = new WebBottomSheet(getResources().getString(R.string.terms_of_use));
            if (!webBottomSheet.isAdded()) {
                webBottomSheet.show(getChildFragmentManager(), webBottomSheet.getClass().getSimpleName());
            }

        });

        binding.loutPolicy.setOnClickListener(view -> {
            webBottomSheet = new WebBottomSheet(getResources().getString(R.string.privacy_policy));
            if (!webBottomSheet.isAdded()) {
                webBottomSheet.show(getChildFragmentManager(), webBottomSheet.getClass().getSimpleName());
            }


        });
    }


    private void createDeleteDialog() {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_delete_account, null);
        dialogDeleteBinding = DataBindingUtil.bind(v);
        deleteDialog = new Dialog(getActivity());
        deleteDialog.setContentView(v);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(deleteDialog.getWindow().getAttributes());
        layoutParams.height = WRAP_CONTENT;
        layoutParams.width = WRAP_CONTENT;

        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        deleteDialog.getWindow().setAttributes(layoutParams);
        deleteDialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;

        dialogDeleteBinding.btnCancel.setOnClickListener(view1 -> deleteDialog.dismiss());
        dialogDeleteBinding.btnYes.setOnClickListener(view1 -> {
            deleteDialog.dismiss();
            deleteAccount();

        });
    }

    private void createLogoutDialog() {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_log_out, null);
        dialogLogOutBinding = DataBindingUtil.bind(v);
        dialogLogout = new Dialog(getActivity());
        dialogLogout.setContentView(v);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogLogout.getWindow().getAttributes());
        layoutParams.height = WRAP_CONTENT;
        layoutParams.width = WRAP_CONTENT;

        dialogLogout.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialogLogout.getWindow().setAttributes(layoutParams);
        dialogLogout.getWindow().getAttributes().windowAnimations = R.style.animationdialog;

        dialogLogOutBinding.btnCancel.setOnClickListener(view1 -> dialogLogout.dismiss());
        dialogLogOutBinding.btnYes.setOnClickListener(view1 -> {
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
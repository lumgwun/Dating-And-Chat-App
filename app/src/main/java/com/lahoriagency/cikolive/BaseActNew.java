package com.lahoriagency.cikolive;

import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.lahoriagency.cikolive.Classes.CityName;
import com.lahoriagency.cikolive.Classes.Comments;
import com.lahoriagency.cikolive.Classes.DiamondTransfer;
import com.lahoriagency.cikolive.Classes.ErrorUtils;
import com.lahoriagency.cikolive.Classes.Gift;
import com.lahoriagency.cikolive.Classes.Notification;
import com.lahoriagency.cikolive.Classes.PurchaseDiamond;
import com.lahoriagency.cikolive.Classes.RedeemRequest;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Interfaces.Consts;
import com.lahoriagency.cikolive.NewPackage.App;
import com.lahoriagency.cikolive.NewPackage.DialogUtils;
import com.lahoriagency.cikolive.NewPackage.GooglePlayServicesHelper;
import com.lahoriagency.cikolive.NewPackage.QBResRequestExecutor;
import com.lahoriagency.cikolive.NewPackage.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

import eightbitlab.com.blurview.BlurAlgorithm;

public abstract class BaseActNew extends CoreBaseActivity {
    protected ActionBar actionBar;
    private static final String TAG = BaseActNew.class.getSimpleName();
    protected ProgressDialog progressDialog;
    protected GooglePlayServicesHelper googlePlayServicesHelper;
    protected QBResRequestExecutor requestExecutor;
    SharedPrefsHelper sharedPrefsHelper;

    private static String DUMMY_VALUE = "dummy_value";

    private BlurAlgorithm blurAlgorithm;
    public final List<String> listOfImages = new ArrayList<>();
    public final List<Notification> listOfNotification = new ArrayList<>();
    public final List<PurchaseDiamond> listOfPurchaseDiamond = new ArrayList<>();
    public final List<Gift> giftList = new ArrayList<>();
    public final List<RedeemRequest> listOfRedeems = new ArrayList<>();
    public final List<DiamondTransfer> listOfDiamondTransfer = new ArrayList<>();
    public final List<CityName> cityList = new ArrayList<>();
    public final List<SavedProfile> savedProfileList = new ArrayList<>();
    public final List<Comments> commentsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_base_act_new);
        sharedPrefsHelper= new SharedPrefsHelper();
        actionBar = getSupportActionBar();
        progressDialog = DialogUtils.getProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        requestExecutor = App.getQbResRequestExecutor();
        sharedPrefsHelper = SharedPrefsHelper.getInstance();
        googlePlayServicesHelper = new GooglePlayServicesHelper();

    }

    protected Snackbar showErrorSnackbar(@StringRes int resId, Exception e,
                                         View.OnClickListener clickListener) {
        return ErrorUtils.showSnackbar(getSnackbarAnchorView(), resId, e,
                R.string.dlg_retry, clickListener);

    }
    protected abstract void initUI();

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt("dummy_value", 0);
        super.onSaveInstanceState(outState, outPersistentState);
    }


    protected abstract View getSnackbarAnchorView();


    public void initDefaultActionBar() {
        String currentUserFullName = "";
        String currentRoomName = sharedPrefsHelper.get(Consts.PREF_CURREN_ROOM_NAME, "");

        if (sharedPrefsHelper.getQbUser() != null) {
            currentUserFullName = sharedPrefsHelper.getQbUser().getFullName();
        }

        setActionBarTitle(currentRoomName);
        setActionbarSubTitle(String.format(getString(R.string.subtitle_text_logged_in_as), currentUserFullName));
    }

    public void setActionbarSubTitle(String subTitle) {
        if (actionBar != null)
            actionBar.setSubtitle(subTitle);
    }

    public void removeActionbarSubTitle() {
        if (actionBar != null)
            actionBar.setSubtitle(null);
    }

    void showProgressDialog(@StringRes int messageId) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            // Disable the back button
            DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            };
            progressDialog.setOnKeyListener(keyListener);
        }

        progressDialog.setMessage(getString(messageId));

        progressDialog.show();

    }

    void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
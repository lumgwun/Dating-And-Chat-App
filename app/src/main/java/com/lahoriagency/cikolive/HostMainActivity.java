package com.lahoriagency.cikolive;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.lahoriagency.cikolive.Classes.Diamond;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Fragments.HostChatsFragment;
import com.lahoriagency.cikolive.Fragments.TestFragment;
import com.lahoriagency.cikolive.NewPackage.ChatMatchAct;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.users.model.QBUser;

import java.text.MessageFormat;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_ACCT_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_APP_ID;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_AUTH_KEY;
import static com.lahoriagency.cikolive.BuildConfig.QUICKBLOX_SECRET_KEY;
import static com.lahoriagency.cikolive.Classes.Consts.EXTRA_DIALOG_ID;


@SuppressWarnings("deprecation")
public class HostMainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private AppCompatButton btnStart;
    private TextView btnCancel;
    private Dialog dialogLive;
    private LinearLayout btnGoLive;
    private Button btnRedeem;
    private FloatingActionButton fab;
    SharedPreferences sharedPref;
    Bundle userExtras;
    private SavedProfile savedProfile;
    private static final String PREF_NAME = "Ciko";
    Gson gson, gson1,gson2;
    String json, json1, json2;
    private QBUser qbUser;
    private SwitchCompat switchAvailOrNot;
    private ImageView btnBack,btnMenu;
    private TextView tv_count,tv_min,tv_total,tv_Wallet;
    private AppCompatButton btnPending,btnHistory,btnRedeemReq;
    int diamondCount,diamondID;
    private int collections;
    private Diamond diamond;
    private AppCompatButton btnStartLive,btnTransferToWallet;
    private static final String APPLICATION_ID = QUICKBLOX_APP_ID;   //QUICKBLOX_APP_ID
    private static final String AUTH_KEY = QUICKBLOX_AUTH_KEY;
    private static final String AUTH_SECRET = QUICKBLOX_SECRET_KEY;
    private static final String ACCOUNT_KEY = QUICKBLOX_ACCT_KEY;
    private static final String SERVER_URL = "";
    public static final String userId = "userId";
    private  int qbUserID,qbUserFieldID;
    static Boolean isTouched = false;
    private EditText edtLiveAmount;
    private String strLiveAmount;
    private double liveAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_host_main);
        setTitle("Host Activity");
        QBSettings.getInstance().init(this, APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        savedProfile= new SavedProfile();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frag_Host, new HostChatsFragment());
        fragmentTransaction.commit();
        gson= new Gson();
        gson1= new Gson();
        gson2= new Gson();
        qbUser= new QBUser();
        diamond= new Diamond();
        sharedPref= getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        json = sharedPref.getString("LastSavedProfileUsed", "");
        savedProfile = gson.fromJson(json, SavedProfile.class);
        json1 = sharedPref.getString("LastQBUserUsed", "");
        qbUser = gson1.fromJson(json1, QBUser.class);
        tv_count =findViewById(R.id.tv_count);
        edtLiveAmount =findViewById(R.id.liveAmount);
        btnTransferToWallet =findViewById(R.id.diamond_transfer);

        if(savedProfile !=null){
            diamond=savedProfile.getSavedPDiamond();

        }
        if(qbUser !=null){
            qbUserFieldID=qbUser.getFileId();
            qbUserID=qbUser.getFileId();

        }
        if(diamond !=null){
            diamondCount=diamond.getDiamondCount();
            diamondID=diamond.getDiamondWalletID();
            collections=diamond.getDiamondCollections();

        }


        tv_min =findViewById(R.id.tv_min);
        tv_Wallet =findViewById(R.id.tv_WalletD);
        tv_total =findViewById(R.id.tv_colectiont);
        btnPending =findViewById(R.id.btn_pending);
        btnMenu =findViewById(R.id.btn_menu);
        tv_Wallet.setText(MessageFormat.format("Wallet ID:{0}Diamonds:{1}"+""+ diamondID, diamondCount));
        tv_total.setText(MessageFormat.format("{0}", collections));
        btnBack =findViewById(R.id.btn_backa);
        btnCancel =findViewById(R.id.btn_cancelLive);

        btnStartLive =findViewById(R.id.btn_startLiveT);
        btnGoLive =findViewById(R.id.btn_goLive);
        btnHistory =findViewById(R.id.diamond_History);
        btnRedeem =findViewById(R.id.btn_redeemT);
        btnRedeemReq =findViewById(R.id.redeem_Btn_Request);
        switchAvailOrNot =findViewById(R.id.avail_for_call);
        Bundle bundle= new Bundle();
        btnTransferToWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diamondCount=diamondCount+collections;
                collections=0;
                diamond.setDiamondCount(diamondCount);
                diamond.setDiamondCollections(collections);
            }
        });
        btnTransferToWallet.setOnClickListener(this::transferDToWallet);

        btnStartLive.setOnClickListener(this::doStartLive);

        btnStartLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strLiveAmount=edtLiveAmount.getText().toString();
                if(strLiveAmount !=null){
                    liveAmt= Double.parseDouble(strLiveAmount);
                }
                bundle.putParcelable("QBUser", (Parcelable) qbUser);
                bundle.putParcelable("SavedProfile",savedProfile);
                bundle.putDouble("LiveAmount",liveAmt);
                bundle.putInt("LiveDuration",25);

                Intent myIntent = new Intent(HostMainActivity.this, ChatMatchAct.class);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myIntent.putExtra(EXTRA_DIALOG_ID,userId);
                myIntent.putExtra(userId,EXTRA_DIALOG_ID);
                myIntent.putExtra(userId,qbUserFieldID);
                myIntent.putExtra(userId,qbUserID);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.putExtras(bundle);
                startActivity(myIntent);

            }
        });
        switchAvailOrNot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isTouched) {
                    isTouched = false;
                    if (isChecked) {

                    }
                    else {
                    }
                }
            }
        });
        btnCancel.setOnClickListener(this::cancelLive);

        btnPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnGoLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialogLive.show();
                createDialog();
            }
        });
        btnRedeemReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle= new Bundle();
                bundle.putParcelable("SavedProfile",savedProfile);
                bundle.putParcelable("QBUser", (Parcelable) qbUser);
                Intent intent = new Intent(HostMainActivity.this, RedeemRequestActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
        btnRedeemReq.setOnClickListener(this::goToRedeemReq);
        btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle= new Bundle();
                bundle.putParcelable("SavedProfile",savedProfile);
                bundle.putParcelable("QBUser", (Parcelable) qbUser);
                Intent intent = new Intent(HostMainActivity.this, SubmitRedeemActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
        btnRedeem.setOnClickListener(this::redeemDo);
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle= new Bundle();
                bundle.putParcelable("SavedProfile",savedProfile);
                bundle.putParcelable("QBUser", (Parcelable) qbUser);
                Intent intent = new Intent(HostMainActivity.this, DiamondHistoryAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
        btnHistory.setOnClickListener(this::getDiamondHistory);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
    private void createDialog() {

        View v = LayoutInflater.from(this).inflate(R.layout.dialog_start_live_, null);
        //dialogLive = new Dialog(this);
        //dialogLive.setContentView(v);
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this,R.style.Theme_Design_BottomSheetDialog);

        ImageView closeBottomLayout = v.findViewById(R.id.can_bottom_close);
        btnCancel =findViewById(R.id.btn_cancelLive);
        //final Dialog mBottomSheetDialog = new Dialog (context, R.style.Theme_Design_BottomSheetDialog);
        mBottomSheetDialog.setContentView (v);
        mBottomSheetDialog.setCancelable (true);
        mBottomSheetDialog.getWindow ().setLayout (MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(mBottomSheetDialog.getWindow().getAttributes());
        layoutParams.height = WRAP_CONTENT;
        layoutParams.width = MATCH_PARENT;

        mBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        mBottomSheetDialog.getWindow().setAttributes(layoutParams);
        mBottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        mBottomSheetDialog.show ();

        btnCancel.setOnClickListener(view1 -> mBottomSheetDialog.dismiss());
        /*btnStart.setOnClickListener(view1 -> {
            //mBottomSheetDialog.show();
            startActivity(new Intent(this, ChatActivity.class));

        });*/


    }
    private void createDeleteDialog() {

        View v = LayoutInflater.from(this).inflate(R.layout.dialog_start_live_, null);
        dialogLive = new Dialog(this);
        dialogLive.setContentView(v);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogLive.getWindow().getAttributes());
        layoutParams.height = WRAP_CONTENT;
        layoutParams.width = WRAP_CONTENT;

        dialogLive.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialogLive.getWindow().setAttributes(layoutParams);
        dialogLive.getWindow().getAttributes().windowAnimations = R.style.animationdialog;

        btnCancel.setOnClickListener(view1 -> dialogLive.dismiss());
        btnStart.setOnClickListener(view1 -> {
            dialogLive.show();
            startActivity(new Intent(this, ChatMatchAct.class));

        });
    }

    public void goToRedeemReq(View view) {
    }

    public void getDiamondHistory(View view) {
    }

    public void redeemDo(View view) {
    }

    public void cancelLive(View view) {
    }

    public void doStartLive(View view) {
    }

    public void transferDToWallet(View view) {
    }
}
package com.lahoriagency.cikolive;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.lahoriagency.cikolive.Adapters.DiamondShopAdapter;
import com.lahoriagency.cikolive.BottomSheets.WebBottomSheet;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class DiamondShopBottomSheet extends BottomSheetDialog {
    DiamondShopAdapter diamondShopAdapter;

    BaseActivity baseActivity;

    OnDismiss onDismiss;
    private RecyclerView recyclerViewDiamond;
    private TextView txtTerms,txtPolicy;

    public DiamondShopBottomSheet(@NotNull Context context) {
        super(context);
    }


    public OnDismiss getOnDismiss() {
        return onDismiss;
    }

    public void setOnDismiss(OnDismiss onDismiss) {
        this.onDismiss = onDismiss;
    }

    WebBottomSheet webBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_diamond_shop_bottom_sheet);
        recyclerViewDiamond = findViewById(R.id.rv_diamond);
        txtTerms = findViewById(R.id.txtTerms);
        txtPolicy = findViewById(R.id.tv_privacyPolicy);

        baseActivity = new BaseActivity();
        baseActivity.makeListOfDiamonds();
        diamondShopAdapter = new DiamondShopAdapter();
        recyclerViewDiamond.setAdapter(diamondShopAdapter);
        baseActivity.listOfPurchaseDiamond.addAll(Collections.unmodifiableList(baseActivity.listOfPurchaseDiamond));
        diamondShopAdapter.updateItems(baseActivity.listOfPurchaseDiamond);
        txtPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webBottomSheet = new WebBottomSheet(baseActivity.getString(R.string.privacy_policy));
                if (!webBottomSheet.isAdded()) {
                    //txtTerms.setText(R.string.terms_of_use);
                    webBottomSheet.show(baseActivity.getSupportFragmentManager(), webBottomSheet.getClass().getSimpleName());
                }


            }
        });
        txtTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webBottomSheet = new WebBottomSheet(baseActivity.getString(R.string.terms_of_use));
                if (!webBottomSheet.isAdded()) {
                    //txtPolicy.setText(R.string.privacy_policy);
                    webBottomSheet.show(baseActivity.getSupportFragmentManager(), webBottomSheet.getClass().getSimpleName());
                }

            }
        });
    }

    public boolean isAdded() {
        return false;
    }



    public interface OnDismiss {
        void onDismiss();
    }
}
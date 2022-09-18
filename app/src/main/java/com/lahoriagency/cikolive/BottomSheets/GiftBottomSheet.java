package com.lahoriagency.cikolive.BottomSheets;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lahoriagency.cikolive.Adapters.GiftAdapter;
import com.lahoriagency.cikolive.BaseActDiamond;
import com.lahoriagency.cikolive.DiamondShopBottomSheet;
import com.lahoriagency.cikolive.R;


import java.util.Collections;

public class GiftBottomSheet extends BottomSheetDialogFragment {

    GiftAdapter giftAdapter;
    BaseActDiamond baseActivity;

    OnDismiss onDismiss;
    private ImageView imgTime;
    private TextView txtWelcome;
    private TextView txtMessage;
    private Button btnAddAccount;
    private RecyclerView rvGift;
    private RecyclerView recyclerViewDiamond;
    private LinearLayout btnAddDiamonds;
    AppCompatButton btn_price;

    public OnDismiss getOnDismiss() {
        return onDismiss;
    }

    public void setOnDismiss(OnDismiss onDismiss) {
        this.onDismiss = onDismiss;
    }

    DiamondShopBottomSheet diamondShopBottomSheet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.bottonsheet_gift, container, false);
        rvGift = rootView.findViewById(R.id.rv_gift);
        btnAddDiamonds = rootView.findViewById(R.id.btn_add_diamonds);
        recyclerViewDiamond = rootView.findViewById(R.id.rv_diamond);
        diamondShopBottomSheet = new DiamondShopBottomSheet(getContext());
        baseActivity = new BaseActDiamond();
        baseActivity.makeListOfGift();
        giftAdapter = new GiftAdapter();
        rvGift.setAdapter(giftAdapter);
        baseActivity.giftList.addAll(Collections.unmodifiableList(baseActivity.giftList));
        baseActivity.giftList.addAll(Collections.unmodifiableList(baseActivity.giftList));
        giftAdapter.updateItems(baseActivity.giftList);
        listeners(rootView);

        return rootView;
    }

    private void listeners(View rootView) {

        btnAddDiamonds.setOnClickListener(view -> {

            if (!diamondShopBottomSheet.isAdded()) {
                rootView.setVisibility(View.GONE);
                diamondShopBottomSheet.show();

            }else {
                rootView.setVisibility(View.VISIBLE);

            }
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        onDismiss.onDismiss();
    }

    public interface OnDismiss {
        void onDismiss();
    }


}

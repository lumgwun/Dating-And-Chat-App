package com.lahoriagency.cikolive.BottomSheets;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lahoriagency.cikolive.Adapters.CityNameAdapter;
import com.lahoriagency.cikolive.BaseActivity;
import com.lahoriagency.cikolive.Classes.CityName;
import com.lahoriagency.cikolive.R;


import java.util.ArrayList;
import java.util.List;

public class CityBottomSheet extends BottomSheetDialogFragment {

    CityNameAdapter.OnItemClick onItemClick;
    BaseActivity baseActivity;

    public CityNameAdapter.OnItemClick getOnItemClick() {
        return onItemClick;
    }


    public void setOnItemClick(CityNameAdapter.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    List<CityName> searchList = new ArrayList<>();
    CityNameAdapter cityNameAdapter;
    String keyWord;
    private RecyclerView rvCity;
    private ImageView btnClose2,btnClose1,btnSearch1;
    private EditText etSearch;
    private TextView tv_languages;
    private RelativeLayout lout1,lout2;

    @Override
    public void onResume() {
        super.onResume();

        baseActivity.makeListOfCity();
        cityNameAdapter.updateItems(baseActivity.cityList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.bottomsheet_city, container, false);

        tv_languages = rootView.findViewById(R.id.tv_languages);
        rvCity = rootView.findViewById(R.id.rv_cityB);
        lout1 = rootView.findViewById(R.id.lout_1);
        lout2 = rootView.findViewById(R.id.lout_2);
        etSearch = rootView.findViewById(R.id.et_search);
        btnSearch1 = rootView.findViewById(R.id.btn_search1);
        btnClose2 = rootView.findViewById(R.id.btn_close2);
        btnClose1 = rootView.findViewById(R.id.btn_close1);
        rootView.setNestedScrollingEnabled(true);
        init();
        listeners();

        return rootView;



    }


    private void init() {

        baseActivity = new BaseActivity();
        lout1.setVisibility(View.VISIBLE);
        lout2.setVisibility(View.GONE);
        cityNameAdapter = new CityNameAdapter();
        rvCity.setAdapter(cityNameAdapter);

    }

    private void listeners() {


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
            }

            @Override
            public void afterTextChanged(Editable s) {
                keyWord = s.toString();

                if (keyWord.isEmpty()) {
                    cityNameAdapter.updateItems(baseActivity.cityList);
                } else {
                    searchCity();
                }


            }
        });

        btnSearch1.setOnClickListener(v -> {


            lout1.setVisibility(View.GONE);
            lout2.setVisibility(View.VISIBLE);


        });
        btnClose2.setOnClickListener(v -> {
            lout1.setVisibility(View.VISIBLE);
            lout2.setVisibility(View.GONE);

        });

        btnClose1.setOnClickListener(v -> dismiss());
        cityNameAdapter.setOnItemClick(name -> onItemClick.onClick(name));

    }

    private void searchCity() {

        searchList.clear();

        for (int i = 0; i < baseActivity.cityList.size(); i++) {

            if (baseActivity.cityList.get(i).getName().toLowerCase().contains(keyWord)) {
                CityName model = new CityName();
                model.setName(baseActivity.cityList.get(i).getName());
                model.setId(baseActivity.cityList.get(i).getId());
                searchList.add(model);
            }
        }
        cityNameAdapter.updateItems(searchList);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getDialog() != null) {

            getDialog().setOnShowListener(dialog -> {

                BottomSheetDialog d = (BottomSheetDialog) dialog;

                // This is gotten directly from the source of BottomSheetDialog
                // in the wrapInBottomSheet() method
                FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);

                // Right here!
                BottomSheetBehavior.from(bottomSheet)
                        .setState(BottomSheetBehavior.STATE_EXPANDED);
            });

        }
    }
}

package com.lahoriagency.cikolive.BottomSheets;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.retry.dimdim.R;
import com.retry.dimdim.activities.BaseActivity;
import com.retry.dimdim.adapters.CityNameAdapter;
import com.retry.dimdim.databinding.BottomsheetCityBinding;
import com.retry.dimdim.modals.CityName;

import java.util.ArrayList;
import java.util.List;

public class CityBottomSheet extends BottomSheetDialogFragment {

    CityNameAdapter.OnItemClick onItemClick;
    BaseActivity baseActivity;

    public CityNameAdapter.OnItemClick getOnItemClick() {
        return onItemClick;
    }

    BottomsheetCityBinding binding;

    public void setOnItemClick(CityNameAdapter.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    List<CityName> searchList = new ArrayList<>();
    CityNameAdapter cityNameAdapter;
    String keyWord;

    @Override
    public void onResume() {
        super.onResume();

        baseActivity.makeListOfCity();
        cityNameAdapter.updateItems(baseActivity.cityList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.bottomsheet_city, container, false);

        binding = DataBindingUtil.bind(v);
        binding.getRoot().setNestedScrollingEnabled(true);

        init();
        listeners();


        return binding.getRoot();
    }


    private void init() {

        baseActivity = new BaseActivity();
        binding.lout1.setVisibility(View.VISIBLE);
        binding.lout2.setVisibility(View.GONE);
        cityNameAdapter = new CityNameAdapter();
        binding.rvCity.setAdapter(cityNameAdapter);

    }

    private void listeners() {


        binding.etSearch.addTextChangedListener(new TextWatcher() {
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

        binding.btnSearch1.setOnClickListener(v -> {


            binding.lout1.setVisibility(View.GONE);
            binding.lout2.setVisibility(View.VISIBLE);


        });
        binding.btnClose2.setOnClickListener(v -> {

            binding.lout1.setVisibility(View.VISIBLE);
            binding.lout2.setVisibility(View.GONE);

        });

        binding.btnClose1.setOnClickListener(v -> dismiss());
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

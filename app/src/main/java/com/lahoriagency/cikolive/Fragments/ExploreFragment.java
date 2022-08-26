package com.lahoriagency.cikolive.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.databinding.ObservableInt;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.lahoriagency.cikolive.Adapters.ViewPagerExploreAdapter;
import com.lahoriagency.cikolive.BottomSheets.CityBottomSheet;
import com.lahoriagency.cikolive.PurchaseDiamondActivity;
import com.lahoriagency.cikolive.R;
import com.lahoriagency.cikolive.Utils.Const;
import com.lahoriagency.cikolive.Utils.SessionManager;


public class ExploreFragment extends Fragment {
    SessionManager sessionManager;
    CityBottomSheet cityBottomSheet;
    private TextView tvCountryName,btnTreasure;
    private TextView btnLiveStreams,btnProfiles;
    private ViewPager2 viewPager;
    private LinearLayoutCompat loutCountry;
    ObservableInt currentPosition = new ObservableInt(0);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_explore, container, false);
        init();
        listeners();
        return rootView;


    }

    private void listeners() {

        /*cityBottomSheet.setOnItemClick(name -> {
            cityBottomSheet.dismiss();
            tvCountryName.setText(name);
        });*/
        loutCountry.setOnClickListener(view -> {

            if (!cityBottomSheet.isAdded()) {

                cityBottomSheet.show(getChildFragmentManager(), cityBottomSheet.getClass().getSimpleName());
            }


        });
        btnProfiles.setOnClickListener(view -> {

            viewPager.setCurrentItem(0);
            currentPosition.set(0);
        });
        btnLiveStreams.setOnClickListener(view -> {

            viewPager.setCurrentItem(1);
            currentPosition.set(1);

        });

        btnTreasure.setOnClickListener(view ->
                startActivity(new Intent(getActivity(), PurchaseDiamondActivity.class)));

    }

    private void init() {
        sessionManager = new SessionManager(getActivity());

        sessionManager.saveIntValue(Const.COUNTRY_ID, 0);


        cityBottomSheet = new CityBottomSheet();
        viewPager.setAdapter(new ViewPagerExploreAdapter(getChildFragmentManager(), getLifecycle()));
        viewPager.setUserInputEnabled(true);
        //setCurrentPosition(currentPosition);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition.set(position);
            }
        });

    }


}
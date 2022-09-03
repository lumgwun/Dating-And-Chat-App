package com.lahoriagency.cikolive.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.lahoriagency.cikolive.Adapters.ExploreProfileAdapter;
import com.lahoriagency.cikolive.Classes.ModelItem;
import com.lahoriagency.cikolive.R;
import com.lahoriagency.cikolive.Utils.SampleData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ExploreLiveStreamsFragment extends Fragment {

    ExploreProfileAdapter profileAdapter;
    List<ModelItem> proList = new ArrayList<>();
    private RecyclerView rvProfiles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_explore_live_streams, container, false);

        rvProfiles = rootView.findViewById(R.id.rv_profilesAll);
        makeList();
        init();

        return rootView;



    }




    private void makeList() {

        ModelItem item1 = new ModelItem();
        ModelItem item2 = new ModelItem();
        ModelItem item3 = new ModelItem();
        ModelItem item4 = new ModelItem();
        ModelItem item5 = new ModelItem();
        ModelItem item6 = new ModelItem();


        item1.setActorName("Mimi Brown");
        item1.setDiamond(95);
        item1.setLocation("Delhi");
        item1.setActorImage(SampleData.getModelImagePath(5));
        item1.setAge(24);

        item2.setActorName("Rosy Jones");
        item2.setDiamond(95);
        item2.setLocation("Mumbai");
        item2.setActorImage(SampleData.getModelImagePath(4));
        item2.setAge(23);

        item3.setActorName("Mimi Brown");
        item3.setDiamond(95);
        item3.setLocation("Vapi");
        item3.setActorImage(SampleData.getModelImagePath(3));
        item3.setAge(22);

        item4.setActorName("Rosy Jones");
        item4.setDiamond(95);
        item4.setLocation("Amreli");
        item4.setActorImage(SampleData.getModelImagePath(2));
        item4.setAge(34);

        item5.setActorName("Peema Ashi");
        item5.setDiamond(95);
        item5.setLocation("London");
        item5.setActorImage(SampleData.getModelImagePath(1));
        item5.setAge(14);

        item6.setActorName("Peema Ashi");
        item6.setDiamond(95);
        item6.setLocation("NewYork");
        item6.setActorImage(SampleData.getModelImagePath(0));
        item6.setAge(34);

        proList.add(item1);
        proList.add(item2);
        proList.add(item3);
        proList.add(item4);
        proList.add(item5);
        proList.add(item6);
    }

    public void init() {
        profileAdapter = new ExploreProfileAdapter(2);
        rvProfiles.setAdapter(profileAdapter);
        proList.addAll(Collections.unmodifiableList(proList));
        profileAdapter.updateItems(proList);
    }


}
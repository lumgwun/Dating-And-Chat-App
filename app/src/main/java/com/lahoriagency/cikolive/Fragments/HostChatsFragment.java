package com.lahoriagency.cikolive.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lahoriagency.cikolive.Adapters.ChatsAdapter;
import com.lahoriagency.cikolive.ChattingActivity;
import com.lahoriagency.cikolive.Classes.ChatsItem;
import com.lahoriagency.cikolive.R;
import com.lahoriagency.cikolive.Utils.SampleData;

import java.util.ArrayList;
import java.util.List;


public class HostChatsFragment extends Fragment {

    List<ChatsItem> chatsList = new ArrayList<>();

    ChatsAdapter chatsAdapter;
    private RecyclerView rvChats;
    private ImageButton btnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_host_chats, container, false);
        rvChats = rootView.findViewById(R.id.r_host_chats);
        btnBack = rootView.findViewById(R.id.btn_back_From_Host);
        createChatsList();
        init();
        listeners();
        return rootView;


    }

    private void createChatsList() {

        ChatsItem chatsItem1 = new ChatsItem();
        ChatsItem chatsItem2 = new ChatsItem();
        ChatsItem chatsItem3 = new ChatsItem();
        ChatsItem chatsItem4 = new ChatsItem();


        chatsItem1.setName("Jhonson Smith 24555555555555555555555555555555");
        chatsItem1.setMessage("Hey, Can we meet today at LackView.I will be Waiting");
        chatsItem1.setTime("5 min");
        chatsItem1.setImage(SampleData.getModelImagePath(1));

        chatsItem2.setName("Jhonson Smith 24");
        chatsItem2.setMessage("You: Hey, Cutie, What are you doing?");
        chatsItem2.setTime("25 min");
        chatsItem2.setImage(SampleData.getModelImagePath(2));

        chatsItem3.setName("Mikal Brown 20");
        chatsItem3.setMessage("You: Where do you live man?");
        chatsItem3.setTime("Tuesday");
        chatsItem3.setImage(SampleData.getModelImagePath(3));


        chatsItem4.setName("Jhonson Smith 24");
        chatsItem4.setMessage("Hey, Can we meet today at LackView.I will be Waiting");
        chatsItem4.setTime("5 Sep 2021");
        chatsItem4.setImage(SampleData.getModelImagePath(4));

        chatsList.add(chatsItem1);
        chatsList.add(chatsItem2);
        chatsList.add(chatsItem3);
        chatsList.add(chatsItem4);


    }

    private void listeners() {

        btnBack.setOnClickListener(view -> getActivity().onBackPressed());
        chatsAdapter.setOnItemClick(() -> startActivity(new Intent(getActivity(), ChattingActivity.class)));

    }

    private void init() {
        rvChats.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        chatsAdapter = new ChatsAdapter();
        rvChats.setAdapter(chatsAdapter);
        rvChats.setItemAnimator(new DefaultItemAnimator());
        chatsAdapter.updateItems(chatsList);


    }
}
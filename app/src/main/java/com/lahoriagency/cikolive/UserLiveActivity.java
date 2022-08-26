package com.lahoriagency.cikolive;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lahoriagency.cikolive.Adapters.CommentAdapter;
import com.lahoriagency.cikolive.BottomSheets.GiftBottomSheet;
import com.lahoriagency.cikolive.Classes.Comments;

import java.util.ArrayList;

public class UserLiveActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    DiamondShopBottomSheet diamondShopBottomSheet;
    GiftBottomSheet giftBottomSheet;

    CommentAdapter commentsAdapter;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private LinearLayout layoutCompatD,loutBottom,lout_rv,lout_2;
    private ImageView btn_gift,btnSend,btn_exit,btn_menu,img_profile;
    private RecyclerView recyclerView;
    private TextView view_count,tv_location,tv_age,tv_name,tv_diamond_rate,tv_time,tv_diamond_you_have;
    private EditText editTextComment;
    private ArrayList<Comments> commentsArrayList;
    private  Comments comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_user_live2);
        setTitle("Live Chats");
        fab = findViewById(R.id.fabLive);
        comments= new Comments();
        commentsArrayList= new ArrayList<>();
        loutBottom = findViewById(R.id.lout_bottom);
        layoutCompatD = findViewById(R.id.btn_add_diamonds);
        btn_gift = findViewById(R.id.btn_gift);
        btnSend = findViewById(R.id.btn_send);
        recyclerView = findViewById(R.id.rv_comment);
        lout_rv = findViewById(R.id.lout_rv);
        btn_exit = findViewById(R.id.btn_exit);
        view_count = findViewById(R.id.view_count);
        lout_2 = findViewById(R.id.lout_2);
        btn_menu = findViewById(R.id.btn_menu);
        tv_location = findViewById(R.id.tv_location);
        tv_diamond_rate = findViewById(R.id.tv_diamond_rate);
        tv_name = findViewById(R.id.tv_name);
        img_profile = findViewById(R.id.img_profile);
        tv_age = findViewById(R.id.tv_age);
        tv_time = findViewById(R.id.tv_time);
        editTextComment = findViewById(R.id.et_massage);
        tv_diamond_you_have = findViewById(R.id.tv_diamond_you_have);
        tv_time = findViewById(R.id.tv_time);
        transparentStatusBar();
        makeListOfComments();
        layoutCompatD.setOnClickListener(view -> {
            loutBottom.setVisibility(View.GONE);
            lout_rv.setVisibility(View.GONE);
            if (!diamondShopBottomSheet.isAdded()) {
                diamondShopBottomSheet.show(getSupportFragmentManager(), diamondShopBottomSheet.getClass().getSimpleName());

            }
        });

        btn_exit.setOnClickListener(view -> onBackPressed());

        btnSend.setOnClickListener(view -> {
            editTextComment.clearFocus();
            editTextComment.setText("");

        });

        btn_gift.setOnClickListener(view -> {
            loutBottom.setVisibility(View.GONE);
            lout_rv.setVisibility(View.GONE);

            if (!giftBottomSheet.isAdded()) {
                giftBottomSheet.show(getSupportFragmentManager(), giftBottomSheet.getClass().getSimpleName());

            }


        });
        commentsAdapter = new CommentAdapter();
        recyclerView.setAdapter(commentsAdapter);
        commentsAdapter.updateItems(comments);
        Log.i("TAG", "init: " + commentsArrayList.size());


        diamondShopBottomSheet = new DiamondShopBottomSheet();
        giftBottomSheet = new GiftBottomSheet();
        giftBottomSheet.setOnDismiss(() -> {
            loutBottom.setVisibility(View.VISIBLE);
            lout_rv.setVisibility(View.VISIBLE);

        });
        diamondShopBottomSheet.setOnDismiss(() -> {
            loutBottom.setVisibility(View.VISIBLE);
            lout_rv.setVisibility(View.VISIBLE);

        });


    }

}
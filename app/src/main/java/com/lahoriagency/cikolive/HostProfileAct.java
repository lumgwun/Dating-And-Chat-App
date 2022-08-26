package com.lahoriagency.cikolive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.lahoriagency.cikolive.Adapters.AddImageAdapter;

public class HostProfileAct extends AppCompatActivity {
    private Button btnUpDate;
    AddImageAdapter addImageAdapter;
    private RecyclerView rvImages,rvVideos;
    private ImageView imgBack;
    private EditText edtAge,edtNames,edtAbout,edtInterest,edtBio,edtHr,edtAddress,edtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_host_profile);
        BaseActivity baseActivity = new BaseActivity();
        btnUpDate = findViewById(R.id.btn_update);
        rvImages = findViewById(R.id.rv_images);
        rvVideos = findViewById(R.id.rv_videos);
        imgBack = findViewById(R.id.btn_back);
        edtAge = findViewById(R.id.et_age);
        edtNames = findViewById(R.id.et_name);
        edtAbout = findViewById(R.id.et_about);
        edtBio = findViewById(R.id.et_bio);
        edtHr = findViewById(R.id.et_hour);
        edtAddress = findViewById(R.id.et_address);
        edtInterest = findViewById(R.id.et_interests);
        edtEmail = findViewById(R.id.et_email);
        baseActivity.makeListOfImages();
        addImageAdapter = new AddImageAdapter();
        rvImages.setAdapter(addImageAdapter);
        rvVideos.setAdapter(addImageAdapter);
        addImageAdapter.updateItems(baseActivity.listOfImages);
        btnUpDate.setOnClickListener(view -> {
            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        imgBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        //imgBack.setOnClickListener(view -> getActivity().onBackPressed());
    }

    public void doUpdate(View view) {
    }
}
package com.lahoriagency.cikolive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AppSettingAct extends AppCompatActivity {
    public static void start(Context context) {
        Intent intent = new Intent(context, AppSettingAct.class);
        context.startActivity(intent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_app_setting);
    }
}
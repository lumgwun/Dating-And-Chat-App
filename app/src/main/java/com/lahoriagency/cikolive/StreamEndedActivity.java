package com.lahoriagency.cikolive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class StreamEndedActivity extends AppCompatActivity {
    public static void start(Context context) {
        Intent intent = new Intent(context, StreamEndedActivity.class);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_stream_ended);
    }
}
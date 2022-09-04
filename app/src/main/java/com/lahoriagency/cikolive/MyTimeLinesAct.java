package com.lahoriagency.cikolive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.lahoriagency.cikolive.Adapters.TimelineAdapter;
import com.lahoriagency.cikolive.DataBase.TimeLineDAO;

public class MyTimeLinesAct extends AppCompatActivity {
    private TimeLineDAO timeLineDAO;
    private RecyclerView recyclerView;
    TimelineAdapter timelineAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_my_time_lines);
        timeLineDAO= new TimeLineDAO(this);
    }
}
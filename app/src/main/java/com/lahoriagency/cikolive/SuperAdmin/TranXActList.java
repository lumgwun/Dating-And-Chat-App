package com.lahoriagency.cikolive.SuperAdmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.lahoriagency.cikolive.Adapters.TranxAdapter;
import com.lahoriagency.cikolive.AppSupportAct;
import com.lahoriagency.cikolive.Classes.ModelItem;
import com.lahoriagency.cikolive.Classes.SavedProfile;
import com.lahoriagency.cikolive.Classes.Transaction;
import com.lahoriagency.cikolive.DataBase.DBHelper;
import com.lahoriagency.cikolive.DataBase.TransactionDAO;
import com.lahoriagency.cikolive.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TranXActList extends AppCompatActivity implements TranxAdapter.OnItemsClickListener{
    private TranxAdapter tranxAdapter;
    private TranxAdapter tranxAdapterDate;
    private TranxAdapter tranxAdapterByMonth;
    ArrayList<Transaction> transactionArrayList;
    ArrayList<Transaction> tranxSByDate;
    ArrayList<Transaction> tranxSByMonth;
    TransactionDAO transactionDAO;
    private Transaction transaction;
    TranxAdapter.OnItemsClickListener onItemsClickListener;
    String finalTodayDate;
    private Calendar mCalendar;
    private String mToday;
    private DBHelper dbHelper;
    private static final String[] MONTH_NAMES = {"January", "February", "March", "April",
            "May", "June", "July", "August",
            "September", "October", "November", "December"};
    private DatePicker picker;
    private static final String PREF_NAME = "CIKO";
    private AppCompatButton btnSearch;
    private RecyclerView tranxRecyclerViewAll,tranxRecyclerViewByDate,tranxRecyclerViewByMonth,tranxRecyclerViewByYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_tran_xact_list);
        setTitle("Diamond Transactions");
        transactionArrayList = new ArrayList<>();
        tranxSByDate = new ArrayList<>();
        tranxSByMonth = new ArrayList<>();
        transaction= new Transaction();

        picker=(DatePicker)findViewById(R.id.date_tx);
        tranxRecyclerViewAll = findViewById(R.id.diamondTRecycler);
        tranxRecyclerViewByDate = findViewById(R.id.date_TRecycler);
        tranxRecyclerViewByMonth = findViewById(R.id.month_TRecycler);
        btnSearch = findViewById(R.id.tx_SearchDB);
        transactionDAO= new TransactionDAO(this);
        dbHelper = new DBHelper(this);
        mCalendar = Calendar.getInstance();
        btnSearch.setOnClickListener(this::SearchDBForTx);

        //mToday = mCalendar.get(Calendar.DATE) + " " + MONTH_NAMES[mCalendar.get(Calendar.MONTH)] + " " + mCalendar.get(Calendar.YEAR);
        mToday = mCalendar.get(Calendar.YEAR) + "/" + MONTH_NAMES[mCalendar.get(Calendar.MONTH)]+"/" +mCalendar.get(Calendar.DATE) ;
        if (transactionDAO !=null) {
            transactionArrayList = transactionDAO.getAllTranX();

        }
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate(finalTodayDate);
            }
        });
        if(finalTodayDate ==null){
            mCalendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd", Locale.getDefault());
            //dateSting = dateFormat.format(mCalendar.getTime());

            finalTodayDate=mToday;

        }
        finalTodayDate = picker.getYear()+"-"+ (picker.getMonth() + 1)+"-"+picker.getDayOfMonth();



        if(transactionDAO !=null){
            tranxSByMonth=transactionDAO.getTranxByMonths(finalTodayDate);

        }


        if(transactionDAO !=null){
            tranxSByDate=transactionDAO.getTranxForSpecMonth(finalTodayDate);

        }

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(TranXActList.this, LinearLayoutManager.HORIZONTAL, false);
        tranxRecyclerViewAll.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(tranxRecyclerViewAll.getContext(),
                layoutManager.getOrientation());
        tranxRecyclerViewAll.addItemDecoration(dividerItemDecoration);
        tranxRecyclerViewAll.setItemAnimator(new DefaultItemAnimator());
        tranxAdapter = new TranxAdapter(TranXActList.this,transactionArrayList);
        SnapHelper snapHelper1 = new PagerSnapHelper();
        snapHelper1.attachToRecyclerView(tranxRecyclerViewAll);
        tranxRecyclerViewAll.setAdapter(tranxAdapter);
        tranxAdapter.setWhenClickListener(onItemsClickListener);



        LinearLayoutManager layoutManagerMonth
                = new LinearLayoutManager(TranXActList.this, LinearLayoutManager.HORIZONTAL, false);
        tranxRecyclerViewByDate.setLayoutManager(layoutManagerMonth);
        DividerItemDecoration dividerItemDec = new DividerItemDecoration(tranxRecyclerViewByMonth.getContext(),
                layoutManager.getOrientation());
        tranxRecyclerViewByMonth.addItemDecoration(dividerItemDec);
        tranxRecyclerViewByMonth.setItemAnimator(new DefaultItemAnimator());
        tranxAdapterByMonth = new TranxAdapter(TranXActList.this,tranxSByMonth);
        tranxRecyclerViewByMonth.setAdapter(tranxAdapterDate);
        tranxAdapterByMonth.setWhenClickListener(onItemsClickListener);
        tranxAdapterByMonth.notifyDataSetChanged();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayoutManager layoutManager
                        = new LinearLayoutManager(TranXActList.this, LinearLayoutManager.HORIZONTAL, false);
                tranxRecyclerViewByDate.setLayoutManager(layoutManager);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(tranxRecyclerViewByDate.getContext(),
                        layoutManager.getOrientation());
                tranxRecyclerViewByDate.addItemDecoration(dividerItemDecoration);
                tranxRecyclerViewByDate.setItemAnimator(new DefaultItemAnimator());
                tranxAdapterDate = new TranxAdapter(TranXActList.this,tranxSByDate);
                tranxRecyclerViewByDate.setAdapter(tranxAdapterDate);
                tranxAdapterDate.notifyDataSetChanged();
                tranxAdapterDate.setWhenClickListener(onItemsClickListener);

            }
        });
    }
    private void chooseDate(String finalTodayDate) {
        finalTodayDate = picker.getYear()+"-"+ (picker.getMonth() + 1)+"-"+picker.getDayOfMonth();


    }

    public void SearchDBForTx(View view) {
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClick(View view, int position) {
        Bundle updateBundle= new Bundle();
        ModelItem modelItem=null;
        if(transaction !=null){
            transaction=transactionArrayList.get(position);
            SavedProfile savedProfile= transaction.getTranxSavedProfile();
            if(savedProfile !=null){
                modelItem = savedProfile.getModelItem();
            }
            int savedProfID=transaction.getTranSenderSavedProfID();
            int qbUserID=transaction.getTranQbUserID();
            updateBundle.putParcelable("Transaction",transaction);
            updateBundle.putInt("savedProfID",savedProfID);
            updateBundle.putInt("qbUserID",qbUserID);
            updateBundle.putParcelable("ModelItem",modelItem);
            Intent pIntent = new Intent(TranXActList.this, UpdateDiamondAct.class);
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
            pIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            pIntent.putExtras(updateBundle);
            startActivity(pIntent);

        }
    }
}
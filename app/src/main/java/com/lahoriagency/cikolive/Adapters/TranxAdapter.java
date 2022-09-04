package com.lahoriagency.cikolive.Adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lahoriagency.cikolive.Classes.Transaction;
import com.lahoriagency.cikolive.R;
import com.quickblox.users.model.QBUser;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class TranxAdapter extends RecyclerView.Adapter<TranxAdapter.ViewHolder> implements ListAdapter {

    private List<Transaction> transactionList;
    private OnItemsClickListener listener;
    Context context;
    private ArrayList<Transaction> transactionArrayList;
    private ArrayList<Transaction> arrayList;
    private GestureDetector gestureDetector;
    private int position,noOfDiamond;
    private QBUser qbUser;
    private String methodOfPayment,date,currency,status;
    private double amount;

    public TranxAdapter(Context context, List<Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;

    }
    public TranxAdapter(Context context, ArrayList<Transaction> sliderDataArrayList, OnItemsClickListener callback) {
        this.context = context;
        this.transactionArrayList = sliderDataArrayList;
        this.listener = callback;


    }
    public void setCallback(OnItemsClickListener callback) {
        this.listener = callback;
    }

    public TranxAdapter(Context context, List<Transaction> transactionList,OnItemsClickListener callback) {
        this.context = context;
        this.transactionList = transactionList;
        this.listener = callback;

    }
    public void updateItems(ArrayList<Transaction> list) {
        this.transactionArrayList = list;


        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tranx_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Transaction transaction = transactionArrayList.get(position);
        if(transaction !=null){
            qbUser= transaction.getTranxQbUser();
            status=transaction.getTranStatus();
            amount=transaction.getTranAmount();
            currency=transaction.getTranCurrency();
            date=transaction.getTranDate();
            methodOfPayment=transaction.getTranMethodOfPayment();
            noOfDiamond=transaction.getTranNoOfDiamond();

        }
        ViewHolder mainHolder = (ViewHolder) holder;
        context = mainHolder.itemView.getContext();
        mainHolder.methodOfPayment.setText(MessageFormat.format("method of Payment.{0}", methodOfPayment));
        mainHolder.noOfDiamond.setText(MessageFormat.format("No. of Diamond:{0}", noOfDiamond));
        mainHolder.amount.setText(MessageFormat.format("Amount {0}", currency+""+amount));
        mainHolder.tranxDate.setText(MessageFormat.format("Date:{0}", date));
        mainHolder.txtStatus.setText(MessageFormat.format("Status:{0}", status));
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onItemClick(position);
                }
            }
        });
    }
    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView noOfDiamond;
        public TextView methodOfPayment;
        public TextView amount;
        public TextView tranxDate, txtStatus;
        public ImageView itemImage;
        private View itemView1;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.item_t_amt);
            noOfDiamond = itemView.findViewById(R.id.no_of_diamonds);
            cardView = itemView.findViewById(R.id.t_ItemCardView);
            tranxDate = itemView.findViewById(R.id.tranx_date);
            methodOfPayment = itemView.findViewById(R.id.tranx_method_of_payment);
            itemImage = itemView.findViewById(R.id.item_image);
            txtStatus = itemView.findViewById(R.id.tranx_status);

            itemView = itemView1;
            itemView.setOnClickListener(this);
        }

        public void setData(int itemImage, String itemName, double price, String description, String duration){
            this.noOfDiamond.setText(itemName);
            this.amount.setText((int) price);
            this.methodOfPayment.setText(description);
            this.tranxDate.setText( duration);
            this.itemImage.setImageResource(itemImage);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onItemClick(position);
            }

        }
    }


    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }


    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }



    public interface OnItemsClickListener{
        void onItemClick(int position);
        void onItemClick(View view, int position);
    }
    public void setWhenClickListener(OnItemsClickListener listener){
        this.listener = listener;
    }
}

package com.lahoriagency.cikolive.SuperAdmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lahoriagency.cikolive.Classes.ModelItem;
import com.lahoriagency.cikolive.Classes.Transaction;
import com.lahoriagency.cikolive.DataBase.TransactionDAO;
import com.lahoriagency.cikolive.R;

public class UpdateDiamondAct extends AppCompatActivity {
    private Bundle bundle;
    private int savedProfID;
    private int qbUserID,tranxID;
    private Transaction transaction;
    private ModelItem modelItem;
    private TransactionDAO transactionDAO;
    private TextView txtTranxID,txtUserID,txtAmount;
    private EditText edtNoOfDiamond;
    private AppCompatButton btnUpdate;
    private double amountPaid;
    private int diamondAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_update_diamond);
        transactionDAO= new TransactionDAO(this);
        txtTranxID = findViewById(R.id.updateTXID);
        txtUserID = findViewById(R.id.updateUsersIDs);
        txtAmount = findViewById(R.id.updateUAmt);
        edtNoOfDiamond = findViewById(R.id.update_diamond);
        btnUpdate = findViewById(R.id.ciko_revenue);
        btnUpdate.setOnClickListener(this::doTXUpdate);

        bundle=getIntent().getExtras();
        if(bundle !=null){
            transaction=bundle.getParcelable("Transaction");
            modelItem=bundle.getParcelable("ModelItem");
            qbUserID=bundle.getInt("qbUserID");
            savedProfID=bundle.getInt("savedProfID");

        }
        if(transaction !=null){
            tranxID=transaction.getTranID();
            amountPaid=transaction.getTranAmount();

        }
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diamondAmount= Integer.parseInt(edtNoOfDiamond.getText().toString());

            }
        });
        if(diamondAmount<0){
            edtNoOfDiamond.setFocusable(true);
            edtNoOfDiamond.setError("Insert No For Diamond");

        }else {
            transactionDAO.updateTranXDiamond(tranxID,diamondAmount,"Delivered");
        }
    }

    public void doTXUpdate(View view) {
    }
}
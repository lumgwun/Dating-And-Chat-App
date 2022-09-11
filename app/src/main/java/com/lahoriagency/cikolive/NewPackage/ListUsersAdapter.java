package com.lahoriagency.cikolive.NewPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lahoriagency.cikolive.Interfaces.ItemClickListener;
import com.lahoriagency.cikolive.R;
import com.quickblox.users.model.QBUser;

import java.util.List;

public class ListUsersAdapter extends RecyclerView.Adapter<ListUsersAdapter.MyViewHolder> {
    private Context context;

    private List<QBUser> qbUserList;

    private ItemClickListener itemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView textView;
        RelativeLayout relativeLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.name_item_text_view);
            relativeLayout = itemView.findViewById(R.id.list_users_main_view);

            relativeLayout.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if(itemClickListener!=null)
            {
                itemClickListener.onClick1(v, getAdapterPosition());
            }
        }
    }

    public ListUsersAdapter(Context context, List<QBUser> qbUserList) {
        this.context = context;
        this.qbUserList = qbUserList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ListUsersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_users, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListUsersAdapter.MyViewHolder myViewHolder, int i) {
        QBUser qbUser = qbUserList.get(i);
        myViewHolder.textView.setText(qbUser.getLogin());
    }

    @Override
    public int getItemCount() {
        return qbUserList.size();
    }
}

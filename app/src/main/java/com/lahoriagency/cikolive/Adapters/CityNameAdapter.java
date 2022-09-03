package com.lahoriagency.cikolive.Adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.lahoriagency.cikolive.Classes.CityName;
import com.lahoriagency.cikolive.R;
import com.lahoriagency.cikolive.Utils.Const;
import com.lahoriagency.cikolive.Utils.SessionManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CityNameAdapter extends RecyclerView.Adapter<CityNameAdapter.ItemHolder> {

    OnItemClick onItemClick;
    List<CityName> list = new ArrayList<>();

    public OnItemClick getOnItemClick() {
        return onItemClick;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @NotNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country_name, parent, false);
        return new ItemHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ItemHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateItems(List<CityName> list) {
        this.list = list;


        notifyDataSetChanged();
    }

    public interface OnItemClick {
        void onClick(String name);


    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        SessionManager sessionManager;
        private LinearLayout lout;
        private TextView tvName;

        public ItemHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_Country_name);
            lout = itemView.findViewById(R.id.loutCountry);
            sessionManager = new SessionManager(itemView.getContext());
        }

        @SuppressLint("UseCompatLoadingForColorStateLists")
        public void setData(int position) {

            CityName model = list.get(position);
            tvName.setText(model.getName());

            for (int i = 0; i < list.size(); i++) {

                if (sessionManager.getIntValue(Const.COUNTRY_ID) == model.getId()) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                        tvName.setTypeface(itemView.getContext().getResources().getFont(R.font.gilroy_bold));
                    }
                    lout.setBackgroundResource(R.drawable.bg_gradiant_10);
                    lout.setBackgroundTintList(null);

                } else {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                        tvName.setTypeface(itemView.getContext().getResources().getFont(R.font.gilroy_medium));
                    }
                    lout.setBackgroundResource(R.drawable.bg_cornered_rect_10);
                    lout.setBackgroundTintList(ContextCompat.getColorStateList(itemView.getContext(), R.color.bg_chats));

                }


            }


            itemView.setOnClickListener(v -> {

                sessionManager.saveIntValue(Const.COUNTRY_ID, model.getId());
                onItemClick.onClick(model.getName());
            });

        }
    }
}

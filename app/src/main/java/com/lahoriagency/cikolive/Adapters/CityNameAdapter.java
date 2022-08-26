package com.lahoriagency.cikolive.Adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.retry.dimdim.R;
import com.retry.dimdim.databinding.ItemCountryNameBinding;
import com.retry.dimdim.modals.CityName;
import com.retry.dimdim.utils.Const;
import com.retry.dimdim.utils.SessionManager;

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
        ItemCountryNameBinding binding;

        public ItemHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            sessionManager = new SessionManager(itemView.getContext());

        }

        @SuppressLint("UseCompatLoadingForColorStateLists")
        public void setData(int position) {

            CityName model = list.get(position);
            binding.tvName.setText(model.getName());


            for (int i = 0; i < list.size(); i++) {

                if (sessionManager.getIntValue(Const.COUNTRY_ID) == model.getId()) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                        binding.tvName.setTypeface(itemView.getContext().getResources().getFont(R.font.gilroy_bold));
                    }
                    binding.lout.setBackgroundResource(R.drawable.bg_gradiant_10);
                    binding.lout.setBackgroundTintList(null);

                } else {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                        binding.tvName.setTypeface(itemView.getContext().getResources().getFont(R.font.gilroy_medium));
                    }
                    binding.lout.setBackgroundResource(R.drawable.bg_cornered_rect_10);
                    binding.lout.setBackgroundTintList(ContextCompat.getColorStateList(itemView.getContext(), R.color.bg_chats));

                }


            }


            binding.getRoot().setOnClickListener(v -> {

                sessionManager.saveIntValue(Const.COUNTRY_ID, model.getId());
                onItemClick.onClick(model.getName());
            });

        }
    }
}

package com.lahoriagency.cikolive.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lahoriagency.cikolive.DataBase.DBHelper;
import com.lahoriagency.cikolive.HostProfileAct;
import com.lahoriagency.cikolive.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class AddImageAdapter extends RecyclerView.Adapter<AddImageAdapter.ItemHolder> {

    List<String> list = new ArrayList<>();
    private Context context;
    private DBHelper dbHelper;

    public AddImageAdapter(HostProfileAct hostProfileAct, List<String> photos) {
        this.context=hostProfileAct;
        this.list=photos;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_images, parent, false);
        return new ItemHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.setItems(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateItems(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @SuppressWarnings("deprecation")
    public class ItemHolder extends RecyclerView.ViewHolder {
        private LinearLayout lout;
        private TextView tvName;
        private ImageView btnDelete,btnAdd,img;



        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgToBeAdded);
            btnDelete = itemView.findViewById(R.id.btn_deleteImage);
            btnAdd = itemView.findViewById(R.id.btn_addPImage);

        }

        public void setItems(int position) {
            if (position < 1) {
                dbHelper= new DBHelper(context.getApplicationContext());

                Glide.with(itemView.getContext()).load(list.get(position)).into(img);
                btnDelete.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.GONE);

                //byte [] image = convertToByteArray(list.get(position));
                //dbHelper.insertValues(image);
            }


        }




    }
    public byte[] convertToByteArray(int image){

        Resources resources = context.getResources();
        Drawable drawable = resources.getDrawable(image);
        Bitmap bitmap =  ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();

        return bitmapData;

    }
}

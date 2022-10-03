package com.lahoriagency.cikolive.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("deprecation")
public class ProfMediaAdapter extends RecyclerView.Adapter<ProfMediaAdapter.ItemHolder> {

    List<Uri> list = new ArrayList<>();
    private Context context;
    private DBHelper dbHelper;
    public ProfMediaAdapter(ArrayList<Uri> uri) {
        this.list = uri;
    }

    public ProfMediaAdapter(Context context, List<Uri> photos) {
        this.context=context;
        this.list=photos;
    }

    @NonNull
    @Override
    public ProfMediaAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_media, parent, false);
        return new ProfMediaAdapter.ItemHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ProfMediaAdapter.ItemHolder holder, int position) {
        holder.img.setImageURI(list.get(position));
        holder.setItems(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateItems(List<Uri> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @SuppressWarnings("deprecation")
    public class ItemHolder extends RecyclerView.ViewHolder {
        private CircleImageView img;



        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_media);

        }

        public void setItems(int position) {
            if (position < 1) {
                dbHelper= new DBHelper(context.getApplicationContext());

                Glide.with(itemView.getContext()).load(list.get(position)).into(img);

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

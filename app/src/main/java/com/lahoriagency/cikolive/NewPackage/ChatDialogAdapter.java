package com.lahoriagency.cikolive.NewPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.lahoriagency.cikolive.Interfaces.ItemClickListener;
import com.lahoriagency.cikolive.R;
import com.quickblox.chat.model.QBChatDialog;

import java.util.List;

public class ChatDialogAdapter extends RecyclerView.Adapter<ChatDialogAdapter.MyViewHolder> {

    private Context context;
    private List<QBChatDialog> list;

    private ItemClickListener itemClickListener;

    @SuppressWarnings("deprecation")
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView chatDialogImage;
        TextView titleTextView;
        TextView messageTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            chatDialogImage = itemView.findViewById(R.id.chat_dialog_image_view);
            titleTextView = itemView.findViewById(R.id.chat_dialog_title);
            messageTextView = itemView.findViewById(R.id.chat_dialog_message);
            chatDialogImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener!=null)
            {
                itemClickListener.onClick1(v, getAdapterPosition());
            }
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ChatDialogAdapter(Context context, List<QBChatDialog> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChatDialogAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat_dialog, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatDialogAdapter.MyViewHolder myViewHolder, int i) {
        QBChatDialog qbChatDialog = list.get(i);

        myViewHolder.titleTextView.setText(qbChatDialog.getName());
        myViewHolder.messageTextView.setText(qbChatDialog.getLastMessage());

        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        int randomColor = colorGenerator.getRandomColor();

        TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig().withBorder(4).endConfig().round();

        TextDrawable textDrawable = builder.build(myViewHolder.titleTextView.getText().toString().substring(0, 1).toUpperCase(), randomColor);

        myViewHolder.chatDialogImage.setImageDrawable(textDrawable);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

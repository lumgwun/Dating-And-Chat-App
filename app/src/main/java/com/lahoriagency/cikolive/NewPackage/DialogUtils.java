package com.lahoriagency.cikolive.NewPackage;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.lahoriagency.cikolive.R;
import com.quickblox.chat.model.QBChatDialog;

public class DialogUtils {
    Integer recipientId;
    public static ProgressDialog getProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage(context.getString(R.string.please_wait));
        return progressDialog;
    }

    public static Dialog createDialog(Context context,
                                      @StringRes int titleId, @StringRes int messageId, View view,
                                      DialogInterface.OnClickListener positiveClickListener,
                                      DialogInterface.OnClickListener negativeClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titleId);
        builder.setMessage(messageId);
        builder.setView(view);
        builder.setPositiveButton(R.string.ok, positiveClickListener);
        builder.setNegativeButton(R.string.dlg_cancel, negativeClickListener);

        return builder.create();
    }

    public static QBChatDialog buildPrivateDialog(Integer recipientId) {
        recipientId=recipientId;
        return null;
    }
}

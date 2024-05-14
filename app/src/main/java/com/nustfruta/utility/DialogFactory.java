package com.nustfruta.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.nustfruta.R;

abstract public class DialogFactory {

    public static Dialog loadingDialog;

    public static void createLoadingDialog(Activity context, boolean isCancellable)
    {
        loadingDialog =  new Dialog(context);
        loadingDialog.setCancelable(isCancellable);
        loadingDialog.setContentView(R.layout.loading_dialog_layout);
        loadingDialog.getWindow().setLayout(RecyclerView.LayoutParams.WRAP_CONTENT,RecyclerView.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loadingDialog.show();
    }

    public static void destroyLoadingDialog()
    {
        loadingDialog.dismiss();
    }

}

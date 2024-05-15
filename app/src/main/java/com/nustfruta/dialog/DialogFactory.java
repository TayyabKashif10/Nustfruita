package com.nustfruta.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.nustfruta.R;


// this class maintains single use dialogs to use in the application.
abstract public class DialogFactory {

    private static Dialog loadingDialog;

    private static Dialog loginDialog;


    public static void createLoadingDialog(Activity context, boolean isCancellable)
    {
        if (loadingDialog != null) {return;}

        loadingDialog =  new Dialog(context);
        loadingDialog.setCancelable(isCancellable);
        loadingDialog.setContentView(R.layout.loading_dialog_layout);
        loadingDialog.getWindow().setLayout(RecyclerView.LayoutParams.WRAP_CONTENT,RecyclerView.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loadingDialog.show();
    }

    public static void destroyLoadingDialog()
    {

        if (loadingDialog == null) {return;}
        loadingDialog.dismiss();
    }

    public static void createLoginDialog(Activity context, boolean isCancellable, LoginDialogEventListener eventListener)
    {
        if (loadingDialog != null) {return;}

        loginDialog =  new Dialog(context);
        loginDialog.setCancelable(isCancellable);
        loginDialog.setContentView(R.layout.login_dialog);
        loginDialog.getWindow().setLayout(RecyclerView.LayoutParams.WRAP_CONTENT,RecyclerView.LayoutParams.WRAP_CONTENT);
        loginDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        loginDialog.findViewById(R.id.dialogGoBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListener.onGoBackClicked();
            }
        });

        loginDialog.findViewById(R.id.dialogLoginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListener.onLoginClicked();
            }
        });


        loginDialog.show();
    }

    public static void destroyLoginDialog()
    {
        if (loginDialog == null) {return;}
        loginDialog.dismiss();
    }


}

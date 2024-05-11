package com.nustfruta.menu_fragments;


import android.app.Dialog;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.nustfruta.R;
import com.nustfruta.menu.ProductCardButtonListener;
import com.nustfruta.menu.MenuActivity;
import com.nustfruta.menu.ProductArrayViewModel;
import com.nustfruta.models.CartProduct;
import com.nustfruta.models.ProductDB;
import com.nustfruta.utility.FirebaseStorageUtil;

abstract public class MenuFragment extends Fragment{

    FirebaseRecyclerOptions<ProductDB> options;
    ProductAdapter adapter;

    RecyclerView recyclerView;

    ProductArrayViewModel productArrayViewModel;

    Dialog deleteProductDialog;
    Button dialogCancel, dialogConfirm;


    ProductCardButtonListener productCardButtonListener = new ProductCardButtonListener() {
        @Override
        public void addObject(ProductDB productDB) {

            ((MenuActivity)getActivity()).displayBottomSheet(new CartProduct(productDB,1));
        }

        @Override
        public void deleteObject(int adapterPosition, String completeImageURL) {

            dialogConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.getRef(adapterPosition).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            adapter.notifyItemRemoved(adapterPosition);
                            FirebaseStorageUtil.deleteImage(completeImageURL);
                        }
                    });
                    deleteProductDialog.dismiss();
                }
            });
            deleteProductDialog.show();
        }
    };

    public MenuFragment() {
        // Required empty public constructor
    }

    public void setUpDeleteProductDialog()
    {
        deleteProductDialog = new Dialog(this.getContext());
        deleteProductDialog.setContentView(R.layout.delete_product_dialog);
        deleteProductDialog.getWindow().setLayout(RecyclerView.LayoutParams.WRAP_CONTENT,RecyclerView.LayoutParams.WRAP_CONTENT);
        deleteProductDialog.setCancelable(true);
        deleteProductDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogConfirm = deleteProductDialog.findViewById(R.id.dialogConfirmButton);
        dialogCancel = deleteProductDialog.findViewById(R.id.dialogCancelButton);

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProductDialog.dismiss();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

        // necessary to reconfigure the recycle view on returning from another activity.
        adapter.notifyDataSetChanged();
    }

}
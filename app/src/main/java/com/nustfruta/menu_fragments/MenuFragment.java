package com.nustfruta.menu_fragments;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.nustfruta.menu.ArrayModifier;
import com.nustfruta.menu.MenuActivity;
import com.nustfruta.menu.ProductArrayViewModel;
import com.nustfruta.models.CartProduct;
import com.nustfruta.models.ProductDB;

abstract public class MenuFragment extends Fragment {

    FirebaseRecyclerOptions<ProductDB> options;
    ProductAdapter adapter;

    RecyclerView recyclerView;

    ProductArrayViewModel productArrayViewModel;

    ArrayModifier arrayModifier = new ArrayModifier() {
        @Override
        public void addObject(ProductDB productDB) {

            ((MenuActivity)getActivity()).displayBottomSheet(new CartProduct(productDB,1));
        }
    };

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();

        // necessary to reconfigure the recycle view on returning from cart activity.
        adapter.notifyDataSetChanged();
    }

}
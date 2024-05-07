package com.nustfruta.menu_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.nustfruta.R;
import com.nustfruta.menu.ArrayModifier;
import com.nustfruta.menu.ProductArrayViewModel;
import com.nustfruta.models.CartProduct;
import com.nustfruta.models.ProductDB;
import com.nustfruta.utility.FirebaseDBUtil;

import org.checkerframework.checker.units.qual.C;

public class Fruits extends Fragment {

    FirebaseRecyclerOptions<ProductDB> options;
    ProductAdapter adapter;

    ArrayModifier arrayModifier = new ArrayModifier() {
        @Override
        public void addObject(ProductDB productDB) {

            // TODO: implement something to control quantity
            productArrayViewModel.addProduct(new CartProduct(productDB, 1));
        }
    };

    RecyclerView recyclerView;

    ProductArrayViewModel productArrayViewModel;

    public Fruits() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fruits, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.fruitsRecylerView);

        // to configure the adapter to parse and use Product objects from the database, basically binds them without adding ChildEventListeners
        options = new FirebaseRecyclerOptions.Builder<ProductDB>().setQuery(FirebaseDBUtil.getProductsNodeRerefence().child("fruits"), ProductDB.class).build();
        adapter = new ProductAdapter(options, arrayModifier);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        productArrayViewModel = new ViewModelProvider(requireActivity()).get(ProductArrayViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}
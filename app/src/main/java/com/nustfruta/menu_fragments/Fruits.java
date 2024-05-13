package com.nustfruta.menu_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.nustfruta.R;
import com.nustfruta.menu.ProductArrayViewModel;
import com.nustfruta.models.ProductDB;
import com.nustfruta.utility.FirebaseDBUtil;

public class Fruits extends MenuFragment {

    public Fruits() {
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
        options = new FirebaseRecyclerOptions.Builder<ProductDB>().setQuery(FirebaseDBUtil.getProductsNodeRerefence().child("fruits"), ProductDB.class).setLifecycleOwner(this).build();
        adapter = new ProductAdapter(options, productCardButtonListener, FirebaseDBUtil.currentUserType, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        productArrayViewModel = new ViewModelProvider(requireActivity()).get(ProductArrayViewModel.class);
        setUpDeleteProductDialog();
    }

}
package com.nustfruta.orders_fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nustfruta.R;
import com.nustfruta.models.OrderDB;
import com.nustfruta.models.OrderStatus;
import com.nustfruta.orders.YourOrdersActivity;
import com.nustfruta.utility.FirebaseDBUtil;

import java.util.ArrayList;

public class Active extends Fragment {

    ArrayList<OrderDB> orderList;

    OrderAdapter adapter;

    RecyclerView rvOrderList;

    FrameLayout lEmptyLayout;

    public Active() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_active, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lEmptyLayout = view.findViewById(R.id.lEmptyOrders);
        rvOrderList = view.findViewById(R.id.rvOrderList);
        orderList = new ArrayList<>();

        if (getActivity() instanceof YourOrdersActivity)
            FirebaseDBUtil.getOrdersNodeReference().orderByChild("userID").equalTo(FirebaseDBUtil.getCurrentUserID()).addValueEventListener(new ValueEventListener() {
                OrderDB thisOrder;
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    orderList.clear();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        thisOrder = childSnapshot.getValue(OrderDB.class);
                        if (!thisOrder.getStatus().equals(OrderStatus.DELIVERED))
                            orderList.add(thisOrder);
                    }
                    if (!orderList.isEmpty()) {
                        initializeRecyclerView();
                        lEmptyLayout.setVisibility(View.GONE);
                    }
                    else {
                        initializeRecyclerView();
                        lEmptyLayout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // TODO
                    Log.d("CANCELLED", error.toString());
                }
            });
        else
            FirebaseDBUtil.getOrdersNodeReference().addValueEventListener(new ValueEventListener() {
                OrderDB thisOrder;
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    orderList.clear();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        thisOrder = childSnapshot.getValue(OrderDB.class);
                        if (!thisOrder.getStatus().equals(OrderStatus.DELIVERED))
                            orderList.add(thisOrder);
                    }
                    if (!orderList.isEmpty()) {
                        initializeRecyclerView();
                        lEmptyLayout.setVisibility(View.GONE);
                    }
                    else
                        lEmptyLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // TODO
                }
            });
    }

    private void initializeRecyclerView() {
        adapter = new OrderAdapter((Activity) this.getContext(), orderList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvOrderList.setLayoutManager(layoutManager);

        rvOrderList.setAdapter(adapter);
    }
}
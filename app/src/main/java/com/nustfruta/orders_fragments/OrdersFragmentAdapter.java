package com.nustfruta.orders_fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OrdersFragmentAdapter extends FragmentStateAdapter {

    public OrdersFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment;

        switch (position)
        {
            case 0:
                fragment = new Active();
                break;
            case 1:
                fragment = new Delivered();
                break;
            default:
                fragment = new Active();
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

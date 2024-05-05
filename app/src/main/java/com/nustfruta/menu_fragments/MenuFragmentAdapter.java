package com.nustfruta.menu_fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.nustfruta.utility.Constants;

public class MenuFragmentAdapter extends FragmentStateAdapter {

    public MenuFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment;

        switch (position)
        {
            case 0:
                fragment = new Fruits();
                break;
            case 1:
                fragment = new Salads();
                break;
            case 2:
                fragment = new FruitBundles();
                break;
            default:
                fragment = new Fruits();
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return Constants.MENU_CATEGORIES;
    }
}

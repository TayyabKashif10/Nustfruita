package com.nustfruta.cart;

public interface CartCardButtonListener {
    void plusButton(CartRecyclerViewAdapter.ViewHolder holder, int position);
    void minusButton(CartRecyclerViewAdapter.ViewHolder holder, int position);
}

package com.nustfruta.cart;

public interface ModifyQuantity {
    void plusButton(CartRecyclerViewAdapter.ViewHolder holder, int position);
    void minusButton(CartRecyclerViewAdapter.ViewHolder holder, int position);
}

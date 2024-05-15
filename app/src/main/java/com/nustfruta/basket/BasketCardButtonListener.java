package com.nustfruta.basket;

public interface BasketCardButtonListener {
    void plusButton(BasketRecyclerViewAdapter.ViewHolder holder, int position);
    void minusButton(BasketRecyclerViewAdapter.ViewHolder holder, int position);
}

package com.nustfruta.models;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PACKING,
    ON_WAY,
    DELIVERED;

    @NonNull
    @Override
    @Exclude
    public String toString() {
        if (this.equals(ON_WAY))
            return "On the way";
        return super.toString().charAt(0) + super.toString().substring(1).toLowerCase();
    }

    @Exclude
    public OrderStatus move(int step) {
        OrderStatus[] statuses = OrderStatus.values();
        int ordinal = this.ordinal();
        if (ordinal + step < 0)
            ordinal = 0;
        else if (ordinal + step > 4)
            ordinal = 4;
        else
            ordinal += step;
        return statuses[ordinal];
    }
}

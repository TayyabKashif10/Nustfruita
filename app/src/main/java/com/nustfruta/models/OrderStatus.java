package com.nustfruta.models;

import androidx.annotation.NonNull;

public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PACKING,
    ON_WAY,
    DELIVERED;

    @NonNull
    @Override
    public String toString() {
        if (this.equals(ON_WAY))
            return "On the way";
        return super.toString().charAt(0) + super.toString().substring(1).toLowerCase();
    }
}

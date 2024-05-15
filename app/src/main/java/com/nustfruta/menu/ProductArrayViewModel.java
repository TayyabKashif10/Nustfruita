package com.nustfruta.menu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nustfruta.models.CartProduct;

import java.util.ArrayList;

public class ProductArrayViewModel extends ViewModel {

    MutableLiveData<ArrayList<CartProduct>> liveProductArray = new MutableLiveData<>();

    public void addProduct(CartProduct product)
    {
        if (getArray() != null && product != null)
        {
            // observer is only triggered if the reference to the observable object is reassigned, not if the internal items of an arraylist are changed
            // so to modify the array you have to reAssign the array.
            ArrayList<CartProduct> currentSnapshot = getArray();
            currentSnapshot.add(product);
            liveProductArray.setValue(currentSnapshot);
        }
    }

    public void initializeArray()
    {
        liveProductArray.setValue(new ArrayList<>());
    }

    public LiveData<ArrayList<CartProduct>> getLiveArray() {
        return liveProductArray;
    }

    public ArrayList<CartProduct> getArray() {
        return liveProductArray.getValue();
    }

    public void updateArray(ArrayList<CartProduct> updatedArray) {
        liveProductArray.setValue(updatedArray);
    }

}

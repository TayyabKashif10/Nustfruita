package com.nustfruta.menu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nustfruta.models.ProductDB;

import java.util.ArrayList;

public class ProductArrayViewModel extends ViewModel {

    MutableLiveData<ArrayList<ProductDB>> liveProductArray = new MutableLiveData<>();

    public void addProduct(ProductDB productDB)
    {
        if (getArray() != null && productDB != null)
        {
            // observer is only triggered if the reference to the observable object is reassigned, not if the internal items of an arraylist are changed
            // so to modify the array you have to reAssign the array.
            ArrayList<ProductDB> currentSnapshot = getArray();
            currentSnapshot.add(productDB);
            liveProductArray.setValue(currentSnapshot);
        }
    }

    public void initializeArray()
    {
        liveProductArray.setValue(new ArrayList<>());
    }

    public LiveData<ArrayList<ProductDB>> getLiveArray() {
        return liveProductArray;
    }

    public ArrayList<ProductDB> getArray() {
        return liveProductArray.getValue();
    }
}

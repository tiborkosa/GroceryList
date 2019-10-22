package com.example.grocerylist.util;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.grocerylist.ui.listItems.ListItemsViewModel;

/**
 * helper class to pass in data to view model
 */
public class MyViewModelFactory implements ViewModelProvider.Factory {

    private Bundle args;

    /**
     * Constructor
     * @param args to be passed in
     */
    public MyViewModelFactory(Bundle args){
        this.args = args;
    }

    /**
     * creating the viewmodel factory
     * @param modelClass of the model
     * @param <T> class of the model
     * @return new list item view model
     */
    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ListItemsViewModel(args);
    }
}

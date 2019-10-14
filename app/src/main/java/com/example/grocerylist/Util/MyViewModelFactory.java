package com.example.grocerylist.Util;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.grocerylist.ui.listItems.ListItemsViewModel;

public class MyViewModelFactory implements ViewModelProvider.Factory {

    private Bundle args;

    public MyViewModelFactory(Bundle args){
        this.args = args;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ListItemsViewModel(args);
    }
}

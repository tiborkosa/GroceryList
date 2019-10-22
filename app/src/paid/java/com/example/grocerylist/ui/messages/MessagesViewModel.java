package com.example.grocerylist.ui.messages;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MessagesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MessagesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Comming soon in version 2.0 ");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
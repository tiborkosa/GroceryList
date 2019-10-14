package com.example.grocerylist.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.grocerylist.Util.UserUtil;
import com.example.grocerylist.entities.User;


public class UserViewModel extends ViewModel {

    private MutableLiveData<User> mUser;
    public UserViewModel(){
        mUser = new MutableLiveData<>();
        mUser.setValue(UserUtil.getUser());
    }

    public void setUser(User user){
        mUser.setValue(user);
    }

    public LiveData<User> getUser() {
        return mUser;
    }

}

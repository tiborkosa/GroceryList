package com.example.grocerylist.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.grocerylist.util.UserUtil;
import com.example.grocerylist.entities.User;

/**
 * User live data
 * Used in the profile fragment and the drawer
 */
public class UserViewModel extends ViewModel {

    private MutableLiveData<User> mUser;

    /**
     * Constructor
     */
    public UserViewModel(){
        mUser = new MutableLiveData<>();
        mUser.setValue(UserUtil.getUser());
    }

    /**
     * setting up the live data
     * @param user to be added
     */
    public void setUser(User user){
        mUser.setValue(user);
    }

    /**
     * getting the live data
     * @return user live data
     */
    public LiveData<User> getUser() {
        return mUser;
    }

}

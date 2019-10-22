package com.example.grocerylist.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.grocerylist.entities.User;
import com.google.gson.Gson;

/**
 * userutil is used to save user info in the preferences
 */
public class UserUtil {
    private static User user;
    private static SharedPreferences sPref;

    public static final String G_LIST = "gList";
    public static final String GL_USER = "gl_user";

    /**
     * getting the user from preferences or save it if first time
     */
    static {
        sPref = MyApplication.getAppContext().getSharedPreferences(G_LIST,Context.MODE_PRIVATE);
        String gl_user = sPref.getString(GL_USER,null);

        if(gl_user == null) {
            String tempUid = String.valueOf(System.currentTimeMillis());
            user = new User(tempUid,"Guest", null,null, false);
            saveUserInPref();
        } else{
            user = new Gson().fromJson(gl_user, User.class);
        }
    }

    /**
     * Save the user in the preferences
     */
    private static void saveUserInPref() {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sPref.edit();
        String json = gson.toJson(user);
        editor.putString(GL_USER, json);
        editor.apply();
    }

    /**
     * Setting the user in the preferences
     * Used to update the user when logged in first time
     * @param u to update the user
     */
    public static void setUser(User u){
        user = u;
        saveUserInPref();
    }

    /**
     * Getting the saved user
     * @return saved user info
     */
    public static User getUser(){
        return user;
    }
}

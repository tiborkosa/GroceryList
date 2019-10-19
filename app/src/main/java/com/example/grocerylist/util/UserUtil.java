package com.example.grocerylist.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.grocerylist.entities.User;
import com.google.gson.Gson;


public class UserUtil {
    private static User user;
    private static SharedPreferences sPref;

    public static final String G_LIST = "gList";
    public static final String GL_USER = "gl_user";

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

    private static void saveUserInPref() {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sPref.edit();
        String json = gson.toJson(user);
        editor.putString(GL_USER, json);
        editor.commit();
    }

    public static void setUser(User u){
        user = u;
        saveUserInPref();
    }

    public static User getUser(){
        return user;
    }
}

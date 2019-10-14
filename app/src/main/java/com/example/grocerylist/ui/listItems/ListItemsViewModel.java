package com.example.grocerylist.ui.listItems;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.grocerylist.entities.ListItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.grocerylist.Util.Constants.GROCERY_LIST_ID;


public class ListItemsViewModel extends ViewModel {

    private static MutableLiveData<List<ListItem>> mListItems;

    public static DatabaseReference List_Items_Ref = null;
    private static final String TAG = ListItemsViewModel.class.getSimpleName();

    public ListItemsViewModel(Bundle arg) {
        String pID = arg.getString(GROCERY_LIST_ID, "");
        List_Items_Ref = FirebaseDatabase.getInstance().getReference("/g_list_items/"+pID);

        mListItems = new MutableLiveData<>();
        List_Items_Ref
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ListItem> listItems = new ArrayList<>();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    ListItem listItem = data.getValue(ListItem.class);
                    listItem.setId(data.getKey());
                    listItems.add(listItem);
                }
                mListItems.setValue(listItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Error while retrieving list" + databaseError.getMessage());
            }
        });
    }

    public static void addNewItem(ListItem item){
        List<ListItem> s = mListItems.getValue();
        s.add(item);
        mListItems.setValue(s);
    }

    public static void updateItem(ListItem item, int position){
        List<ListItem> s = mListItems.getValue();
        s.set(position, item);
        mListItems.setValue(s);
    }

    public static void deleteItem(int position){
        List<ListItem> s = mListItems.getValue();
        s.remove(position);
        mListItems.setValue(s);
    }

    public LiveData<List<ListItem>> getItemsList() {
        return mListItems;
    }
}
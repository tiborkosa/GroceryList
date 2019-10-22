package com.example.grocerylist.ui.listItems;

import android.os.Bundle;

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

import timber.log.Timber;

import static com.example.grocerylist.util.Constants.GROCERY_LIST_ID;


public class ListItemsViewModel extends ViewModel {

    private static MutableLiveData<List<ListItem>> mListItems;

    public static DatabaseReference List_Items_Ref = null;
    private static final String TAG = ListItemsViewModel.class.getSimpleName();

    /**
     * constructor to pass in the data
     * @param arg required when the grocery list item is clicked
     *            becase we need the item id to retrieve the data
     *
     *        args can be null if its tablet view and no item was clicked on first time
     */
    public ListItemsViewModel(Bundle arg) {

        if(arg != null){
            String pID = arg.getString(GROCERY_LIST_ID, null);
            if(pID != null){
                List_Items_Ref = FirebaseDatabase.getInstance().getReference("/g_list_items/"+pID);
                Timber.d("id is: %s", pID);

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
                                Timber.d( "Error while retrieving list%s", databaseError.getMessage());
                            }
                        });
            }
        }
    }

    /**
     * cleaning up the data
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        List_Items_Ref = null;
    }

    /**
     * updating the list by adding new
     * @param item
     */
    public static void addNewItem(ListItem item){
        List<ListItem> s = mListItems.getValue();
        s.add(item);
        mListItems.setValue(s);
    }

    /**
     * updating the list by editing existing item
     * @param item
     */
    public static void updateItem(ListItem item, int position){
        List<ListItem> s = mListItems.getValue();
        s.set(position, item);
        mListItems.setValue(s);
    }


    /**
     * updating the list when deleting item
     * @param position
     */
    public static void deleteItem(int position){
        List<ListItem> s = mListItems.getValue();
        s.remove(position);
        mListItems.setValue(s);
    }

    /**
     * setting up the live data with the list
     * @return
     */
    public LiveData<List<ListItem>> getItemsList() {
        return mListItems;
    }
}
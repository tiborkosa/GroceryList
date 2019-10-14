package com.example.grocerylist.ui.grocerylist;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.grocerylist.Util.UserUtil;
import com.example.grocerylist.entities.GroceryList;
import com.example.grocerylist.repository.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class GroceryListViewModel extends ViewModel {

    private final static String TAG = GroceryListViewModel.class.getSimpleName();
    private static String userId = UserUtil.getUser().getId();
    public static DatabaseReference listRef = FirebaseDatabase.getInstance().getReference(userId+"/my_list");
    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(listRef);

    //-------------
    /*private final MediatorLiveData<List<GroceryList>> groceryListLiveData = new MediatorLiveData<>();

    public HotStockViewModel() {
        // Set up the MediatorLiveData to convert DataSnapshot objects into HotStock objects
        groceryListLiveData.addSource(liveData, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable final DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            List<GroceryList> groceryLists = new ArrayList<>();
                            for(DataSnapshot data: dataSnapshot.getChildren()){
                                groceryLists.add(data.getValue(GroceryList.class));
                            }
                            groceryListLiveData.postValue(groceryLists);
                        }
                    }).start();
                } else {
                    groceryListLiveData.setValue(null);
                }
            }
        });
    }*/


    //-------------

    private final LiveData<List<GroceryList>> groceryListLiveData =
            Transformations.map(liveData, new Deserializer());

    private class Deserializer implements Function<DataSnapshot, List<GroceryList>> {
        @Override
        public List<GroceryList> apply(DataSnapshot dataSnapshot) {
            Log.d(TAG, "Parsing grocery list dataSnapshot.");
            List<GroceryList> groceryLists = new ArrayList<>();
            for(DataSnapshot data: dataSnapshot.getChildren()){
                GroceryList groceryList = data.getValue(GroceryList.class);
                groceryList.setId(data.getKey());
                groceryLists.add(groceryList);
            }
            Log.d(TAG, "Size of the grocery list " + groceryLists.size());
            return groceryLists;
        }
    }

    public LiveData<List<GroceryList>> getGroceryList() {
        Log.d(TAG,"Getting GroceryList ");
        return groceryListLiveData;
    }
}

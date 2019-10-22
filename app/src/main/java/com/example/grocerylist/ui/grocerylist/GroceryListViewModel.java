package com.example.grocerylist.ui.grocerylist;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.grocerylist.util.UserUtil;
import com.example.grocerylist.entities.GroceryList;
import com.example.grocerylist.repository.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class GroceryListViewModel extends ViewModel {

    private final static String TAG = GroceryListViewModel.class.getSimpleName();
    private static String userId = UserUtil.getUser().getId();
    public static DatabaseReference listRef = FirebaseDatabase.getInstance().getReference(userId+"/my_list");
    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(listRef);

    /**
     * Setting up the live data with deserializer
     */
    private final LiveData<List<GroceryList>> groceryListLiveData =
            Transformations.map(liveData, new Deserializer());

    /**
     * Deserializer class to transform the data from firebase
     */
    private class Deserializer implements Function<DataSnapshot, List<GroceryList>> {
        /**
         * parsing the date returned fromm firebase
         * @param dataSnapshot from firebase
         * @return list of grocery list
         */
        @Override
        public List<GroceryList> apply(DataSnapshot dataSnapshot) {
            Timber.d("Parsing grocery list dataSnapshot.");
            List<GroceryList> groceryLists = new ArrayList<>();
            for(DataSnapshot data: dataSnapshot.getChildren()){
                GroceryList groceryList = data.getValue(GroceryList.class);
                groceryList.setId(data.getKey());
                groceryLists.add(groceryList);
            }
            Timber.d( "Size of the grocery list %s", groceryLists.size());
            return groceryLists;
        }
    }

    /**
     * Setting up the live data
     * @return
     */
    public LiveData<List<GroceryList>> getGroceryList() {
        Timber.d("Getting GroceryList ");
        return groceryListLiveData;
    }

}

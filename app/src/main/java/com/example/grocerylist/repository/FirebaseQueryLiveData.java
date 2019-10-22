package com.example.grocerylist.repository;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import timber.log.Timber;

/**
 * This is a helper class that reduces the amount of firebase queries
 * This is also allows the list to be updated when the data is changed in the db
 * For example when the list is shared by other user
 */
public class FirebaseQueryLiveData extends LiveData<DataSnapshot> {

    private static final String TAG = FirebaseQueryLiveData.class.getSimpleName();
    private final Query query;
    private final MyValueEventListener listener = new MyValueEventListener();
    private boolean listenerRemovePending = false;

    private final Handler handler = new Handler();

    /**
     * Runnable to remove the listener
     */
    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            query.removeEventListener(listener);
            listenerRemovePending = false;
        }
    };

    /**
     * Constructor for query
     * @param query of the item we want to query
     */
    public FirebaseQueryLiveData(Query query){
        this.query = query;
    }

    /**
     * Constructor for reference
     * @param ref that we want to pull the data from
     */
    public FirebaseQueryLiveData(DatabaseReference ref){
        this.query = ref;
    }

    /**
     * Checks if the live data is active or not
     * Adds or removes the listener
     */
    @Override
    protected void onActive() {
        super.onActive();
        Timber.d("%s [onActive]", TAG);
        if (listenerRemovePending) {
            handler.removeCallbacks(removeListener);
        }
        else {
            query.addValueEventListener(listener);
        }
        listenerRemovePending = false;
    }

    /**
     * Starts the runnable and removes the listener after 2 seconds
     * this is usefull when the device is rotated and we wont  query the db again
     */
    @Override
    protected void onInactive() {
        super.onInactive();
        Timber.d( "%s [onInactive]", TAG);
        handler.postDelayed(removeListener, 2000);
        listenerRemovePending = true;
    }

    /**
     * Value listener of firebase
     */
    private class MyValueEventListener implements ValueEventListener{

        /**
         * When the data is changed
         * @param dataSnapshot of the collection
         */
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(dataSnapshot);
        }

        /**
         * when the request got cancelled
         * @param databaseError that describes what happened
         */
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Timber.d( "Could not listened to query: %s", databaseError.getMessage());
        }
    }
}

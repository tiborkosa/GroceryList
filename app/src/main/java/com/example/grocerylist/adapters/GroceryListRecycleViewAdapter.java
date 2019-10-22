package com.example.grocerylist.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerylist.R;
import com.example.grocerylist.entities.GroceryList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Recycler view adapter for displaying the grocery list items
 */
public class GroceryListRecycleViewAdapter extends RecyclerView.Adapter<GroceryListRecycleViewAdapter.GroceryListHolder> {

    private final List<GroceryList> mGroceryList;
    private final int mNumberOfItems;
    private static final String TAG = GroceryListRecycleViewAdapter.class.getSimpleName();

    /**
     * Constructor to set up the data
     * @param groceryLists array list to be displayed
     */
    public GroceryListRecycleViewAdapter(List<GroceryList> groceryLists){

        mGroceryList = groceryLists;
        int size = 0;
        if(groceryLists != null){
            size = groceryLists.size();
        }
        mNumberOfItems = size;
    }

    /**
     * Create the view holder and inflate the item res file
     * @param parent where we want to inflate the items
     * @param viewType
     * @return a new view holder
     */
    @NonNull
    @Override
    public GroceryListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Timber.d("onCreateViewHolder");
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.rv_gl_item, parent, false);

        return new GroceryListHolder(view);
    }

    /**
     * Binding the data to the views
     * @param holder parent holder
     * @param position position of the item to be bind
     */
    @Override
    public void onBindViewHolder(@NonNull GroceryListHolder holder, int position) {
        holder.bind(mGroceryList.get(position));
    }

    /**
     * The count of the item in the list
     * @return number if elements in the list
     */
    @Override
    public int getItemCount() {
        return mNumberOfItems;
    }

    /**
     * View holder class
     */
    class GroceryListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_gli_name) TextView mListName;
        @BindView(R.id.tv_gli_due_date_text) TextView mDueDate;

        /**
         * Constructor of the view holder
         * @param view of the element
         */
        public GroceryListHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }

        /**
         * Binding the data to the view
         * @param groceryList data
         */
        private void bind(GroceryList groceryList){
            mListName.setText(groceryList.getListName());
            mDueDate.setText(groceryList.getDueDate());
        }
    }
}

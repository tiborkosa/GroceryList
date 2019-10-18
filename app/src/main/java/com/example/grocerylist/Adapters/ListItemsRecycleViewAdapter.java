package com.example.grocerylist.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerylist.R;
import com.example.grocerylist.Util.MyApplication;
import com.example.grocerylist.entities.ListItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Recycler view adapter for displaying the list items
 */
public class ListItemsRecycleViewAdapter extends RecyclerView.Adapter<ListItemsRecycleViewAdapter.ListItemsHolder> {

    private final List<ListItem> mListItems;
    private final int mNumberOfItems;
    private OnItemClicked mCallback;
    private static final String TAG = ListItemsRecycleViewAdapter.class.getSimpleName();
    private static String[] measurements = MyApplication.getAppContext().getResources().getStringArray(R.array.unit_measure_imperial);


    /**
     * On click interface to be implemented in the calling class
     * This is performed when the checkbox is clicked
     * Will mark the item as purchased
     * Returns the item and the position in the adapter
     */
    public interface OnItemClicked {
        void onCheckBoxClicked(ListItem listItem, int position);
    }

    /**
     * Constructor to set up the data
     * @param listItems array list to be displayed
     * @param callback of the item clicked
     */
    public ListItemsRecycleViewAdapter(List<ListItem> listItems, OnItemClicked callback ){
        mListItems = listItems;
        int size = 0;
        if(listItems != null){
            size = listItems.size();
        }
        mNumberOfItems = size;
        mCallback = callback;
    }

    /**
     * Create the view holder and inflate the item res file
     * @param parent where we want to inflate the items
     * @param viewType
     * @return a new view holder
     */
    @NonNull
    @Override
    public ListItemsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder");
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.rv_list_items, parent, false);

        return new ListItemsHolder(view);
    }

    /**
     * Binding the data to the views
     * @param holder parent holder
     * @param position position of the item to be bind
     */
    @Override
    public void onBindViewHolder(@NonNull ListItemsHolder holder, int position) {
        holder.bind(mListItems.get(position));
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
    class ListItemsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_list_item_name) TextView mListName;
        @BindView(R.id.tv_amount) TextView mAmount;
        @BindView(R.id.tv_measure_unit) TextView mUnitMeasure;
        @BindView(R.id.cb_purchased) CheckBox mIsPurchased;


        /**
         * Constructor of the view holder
         * @param view of the element
         */
        public ListItemsHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }

        /**
         * Binding the data to the view
         * @param listItem data
         */
        private void bind(ListItem listItem){
            mListName.setText(listItem.getName());
            mAmount.setText(String.valueOf(listItem.getQuantity()));
            mUnitMeasure.setText(measurements[listItem.getMeasure()]);
            if(listItem.isPurchased()){
                mIsPurchased.setChecked(true);
            }
        }

        /**
         * Checkbox clicked listener and setting up the callback
         * @param v that was clicked
         */
        @OnClick(R.id.cb_purchased)
        public void onCheckedBoxClicked(View v){
            CheckBox checkBox = (CheckBox) v;
            if(checkBox != null){
                ListItem listItem = mListItems.get(getAdapterPosition());
                listItem.setPurchased(checkBox.isChecked());
                mCallback.onCheckBoxClicked(mListItems.get(getAdapterPosition()), getAdapterPosition());
            }
        }
    }
}

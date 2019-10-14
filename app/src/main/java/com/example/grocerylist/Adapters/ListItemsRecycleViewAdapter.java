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

public class ListItemsRecycleViewAdapter extends RecyclerView.Adapter<ListItemsRecycleViewAdapter.ListItemsHolder> {

    private final List<ListItem> mListItems;
    private final int mNumberOfItems;
    private OnItemClicked mCallback;
    private static final String TAG = ListItemsRecycleViewAdapter.class.getSimpleName();
    private static String[] measurements = MyApplication.getAppContext().getResources().getStringArray(R.array.unit_measure_imperial);


    public interface OnItemClicked {
        void onCheckBoxClicked(ListItem listItem, boolean isSelected);
    }
    public ListItemsRecycleViewAdapter(List<ListItem> listItems, OnItemClicked callback ){

        mListItems = listItems;
        int size = 0;
        if(listItems != null){
            size = listItems.size();
        }
        mNumberOfItems = size;
        mCallback = callback;
    }

    @NonNull
    @Override
    public ListItemsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder");
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.rv_list_items, parent, false);

        return new ListItemsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemsHolder holder, int position) {
        holder.bind(mListItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mNumberOfItems;
    }

    class ListItemsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_list_item_name) TextView mListName;
        @BindView(R.id.tv_amount) TextView mAmount;
        @BindView(R.id.tv_measure_unit) TextView mUnitMeasure;


        public ListItemsHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }

        private void bind(ListItem listItem){
            mListName.setText(listItem.getName());
            mAmount.setText(String.valueOf(listItem.getQuantity()));
            mUnitMeasure.setText(measurements[listItem.getMeasure()]);
        }

        @OnClick(R.id.cb_purchased)
        public void onCheckedBoxClicked(View v){
            CheckBox checkBox = (CheckBox) v;
            if(checkBox != null){
                mCallback.onCheckBoxClicked(mListItems.get(getAdapterPosition()), checkBox.isChecked());
            }
            if(checkBox.isChecked()){
                Log.d(TAG, "item is selected");
            } else {
                Log.d(TAG, "item is not selected");
            }
        }
    }
}

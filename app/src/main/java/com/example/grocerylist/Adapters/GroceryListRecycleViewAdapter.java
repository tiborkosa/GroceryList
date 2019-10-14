package com.example.grocerylist.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerylist.R;
import com.example.grocerylist.entities.GroceryList;

import java.util.List;

public class GroceryListRecycleViewAdapter extends RecyclerView.Adapter<GroceryListRecycleViewAdapter.GroceryListHolder> {

    private final List<GroceryList> mGroceryList;
    private final int mNumberOfItems;
    private static final String TAG = GroceryListRecycleViewAdapter.class.getSimpleName();

    public GroceryListRecycleViewAdapter(List<GroceryList> groceryLists){

        mGroceryList = groceryLists;
        int size = 0;
        if(groceryLists != null){
            size = groceryLists.size();
        }
        mNumberOfItems = size;
    }

    @NonNull
    @Override
    public GroceryListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder");
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.rv_gl_item, parent, false);

        return new GroceryListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryListHolder holder, int position) {
        holder.bind(mGroceryList.get(position));
    }

    @Override
    public int getItemCount() {
        return mNumberOfItems;
    }

    class GroceryListHolder extends RecyclerView.ViewHolder {

        private final TextView mListName;
        private final TextView mDueDate;

        public GroceryListHolder(View view){
            super(view);
            mListName = view.findViewById(R.id.tv_gli_name);
            mDueDate = view.findViewById(R.id.tv_gli_due_date_text);
        }

        private void bind(GroceryList groceryList){
            mListName.setText(groceryList.getListName());
            mDueDate.setText(groceryList.getCreateDate());
        }
    }
}

package com.example.grocerylist.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerylist.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Recycler view adapter for displaying the list of user emails to share the list with
 */
public class ShareEmailRecycleViewAdapter extends RecyclerView.Adapter<ShareEmailRecycleViewAdapter.ListItemsHolder> {

    private final List<String> mListItems;
    private final int mNumberOfItems;
    private OnItemClicked mCallback;
    private static final String TAG = ShareEmailRecycleViewAdapter.class.getSimpleName();

    /**
     * On click interface to be implemented in the calling class
     * This is used if we want to remove an item
     */
    public interface OnItemClicked {
        void onDeleteItem(int position);
    }

    /**
     * Constructor to set up the data
     * @param emailList array list to be displayed
     * @param callback of the item clicked
     */
    public ShareEmailRecycleViewAdapter(List<String> emailList, OnItemClicked callback ){
        Log.d(TAG, "ShareEmailRecycleViewAdapter");
        mListItems = emailList;
        int size = 0;
        if(emailList != null){
            size = emailList.size();
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
                .inflate(R.layout.rv_share_email_item, parent, false);

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

        @BindView(R.id.tv_rv_email) TextView mEmail;

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
         * @param email data
         */
        private void bind(String email){
            mEmail.setText(email);
        }

        /**
         * Icon clicked listener and setting up the callback
         * @param v that was clicked
         */
        @OnClick(R.id.rv_delete_email)
        public void onEmailDeleted(View v){
            ImageButton img_btn = (ImageButton) v;
            if(img_btn != null){
                mCallback.onDeleteItem(getAdapterPosition());
            }
        }
    }
}

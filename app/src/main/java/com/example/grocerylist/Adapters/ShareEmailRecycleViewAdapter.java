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

public class ShareEmailRecycleViewAdapter extends RecyclerView.Adapter<ShareEmailRecycleViewAdapter.ListItemsHolder> {

    private final List<String> mListItems;
    private final int mNumberOfItems;
    private OnItemClicked mCallback;
    private static final String TAG = ShareEmailRecycleViewAdapter.class.getSimpleName();

    public interface OnItemClicked {
        void onDeleteItem(int position);
    }

    public ShareEmailRecycleViewAdapter(List<String> emailList, OnItemClicked callback ){

        mListItems = emailList;
        int size = 0;
        if(emailList != null){
            size = emailList.size();
        }
        mNumberOfItems = size;
        mCallback = callback;
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ListItemsHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Log.d(TAG, "detached called here");
        mCallback = null;
    }

    @NonNull
    @Override
    public ListItemsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder");
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.rv_share_email_item, parent, false);

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

        @BindView(R.id.tv_rv_email) TextView mEmail;


        public ListItemsHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }

        private void bind(String email){
            mEmail.setText(email);
        }

        @OnClick(R.id.rv_delete_email)
        public void onEmailDeleted(View v){
            ImageButton img_btn = (ImageButton) v;
            if(img_btn != null){
                mCallback.onDeleteItem(getAdapterPosition());
            }
        }
    }
}

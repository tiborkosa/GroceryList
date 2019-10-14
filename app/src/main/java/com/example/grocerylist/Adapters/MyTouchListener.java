package com.example.grocerylist.Adapters;

import android.graphics.Rect;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerylist.R;
import com.example.grocerylist.Util.MyApplication;

public class MyTouchListener  implements RecyclerView.OnItemTouchListener {

    private OnTouchActionListener mOnTouchActionListener;
    private GestureDetectorCompat mGestureDetector;

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    public interface OnTouchActionListener {
        void onClick(int position);
        void onSharedClicked(int position);
    }

    public MyTouchListener(final RecyclerView recyclerView,
                           OnTouchActionListener onTouchActionListener){

        mOnTouchActionListener = onTouchActionListener;
        mGestureDetector = new GestureDetectorCompat(MyApplication.getAppContext(),new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                // Find the item view that was swiped based on the coordinates
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                int childPosition = recyclerView.getChildAdapterPosition(child);

                // get the share button
                // NOTE: everything from the share icon's left will not trigger the list to open
                View view = child.findViewById(R.id.ib_share_list);
                if(view != null) {
                    if (e.getRawX() >= view.getLeft()) {
                        Log.d("MyTouchListener", "Shared button was clicked");
                        mOnTouchActionListener.onSharedClicked(childPosition);
                        return true;
                    }
                }

                mOnTouchActionListener.onClick(childPosition);
                return false;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                                   float velocityX, float velocityY) {
                return false;
            }
        });
    }
}

package com.example.grocerylist.adapters;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerylist.R;
import com.example.grocerylist.util.MyApplication;

/**
 *  Helper class to perform click on the RecyclerView
 *  This will distinguishes the area of the user click/ touch to perform different actions
 */
public class MyTouchListener  implements RecyclerView.OnItemTouchListener {

    private OnTouchActionListener mOnTouchActionListener;
    private GestureDetectorCompat mGestureDetector;

    /**
     * Interfaces to be implemented in the calling class
     * onClick will be called on the left side of the recycler view's item
     * onShareClicked will be called if user clicks on the share icon
     */
    public interface OnTouchActionListener {
        void onClick(int position);
        void onSharedClicked(int position);
    }

    /**
     * Adds the touch event to the gestureDetector
     * @param rv recycler view
     * @param e motion event
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    /**
     * Not needed
     * @param rv
     * @param e
     */
    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {}

    /**
     * Not needed
     * @param disallowIntercept
     */
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}

    /**
     * Constructor to set up the listener and the GestureDetector
     * @param recyclerView where the events will be performed
     * @param onTouchActionListener implemented callback in the calling class
     */
    public MyTouchListener(final RecyclerView recyclerView,
                           OnTouchActionListener onTouchActionListener){

        mOnTouchActionListener = onTouchActionListener;
        mGestureDetector = new GestureDetectorCompat(MyApplication.getAppContext(),new GestureDetector.SimpleOnGestureListener(){

            /**
             * Catching the single tap event
             * If the event is the right side of the share icon, initialize the onSharedClicked callback
             * Else the onClick
             * @param e event of the touch
             * @return true if we want to stop further action
             */
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                // Find the item view that was swiped based on the coordinates
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                int childPosition = recyclerView.getChildAdapterPosition(child);

                // Since the recycler view's height is match parent we need to return if child is not found
                if(childPosition < 0) return true;

                // get the share button
                // NOTE: everything from the share icon's left will not trigger the list to open
                View view = child.findViewById(R.id.ib_share_list);
                if(view != null && childPosition >=0) {
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

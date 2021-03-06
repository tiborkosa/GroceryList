package com.example.grocerylist.adapters;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerylist.R;
import com.example.grocerylist.util.MyApplication;

import timber.log.Timber;

/**
 * Helper class to perform left and right swipe on the recycler view
 */
public class MyItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private static final String TAG = MyItemTouchHelper.class.getSimpleName();
    private OnItemTouchActionListener mCallback;

    /**
     * Interfaces to be implemented in the calling class
     * Both will return the position of the recycler view where the action was performed
     */
    public interface OnItemTouchActionListener {
        void onLeftSwipe(int position);
        void onRightSwipe(int position);
    }

    /**
     * Constructor to set up the listeners and the swipe directions
     * @param listener of the implemented interface
     */
    public MyItemTouchHelper(OnItemTouchActionListener listener) {
        super(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT);
        mCallback = listener;
    }

    /**
     * Not used
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    /**
     * Setting up the callback depending of the swipe
     * @param viewHolder of the item swiped
     * @param direction of the item swiped
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if(direction == ItemTouchHelper.LEFT){
            mCallback.onLeftSwipe(viewHolder.getAdapterPosition());
            Timber.d( "left swipe");
        }else if(direction == ItemTouchHelper.RIGHT){
            mCallback.onRightSwipe(viewHolder.getAdapterPosition());
            Timber.d("right swipe");
        }
    }

    /**
     * Not needed
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    /**
     * This is needed to show the background after the swipe happened
     * We are adding the icons and the background color
     * @param c canvas to be drawn on
     * @param recyclerView container
     * @param viewHolder that was swiped
     * @param dX x coordinated
     * @param dY y coordinates
     * @param actionState
     * @param isCurrentlyActive
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20; //so background is behind the rounded corners of itemView
        Drawable icon = ContextCompat.getDrawable(MyApplication.getAppContext(),
                R.drawable.ic_edit_24px);
        ColorDrawable background = new ColorDrawable(Color.TRANSPARENT);

        if(dX > 0){
            background = new ColorDrawable(MyApplication.getAppContext().getResources().getColor(R.color.colorPrimary));
        } else if(dX < 0){
            icon = ContextCompat.getDrawable(MyApplication.getAppContext(),
                    R.drawable.ic_delete_24px);
            background = new ColorDrawable(MyApplication.getAppContext().getResources().getColor(R.color.darkRed));
        }

        int top = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int left = itemView.getWidth() - icon.getIntrinsicWidth() - (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int right = left + icon.getIntrinsicHeight();
        int bottom = top + icon.getIntrinsicHeight();

        if (dX > 0 && isCurrentlyActive) { // Swiping to the right
            icon.setBounds(40, top, icon.getIntrinsicWidth()+40, bottom);
            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset - 20, itemView.getBottom());
        } else if (dX < 0 && isCurrentlyActive) { // Swiping to the left
            icon.setBounds(left, top, right, bottom);
            background.setBounds(itemView.getRight()+ 20 + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        if(dX < -icon.getIntrinsicHeight() || dX > (icon.getIntrinsicHeight()+20)) icon.draw(c);
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}

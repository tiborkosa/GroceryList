package com.example.grocerylist.Adapters;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerylist.R;
import com.example.grocerylist.Util.MyApplication;

public class MyItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private static final String TAG = MyItemTouchHelper.class.getSimpleName();
    private Drawable icon;
    private ColorDrawable background;
    private OnItemTouchActionListener mCallback;

    public interface OnItemTouchActionListener {
        void onLeftSwipe(int position);
        void onRightSwipe(int position);
    }

    public MyItemTouchHelper(OnItemTouchActionListener listener) {
        super(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT);
        mCallback = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if(direction == ItemTouchHelper.LEFT){
            mCallback.onLeftSwipe(viewHolder.getAdapterPosition());
            Log.d(TAG, "left swipe");
        }else if(direction == ItemTouchHelper.RIGHT){
            mCallback.onRightSwipe(viewHolder.getAdapterPosition());
            Log.d(TAG, "right swipe");
        }
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20; //so background is behind the rounded corners of itemView
        icon = ContextCompat.getDrawable(MyApplication.getAppContext(),
                R.drawable.ic_edit_24px);
        background = new ColorDrawable(Color.TRANSPARENT);

        if(dX > 0){
            background = new ColorDrawable(Color.GREEN);
        } else if(dX < 0){
            icon = ContextCompat.getDrawable(MyApplication.getAppContext(),
                    R.drawable.ic_delete_24px);
            background = new ColorDrawable(Color.RED);
        }

        int top = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int left = itemView.getWidth() - icon.getIntrinsicWidth() - (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int right = left + icon.getIntrinsicHeight();
        int bottom = top + icon.getIntrinsicHeight();

        if (dX > 0 && isCurrentlyActive) { // Swiping to the right
            icon.setBounds(20, top, icon.getIntrinsicWidth()+20, bottom);
            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset - 40, itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left
            icon.setBounds(left, top, right, bottom);
            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        if(dX < -icon.getIntrinsicHeight() || dX > (icon.getIntrinsicHeight()+40)) icon.draw(c);
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}

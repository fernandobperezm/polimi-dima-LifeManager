package fernandoperez.lifemanager.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by fernandoperez on 2/4/17.
 */

/**
 * The class RecyclerItemClickListener implements the OnItemTouchListener interface so every element
 * in a Recycler View can be clicked and perform an specific action.
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener mListener;
    GestureDetector mGestureDetector;

    // TODO: Javadocs.
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    // TODO: Javadocs.
    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    // TODO: Javadocs.
    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }
        return false;
    }

    // TODO: Javadocs.
    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    // TODO: Javadocs.
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
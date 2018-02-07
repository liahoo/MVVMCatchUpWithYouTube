package com.aguosoft.app.videocamp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.aguosoft.app.videocamp.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by liang on 2016/09/01.
 */
public abstract class BindingListAdapter<T> extends RecyclerView.Adapter<BindingHolder>{
    private static final String TAG = BindingListAdapter.class.getSimpleName();
    private final RecyclerView mRecyclerView;
    private OnItemClickListener mOnItemClickListener;
    protected ArrayList<T> mList = new ArrayList<>();
    private RecyclerView.OnItemTouchListener mRecyclerItemClickListener;


    public BindingListAdapter(RecyclerView recyclerView) {
        this(recyclerView, null);
    }

    public BindingListAdapter(RecyclerView recyclerView, Collection<T> list) {
        mRecyclerView = recyclerView;
            addAll(list);
    }

    public void add(T item){
        mList.add(item);
    }
    public void addAll(Collection<T> items) {
        if (items != null && items.size() > 0)
            mList.addAll(items);

    }

    public void clear(){
        mList.clear();
    }

    public T getItem(int position) {
        if (position < mList.size()) {
            return mList.get(position);
        } else
            return null;
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
    public LayoutInflater getLayoutInflater(){
        return LayoutInflater.from(mRecyclerView.getContext());
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
        if (mRecyclerItemClickListener != null)
            mRecyclerView.removeOnItemTouchListener(mRecyclerItemClickListener);
        mRecyclerItemClickListener = new RecyclerItemClickListener(
                mRecyclerView.getContext(), mOnItemClickListener);
        mRecyclerView.addOnItemTouchListener(mRecyclerItemClickListener);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;


        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            View itemSelector = childView.findViewById(R.id.item_selector);
            if(null == itemSelector){
                itemSelector = childView;
            }
            switch(e.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    itemSelector.setPressed(true);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    itemSelector.setPressed(false);
                    break;
            }
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            if(com.aguosoft.app.videocamp.fragment.AppConfig.SHOW_LOG)
                Log.i(TAG, "[onRequestDisallowInterceptTouchEvent] disallowIntercept:"+disallowIntercept);
            // do nothing
        }
    }

}

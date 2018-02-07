package com.aguosoft.app.videocamp.view;

import android.content.Context;
import android.graphics.Point;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Checkable;
import android.widget.LinearLayout;

@CoordinatorLayout.DefaultBehavior(BottomNavigationBehavior.class)
public class TabMenu extends LinearLayout {
	public interface TabMenuListener {
		void onTabChanged(int pos, boolean byUser);
	}
	private TabMenuListener mTabListener;

	public TabMenu(Context context) {
		super(context);
	}

	public TabMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TabMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onViewAdded(View child) {
		super.onViewAdded(child);
		child.setOnClickListener(mChildViewClickListener);
	}

	private OnClickListener mChildViewClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setCurrentTab(v, true);
		}

	};
	private Checkable mCurrentTab;

	public void setTabListener(TabMenuListener l) {
		mTabListener = l;
	}

	public void setCurrentTab(int pos, boolean byUser) {
		setCurrentTab(getChildAt(pos), pos, byUser);
	}

	protected void setCurrentTab(View v, boolean byUser) {
		setCurrentTab(v, indexOfChild(v), byUser);
	}

	protected void setCurrentTab(View v, int pos, boolean byUser) {
		if(v instanceof Checkable == false){
			throw new IllegalArgumentException("The View for tab item has to implement Checkable!");
		}
		if (mCurrentTab != null) {
			mCurrentTab.setChecked(false);
		}
		if (v != null && v instanceof Checkable) {
			mCurrentTab = (Checkable) v;
			mCurrentTab.setChecked(true);
		}
		if (mTabListener != null)
			mTabListener.onTabChanged(pos, byUser);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int child_count = getChildCount();
		int full_width = getScreenWidth(getContext());
		if (child_count > 0 && full_width > getMeasuredWidth()) {
			int average_width = full_width / child_count;
			for (int i = 0; i < child_count; i++) {
				View child = getChildAt(i);
				LayoutParams params = (LayoutParams) child.getLayoutParams();
				params.width = average_width;
				child.setLayoutParams(params);
			}
			setMeasuredDimension(full_width, getMeasuredHeight());
		}
	}

	public static int getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size.x;
	}
}
package com.aguosoft.app.videocamp.view;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

public class CheckableImageView extends ImageView implements Checkable
{

	public interface OnCheckedChangeListener
	{
		void onChecked(CheckableImageView checkableButton, boolean checked);
	}

	private boolean mChecked;
	private OnCheckedChangeListener mOnCheckedListener;
	private static final int[] CheckedStateSet = {
			android.R.attr.state_checked,
	};

	public CheckableImageView(Context context)
	{
		super(context);
	}

	public CheckableImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public CheckableImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public int[] onCreateDrawableState(int extraSpace)
	{
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
		if (isChecked())
		{
			mergeDrawableStates(drawableState, CheckedStateSet);
		}
		return drawableState;
	}

	@Override
	public boolean isChecked()
	{
		return mChecked;
	}


	@BindingAdapter("checked")
	public static void setChecked(CheckableImageView view, boolean checked){
		view.setChecked(checked);
	}
	@Override
	public void setChecked(boolean checked)
	{
		boolean isCheckedChanged = (mChecked != checked);
		mChecked = checked;
		refreshDrawableState();
		if (isCheckedChanged && mOnCheckedListener != null)
			mOnCheckedListener.onChecked(this, checked);
	}

	public void setOnCheckedChangeListener(OnCheckedChangeListener l)
	{
		mOnCheckedListener = l;
	}

	@Override
	public void toggle()
	{
		setChecked(!mChecked);
	}

	@Override
	public boolean performClick()
	{
		toggle();
		return super.performClick();
	}

}

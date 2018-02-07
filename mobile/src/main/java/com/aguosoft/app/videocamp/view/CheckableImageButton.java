package com.aguosoft.app.videocamp.view;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageButton;

public class CheckableImageButton extends ImageButton implements Checkable
{

	public interface OnCheckedChangeListener
	{
		void onChecked(CheckableImageButton checkableButton, boolean checked);
	}

	private boolean mChecked;
	private OnCheckedChangeListener mOnCheckedListener;
	private static final int[] CheckedStateSet = {
			android.R.attr.state_checked,
	};

	public CheckableImageButton(Context context)
	{
		super(context);
	}

	public CheckableImageButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public CheckableImageButton(Context context, AttributeSet attrs, int defStyle)
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

	@Override
	public void setChecked(boolean checked)
	{
		boolean isCheckedChanged = (mChecked != checked);
		mChecked = checked;
		refreshDrawableState();
		if (isCheckedChanged && mOnCheckedListener != null)
			mOnCheckedListener.onChecked(this, checked);
	}

    @BindingAdapter("checked")
    public static void setChecked(CheckableImageButton view, boolean checked){
        view.setChecked(checked);
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

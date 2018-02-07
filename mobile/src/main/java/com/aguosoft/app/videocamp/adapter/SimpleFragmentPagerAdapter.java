package com.aguosoft.app.videocamp.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class SimpleFragmentPagerAdapter<Pager extends SimpleFragmentPagerAdapter.Pagerable> extends FragmentStatePagerAdapter
{
	public interface Pagerable
	{
		CharSequence getTitle();

		Object getPageContent();
	}

	private ArrayList<Pager> mPagerableList;

	public SimpleFragmentPagerAdapter(FragmentManager fm, ArrayList<Pager> pagerable_list)
	{
		super(fm);
		mPagerableList = pagerable_list;
	}

	@Override
	public Parcelable saveState()
	{
		return null;
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		if (mPagerableList != null && position >= 0 && position < mPagerableList.size())
			return mPagerableList.get(position).getTitle();
		return null;
	}

	@Override
	public Fragment getItem(int position)
	{
		return (Fragment) mPagerableList.get(position).getPageContent();
	}

	@Override
	public int getCount()
	{
		if (mPagerableList != null)
			return mPagerableList.size();
		return 0;
	}

	@Override
	public int getItemPosition(Object object)
	{
		if (mPagerableList != null)
			return mPagerableList.indexOf(object);
		return -1;
	}

	public ArrayList<Pager> getPageList()
	{
		return mPagerableList;
	}

	public Pager getPageItem(int position)
	{
		if (mPagerableList != null && position >= 0 && position < mPagerableList.size())
			return mPagerableList.get(position);
		return null;
	}

}

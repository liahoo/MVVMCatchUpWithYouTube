package com.aguosoft.app.videocamp.utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

public class StringHelper
{
	public static String makeTimeString(long secs)
	{
		String time_text = "";
		long value_hour = secs / 3600;
		long value_min = (secs % 3600) / 60;
		long value_sec = secs % 60;
		if (value_hour > 0)
		{
			String hour = (value_hour > 10) ? String.valueOf(value_hour) : "0" + String.valueOf(value_hour);
			time_text += hour + ":";
		}
		String min = (value_min / 10 > 0) ? String.valueOf(value_min) : "0" + String.valueOf(value_min);
		String sec = (value_sec / 10 > 0) ? String.valueOf(value_sec) : "0" + String.valueOf(value_sec);
		time_text = time_text + min + ":" + sec;

		return time_text;
	}

	public static String convertTimeStringPnYnMnDTnHnMnS(String pymd_thms)
	{
		if (pymd_thms == null)
			return null;
		pymd_thms = pymd_thms.toUpperCase(Locale.getDefault());
		StringBuffer sb = new StringBuffer();
		int p_index = pymd_thms.indexOf("P");
		int t_index = pymd_thms.indexOf("T");
		if (p_index + 1 < t_index)
		{
			String date_string = pymd_thms.substring(p_index, t_index);
			sb.append(date_string.replace("Y", " Year ").replace("M", " Month ").replace("D", " Day "));
		}
		if (t_index + 1 < pymd_thms.length())
		{
			String time_string = pymd_thms.substring(t_index + 1);
			int start_idx = 0;
			int h_idx = time_string.indexOf("H");
			int m_idx = time_string.indexOf("M");
			int s_idx = time_string.indexOf("S");
			if (h_idx > start_idx)
			{
				String hour = time_string.substring(0, h_idx);
				sb.append(hour).append(":");
				start_idx = h_idx + 1;
			}
			if (m_idx > start_idx)
			{
				String min = time_string.substring(start_idx, m_idx);
				if (min.length() == 1)
					sb.append("0").append(min).append(":");
				else
					sb.append(min).append(":");
				start_idx = m_idx + 1;
			}
			else
			{
				sb.append("00").append(":");
			}
			if (s_idx > start_idx)
			{
				String sec = time_string.substring(start_idx, s_idx);
				if (sec.length() == 1)
					sb.append("0");
				sb.append(sec);
			}
			else
			{
				sb.append("00");
			}
		}
		return sb.toString();
	}

	public static String toShowableNumber(String text)
	{
		if (text == null || text.length() < 4)
			return text;
		int end = text.length();
		int pos = end % 3;
		StringBuffer sb = new StringBuffer();
		if (pos != 0)
			sb.append(text.substring(0, pos)).append(',');
		while ((pos + 3) <= end)
		{
			sb.append(text.substring(pos, pos + 3)).append(',');
			pos += 3;
		}
		return sb.substring(0, sb.length() - 1);
	}

	public static String toAlignedNumber(int no)
	{
		if (no < 10)
		{
			return "0" + String.valueOf(no);
		}
		return String.valueOf(no);
	}

	/**
	 * this method will sub text to new string that has bytes length <= limit bytes length.
	 * 
	 * @param text
	 *            the original text
	 * @param limitBytesLength
	 *            the limit bytes length that want to sub
	 * @return
	 */
	public static String subStringLimitBytesLength(String text, int limitBytesLength)
	{
		int bytesLength = getBytesLength(text);
		Log.e("KST", "sub string text=" + text + ", byteLength=" + bytesLength);
		if (bytesLength <= limitBytesLength)
		{
			Log.e("KST", "result text=" + text);
			return text;
		}
		bytesLength = 0;
		int length = text.length();
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < length; i++)
		{
			char ch = text.charAt(i);
			bytesLength += getBytesLength(ch);
			if (bytesLength <= limitBytesLength)
			{
				stringBuffer.append(ch);
			}
			else
			{
				Log.e("KST", "result string cut=" + stringBuffer.toString());
				return stringBuffer.toString();
			}
		}
		return text;
	}

	/**
	 * support get bytes length of this char
	 * 
	 * @param ch
	 * @return
	 */
	public static int getBytesLength(char ch)
	{
		return Charset.forName("Shift-JIS").encode(CharBuffer.wrap(new char[] { ch })).limit();
	}

	/**
	 * support get bytes length of this string
	 * 
	 * @param text
	 * @return
	 */
	public static int getBytesLength(String text)
	{
		return Charset.forName("Shift-JIS").encode(text).limit();
	}

	public static String join(CharSequence delimiter, List<String> tokens)
	{
		if (delimiter == null || tokens == null || tokens.size() < 1)
		{
			return null;
		}

		StringBuffer str_buff = new StringBuffer();
		str_buff.append(tokens.get(0));
		for (int i = 1; i < tokens.size(); i++)
		{
			str_buff.append(delimiter);
			str_buff.append(tokens.get(i));
		}
		return str_buff.toString();
	}

	public static String getLocalizedString(Activity activity, int string_id, String language)
	{
		Configuration conf = activity.getResources().getConfiguration();
		conf.locale = new Locale(language);
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Resources resources = new Resources(activity.getAssets(), metrics, conf);
		/* get localized string */
		return resources.getString(string_id);

	}
}

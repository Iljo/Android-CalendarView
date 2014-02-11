package org.iljo.android.calendarview.widget;

import java.util.List;

import org.iljo.android.calendarview.R;
import org.iljo.android.calendarview.model.GridAdapterSample;
import org.iljo.android.calendarview.widget.CalendarView.Focus;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


/**
 * @author iljo
 */
public class CalendarGridAdapter extends ArrayAdapter<GridAdapterSample> {

	public CalendarGridAdapter(Context p_context, List<GridAdapterSample> p_objects) {
		super(p_context, R.layout.calendar_cell, p_objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView retVal = (TextView) super.getView(position, convertView, parent);
		attachStyle(retVal, position);

		return retVal;
	}

	private void attachStyle(TextView p_arg, int p_position) {
		GridAdapterSample sample = getItem(p_position);
		p_arg.setTextAppearance(getContext(), sample.getStyleId());

		int[] attrs = new int[] {android.R.attr.background, android.R.attr.visibility};
		TypedArray ta = getContext().obtainStyledAttributes(sample.getStyleId(), attrs);

		int backgroundColor = ta.getColor(0, Color.TRANSPARENT);
		p_arg.setBackgroundColor(backgroundColor);

		int attrVisibilityValue = ta.getInt(1, View.VISIBLE);
		int viewVisibilityValue = fromAttrToViewVisibility(attrVisibilityValue);
		p_arg.setVisibility(viewVisibilityValue);
		if (p_arg.getTag() == Focus.FOCUSED) {
			p_arg.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					getContext().getResources().getDimension(R.dimen.cal_day_focued));
		}

		ta.recycle();
	}

	// TODO: Method is little hardcoded too much, find a better way.
	private int fromAttrToViewVisibility(int p_arg) {
		int retVal = View.VISIBLE;
		switch (p_arg) {
			case 1:
				retVal = View.INVISIBLE;
				break;
			case 2:
				retVal = View.GONE;
				break;
		}
		return retVal;
	}

}

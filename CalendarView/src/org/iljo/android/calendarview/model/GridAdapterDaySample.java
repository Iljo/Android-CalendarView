package org.iljo.android.calendarview.model;

import java.util.Calendar;

/**
 * @author iljo
 */
public class GridAdapterDaySample implements GridAdapterSample {
	private Integer		m_styleId	= null;
	private Calendar	m_day		= null;

	public GridAdapterDaySample(Integer p_styleId, Calendar p_day) {
		m_styleId = p_styleId;
		m_day = p_day;
	}

	public Calendar getDay() {
		return m_day;
	}

	@Override
	public Integer getStyleId() {
		return m_styleId;
	}

	@Override
	public String toString() {
		Integer dayOfMonth = m_day.get(Calendar.DAY_OF_MONTH);
		String retVal = Integer.toString(dayOfMonth);
		return retVal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_day == null) ? 0 : m_day.hashCode());
		result = prime * result + ((m_styleId == null) ? 0 : m_styleId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GridAdapterDaySample other = (GridAdapterDaySample) obj;
		if (m_day == null) {
			if (other.m_day != null)
				return false;
		} else if (!m_day.equals(other.m_day))
			return false;
		if (m_styleId == null) {
			if (other.m_styleId != null)
				return false;
		} else if (!m_styleId.equals(other.m_styleId))
			return false;
		return true;
	}
}

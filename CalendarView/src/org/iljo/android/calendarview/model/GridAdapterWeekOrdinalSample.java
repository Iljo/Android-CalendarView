package org.iljo.android.calendarview.model;

/**
 * @author iljo
 */
public class GridAdapterWeekOrdinalSample implements GridAdapterSample {
	private Integer	m_styleId		= null;
	private Integer	m_ordinalNu		= null;
	private boolean	m_displayText	= true;

	public GridAdapterWeekOrdinalSample(Integer p_styleId, Integer p_ordinalNu) {
		m_styleId = p_styleId;
		m_ordinalNu = p_ordinalNu;
	}

	public GridAdapterWeekOrdinalSample(Integer p_styleId, Integer p_ordinalNu, boolean p_displayText) {
		m_styleId = p_styleId;
		m_ordinalNu = p_ordinalNu;
		m_displayText = p_displayText;
	}

	public Integer getOrdinalNu() {
		return m_ordinalNu;
	}

	@Override
	public Integer getStyleId() {
		return m_styleId;
	}

	@Override
	public String toString() {
		String retVal = "";
		if (m_ordinalNu != null && m_ordinalNu > 0 && m_displayText) {
			retVal = Integer.toString(m_ordinalNu);
		}
		return retVal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_ordinalNu == null) ? 0 : m_ordinalNu.hashCode());
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
		GridAdapterWeekOrdinalSample other = (GridAdapterWeekOrdinalSample) obj;
		if (m_ordinalNu == null) {
			if (other.m_ordinalNu != null)
				return false;
		} else if (!m_ordinalNu.equals(other.m_ordinalNu))
			return false;
		if (m_styleId == null) {
			if (other.m_styleId != null)
				return false;
		} else if (!m_styleId.equals(other.m_styleId))
			return false;
		return true;
	}

}

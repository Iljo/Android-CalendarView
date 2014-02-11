package org.iljo.android.calendarview.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author iljo
 */
public class DateUtil {
    private static final Calendar         m_today = Calendar.getInstance();
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat m_sdf   = new SimpleDateFormat("MMMM, yyyy");

    /**
     * Compares the day represented by this {@code Calendar} to one that repersents today
     * 
     * @param p_cal
     *        the object to compare to this instance.
     * @return 0 if the days of the two {@code Calendar}s are equal, -1 if the day of this {@code Calendar} is before
     *         the today, 1 if the day of this {@code Calendar} is after today.
     */
    public static Integer compareWithToday(Calendar p_cal) {
        Calendar comparer = (Calendar) m_today.clone();
        comparer.set(Calendar.YEAR, p_cal.get(Calendar.YEAR));
        comparer.set(Calendar.MONTH, p_cal.get(Calendar.MONTH));
        comparer.set(Calendar.DAY_OF_MONTH, p_cal.get(Calendar.DAY_OF_MONTH));

        Integer retVal = comparer.compareTo(m_today);
        return retVal;
    }

    public static String format(Calendar p_cal) {
        String retVal = m_sdf.format(p_cal.getTime());
        return retVal;
    }

    /**
     * @param p_cal1
     * @param p_cal2
     * @return p_cal1 - p_cal2 in weeks, <code>null</code> if one of them is null
     */
    public static Integer differnceBetweenWeeks(Calendar p_cal1, Calendar p_cal2) {
        if (p_cal1 == null || p_cal2 == null) {
            return null;
        }
        Calendar cal1 = (Calendar) p_cal1.clone();
        Calendar cal2 = (Calendar) p_cal1.clone();
        cal2.set(Calendar.YEAR, p_cal2.get(Calendar.YEAR));
        cal2.set(Calendar.MONTH, p_cal2.get(Calendar.MONTH));
        cal2.set(Calendar.WEEK_OF_MONTH, p_cal2.get(Calendar.WEEK_OF_MONTH));
        int counter = 0;
        int adder = compareWeeks(cal1, cal2);
        while (adder != 0) {
            counter += adder;
            cal2.add(Calendar.WEEK_OF_MONTH, adder);
            adder = compareWeeks(cal1, cal2);
        }

        return counter;
    }

    /**
     * @param p_cal1
     * @param p_cal2
     * @return -1 or 1
     */
    private static int compareWeeks(Calendar p_cal1, Calendar p_cal2) {
        int a[] = {Calendar.YEAR, Calendar.MONTH, Calendar.WEEK_OF_MONTH};
        int difference = 0;
        for (int i = 0; i < a.length; i++) {
            difference = p_cal1.get(a[i]) - p_cal2.get(a[i]);
            if (difference != 0) {
                break;
            }
        }
        int retVal = 0;
        if (difference != 0) {
            retVal = difference / Math.abs(difference);
        }
        return retVal;
    }
}

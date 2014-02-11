package org.iljo.android.calendarview.widget;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.iljo.android.calendarview.R;
import org.iljo.android.calendarview.model.GridAdapterDaySample;
import org.iljo.android.calendarview.model.GridAdapterSample;
import org.iljo.android.calendarview.model.GridAdapterWeekOrdinalSample;
import org.iljo.android.calendarview.util.DateUtil;

import android.app.Service;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

/**
 * @author iljo
 */
public class CalendarView extends FrameLayout {
    private static final int        NUM_OF_WEEKS       = 6;

    private CalendarViewListener    m_listener         = null;
    private List<GridAdapterSample> m_gridSamples      = new LinkedList<GridAdapterSample>();
    private Calendar                m_currentCal       = Calendar.getInstance();
    private Calendar                m_dayInFirstWeek   = null;
    private Integer                 m_weeksToCount     = null;
    private Set<Calendar>           m_highlightedDays  = new HashSet<Calendar>();
    private Set<Integer>            m_highlightedWeeks = new HashSet<Integer>();
    private GridView                m_calendarGridView;

    enum Focus {
        FOCUSED, UNFOCUSED
    }

    public interface CalendarViewListener {
        public void daySelected(boolean p_highlighted, Calendar p_day);

        public void weekSelected(boolean p_highlighted, Integer p_weekNu);
    }

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, 0);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        m_calendarGridView = (GridView) layoutInflater.inflate(R.layout.calendar_view, null, false);

        makeNewGridSamples();
        CalendarGridAdapter adapter = new CalendarGridAdapter(getContext(), m_gridSamples);
        m_calendarGridView.setAdapter(adapter);
        m_calendarGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p_parent, View p_view, int p_position, long p_id) {
                GridAdapterSample sample = (GridAdapterSample) m_calendarGridView.getItemAtPosition(p_position);
                if (m_listener != null) {
                    if (sample instanceof GridAdapterDaySample) {
                        GridAdapterDaySample daySample = (GridAdapterDaySample) sample;
                        Calendar day = (Calendar) daySample.getDay().clone();
                        boolean highlighted = isDayHighlighted(day);
                        m_listener.daySelected(highlighted, day);
                    } else if (sample instanceof GridAdapterWeekOrdinalSample) {
                        GridAdapterWeekOrdinalSample weekSample = (GridAdapterWeekOrdinalSample) sample;
                        Integer ordinalNu = weekSample.getOrdinalNu();
                        boolean highlighted = isWeekSelected(ordinalNu);
                        m_listener.weekSelected(highlighted, ordinalNu);
                    }
                }
                if (sample instanceof GridAdapterDaySample) {
                    View view = p_parent.findViewWithTag(Focus.FOCUSED);
                    if (view != null) {
                        TextView toUnfocus = (TextView) view;
                        toUnfocus.setTag(Focus.UNFOCUSED);
                    }
                    if (p_view instanceof TextView) {
                        TextView toFocus = (TextView) p_view;
                        toFocus.setTag(Focus.FOCUSED);
                    }
                    refreshFocus();
                }
            }
        });

        addView(m_calendarGridView);
    }

    private void refreshFocus() {
        View view = m_calendarGridView.findViewWithTag(Focus.UNFOCUSED);
        TextView day = null;
        if (view != null) {
            day = (TextView) view;
            day.setTag(null);
            day.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.cal_day_unfocued));
        }
        view = m_calendarGridView.findViewWithTag(Focus.FOCUSED);
        if (view != null) {
            day = (TextView) view;
            day.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.cal_day_focued));
        }

    }

    @Override
    protected void onDraw(Canvas p_canvas) {
        refreshFocus();
        System.out.println("evo me sada!!!!");
        super.onDraw(p_canvas);
    }

    public void setListener(CalendarViewListener p_listener) {
        m_listener = p_listener;
    }

    public void makeNewGridSamples() {
        Calendar cal = (Calendar) m_currentCal.clone();
        int month = cal.get(Calendar.MONTH);

        cal.set(Calendar.DAY_OF_MONTH, 1);
        int monthStartDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        // Display Monday as the first day of the week
        cal.add(Calendar.DAY_OF_MONTH, -((monthStartDayOfWeek + (7 - Calendar.MONDAY)) % 7));

        m_gridSamples.clear();
        GridAdapterDaySample daySample = null;
        GridAdapterWeekOrdinalSample weekNuSample;
        for (int i = 0; i < NUM_OF_WEEKS; i++) {
            int daysOfWeekInMonthCount = 0;
            int styleId = R.style.CalendarDay;

            for (int j = 0; j < 7; j++) {
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

                if (cal.get(Calendar.MONTH) != month) {
                    styleId = R.style.CalendarDayNotInMonth;
                } else if (isDayHighlighted(cal)) {
                    styleId = R.style.CalendarDayHighlighted;
                } else if (DateUtil.compareWithToday(cal) < 0) {
                    if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                        styleId = R.style.CalendarHollyDaysBeforeToday;
                    } else {
                        styleId = R.style.CalendarDaysBeforeToday;
                    }
                    daysOfWeekInMonthCount++;
                } else {
                    if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                        styleId = R.style.CalendarHollyDay;
                    } else {
                        styleId = R.style.CalendarDay;
                    }
                    daysOfWeekInMonthCount++;
                }

                daySample = new GridAdapterDaySample(styleId, (Calendar) cal.clone());
                m_gridSamples.add(daySample);
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }

            // Setting style of week number
            Integer weekNu = null;
            boolean displayText = true;
            if (m_dayInFirstWeek != null) {
                weekNu = DateUtil.differnceBetweenWeeks(cal, m_dayInFirstWeek);
            }
            if (daysOfWeekInMonthCount > 0) {
                if (weekNu != null && m_highlightedWeeks.contains(weekNu)) {
                    styleId = R.style.CalendarWeekNuHighlighted;
                } else if (DateUtil.compareWithToday(daySample.getDay()) < 0) {
                    styleId = R.style.CalendarWeekNusBeforeToday;
                } else {
                    styleId = R.style.CalendarWeekNu;
                }
                if (m_weeksToCount != null && weekNu != null && m_weeksToCount < weekNu) {
                    // This is some babywatch thing!!!
                    displayText = false;
                }
            } else {
                styleId = R.style.CalendarWeekNuNotInMonth;
            }
            weekNuSample = new GridAdapterWeekOrdinalSample(styleId, weekNu, displayText);
            m_gridSamples.add(weekNuSample);
        }
    }

    public void setHighlightedDays(Collection<Calendar> p_days) {
        m_highlightedDays.clear();
        for (Calendar day : p_days) {
            addHighlightedDay(day);
        }
    }

    public void addHighlightedDays(Collection<Calendar> p_days) {
        for (Calendar day : p_days) {
            addHighlightedDay(day);
        }
    }

    public void addHighlightedDay(Calendar p_day) {
        Calendar cal;
        cal = (Calendar) m_currentCal.clone();
        cal.setTimeInMillis(0);
        cal.set(Calendar.YEAR, p_day.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, p_day.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, p_day.get(Calendar.DAY_OF_MONTH));

        m_highlightedDays.add(cal);
    }

    public void removeHighlightFromDay(Calendar p_day) {
        Calendar cal;
        cal = (Calendar) m_currentCal.clone();
        cal.setTimeInMillis(0);
        cal.set(Calendar.YEAR, p_day.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, p_day.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, p_day.get(Calendar.DAY_OF_MONTH));

        m_highlightedDays.remove(cal);
    }

    public boolean isDayHighlighted(Calendar p_day) {
        Calendar cal = (Calendar) m_currentCal.clone();
        cal.setTimeInMillis(0);
        cal.set(Calendar.YEAR, p_day.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, p_day.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, p_day.get(Calendar.DAY_OF_MONTH));

        boolean retVal = m_highlightedDays.contains(cal);
        return retVal;
    }

    public boolean isWeekSelected(Integer p_ordinalNu) {
        return m_highlightedWeeks.contains(p_ordinalNu);
    }

    public void setDayInFirstWeek(Calendar p_day) {
        m_dayInFirstWeek = p_day;
    }

    public Integer getWeeksToCount() {
        return m_weeksToCount;
    }

    public void setWeeksToCount(Integer p_weeksToCount) {
        m_weeksToCount = p_weeksToCount;
    }

    public Calendar getDayInFirstWeek() {
        return m_dayInFirstWeek;
    }

    public Set<Integer> getHighlightedWeeks() {
        return m_highlightedWeeks;
    }

    public void setHighlightedWeeks(Set<Integer> p_highlightedWeeks) {
        if (p_highlightedWeeks != null) {
            m_highlightedWeeks = p_highlightedWeeks;
        } else {
            m_highlightedWeeks.clear();
        }
    }

    private void removeTagFromFocused() {
        TextView view = (TextView) m_calendarGridView.findViewWithTag(Focus.FOCUSED);
        if (view != null) {
            view.setTag(null);
        }
    }

    public Calendar toPreviousMonth() {
        removeTagFromFocused();
        m_currentCal.add(Calendar.MONTH, -1);
        return m_currentCal;
    }

    public Calendar toNextMonth() {
        removeTagFromFocused();
        m_currentCal.add(Calendar.MONTH, 1);
        return m_currentCal;
    }

    public Calendar getCurrentCal() {
        return m_currentCal;
    }

    public void setCurrentCal(Calendar p_currentCal) {
        if (p_currentCal == null) {
            return;
        }
        m_currentCal = p_currentCal;
    }

    @Override
    public void invalidate() {
        if (m_currentCal == null) {
            return;
        }
        makeNewGridSamples();
        ((BaseAdapter) m_calendarGridView.getAdapter()).notifyDataSetChanged();
        m_calendarGridView.invalidate();
        super.invalidate();
    }

}

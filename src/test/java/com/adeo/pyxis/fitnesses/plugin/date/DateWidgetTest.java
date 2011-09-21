package com.adeo.pyxis.fitnesses.plugin.date;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import fitnesse.wikitext.widgets.MockWidgetRoot;

/**
 * Test the widgets '!now', '!tomorrow' and '!yesterday'.
 * We test theses widgets in the same class because they share the same
 * parent class and very few code is specific for each widget.
 * 
 * @author Julien Sobczak
 */
public class DateWidgetTest
{
    /* Commons variables used in assertions. (@see setUp) */
    
    private Calendar cNow; 
    private Date now;
    private Calendar cTomorrow; 
    private Date tomorrow;
    private Calendar cYesterday; 
    private Date yesterday;
    
    @Before
    public void setUp() {
        cNow = Calendar.getInstance();
        now = cNow.getTime();
        
        cTomorrow = Calendar.getInstance();
        cTomorrow.add(Calendar.DAY_OF_MONTH, 1);
        tomorrow = cTomorrow.getTime();
        
        cYesterday = Calendar.getInstance();
        cYesterday.add(Calendar.DAY_OF_MONTH, -1);
        yesterday = cYesterday.getTime();
    }
    
    @Test
    public void testNowWithoutOption() throws Exception {
        NowWidget widget = new NowWidget(new MockWidgetRoot(), "!now");
        assertEquals(formatDate(now), widget.render());
    }
    
    @Test
    public void testNowWithTimeOption() throws Exception {
        NowWidget widget = new NowWidget(new MockWidgetRoot(), "!now(-t)");
        assertEquals(formatDatetime(now), widget.render());
    }
    
    @Test
    public void testNowWithFormatOption() throws Exception {
        NowWidget widget = new NowWidget(new MockWidgetRoot(), "!now(-fyyyy)");
        assertEquals(String.valueOf(cNow.get(Calendar.YEAR)), widget.render());
    }
    
    @Test
    public void testNowWithExtraSpace() throws Exception {
        NowWidget widget = new NowWidget(new MockWidgetRoot(), "!now( -t  )");
        assertEquals(formatDatetime(now), widget.render());
    }
    
    @Test
    public void setAFixedYear() throws Exception {
        NowWidget widget = new NowWidget(new MockWidgetRoot(), "!now(2012y)");
        cNow.set(Calendar.YEAR, 2012);
        assertEquals(formatDate(cNow), widget.render());
    }
    
    @Test
    public void setManyFields() throws Exception {
        NowWidget widget = new NowWidget(new MockWidgetRoot(), "!now(2012y12M)");
        cNow.set(Calendar.YEAR, 2012);
        cNow.set(Calendar.MONTH, 12);
        assertEquals(formatDate(cNow), widget.render());
    }
    
    @Test
    public void useMultipleOptions() throws Exception {
        NowWidget widget = new NowWidget(new MockWidgetRoot(), "!now(2012y -t)");
        cNow.set(Calendar.YEAR, 2012);
        assertEquals(formatDatetime(cNow), widget.render());
    }
    
    @Test
    public void assertOptionsOrderIsNotRelevant() throws Exception {
        NowWidget widget = new NowWidget(new MockWidgetRoot(), "!now(-t 2012y)");
        cNow.set(Calendar.YEAR, 2012);
        assertEquals(formatDatetime(cNow), widget.render());
    }
    
    @Test
    public void addOneDay() throws Exception {
        NowWidget widget = new NowWidget(new MockWidgetRoot(), "!now(+1d)");
        cNow.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals(formatDate(cNow), widget.render());
    }
    
    @Test
    public void addOneDayAnd3Hours() throws Exception {
        NowWidget widget = new NowWidget(new MockWidgetRoot(), "!now(+1d3h)");
        cNow.add(Calendar.DAY_OF_MONTH, 1);
        cNow.add(Calendar.HOUR, 3);
        assertEquals(formatDate(cNow), widget.render());
    }
    
    @Test
    public void testTomorrow() throws Exception {
        TomorrowWidget widget = new TomorrowWidget(new MockWidgetRoot(), "!tomorrow");
        assertEquals(formatDate(tomorrow), widget.render());
    }
    
    @Test
    public void testYesterday() throws Exception {
        YesterdayWidget widget = new YesterdayWidget(new MockWidgetRoot(), "!yesterday");
        assertEquals(formatDate(yesterday), widget.render());
    }
    
    @Test
    public void testNowAnd1DEqualsTomorrow() throws Exception {
        NowWidget widget = new NowWidget(new MockWidgetRoot(), "!now(+1d -t)");
        assertEquals(formatDatetime(tomorrow), widget.render());
    } 

    @Test
    public void testManyOptions() throws Exception {
        NowWidget widget = new NowWidget(new MockWidgetRoot(), "!now(2012y +2d3h -t -f\"ddMMyyyy hhmm\")");
        DateFormat format = new SimpleDateFormat("ddMMyyyy hhmm");
        cNow.set(Calendar.YEAR, 2012);
        cNow.add(Calendar.DAY_OF_MONTH, 2);
        cNow.add(Calendar.HOUR, 3);
        assertEquals(format.format(cNow.getTime()), widget.render());
    } 
    
    @Test
    public void testTimeDontElapsedBetweenTwoCalls() throws Exception {
        NowWidget widget1 = new NowWidget(new MockWidgetRoot(), "!now(-t -fyyyyMMddhhmmssSS)");
        String render1 = widget1.render();
        Thread.sleep(10); // assert the millisecond has changed between the two calls
        NowWidget widget2 = new NowWidget(new MockWidgetRoot(), "!now(-t -fyyyyMMddhhmmssSS)");
        String render2 = widget2.render();
        assertEquals(render1, render2);
    } 
    
    
    
    /* Utility methods used in assertions. */
    
    private String formatDate(Date date) {
        return AbstractDateWidget.DATE_FORMAT.format(date);
    }
    
    private String formatDatetime(Date date) {
        return AbstractDateWidget.DATETIME_FORMAT.format(date);
    }
    
    private String formatDate(Calendar calendar) {
        return AbstractDateWidget.DATE_FORMAT.format(calendar.getTime());
    }
    
    private String formatDatetime(Calendar calendar) {
        return AbstractDateWidget.DATETIME_FORMAT.format(calendar.getTime());
    }
    
}

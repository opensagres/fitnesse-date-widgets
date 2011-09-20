
package com.adeo.pyxis.fitnesses.plugin.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.adeo.pyxis.fitnesses.plugin.date.internals.AbstractDateOptions;

import fitnesse.wikitext.WikiWidget;
import fitnesse.wikitext.widgets.ParentWidget;

/**
 * Parent class for the date widget !now, !tomorrow, !yesterday.
 * The widgets have the same options and the same behaviour, except that the
 * reference date is different for each widget (!now will use the current date
 * but !tomorrow will use the current date + 1 day).
 * 
 * Why not use the widget !today, available with Fitnesse ?
 * 
 * - The widget is currently bugged: you can not use the format option because 
 *   the regex is greedy (https://github.com/unclebob/fitnesse/issues/42).
 * - You can just add/subtract days to the date
 * - You can not override a time field (like the year for example). For that,
 *   you should add the correct number of days (caution with leap year...).
 * - The time is not constant between two use of the widget.
 *   (can be a problem in some cases)
 *   Ex: !today -t some text between the two calls !today -t
 *   =>  08 Sep, 2011 09:54 some text between the two calls 08 Sep, 2011 09:54
 *   According to the current time, you could also have :  
 *   =>  08 Sep, 2011 09:54 some text between the two calls 08 Sep, 2011 09:55
 *   Note the difference between the two date (09h54/09h55)
 *   Will the !now widget, the same use of the widget give always the same
 *   time (for the current page of course, the time will be different on the next
 *   fixture page)
 *   Ex: !now(-t) and !now(-t) always render '08/09/2011 09:54 and 08/09/2011 09:54'  
 * 
 * How to use the widgets ?
 * (for the example, we consider the today date is September, 8th 2011)
 * 
 * !today
 * => 08/09/2011 (the time is not included by default)
 * 
 * !today(-t)
 * => 08/09/2011 09:54 (with time enable, only the hours and minutes are displayed)
 * 
 * Important: the widget will be used in inline mode in your fixtures. So, the options
 * should be specified in parenthesis to avoid confusion like that :
 * !today 8h => should we print 08/09/2011 -t or 08/09/2011 08:00 ?
 * With the use of parenthesis, the syntax is unambiguous :
 * !today 8h will print 08/09/2011 8h
 * !today(8h) will print 08/09/2011 08:00
 * 
 * !today(+1d)
 * => 09/09/2011 (you could add or subtract time elements, just pass a option with
 * the following format (+|-)(\d+[yMdhmsS])+ where: 
 * y: year, M: month, d: day, h: hour, m: minute, s: second, S: millisecond) 
 * 
 * More example to illustrate this option:
 * !today(+1y6M) => 08/03/2013 (one year and 6 months later)
 * !today(+7d4h -t) => 15/09/2011 13h53
 * 
 * !today(2012y) 
 * => 08/09/2012 (you could override time elements, the option has the same format 
 * as the option to add time without the leading operator)
 * 
 * !today(2012y5M)
 * => 08/05/2012 (you can combine time elements)
 * 
 * !today(2012y +1m -t)
 * => 08/10/2012 09:54 (and you can combine all options)
 *    
 * !today(-fYYYY)
 * => 2011 (you can choose a custom date format (@see SimpleDateFormat for the
 * allowing formats)
 * 
 * !today(-f"yyyy/MM/dd hh:mm:ss:SS")
 * => 2011/09/08 09:54:55:21 (if the format contains spaces, enclosed it in double quotes) 
 * 
 * For convenience purpose, you can use the other widgets: 
 * !today(+1d) is the same as !tomorrow
 * !today(-1d) is the same as !yesterday
 * 
 * @see NowWidget
 * @see YesterdayWidget
 * @see TomorrowWidget
 * 
 * @author Julien Sobczak
 */
public abstract class AbstractDateWidget extends WikiWidget
{
    /** Regex suffix: the same for all widgets. The suffix matches the options part. */
    protected static final String REGEXP_SUFFIX = "(?:[(](.*?)[)])?";
    
    /** Default formatter for a date without time. */
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    /** Default formatter for a datetime. */
    public static SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    /** The matched text by Fitnesse. */
    private String text;
    
    /** Lists of the options given. */
    private AbstractDateOptions options = null;
    
    /** {@inheritDoc} */
    public AbstractDateWidget(ParentWidget parent, String text) throws Exception {
        super(parent);
        this.text = text;
        init();
    }
    
    /** Return the pattern of the widget. */
    protected abstract Pattern getPattern();
    
    /** Return the reference date (the starting point for the date calculations). */
    protected abstract Calendar getReferenceDate();
   
    /** Utility method. Subclasses could use this method to build the regex. */
    protected static String formatRegex(final String keyword) {
        return "!" + keyword + REGEXP_SUFFIX;
    }
    
    /** 
     * Return the current date. Could be use by subclasses to calculate the
     * reference date.
     */
    protected Calendar today() {
        return Calendar.getInstance();
    }
    
    /**
     * Initialize the widget (options parsing).
     */
    private void init() {
        Matcher match = getPattern().matcher(text);
        if (!match.find()) {
            System.err.println(this.getClass().getName() 
                    + ": match was not found, text = '" + text + "'");
            return;
        } 
        String optionsStr = match.group(1);
        if (optionsStr == null) {
            this.options = AbstractDateOptions.DEFAULTS;
            return;
        }
        
        this.options = new AbstractDateOptions(optionsStr);
    }
    
    
    /** {@inheritDoc} */
    public String render() throws Exception {
        Calendar calendar = (Calendar) getReferenceDate()
            .clone(); // Warning: important to clone to not modify the 
                      // date for the next calls.

        if (options.hasTimeToSet()) {
            calendar = options.setTime(calendar);
        }
        if (options.hasTimeToAdd()) {
            calendar = options.addTime(calendar);
        }
        
        Date date = calendar.getTime();
        
        if (options.hasFormat()) {
            return options.getFormat().format(date);
        }
        
        return (options.hasTime()) 
            ? DATETIME_FORMAT.format(date) 
            : DATE_FORMAT.format(date);
    }
}

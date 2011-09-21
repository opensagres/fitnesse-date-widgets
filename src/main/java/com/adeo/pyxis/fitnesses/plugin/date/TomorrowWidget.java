package com.adeo.pyxis.fitnesses.plugin.date;

import java.util.Calendar;
import java.util.regex.Pattern;

import fitnesse.wikitext.widgets.ParentWidget;

/**
 * Display the tomorrow date.
 * Read the documentation for <code>AbstractDateWidget</code> for examples and
 * the detail of the widget.
 * 
 * @see AbstractDateWidget (list of available options)
 * @author Julien Sobczak
 */
public class TomorrowWidget extends AbstractDateWidget
{
    /** Singleton to keep the same date between all the calls. */
    public static Calendar dateInstance = null; 
    
    /** Required for Fitnesse. */
    public static final String REGEXP = formatRegex("tomorrow");
    
    /** Common field on widget. */
    public static final Pattern PATTERN = Pattern.compile(REGEXP);
    
    /** {@inheritDoc} */
    public TomorrowWidget(ParentWidget parent, String text) throws Exception {
        super(parent, text);
    }

    /** {@inheritDoc} */
    @Override
    protected Calendar getReferenceDate() {
        if (dateInstance == null) {
            dateInstance = today();
            dateInstance.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dateInstance;
    }

    /** {@inheritDoc} */
    @Override
    protected Pattern getPattern() {
        return PATTERN;
    }
}

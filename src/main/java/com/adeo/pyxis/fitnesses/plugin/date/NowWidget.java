package com.adeo.pyxis.fitnesses.plugin.date;

import java.util.Calendar;
import java.util.regex.Pattern;

import fitnesse.wikitext.widgets.ParentWidget;

/**
 * Display the today date.
 * Read the documentation for <code>AbstractDateWidget</code> for examples and
 * the detail of the widget.
 * 
 * Note: Today would be a better name but it's already used by fitnesse. 
 *       (the reason behind the similar widget are explained the documentation
 *       of the superclass <code>AbstractDateWidget</code>) 
 * 
 * @see AbstractDateWidget (list of available options)
 * @author Julien Sobczak
 */
public class NowWidget extends AbstractDateWidget
{
    /** Singleton to keep the same date between all the calls. */
    private static Calendar dateInstance = null; 

    /** Required for Fitnesse. */
    public static final String REGEXP = formatRegex("now");
    
    /** Common field on widget. */
    public static final Pattern PATTERN = Pattern.compile(REGEXP);
    
    /** {@inheritDoc} */
    public NowWidget(ParentWidget parent, String text) throws Exception {
        super(parent, text);
    }
    
    /** {@inheritDoc} */
    @Override
    protected Calendar getReferenceDate() {
        if (dateInstance == null) {
            dateInstance = today();
        }
        return dateInstance;
    }

    /** {@inheritDoc} */
    @Override
    protected Pattern getPattern() {
        return PATTERN;
    }

    
    
}

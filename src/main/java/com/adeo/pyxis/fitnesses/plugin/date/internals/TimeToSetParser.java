package com.adeo.pyxis.fitnesses.plugin.date.internals;

import java.util.Calendar;

/**
 * Parser for the option time to add.
 * Example: 1d, 2012y5M
 *   
 * @author Julien Sobczak
 */
public class TimeToSetParser extends TimeExpressionParser
{
    /** {@inheritDoc} */
    public TimeToSetParser(String expression) {
        super(expression);
    }

    /**
     * Add the time element given to the current date.
     * 
     * @param calendar {@inheritDoc}
     * @param type {@inheritDoc}
     * @param value {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    protected void doProcess(Calendar calendar, int type, int value) {
        calendar.set(type, value);
    }
}
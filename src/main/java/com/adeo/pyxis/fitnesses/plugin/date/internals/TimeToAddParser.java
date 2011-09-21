package com.adeo.pyxis.fitnesses.plugin.date.internals;

import java.util.Calendar;

/**
 * Parser for the option time to add.
 * Example: +1d, -5M4d
 *   
 * @author Julien Sobczak
 */
public class TimeToAddParser extends TimeExpressionParser
{
    /** 1 if '+' or -1 if '-'. */
    private int factor;
    
    /** {@inheritDoc} */
    public TimeToAddParser(String expression) {
        super(expression.substring(1)); // remove leading operator
        this.factor = expression.startsWith("+") ? 1 : -1;
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
        calendar.add(type, factor * value);
    }
}
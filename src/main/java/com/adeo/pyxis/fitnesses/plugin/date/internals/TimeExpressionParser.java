package com.adeo.pyxis.fitnesses.plugin.date.internals;

import java.util.Calendar;

/**
 * Base class to refactor the subclasses and avoid code duplication.
 * Parse a time expression (see options available for widgets date) and
 * let the subclasses processes the token extracted.
 * 
 * Example: 2012y6m
 * => doProcess(..., Calender.YEAR, 2012);
 * => doProcess(..., Calender.MONTH, 6); 
 * 
 * @author Julien Sobczak
 */
public abstract class TimeExpressionParser
{
    /** Expression to parse. */
    private String expression;
    
    /**
     * Unique constructor.
     * 
     * @param expression the expression to parse.
     */
    public TimeExpressionParser(String expression) {
        this.expression = expression;
    }
    
    /** 
     * Parse the expression and calls subclasses for the processing. 
     * 
     * @param calendar the current date on which do the processing is applied
     * @return the date updated 
     */
    public Calendar parse(Calendar calendar) {
        int numberAccumulator = 0;
        
        for (int i = 0; i < expression.length(); i++) {
            char character = expression.charAt(i);
            if (Character.isDigit(character)) {
                numberAccumulator = numberAccumulator * 10 + Integer.parseInt(String.valueOf(character));
            } else { // letter
                int type = TimeField.parse(String.valueOf(character)).getType();
                doProcess(calendar, type, numberAccumulator);
                numberAccumulator = 0;
            }
        }
        
        return calendar;
    }
    
    /**
     * Override this method in subclass to define the behaviour.
     * 
     * @param calendar the current date to update
     * @param type the Calendar constant value
     * @param value the value associated with the type.
     */
    protected abstract void doProcess(Calendar calendar, int type, int value);
}

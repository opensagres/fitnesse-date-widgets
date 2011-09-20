package com.adeo.pyxis.fitnesses.plugin.date.internals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Bean to abstract the list of options allowed by the date widget.
 * 
 * @author Julien Sobczak
 */
public class AbstractDateOptions {
    
    /** Format of the with time option. */
    private static final String OPTION_WITH_TIME = "-t";
    
    /** Format prefix of the format date option. */
    private static final String OPTION_EXPLICIT_FORMAT = "-f";
    
    /** Instance with default values specified to use when no option are given. */
    public static final AbstractDateOptions DEFAULTS = new AbstractDateOptions();
    
    /** The with time option is specified ? */
    private boolean withTime = false;
    
    /** The with explicat format date option is specified ? */
    private boolean withFormat = false;
    /** The formatter to use if an explicit format is specified. */
    private SimpleDateFormat explicitDateFormat;
    
    /** The time to add option is specified ? */
    private boolean withTimeToAdd = false;
    /** Parser associated with the option time to add to mask the implementation. */
    private TimeToAddParser timeToAdd;
    
    /** The time to set option is specified ? */
    private boolean withTimeToSet = false;
    /** Parser associated with the option time to set to mask the implementation. */
    private TimeToSetParser timeToSet;
    
    /** Constructor to use when no option specified. */
    public AbstractDateOptions() {
        // the defaults are set directly on the declarations of fields
    }
    
    /** 
     * Constructor to use when options are specified.
     * 
     * @param expression the expression between the parenthesis
     */
    public AbstractDateOptions(final String expression) {
        init(expression);
    }
    
    /**
     * Parse the options expression to extract the different options required.
     * 
     * @param expression the expression between the parenthesis
     */
    private void init(String expression) {
        for (String option : tokenizeOptions(expression)) {
            if (OPTION_WITH_TIME.equals(option)) {
                withTime();
            } else if (option.startsWith(OPTION_EXPLICIT_FORMAT)) {
                withFormat(option.substring(OPTION_EXPLICIT_FORMAT.length()));
            } else if (option.startsWith("+") || option.startsWith("-")) {
                withTimeToAdd(option);
            } else {
                withTimeToSet(option);
            }
        }   
    }
    
    /**
     * Parse the options expression and returns the list of tokens
     * to analyze in order to determine the given options.
     * 
     * @param expression the expression between the parenthesis
     * @return the list of the option token
     */
    private List<String> tokenizeOptions(String expression) {
        List<String> result = new ArrayList<String>();

        String[] optionsList = expression.split("\\s");
        /*
         * The problem with the previous statement:
         * ex: !now(-t -f"yy/MM/dd hh:mm")
         * => ['-t', '-f"yy/MM/dd', 'hh:mm'] 
         * The last option has been splitted in two option.
         */
        
        for (int i = 0; i < optionsList.length; i++) {
            String option = optionsList[i];
            
            if (option.contains("\"") && (i + 1 < optionsList.length)) {
                // A character " is present and we are not at the end of the list.
                
                // we should merge the current option with the next
                // to retrieve the original option value
                String nextOption = optionsList[i + 1];
                option += " " + nextOption; 
                // If more than one space was initially present, this is 
                // not a problem. The output of the widget will be included
                // in a HTML page. XML is not very sensible concerning spaces.
                // Indeed, more than one space are always compressed as one space. 
                // (except with HTML entity &nbsp;)
                i++; // ignore the next element 
            }
            result.add(option);
        }
        return result;
    }
    
    /** 
     * Enable the option with time. 
     */
    private void withTime() {
        withTime = true;
    }
    
    /**
     * Return if the option time is enable.
     * 
     * @return time enable ?
     */
    public boolean hasTime() {
        return withTime;
    }
    
    /**
     * Enable the option with explicit date format.
     *  
     * @param format the format compatible with <code>SimpleDateFormat</code>
     * @see SimpleDateFormat
     */
    private void withFormat(String format) {
        withFormat =  true;
        if (format.startsWith("\"")) {
            /*
             * If the format contains space, we should enclosed the format
             * with double quote characters.
             */
            format = format.substring(1, format.length() - 1);
        }
        explicitDateFormat = new SimpleDateFormat(format);
    }
    
    /**
     * Return if the explicit date format option is enable.
     * 
     * @return explicit date format enable ?
     */
    public boolean hasFormat() {
        return withFormat;
    }
    
    /**
     * Return the format to apply.
     * Note: you should test before if the explicit date format is specify.
     * 
     * @return the date formatter
     */
    public SimpleDateFormat getFormat() {
        return explicitDateFormat;
    }
    
    /**
     * Enable the time to add option.
     * 
     * @param expression the expression associated with the option
     */
    private void withTimeToAdd(String expression) {
        withTimeToAdd = true;
        timeToAdd = new TimeToAddParser(expression);
    }
    
    /**
     * Return if the option time to add is specified.
     * 
     * @return time to add specified ?
     */
    public boolean hasTimeToAdd() {
        return withTimeToAdd;
    }
    
    /**
     * Apply the option time to add on the parameter.
     * 
     * @param calendar the current calendar instance
     * @return the new calendar with the added time 
     */
    public Calendar addTime(Calendar calendar) {
        return timeToAdd.parse(calendar);
    }
    
    /**
     * Enable the time to set option.
     * 
     * @param expression the expression associated with the option
     */
    private void withTimeToSet(String expression) {
        withTimeToSet = true;
        timeToSet = new TimeToSetParser(expression);
    }
    
    /**
     * Return if the option time to set is specified.
     * 
     * @return time to set specified ?
     */
    public boolean hasTimeToSet() {
        return withTimeToSet;
    }
    
    /**
     * Apply the option time to set on the parameter.
     * 
     * @param calendar the current calendar instance
     * @return the new calendar with the time updated 
     */
    public Calendar setTime(Calendar calendar) {
        return timeToSet.parse(calendar);
    }
    
}

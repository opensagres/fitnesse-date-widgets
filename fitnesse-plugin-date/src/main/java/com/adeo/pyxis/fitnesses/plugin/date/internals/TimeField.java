package com.adeo.pyxis.fitnesses.plugin.date.internals;

import java.util.Calendar;

/**
 * Enumeration to match a time element with the <code>Calendar</code> constant associated.
 * Ex: the user give 2012y. With this enum, you can retrieve the value of Calendar.YEAR easily.
 * 
 * <code>
 * int type = TimeField.parse("y").getType();
 * // type == Calendar.YEAR
 * </code>
 * 
 * @see Calendar
 * @author Julien Sobczak
 */
public enum TimeField {
    
    YEAR("y", Calendar.YEAR),
    MONTH("M", Calendar.MONTH),
    DAY("d", Calendar.DAY_OF_MONTH),
    HOUR("h", Calendar.HOUR),
    MINUTE("m", Calendar.MINUTE),
    SECOND("s", Calendar.SECOND),
    MILLISECOND("S", Calendar.MILLISECOND);
    
    /** The value as in the option. */
    private String value;
    
    /** The associated type as in the Calendar class. */
    private int type;
    
    /**
     * Private constructor.
     * 
     * @param value the value as in the option
     * @param type  the Calendar constant value
     */
    TimeField(String value, int type) {
        this.value = value;
        this.type = type;
    }
    
    /**
     * Return the value as present in options.
     * 
     * @return the character
     */
    public String getValue() {
        return this.value;
    }
    
    /**
     * Return the value of the Calendar constant.
     * 
     * @return the value
     */
    public int getType() {
        return this.type;
    }
    
    /**
     * Parse the value and return the enum value associated.
     * 
     * @param value the value as in options
     * @return the enum instance associated
     */
    public static TimeField parse(String value) {
        for (TimeField field : values()) {
           if (field.getValue().equals(value)) {
               return field;
           }
        }
        throw new IllegalArgumentException(value + " is not a valid type");
    }
    
}
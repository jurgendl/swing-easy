package org.swingeasy;

import java.util.Arrays;

/**
 * @author Jurgen
 */
public class ETableRecordArray implements ETableRecord<Object[]> {
    protected Object[] array;

    public ETableRecordArray(Object... o) {
        this.array = o;
    }

    /**
     * 
     * @see org.swingeasy.ETableRecord#get(int)
     */
    @Override
    public Object get(int column) {
        return this.array[column];
    }

    /**
     * 
     * @see org.swingeasy.ETableRecord#getBean()
     */
    @Override
    public Object[] getBean() {
        return this.array;
    }

    /**
     * 
     * @see org.swingeasy.ETableRecord#getStringValue(int)
     */
    @Override
    public String getStringValue(int column) {
        Object value = this.get(column);
        return value == null ? null : "" + value;
    }

    /**
     * 
     * @see org.swingeasy.ETableRecord#getTooltip(int)
     */
    @Override
    public String getTooltip(int column) {
        return this.getStringValue(column);
    }

    /**
     * 
     * @see org.swingeasy.ETableRecord#set(int, java.lang.Object)
     */
    @Override
    public void set(int column, Object newValue) {
        this.array[column] = newValue;
    }

    /**
     * 
     * @see org.swingeasy.ETableRecord#size()
     */
    @Override
    public int size() {
        return this.array.length;
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return Arrays.toString(this.array);
    }
}

package org.swingeasy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * @author Jurgen
 */
public class ETableRecordArray implements ETableRecord<Object[]> {
    protected Object[] array;

    protected final Map<Integer, Object> originalValues = new HashMap<Integer, Object>();

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
        return value == null ? null : "" + value; //$NON-NLS-1$
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
     * @see org.swingeasy.ETableRecord#hasChanged(int)
     */
    @Override
    public boolean hasChanged(int column) {
        Object ov = this.originalValues.get(column);
        return (ov != null) && !new EqualsBuilder().append(ov, this.get(column)).isEquals();
    }

    /**
     * 
     * @see org.swingeasy.ETableRecord#set(int, java.lang.Object)
     */
    @Override
    public void set(int column, Object newValue) {
        if (this.originalValues.get(column) == null) {
            Object ov = this.get(column);
            this.originalValues.put(column, ov == null ? Void.TYPE : ov);
        }
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

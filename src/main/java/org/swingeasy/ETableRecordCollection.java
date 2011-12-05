package org.swingeasy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * @author Jurgen
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ETableRecordCollection implements ETableRecord<List> {
    protected List collection;

    protected final Map<Integer, Object> originalValues = new HashMap<Integer, Object>();

    public ETableRecordCollection() {
        this.collection = new ArrayList();
    }

    public ETableRecordCollection(Collection o) {
        this.collection = new ArrayList(o);
    }

    public ETableRecordCollection(List o) {
        this.collection = List.class.cast(o);
    }

    public void add(Object item) {
        this.collection.add(item);
    }

    /**
     * 
     * @see org.swingeasy.ETableRecord#get(int)
     */
    @Override
    public Object get(int column) {
        return this.collection.get(column);
    }

    /**
     * 
     * @see org.swingeasy.ETableRecord#getBean()
     */
    @Override
    public List getBean() {
        return this.collection;
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
        this.collection.set(column, newValue);
    }

    /**
     * 
     * @see org.swingeasy.ETableRecord#size()
     */
    @Override
    public int size() {
        return this.collection.size();
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.collection.toString();
    }
}

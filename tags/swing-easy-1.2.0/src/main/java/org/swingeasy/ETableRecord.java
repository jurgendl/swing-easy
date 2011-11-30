package org.swingeasy;

/**
 * @author Jurgen
 */
public interface ETableRecord<T> {
    public abstract Object get(int column);

    public abstract T getBean();

    public abstract String getStringValue(int column);

    public abstract String getTooltip(int column);

    public abstract void set(int column, Object newValue);

    public abstract int size();
}

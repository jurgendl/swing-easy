package org.swingeasy.test;

import org.swingeasy.ETableRecord;

/**
 * @author Jurgen
 */
public interface ETreeTableRecord<T> extends ETableRecord<T> {
    public abstract ETreeTableRecord<T> getParent();

    public abstract void setParent(ETreeTableRecord<T> parent);
}

package org.swingeasy;

import java.util.Collection;

import org.swingeasy.test.ETreeTableRecord;

/**
 * @author Jurgen
 */
public interface ETreeTableI<T> extends EComponentI {
    public abstract void addRecord(final ETreeTableRecord<T> record);

    public abstract void addRecords(final Collection<ETreeTableRecord<T>> records);
}

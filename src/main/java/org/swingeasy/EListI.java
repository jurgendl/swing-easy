package org.swingeasy;

import java.util.Collection;

import ca.odell.glazedlists.EventList;

/**
 * @author Jurgen
 * 
 * @param <T>
 */
public interface EListI<T> {
    public abstract void addRecord(EListRecord<T> EListRecord);

    public abstract void addRecords(Collection<EListRecord<T>> EListRecords);

    public abstract void addRecords(EListRecord<T>... EListRecord);

    public abstract EventList<EListRecord<T>> getRecords();

    public abstract void removeAllRecords();

    public abstract void removeRecord(final EListRecord<T> record);
}
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

    public abstract EventList<EListRecord<T>> getRecords();

    public abstract Collection<EListRecord<T>> getSelectedRecords();

    public abstract void removeAllRecords();

    public abstract void removeRecord(final EListRecord<T> record);

    public abstract void scrollToVisibleRecord(EListRecord<T> record);

    public abstract void setSelectedRecord(EListRecord<T> record);

    public abstract void setSelectedRecords(Collection<EListRecord<T>> EListRecords);
}
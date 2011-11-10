package org.swingeasy;

import java.util.Collection;

import ca.odell.glazedlists.EventList;

/**
 * @author Jurgen
 * 
 * @param <T>
 */
public interface EListI<T> {
    public abstract void addRecord(EComboBoxRecord<T> eComboBoxRecord);

    public abstract void addRecords(Collection<EComboBoxRecord<T>> eComboBoxRecords);

    public abstract void addRecords(EComboBoxRecord<T>... eComboBoxRecord);

    public abstract EventList<EComboBoxRecord<T>> getRecords();

    public abstract void removeAllRecords();

    public abstract void removeRecord(final EComboBoxRecord<T> record);
}
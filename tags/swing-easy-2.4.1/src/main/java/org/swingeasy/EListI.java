package org.swingeasy;

import java.util.Collection;
import java.util.List;

/**
 * @author Jurgen
 * 
 * @param <T>
 */
public interface EListI<T> extends EComponentI {
    public abstract void addRecord(EListRecord<T> EListRecord);

    public abstract void addRecords(Collection<EListRecord<T>> EListRecords);

    public abstract void clearSelection();

    public abstract List<EListRecord<T>> getRecords();

    public abstract EListRecord<T> getSelectedRecord();

    public abstract List<EListRecord<T>> getSelectedRecords();

    public abstract void moveSelectedDown();

    public abstract void moveSelectedUp();

    public abstract void removeAllRecords();

    public abstract void removeRecord(final EListRecord<T> record);

    public abstract void removeRecords(Collection<EListRecord<T>> records);

    public abstract void removeSelectedRecords();

    public abstract void scrollToVisibleRecord(EListRecord<T> record);

    public abstract void setSelectedRecord(EListRecord<T> record);

    public abstract void setSelectedRecords(Collection<EListRecord<T>> EListRecords);
}
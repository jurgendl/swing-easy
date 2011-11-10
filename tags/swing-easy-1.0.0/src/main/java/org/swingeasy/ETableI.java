package org.swingeasy;

import java.util.Collection;
import java.util.List;

/**
 * @author Jurgen
 */
@SuppressWarnings("rawtypes")
public interface ETableI {
    public abstract void addRecord(final ETableRecord record);

    public abstract void addRecords(final Collection<ETableRecord> records);

    public abstract void clear();

    public abstract Object getColumnValueAtVisualColumn(int i);

    public abstract List<String> getHeadernames();

    public abstract ETableRecord getRecordAtVisualRow(int i);

    public abstract List<ETableRecord> getRecords();

    public abstract void packColumn(int vColIndex);

    public abstract void packColumn(int vColIndex, int margin);

    public abstract void removeAllRecords();

    public abstract void removeRecord(final ETableRecord record);

    public abstract void removeRecordAtVisualRow(final int i);

    public abstract void setHeaders(final ETableHeaders headers);

    public abstract void sort(final int col);

    public abstract void unsort();
}
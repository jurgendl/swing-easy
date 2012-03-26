package org.swingeasy;

import java.awt.Point;
import java.util.Collection;
import java.util.List;

/**
 * @author Jurgen
 */
public interface ETableI<T> extends EComponentI {
    public abstract void addRecord(final ETableRecord<T> record);

    public abstract void addRecords(final Collection<ETableRecord<T>> records);

    public abstract void clear();

    public abstract Object getColumnValueAtVisualColumn(int i);

    public abstract List<String> getHeadernames();

    public abstract ETableRecord<T> getRecordAtVisualRow(int i);

    public abstract List<ETableRecord<T>> getRecords();

    public T getSelectedCell();

    public List<Object> getSelectedCells();

    /**
     * gets selected record (first one if multiple ones are selected
     * 
     * @return
     */
    public ETableRecord<T> getSelectedRecord();

    /**
     * gets selected record (first one if multiple ones are selected
     * 
     * @return
     */
    public List<ETableRecord<T>> getSelectedRecords();

    public abstract void packColumn(int vColIndex);

    public abstract void packColumn(int vColIndex, int margin);

    public abstract void removeAllRecords();

    public abstract void removeRecord(final ETableRecord<T> record);

    public abstract void removeRecordAtVisualRow(final int i);

    public abstract void scrollToVisibleRecord(final ETableRecord<T> record);

    public abstract void selectCell(Point p);

    /**
     * 
     * @param headers we do not want null here, use an empty header object instead
     */
    public abstract void setHeaders(final ETableHeaders<T> headers);

    public abstract void sort(final int col);

    public abstract void unsort();
}
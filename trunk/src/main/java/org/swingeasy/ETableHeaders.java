package org.swingeasy;

import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.builder.CompareToBuilder;

import ca.odell.glazedlists.gui.AdvancedTableFormat;
import ca.odell.glazedlists.gui.WritableTableFormat;

/**
 * @author Jurgen
 */
@SuppressWarnings("rawtypes")
public class ETableHeaders<T> implements WritableTableFormat<ETableRecord<T>>, AdvancedTableFormat<ETableRecord<T>> {
    protected final List<String> columnNames = new Vector<String>();

    protected final List<Class> columnClasses = new Vector<Class>();

    protected final List<Boolean> editable = new Vector<Boolean>();

    /**
     * Instantieer een nieuwe Headers
     * 
     * @param names
     */
    public ETableHeaders() {
        super();
    }

    public ETableHeaders(String... cols) {
        for (String col : cols) {
            this.add(col);
        }
    }

    /**
     * JDOC
     * 
     * @param column
     */
    public void add(String column) {
        this.add(column, Object.class);
    }

    /**
     * 
     * JDOC
     * 
     * @param column
     * @param clazz
     */
    public void add(String column, Class<?> clazz) {
        this.add(column, clazz, Boolean.FALSE);
    }

    /**
     * 
     * JDOC
     * 
     * @param column
     * @param clazz
     * @param edit
     */
    public void add(String column, Class<?> clazz, Boolean edit) {
        this.columnNames.add(column);
        this.columnClasses.add(clazz);
        this.editable.add(edit);
    }

    /**
     * 
     * @see ca.odell.glazedlists.gui.AdvancedTableFormat#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (this.columnNames.size() == 0) {
            return Object.class;
        }
        Class<?> clas = this.columnClasses.get(columnIndex);
        // System.out.println("ETableHeaders.getColumnClass(" + columnIndex + ")=" + clas);
        return clas;
    }

    /**
     * 
     * @see ca.odell.glazedlists.gui.AdvancedTableFormat#getColumnComparator(int)
     */
    @Override
    public Comparator<?> getColumnComparator(int column) {
        return new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return new CompareToBuilder().append(o1, o2).toComparison();
            }
        };
    }

    /**
     * @see ca.odell.glazedlists.gui.TableFormat#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return this.columnNames.size();
    }

    /**
     * @see ca.odell.glazedlists.gui.TableFormat#getColumnName(int)
     */
    @Override
    public String getColumnName(int column) {
        return this.columnNames.get(column);
    }

    public List<String> getColumnNames() {
        return this.columnNames;
    }

    /**
     * @see ca.odell.glazedlists.gui.TableFormat#getColumnValue(java.lang.Object, int)
     */
    @Override
    public Object getColumnValue(ETableRecord row, int column) {
        return row.get(column);
    }

    /**
     * 
     * @see ca.odell.glazedlists.gui.WritableTableFormat#isEditable(java.lang.Object, int)
     */
    @Override
    public boolean isEditable(ETableRecord baseObject, int column) {
        return Boolean.TRUE.equals(this.editable.get(column));
    }

    /**
     * 
     * @see ca.odell.glazedlists.gui.WritableTableFormat#setColumnValue(java.lang.Object, java.lang.Object, int)
     */
    @Override
    public ETableRecord setColumnValue(ETableRecord row, Object newValue, int column) {
        row.set(column, newValue);
        return row;
    }
}

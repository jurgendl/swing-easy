package org.swingeasy.test;

import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.swingeasy.ETableRecord;

import ca.odell.glazedlists.gui.AdvancedTableFormat;

/**
 * @author Jurgen
 */
@SuppressWarnings("rawtypes")
public class ETreeTableHeaders<T> implements AdvancedTableFormat<ETableRecord<T>> {
    protected final List<String> columnNames = new Vector<String>();

    protected final List<Class> columnClasses = new Vector<Class>();

    // protected final List<Boolean> editable = new Vector<Boolean>();

    public ETreeTableHeaders() {
        super();
    }

    public ETreeTableHeaders(String... cols) {
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

    // public void add(String column, Class<?> clazz) {
    // this.add(column, clazz, Boolean.FALSE);
    // }

    /**
     * 
     * JDOC
     * 
     * @param column
     * @param clazz
     */
    public void add(String column, Class<?> clazz/* , Boolean edit */) {
        this.columnNames.add(column);
        this.columnClasses.add(clazz);
        // this.editable.add(edit);
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
                if (((o1 == null) || (o1 instanceof Comparable)) && ((o2 == null) || (o2 instanceof Comparable))) {
                    return new CompareToBuilder().append(o1, o2).toComparison();
                }
                return new CompareToBuilder().append(String.valueOf(o1), String.valueOf(o2)).toComparison();
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

    // @Override
    // public boolean isEditable(ETableRecord baseObject, int column) {
    // return Boolean.TRUE.equals(this.editable.get(column));
    // }
    // public ETableRecord setColumnValue(ETableRecord row, Object newValue, int column) {
    // row.set(column, newValue);
    // return row;
    // }
}

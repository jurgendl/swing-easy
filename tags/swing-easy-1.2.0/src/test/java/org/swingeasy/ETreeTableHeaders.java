package org.swingeasy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jurgen
 */
public class ETreeTableHeaders {
    protected List<String> names = new ArrayList<String>();

    @SuppressWarnings("rawtypes")
    protected List<Class> types = new ArrayList<Class>();

    protected List<Boolean> editable = new ArrayList<Boolean>();

    public ETreeTableHeaders() {
        super();
    }

    public ETreeTableHeaders(String... names) {
        for (String name : names) {
            this.addColumn(name);
        }
    }

    public void addColumn(String name) {
        this.addColumn(name, Object.class, false);
    }

    public void addColumn(String name, Class<?> type, boolean coleditable) {
        this.names.add(name);
        this.types.add(type);
        this.editable.add(coleditable);
    }

    public Class<?> getColumnClass(int columnIndex) {
        return this.types.get(columnIndex);
    }

    public int getColumnCount() {
        return this.names.size();
    }

    public String getColumnName(int columnIndex) {
        return this.names.get(columnIndex);
    }

    public boolean isColumnEditable(int columnIndex) {
        return this.editable.get(columnIndex);
    }
}

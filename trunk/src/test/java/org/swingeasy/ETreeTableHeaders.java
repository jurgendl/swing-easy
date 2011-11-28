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

    public ETreeTableHeaders() {
        super();
    }

    public ETreeTableHeaders(String... names) {
        for (String name : names) {
            this.addColumn(name);
        }
    }

    public void addColumn(String name) {
        this.addColumn(name, Object.class);
    }

    public void addColumn(String name, Class<?> type) {
        this.names.add(name);
        this.types.add(type);
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
}

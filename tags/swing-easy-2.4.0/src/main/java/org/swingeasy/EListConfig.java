package org.swingeasy;

/**
 * @author Jurgen
 */
public class EListConfig extends EComponentConfig<EListConfig> {
    protected boolean threadSafe = true;

    protected boolean sortable = true;

    protected boolean filterable = false;

    public EListConfig() {
        super();
    }

    public boolean isFilterable() {
        return this.filterable;
    }

    public boolean isSortable() {
        return this.sortable;
    }

    public boolean isThreadSafe() {
        return this.threadSafe;
    }

    public void setFilterable(boolean filterable) {
        this.lockCheck();
        this.filterable = filterable;
    }

    public void setSortable(boolean sortable) {
        this.lockCheck();
        this.sortable = sortable;
    }

    public void setThreadSafe(boolean threadSafe) {
        this.lockCheck();
        this.threadSafe = threadSafe;
    }
}

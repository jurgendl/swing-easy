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

    public EListConfig setFilterable(boolean filterable) {
        this.lockCheck();
        this.filterable = filterable;
        return this;
    }

    public EListConfig setSortable(boolean sortable) {
        this.lockCheck();
        this.sortable = sortable;
        return this;
    }

    public EListConfig setThreadSafe(boolean threadSafe) {
        this.lockCheck();
        this.threadSafe = threadSafe;
        return this;
    }
}

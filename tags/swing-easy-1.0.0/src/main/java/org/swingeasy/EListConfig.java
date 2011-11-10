package org.swingeasy;

/**
 * @author Jurgen
 */
public class EListConfig {
    protected boolean threadSafe = true;

    protected boolean sortable = true;

    protected boolean locked;

    public EListConfig() {
        super();
    }

    public boolean isLocked() {
        return this.locked;
    }

    public boolean isSortable() {
        return this.sortable;
    }

    public boolean isThreadSafe() {
        return this.threadSafe;
    }

    public EListConfig lock() {
        this.setLocked(true);
        return this;
    }

    private void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setSortable(boolean sortable) {
        if (this.isLocked()) {
            throw new IllegalArgumentException();
        }
        this.sortable = sortable;
    }

    public void setThreadSafe(boolean threadSafe) {
        if (this.isLocked()) {
            throw new IllegalArgumentException();
        }
        this.threadSafe = threadSafe;
    }
}

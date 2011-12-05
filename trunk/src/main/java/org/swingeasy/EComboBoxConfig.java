package org.swingeasy;

/**
 * @author Jurgen
 */
public class EComboBoxConfig extends EConfig {
    protected boolean autoComplete = true;

    protected boolean threadSafe = true;

    protected boolean scrolling = true;

    protected boolean sortable = true;

    protected boolean locked;

    public EComboBoxConfig() {
        super();
    }

    public boolean isAutoComplete() {
        return this.autoComplete;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public boolean isScrolling() {
        return this.scrolling;
    }

    public boolean isSortable() {
        return this.sortable;
    }

    public boolean isThreadSafe() {
        return this.threadSafe;
    }

    public void lock() {
        this.setLocked(true);
    }

    public void setAutoComplete(boolean autoComplete) {
        if (this.isLocked()) {
            throw new IllegalArgumentException();
        }
        this.autoComplete = autoComplete;
    }

    private void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setScrolling(boolean scrolling) {
        if (this.isLocked()) {
            throw new IllegalArgumentException();
        }
        this.scrolling = scrolling;
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

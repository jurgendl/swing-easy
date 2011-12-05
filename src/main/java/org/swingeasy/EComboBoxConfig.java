package org.swingeasy;

/**
 * @author Jurgen
 */
public class EComboBoxConfig extends EComponentConfig<EComboBoxConfig> {
    protected boolean autoComplete = true;

    protected boolean threadSafe = true;

    protected boolean scrolling = true;

    protected boolean sortable = true;

    public EComboBoxConfig() {
        super();
    }

    public boolean isAutoComplete() {
        return this.autoComplete;
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

    public void setAutoComplete(boolean autoComplete) {
        this.lockCheck();
        this.autoComplete = autoComplete;
    }

    public void setScrolling(boolean scrolling) {
        this.lockCheck();
        this.scrolling = scrolling;
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

package org.swingeasy;

/**
 * @author Jurgen
 */
public class ETableConfig extends EComponentConfig<ETableConfig> {
    protected boolean threadSafe = true;

    protected boolean sortable;

    protected boolean filterable;

    protected boolean editable;

    protected boolean reorderable;

    protected boolean resizable;

    protected boolean vertical;

    protected boolean draggable;

    public ETableConfig() {
        super();
        this.threadSafe = true;
    }

    public ETableConfig(boolean all) {
        this(all, all, all, all, all, all, all, all);
        this.threadSafe = true;
        this.vertical = false;
    }

    public ETableConfig(boolean threadSafe, boolean sortable, boolean filterable, boolean editable, boolean reorderable, boolean resizable,
            boolean vertical, boolean draggable) {
        super();
        this.threadSafe = threadSafe;
        this.sortable = sortable;
        this.filterable = filterable;
        this.editable = editable;
        this.reorderable = reorderable;
        this.resizable = resizable;
        this.vertical = vertical;
        this.draggable = draggable;
    }

    public boolean isDraggable() {
        return this.draggable;
    }

    public boolean isEditable() {
        return this.editable;
    }

    public boolean isFilterable() {
        return this.filterable;
    }

    public boolean isReorderable() {
        return this.reorderable;
    }

    public boolean isResizable() {
        return this.resizable;
    }

    public boolean isSortable() {
        return this.sortable;
    }

    public boolean isThreadSafe() {
        return this.threadSafe;
    }

    public boolean isVertical() {
        return this.vertical;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setFilterable(boolean filterable) {
        this.lockCheck();
        this.filterable = filterable;
    }

    public void setReorderable(boolean reorderable) {
        this.lockCheck();
        this.reorderable = reorderable;
    }

    public void setResizable(boolean resizable) {
        this.lockCheck();
        this.resizable = resizable;
    }

    public void setSortable(boolean sortable) {
        this.lockCheck();
        this.sortable = sortable;
    }

    public void setThreadSafe(boolean threadSafe) {
        this.lockCheck();
        this.threadSafe = threadSafe;
    }

    public void setVertical(boolean vertical) {
        this.lockCheck();
        this.vertical = vertical;
    }
}

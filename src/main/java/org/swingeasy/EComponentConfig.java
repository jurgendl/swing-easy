package org.swingeasy;

/**
 * @author Jurgen
 */
public abstract class EComponentConfig<T> {
    protected boolean locked;

    protected boolean defaultPopupMenu = true;

    public boolean isDefaultPopupMenu() {
        return this.defaultPopupMenu;
    }

    final public boolean isLocked() {
        return this.locked;
    }

    @SuppressWarnings("unchecked")
    final public T lock() {
        this.setLocked(true);
        return (T) this;
    }

    final protected void lockCheck() {
        if (this.isLocked()) {
            throw new IllegalArgumentException();
        }
    }

    public void setDefaultPopupMenu(boolean defaultPopupMenu) {
        this.lockCheck();
        this.defaultPopupMenu = defaultPopupMenu;
    }

    final private void setLocked(boolean locked) {
        this.locked = locked;
    }
}

package org.swingeasy;

/**
 * @author Jurgen
 */
public abstract class EComponentConfig<T> {
    protected boolean locked;

    protected boolean defaultPopupMenu = true;

    @SuppressWarnings("unchecked")
    final protected T castThis() {
        return (T) this;
    }

    final public boolean isDefaultPopupMenu() {
        return this.defaultPopupMenu;
    }

    final public boolean isLocked() {
        return this.locked;
    }

    final public T lock() {
        this.setLocked(true);
        return this.castThis();
    }

    final protected void lockCheck() {
        if (this.isLocked()) {
            throw new IllegalArgumentException();
        }
    }

    final public T setDefaultPopupMenu(boolean defaultPopupMenu) {
        this.lockCheck();
        this.defaultPopupMenu = defaultPopupMenu;
        return this.castThis();
    }

    final private T setLocked(boolean locked) {
        this.locked = locked;
        return this.castThis();
    }
}

package org.swingeasy;

/**
 * @author Jurgen
 */
public abstract class EComponentConfig<T> {
    protected boolean locked;

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

    final private void setLocked(boolean locked) {
        this.locked = locked;
    }
}

package org.swingeasy;

public class ETabbedPaneConfig extends EComponentConfig<ETabbedPaneConfig> {
    protected boolean closable;

    protected boolean minimizable;

    protected Rotation rotation = Rotation.DEFAULT;

    public ETabbedPaneConfig() {
        super();
    }

    public ETabbedPaneConfig(boolean closable, boolean minimizable) {
        super();
        this.closable = closable;
        this.minimizable = minimizable;
    }

    public ETabbedPaneConfig(Rotation rotation, boolean closable, boolean minimizable) {
        super();
        this.rotation = rotation;
        this.closable = closable;
        this.minimizable = minimizable;
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public boolean isClosable() {
        return this.closable;
    }

    public boolean isMinimizable() {
        return this.minimizable;
    }

    public void setClosable(boolean closable) {
        this.lockCheck();
        this.closable = closable;
    }

    public void setMinimizable(boolean minimizable) {
        this.lockCheck();
        this.minimizable = minimizable;
    }

    public void setRotation(Rotation rotation) {
        this.lockCheck();
        this.rotation = rotation;
    }
}

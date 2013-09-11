package org.swingeasy;

/**
 * @author Jurgen
 */
public class ETreeConfig extends EComponentConfig<ETreeConfig> {
    private boolean editable = true;

    public ETreeConfig() {
        super();
    }

    public boolean isEditable() {
        return this.editable;
    }

    public ETreeConfig setEditable(boolean editable) {
        this.lockCheck();
        this.editable = editable;
        return this;
    }
}

package org.swingeasy;

/**
 * @author Jurgen
 */
public class ETextFieldConfig extends EComponentConfig<ETextFieldConfig> {
    private int columns = 0;

    private boolean enabled = true;

    public ETextFieldConfig() {
        super();
    }

    public ETextFieldConfig(boolean enabled) {
        super();
        this.enabled = enabled;
    }

    public ETextFieldConfig(boolean enabled, int columns) {
        super();
        this.enabled = enabled;
        this.columns = columns;
    }

    public int getColumns() {
        return this.columns;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public ETextFieldConfig setColumns(int columns) {
        this.lockCheck();
        this.columns = columns;
        return this;
    }

    public ETextFieldConfig setEnabled(boolean enabled) {
        this.lockCheck();
        this.enabled = enabled;
        return this;
    }
}

package org.swingeasy;

/**
 * @author Jurgen
 */
public class ETextFieldConfig extends EComponentConfig<ETextFieldConfig> {
    private int columns = 0;

    private boolean enabled = true;

    private boolean selectAllOnFocus = false;

    public ETextFieldConfig() {
        super();
    }

    public ETextFieldConfig(boolean enabled) {
        super();
        this.enabled = enabled;
    }

    public ETextFieldConfig(boolean enabled, int columns) {
        this(enabled, columns, false);
    }

    public ETextFieldConfig(boolean enabled, int columns, boolean selectAllOnFocus) {
        super();
        this.selectAllOnFocus = selectAllOnFocus;
        this.enabled = enabled;
        this.columns = columns;
    }

    public ETextFieldConfig(int columns) {
        this(true, columns, false);
    }

    public int getColumns() {
        return this.columns;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isSelectAllOnFocus() {
        return this.selectAllOnFocus;
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

    public ETextFieldConfig setSelectAllOnFocus(boolean selectAllOnFocus) {
        this.lockCheck();
        this.selectAllOnFocus = selectAllOnFocus;
        return this;
    }
}

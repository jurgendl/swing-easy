package org.swingeasy;

/**
 * @author Jurgen
 */
public class ETextAreaConfig extends EComponentConfig<ETextAreaConfig> {
    private int rows = 0;

    private int columns = 0;

    private boolean enabled = true;

    public ETextAreaConfig() {
        super();
    }

    public ETextAreaConfig(boolean enabled) {
        super();
        this.enabled = enabled;
    }

    public ETextAreaConfig(boolean enabled, int rows, int columns) {
        super();
        this.enabled = enabled;
        this.rows = rows;
        this.columns = columns;
    }

    public int getColumns() {
        return this.columns;
    }

    public int getRows() {
        return this.rows;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setColumns(int columns) {
        this.lockCheck();
        this.columns = columns;
    }

    public void setEnabled(boolean enabled) {
        this.lockCheck();
        this.enabled = enabled;
    }

    public void setRows(int rows) {
        this.lockCheck();
        this.rows = rows;
    }
}

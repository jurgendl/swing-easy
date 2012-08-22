package org.swingeasy;

/**
 * @author Jurgen
 */
public class ETextAreaConfig extends EComponentConfig<ETextAreaConfig> {
    private int rows = 0;

    private int columns = 0;

    private boolean enabled = true;

    private boolean autoScroll = true;

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

    public boolean isAutoScroll() {
        return this.autoScroll;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setAutoScroll(boolean autoScroll) {
        this.autoScroll = autoScroll;
    }

    public ETextAreaConfig setColumns(int columns) {
        this.lockCheck();
        this.columns = columns;
        return this;
    }

    public ETextAreaConfig setEnabled(boolean enabled) {
        this.lockCheck();
        this.enabled = enabled;
        return this;
    }

    public ETextAreaConfig setRows(int rows) {
        this.lockCheck();
        this.rows = rows;
        return this;
    }
}

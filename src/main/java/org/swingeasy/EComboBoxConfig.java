package org.swingeasy;

public class EComboBoxConfig {
    protected boolean autoComplete = true;

    protected boolean threadSafe = true;

    public EComboBoxConfig() {
        super();
    }

    public boolean isAutoComplete() {
        return this.autoComplete;
    }

    public boolean isThreadSafe() {
        return this.threadSafe;
    }

    public void setAutoComplete(boolean autoComplete) {
        this.autoComplete = autoComplete;
    }

    public void setThreadSafe(boolean threadSafe) {
        this.threadSafe = threadSafe;
    }
}

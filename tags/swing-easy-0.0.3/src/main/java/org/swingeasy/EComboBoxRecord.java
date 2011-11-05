package org.swingeasy;

/**
 * @author jdlandsh
 */
public class EComboBoxRecord<T> {
    protected T bean;

    public EComboBoxRecord() {
        super();
    }

    public EComboBoxRecord(T bean) {
        super();
        this.bean = bean;
    }

    public T getBean() {
        return this.bean;
    }

    public String getStringValue() {
        return String.valueOf(this.bean);
    }

    public void set(T newValue) {
        this.bean = newValue;
    }
}

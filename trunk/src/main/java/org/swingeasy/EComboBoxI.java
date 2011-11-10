package org.swingeasy;

public interface EComboBoxI<T> {
    public void activateAutoCompletion();

    public void addRecord(EComboBoxRecord<T> eComboBoxRecord);

    public abstract EComboBoxRecord<T> getSelectedRecord();

    public abstract void setSelectedRecord(EComboBoxRecord<T> record);
}

package org.swingeasy;

public interface EComboBoxI<T> {
    public void activateAutoCompletion();

    public void addRecord(EComboBoxRecord<T> eComboBoxRecord);
}

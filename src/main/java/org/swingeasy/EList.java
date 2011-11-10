package org.swingeasy;

import java.util.Arrays;
import java.util.Collection;

import javax.swing.JList;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.EventListModel;

/**
 * @author Jurgen
 */
public class EList<T> extends JList implements EListI<T> {
    private static final long serialVersionUID = -3602504810131193505L;

    protected EListConfig cfg;

    protected EventList<EComboBoxRecord<T>> records;

    protected EList() {
        //
    }

    public EList(EListConfig cfg) {
        this.cfg = cfg;
        this.cfg.lock();
        this.records = new BasicEventList<EComboBoxRecord<T>>();
        if (this.cfg.isSortable()) {
            this.records = new SortedList<EComboBoxRecord<T>>(this.records);
        }
        if (this.cfg.isThreadSafe()) {
            this.records = GlazedLists.threadSafeList(this.records);
        }
        EventListModel<EComboBoxRecord<T>> model = new EventListModel<EComboBoxRecord<T>>(this.records);
        this.setModel(model);
    }

    /**
     * 
     * @see org.swingeasy.EListI#addRecord(org.swingeasy.EComboBoxRecord)
     */
    @Override
    public void addRecord(EComboBoxRecord<T> record) {
        this.records.add(record);
    }

    /**
     * 
     * @see org.swingeasy.EListI#addRecords(java.util.Collection)
     */
    @Override
    public void addRecords(Collection<EComboBoxRecord<T>> eComboBoxRecords) {
        this.records.addAll(eComboBoxRecords);
    }

    /**
     * 
     * @see org.swingeasy.EListI#addRecords(org.swingeasy.EComboBoxRecord<T>[])
     */
    @Override
    public void addRecords(EComboBoxRecord<T>... eComboBoxRecords) {
        this.records.addAll(Arrays.asList(eComboBoxRecords));
    }

    /**
     * 
     * @see org.swingeasy.EListI#getRecords()
     */
    @Override
    public EventList<EComboBoxRecord<T>> getRecords() {
        return this.records;
    }

    /**
     * J_DOC
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public EList<T> getSimpleThreadSafeInterface() {
        return EventThreadSafeWrapper.getSimpleThreadSafeInterface(EList.class, this, EListI.class);
    }

    /**
     * 
     * @see org.swingeasy.EListI#removeAllRecords()
     */
    @Override
    public void removeAllRecords() {
        this.records.clear();
    }

    /**
     * 
     * @see org.swingeasy.EListI#removeRecord(org.swingeasy.EComboBoxRecord)
     */
    @Override
    public void removeRecord(EComboBoxRecord<T> record) {
        this.records.remove(record);
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public EList<T> stsi() {
        return this.getSimpleThreadSafeInterface();
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public EList<T> STSI() {
        return this.getSimpleThreadSafeInterface();
    }
}

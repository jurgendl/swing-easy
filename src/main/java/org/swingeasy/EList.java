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

    protected EventList<EListRecord<T>> records;

    protected EList() {
        //
    }

    public EList(EListConfig cfg) {
        this.cfg = cfg;
        this.cfg.lock();
        this.records = new BasicEventList<EListRecord<T>>();
        if (this.cfg.isSortable()) {
            this.records = new SortedList<EListRecord<T>>(this.records);
        }
        if (this.cfg.isThreadSafe()) {
            this.records = GlazedLists.threadSafeList(this.records);
        }
        EventListModel<EListRecord<T>> model = new EventListModel<EListRecord<T>>(this.records);
        this.setModel(model);
    }

    /**
     * 
     * @see org.swingeasy.EListI#addRecord(org.swingeasy.EListRecord)
     */
    @Override
    public void addRecord(EListRecord<T> record) {
        this.records.add(record);
    }

    /**
     * 
     * @see org.swingeasy.EListI#addRecords(java.util.Collection)
     */
    @Override
    public void addRecords(Collection<EListRecord<T>> EListRecords) {
        this.records.addAll(EListRecords);
    }

    /**
     * 
     * @see org.swingeasy.EListI#addRecords(org.swingeasy.EListRecord<T>[])
     */
    @Override
    public void addRecords(EListRecord<T>... EListRecords) {
        this.records.addAll(Arrays.asList(EListRecords));
    }

    /**
     * 
     * @see org.swingeasy.EListI#getRecords()
     */
    @Override
    public EventList<EListRecord<T>> getRecords() {
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
     * @see org.swingeasy.EListI#removeRecord(org.swingeasy.EListRecord)
     */
    @Override
    public void removeRecord(EListRecord<T> record) {
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

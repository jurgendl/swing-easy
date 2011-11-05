package org.swingeasy;

import java.util.List;

import javax.swing.JComboBox;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import ca.odell.glazedlists.swing.EventComboBoxModel;

/**
 * @author Jurgen
 */
public class EComboBox<T> extends JComboBox implements EComboBoxI<T> {
    private static final long serialVersionUID = -3602504810131193505L;

    protected EComboBoxConfig cfg;

    protected EventList<EComboBoxRecord<T>> records;

    protected EComboBox() {
        //
    }

    public EComboBox(EComboBoxConfig cfg) {
        this.cfg = cfg;
        this.records = (this.cfg.isThreadSafe() ? GlazedLists.threadSafeList(new BasicEventList<EComboBoxRecord<T>>())
                : new BasicEventList<EComboBoxRecord<T>>());
        EventComboBoxModel<EComboBoxRecord<T>> model = new EventComboBoxModel<EComboBoxRecord<T>>(this.records);
        this.setModel(model);
        if (cfg.isAutoComplete()) {
            this.getSimpleThreadSafeInterface().activateAutoCompletion();
        }
    }

    /**
     * 
     * @see org.swingeasy.EComboBoxI#activateAutoCompletion()
     */
    @Override
    public void activateAutoCompletion() {
        AutoCompleteSupport<EComboBoxRecord<T>> support = AutoCompleteSupport.install(this, this.records, new TextFilterator<EComboBoxRecord<T>>() {
            @Override
            public void getFilterStrings(List<String> baseList, EComboBoxRecord<T> element) {
                baseList.add(element.getStringValue());
            }
        });
        support.setFilterMode(TextMatcherEditor.CONTAINS);
    }

    /**
     * 
     * @see org.swingeasy.EComboBoxI#addRecord(org.swingeasy.EComboBoxRecord)
     */
    @Override
    public void addRecord(EComboBoxRecord<T> record) {
        this.records.add(record);
    }

    /**
     * J_DOC
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public EComboBox<T> getSimpleThreadSafeInterface() {
        return EventThreadSafeWrapper.getSimpleThreadSafeInterface(EComboBox.class, this, EComboBoxI.class);
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public EComboBox<T> stsi() {
        return this.getSimpleThreadSafeInterface();
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public EComboBox<T> STSI() {
        return this.getSimpleThreadSafeInterface();
    }
}

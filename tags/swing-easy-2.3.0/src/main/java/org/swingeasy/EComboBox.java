package org.swingeasy;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.swingeasy.EComponentPopupMenu.ReadableComponent;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import ca.odell.glazedlists.swing.EventComboBoxModel;

/**
 * @author Jurgen
 */
public class EComboBox<T> extends JComboBox implements EComboBoxI<T>, Iterable<EComboBoxRecord<T>>, ReadableComponent {
    protected class MouseValueScroller implements MouseWheelListener {
        @Override
        public synchronized void mouseWheelMoved(MouseWheelEvent e) {
            int currentSelected = EComboBox.this.getSelectedIndex();
            final boolean nullFound = EComboBox.this.getModel().getElementAt(0) == null;
            if (currentSelected == -1) {
                currentSelected = nullFound ? 1 : 0;
                EComboBox.this.setSelectedIndex(currentSelected);
                return;
            }
            final int max = EComboBox.this.getItemCount() - 1;
            final boolean down = e.getWheelRotation() > 0;
            if (down) {
                currentSelected++;
                if (currentSelected > max) {
                    currentSelected = 0;
                }
                if (nullFound && (currentSelected == 0)) {
                    currentSelected = 1;
                }
            } else {
                currentSelected--;
                if (currentSelected < 0) {
                    currentSelected = max;
                }
                if (nullFound && (currentSelected == 0)) {
                    currentSelected = max;
                }
            }
            EComboBox.this.setSelectedIndex(currentSelected);
        }
    }

    private static final long serialVersionUID = -3602504810131193505L;

    protected EComboBoxConfig cfg;

    protected EventList<EComboBoxRecord<T>> records;

    protected MouseValueScroller mouseValueScroller = null;

    protected EComboBox() {
        super();
    }

    public EComboBox(EComboBoxConfig cfg) {
        this.cfg = cfg;
        this.cfg.lock();
        this.records = new BasicEventList<EComboBoxRecord<T>>();
        if (this.cfg.isSortable()) {
            this.records = new SortedList<EComboBoxRecord<T>>(this.records);
        }
        if (this.cfg.isThreadSafe()) {
            this.records = GlazedLists.threadSafeList(this.records);
        }
        EventComboBoxModel<EComboBoxRecord<T>> model = new EventComboBoxModel<EComboBoxRecord<T>>(this.records);
        this.setModel(model);
        if (this.cfg.isAutoComplete()) {
            this.getSimpleThreadSafeInterface().activateAutoCompletion();
        }
        if (this.cfg.isScrolling()) {
            this.activateScrolling();
        }

        UIUtils.registerLocaleChangeListener(this);

        if (cfg.isDefaultPopupMenu()) {
            EComponentPopupMenu.installTextComponentPopupMenu(this);
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
                if (element == null) {
                    return;
                }
                baseList.add(element.getStringValue());
            }
        });
        support.setFilterMode(TextMatcherEditor.CONTAINS);
    }

    /**
     * 
     * @see org.swingeasy.EComboBoxI#activateScrolling()
     */
    @Override
    public void activateScrolling() {
        if (this.mouseValueScroller == null) {
            this.mouseValueScroller = new MouseValueScroller();
            this.addMouseWheelListener(new MouseValueScroller());
        }
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
     * 
     * @see org.swingeasy.EComboBoxI#addRecords(java.util.Collection)
     */
    @Override
    public void addRecords(Collection<EComboBoxRecord<T>> eComboBoxRecords) {
        this.records.addAll(eComboBoxRecords);
    }

    /**
     * 
     * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy()
     */
    @Override
    public void copy() {
        StringBuilder sb = new StringBuilder();
        for (EComboBoxRecord<T> record : this) {
            sb.append(record.getStringValue()).append(EComponentPopupMenu.newline);
        }
        EComponentPopupMenu.copy(sb.toString());
    }

    /**
     * 
     * @see org.swingeasy.EComboBoxI#deactivateScrolling()
     */
    @Override
    public void deactivateScrolling() {
        if (this.mouseValueScroller != null) {
            this.removeMouseWheelListener(this.mouseValueScroller);
            this.mouseValueScroller = null;
        }
    }

    /**
     * 
     * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#getPopupParentComponent()
     */
    @Override
    public JComponent getPopupParentComponent() {
        return this;
    }

    /**
     * 
     * @see org.swingeasy.EComboBoxI#getRecords()
     */
    @Override
    public List<EComboBoxRecord<T>> getRecords() {
        return this.records;
    }

    /**
     * 
     * @see org.swingeasy.EComboBoxI#getSelectedRecord()
     */
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public EComboBoxRecord<T> getSelectedRecord() {
        return (EComboBoxRecord) this.getSelectedItem();
    }

    /**
     * JDOC
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public EComboBox<T> getSimpleThreadSafeInterface() {
        try {
            return EventThreadSafeWrapper.getSimpleThreadSafeInterface(EComboBox.class, this, EComboBoxI.class);
        } catch (Exception ex) {
            System.err.println(ex);
            return this; // no javassist
        }
    }

    /**
     * 
     * @see javax.swing.JComponent#getToolTipText()
     */
    @Override
    public String getToolTipText() {
        if (this.getSelectedRecord() == null) {
            return null;
        }

        return this.getSelectedRecord().getTooltip();
    }

    /**
     * threadsafe unmodifiable iterator
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<EComboBoxRecord<T>> iterator() {
        return Collections.unmodifiableCollection(this.getRecords()).iterator();
    }

    /**
     * 
     * @see org.swingeasy.EComboBoxI#removeAllRecords()
     */
    @Override
    public void removeAllRecords() {
        this.records.clear();
    }

    /**
     * 
     * @see org.swingeasy.EComboBoxI#removeRecord(org.swingeasy.EComboBoxRecord)
     */
    @Override
    public void removeRecord(EComboBoxRecord<T> record) {
        this.records.remove(record);
    }

    /**
     * 
     * @see org.swingeasy.ETableI#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        this.repaint();
    }

    /**
     * 
     * @see org.swingeasy.EComboBoxI#setSelectedRecord(org.swingeasy.EComboBoxRecord)
     */
    @Override
    public void setSelectedRecord(EComboBoxRecord<T> record) {
        this.setSelectedItem(record);
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

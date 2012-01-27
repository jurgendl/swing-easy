package org.swingeasy;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.swing.EventListModel;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;

/**
 * @author Jurgen
 */
public class EList<T> extends JList implements EListI<T> {
    private class DelegatingListCellRenderer implements ListCellRenderer {
        @SuppressWarnings("rawtypes")
        protected transient Hashtable<Class, ListCellRenderer> defaultRenderersByClass = new Hashtable<Class, ListCellRenderer>();

        public DelegatingListCellRenderer(ListCellRenderer defaultListCellRenderer) {
            this.setDefaultRenderer(Object.class, defaultListCellRenderer);
            this.setDefaultRenderer(Date.class, new DateListCellRenderer());
            this.setDefaultRenderer(Number.class, new NumberListCellRenderer());
            this.setDefaultRenderer(Color.class, new ColorListCellRenderer());
        }

        public ListCellRenderer getDefaultRenderer(Class<?> columnClass) {
            if (columnClass == null) {
                return null;
            }
            ListCellRenderer renderer = this.defaultRenderersByClass.get(columnClass);
            if (renderer != null) {
                return renderer;
            }
            return this.getDefaultRenderer(columnClass.getSuperclass());
        }

        /**
         * 
         * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
         */
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            value = EListRecord.class.cast(value).get();
            return this.getDefaultRenderer(value.getClass()).getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }

        public void setDefaultRenderer(Class<?> columnClass, ListCellRenderer renderer) {
            this.defaultRenderersByClass.put(columnClass, renderer);
        }

        public void setLocale(Locale l) {
            for (ListCellRenderer renderer : this.defaultRenderersByClass.values()) {
                if (renderer instanceof Component) {
                    Component.class.cast(renderer).setLocale(l);
                }
            }
        }
    }

    private static class EListModel<T> extends EventListModel<EListRecord<T>> {
        protected EventList<EListRecord<T>> source;

        protected JTextField filtercomponent;

        public EListModel(EventList<EListRecord<T>> source) {
            super(source);
            this.source = source;
        }
    }

    private static final long serialVersionUID = -3602504810131193505L;

    private static <T> EListModel<T> createModel(EListConfig cfg) {
        EventList<EListRecord<T>> records = new BasicEventList<EListRecord<T>>();
        if (cfg.isSortable()) {
            records = new SortedList<EListRecord<T>>(records);
        }
        JTextField filtercomponent = null;
        if (cfg.isFilterable()) {
            filtercomponent = new JTextField();
            TextFilterator<EListRecord<T>> textFilterator = new TextFilterator<EListRecord<T>>() {
                @Override
                public void getFilterStrings(List<String> baseList, EListRecord<T> element) {
                    if (element != null) {
                        baseList.add(element.getStringValue());
                    }
                }
            };
            TextComponentMatcherEditor<EListRecord<T>> filter = new TextComponentMatcherEditor<EListRecord<T>>(filtercomponent, textFilterator, true);
            records = new FilterList<EListRecord<T>>(records, filter);
        }
        if (cfg.isThreadSafe()) {
            records = GlazedLists.threadSafeList(records);
        }
        EListModel<T> model = new EListModel<T>(records);
        model.filtercomponent = filtercomponent;
        return model;
    }

    protected final JTextField filtercomponent;

    protected EListConfig cfg;

    protected EventList<EListRecord<T>> records;

    protected DelegatingListCellRenderer delegatingListCellRenderer;

    /**
     * do not use, do not change access
     */
    protected EList() {
        this.filtercomponent = null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public EList(EListConfig cfg) {
        super(EList.createModel(cfg.lock()));
        this.cfg = cfg;
        EListModel elistModel = EListModel.class.cast(this.getModel());
        this.records = elistModel.source;
        this.filtercomponent = elistModel.filtercomponent;
        elistModel.filtercomponent = null;
        this.delegatingListCellRenderer = new DelegatingListCellRenderer(this.getCellRenderer());
        this.setCellRenderer(this.delegatingListCellRenderer);

        // drag and drop test jvm internally, intra jvm, tostring
        this.setDragEnabled(true);
        this.setTransferHandler(new EListTransferHandler<T>());
    }

    /**
     * 
     * @see org.swingeasy.EListI#addRecord(org.swingeasy.EListRecord)
     */
    @Override
    public void addRecord(EListRecord<T> r) {
        if (r == null) {
            throw new NullPointerException();
        }
        this.records.add(r);
    }

    /**
     * 
     * @see org.swingeasy.EListI#addRecords(java.util.Collection)
     */
    @Override
    public void addRecords(Collection<EListRecord<T>> r) {
        for (EListRecord<T> el : r) {
            if (el == null) {
                throw new NullPointerException();
            }
        }
        this.records.addAll(r);
    }

    /**
     * 
     * @see javax.swing.JList#createSelectionModel()
     */
    @Override
    @SuppressWarnings("unchecked")
    protected ListSelectionModel createSelectionModel() {
        try {
            EListModel<T> model = EListModel.class.cast(this.getModel());
            return new EventSelectionModel<EListRecord<T>>(model.source);
        } catch (ClassCastException ex) {
            return super.createSelectionModel();
        }
    }

    public JTextField getFiltercomponent() {
        return this.filtercomponent;
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
     * 
     * @see org.swingeasy.EListI#getSelectedRecords()
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<EListRecord<T>> getSelectedRecords() {
        List<EListRecord<T>> list = new ArrayList<EListRecord<T>>();
        for (Object o : this.getSelectedValues()) {
            list.add(EListRecord.class.cast(o));
        }
        return list;
    }

    /**
     * JDOC
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public EList<T> getSimpleThreadSafeInterface() {
        try {
            return EventThreadSafeWrapper.getSimpleThreadSafeInterface(EList.class, this, EListI.class);
        } catch (Exception ex) {
            System.err.println(ex);
            return this; // no javassist
        }
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
     * 
     * @see org.swingeasy.EListI#scrollToVisibleRecord(org.swingeasy.EListRecord)
     */
    @Override
    public void scrollToVisibleRecord(EListRecord<T> record) {
        if (!this.isDisplayable()) {
            throw new IllegalArgumentException("can only be used when list is displayable (visible)"); //$NON-NLS-1$
        }
        int index = this.records.indexOf(record);
        Rectangle cellbounds = this.getCellBounds(index, index);
        this.scrollRectToVisible(cellbounds);
    }

    /**
     * JDOC
     * 
     * @param columnClass
     * @param renderer
     */
    public void setDefaultRenderer(Class<?> columnClass, ListCellRenderer renderer) {
        this.delegatingListCellRenderer.setDefaultRenderer(columnClass, renderer);
    }

    /**
     * 
     * @see org.swingeasy.ETableI#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        this.delegatingListCellRenderer.setLocale(l);
        this.repaint();
    }

    /**
     * 
     * @see org.swingeasy.EListI#setSelectedRecord(org.swingeasy.EListRecord)
     */
    @Override
    public void setSelectedRecord(EListRecord<T> record) {
        if (!this.records.contains(record)) {
            throw new IllegalArgumentException();
        }
        this.setSelectedValue(record, true);
    }

    /**
     * 
     * @see org.swingeasy.EListI#setSelectedRecords(java.util.Collection)
     */
    @Override
    public void setSelectedRecords(Collection<EListRecord<T>> eListRecords) {
        int[] indices = new int[eListRecords.size()];
        int i = 0;
        for (EListRecord<T> toSelect : eListRecords) {
            int index = this.records.indexOf(toSelect);
            if (index == -1) {
                throw new IllegalArgumentException();
            }
            indices[i++] = index;
        }
        this.setSelectedIndices(indices);
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

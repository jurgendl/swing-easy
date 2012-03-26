package org.swingeasy;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import org.swingeasy.EComponentPopupMenu.ReadableComponent;
import org.swingeasy.list.renderer.ColorListCellRenderer;
import org.swingeasy.list.renderer.DateListCellRenderer;
import org.swingeasy.list.renderer.NumberListCellRenderer;
import org.swingeasy.system.SystemSettings;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.EventListModel;
import ca.odell.glazedlists.swing.EventSelectionModel;

/**
 * @author Jurgen
 */
public class EList<T> extends JList implements EListI<T>, Iterable<EListRecord<T>>, ReadableComponent {
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
                if (renderer instanceof EComponentI) {
                    EComponentI.class.cast(renderer).setLocale(l);
                }
            }
        }
    }

    private static class EListModel<T> extends EventListModel<EListRecord<T>> {
        protected EventList<EListRecord<T>> source;

        protected EListFilterComponent<T> filtercomponent;

        public EListModel(EventList<EListRecord<T>> source) {
            super(source);
            this.source = source;
        }
    }

    private static final long serialVersionUID = -3602504810131193505L;

    /**
     * convert type
     * 
     * @param records
     * @return
     */
    public static <T> List<EListRecord<T>> convert(Collection<T> records) {
        List<EListRecord<T>> list = new ArrayList<EListRecord<T>>();
        for (T r : records) {
            list.add(new EListRecord<T>(r));
        }
        return list;
    }

    /**
     * convert type
     * 
     * @param records
     * @return
     */
    public static <T> List<T> convertRecords(Collection<EListRecord<T>> records) {
        List<T> list = new ArrayList<T>();
        for (EListRecord<T> r : records) {
            list.add(r.get());
        }
        return list;
    }

    private static <T> EListModel<T> createModel(EListConfig cfg) {
        EventList<EListRecord<T>> records = new BasicEventList<EListRecord<T>>();
        if (cfg.isSortable()) {
            records = new SortedList<EListRecord<T>>(records);
        }
        EListFilterComponent<T> filtercomponent = null;
        if (cfg.isFilterable()) {
            filtercomponent = new EListFilterComponent<T>(records);
            records = filtercomponent.grabRecords();
        }
        if (cfg.isThreadSafe()) {
            records = GlazedLists.threadSafeList(records);
        }
        EListModel<T> model = new EListModel<T>(records);
        model.filtercomponent = filtercomponent;
        return model;
    }

    protected EListConfig cfg;

    protected EventList<EListRecord<T>> records;

    protected DelegatingListCellRenderer delegatingListCellRenderer;

    protected EListSearchComponent<T> searchComponent = null;

    protected EListFilterComponent<T> filtercomponent = null;

    /**
     * do not use, do not change access
     */
    protected EList() {
        this.filtercomponent = null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public EList(EListConfig cfg) {
        super(EList.createModel(cfg.lock()));

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    EList.this.selectCell(new Point(e.getX(), e.getY()));
                }
            }
        });

        this.cfg = cfg;
        EListModel elistModel = EListModel.class.cast(this.getModel());
        this.records = elistModel.source;
        this.filtercomponent = elistModel.filtercomponent;
        if (this.filtercomponent != null) {
            this.filtercomponent.setList(this);
        }
        elistModel.filtercomponent = null;
        this.delegatingListCellRenderer = new DelegatingListCellRenderer(this.getCellRenderer());
        this.setCellRenderer(this.delegatingListCellRenderer);

        // drag and drop test jvm internally, intra jvm, tostring
        this.setDragEnabled(true);
        this.setTransferHandler(new EListTransferHandler<T>());

        UIUtils.registerLocaleChangeListener((EComponentI) this);

        if (cfg.isDefaultPopupMenu()) {
            EComponentPopupMenu.installPopupMenu(this);
        }
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
     * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy()
     */
    @Override
    public void copy() {
        StringBuilder sb = new StringBuilder();
        for (EListRecord<T> record : this) {
            sb.append(record.getStringValue()).append(SystemSettings.getNewline());
        }
        EComponentPopupMenu.copyToClipboard(sb.toString());
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

    public EListFilterComponent<T> getFiltercomponent() {
        return this.filtercomponent;
    }

    /**
     * 
     * @see org.swingeasy.HasParentComponent#getParentComponent()
     */
    @Override
    public JComponent getParentComponent() {
        return this;
    }

    /**
     * 
     * @see org.swingeasy.EListI#getRecords()
     */
    @Override
    public List<EListRecord<T>> getRecords() {
        return this.records;
    }

    /**
     * returns and creates if necessary {@link EListSearchComponent}
     */
    public EListSearchComponent<T> getSearchComponent() {
        if (this.searchComponent == null) {
            this.searchComponent = new EListSearchComponent<T>(this);
            this.searchComponent.setLocale(this.getLocale());
        }
        return this.searchComponent;
    }

    /**
     * 
     * @see org.swingeasy.EListI#getSelectedRecord()
     */
    @SuppressWarnings("unchecked")
    @Override
    public EListRecord<T> getSelectedRecord() {
        return (EListRecord<T>) this.getSelectedValue();
    }

    /**
     * 
     * @see org.swingeasy.EListI#getSelectedRecords()
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<EListRecord<T>> getSelectedRecords() {
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
     * threadsafe unmodifiable iterator
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<EListRecord<T>> iterator() {
        return Collections.unmodifiableCollection(this.getRecords()).iterator();
    }

    /**
     * 
     * @see org.swingeasy.EListI#moveSelectedDown()
     */
    @Override
    public void moveSelectedDown() {
        int selectedIndex = this.getSelectedIndex();

        // none selected
        if (selectedIndex == -1) {
            return;
        }

        // last selected
        if (this.records.size() == (selectedIndex + 1)) {
            return;
        }

        Collections.rotate(this.records, 1);
        this.setSelectedIndex(selectedIndex + 1);
    }

    /**
     * 
     * @see org.swingeasy.EListI#moveSelectedDown()
     */
    @Override
    public void moveSelectedUp() {
        int selectedIndex = this.getSelectedIndex();

        // none selected
        if (selectedIndex == -1) {
            return;
        }

        // first selected
        if (selectedIndex == 0) {
            return;
        }

        Collections.rotate(this.records, -1);
        this.setSelectedIndex(selectedIndex - 1);
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
     * @see org.swingeasy.EListI#removeRecords(java.util.Collection)
     */
    @Override
    public void removeRecords(Collection<EListRecord<T>> recordsToRemove) {
        this.records.removeAll(recordsToRemove);
    }

    /**
     * 
     * @see org.swingeasy.EListI#removeSelectedRecords()
     */
    @Override
    public void removeSelectedRecords() {
        Collection<EListRecord<T>> selectedRecords = this.getSelectedRecords();
        this.records.removeAll(selectedRecords);
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
     * 
     * @see org.swingeasy.EListI#selectCell(java.awt.Point)
     */
    @Override
    public void selectCell(Point point) {
        int idx = this.locationToIndex(point);
        if (idx != -1) {
            this.setSelectedIndex(idx);
        }
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

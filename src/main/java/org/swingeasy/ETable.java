package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.DropMode;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.commons.lang.StringUtils;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.gui.AbstractTableComparatorChooser;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import ca.odell.glazedlists.matchers.Matcher;
import ca.odell.glazedlists.swing.EventSelectionModel;
import ca.odell.glazedlists.swing.EventTableColumnModel;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.SortableRenderer;
import ca.odell.glazedlists.swing.TableComparatorChooser;

/**
 * @author Jurgen
 */
@SuppressWarnings("rawtypes")
public class ETable extends JTable implements ETableI, Reorderable {
    protected class EFiltering {
        /**
         * JDOC
         */
        protected class FilterPopup extends JWindow {
            /**
             * JDOC
             */
            protected class RecordMatcher implements Matcher<ETableRecord> {
                protected final Pattern pattern;

                protected final int column;

                /**
                 * Instantieer een nieuwe RecordMatcher
                 * 
                 * @param column
                 * @param text
                 */
                protected RecordMatcher(int column, String text) {
                    this.column = column;
                    this.pattern = text == null ? null : Pattern.compile(text, Pattern.CASE_INSENSITIVE);
                }

                /**
                 * @see ca.odell.glazedlists.matchers.Matcher#matches(java.lang.Object)
                 */
                @Override
                public boolean matches(ETableRecord item) {
                    if (this.pattern == null) {
                        return true;
                    }
                    String value = item.getStringValue(this.column);
                    if (value == null) {
                        return false;
                    }
                    return this.pattern.matcher(value).find();
                }
            }

            /** serialVersionUID */
            private static final long serialVersionUID = 5033445579635687866L;

            protected JTextField popupTextfield = new JTextField();

            protected int popupForColumn = -1;

            protected Map<Integer, String> popupFilters = new HashMap<Integer, String>();

            /**
             * Instantieer een nieuwe FilterPopup
             * 
             * @param frame
             */
            protected FilterPopup(Frame frame) {
                super(frame);

                this.popupTextfield.setBackground(new Color(246, 243, 149));
                this.getContentPane().add(this.popupTextfield, BorderLayout.CENTER);
                this.popupTextfield.setFocusTraversalKeysEnabled(false);
                this.popupTextfield.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        FilterPopup.this.setVisible(false);
                    }
                });
                this.popupTextfield.addKeyListener(new KeyAdapter() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void keyReleased(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            // preview filtering
                            System.out.println("preview filter " + FilterPopup.this.popupTextfield.getText()); //$NON-NLS-1$
                            ETable.this.filtering.matcherEditor.fire(new RecordMatcher(FilterPopup.this.popupForColumn,
                                    FilterPopup.this.popupTextfield.getText()));
                        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                            // revert filtering, close
                            System.out.println("revert filter"); //$NON-NLS-1$
                            ETable.this.filtering.matcherEditor.fire(new RecordMatcher(FilterPopup.this.popupForColumn, FilterPopup.this.popupFilters
                                    .get(FilterPopup.this.popupForColumn)));
                            FilterPopup.this.setVisible(false);
                        } else if (e.getKeyCode() == KeyEvent.VK_TAB) {
                            // commit filtering
                            System.out.println("filter " + FilterPopup.this.popupTextfield.getText()); //$NON-NLS-1$
                            ETable.this.filtering.matcherEditor.fire(new RecordMatcher(FilterPopup.this.popupForColumn,
                                    FilterPopup.this.popupTextfield.getText()));
                            FilterPopup.this.popupFilters.put(FilterPopup.this.popupForColumn, FilterPopup.this.popupTextfield.getText());
                            FilterPopup.this.setVisible(false);
                        } else {
                            //
                        }
                    }
                });
            }

            /**
             * JDOC
             * 
             * @param p
             */
            protected void activate(Point p) {
                this.popupForColumn = ETable.this.getTableHeader().columnAtPoint(p);
                String filter = this.popupFilters.get(this.popupForColumn);
                this.popupTextfield.setText(filter);
                Rectangle headerRect = ETable.this.getTableHeader().getHeaderRect(this.popupForColumn);
                Point pt = ETable.this.getLocationOnScreen();
                pt.translate(headerRect.x - 1, -headerRect.height - 1);
                this.setLocation(pt);
                this.setSize(headerRect.width, headerRect.height);
                this.toFront();
                this.setVisible(true);
                this.requestFocusInWindow();
                this.popupTextfield.requestFocusInWindow();
            }

            /**
             * JDOC
             */
            public void clear() {
                this.popupFilters.clear();
                this.popupForColumn = -1;
            }
        }

        /**
         * JDOC
         */
        protected class FilterPopupActivate extends MouseAdapter {
            /**
             * 
             * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                ETable.this.filtering.mouseClicked(e);
            }
        }

        protected RecordMatcherEditor matcherEditor;

        protected FilterList<ETableRecord> filteredRecords;

        protected FilterPopup filterPopup;

        protected EventList<ETableRecord> source;

        @SuppressWarnings("unchecked")
        protected EFiltering(EventList<ETableRecord> source, AbstractMatcherEditor<ETableRecord> matcher) {
            this.source = source;
            if (matcher != null) {
                // we do matching ourselves
                this.filteredRecords = new FilterList<ETableRecord>(source, matcher);
                return;
            }
            if (!ETable.this.cfg.isFilterable()) {
                return;
            }
            // default matching
            this.matcherEditor = new RecordMatcherEditor();
            this.filteredRecords = new FilterList<ETableRecord>(source, this.matcherEditor);
        }

        protected void clear() {
            if (!ETable.this.cfg.isFilterable()) {
                return;
            }
            this.getFilterPopup().clear();
        }

        protected FilterPopup getFilterPopup() {
            if (!ETable.this.cfg.isFilterable()) {
                return null;
            }
            if (this.filterPopup == null) {
                this.filterPopup = new FilterPopup(ETable.this.getFrame(ETable.this));
            }
            return this.filterPopup;
        }

        protected EventList<ETableRecord> getRecords() {
            if (this.filteredRecords == null) {
                return this.source;
            }
            return this.filteredRecords;
        }

        protected void install() {
            if (!ETable.this.cfg.isFilterable()) {
                return;
            }
            ETable.this.getTableHeader().addMouseListener(new FilterPopupActivate());
        }

        protected void mouseClicked(MouseEvent e) {
            if (!ETable.this.cfg.isFilterable()) {
                return;
            }
            if ((e.getClickCount() == 1) && (e.getButton() == MouseEvent.BUTTON3)) {
                this.getFilterPopup().activate(e.getPoint());
            }
        }

    }

    protected class ESorting {
        protected TableComparatorChooser<ETableRecord> tableSorter;

        protected SortedList<ETableRecord> sortedRecords;

        protected EventList<ETableRecord> source;

        protected ESorting(EventList<ETableRecord> source) {
            this.source = source;
            if (!ETable.this.cfg.isSortable()) {
                return;
            }
            this.sortedRecords = new SortedList<ETableRecord>(source, null);
        }

        protected void dispose() {
            if (!ETable.this.cfg.isSortable()) {
                return;
            }
            this.tableSorter.dispose();
        }

        protected EventList<ETableRecord> getRecords() {
            if (!ETable.this.cfg.isSortable()) {
                return this.source;
            }
            return this.sortedRecords;
        }

        protected void install() {
            if (!ETable.this.cfg.isSortable()) {
                return;
            }
            this.tableSorter = TableComparatorChooser.install(ETable.this, this.sortedRecords, AbstractTableComparatorChooser.MULTIPLE_COLUMN_MOUSE,
                    ETable.this.tableFormat);
        }

        protected void sort(int col) {
            if (!ETable.this.cfg.isSortable()) {
                return;
            }
            this.tableSorter.clearComparator();
            this.tableSorter.appendComparator(col, 0, false);

        }

        protected void unsort() {
            if (!ETable.this.cfg.isSortable()) {
                return;
            }
            this.tableSorter.clearComparator();
        }
    }

    public class ETableModel extends EventTableModel<ETableRecord> {
        private static final long serialVersionUID = -8936359559294414836L;

        protected ETableModel(EventList<ETableRecord> source, TableFormat<? super ETableRecord> tableFormat) {
            super(source, tableFormat);
        }

        /**
         * 
         * @see javax.swing.table.AbstractTableModel#fireTableCellUpdated(int, int)
         */
        @Override
        public void fireTableCellUpdated(int row, int column) {
            super.fireTableCellUpdated(row, column);
        };

        /**
         * 
         * @see ca.odell.glazedlists.swing.EventTableModel#getColumnClass(int)
         */
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            Class<?> clas = ETable.this.tableFormat.getColumnClass(columnIndex);
            // System.out.println("ETableModel.getColumnClass(" + columnIndex + ")=" + clas);
            return clas;
        }
    }

    public static abstract class Filter<T> extends RecordMatcherEditor<T> implements Matcher<ETableRecord<T>> {
        public void fire() {
            this.fire(this);
        }
    }

    /**
     * JDOC
     */
    protected static class RecordMatcherEditor<T> extends AbstractMatcherEditor<ETableRecord<T>> {
        protected void fire(Matcher<ETableRecord<T>> matcher) {
            this.fireChanged(matcher);
        }
    }

    /**
     * @see http://publicobject.com/glazedlistsdeveloper/
     */
    protected class VerticalHeaderRenderer extends VerticalTableHeaderCellRenderer implements SortableRenderer {
        private static final long serialVersionUID = 2995810245364522046L;

        protected Icon sortIcon = null;

        protected final static double DEGREE_90 = (90.0 * Math.PI) / 180.0;

        protected Map<Icon, Icon> rotatedIconsCache = new HashMap<Icon, Icon>();

        /**
         * Creates a rotated version of the input image.
         * 
         * @param c The component to get properties useful for painting, e.g. the foreground or background color.
         * @param icon the image to be rotated.
         * @param rotatedAngle the rotated angle, in degree, clockwise. It could be any double but we will mod it with 360 before using it.
         * 
         * @return the image after rotating.
         * 
         * @see http://www.jidesoft.com/blog/2008/02/29/rotate-an-icon-in-java/
         */
        protected ImageIcon createRotatedImage(Component c, Icon icon, double rotatedAngle) {
            // convert rotatedAngle to a value from 0 to 360
            double originalAngle = rotatedAngle % 360;
            if ((rotatedAngle != 0) && (originalAngle == 0)) {
                originalAngle = 360.0;
            }

            // convert originalAngle to a value from 0 to 90
            double angle = originalAngle % 90;
            if ((originalAngle != 0.0) && (angle == 0.0)) {
                angle = 90.0;
            }

            double radian = Math.toRadians(angle);

            int iw = icon.getIconWidth();
            int ih = icon.getIconHeight();
            int w;
            int h;

            if (((originalAngle >= 0) && (originalAngle <= 90)) || ((originalAngle > 180) && (originalAngle <= 270))) {
                w = (int) ((iw * Math.sin(VerticalHeaderRenderer.DEGREE_90 - radian)) + (ih * Math.sin(radian)));
                h = (int) ((iw * Math.sin(radian)) + (ih * Math.sin(VerticalHeaderRenderer.DEGREE_90 - radian)));
            } else {
                w = (int) ((ih * Math.sin(VerticalHeaderRenderer.DEGREE_90 - radian)) + (iw * Math.sin(radian)));
                h = (int) ((ih * Math.sin(radian)) + (iw * Math.sin(VerticalHeaderRenderer.DEGREE_90 - radian)));
            }
            BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            Graphics2D g2d = (Graphics2D) g.create();

            // calculate the center of the icon.
            int cx = iw / 2;
            int cy = ih / 2;

            // move the graphics center point to the center of the icon.
            g2d.translate(w / 2, h / 2);

            // rotate the graphcis about the center point of the icon
            g2d.rotate(Math.toRadians(originalAngle));

            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            icon.paintIcon(c, g2d, -cx, -cy);

            g2d.dispose();
            return new ImageIcon(image);
        }

        /**
         * 
         * @see org.swingeasy.VerticalTableHeaderCellRenderer#getIcon(javax.swing.JTable, int)
         */
        @Override
        protected Icon getIcon(JTable table, int column) {
            return this.sortIcon;
        }

        /**
         * 
         * @see ca.odell.glazedlists.swing.SortableRenderer#setSortIcon(javax.swing.Icon)
         */
        @Override
        public void setSortIcon(Icon sortIcon) {
            if (sortIcon != null) {
                Icon found = this.rotatedIconsCache.get(sortIcon);
                if (found == null) {
                    found = this.createRotatedImage(this, sortIcon, 90.0);
                    this.rotatedIconsCache.put(sortIcon, found);
                }
                this.sortIcon = found;
            } else {
                this.sortIcon = null;
            }
        }
    }

    private static final long serialVersionUID = 6515690492295488815L;

    protected final EventList<ETableRecord> records;

    protected final EventTableModel<ETableRecord> tableModel;

    protected final EventSelectionModel<ETableRecord> tableSelectionModel;

    protected final EFiltering filtering;

    protected final ESorting sorting;

    protected final ETableConfig cfg;

    protected ETableHeaders tableFormat;

    public ETable() {
        this(new ETableConfig(false));
    }

    public ETable(ETableConfig configuration) {
        this(configuration, null);
    }

    @SuppressWarnings("unchecked")
    public ETable(ETableConfig configuration, Filter matcher) {
        this.cfg = configuration;
        this.cfg.lock();
        if (this.cfg.isVertical()) {
            this.getTableHeader().setDefaultRenderer(new VerticalHeaderRenderer());
        }
        if (this.cfg.isDraggable()) {
            this.setDragEnabled(true);
            this.setDropMode(DropMode.INSERT_ROWS);
            this.setTransferHandler(new TableRowTransferHandler(this));
        }
        this.setColumnModel(new EventTableColumnModel<TableColumn>(new BasicEventList<TableColumn>()));
        this.records = (this.cfg.isThreadSafe() ? GlazedLists.threadSafeList(new BasicEventList<ETableRecord>()) : new BasicEventList<ETableRecord>());
        this.sorting = new ESorting(this.records);
        this.tableFormat = new ETableHeaders();
        this.filtering = new EFiltering(this.sorting.getRecords(), matcher);
        this.tableModel = new ETableModel(this.filtering.getRecords(), this.tableFormat);
        this.setModel(this.tableModel);
        this.tableSelectionModel = new EventSelectionModel<ETableRecord>(this.records);
        this.tableSelectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.setColumnSelectionAllowed(true);
        this.setRowSelectionAllowed(true);
        this.setSelectionModel(this.tableSelectionModel);
        this.sorting.install();
        this.filtering.install();
        this.getTableHeader().setReorderingAllowed(this.cfg.isReorderable());
        this.getTableHeader().setResizingAllowed(this.cfg.isResizable());
    }

    /**
     * 
     * @see org.swingeasy.ETableI#addRecord(org.swingeasy.ETableRecord)
     */
    @Override
    public void addRecord(final ETableRecord record) {
        this.records.add(record);
    }

    /**
     * 
     * @see org.swingeasy.ETableI#addRecords(java.util.Collection)
     */
    @Override
    public void addRecords(final Collection<ETableRecord> r) {
        this.records.addAll(r);
    }

    /**
     * 
     * @see org.swingeasy.ETableI#clear()
     */
    @Override
    public void clear() {
        this.records.clear();
        this.sorting.dispose();
        this.tableModel.setTableFormat(new ETableHeaders());
        this.filtering.clear();
    }

    /**
     * 
     * @see javax.swing.JTable#createDefaultEditors()
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void createDefaultEditors() {
        super.createDefaultEditors();
        this.defaultEditorsByColumnClass.put(Boolean.class, new javax.swing.UIDefaults.ProxyLazyValue("org.swingeasy.BooleanTableCellEditor"));
        this.defaultEditorsByColumnClass.put(Date.class, new javax.swing.UIDefaults.ProxyLazyValue("org.swingeasy.DateTableCellEditor"));
        this.defaultEditorsByColumnClass.put(Color.class, new javax.swing.UIDefaults.ProxyLazyValue("org.swingeasy.ColorTableCellEditor"));
        this.defaultEditorsByColumnClass.put(Number.class, new javax.swing.UIDefaults.ProxyLazyValue("org.swingeasy.NumberTableCellEditor"));
    }

    /**
     * 
     * @see javax.swing.JTable#createDefaultRenderers()
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void createDefaultRenderers() {
        super.createDefaultRenderers();
        this.defaultRenderersByColumnClass.put(Boolean.class, new javax.swing.UIDefaults.ProxyLazyValue("org.swingeasy.BooleanTableCellRenderer"));
        this.defaultRenderersByColumnClass.put(Date.class, new javax.swing.UIDefaults.ProxyLazyValue("org.swingeasy.DateTableCellRenderer"));
        this.defaultRenderersByColumnClass.put(Color.class, new javax.swing.UIDefaults.ProxyLazyValue("org.swingeasy.ColorTableCellRenderer"));
        this.defaultRenderersByColumnClass.put(Number.class, new javax.swing.UIDefaults.ProxyLazyValue("org.swingeasy.NumberTableCellRenderer"));
        this.defaultRenderersByColumnClass.put(Float.class, new javax.swing.UIDefaults.ProxyLazyValue("org.swingeasy.NumberTableCellRenderer"));
        this.defaultRenderersByColumnClass.put(Double.class, new javax.swing.UIDefaults.ProxyLazyValue("org.swingeasy.NumberTableCellRenderer"));
    }

    /**
     * 
     * @see javax.swing.JTable#createDefaultTableHeader()
     */
    @Override
    protected JTableHeader createDefaultTableHeader() {
        JTableHeader jTableHeader = new JTableHeader(this.columnModel) {
            private static final long serialVersionUID = -378778832166135907L;

            @Override
            public String getToolTipText(MouseEvent e) {
                Point p = e.getPoint();
                int index = this.columnModel.getColumnIndexAtX(p.x);
                if (index == -1) {
                    return null;
                }
                String headerValue = String.valueOf(ETable.this.getSimpleThreadSafeInterface().getColumnValueAtVisualColumn(index));

                if (ETable.this.cfg.isFilterable()) {
                    String filter = ETable.this.filtering.getFilterPopup().popupFilters.get(index);

                    if ((filter != null) && (filter.trim().length() > 0)) {
                        headerValue += "<br/>" + "filter: '" + filter + "'";
                    } else {
                        headerValue += "<br/>" + "no filter";
                    }

                    headerValue += "<br/><br/>right click to edit filter<br/>enter to preview filter<br/>tab to accept filter";
                }

                return "<html><body>" + headerValue + "</body></html>";
            }
        };
        return jTableHeader;
    }

    /**
     * 
     * @see javax.swing.JTable#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Class<?> clas = super.getColumnClass(columnIndex);
        // System.out.println("ETableHeaders.getColumnClass(" + columnIndex + ")=" + clas);
        return clas;
    }

    /**
     * 
     * @see org.swingeasy.ETableI#getColumnValueAtVisualColumn(int)
     */
    @Override
    public Object getColumnValueAtVisualColumn(int i) {
        return this.getColumnModel().getColumn(i).getHeaderValue();
    }

    /**
     * JDOC
     * 
     * @param comp
     * @return
     */
    protected Frame getFrame(Component comp) {
        if (comp == null) {
            comp = this;
        }
        if (comp.getParent() instanceof Frame) {
            return (Frame) comp.getParent();
        }
        return this.getFrame(comp.getParent());
    }

    /**
     * 
     * @see org.swingeasy.ETableI#getHeadernames()
     */
    @Override
    public List<String> getHeadernames() {
        return this.tableFormat.getColumnNames();
    }

    /**
     * 
     * @see org.swingeasy.ETableI#getRecordAtVisualRow(int)
     */
    @Override
    public ETableRecord getRecordAtVisualRow(int i) {
        return this.filtering.getRecords().get(i);
    }

    /**
     * 
     * @see org.swingeasy.ETableI#getRecords()
     */
    @Override
    public List<ETableRecord> getRecords() {
        return this.filtering.getRecords();
    }

    /**
     * JDOC
     * 
     * @return
     */
    public ETable getSimpleThreadSafeInterface() {
        try {
            return EventThreadSafeWrapper.getSimpleThreadSafeInterface(ETable.class, this, ETableI.class);
        } catch (Exception ex) {
            System.err.println(ex);
            return this; // no javassist
        }
    }

    /**
     * 
     * @see javax.swing.JTable#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        return this.cfg.isEditable() && super.isCellEditable(row, column);
    }

    /**
     * 
     * @see org.swingeasy.ETableI#packColumn(int)
     */
    @Override
    public void packColumn(int vColIndex) {
        this.packColumn(vColIndex, 4);
    }

    /**
     * Sets the preferred width of the visible column specified by vColIndex. The column will be just wide enough to show the column head and the
     * widest cell in the column. margin pixels are added to the left and right (resulting in an additional width of 2*margin pixels).
     * 
     * @param table
     * @param vColIndex
     * @param margin
     */
    @Override
    public void packColumn(int vColIndex, int margin) {
        TableColumnModel colModel = this.getColumnModel();
        TableColumn col = colModel.getColumn(vColIndex);
        int width = 0;

        // Get width of column header
        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = this.getTableHeader().getDefaultRenderer();
        }
        Component comp = renderer.getTableCellRendererComponent(this, col.getHeaderValue(), false, false, 0, 0);
        width = comp.getPreferredSize().width;

        // Get maximum width of column data
        for (int r = 0; r < this.getRowCount(); r++) {
            renderer = this.getCellRenderer(r, vColIndex);
            comp = renderer.getTableCellRendererComponent(this, this.getValueAt(r, vColIndex), false, false, r, vColIndex);
            width = Math.max(width, comp.getPreferredSize().width);
        }

        // Add margin
        width += 2 * margin;

        // Set the width
        col.setPreferredWidth(width);
        col.setWidth(width);
        col.setMaxWidth(width);
    }

    /**
     * 
     * @see javax.swing.JTable#prepareRenderer(javax.swing.table.TableCellRenderer, int, int)
     */
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
        Component c = this.super_prepareRenderer(renderer, rowIndex, vColIndex);
        if (c instanceof JLabel) {
            String text = JLabel.class.cast(c).getText();
            JLabel.class.cast(c).setToolTipText(StringUtils.isNotBlank(text) ? text : null);
        }
        return c;
    }

    /**
     * 
     * @see org.swingeasy.ETableI#removeAllRecords()
     */
    @Override
    public void removeAllRecords() {
        this.records.clear();
    }

    /**
     * 
     * @see org.swingeasy.ETableI#removeRecord(org.swingeasy.ETableRecord)
     */
    @Override
    public void removeRecord(final ETableRecord record) {
        this.records.remove(record);
    }

    /**
     * 
     * @see org.swingeasy.ETableI#removeRecordAtVisualRow(int)
     */
    @Override
    public void removeRecordAtVisualRow(final int i) {
        this.records.remove(this.sorting.getRecords().get(i));
    }

    /**
     * 
     * @see org.swingeasy.Reorderable#reorder(int, int)
     */
    @Override
    public void reorder(int fromIndex, int toIndex) {
        ETableRecord record = this.getRecordAtVisualRow(fromIndex);
        this.removeRecord(record);
        this.records.add(toIndex, record);
    }

    /**
     * 
     * @see org.swingeasy.ETableI#scrollToVisibleRecord(org.swingeasy.ETableRecord)
     */
    @Override
    public void scrollToVisibleRecord(ETableRecord record) {
        if (!this.isDisplayable()) {
            throw new IllegalArgumentException("can only be used when table is displayable (visible)"); //$NON-NLS-1$
        }
        int index = this.filtering.getRecords().indexOf(record);
        Rectangle cellbounds = this.getCellRect(index, index, true);
        this.scrollRectToVisible(cellbounds);
    }

    /**
     * 
     * @see org.swingeasy.ETableI#setHeaders(org.swingeasy.ETableHeaders)
     */
    @Override
    public void setHeaders(final ETableHeaders headers) {
        this.tableFormat = headers;
        this.sorting.dispose();
        this.tableModel.setTableFormat(headers);
        this.sorting.install();
    }

    /**
     * 
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
    }

    /**
     * 
     * @see org.swingeasy.ETableI#sort(int)
     */
    @Override
    public void sort(final int col) {
        this.sorting.sort(col);
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public ETable stsi() {
        return this.getSimpleThreadSafeInterface();
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public ETable STSI() {
        return this.getSimpleThreadSafeInterface();
    }

    /**
     * calls original renderer
     * 
     * @param renderer
     * @param rowIndex
     * @param vColIndex
     * @return
     */
    protected Component super_prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
        return super.prepareRenderer(renderer, rowIndex, vColIndex);
    }

    /**
     * 
     * @see org.swingeasy.ETableI#unsort()
     */
    @Override
    public void unsort() {
        this.sorting.unsort();
    }
}

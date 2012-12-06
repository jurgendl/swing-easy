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
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.regex.Pattern;

import javax.swing.DropMode;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.commons.lang.StringUtils;
import org.swingeasy.EComponentPopupMenu.ReadableComponent;
import org.swingeasy.system.SystemSettings;
import org.swingeasy.table.editor.BooleanTableCellEditor;
import org.swingeasy.table.editor.ColorTableCellEditor;
import org.swingeasy.table.editor.DateTableCellEditor;
import org.swingeasy.table.editor.NumberTableCellEditor;
import org.swingeasy.table.renderer.BooleanTableCellRenderer;
import org.swingeasy.table.renderer.ByteArrayTableCellRenderer;
import org.swingeasy.table.renderer.ColorTableCellRenderer;
import org.swingeasy.table.renderer.DateTableCellRenderer;
import org.swingeasy.table.renderer.NumberTableCellRenderer;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.ListSelection;
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
 * 
 * @see http://www.chka.de/swing/table/faq.html
 */
public class ETable<T> extends JTable implements ETableI<T>, Reorderable, Iterable<ETableRecord<T>>, ReadableComponent {
    protected class EFiltering {
        /**
         * JDOC
         */
        protected class FilterPopup extends JWindow {
            /**
             * JDOC
             */
            protected class RecordMatcher implements Matcher<ETableRecord<T>> {
                protected final Map<Integer, Pattern> filters = new HashMap<Integer, Pattern>();

                protected RecordMatcher(int column, String text) {
                    if (text != null) {
                        this.filters.put(column, Pattern.compile(text, Pattern.CASE_INSENSITIVE));
                    }
                }

                protected RecordMatcher(Map<Integer, String> popupFilters) {
                    for (Map.Entry<Integer, String> entry : popupFilters.entrySet()) {
                        if (entry.getValue() != null) {
                            this.filters.put(entry.getKey(), Pattern.compile(entry.getValue(), Pattern.CASE_INSENSITIVE));
                        }
                    }
                }

                /**
                 * @see ca.odell.glazedlists.matchers.Matcher#matches(java.lang.Object)
                 */
                @Override
                public boolean matches(ETableRecord<T> item) {
                    for (Map.Entry<Integer, Pattern> entry : this.filters.entrySet()) {
                        String value = item.getStringValue(entry.getKey());
                        if (!entry.getValue().matcher(value).find()) {
                            return false;
                        }
                    }
                    return true;
                }
            }

            private static final long serialVersionUID = 5033445579635687866L;

            protected JTextField popupTextfield = new JTextField();

            protected int popupForColumn = -1;

            protected Map<Integer, String> popupFilters = new HashMap<Integer, String>();

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
                            FilterPopup.this.popupFilters.put(FilterPopup.this.popupForColumn, FilterPopup.this.popupTextfield.getText());
                            System.out.println("filter " + FilterPopup.this.popupFilters); //$NON-NLS-1$
                            ETable.this.filtering.matcherEditor.fire(new RecordMatcher(FilterPopup.this.popupFilters));
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

        protected RecordMatcherEditor<T> matcherEditor;

        protected FilterList<ETableRecord<T>> filteredRecords;

        protected FilterPopup filterPopup;

        protected EventList<ETableRecord<T>> source;

        protected EFiltering(EventList<ETableRecord<T>> source, AbstractMatcherEditor<ETableRecord<T>> matcher) {
            this.source = source;
            if (matcher != null) {
                // we do matching ourselves
                this.filteredRecords = new FilterList<ETableRecord<T>>(source, matcher);
                return;
            }
            if (!ETable.this.cfg.isFilterable()) {
                return;
            }
            // default matching
            this.matcherEditor = new RecordMatcherEditor<T>();
            this.filteredRecords = new FilterList<ETableRecord<T>>(source, this.matcherEditor);
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

        protected EventList<ETableRecord<T>> getRecords() {
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
        protected TableComparatorChooser<ETableRecord<T>> tableSorter;

        protected SortedList<ETableRecord<T>> sortedRecords;

        protected EventList<ETableRecord<T>> source;

        protected ESorting(EventList<ETableRecord<T>> source) {
            this.source = source;
            if (!ETable.this.cfg.isSortable()) {
                return;
            }
            this.sortedRecords = new SortedList<ETableRecord<T>>(source, null);
        }

        protected void dispose() {
            if (!ETable.this.cfg.isSortable()) {
                return;
            }
            this.tableSorter.dispose();
        }

        protected EventList<ETableRecord<T>> getRecords() {
            if (!ETable.this.cfg.isSortable()) {
                return this.source;
            }
            return this.sortedRecords;
        }

        protected void install() {
            if (!ETable.this.cfg.isSortable()) {
                return;
            }
            this.tableSorter = TableComparatorChooser.install(ETable.this, this.sortedRecords,
                    AbstractTableComparatorChooser.MULTIPLE_COLUMN_MOUSE_WITH_UNDO, ETable.this.tableFormat);
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

    public class ETableModel extends EventTableModel<ETableRecord<T>> {
        private static final long serialVersionUID = -8936359559294414836L;

        protected ETableModel(EventList<ETableRecord<T>> source, TableFormat<? super ETableRecord<T>> tableFormat) {
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

    protected final EventList<ETableRecord<T>> records;

    protected final EventTableModel<ETableRecord<T>> tableModel;

    protected final EventSelectionModel<ETableRecord<T>> tableSelectionModel;

    protected final EFiltering filtering;

    protected final ESorting sorting;

    protected final ETableConfig cfg;

    protected ETableHeaders<T> tableFormat;

    protected static final String TABLE_RENDERERS_DIR = "org.swingeasy.table.renderer";

    protected static final String TABLE_EDITORS_DIR = "org.swingeasy.table.editor";

    /**
     * use other constructors instead
     */
    protected ETable() {
        this.filtering = null;
        this.sorting = null;
        this.cfg = null;
        this.records = null;
        this.tableModel = null;
        this.tableSelectionModel = null;
    }

    public ETable(ETableConfig configuration) {
        this(configuration, null);
    }

    public ETable(ETableConfig configuration, Filter<T> matcher) {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    ETable.this.selectCell(new Point(e.getX(), e.getY()));
                }
            }
        });

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
        this.records = (this.cfg.isThreadSafe() ? GlazedLists.threadSafeList(new BasicEventList<ETableRecord<T>>())
                : new BasicEventList<ETableRecord<T>>());
        this.sorting = new ESorting(this.records);
        this.tableFormat = new ETableHeaders<T>();
        this.filtering = new EFiltering(this.sorting.getRecords(), matcher);
        this.tableModel = new ETableModel(this.filtering.getRecords(), this.tableFormat);
        this.setModel(this.tableModel);
        this.tableSelectionModel = new EventSelectionModel<ETableRecord<T>>(this.records);
        this.tableSelectionModel.setSelectionMode(ListSelection.MULTIPLE_INTERVAL_SELECTION_DEFENSIVE);
        this.setColumnSelectionAllowed(true);
        this.setRowSelectionAllowed(true);
        this.setSelectionModel(this.tableSelectionModel);
        this.sorting.install();
        this.filtering.install();
        this.getTableHeader().setReorderingAllowed(this.cfg.isReorderable());
        this.getTableHeader().setResizingAllowed(this.cfg.isResizable());

        UIUtils.registerLocaleChangeListener((EComponentI) this);

        if (this.cfg.isDefaultPopupMenu()) {
            this.installPopupMenuAction(EComponentPopupMenu.installPopupMenu(this));
        }
    }

    /**
     * 
     * @see org.swingeasy.ETableI#addRecord(org.swingeasy.ETableRecord)
     */
    @Override
    public void addRecord(final ETableRecord<T> record) {
        this.records.add(record);
    }

    /**
     * 
     * @see org.swingeasy.ETableI#addRecords(java.util.Collection)
     */
    @Override
    public void addRecords(final Collection<ETableRecord<T>> r) {
        this.records.addAll(r);
    }

    /**
     * add rowheader to scrollpane, 4 digit number
     */
    public void addRowHeader(JScrollPane scrollpane) {
        this.addRowHeader(scrollpane, 4);
    }

    /**
     * add rowheader to scrollpane
     */
    public void addRowHeader(JScrollPane scrollpane, int cw) {
        RowNumberTable rowTable = this.getRowHeader(cw);
        scrollpane.setRowHeaderView(rowTable);
        scrollpane.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, rowTable.getTableHeader());
    }

    /**
     * 
     * @see org.swingeasy.ETableI#clear()
     */
    @Override
    public void clear() {
        this.tableSelectionModel.clearSelection();
        this.records.clear();
        this.sorting.dispose();
        this.tableModel.setTableFormat(new ETableHeaders<T>());
        this.filtering.clear();
    }

    /**
     * 
     * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy(java.awt.event.ActionEvent)
     */
    @Override
    public void copy(ActionEvent e) {
        StringBuilder sb = new StringBuilder();
        for (Object cell : this.getSelectedCells()) {
            sb.append(String.valueOf(cell)).append(SystemSettings.getNewline());
        }
        EComponentPopupMenu.copyToClipboard(sb.toString());
    }

    /**
     * 
     * @see javax.swing.JTable#createDefaultEditors()
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void createDefaultEditors() {
        super.createDefaultEditors();
        this.defaultEditorsByColumnClass.put(Boolean.class, new javax.swing.UIDefaults.ProxyLazyValue(BooleanTableCellEditor.class.getName()));
        this.defaultEditorsByColumnClass.put(Date.class, new javax.swing.UIDefaults.ProxyLazyValue(DateTableCellEditor.class.getName()));
        this.defaultEditorsByColumnClass.put(Color.class, new javax.swing.UIDefaults.ProxyLazyValue(ColorTableCellEditor.class.getName()));
        this.defaultEditorsByColumnClass.put(Number.class, new javax.swing.UIDefaults.ProxyLazyValue(NumberTableCellEditor.class.getName()));
    }

    /**
     * 
     * @see javax.swing.JTable#createDefaultRenderers()
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void createDefaultRenderers() {
        super.createDefaultRenderers();
        this.defaultRenderersByColumnClass.put(Boolean.class, new javax.swing.UIDefaults.ProxyLazyValue(BooleanTableCellRenderer.class.getName()));
        this.defaultRenderersByColumnClass.put(Date.class, new javax.swing.UIDefaults.ProxyLazyValue(DateTableCellRenderer.class.getName()));
        this.defaultRenderersByColumnClass.put(Color.class, new javax.swing.UIDefaults.ProxyLazyValue(ColorTableCellRenderer.class.getName()));
        this.defaultRenderersByColumnClass.put(Number.class, new javax.swing.UIDefaults.ProxyLazyValue(NumberTableCellRenderer.class.getName()));
        this.defaultRenderersByColumnClass.put(Float.class, new javax.swing.UIDefaults.ProxyLazyValue(NumberTableCellRenderer.class.getName()));
        this.defaultRenderersByColumnClass.put(Double.class, new javax.swing.UIDefaults.ProxyLazyValue(NumberTableCellRenderer.class.getName()));
        this.defaultRenderersByColumnClass.put(byte[].class, new javax.swing.UIDefaults.ProxyLazyValue(ByteArrayTableCellRenderer.class.getName()));
        this.defaultRenderersByColumnClass.put(Byte[].class, new javax.swing.UIDefaults.ProxyLazyValue(ByteArrayTableCellRenderer.class.getName()));
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
                        headerValue += "<br/>" + "filter: '" + filter + "'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    } else {
                        headerValue += "<br/>" + "no filter"; //$NON-NLS-1$ //$NON-NLS-2$
                    }

                    headerValue += "<br/><br/>right click to edit filter<br/>enter to preview filter<br/>tab to accept filter"; //$NON-NLS-1$
                }

                return "<html><body>" + headerValue + "</body></html>"; //$NON-NLS-1$ //$NON-NLS-2$
            }
        };
        return jTableHeader;
    }

    /**
     * @see javax.swing.JTable#getCellRenderer(int, int)
     */
    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        return super.getCellRenderer(row, column);
    }

    /**
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
     * 
     * @see javax.swing.JTable#getDefaultEditor(java.lang.Class)
     */
    @Override
    public TableCellEditor getDefaultEditor(Class<?> columnClass) {
        TableCellEditor de = super.getDefaultEditor(columnClass);
        if (de instanceof EComponentI) {
            UIUtils.registerLocaleChangeListener(EComponentI.class.cast(de));
            // } else if (de instanceof Component) {
            // UIUtils.registerLocaleChangeListener(Component.class.cast(de));
        }
        return de;
    }

    /**
     * 
     * @see javax.swing.JTable#getDefaultRenderer(java.lang.Class)
     */
    @Override
    public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
        TableCellRenderer dr = super.getDefaultRenderer(columnClass);

        if (dr instanceof EComponentI) {
            UIUtils.registerLocaleChangeListener(EComponentI.class.cast(dr));
            // } else if (dr instanceof Component) {
            // UIUtils.registerLocaleChangeListener(Component.class.cast(dr));
        }
        return dr;
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
        List<String> columnNames = this.tableFormat.getColumnNames();
        return columnNames;
    }

    /**
     * gets tableFormat
     * 
     * @return Returns the tableFormat.
     */
    public ETableHeaders<T> getHeaders() {
        return this.tableFormat;
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
     * @see org.swingeasy.ETableI#getRecordAtVisualRow(int)
     */
    @Override
    public ETableRecord<T> getRecordAtVisualRow(int i) {
        return this.filtering.getRecords().get(i);
    }

    /**
     * 
     * @see org.swingeasy.ETableI#getRecords()
     */
    @Override
    public List<ETableRecord<T>> getRecords() {
        return this.filtering.getRecords();
    }

    /**
     * @see #getRowHeader()
     */
    public RowNumberTable getRowHeader() {
        return this.getRowHeader(4);
    }

    /**
     * gets row header that can be added to the RowHeaderView of a {@link JScrollPane} or can be docked WEST in a {@link BorderLayout}
     */
    public RowNumberTable getRowHeader(int cw) {
        return new RowNumberTable(this, cw);
    }

    /**
     * 
     * @see org.swingeasy.ETableI#getSelectedCell()
     */
    @SuppressWarnings("unchecked")
    @Override
    public T getSelectedCell() {
        ETableRecord<T> record = this.getSelectedRecord();
        if (record == null) {
            return null;
        }
        return (T) record.get(this.getSelectedColumn());
    }

    /**
     * 
     * @see org.swingeasy.ETableI#getSelectedCells()
     */
    @Override
    public List<Object> getSelectedCells() {
        List<Object> cells = new ArrayList<Object>();
        for (ETableRecord<T> record : this.getSelectedRecords()) {
            cells.add(record.get(this.getSelectedColumn()));
        }
        return cells;
    }

    /**
     * 
     * @see org.swingeasy.ETableI#getSelectedRecord()
     */
    @Override
    public ETableRecord<T> getSelectedRecord() {
        EventList<ETableRecord<T>> selected = this.tableSelectionModel.getSelected();
        return selected.size() == 0 ? null : selected.iterator().next();

    }

    /**
     * 
     * @see org.swingeasy.ETableI#getSelectedRecords()
     */
    @Override
    public List<ETableRecord<T>> getSelectedRecords() {
        return this.tableSelectionModel.getSelected();
    }

    /**
     * JDOC
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public ETable<T> getSimpleThreadSafeInterface() {
        try {
            return EventThreadSafeWrapper.getSimpleThreadSafeInterface(ETable.class, this, ETableI.class);
        } catch (Exception ex) {
            System.err.println(ex);
            return this; // no javassist
        }
    }

    /**
     * JDOC
     */
    protected void installPopupMenuAction(JPopupMenu menu) {
        menu.addSeparator();
        @SuppressWarnings({ "unchecked", "rawtypes" })
        ServiceLoader<ETableExporter<T>> exporterService = (ServiceLoader) ServiceLoader.load(ETableExporter.class);
        Iterator<ETableExporter<T>> iterator = exporterService.iterator();
        while (iterator.hasNext()) {
            try {
                ETableExporter<T> exporter = iterator.next();
                ETableExporterAction<T> action = new ETableExporterAction<T>(exporter, this);
                menu.add(action);
            } catch (ServiceConfigurationError ex) {
                ex.printStackTrace(System.err);
            }
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
     * threadsafe unmodifiable iterator
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<ETableRecord<T>> iterator() {
        return Collections.unmodifiableCollection(this.getRecords()).iterator();
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
    public void removeRecord(final ETableRecord<T> record) {
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
     * remove rowheader
     */
    public void removeRowHeader(JScrollPane scrollpane) {
        if (scrollpane.getRowHeader().getView() instanceof RowNumberTable) {
            scrollpane.getRowHeader().removeAll();
            scrollpane.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, new JComponent() {
                private static final long serialVersionUID = 1L;
            });
        }
    }

    /**
     * 
     * @see org.swingeasy.Reorderable#reorder(int, int)
     */
    @Override
    public void reorder(int fromIndex, int toIndex) {
        ETableRecord<T> record = this.getRecordAtVisualRow(fromIndex);
        this.removeRecord(record);
        this.records.add(toIndex, record);
    }

    /**
     * 
     * @see org.swingeasy.ETableI#scrollToVisibleRecord(org.swingeasy.ETableRecord)
     */
    @Override
    public void scrollToVisibleRecord(ETableRecord<T> record) {
        if (!this.isDisplayable()) {
            throw new IllegalArgumentException("can only be used when table is displayable (visible)"); //$NON-NLS-1$
        }
        int index = this.filtering.getRecords().indexOf(record);
        Rectangle cellbounds = this.getCellRect(index, index, true);
        this.scrollRectToVisible(cellbounds);
    }

    /**
     * 
     * @see org.swingeasy.ETableI#selectCell(java.awt.Point)
     */
    @Override
    public void selectCell(Point p) {
        int r = this.rowAtPoint(p);
        if (r == -1) {
            return;
        }
        int c = this.columnAtPoint(p);
        if (c == -1) {
            return;
        }
        // if already selected: do nothing (this keeps multiple selection when current cell is part of it
        for (int sr : this.getSelectedRows()) {
            if (sr == r) {
                for (int sc : this.getSelectedColumns()) {
                    if (sc == c) {
                        return;
                    }
                }
            }
        }
        this.setColumnSelectionInterval(c, c);
        this.setRowSelectionInterval(r, r);
    }

    /**
     * 
     * @see org.swingeasy.ETableI#setHeaders(org.swingeasy.ETableHeaders)
     */
    @Override
    public void setHeaders(final ETableHeaders<T> headers) {
        // we do not want null here, use an empty header object instead
        if (headers == null) {
            throw new NullPointerException();
        }

        this.tableFormat = headers;
        this.sorting.dispose();
        this.tableModel.setTableFormat(headers);
        this.sorting.install();
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
     * @see org.swingeasy.ETableI#sort(int)
     */
    @Override
    public void sort(final int col) {
        this.sorting.sort(col);
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public ETable<T> stsi() {
        return this.getSimpleThreadSafeInterface();
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public ETable<T> STSI() {
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

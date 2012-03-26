package org.swingeasy;

import java.awt.Component;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 * Use a JTable as a renderer for row numbers of a given main table. This table must be added to the row header of the scrollpane that contains the
 * main table.
 * 
 * @see http://tips4java.wordpress.com/2008/11/18/row-number-table/
 */
public class RowNumberTable extends JTable implements ChangeListener, PropertyChangeListener {
    /**
     * Borrow the renderer from JDK1.4.2 table header
     */
    @SuppressWarnings("unused")
    private static class RowNumberRenderer extends DefaultTableCellRenderer.UIResource {
        private static final long serialVersionUID = -4438439969007063384L;

        public RowNumberRenderer() {
            this.setHorizontalTextPosition(SwingConstants.RIGHT);
            this.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        /**
         * 
         * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int,
         *      int)
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (table != null) {
                JTableHeader header = table.getTableHeader();

                if (header != null) {
                    this.setForeground(header.getForeground());
                    this.setBackground(header.getBackground());
                    this.setFont(header.getFont());
                }
            }

            if (isSelected) {
                this.setFont(this.getFont().deriveFont(Font.BOLD));
            }

            this.setText((value == null) ? "" : value.toString()); //$NON-NLS-1$
            this.setBorder(UIManager.getBorder("TableHeader.cellBorder")); //$NON-NLS-1$

            return this;
        }
    }

    private static final long serialVersionUID = 1854035830771958284L;

    private JTable main;

    public RowNumberTable(JTable table) {
        this(table, 4);
    }

    private RowNumberTable(JTable table, double w) {
        this.main = table;
        this.main.addPropertyChangeListener(this);

        this.setFocusable(false);
        this.setAutoCreateColumnsFromModel(false);
        this.setModel(this.main.getModel());
        this.setSelectionModel(this.main.getSelectionModel());

        TableColumn column = new TableColumn();
        column.setHeaderValue(" "); //$NON-NLS-1$
        this.addColumn(column);

        column.setCellRenderer(this.getTableHeader().getDefaultRenderer());

        this.getColumnModel().getColumn(0).setPreferredWidth((int) w);
        this.setPreferredScrollableViewportSize(this.getPreferredSize());
    }

    public RowNumberTable(JTable table, int chars) {
        this(table, new JTextField(chars).getPreferredSize().getWidth());
    }

    @Override
    public void addNotify() {
        super.addNotify();

        Component c = this.getParent();

        // Keep scrolling of the row table in sync with the main table.

        if (c instanceof JViewport) {
            JViewport viewport = (JViewport) c;
            viewport.addChangeListener(this);
        }
    }

    /**
     * Delegate method to main table
     */
    @Override
    public int getRowCount() {
        return this.main.getRowCount();
    }

    /**
     * 
     * @see javax.swing.JTable#getRowHeight(int)
     */
    @Override
    public int getRowHeight(int row) {
        return this.main.getRowHeight(row);
    }

    /**
     * This table does not use any data from the main TableModel, so just return a value based on the row parameter.
     */
    @Override
    public Object getValueAt(int row, int column) {
        return Integer.toString(row + 1);
    }

    /**
     * Don't edit data in the main TableModel by mistake
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /**
     * Implement the PropertyChangeListener
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        // Keep the row table in sync with the main table

        if ("selectionModel".equals(e.getPropertyName())) { //$NON-NLS-1$
            this.setSelectionModel(this.main.getSelectionModel());
        }

        if ("model".equals(e.getPropertyName())) { //$NON-NLS-1$
            this.setModel(this.main.getModel());
        }
    }

    /**
     ** Implement the ChangeListener
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        // Keep the scrolling of the row table in sync with main table

        JViewport viewport = (JViewport) e.getSource();
        JScrollPane scrollPane = (JScrollPane) viewport.getParent();
        scrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
    }
}

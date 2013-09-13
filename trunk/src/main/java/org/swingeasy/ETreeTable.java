package org.swingeasy;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.apache.commons.lang.StringUtils;
import org.swingeasy.EComponentPopupMenu.ReadableComponent;
import org.swingeasy.table.renderer.ETreeTableCellRenderer;

/**
 * @author Jurgen
 */
public class ETreeTable extends JTable implements MouseListener, ETreeTableI, ReadableComponent {
    public static enum CheckMode {
        NONE, NODE, NODE_AND_PARENTS, NODE_AND_CHILDREN;
    }

    private static final long serialVersionUID = -3695054922987210010L;

    protected ETreeTableCellRenderer tree;

    protected CheckMode checkMode = CheckMode.NONE;

    protected ETreeTableConfig cfg;

    protected ETreeTable stsi;

    protected ETreeTable() {
        this(new ETreeTableRecordNode(), new ETreeTableHeaders());
    }

    protected ETreeTable(ETreeTableConfig cfg, final ETreeTableModel model) {
        super(model);
        this.init(this.cfg = cfg.lock());
    }

    public ETreeTable(ETreeTableConfig cfg, ETreeTableRecordNode root, ETreeTableHeaders headers) {
        this(cfg, new ETreeTableModel(root, headers));
    }

    public ETreeTable(ETreeTableRecordNode root, ETreeTableHeaders headers) {
        this(new ETreeTableConfig(), root, headers);
    }

    /**
     * 
     * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy(java.awt.event.ActionEvent)
     */
    @Override
    public void copy(ActionEvent e) {
        throw new UnsupportedOperationException("not implemented"); // TODO implement
    }

    /**
     * 
     * @see javax.swing.JTable#createDefaultTableHeader()
     */
    @Override
    protected JTableHeader createDefaultTableHeader() {
        JTableHeader jTableHeader = new JTableHeader(this.columnModel) {
            private static final long serialVersionUID = -7094445731424724186L;

            @Override
            public String getToolTipText(MouseEvent e) {
                Point p = e.getPoint();
                int index = this.columnModel.getColumnIndexAtX(p.x);
                if (index == -1) {
                    return null;
                }
                String headerValue = String.valueOf(ETreeTable.this.getColumnName(index));
                return StringUtils.isNotBlank(headerValue) ? headerValue : null;
            }
        };
        return jTableHeader;
    }

    public CheckMode getCheckMode() {
        return this.checkMode;
    }

    /**
     * 
     * @see javax.swing.JTable#getDefaultRenderer(java.lang.Class)
     */
    @Override
    public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
        TableCellRenderer dr = super.getDefaultRenderer(columnClass);
        if (dr instanceof Component) {
            final Component c = Component.class.cast(dr);
            this.addPropertyChangeListener("locale", new PropertyChangeListener() { //$NON-NLS-1$
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            c.setLocale(Locale.class.cast(evt.getNewValue()));
                        }
                    });
        }
        return dr;
    }

    public ETreeTable getOriginal() {
        return this;
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
     * JDOC
     * 
     * @return
     */
    public ETreeTable getSimpleThreadSafeInterface() {
        try {
            if (this.stsi == null) {
                this.stsi = EventThreadSafeWrapper.getSimpleThreadSafeInterface(ETreeTable.class, this, ETreeTableI.class);
            }
            return this.stsi;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public ETreeTableModel getTreeTableModel() {
        return (ETreeTableModel) this.getModel();
    }

    protected void init(ETreeTableConfig config) {
        this.getTreeTableModel().parent = this;
        this.tree = new ETreeTableCellRenderer(this, this.getTreeTableModel());
        this.getColumn(this.getColumnName(0)).setCellRenderer(this.tree);
        this.setIntercellSpacing(new Dimension(0, 0));
        this.setRowHeight(18);
        this.addMouseListener(this);

        UIUtils.registerLocaleChangeListener((EComponentI) this);

        if (config.isDefaultPopupMenu()) {
            EComponentPopupMenu.installPopupMenu(this);
        }
    }

    /**
     * 
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent evt) {
        if ((evt.getClickCount() == 1) && (evt.getButton() == MouseEvent.BUTTON1)) {
            this.nodeExpandToggle(evt);
        }
        if ((evt.getClickCount() == 1) && (evt.getButton() == MouseEvent.BUTTON3)) {
            this.nodeSelectionToggle(evt);
        }
    }

    /**
     * 
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        //
    }

    /**
     * 
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(MouseEvent e) {
        //
    }

    /**
     * 
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e) {
        //
    }

    /**
     * 
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        //
    }

    private void nodeAndChildrenSelectionToggle(ETreeTableRecordNode recordnode, boolean newvalue) {
        int row = this.getTreeTableModel().getRowIndex(recordnode);
        this.nodeSelectionToggle(row, recordnode, newvalue);
        for (ETreeTableRecordNode child : recordnode) {
            this.nodeAndChildrenSelectionToggle(child, newvalue);
        }
    }

    private void nodeExpandToggle(MouseEvent evt) {
        int col = ETreeTable.this.columnAtPoint(evt.getPoint());
        if (col != 0) {
            return;
        }
        int row = ETreeTable.this.rowAtPoint(evt.getPoint());
        ETreeTableRecordNode recordnode = this.getTreeTableModel().getRow(row);
        this.getTreeTableModel().toggle(recordnode);
    }

    private void nodeSelectionToggle(int row, ETreeTableRecordNode recordnode, boolean newvalue) {
        recordnode.setSelected(newvalue);
        this.getTreeTableModel().fireTableCellUpdated(row, 0);
    }

    private void nodeSelectionToggle(MouseEvent evt) {
        switch (this.checkMode) {
            case NONE:
                break;

            default: {
                int col = ETreeTable.this.columnAtPoint(evt.getPoint());
                if (col != 0) {
                    return;
                }
                int row = ETreeTable.this.rowAtPoint(evt.getPoint());
                ETreeTableRecordNode recordnode = this.getTreeTableModel().getRow(row);
                boolean newvalue = !recordnode.isSelected();

                switch (this.checkMode) {
                    case NODE:
                        // node only
                        this.nodeSelectionToggle(row, recordnode, newvalue);
                        break;
                    case NODE_AND_CHILDREN:
                        // also children
                        this.nodeAndChildrenSelectionToggle(recordnode, newvalue);
                        break;
                    case NODE_AND_PARENTS:
                        // also parents
                        while (recordnode != null) {
                            row = this.getTreeTableModel().getRowIndex(recordnode);
                            if (row == -1) {
                                break;
                            }
                            this.nodeSelectionToggle(row, recordnode, newvalue);
                            recordnode = recordnode.getParent();
                        }
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
            }
                break;
        }
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

    public void setCheckMode(CheckMode checkMode) {
        this.checkMode = checkMode;

        switch (checkMode) {
            case NONE:
                this.tree.setCellRenderer(new DefaultTreeCellRenderer());
                break;
            default:
                this.tree.setCellRenderer(new ETreeTableCheckBoxTreeNodeRenderer());
                break;
        }

        this.repaint();
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
     * @see javax.swing.JTable#setRowHeight(int)
     */
    @Override
    public void setRowHeight(int rowHeight) {
        super.setRowHeight(rowHeight);
        if (this.tree != null) {
            this.tree.setRowHeight(18);
        }
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public ETreeTable stsi() {
        return this.getSimpleThreadSafeInterface();
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public ETreeTable STSI() {
        return this.getSimpleThreadSafeInterface();
    }

    /**
     * 
     * @see javax.swing.JTable#prepareRenderer(javax.swing.table.TableCellRenderer, int, int)
     */
    public Component super_prepareRenderer(TableCellRenderer renderer, int row, int column) {
        return super.prepareRenderer(renderer, row, column);
    }
}

package org.swingeasy;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

/**
 * @author Jurgen
 */
public class ETreeTableCellRenderer extends JTree implements TableCellRenderer {
    private static final long serialVersionUID = -4228448827157089089L;

    protected int currentRow = 0;

    protected ETreeTable eTreeTable;

    public ETreeTableCellRenderer(ETreeTable eTreeTable, ETreeTableModel model) {
        super(model);
        this.eTreeTable = eTreeTable;
        this.setShowsRootHandles(true);
        this.setRootVisible(false);
        DefaultTreeCellRenderer treecellrenderer = new DefaultTreeCellRenderer() {
            private static final long serialVersionUID = -1637612854225410970L;

            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row,
                    boolean hasFocus1) {
                DefaultTreeCellRenderer treeCellRendererComponent = DefaultTreeCellRenderer.class.cast(super.getTreeCellRendererComponent(tree,
                        value, sel, expanded, leaf, row, hasFocus1));
                treeCellRendererComponent.setOpaque(true);
                return treeCellRendererComponent;
            }
        };
        this.setCellRenderer(treecellrenderer);
    }

    /**
     * 
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // sets row to paint
        this.currentRow = row;
        // sets correct background color depending if selected or not
        this.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        return this; // tree is painting
    }

    /**
     * 
     * @see javax.swing.JComponent#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics g) {
        // paints currentrow of tree to tablecell; rowheight has to be fixed
        g.translate(0, -this.currentRow * this.getRowHeight());
        super.paint(g);
    }

    /**
     * This is overridden to set the height to match that of the JTable.
     */
    @Override
    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, 0, w, this.eTreeTable.getHeight());
    }

    /**
     * Sets the row height of the tree, and forwards the row height to the table.
     */
    @Override
    public void setRowHeight(int rowHeight) {
        if (rowHeight > 0) {
            super.setRowHeight(rowHeight);
            if ((this.eTreeTable != null) && (this.eTreeTable.getRowHeight() != rowHeight)) {
                this.eTreeTable.setRowHeight(this.getRowHeight());
            }
        }
    }

    /**
     * updateUI is overridden to set the colors of the Tree's renderer to match that of the table.
     */
    @Override
    public void updateUI() {
        super.updateUI();
        // Make the tree's cell renderer use the table's cell selection
        // colors.
        TreeCellRenderer tcr = this.getCellRenderer();
        if (tcr instanceof DefaultTreeCellRenderer) {
            DefaultTreeCellRenderer dtcr = ((DefaultTreeCellRenderer) tcr);
            // For 1.1 uncomment this, 1.2 has a bug that will cause an
            // exception to be thrown if the border selection color is
            // null.
            // dtcr.setBorderSelectionColor(null);
            dtcr.setTextSelectionColor(UIManager.getColor("Table.selectionForeground"));
            dtcr.setBackgroundSelectionColor(UIManager.getColor("Table.selectionBackground"));
        }
    }
}

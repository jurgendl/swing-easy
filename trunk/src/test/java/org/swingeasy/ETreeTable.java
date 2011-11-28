package org.swingeasy;

import java.awt.Dimension;

import javax.swing.JTable;

/**
 * @author Jurgen
 */
public class ETreeTable extends JTable {
    private static final long serialVersionUID = -3695054922987210010L;

    protected ETreeTableModel model;

    protected ETreeTableCellRenderer tree;

    public ETreeTable(ETreeTableModel model) {
        super(model);
        this.model = model;
        this.tree = new ETreeTableCellRenderer(this, model);
        this.getColumn(this.getColumnName(0)).setCellRenderer(this.tree);
        this.setIntercellSpacing(new Dimension(0, 0));
        this.setRowHeight(18);
        this.tree.setRowHeight(18);
    }
}

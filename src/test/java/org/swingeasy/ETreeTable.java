package org.swingeasy;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.apache.commons.lang.StringUtils;

/**
 * @author Jurgen
 */
public class ETreeTable extends JTable {
    private static final long serialVersionUID = -3695054922987210010L;

    protected ETreeTableModel model;

    protected ETreeTableCellRenderer tree;

    public ETreeTable(final ETreeTableModel model) {
        super(model);
        this.model = model;
        this.model.parent = this;
        this.tree = new ETreeTableCellRenderer(this, model);
        this.getColumn(this.getColumnName(0)).setCellRenderer(this.tree);
        this.setIntercellSpacing(new Dimension(0, 0));
        this.setRowHeight(18);
        this.tree.setRowHeight(18);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if ((evt.getClickCount() == 1) && (evt.getButton() == MouseEvent.BUTTON1)) {
                    int col = ETreeTable.this.columnAtPoint(evt.getPoint());
                    if (col != 0) {
                        return;
                    }
                    int row = ETreeTable.this.rowAtPoint(evt.getPoint());
                    ETreeTableRecordNode recordnode = model.getRow(row);
                    model.toggle(recordnode);
                }
            }
        });
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
     * @see javax.swing.JTable#prepareRenderer(javax.swing.table.TableCellRenderer, int, int)
     */
    public Component super_prepareRenderer(TableCellRenderer renderer, int row, int column) {
        return super.prepareRenderer(renderer, row, column);
    }
}

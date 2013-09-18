package org.swingeasy;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * @author Jurgen
 */
public class ETreeTableTreeNodeRenderer extends DefaultTreeCellRenderer {
    private static final long serialVersionUID = -2054276646708826870L;

    public ETreeTableTreeNodeRenderer() {
        this.setOpaque(true);
        this.setBackgroundSelectionColor(Color.white);
        Border border = sun.swing.DefaultLookup.getBorder(this, this.ui, "Table.cellNoFocusBorder");
        System.out.println(border);
        this.setBorder(border);
    }

    /**
     * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int,
     *      boolean)
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean focused) {
        System.out.println(value);
        // ETreeTableRecordNode node = ETreeTableRecordNode.class.cast(value);
        DefaultTreeCellRenderer delegate = (DefaultTreeCellRenderer) super
                .getTreeCellRendererComponent(tree, null, sel, expanded, leaf, row, focused);
        delegate.setToolTipText(null);
        delegate.setText(null);
        // delegate.setSelected(node.isSelected());
        delegate.setEnabled(tree.isEnabled());
        delegate.setComponentOrientation(tree.getComponentOrientation());
        return delegate;
    }
}

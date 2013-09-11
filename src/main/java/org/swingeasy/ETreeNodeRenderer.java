package org.swingeasy;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * @author Jurgen
 */
public class ETreeNodeRenderer extends DefaultTreeCellRenderer {
    private static final long serialVersionUID = 157224120324383687L;

    /**
     * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int,
     *      boolean)
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean _hasFocus) {
        @SuppressWarnings("rawtypes")
        ETreeNode node = ETreeNode.class.cast(value);
        super.getTreeCellRendererComponent(tree, node == null ? null : node.getStringValue(), sel, expanded, leaf, row, _hasFocus);
        this.setToolTipText(node == null ? null : node.getTooltip());
        return this;
    }
}

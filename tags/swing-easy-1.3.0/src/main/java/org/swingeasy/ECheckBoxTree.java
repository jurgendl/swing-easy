package org.swingeasy;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * @author Jurgen
 */
public class ECheckBoxTree<T> extends JTree {
    private static final long serialVersionUID = 6378784816121886802L;

    public ECheckBoxTree(ECheckBoxTreeNode<T> root) {
        super(root);

        this.setCellRenderer(new ECheckBoxTreeNodeRenderer());

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TreePath path = ECheckBoxTree.this.getPathForLocation(e.getPoint().x, e.getPoint().y);
                if (path != null) {
                    @SuppressWarnings("unchecked")
                    ECheckBoxTreeNode<T> node = ECheckBoxTreeNode.class.cast(path.getLastPathComponent());
                    node.setSelected(!node.isSelected());
                    DefaultTreeModel.class.cast(ECheckBoxTree.this.getModel()).nodeChanged(node);
                }
            }
        });
        this.setEditable(false);

        ToolTipManager.sharedInstance().registerComponent(this);
    }
}

package org.swingeasy;

import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author Jurgen
 */
public class ETree<T> extends JTree implements ETreeI<T> {
    private static final long serialVersionUID = -2866936668266217327L;

    public ETree(ETreeNode<T> rootNode) {
        super(new DefaultTreeModel(rootNode));
        this.setShowsRootHandles(true);
        this.setRootVisible(true);
        this.setRowHeight(17);
        this.setCellRenderer(new ETreeNodeRenderer());
        ToolTipManager.sharedInstance().registerComponent(this);
    }

    /**
     * JDOC
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public ETreeI<T> getSimpleThreadSafeInterface() {
        return EventThreadSafeWrapper.getSimpleThreadSafeInterface(ETree.class, this, ETreeI.class);
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public ETreeI<T> stsi() {
        return this.getSimpleThreadSafeInterface();
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public ETreeI<T> STSI() {
        return this.getSimpleThreadSafeInterface();
    }
}

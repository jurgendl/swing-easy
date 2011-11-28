package org.swingeasy;

import javax.swing.JTree;
import javax.swing.ToolTipManager;

/**
 * @author Jurgen
 */
public class ETree<T> extends JTree implements ETreeI<T> {

    private static final long serialVersionUID = -2866936668266217327L;

    public ETree(ETreeNode<T> rootNode) {
        super(new javax.swing.tree.DefaultTreeModel(rootNode, true));
        this.setShowsRootHandles(true);
        this.setRootVisible(true);

        this.setCellRenderer(new ETreeNodeRenderer());
        ToolTipManager.sharedInstance().registerComponent(this);

        this.setEditable(true);
        this.setCellEditor(new ETreeNodeEditor());
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

package org.swingeasy;

import javax.swing.JTree;
import javax.swing.ToolTipManager;

/**
 * @author Jurgen
 */
public class ETree<T> extends JTree implements ETreeI<T> {
    private static final long serialVersionUID = -2866936668266217327L;

    protected ETree() {
        this(new ETreeNode<T>(null));
    }

    public ETree(ETreeConfig cfg, ETreeNode<T> rootNode) {
        super(new javax.swing.tree.DefaultTreeModel(rootNode, true));
        this.setShowsRootHandles(true);
        this.setRootVisible(true);

        this.setCellRenderer(new ETreeNodeRenderer());
        ToolTipManager.sharedInstance().registerComponent(this);

        this.setEditable(true);
        this.setCellEditor(new ETreeNodeEditor());
    }

    public ETree(ETreeNode<T> rootNode) {
        this(new ETreeConfig(), rootNode);
    }

    /**
     * JDOC
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public ETreeI<T> getSimpleThreadSafeInterface() {
        try {
            return EventThreadSafeWrapper.getSimpleThreadSafeInterface(ETree.class, this, ETreeI.class);
        } catch (Exception ex) {
            System.err.println(ex);
            return this; // no javassist
        }
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

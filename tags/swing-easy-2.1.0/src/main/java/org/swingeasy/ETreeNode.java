package org.swingeasy;

import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * @author Jurgen
 */
public class ETreeNode<T> extends DefaultMutableTreeNode {
    private static final long serialVersionUID = -437035560334777359L;

    public ETreeNode(T userObject) {
        super(userObject);
        this.children = null; // not initialized (lazy)
    }

    public ETreeNode(T userObject, Collection<ETreeNode<T>> children) {
        super(userObject);
        this.children = new Vector<ETreeNode<T>>(children); // initialized (eager)
    }

    /**
     * 
     * @see javax.swing.tree.DefaultMutableTreeNode#children()
     */
    @Override
    public Enumeration<ETreeNode<T>> children() {
        return this.getChildren().elements();
    }

    /**
     * 
     * @see javax.swing.tree.DefaultMutableTreeNode#getChildAt(int)
     */
    @Override
    public TreeNode getChildAt(int index) {
        return this.getChildren().elementAt(index);
    }

    /**
     * 
     * @see javax.swing.tree.DefaultMutableTreeNode#getChildCount()
     */
    @Override
    public int getChildCount() {
        return this.getChildren().size();
    }

    @SuppressWarnings("unchecked")
    public Vector<ETreeNode<T>> getChildren() {
        if (this.getAllowsChildren() && (this.children == null)) {
            this.children = new Vector<T>();
            this.initChildren(this.children);
        }

        return this.children;
    }

    public String getStringValue() {
        return String.valueOf(this.getUserObject());
    }

    public String getTooltip() {
        return this.getStringValue();
    }

    /**
     * called when children is not initialized an {@link #getAllowsChildren()} is true (default)
     * 
     * @param list
     */
    protected void initChildren(@SuppressWarnings("unused") List<ETreeNode<T>> list) {
        // override
    }

    public boolean isInitialized() {
        return this.children != null;
    }

    /**
     * 
     * @see javax.swing.tree.DefaultMutableTreeNode#isLeaf()
     */
    @Override
    public boolean isLeaf() {
        return !this.getAllowsChildren();
    }

    /**
     * 
     * @see javax.swing.tree.DefaultMutableTreeNode#removeAllChildren()
     */
    @Override
    public void removeAllChildren() {
        super.removeAllChildren();
        this.children = null;
    }

    /**
     * 
     * @see javax.swing.tree.DefaultMutableTreeNode#toString()
     */
    @Override
    public String toString() {
        return this.getUserObject().toString() + "=" + this.isInitialized(); //$NON-NLS-1$
    }
}

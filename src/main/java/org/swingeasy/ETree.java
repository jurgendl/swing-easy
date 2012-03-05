package org.swingeasy;

import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.TreePath;

import org.swingeasy.EComponentPopupMenu.ReadableComponent;

import ca.odell.glazedlists.matchers.Matcher;

/**
 * @author Jurgen
 */
public class ETree<T> extends JTree implements ETreeI<T>, ReadableComponent {
    private static final long serialVersionUID = -2866936668266217327L;

    protected ETreeSearchComponent<T> searchComponent = null;

    protected ETree() {
        this(new ETreeNode<T>(null));
    }

    public ETree(ETreeConfig cfg, ETreeNode<T> rootNode) {
        super(new javax.swing.tree.DefaultTreeModel(rootNode, true));

        cfg.lock();

        this.setShowsRootHandles(true);
        this.setRootVisible(true);

        this.setCellRenderer(new ETreeNodeRenderer());
        ToolTipManager.sharedInstance().registerComponent(this);

        this.setEditable(true);
        this.setCellEditor(new ETreeNodeEditor());

        UIUtils.registerLocaleChangeListener((EComponentI) this);

        if (cfg.isDefaultPopupMenu()) {
            EComponentPopupMenu.installPopupMenu(this);
        }
    }

    public ETree(ETreeNode<T> rootNode) {
        this(new ETreeConfig(), rootNode);
    }

    /**
     * 
     * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy()
     */
    @Override
    public void copy() {
        throw new UnsupportedOperationException("not implemented"); // TODO implement
    }

    /**
     * 
     * @see javax.swing.JTree#expandPath(javax.swing.tree.TreePath)
     */
    @Override
    public void expandPath(TreePath path) {
        super.expandPath(path);
    }

    /**
     * 
     * @see org.swingeasy.ETreeI#getNextMatch(TreePath, String)
     */
    @Override
    public TreePath getNextMatch(TreePath current, Matcher<T> matcher) {
        int startingRow = this.getRowForPath(current) + 1;
        int max = this.getRowCount();
        if ((startingRow < 0) || (startingRow >= max)) {
            throw new IllegalArgumentException();
        }
        int row = startingRow;
        do {
            TreePath path = this.getPathForRow(row);
            @SuppressWarnings("unchecked")
            ETreeNode<T> treeNode = (ETreeNode<T>) path.getLastPathComponent();
            @SuppressWarnings("unchecked")
            T t = (T) treeNode.getUserObject();
            if (matcher.matches(t)) {
                return path;
            }
            row = (row + 1 + max) % max;
        } while (row != startingRow);
        return null;
    }

    /**
     * 
     * @see org.swingeasy.HasParentComponent#getParentComponent()
     */
    @Override
    public JComponent getParentComponent() {
        return this;
    }

    /**
     * returns and creates if necessary {@link ETreeSearchComponent}
     */
    public ETreeSearchComponent<T> getSearchComponent() {
        if (this.searchComponent == null) {
            this.searchComponent = new ETreeSearchComponent<T>(this);
            this.searchComponent.setLocale(this.getLocale());
        }
        return this.searchComponent;
    }

    /**
     * 
     * @see org.swingeasy.ETreeI#getSelectedOrTopNodePath()
     */
    @Override
    public TreePath getSelectedOrTopNodePath() {
        try {
            return this.getSelectionPath();
        } catch (Exception ex) {
            return this.getTopNodePath();
        }
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
            System.err.println("javassist error"); //$NON-NLS-1$
            System.err.println(ex);
            return this; // no javassist
        }
    }

    /**
     * 
     * @see org.swingeasy.ETreeI#getTopNodePath()
     */
    @Override
    public TreePath getTopNodePath() {
        return new TreePath(this.getModel().getRoot());
    }

    /**
     * 
     * @see org.swingeasy.ETableI#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        this.repaint();
    }

    /**
     * 
     * @see javax.swing.JTree#setSelectionPath(javax.swing.tree.TreePath)
     */
    @Override
    public void setSelectionPath(TreePath path) {
        super.setSelectionPath(path);
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

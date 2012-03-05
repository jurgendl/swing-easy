package org.swingeasy;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;

import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * @author Jurgen
 */
public class ECheckBoxTree<T> extends JTree implements ECheckBoxTreeI<T> {
    private static final long serialVersionUID = 6378784816121886802L;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected ECheckBoxTree() {
        super(new ECheckBoxTreeNode("root")); //$NON-NLS-1$
    }

    public ECheckBoxTree(ECheckBoxTreeConfig cfg, ECheckBoxTreeNode<T> root) {
        super(root);

        cfg.lock();

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

        UIUtils.registerLocaleChangeListener(this);
    }

    public ECheckBoxTree(ECheckBoxTreeNode<T> root) {
        this(new ECheckBoxTreeConfig(), root);
    }

    /**
     * JDOC
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public ECheckBoxTreeI<T> getSimpleThreadSafeInterface() {
        try {
            return EventThreadSafeWrapper.getSimpleThreadSafeInterface(ECheckBoxTree.class, this, ECheckBoxTreeI.class);
        } catch (Exception ex) {
            System.err.println(ex);
            return this; // no javassist
        }
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
     * @see #getSimpleThreadSafeInterface()
     */
    public ECheckBoxTreeI<T> stsi() {
        return this.getSimpleThreadSafeInterface();
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public ECheckBoxTreeI<T> STSI() {
        return this.getSimpleThreadSafeInterface();
    }
}
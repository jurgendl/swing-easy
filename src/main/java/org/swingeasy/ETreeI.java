package org.swingeasy;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import ca.odell.glazedlists.matchers.Matcher;

/**
 * @author Jurgen
 */
public interface ETreeI<T> {
    /**
     * @see JTree#expandPath(TreePath)
     */
    public abstract void expandPath(TreePath nextMatch);

    /**
     * JDOC
     * 
     * @param current
     * @param matcher
     * @return
     */
    public abstract TreePath getNextMatch(TreePath current, Matcher<T> matcher);

    /**
     * @see JTree#setSelectionPath(TreePath)
     */
    public abstract void setSelectionPath(TreePath nextMatch);
}

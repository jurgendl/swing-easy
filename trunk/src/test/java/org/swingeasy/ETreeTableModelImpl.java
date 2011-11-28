package org.swingeasy;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.TableModel;
import javax.swing.tree.TreePath;

public class ETreeTableModelImpl implements ETreeTableModel {
    protected ETreeTableRecordNode root;

    protected ETreeTableHeaders headers;

    protected List<ETreeTableRecordNode> rows = new ArrayList<ETreeTableRecordNode>();

    protected EventListenerList listenerList = new EventListenerList();

    public ETreeTableModelImpl(ETreeTableRecordNode root, ETreeTableHeaders headers) {
        this.root = root;
        this.headers = headers;
        this.rows.addAll(root.getChildren());
    }

    /**
     * 
     * @see javax.swing.table.TableModel#addTableModelListener(javax.swing.event.TableModelListener)
     */
    @Override
    public void addTableModelListener(TableModelListener l) {
        this.listenerList.add(TableModelListener.class, l);
    }

    /**
     * 
     * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
     */
    @Override
    public void addTreeModelListener(TreeModelListener l) {
        this.listenerList.add(TreeModelListener.class, l);
    }

    protected void fireTableChanged(TableModel source, int firstRow, int lastRow, int column, int type) {
        for (TableModelListener tableModelListener : this.listenerList.getListeners(TableModelListener.class)) {
            tableModelListener.tableChanged(new TableModelEvent(source, firstRow, lastRow, column, type));
        }
    }

    protected void fireTreeNodesChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
        for (TreeModelListener treeModelListener : this.listenerList.getListeners(TreeModelListener.class)) {
            treeModelListener.treeNodesChanged(new TreeModelEvent(source, path, childIndices, children));
        }
    }

    protected void fireTreeNodesInserted(Object source, Object[] path, int[] childIndices, Object[] children) {
        for (TreeModelListener treeModelListener : this.listenerList.getListeners(TreeModelListener.class)) {
            treeModelListener.treeNodesInserted(new TreeModelEvent(source, path, childIndices, children));
        }
    }

    protected void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices, Object[] children) {
        for (TreeModelListener treeModelListener : this.listenerList.getListeners(TreeModelListener.class)) {
            treeModelListener.treeNodesRemoved(new TreeModelEvent(source, path, childIndices, children));
        }
    }

    protected void fireTreeStructureChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
        for (TreeModelListener treeModelListener : this.listenerList.getListeners(TreeModelListener.class)) {
            treeModelListener.treeStructureChanged(new TreeModelEvent(source, path, childIndices, children));
        }
    }

    /**
     * 
     * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
     */
    @Override
    public Object getChild(Object parent, int index) {
        return ETreeTableRecordNode.class.cast(parent).getChildren(index);
    }

    /**
     * 
     * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
     */
    @Override
    public int getChildCount(Object parent) {
        return ETreeTableRecordNode.class.cast(parent).getChildCount();
    }

    /**
     * 
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return this.headers.getColumnClass(columnIndex);
    }

    /**
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return this.headers.getColumnCount();
    }

    /**
     * 
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int columnIndex) {
        return this.headers.getColumnName(columnIndex);
    }

    /**
     * 
     * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
     */
    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return ETreeTableRecordNode.class.cast(parent).getIndexOfChild(child);
    }

    /**
     * 
     * @see javax.swing.tree.TreeModel#getRoot()
     */
    @Override
    public Object getRoot() {
        return this.root;
    }

    /**
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return this.rows.size();
    }

    /**
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.rows.get(rowIndex).getValue(columnIndex);
    }

    /**
     * 
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    /**
     * 
     * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
     */
    @Override
    public boolean isLeaf(Object node) {
        return false;
    }

    /**
     * 
     * @see javax.swing.table.TableModel#removeTableModelListener(javax.swing.event.TableModelListener)
     */
    @Override
    public void removeTableModelListener(TableModelListener l) {
        this.listenerList.remove(TableModelListener.class, l);
    }

    /**
     * 
     * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
     */
    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        this.listenerList.remove(TreeModelListener.class, l);
    }

    /**
     * 
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        this.rows.get(rowIndex).setValue(columnIndex, aValue);
    }

    /**
     * 
     * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        ETreeTableRecordNode.class.cast(path.getLastPathComponent()).setValue(0, newValue);
    }
}

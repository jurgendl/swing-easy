package org.swingeasy;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * @author Jurgen
 */
public class ETreeTableModel implements TableModel, TreeModel {
    protected ETreeTableRecordNode root;

    protected ETreeTableHeaders headers;

    protected List<ETreeTableRecordNode> rows = new ArrayList<ETreeTableRecordNode>();

    protected EventListenerList listenerList = new EventListenerList();

    protected ETreeTable parent;

    public ETreeTableModel(ETreeTableRecordNode root, ETreeTableHeaders headers) {
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

    /**
     * carbon copy of javax.swing.table.AbstractTableModel
     */
    protected void fireTableCellUpdated(int rowIndex, int columnIndex) {
        this.fireTableChanged(new TableModelEvent(this, rowIndex, rowIndex, columnIndex));
    }

    protected void fireTableChanged(TableModelEvent e) {
        for (TableModelListener tableModelListener : this.listenerList.getListeners(TableModelListener.class)) {
            tableModelListener.tableChanged(e);
        }
    }

    /**
     * carbon copy of javax.swing.table.AbstractTableModel
     */
    protected void fireTableDataChanged() {
        this.fireTableChanged(new TableModelEvent(this));
    }

    /**
     * carbon copy of javax.swing.table.AbstractTableModel
     */
    protected void fireTableRowsDeleted(int firstRow, int lastRow) {
        this.fireTableChanged(new TableModelEvent(this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
    }

    /**
     * carbon copy of javax.swing.table.AbstractTableModel
     */
    protected void fireTableRowsInserted(int firstRow, int lastRow) {
        this.fireTableChanged(new TableModelEvent(this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
    }

    /**
     * carbon copy of javax.swing.table.AbstractTableModel
     */
    protected void fireTableRowsUpdated(int firstRow, int lastRow) {
        this.fireTableChanged(new TableModelEvent(this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE));
    }

    /**
     * carbon copy of javax.swing.table.AbstractTableModel
     */
    protected void fireTableStructureChanged() {
        this.fireTableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));
    }

    protected void fireTreeNodesChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
        TreeModelEvent e = new TreeModelEvent(source, path, childIndices, children);
        for (TreeModelListener treeModelListener : this.listenerList.getListeners(TreeModelListener.class)) {
            treeModelListener.treeNodesChanged(e);
        }
    }

    protected void fireTreeNodesInserted(Object source, Object[] path, int[] childIndices, Object[] children) {
        TreeModelEvent e = new TreeModelEvent(source, path, childIndices, children);
        for (TreeModelListener treeModelListener : this.listenerList.getListeners(TreeModelListener.class)) {
            treeModelListener.treeNodesInserted(e);
        }
    }

    protected void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices, Object[] children) {
        TreeModelEvent e = new TreeModelEvent(source, path, childIndices, children);
        for (TreeModelListener treeModelListener : this.listenerList.getListeners(TreeModelListener.class)) {
            treeModelListener.treeNodesRemoved(e);
        }
    }

    protected void fireTreeStructureChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
        TreeModelEvent e = new TreeModelEvent(source, path, childIndices, children);
        for (TreeModelListener treeModelListener : this.listenerList.getListeners(TreeModelListener.class)) {
            treeModelListener.treeStructureChanged(e);
        }
    }

    /**
     * 
     * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
     */
    @Override
    public Object getChild(Object _parent, int index) {
        return ETreeTableRecordNode.class.cast(_parent).getChildren(index);
    }

    /**
     * 
     * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
     */
    @Override
    public int getChildCount(Object _parent) {
        return ETreeTableRecordNode.class.cast(_parent).getChildCount();
    }

    /**
     * 
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return ETreeTableRecordNode.class;
        }
        return this.headers.getColumnClass(columnIndex - 1);
    }

    /**
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return this.headers.getColumnCount() + 1;
    }

    /**
     * 
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return ""; //$NON-NLS-1$
        }
        return this.headers.getColumnName(columnIndex - 1);
    }

    /**
     * 
     * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
     */
    @Override
    public int getIndexOfChild(Object _parent, Object child) {
        return ETreeTableRecordNode.class.cast(_parent).getIndexOfChild(ETreeTableRecordNode.class.cast(child));
    }

    /**
     * 
     * @see javax.swing.tree.TreeModel#getRoot()
     */
    @Override
    public Object getRoot() {
        return this.root;
    }

    public ETreeTableRecordNode getRow(int rowIndex) {
        return this.rows.get(rowIndex);
    }

    /**
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return this.rows.size();
    }

    public int getRowIndex(ETreeTableRecordNode node) {
        return this.rows.indexOf(node);
    }

    /**
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return this.rows.get(rowIndex);
        }
        return this.rows.get(rowIndex).getValue(columnIndex - 1);
    }

    /**
     * 
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return true;
        }
        return this.headers.isColumnEditable(columnIndex - 1);
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
        if (columnIndex == 0) {
            return;
        }
        this.rows.get(rowIndex).setValue(columnIndex - 1, aValue);
    }

    public void toggle(ETreeTableRecordNode recordnode) {
        int row = this.rows.indexOf(recordnode);
        boolean expanded = this.parent.tree.isExpanded(row);
        if (expanded) {
            this.parent.tree.collapseRow(row);
            int count = 0;
            for (ETreeTableRecordNode child : recordnode) {
                this.rows.remove(child);
                count++;
            }
            this.fireTableRowsDeleted(row + 1, row + count);
        } else {
            this.parent.tree.expandRow(row);
            int index = 0;
            for (ETreeTableRecordNode child : recordnode) {
                this.rows.add(row + index + 1, child);
                index++;
            }
            this.fireTableRowsInserted(row + 1, row + index);
        }
        this.fireTableCellUpdated(row, 0);
    }

    /**
     * 
     * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        throw new UnsupportedOperationException();
    }
}

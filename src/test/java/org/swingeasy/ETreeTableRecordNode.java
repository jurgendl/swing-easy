package org.swingeasy;

import java.util.ArrayList;
import java.util.List;

public class ETreeTableRecordNode {
    private List<ETreeTableRecordNode> children = new ArrayList<ETreeTableRecordNode>();

    private List<Object> values = new ArrayList<Object>();

    public ETreeTableRecordNode() {
        super();
    }

    public ETreeTableRecordNode(List<Object> values) {
        this.values = values;
    }

    public ETreeTableRecordNode(List<Object> values, List<ETreeTableRecordNode> children) {
        super();
        this.values = values;
        this.children = children;
    }

    public void add(ETreeTableRecordNode child) {
        this.children.add(child);
    }

    public int getChildCount() {
        return this.children.size();
    }

    public List<ETreeTableRecordNode> getChildren() {
        return this.children;
    }

    public ETreeTableRecordNode getChildren(int index) {
        return this.children.get(index);
    }

    public int getIndexOfChild(Object child) {
        return this.children.indexOf(child);
    }

    public Object getValue(int columnIndex) {
        return this.values.get(columnIndex);
    }

    public List<Object> getValues() {
        return this.values;
    }

    public void setChildren(List<ETreeTableRecordNode> children) {
        this.children = children;
    }

    public void setValue(int columnIndex, Object aValue) {
        this.values.set(columnIndex, aValue);
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.values.isEmpty() ? null : String.valueOf(this.values.get(0));
    }
}

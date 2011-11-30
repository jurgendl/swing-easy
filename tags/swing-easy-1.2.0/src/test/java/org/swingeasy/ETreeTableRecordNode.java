package org.swingeasy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Jurgen
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ETreeTableRecordNode implements Iterable<ETreeTableRecordNode> {
    protected ETreeTableRecordNode parent = null;

    protected final List<ETreeTableRecordNode> children = new ArrayList<ETreeTableRecordNode>();

    protected final List values = new ArrayList();

    public ETreeTableRecordNode() {
        this.setValues(Collections.singletonList("ROOT"));
    }

    public ETreeTableRecordNode(List values) {
        this.setValues(values);
    }

    public ETreeTableRecordNode(List values, List<ETreeTableRecordNode> children) {
        this.setValues(values);
        this.addAll(children);
    }

    // write access
    public void add(ETreeTableRecordNode child) {
        if (child.parent != null) {
            throw new IllegalArgumentException();
        }
        this.children.add(child);
        child.parent = this;
    }

    // write access
    public void addAll(Collection<ETreeTableRecordNode> _children) {
        for (ETreeTableRecordNode child : _children) {
            this.add(child);
        }
    }

    public int getChildCount() {
        return this.getChildren().size();
    }

    // init
    public List<ETreeTableRecordNode> getChildren() {
        return Collections.unmodifiableList(this.children);
    }

    public ETreeTableRecordNode getChildren(int index) {
        return this.getChildren().get(index);
    }

    public int getIndexOfChild(ETreeTableRecordNode child) {
        return this.getChildren().indexOf(child);
    }

    public Object getValue(int columnIndex) {
        return this.getValues().get(columnIndex);
    }

    // init
    public List getValues() {
        return Collections.unmodifiableList(this.values);
    }

    /**
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<ETreeTableRecordNode> iterator() {
        return this.getChildren().iterator();
    }

    // write access
    public void remove(ETreeTableRecordNode child) {
        if (child.parent != this) {
            throw new IllegalArgumentException();
        }
        this.children.remove(child);
        child.parent = null;
    }

    // write access
    public void setValue(int columnIndex, Object aValue) {
        this.values.set(columnIndex, aValue);
    }

    // write access
    public void setValues(Collection values) {
        this.values.clear();
        this.values.addAll(values);
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.getValues().toString();
    }
}

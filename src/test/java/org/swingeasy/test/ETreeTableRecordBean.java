package org.swingeasy.test;

import java.util.List;

import org.swingeasy.ETableRecordBean;

/**
 * @author Jurgen
 */
public class ETreeTableRecordBean<T> extends ETableRecordBean<T> implements ETreeTableRecord<T> {
    protected ETreeTableRecord<T> parent = null;

    public ETreeTableRecordBean(List<String> orderedFields, T o) {
        super(orderedFields, o);
    }

    public ETreeTableRecordBean(T o, String... orderedFields) {
        super(o, orderedFields);
    }

    /**
     * @see org.swingeasy.test.ETreeTableRecord#getParent()
     */
    @Override
    public ETreeTableRecord<T> getParent() {
        return this.parent;
    }

    /**
     * @see org.swingeasy.test.ETreeTableRecord#setParent(org.swingeasy.test.ETreeTableRecord)
     */
    @Override
    public void setParent(ETreeTableRecord<T> parent) {
        this.parent = parent;
    }
}

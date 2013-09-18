package org.swingeasy.test;

import org.swingeasy.ETableRecordArray;

/**
 * @author Jurgen
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ETreeTableRecordArray extends ETableRecordArray implements ETreeTableRecord {
    protected ETreeTableRecord parent = null;

    /**
     * @see org.swingeasy.test.ETreeTableRecord#getParent()
     */
    @Override
    public ETreeTableRecord getParent() {
        return this.parent;
    }

    /**
     * @see org.swingeasy.test.ETreeTableRecord#setParent(org.swingeasy.test.ETreeTableRecord)
     */
    @Override
    public void setParent(ETreeTableRecord parent) {
        this.parent = parent;
    }
}

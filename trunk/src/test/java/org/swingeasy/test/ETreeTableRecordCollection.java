package org.swingeasy.test;

import java.util.Collection;
import java.util.List;

import org.swingeasy.ETableRecordCollection;

/**
 * @author Jurgen
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ETreeTableRecordCollection extends ETableRecordCollection implements ETreeTableRecord {
    protected ETreeTableRecord parent = null;

    public ETreeTableRecordCollection() {
        super();
    }

    public ETreeTableRecordCollection(Collection o) {
        super(o);
    }

    public ETreeTableRecordCollection(List o) {
        super(o);
    }

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

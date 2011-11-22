package org.swingeasy;

import javax.swing.JTree;

/**
 * @author Jurgen
 */
public class ETree extends JTree implements ETreeI {
    private static final long serialVersionUID = -2866936668266217327L;

    public ETree() {
        super();
    }

    /**
     * JDOC
     * 
     * @return
     */
    public ETreeI getSimpleThreadSafeInterface() {
        return EventThreadSafeWrapper.getSimpleThreadSafeInterface(ETree.class, this, ETreeI.class);
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public ETreeI stsi() {
        return this.getSimpleThreadSafeInterface();
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public ETreeI STSI() {
        return this.getSimpleThreadSafeInterface();
    }
}

package org.swingeasy;

/**
 * @see http://docs.oracle.com/javase/tutorial/uiswing/components/spinner.html
 * 
 * @author Jurgen
 */
public interface CyclingSpinnerListModelListener {
    public abstract void overflow();

    public abstract void rollback();
}
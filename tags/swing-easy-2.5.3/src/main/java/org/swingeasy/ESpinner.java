package org.swingeasy;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

/**
 * @author Jurgen
 */
public class ESpinner<T> extends JSpinner {
    private static final long serialVersionUID = -5205530967336536976L;

    public ESpinner(SpinnerModel model) {
        super(model);

        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    ESpinner.this.gotoNextValue();
                } else {
                    ESpinner.this.gotoPreviousValue();
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    public T get() {
        return (T) this.getValue();
    }

    public T gotoNextValue() {
        @SuppressWarnings("unchecked")
        T nextValue = (T) ESpinner.this.getNextValue();
        ESpinner.this.getModel().setValue(nextValue);
        return nextValue;
    }

    public T gotoPreviousValue() {
        @SuppressWarnings("unchecked")
        T nextValue = (T) ESpinner.this.getPreviousValue();
        ESpinner.this.getModel().setValue(nextValue);
        return nextValue;
    }
}

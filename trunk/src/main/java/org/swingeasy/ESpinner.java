package org.swingeasy;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.ToolTipManager;

/**
 * @author Jurgen
 */
public class ESpinner<T> extends JSpinner {
    private static final long serialVersionUID = -5205530967336536976L;

    public ESpinner(SpinnerModel model) {
        super(model);
        this.init();
    }

    @SuppressWarnings("unchecked")
    public T get() {
        return (T) this.getValue();
    }

    /**
     * 
     * @see javax.swing.JComponent#getToolTipText()
     */
    @Override
    public String getToolTipText() {
        String toolTipText = super.getToolTipText();
        if (toolTipText == null) {
            // FIXME
            // Object value = this.getValue();
            // if (value == null) {
            // return null;
            // }
            // String text = String.valueOf(value);
            // if (text.trim().length() == 0) {
            // text = null;
            // }
            // return text;
        }
        return toolTipText;
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

    protected void init() {
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

        ToolTipManager.sharedInstance().registerComponent(this);
    }
}

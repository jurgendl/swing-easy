package org.swingeasy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.ToolTipManager;

/**
 * @author Jurgen
 */
public class ECheckBox extends JCheckBox implements EComponentI, HasValue<Boolean> {
    private static final long serialVersionUID = -7050606626337213461L;

    protected final List<ValueChangeListener<Boolean>> valueChangeListeners = new ArrayList<ValueChangeListener<Boolean>>();

    public ECheckBox() {
        this.init();
    }

    public ECheckBox(Action a) {
        super(a);
        this.init();
    }

    public ECheckBox(Icon icon) {
        super(icon);
        this.init();
    }

    public ECheckBox(Icon icon, boolean selected) {
        super(icon, selected);
        this.init();
    }

    public ECheckBox(String text) {
        super(text);
        this.init();
    }

    public ECheckBox(String text, boolean selected) {
        super(text, selected);
        this.init();
    }

    public ECheckBox(String text, Icon icon) {
        super(text, icon);
        this.init();
    }

    public ECheckBox(String text, Icon icon, boolean selected) {
        super(text, icon, selected);
        this.init();
    }

    /**
     * 
     * @see org.swingeasy.HasValue#addValueChangeListener(org.swingeasy.ValueChangeListener)
     */
    @Override
    public void addValueChangeListener(ValueChangeListener<Boolean> listener) {
        this.valueChangeListeners.add(listener);
    }

    /**
     * 
     * @see org.swingeasy.HasValue#clearValueChangeListeners()
     */
    @Override
    public void clearValueChangeListeners() {
        this.valueChangeListeners.clear();
    }

    /**
     * 
     * @see javax.swing.JComponent#getToolTipText()
     */
    @Override
    public String getToolTipText() {
        String toolTipText = super.getToolTipText();
        if (toolTipText == null) {
            String text = this.getText();
            if (text.trim().length() == 0) {
                text = null;
            }
            return text;
        }
        return toolTipText;
    }

    /**
     * 
     * @see org.swingeasy.HasValue#getValue()
     */
    @Override
    public Boolean getValue() {
        return this.isSelected();
    }

    protected void init() {
        ToolTipManager.sharedInstance().registerComponent(this);

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Boolean value = ECheckBox.this.getValue();
                for (ValueChangeListener<Boolean> valueChangeListener : ECheckBox.this.valueChangeListeners) {
                    valueChangeListener.valueChanged(value);
                }
            }
        });
    }

    /**
     * 
     * @see org.swingeasy.HasValue#removeValueChangeListener(org.swingeasy.ValueChangeListener)
     */
    @Override
    public void removeValueChangeListener(ValueChangeListener<Boolean> listener) {
        this.valueChangeListeners.remove(listener);
    }
}

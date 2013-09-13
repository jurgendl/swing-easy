package org.swingeasy;

import javax.swing.Icon;

/**
 * @author Jurgen
 */
public class ECheckBoxConfig extends EComponentConfig<ECheckBoxConfig> {
    protected boolean selected = true;

    protected Icon icon = null;

    protected String text = null;

    public Icon getIcon() {
        return this.icon;
    }

    public String getText() {
        return this.text;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setText(String text) {
        this.text = text;
    }
}

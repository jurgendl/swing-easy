package org.swingeasy;

import javax.swing.Action;
import javax.swing.Icon;

/**
 * @author Jurgen
 */
public class EToggleToolBarButtonConfig extends EComponentConfig<EToggleToolBarButtonConfig> {
    protected EToolBarButtonCustomizer buttonCustomizer;

    protected Action action;

    protected Icon icon;

    public EToggleToolBarButtonConfig() {
        super();
    }

    public EToggleToolBarButtonConfig(Action a) {
        this.action = a;
    }

    public EToggleToolBarButtonConfig(EToolBarButtonCustomizer ebc) {
        this.buttonCustomizer = ebc;
    }

    public EToggleToolBarButtonConfig(EToolBarButtonCustomizer ebc, Action a) {
        this.buttonCustomizer = ebc;
        this.action = a;
    }

    public EToggleToolBarButtonConfig(EToolBarButtonCustomizer ebc, Icon icon) {
        this.buttonCustomizer = ebc;
        this.icon = icon;
    }

    public EToggleToolBarButtonConfig(Icon icon) {
        this.icon = icon;
    }

    public Action getAction() {
        return this.action;
    }

    public EButtonCustomizer getButtonCustomizer() {
        return this.buttonCustomizer;
    }

    public Icon getIcon() {
        return this.icon;
    }

    public EToggleToolBarButtonConfig setAction(Action action) {
        this.lockCheck();
        this.action = action;
        return this;
    }

    public EToggleToolBarButtonConfig setButtonCustomizer(EToolBarButtonCustomizer buttonCustomizer) {
        this.lockCheck();
        this.buttonCustomizer = buttonCustomizer;
        return this;
    }

    public EToggleToolBarButtonConfig setIcon(Icon icon) {
        this.lockCheck();
        this.icon = icon;
        return this;
    }
}

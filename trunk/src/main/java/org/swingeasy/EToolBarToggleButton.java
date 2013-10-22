package org.swingeasy;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.ToolTipManager;

import org.swingeasy.EComponentPopupMenu.ReadableComponent;

/**
 * @author Jurgen
 */
public class EToolBarToggleButton extends JToggleButton implements EComponentI, ReadableComponent {
    private static final long serialVersionUID = -6193067407274776197L;

    protected EButtonConfig cfg;

    protected EToolBarToggleButton() {
        this.cfg = null;
    }

    public EToolBarToggleButton(EToolBarButtonConfig cfg) {
        this.init(cfg = cfg.lock());
    }

    /**
     * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy(java.awt.event.ActionEvent)
     */
    @Override
    public void copy(ActionEvent e) {
        // TODO
        System.err.println("not implemented");
    }

    /**
     * @see org.swingeasy.HasParentComponent#getParentComponent()
     */
    @Override
    public JComponent getParentComponent() {
        return this;
    }

    protected void init(EToolBarButtonConfig config) {
        if (config.getAction() != null) {
            this.setAction(config.getAction());
            this.setName(String.valueOf(config.getAction().getValue(Action.NAME)));
        }
        if (config.getIcon() != null) {
            this.setIcon(config.getIcon());
        }
        if (config.getButtonCustomizer() != null) {
            config.getButtonCustomizer().customize(this);
        }
        if (config.isDefaultPopupMenu()) {
            this.installPopupMenuAction(EComponentPopupMenu.installPopupMenu(this));
        }
        UIUtils.registerLocaleChangeListener((EComponentI) this);
        if (config.isTooltips()) {
            ToolTipManager.sharedInstance().registerComponent(this);
        }
    }

    protected void installPopupMenuAction(@SuppressWarnings("unused") EComponentPopupMenu menu) {
        //
    }
}

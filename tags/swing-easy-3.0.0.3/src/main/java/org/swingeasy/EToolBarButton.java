package org.swingeasy;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.ToolTipManager;

import org.apache.commons.lang.StringUtils;
import org.swingeasy.EComponentPopupMenu.ReadableComponent;

/**
 * @author Jurgen
 */
public class EToolBarButton extends JButton implements EComponentI, ReadableComponent {
    private static final long serialVersionUID = -6193067407274776197L;

    protected EToolBarButtonConfig cfg;

    protected EToolBarButton() {
        this.cfg = null;
    }

    public EToolBarButton(EToolBarButtonConfig cfg) {
        this.init(cfg = cfg.lock());
    }

    /**
     * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy(java.awt.event.ActionEvent)
     */
    @Override
    public void copy(ActionEvent e) {
        String text = this.getText();
        EComponentPopupMenu.copyToClipboard(StringUtils.isNotBlank(text) ? text : this.getToolTipText());
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

        if (config.isLocalized()) {
            UIUtils.registerLocaleChangeListener((EComponentI) this);
        }

        if (config.isTooltips()) {
            ToolTipManager.sharedInstance().registerComponent(this);
        }
    }

    protected void installPopupMenuAction(@SuppressWarnings("unused") EComponentPopupMenu menu) {
        //
    }
}

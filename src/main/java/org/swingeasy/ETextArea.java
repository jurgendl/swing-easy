package org.swingeasy;

import javax.swing.JTextArea;

/**
 * @author Jurgen
 */
public class ETextArea extends JTextArea implements EComponentI {
    private static final long serialVersionUID = -854993140855661563L;

    public ETextArea(ETextAreaConfig cfg) {
        super(cfg.getRows(), cfg.getColumns());
        this.setEnabled(cfg.isEnabled());
        this.init();
        cfg.lock();
    }

    public ETextArea(ETextAreaConfig cfg, String text) {
        super(text, cfg.getRows(), cfg.getColumns());
        this.setEnabled(cfg.isEnabled());
        this.init();
        cfg.lock();
    }

    protected void init() {
        EComponentPopupMenu.installTextComponentPopupMenu(this);
        UIUtils.registerLocaleChangeListener(this);
    }
}

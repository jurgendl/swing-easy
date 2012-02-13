package org.swingeasy;

import java.awt.Component;
import java.awt.dnd.DropTargetListener;
import java.util.TooManyListenersException;

import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.TransferHandler;

import org.swingeasy.tab.DnDTabbedPane;
import org.swingeasy.tab.DnDTabbedPaneDropTargetListener;
import org.swingeasy.tab.TabTransferHandler;

/**
 * @author Jurgen
 */
public class ETabbedPane extends DnDTabbedPane {
    /** serialVersionUID */
    private static final long serialVersionUID = -46108541490198511L;

    protected ETabbedPaneConfig config;

    protected static DropTargetListener DROP_TARGET_LISTENER = new DnDTabbedPaneDropTargetListener();

    protected static TransferHandler TRANSFER_HANDLER = new TabTransferHandler<ETabbedPane>(ETabbedPane.class);

    public ETabbedPane() {
        this(new ETabbedPaneConfig());
    }

    public ETabbedPane(ETabbedPaneConfig cfg) {
        this.config = cfg.lock();
        this.register();
    }

    /**
     * 
     * @see javax.swing.JTabbedPane#insertTab(java.lang.String, javax.swing.Icon, java.awt.Component, java.lang.String, int)
     */
    @Override
    public void insertTab(String title, Icon icon, Component component, String tip, int index) {
        super.insertTab(title, icon, component, tip == null ? title : tip, index);
        this.setTabComponentAt(index, new ETabbedPaneHeader(title, icon, this.config));
    }

    protected void register() {
        this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        this.setTransferHandler(ETabbedPane.TRANSFER_HANDLER);
        try {
            this.getDropTarget().addDropTargetListener(ETabbedPane.DROP_TARGET_LISTENER);
        } catch (TooManyListenersException ex) {
            throw new RuntimeException(ex);
        }
    }

}

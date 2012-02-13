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

    private Rotation rotation = Rotation.DEFAULT;

    protected static DropTargetListener dtl = new DnDTabbedPaneDropTargetListener();

    protected static TransferHandler handler = new TabTransferHandler<ETabbedPane>(ETabbedPane.class);

    public ETabbedPane() {
        this.register();
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    /**
     * 
     * @see javax.swing.JTabbedPane#insertTab(java.lang.String, javax.swing.Icon, java.awt.Component, java.lang.String, int)
     */
    @Override
    public void insertTab(String title, Icon icon, Component component, String tip, int index) {
        if (icon instanceof RotatedLabel) {
            RotatedLabel rl = RotatedLabel.class.cast(icon);
            if (this.rotation == Rotation.DEFAULT) {
                super.insertTab(rl.getText(), rl.getIcon(), component, tip == null ? title : tip, index);
            } else {
                rl.setClockwise(this.rotation == Rotation.CLOCKWISE);
                super.insertTab(title, icon, component, tip == null ? title : tip, index);
            }
        } else if (this.rotation != Rotation.DEFAULT) {
            super.insertTab(null, new RotatedLabel(title, icon, this.rotation == Rotation.CLOCKWISE), component, tip == null ? title : tip, index);
        } else {
            super.insertTab(title, icon, component, tip == null ? title : tip, index);
        }
    }

    protected void register() {
        this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        this.setTransferHandler(ETabbedPane.handler);
        try {
            this.getDropTarget().addDropTargetListener(ETabbedPane.dtl);
        } catch (TooManyListenersException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }
}

package org.swingeasy;

import java.awt.Event;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 * @see http://java-swing-tips.blogspot.com/2010/11/jtable-celleditor-popupmenu.html
 * @author jdlandsh
 */
public class EComponentPopupMenu {
    public static class AncestorAdapter implements AncestorListener {
        @Override
        public void ancestorAdded(AncestorEvent event) {
            //
        }

        @Override
        public void ancestorMoved(AncestorEvent event) {
            //
        }

        @Override
        public void ancestorRemoved(AncestorEvent event) {
            //
        }
    }

    public static class ClipboardOwnerAdapter implements ClipboardOwner {
        @Override
        public void lostOwnership(Clipboard clipboard, Transferable contents) {
            //
        }
    }

    private static class CopyAction extends AbstractAction {
        private static final long serialVersionUID = 3044725124645042202L;

        private final ReadableComponent component;

        public CopyAction(ReadableComponent component) {
            super(EComponentPopupMenu.COPY, Resources.getImageResource("page_copy.png"));
            this.component = component;
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.component.copy();
        }
    }

    private static class CutAction extends AbstractAction {
        private static final long serialVersionUID = 4328082010034890480L;

        private final WritableComponent component;

        public CutAction(WritableComponent component) {
            super(EComponentPopupMenu.CUT, Resources.getImageResource("cut.png"));
            this.component = component;
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.component.cut();
        }
    }

    private static class DeleteAction extends AbstractAction {
        private static final long serialVersionUID = -7609111337852520512L;

        private final WritableComponent component;

        public DeleteAction(WritableComponent component) {
            super(EComponentPopupMenu.DELETE, Resources.getImageResource("bin_closed.png"));
            this.component = component;
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.component.delete();
        }
    }

    private static class PasteAction extends AbstractAction {
        private static final long serialVersionUID = -7609111337852520512L;

        private final WritableComponent component;

        public PasteAction(WritableComponent component) {
            super(EComponentPopupMenu.PASTE, Resources.getImageResource("page_paste.png"));
            this.component = component;
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.component.paste();
        }
    }

    public static class PopupMenuAdapter implements PopupMenuListener {
        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
            //
        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            //
        }

        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            //
        }
    }

    public static interface ReadableComponent {
        public void copy();

        public JComponent getComponent();
    }

    private static class RedoAction extends AbstractAction {
        private static final long serialVersionUID = 700221902961828425L;

        private final UndoManager undoManager;

        public RedoAction(UndoManager manager) {
            super(EComponentPopupMenu.REDO, Resources.getImageResource("arrow_redo.png"));
            this.undoManager = manager;
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                this.undoManager.redo();
            } catch (CannotRedoException cre) {
                //
            }
        }
    }

    private static class SelectAllAction extends AbstractAction {
        private static final long serialVersionUID = -6873629703224034266L;

        private final WritableComponent component;

        public SelectAllAction(WritableComponent component) {
            super(EComponentPopupMenu.SELECT_ALL);
            this.component = component;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            this.component.selectAll();
        }
    }

    private static class TextComponentWritableComponent implements WritableComponent {
        protected final JTextComponent component;

        public TextComponentWritableComponent(JTextComponent component) {
            this.component = component;
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#addUndoableEditListener(javax.swing.undo.UndoManager)
         */
        @Override
        public void addUndoableEditListener(UndoManager manager) {
            this.component.getDocument().addUndoableEditListener(manager);
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy()
         */
        @Override
        public void copy() {
            this.component.copy();
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#cut()
         */
        @Override
        public void cut() {
            this.component.cut();
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#delete()
         */
        @Override
        public void delete() {
            this.component.replaceSelection(null);
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#getComponent()
         */
        @Override
        public JComponent getComponent() {
            return this.component;
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#hasSelection()
         */
        @Override
        public boolean hasSelection() {
            return this.component.getSelectedText() != null;
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#paste()
         */
        @Override
        public void paste() {
            this.component.paste();
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#selectAll()
         */
        @Override
        public void selectAll() {
            Document doc = this.component.getDocument();
            this.component.setCaretPosition(0);
            this.component.moveCaretPosition(doc.getLength());

        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#unselect()
         */
        @Override
        public void unselect() {
            this.component.setCaretPosition(this.component.getCaretPosition());
        }
    }

    private static class UndoAction extends AbstractAction {
        private static final long serialVersionUID = -2639363038955484287L;

        private final UndoManager undoManager;

        public UndoAction(UndoManager manager) {
            super(EComponentPopupMenu.UNDO, Resources.getImageResource("arrow_undo.png"));
            this.undoManager = manager;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                this.undoManager.undo();
            } catch (CannotUndoException cue) {
                //
            }
        }
    }

    private static class UnselectAction extends AbstractAction {

        private static final long serialVersionUID = -736429406339064829L;

        private final WritableComponent component;

        public UnselectAction(WritableComponent component) {
            super(EComponentPopupMenu.UNSELECT);
            this.component = component;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            this.component.unselect();
        }
    }

    public static interface WritableComponent extends ReadableComponent {
        public void addUndoableEditListener(UndoManager manager);

        public void cut();

        public void delete();

        public boolean hasSelection();

        public void paste();

        public void selectAll();

        public void unselect();
    }

    public static final String DELETE = "delete";

    public static final String REDO = "redo";

    public static final String UNDO = "undo";

    public static final String CUT = "cut";

    public static final String COPY = "copy";

    public static final String PASTE = "paste";

    public static final String SELECT_ALL = "select-all";

    public static final String UNSELECT = "unselect";

    public static String newline = System.getProperty("line.separator");

    /**
     * copy to clipboard
     * 
     * @param content
     */
    public static void copy(String content) {
        java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(content), new ClipboardOwnerAdapter());
    }

    public static JPopupMenu installTextComponentPopupMenu(final JTextComponent component) {
        return EComponentPopupMenu.installTextComponentPopupMenu(new TextComponentWritableComponent(component));
    }

    public static JPopupMenu installTextComponentPopupMenu(final ReadableComponent component) {
        final Action copyAction = new CopyAction(component);
        JPopupMenu popup = new JPopupMenu();
        popup.add(copyAction);
        component.getComponent().addAncestorListener(new AncestorAdapter() {
            @Override
            public void ancestorAdded(AncestorEvent e) {
                component.getComponent().requestFocusInWindow();
            }
        });
        popup.addPopupMenuListener(new PopupMenuAdapter() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                copyAction.setEnabled(true);
            }
        });
        component.getComponent().setComponentPopupMenu(popup);
        return popup;
    }

    public static JPopupMenu installTextComponentPopupMenu(final WritableComponent component) {
        final UndoManager manager = new UndoManager();
        final Action copyAction = new CopyAction(component);
        final Action undoAction = new UndoAction(manager);
        final Action redoAction = new RedoAction(manager);
        final Action cutAction = new CutAction(component);
        final Action pasteAction = new PasteAction(component);
        final Action deleteAction = new DeleteAction(component);
        final Action _selectAllAction = new SelectAllAction(component);
        final Action _unselectAction = new UnselectAction(component);

        component.getComponent().addAncestorListener(new AncestorAdapter() {
            @Override
            public void ancestorAdded(AncestorEvent e) {
                manager.discardAllEdits();
                component.getComponent().requestFocusInWindow();
            }
        });

        component.addUndoableEditListener(manager);

        component.getComponent().getActionMap().put(EComponentPopupMenu.UNDO, undoAction);
        component.getComponent().getActionMap().put(EComponentPopupMenu.REDO, redoAction);
        InputMap imap = component.getComponent().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK), EComponentPopupMenu.UNDO);
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK), EComponentPopupMenu.REDO);

        JPopupMenu popup = new JPopupMenu();
        popup.add(redoAction);
        popup.addSeparator();
        popup.add(cutAction);
        popup.add(copyAction);
        popup.add(pasteAction);
        popup.add(deleteAction);
        popup.addSeparator();
        popup.add(_selectAllAction);
        popup.add(_unselectAction);

        popup.addPopupMenuListener(new PopupMenuAdapter() {
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                undoAction.setEnabled(true);
                redoAction.setEnabled(true);
            }

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                undoAction.setEnabled(manager.canUndo());
                redoAction.setEnabled(manager.canRedo());

                boolean flg = component.hasSelection();
                cutAction.setEnabled(flg);
                copyAction.setEnabled(flg);
                deleteAction.setEnabled(flg);
                _unselectAction.setEnabled(flg);

                _selectAllAction.setEnabled(true);
            }
        });
        component.getComponent().setComponentPopupMenu(popup);
        return popup;
    }

    /**
     * get text data on clipboard or null
     * 
     * @return
     */
    @SuppressWarnings("null")
    public static String paste() {
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // odd: the Object param of getContents is not currently used
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            try {
                result = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException ex) {
                // highly unlikely since we are using a standard DataFlavor
                System.out.println(ex);
                ex.printStackTrace();
            } catch (IOException ex) {
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
        return result;
    }
}

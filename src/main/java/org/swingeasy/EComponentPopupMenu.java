package org.swingeasy;

import java.awt.Component;
import java.awt.Event;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
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
public class EComponentPopupMenu extends JPopupMenu implements EComponentI {
    /**
     * JDOC
     */
    public static class AncestorAdapter implements AncestorListener {
        /**
         * 
         * @see javax.swing.event.AncestorListener#ancestorAdded(javax.swing.event.AncestorEvent)
         */
        @Override
        public void ancestorAdded(AncestorEvent event) {
            //
        }

        /**
         * 
         * @see javax.swing.event.AncestorListener#ancestorMoved(javax.swing.event.AncestorEvent)
         */
        @Override
        public void ancestorMoved(AncestorEvent event) {
            //
        }

        /**
         * 
         * @see javax.swing.event.AncestorListener#ancestorRemoved(javax.swing.event.AncestorEvent)
         */
        @Override
        public void ancestorRemoved(AncestorEvent event) {
            //
        }
    }

    /**
     * JDOC
     */
    public static class ClipboardOwnerAdapter implements ClipboardOwner {
        @Override
        public void lostOwnership(@SuppressWarnings("hiding") Clipboard clipboard, Transferable contents) {
            //
        }
    }

    /**
     * JDOC
     */
    private static class CopyAction extends EComponentPopupMenuAction {
        private static final long serialVersionUID = 3044725124645042202L;

        private final ReadableComponent component;

        public CopyAction(ReadableComponent component) {
            super(EComponentPopupMenu.COPY, Resources.getImageResource("page_copy.png"));
            this.component = component;
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK));
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

    /**
     * JDOC
     */
    private static class CutAction extends EComponentPopupMenuAction {
        private static final long serialVersionUID = 4328082010034890480L;

        private final WritableComponent component;

        public CutAction(WritableComponent component) {
            super(EComponentPopupMenu.CUT, Resources.getImageResource("cut.png"));
            this.component = component;
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK));
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

    /**
     * JDOC
     */
    private static class DeleteAction extends EComponentPopupMenuAction {
        private static final long serialVersionUID = -7609111337852520512L;

        private final WritableComponent component;

        public DeleteAction(WritableComponent component) {
            super(EComponentPopupMenu.DELETE, Resources.getImageResource("bin_closed.png"));
            this.component = component;
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
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

    /**
     * JDOC
     */
    private static class DeleteAllAction extends EComponentPopupMenuAction {
        private static final long serialVersionUID = -6873629703224034266L;

        private final WritableComponent component;

        public DeleteAllAction(WritableComponent component) {
            super(EComponentPopupMenu.DELETE_ALL, null);
            this.component = component;
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.component.selectAll();
            this.component.delete();
        }
    }

    /**
     * JDOC
     */
    public abstract static class EComponentPopupMenuAction extends AbstractAction implements EComponentI {
        private static final long serialVersionUID = 3408961844539862485L;

        protected String key;

        public EComponentPopupMenuAction(String name, Icon icon) {
            super(name, icon);
            this.key = name;
        }

        /**
         * 
         * @see org.swingeasy.EComponentI#setLocale(java.util.Locale)
         */
        @Override
        public void setLocale(Locale l) {
            super.putValue(Action.NAME, Messages.getString(l, "EComponentPopupMenuAction." + this.key));
        }
    }

    /**
     * JDOC
     */
    private static class FindAction extends EComponentPopupMenuAction {
        private static final long serialVersionUID = 4328082010034890480L;

        private final WritableComponent component;

        public FindAction(WritableComponent component) {
            super(EComponentPopupMenu.FIND, Resources.getImageResource("find.png"));
            this.component = component;
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK));
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.component.find();
        }
    }

    /**
     * JDOC
     */
    private static class FindNextAction extends EComponentPopupMenuAction {
        private static final long serialVersionUID = 4328082010034890480L;

        private final WritableComponent component;

        public FindNextAction(WritableComponent component) {
            super(EComponentPopupMenu.FIND_NEXT, null);
            this.component = component;
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.component.findNext();
        }
    }

    /**
     * JDOC
     */
    private static class FindPreviousAction extends EComponentPopupMenuAction {
        private static final long serialVersionUID = 4328082010034890480L;

        private final WritableComponent component;

        public FindPreviousAction(WritableComponent component) {
            super(EComponentPopupMenu.FIND_PREVIOUS, null);
            this.component = component;
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F3, Event.SHIFT_MASK));
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.component.findNext();
        }
    }

    /**
     * JDOC
     */
    private static class GotoBeginAction extends EComponentPopupMenuAction {
        private static final long serialVersionUID = 3085509525399492253L;

        private final WritableComponent component;

        public GotoBeginAction(WritableComponent component) {
            super(EComponentPopupMenu.GOTO_BEGIN, Resources.getImageResource("arrow_up.png"));
            this.component = component;
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0));
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.component.gotoBegin();
        }
    }

    /**
     * JDOC
     */
    private static class GotoEndAction extends EComponentPopupMenuAction {
        private static final long serialVersionUID = 6262977802889470104L;

        private final WritableComponent component;

        public GotoEndAction(WritableComponent component) {
            super(EComponentPopupMenu.GOTO_END, Resources.getImageResource("arrow_down.png"));
            this.component = component;
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_END, 0));
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.component.gotoEnd();
        }
    }

    /**
     * JDOC
     */
    private static class PasteAction extends EComponentPopupMenuAction {
        private static final long serialVersionUID = -7609111337852520512L;

        private final WritableComponent component;

        public PasteAction(WritableComponent component) {
            super(EComponentPopupMenu.PASTE, Resources.getImageResource("page_paste.png"));
            this.component = component;
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK));
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

    /**
     * JDOC
     */
    public static class PopupMenuAdapter implements PopupMenuListener {
        /**
         * 
         * @see javax.swing.event.PopupMenuListener#popupMenuCanceled(javax.swing.event.PopupMenuEvent)
         */
        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
            //
        }

        /**
         * 
         * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent)
         */
        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            //
        }

        /**
         * 
         * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent)
         */
        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            //
        }
    }

    /**
     * JDOC
     */
    public static interface ReadableComponent {
        public void copy();

        public JComponent getPopupParentComponent();
    }

    /**
     * JDOC
     */
    private static class RedoAction extends EComponentPopupMenuAction {
        private static final long serialVersionUID = 700221902961828425L;

        private final UndoManager undoManager;

        public RedoAction(UndoManager manager) {
            super(EComponentPopupMenu.REDO, Resources.getImageResource("arrow_redo.png"));
            this.undoManager = manager;
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK));
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

    /**
     * JDOC
     */
    private static class ReplaceAction extends EComponentPopupMenuAction {
        private static final long serialVersionUID = 4328082010034890480L;

        private final WritableComponent component;

        public ReplaceAction(WritableComponent component) {
            super(EComponentPopupMenu.REPLACE, Resources.getImageResource("text_replace.png"));
            this.component = component;
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_H, Event.CTRL_MASK));
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.component.replace();
        }
    }

    /**
     * JDOC
     */
    private static class SelectAllAction extends EComponentPopupMenuAction {
        private static final long serialVersionUID = -6873629703224034266L;

        private final WritableComponent component;

        public SelectAllAction(WritableComponent component) {
            super(EComponentPopupMenu.SELECT_ALL, null);
            this.component = component;
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            this.component.selectAll();
        }
    }

    /**
     * JDOC
     */
    public static class TextComponentWritableComponent implements WritableComponent {
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
            if (this.hasSelection()) {
                this.component.copy();
            } else {
                this.selectAll();
                this.component.copy();
                this.unselect();
                this.gotoBegin();
            }
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
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#find()
         */
        @Override
        public void find() {
            CustomizableOptionPane.showCustomDialog(this.component,
                    new JLabel(Messages.getString(this.component.getLocale(), "not.implemented.message")),
                    Messages.getString(this.component.getLocale(), "not.implemented.title"), MessageType.ERROR, OptionType.OK, null, null);
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#findNext()
         */
        @Override
        public void findNext() {
            CustomizableOptionPane.showCustomDialog(this.component,
                    new JLabel(Messages.getString(this.component.getLocale(), "not.implemented.message")),
                    Messages.getString(this.component.getLocale(), "not.implemented.title"), MessageType.ERROR, OptionType.OK, null, null);
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#getPopupParentComponent()
         */
        @Override
        public JComponent getPopupParentComponent() {
            return this.component;
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#gotoBegin()
         */
        @Override
        public void gotoBegin() {
            this.component.setCaretPosition(0);
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#gotoEnd()
         */
        @Override
        public void gotoEnd() {
            this.component.setCaretPosition(this.component.getText().length());
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
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#hasText()
         */
        @Override
        public boolean hasText() {
            return this.component.getText().length() > 0;
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
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#replace()
         */
        @Override
        public void replace() {
            CustomizableOptionPane.showCustomDialog(this.component,
                    new JLabel(Messages.getString(this.component.getLocale(), "not.implemented.message")),
                    Messages.getString(this.component.getLocale(), "not.implemented.title"), MessageType.ERROR, OptionType.OK, null, null);
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

    /**
     * JDOC
     */
    private static class UndoAction extends EComponentPopupMenuAction {
        private static final long serialVersionUID = -2639363038955484287L;

        private final UndoManager undoManager;

        public UndoAction(UndoManager manager) {
            super(EComponentPopupMenu.UNDO, Resources.getImageResource("arrow_undo.png"));
            this.undoManager = manager;
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK));
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                this.undoManager.undo();
            } catch (CannotUndoException cue) {
                //
            }
        }
    }

    /**
     * JDOC
     */
    private static class UnselectAction extends EComponentPopupMenuAction {
        private static final long serialVersionUID = -736429406339064829L;

        private final WritableComponent component;

        public UnselectAction(WritableComponent component) {
            super(EComponentPopupMenu.UNSELECT, null);
            this.component = component;
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.component.unselect();
        }
    }

    /**
     * JDOC
     */
    public static interface WritableComponent extends ReadableComponent {
        public void addUndoableEditListener(UndoManager manager);

        public void cut();

        public void delete();

        public void find();

        public void findNext();

        public void gotoBegin();

        public void gotoEnd();

        public boolean hasSelection();

        public boolean hasText();

        public void paste();

        public void replace();

        public void selectAll();

        public void unselect();
    }

    private static final long serialVersionUID = 8362926178135321789L;

    public static final String DELETE = "delete";

    public static final String REDO = "redo";

    public static final String UNDO = "undo";

    public static final String CUT = "cut";

    public static final String COPY = "copy";

    public static final String PASTE = "paste";

    public static final String SELECT_ALL = "select-all";

    public static final String DELETE_ALL = "delete-all";

    public static final String GOTO_BEGIN = "goto-begin";

    public static final String GOTO_END = "goto-end";

    public static final String UNSELECT = "unselect";

    public static final String FIND = "find";

    public static final String REPLACE = "replace";

    public static final String FIND_NEXT = "find-next";

    public static final String FIND_PREVIOUS = "find-previous";

    public static String newline = System.getProperty("line.separator");

    protected static Clipboard clipboard;

    /**
     * copy to clipboard
     * 
     * @param content
     */
    public static void copyToClipboard(String content) {
        EComponentPopupMenu.getClipboard().setContents(new StringSelection(content), new ClipboardOwnerAdapter());
    }

    /**
     * gets default (system) clipboard
     * 
     * @return
     */
    public static Clipboard getClipboard() {
        if (EComponentPopupMenu.clipboard == null) {
            EComponentPopupMenu.clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
        }
        return EComponentPopupMenu.clipboard;
    }

    /**
     * JDOC
     */
    public static JPopupMenu installPopupMenu(final ReadableComponent component) {
        final EComponentPopupMenuAction copyAction = new CopyAction(component);
        EComponentPopupMenu popup = new EComponentPopupMenu();
        popup.add(copyAction);
        component.getPopupParentComponent().addAncestorListener(new AncestorAdapter() {
            @Override
            public void ancestorAdded(AncestorEvent e) {
                component.getPopupParentComponent().requestFocusInWindow();
            }
        });
        popup.addPopupMenuListener(new PopupMenuAdapter() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                copyAction.setEnabled(true);
            }
        });
        component.getPopupParentComponent().setComponentPopupMenu(popup);
        return popup;
    }

    /**
     * JDOC
     */
    public static JPopupMenu installPopupMenu(final WritableComponent component) {
        final UndoManager manager = new UndoManager();
        final EComponentPopupMenuAction copyAction = new CopyAction(component);
        final EComponentPopupMenuAction undoAction = new UndoAction(manager);
        final EComponentPopupMenuAction redoAction = new RedoAction(manager);
        final EComponentPopupMenuAction cutAction = new CutAction(component);
        final EComponentPopupMenuAction pasteAction = new PasteAction(component);
        final EComponentPopupMenuAction deleteAction = new DeleteAction(component);
        final EComponentPopupMenuAction selectAllAction = new SelectAllAction(component);
        final EComponentPopupMenuAction unselectAction = new UnselectAction(component);
        final EComponentPopupMenuAction deleteAllAction = new DeleteAllAction(component);
        final EComponentPopupMenuAction gotoBeginAction = new GotoBeginAction(component);
        final EComponentPopupMenuAction gotoEndAction = new GotoEndAction(component);
        final EComponentPopupMenuAction findAction = new FindAction(component);
        final EComponentPopupMenuAction findNextAction = new FindNextAction(component);
        final EComponentPopupMenuAction findPreviousAction = new FindPreviousAction(component);
        final EComponentPopupMenuAction replaceAction = new ReplaceAction(component);

        component.getPopupParentComponent().addAncestorListener(new AncestorAdapter() {
            @Override
            public void ancestorAdded(AncestorEvent e) {
                manager.discardAllEdits();
                component.getPopupParentComponent().requestFocusInWindow();
            }
        });

        component.addUndoableEditListener(manager);

        component.getPopupParentComponent().getActionMap().put(EComponentPopupMenu.UNDO, undoAction);
        component.getPopupParentComponent().getActionMap().put(EComponentPopupMenu.REDO, redoAction);
        InputMap imap = component.getPopupParentComponent().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK), EComponentPopupMenu.UNDO);
        imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK), EComponentPopupMenu.REDO);

        EComponentPopupMenu popup = new EComponentPopupMenu();
        popup.add(undoAction);
        popup.add(redoAction);
        popup.addSeparator();
        popup.add(cutAction);
        popup.add(copyAction);
        popup.add(pasteAction);
        popup.add(deleteAction);
        popup.addSeparator();
        popup.add(selectAllAction);
        popup.add(unselectAction);
        popup.add(deleteAllAction);
        popup.add(gotoBeginAction);
        popup.add(gotoEndAction);
        popup.addSeparator();
        popup.add(findAction);
        popup.add(findNextAction);
        popup.add(findPreviousAction);
        popup.add(replaceAction);

        popup.addPopupMenuListener(new PopupMenuAdapter() {
            /**
             * 
             * @see org.swingeasy.EComponentPopupMenu.PopupMenuAdapter#popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent)
             */
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // undoAction.setEnabled(true);
                // redoAction.setEnabled(true);
            }

            /**
             * 
             * @see org.swingeasy.EComponentPopupMenu.PopupMenuAdapter#popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent)
             */
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                undoAction.setEnabled(manager.canUndo());
                redoAction.setEnabled(manager.canRedo());

                boolean flg = component.hasSelection();

                boolean ht = component.hasText();

                cutAction.setEnabled(flg);
                copyAction.setEnabled(flg);
                deleteAction.setEnabled(flg);
                unselectAction.setEnabled(flg);

                deleteAllAction.setEnabled(ht);
                selectAllAction.setEnabled(ht);
                gotoBeginAction.setEnabled(ht);
                gotoEndAction.setEnabled(ht);
                findAction.setEnabled(ht);
                findPreviousAction.setEnabled(ht);
                findNextAction.setEnabled(ht);
                replaceAction.setEnabled(ht);
            }
        });
        component.getPopupParentComponent().setComponentPopupMenu(popup);
        return popup;
    }

    /**
     * JDOC
     */
    public static JPopupMenu installReadOnlyTextComponentPopupMenu(final JTextComponent component) {
        return EComponentPopupMenu.installPopupMenu((ReadableComponent) new TextComponentWritableComponent(component));
    }

    /**
     * JDOC
     */
    public static JPopupMenu installTextComponentPopupMenu(final JTextComponent component) {
        return EComponentPopupMenu.installPopupMenu(new TextComponentWritableComponent(component));
    }

    /**
     * get text data on clipboard or null
     * 
     * @return
     */
    @SuppressWarnings("null")
    public static String pasteFromClipboard() {
        String result = "";
        // odd: the Object param of getContents is not currently used
        Transferable contents = EComponentPopupMenu.getClipboard().getContents(null);
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

    /**
     * JDOC
     */
    public static void setClipboard(Clipboard clipboard) {
        EComponentPopupMenu.clipboard = clipboard;
    }

    /**
     * hidden contructor
     */
    protected EComponentPopupMenu() {
        UIUtils.registerLocaleChangeListener(this);
    }

    /**
     * 
     * @see javax.swing.JPopupMenu#add(javax.swing.Action)
     */
    @Override
    public JMenuItem add(Action a) {
        if (a instanceof EComponentI) {
            EComponentI.class.cast(a).setLocale(this.getLocale());
        }
        JMenuItem mi = super.add(a);
        return mi;
    }

    /**
     * actions should implement {@link EComponentI} or extend {@link EComponentPopupMenuAction} so they listen to {@link Locale} changes
     * 
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        for (int i = 0; i < this.getComponentCount(); i++) {
            Component component = this.getComponent(i);
            if (component instanceof JMenuItem) {
                Action action = JMenuItem.class.cast(component).getAction();
                if (action instanceof EComponentI) {
                    EComponentI.class.cast(action).setLocale(l);
                }
            }
        }
    }
}

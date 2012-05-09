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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ComponentInputMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
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

import org.swingeasy.system.SystemSettings;

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
        public void lostOwnership(Clipboard clipboard, Transferable contents) {
            //
        }
    }

    /**
     * JDOC
     */
    private static class CopyAction extends EComponentPopupMenuAction<ReadableComponent> {
        private static final long serialVersionUID = 3044725124645042202L;

        public CopyAction(ReadableComponent component) {
            super(component, EComponentPopupMenu.COPY, Resources.getImageResource("page_copy.png"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK));
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.delegate.copy();
        }
    }

    /**
     * JDOC
     */
    private static class CutAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = 4328082010034890480L;

        public CutAction(WritableComponent component) {
            super(component, EComponentPopupMenu.CUT, Resources.getImageResource("cut.png"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK));
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.delegate.cut();
        }
    }

    /**
     * JDOC
     */
    private static class DeleteAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = -7609111337852520512L;

        public DeleteAction(WritableComponent component) {
            super(component, EComponentPopupMenu.DELETE, Resources.getImageResource("bin_closed.png"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.delegate.delete();
        }
    }

    /**
     * JDOC
     */
    private static class DeleteAllAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = -6873629703224034266L;

        public DeleteAllAction(WritableComponent component) {
            super(component, EComponentPopupMenu.DELETE_ALL, Resources.getImageResource("bin_closed.png"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, Event.CTRL_MASK));
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.delegate.selectAll();
            this.delegate.delete();
        }
    }

    /**
     * JDOC
     */
    public abstract static class EComponentPopupMenuAction<C extends ReadableComponent> extends AbstractAction implements EComponentI,
            HasParentComponent {
        private static final long serialVersionUID = 3408961844539862485L;

        protected String key;

        protected final C delegate;

        public EComponentPopupMenuAction(C delegate, String name, Icon icon) {
            super(name, icon);
            this.key = name;
            this.delegate = delegate;
            super.putValue(Action.ACTION_COMMAND_KEY, this.key);
        }

        /**
         * 
         * @see org.swingeasy.HasParentComponent#getParentComponent()
         */
        @Override
        public JComponent getParentComponent() {
            return this.delegate.getParentComponent();
        }

        /**
         * 
         * @see org.swingeasy.EComponentI#setLocale(java.util.Locale)
         */
        @Override
        public void setLocale(Locale l) {
            String name = Messages.getString(l, "EComponentPopupMenuAction." + this.key);
            super.putValue(Action.NAME, name);
            {
                String shortDescriptionKey = "EComponentPopupMenuAction." + this.key + "." + Action.SHORT_DESCRIPTION;
                String shortDescription = Messages.getString(l, shortDescriptionKey);
                if (shortDescriptionKey.equals(shortDescription)) {
                    shortDescription = name;
                }
                super.putValue(Action.SHORT_DESCRIPTION, shortDescription);
            }
            {
                String longDescriptionKey = "EComponentPopupMenuAction." + this.key + "." + Action.LONG_DESCRIPTION;
                String longDescription = Messages.getString(l, longDescriptionKey);
                if (longDescriptionKey.equals(longDescription)) {
                    longDescription = name;
                }
                super.putValue(Action.LONG_DESCRIPTION, longDescription);
            }
        }
    }

    /**
     * JDOC
     */
    private static class FindAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = 4328082010034890480L;

        public FindAction(WritableComponent component) {
            super(component, EComponentPopupMenu.FIND, Resources.getImageResource("find.png"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK));
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.delegate.find();
        }
    }

    /**
     * JDOC
     */
    private static class FindNextAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = 4328082010034890480L;

        public FindNextAction(WritableComponent component) {
            super(component, EComponentPopupMenu.FIND_NEXT, Resources.getImageResource("find.png"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.delegate.findNext();
        }
    }

    /**
     * JDOC
     */
    private static class GotoBeginAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = 3085509525399492253L;

        public GotoBeginAction(WritableComponent component) {
            super(component, EComponentPopupMenu.GOTO_BEGIN, Resources.getImageResource("arrow_up.png"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0));
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.delegate.gotoBegin();
        }
    }

    /**
     * JDOC
     */
    private static class GotoEndAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = 6262977802889470104L;

        public GotoEndAction(WritableComponent component) {
            super(component, EComponentPopupMenu.GOTO_END, Resources.getImageResource("arrow_down.png"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_END, 0));
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.delegate.gotoEnd();
        }
    }

    /**
     * JDOC
     */
    private static class PasteAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = -7609111337852520512L;

        public PasteAction(WritableComponent component) {
            super(component, EComponentPopupMenu.PASTE, Resources.getImageResource("page_paste.png"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK));
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.delegate.paste();
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
    public static interface ReadableComponent extends HasParentComponent {
        public void copy();
    }

    /**
     * JDOC
     */
    private static class RedoAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = 700221902961828425L;

        private final UndoManager undoManager;

        public RedoAction(WritableComponent component, UndoManager manager) {
            super(component, EComponentPopupMenu.REDO, Resources.getImageResource("arrow_redo.png"));
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
    private static class ReplaceAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = 4328082010034890480L;

        public ReplaceAction(WritableComponent component) {
            super(component, EComponentPopupMenu.REPLACE, Resources.getImageResource("text_replace.png"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_H, Event.CTRL_MASK));
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.delegate.replace();
        }
    }

    /**
     * JDOC
     */
    private static class SelectAllAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = -6873629703224034266L;

        public SelectAllAction(WritableComponent component) {
            super(component, EComponentPopupMenu.SELECT_ALL, Resources.getImageResource("page_white_text_width.png"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            this.delegate.selectAll();
        }
    }

    /**
     * JDOC
     */
    public static class TextComponentWritableComponent implements WritableComponent {
        protected final JTextComponent parentComponent;

        public TextComponentWritableComponent(JTextComponent parentComponent) {
            this.parentComponent = parentComponent;
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#addUndoableEditListener(javax.swing.undo.UndoManager)
         */
        @Override
        public void addUndoableEditListener(UndoManager manager) {
            this.parentComponent.getDocument().addUndoableEditListener(manager);
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy()
         */
        @Override
        public void copy() {
            if (this.hasSelection()) {
                this.parentComponent.copy();
            } else {
                this.selectAll();
                this.parentComponent.copy();
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
            this.parentComponent.cut();
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#delete()
         */
        @Override
        public void delete() {
            this.parentComponent.replaceSelection(null);
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#find()
         */
        @Override
        public void find() {
            if (this.parentComponent instanceof ETextArea) {
                new SearchDialog(false, ETextArea.class.cast(this.parentComponent)).setVisible(true);
            }
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#findNext()
         */
        @Override
        public void findNext() {
            if (this.parentComponent instanceof ETextArea) {
                ETextArea.class.cast(this.parentComponent).findNext();
            }
        }

        /**
         * 
         * @see org.swingeasy.HasParentComponent#getParentComponent()
         */
        @Override
        public JComponent getParentComponent() {
            return this.parentComponent;
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#gotoBegin()
         */
        @Override
        public void gotoBegin() {
            this.parentComponent.setCaretPosition(0);
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#gotoEnd()
         */
        @Override
        public void gotoEnd() {
            this.parentComponent.setCaretPosition(this.parentComponent.getText().length());
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#hasSelection()
         */
        @Override
        public boolean hasSelection() {
            return this.parentComponent.getSelectedText() != null;
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#hasText()
         */
        @Override
        public boolean hasText() {
            return (this.parentComponent.getText() != null) && (this.parentComponent.getText().length() > 0);
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#isEditable()
         */
        @Override
        public boolean isEditable() {
            return this.parentComponent.isEditable();
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#paste()
         */
        @Override
        public void paste() {
            this.parentComponent.paste();
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#replace()
         */
        @Override
        public void replace() {
            if (this.parentComponent instanceof ETextArea) {
                new SearchDialog(true, ETextArea.class.cast(this.parentComponent)).setVisible(true);
            }
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#selectAll()
         */
        @Override
        public void selectAll() {
            Document doc = this.parentComponent.getDocument();
            this.parentComponent.setCaretPosition(0);
            this.parentComponent.moveCaretPosition(doc.getLength());
        }

        /**
         * 
         * @see org.swingeasy.EComponentPopupMenu.WritableComponent#unselect()
         */
        @Override
        public void unselect() {
            this.parentComponent.setCaretPosition(this.parentComponent.getCaretPosition());
        }
    }

    /**
     * JDOC
     */
    private static class UndoAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = -2639363038955484287L;

        private final UndoManager undoManager;

        public UndoAction(WritableComponent component, UndoManager manager) {
            super(component, EComponentPopupMenu.UNDO, Resources.getImageResource("arrow_undo.png"));
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
    private static class UnselectAction extends EComponentPopupMenuAction<WritableComponent> {
        private static final long serialVersionUID = -736429406339064829L;

        public UnselectAction(WritableComponent component) {
            super(component, EComponentPopupMenu.UNSELECT, Resources.getImageResource("page_white_width.png"));
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        }

        /**
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            this.delegate.unselect();
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

        public boolean isEditable();

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

    /**
     * copy to clipboard
     * 
     * @param content
     */
    public static void copyToClipboard(String content) {
        SystemSettings.getClipboard().setContents(new StringSelection(content), new ClipboardOwnerAdapter());
    }

    public static void debug(JComponent component) {
        // List keystrokes in the WHEN_FOCUSED input map of the component
        InputMap map = component.getInputMap(JComponent.WHEN_FOCUSED);
        EComponentPopupMenu.list(map, map.keys());
        // List keystrokes in the component and in all parent input maps
        EComponentPopupMenu.list(map, map.allKeys());

        // List keystrokes in the WHEN_ANCESTOR_OF_FOCUSED_COMPONENT input map of the component
        map = component.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        EComponentPopupMenu.list(map, map.keys());
        // List keystrokes in all related input maps
        EComponentPopupMenu.list(map, map.allKeys());

        // List keystrokes in the WHEN_IN_FOCUSED_WINDOW input map of the component
        map = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        EComponentPopupMenu.list(map, map.keys());
        // List keystrokes in all related input maps
        EComponentPopupMenu.list(map, map.allKeys());
    }

    /**
     * JDOC
     */
    public static JPopupMenu installPopupMenu(final ReadableComponent component) {
        final EComponentPopupMenuAction<ReadableComponent> copyAction = new CopyAction(component);
        EComponentPopupMenu popup = new EComponentPopupMenu();
        popup.add(copyAction);
        component.getParentComponent().addAncestorListener(new AncestorAdapter() {
            @Override
            public void ancestorAdded(AncestorEvent e) {
                component.getParentComponent().requestFocusInWindow();
            }
        });
        popup.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                if (component instanceof PopupMenuListener) {
                    PopupMenuListener.class.cast(component).popupMenuCanceled(e);
                }
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                if (component instanceof PopupMenuListener) {
                    PopupMenuListener.class.cast(component).popupMenuWillBecomeInvisible(e);
                }
            }

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                copyAction.setEnabled(true);
                if (component instanceof PopupMenuListener) {
                    PopupMenuListener.class.cast(component).popupMenuWillBecomeVisible(e);
                }
            }
        });
        component.getParentComponent().setComponentPopupMenu(popup);
        return popup;
    }

    /**
     * JDOC
     */
    public static JPopupMenu installPopupMenu(final WritableComponent component) {
        if (!component.isEditable()) {
            return EComponentPopupMenu.installReadOnlyTextComponentPopupMenu((JTextComponent) component.getParentComponent());
        }

        final UndoManager manager = new UndoManager();
        final EComponentPopupMenuAction<ReadableComponent> copyAction = new CopyAction(component);
        final EComponentPopupMenuAction<WritableComponent> undoAction = new UndoAction(component, manager);
        final EComponentPopupMenuAction<WritableComponent> redoAction = new RedoAction(component, manager);
        final EComponentPopupMenuAction<WritableComponent> cutAction = new CutAction(component);
        final EComponentPopupMenuAction<WritableComponent> pasteAction = new PasteAction(component);
        final EComponentPopupMenuAction<WritableComponent> deleteAction = new DeleteAction(component);
        final EComponentPopupMenuAction<WritableComponent> selectAllAction = new SelectAllAction(component);
        final EComponentPopupMenuAction<WritableComponent> unselectAction = new UnselectAction(component);
        final EComponentPopupMenuAction<WritableComponent> deleteAllAction = new DeleteAllAction(component);
        final EComponentPopupMenuAction<WritableComponent> gotoBeginAction = new GotoBeginAction(component);
        final EComponentPopupMenuAction<WritableComponent> gotoEndAction = new GotoEndAction(component);

        final EComponentPopupMenuAction<WritableComponent> findAction = new FindAction(component);
        final EComponentPopupMenuAction<WritableComponent> findNextAction = new FindNextAction(component);
        final EComponentPopupMenuAction<WritableComponent> replaceAction = new ReplaceAction(component);

        final JComponent parentComponent = component.getParentComponent();

        parentComponent.addAncestorListener(new AncestorAdapter() {
            @Override
            public void ancestorAdded(AncestorEvent e) {
                manager.discardAllEdits();
                parentComponent.requestFocusInWindow();
            }
        });

        component.addUndoableEditListener(manager);

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

        if (parentComponent instanceof ETextArea) {
            popup.addSeparator();
            popup.add(findAction);
            popup.add(findNextAction);
            popup.add(replaceAction);
        }

        popup.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                if (component instanceof PopupMenuListener) {
                    PopupMenuListener.class.cast(component).popupMenuCanceled(e);
                }
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                undoAction.setEnabled(true);
                redoAction.setEnabled(true);
                if (component instanceof PopupMenuListener) {
                    PopupMenuListener.class.cast(component).popupMenuWillBecomeInvisible(e);
                }
            }

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                boolean flg = component.hasSelection();
                boolean ht = component.hasText();

                undoAction.setEnabled(manager.canUndo());
                redoAction.setEnabled(manager.canRedo());

                cutAction.setEnabled(flg);
                copyAction.setEnabled(flg);
                deleteAction.setEnabled(flg);
                unselectAction.setEnabled(flg);

                deleteAllAction.setEnabled(ht);
                selectAllAction.setEnabled(ht);
                gotoBeginAction.setEnabled(ht);
                gotoEndAction.setEnabled(ht);
                findAction.setEnabled(ht);
                findNextAction.setEnabled(ht);
                replaceAction.setEnabled(ht);

                if (component instanceof PopupMenuListener) {
                    PopupMenuListener.class.cast(component).popupMenuWillBecomeVisible(e);
                }
            }
        });

        parentComponent.setComponentPopupMenu(popup);

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

    public static String keyStroke2String(KeyStroke key) {
        if (key == null) {
            return "";
        }
        if (key.getModifiers() == 0) {
            return KeyEvent.getKeyText(key.getKeyCode());
        }
        return InputEvent.getModifiersExText(key.getModifiers()) + "+" + KeyEvent.getKeyText(key.getKeyCode());
    }

    protected static void list(InputMap map, KeyStroke[] keys) {
        if (keys == null) {
            return;
        }
        for (KeyStroke key : keys) {
            // This method is defined in Converting a KeyStroke to a String
            String keystrokeStr = EComponentPopupMenu.keyStroke2String(key);
            System.out.println(keystrokeStr);
            // Get the action name bound to this keystroke
            while (map.get(key) == null) {
                map = map.getParent();
            }
            if (map.get(key) instanceof String) {
                String actionName = (String) map.get(key);
                System.out.println(actionName);
            } else {
                Action action = (Action) map.get(key);
                System.out.println(action);
            }
        }
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
        Transferable contents = SystemSettings.getClipboard().getContents(null);
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

    public static void removeAllRegisteredKeystroke(JComponent component) {
        // removes all key actions
        // like F6 toggleFocus
        // like F8 startResize
        int[] conditions = { JComponent.WHEN_FOCUSED, JComponent.WHEN_IN_FOCUSED_WINDOW, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT };

        for (int condition : conditions) {
            component.setInputMap(condition, condition != JComponent.WHEN_IN_FOCUSED_WINDOW ? new InputMap() : new ComponentInputMap(component));
            component.setActionMap(new ActionMap());
        }
    }

    protected int cpx = 0;

    protected int cpy = 0;

    /**
     * hidden contructor
     */
    protected EComponentPopupMenu() {
        UIUtils.registerLocaleChangeListener((EComponentI) this);
    }

    protected void accelerate(Action action) {
        if (action instanceof HasParentComponent) {
            KeyStroke acceleratorKey = KeyStroke.class.cast(action.getValue(Action.ACCELERATOR_KEY));
            if (acceleratorKey != null) {
                JComponent component = HasParentComponent.class.cast(action).getParentComponent();
                if (component != null) {
                    String actionCommandKey = String.valueOf(action.getValue(Action.ACTION_COMMAND_KEY));
                    this.accelerate(component, action, acceleratorKey, actionCommandKey);
                }
            }
        }
    }

    protected void accelerate(JComponent component, Action action, KeyStroke acceleratorKey, String actionCommandKey) {
        if (component.getActionMap().get(actionCommandKey) != null) {
            return;
        }
        // System.out.println(component.getClass().getName() + " :: " + acceleratorKey + " :: " + actionCommandKey);
        component.getActionMap().put(actionCommandKey, action);
        component.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(acceleratorKey, actionCommandKey);
    }

    /**
     * 
     * let action implement {@link EComponentI} to listen to {@link Locale} changes and {@link HasParentComponent} to accelerate {@link Action}s for
     * the {@link JComponent}
     * 
     * @see javax.swing.JPopupMenu#add(javax.swing.Action)
     */
    @Override
    public JMenuItem add(Action action) {
        this.setLocale(action);
        JMenuItem mi = super.add(action);
        this.accelerate(action);
        return mi;
    }

    protected void setLocale(Action action) {
        if (action instanceof EComponentI) {
            EComponentI.class.cast(action).setLocale(this.getLocale());
        }
    }

    /**
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

    /**
     * 
     * @see javax.swing.JPopupMenu#setLocation(int, int)
     */
    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        this.cpx = x;
        this.cpy = y;
    }
}

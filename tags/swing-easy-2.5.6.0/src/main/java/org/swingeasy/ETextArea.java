package org.swingeasy;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.event.CaretEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.Highlight;

import org.apache.commons.lang.StringUtils;

/**
 * @author Jurgen
 */
public class ETextArea extends JTextArea implements EComponentI, HasValue<String>, ETextComponentI {
    protected class SearchHighlightPainter extends ETextAreaFillHighlightPainter {
        public SearchHighlightPainter() {
            super(new Color(245, 225, 145));
        }
    }

    private static final long serialVersionUID = -854993140855661563L;

    protected final List<ValueChangeListener<String>> valueChangeListeners = new ArrayList<ValueChangeListener<String>>();

    protected ETextAreaHighlightPainter highlightPainter;

    protected String lastSearch = null;

    public ETextArea(ETextAreaConfig cfg) {
        super(cfg.getRows(), cfg.getColumns());
        this.setEditable(cfg.isEnabled());
        this.init();
        cfg.lock();
    }

    public ETextArea(ETextAreaConfig cfg, String text) {
        super(text, cfg.getRows(), cfg.getColumns());
        this.setEditable(cfg.isEnabled());
        this.setAutoScroll(cfg.isAutoScroll());
        this.init();
        cfg.lock();
    }

    public void addDocumentKeyListener(DocumentKeyListener listener) {
        this.getDocument().addDocumentListener(listener);
        this.addKeyListener(listener);
    }

    public void addHighlight(int from, int to, ETextAreaHighlightPainter painter) throws BadLocationException {
        this.getHighlighter().addHighlight(from, to, painter);
    }

    /**
     * 
     * @see org.swingeasy.HasValue#addValueChangeListener(org.swingeasy.ValueChangeListener)
     */
    @Override
    public void addValueChangeListener(ValueChangeListener<String> listener) {
        this.valueChangeListeners.add(listener);
    }

    /**
     * 
     * @see org.swingeasy.HasValue#clearValueChangeListeners()
     */
    @Override
    public void clearValueChangeListeners() {
        this.valueChangeListeners.clear();
    }

    public void find(String find) {
        int start = this.getSelectionStart();
        Pattern pattern = Pattern.compile(find, Pattern.CASE_INSENSITIVE);
        String text = this.getText();
        Matcher matcher = pattern.matcher(text);
        if (matcher.find(start)) {
            // search from caret
            int selectionStart = matcher.start();
            if (selectionStart == start) {
                // search from caret +1
                if (matcher.find(start + 1)) {
                    selectionStart = matcher.start();
                } else {
                    selectionStart = -1;
                }
            }
            if (selectionStart != -1) {
                // result found
                int selectionEnd = matcher.end();
                this.select(selectionStart, selectionEnd);
                this.lastSearch = find;
            } else {
                // no result found: search from begin
                if (matcher.find(0)) {
                    selectionStart = matcher.start();
                    if (selectionStart != start) {
                        // result found
                        int selectionEnd = matcher.end();
                        this.select(selectionStart, selectionEnd);
                        this.lastSearch = find;
                    }
                }
            }
        }
    }

    public void findNext() {
        if (this.lastSearch != null) {
            this.find(this.lastSearch);
        }
    }

    public void fireCaretUpdate() {
        this.fireCaretUpdate(new ObjectWrapper(this).get("caretEvent", CaretEvent.class));
    }

    public ETextAreaHighlightPainter getHighlightPainter() {
        if (this.highlightPainter == null) {
            this.highlightPainter = new SearchHighlightPainter();
        }
        return this.highlightPainter;
    }

    public JToolBar getToolbar() {
        return new EToolBar(this.getComponentPopupMenu());
    }

    /**
     * 
     * @see org.swingeasy.ValidationDemo.HasValue#getValue()
     */
    @Override
    public String getValue() {
        String text = this.getText();
        return StringUtils.isBlank(text) ? null : text;
    }

    /** Creates highlights around all occurrences of pattern in textComp */
    public void highlightAll(String patternText) {
        // First remove all old highlights
        this.removeHighlights();

        try {
            Pattern pattern = Pattern.compile(patternText, Pattern.CASE_INSENSITIVE);
            String text = this.getText();
            // System.out.println("finding " + text);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                // System.out.println("highlighting: " + matcher.start() + " -> " + matcher.end());
                // Create highlighter using private painter and apply around pattern
                this.addHighlight(matcher.start(), matcher.end(), this.getHighlightPainter());
                this.lastSearch = patternText;
            }
        } catch (BadLocationException e) {
            //
        }
    }

    protected void init() {
        EComponentPopupMenu.installTextComponentPopupMenu(this);
        UIUtils.registerLocaleChangeListener((EComponentI) this);
        this.addDocumentKeyListener(new DocumentKeyListener() {
            @Override
            public void update(Type type, DocumentEvent e) {
                String value = ETextArea.this.getValue();
                for (ValueChangeListener<String> valueChangeListener : ETextArea.this.valueChangeListeners) {
                    valueChangeListener.valueChanged(value);
                }
            }
        });
    }

    public void removeDocumentKeyListener(DocumentKeyListener listener) {
        this.getDocument().removeDocumentListener(listener);
        this.removeKeyListener(listener);
    }

    /** Removes only our private highlights */
    public void removeHighlights() {
        this.removeHighlights(this.getHighlightPainter());
    }

    public void removeHighlights(ETextAreaHighlightPainter painter) {
        Highlighter hilite = this.getHighlighter();
        for (Highlight hi : hilite.getHighlights()) {
            if (hi.getPainter().equals(painter)) {
                hilite.removeHighlight(hi);
            }
        }
    }

    /**
     * 
     * @see org.swingeasy.HasValue#removeValueChangeListener(org.swingeasy.ValueChangeListener)
     */
    @Override
    public void removeValueChangeListener(ValueChangeListener<String> listener) {
        this.valueChangeListeners.remove(listener);
    }

    public void replace(String find, String replace) {
        this.setText(this.getText().replace(find, replace));
    }

    public void replaceAll(String find, String replace) {
        this.setText(this.getText().replaceAll(find, replace));
    }

    /**
     * enable/disable automatic scrolling to bottom when a line is added
     */
    public boolean setAutoScroll(boolean enable) {
        Caret caret = this.getCaret();
        if (!(caret instanceof DefaultCaret)) {
            return false;
        }
        ((DefaultCaret) caret).setUpdatePolicy(enable ? DefaultCaret.ALWAYS_UPDATE : DefaultCaret.NEVER_UPDATE);
        return true;
    };

    /**
     * 
     * @see org.swingeasy.ETextComponentI#setCaret(int)
     */
    @Override
    public void setCaret(int pos) {
        this.setCaretPosition(pos);
        this.fireCaretUpdate();
    }

    /**
     * 
     * @see org.swingeasy.ETextComponentI#setCaret(int, int)
     */
    @Override
    public void setCaret(int from, int to) {
        this.setCaretPosition(from);
        this.moveCaretPosition(to);
        this.fireCaretUpdate();
    }

    public void setHighlightPainter(ETextAreaHighlightPainter highlightPainter) {
        this.highlightPainter = highlightPainter;
    }
}

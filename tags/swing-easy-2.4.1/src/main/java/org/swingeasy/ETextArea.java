package org.swingeasy;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.Highlight;

/**
 * @author Jurgen
 */
public class ETextArea extends JTextArea implements EComponentI {
    public static class ETextAreaHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
        public ETextAreaHighlightPainter(Color hLColor) {
            super(hLColor);
        }
    }

    private static final long serialVersionUID = -854993140855661563L;

    protected ETextAreaHighlightPainter highlightPainter;

    protected String lastSearch = null;

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

    public ETextAreaHighlightPainter getHighlightPainter() {
        if (this.highlightPainter == null) {
            this.highlightPainter = new ETextAreaHighlightPainter(new Color(245, 225, 145));
        }
        return this.highlightPainter;
    }

    /** Creates highlights around all occurrences of pattern in textComp */
    public void highlightAll(String patternText) {
        // First remove all old highlights
        this.removeHighlights();

        try {
            Highlighter hilite = this.getHighlighter();
            Pattern pattern = Pattern.compile(patternText, Pattern.CASE_INSENSITIVE);
            String text = this.getText();
            System.out.println("finding " + text);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                // System.out.println("highlighting: " + matcher.start() + " -> " + matcher.end());
                // Create highlighter using private painter and apply around pattern
                hilite.addHighlight(matcher.start(), matcher.end(), this.getHighlightPainter());
                this.lastSearch = patternText;
            }
        } catch (BadLocationException e) {
            //
        }
    }

    protected void init() {
        EComponentPopupMenu.installTextComponentPopupMenu(this);
        UIUtils.registerLocaleChangeListener(this);
    }

    /** Removes only our private highlights */
    public void removeHighlights() {
        Highlighter hilite = this.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();

        for (Highlight _hilite : hilites) {
            if (_hilite.getPainter() instanceof ETextAreaHighlightPainter) {
                hilite.removeHighlight(_hilite);
            }
        }
    }

    public void replace(String find, String replace) {
        this.setText(this.getText().replace(find, replace));
    }

    public void replaceAll(String find, String replace) {
        this.setText(this.getText().replaceAll(find, replace));
    }

    public void setHighlightPainter(ETextAreaHighlightPainter highlightPainter) {
        this.highlightPainter = highlightPainter;
    }
}

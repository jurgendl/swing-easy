package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import javax.swing.tree.TreePath;

import ca.odell.glazedlists.matchers.Matcher;

/**
 * @author Jurgen
 */
public class ETreeSearchComponent<T> extends JComponent implements Matcher<T>, EComponentI {
    /** serialVersionUID */
    private static final long serialVersionUID = 5196244125968828897L;

    protected final ETree<T> eTree;

    protected final ETreeI<T> sTree;

    protected JTextComponent input;

    protected Pattern pattern = null;

    protected JButton commit;

    protected JLabel label;

    public ETreeSearchComponent(ETree<T> eTree) {
        this.eTree = eTree;
        this.sTree = eTree.stsi();
        this.createComponent();
    }

    protected void createComponent() {
        this.setLayout(new BorderLayout());
        this.input = new JTextField();
        this.input.setBorder(null);
        this.input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ETreeSearchComponent.this.search();
                }
            }
        });
        this.add(this.input, BorderLayout.CENTER);
        this.commit = new EIconButton(new Dimension(18, 18), Resources.getImageResource("find.png"));//$NON-NLS-1$
        this.commit.setActionCommand("search");//$NON-NLS-1$
        this.commit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ETreeSearchComponent.this.search();
            }
        });
        this.add(this.commit, BorderLayout.EAST);
        this.label = new JLabel();
        this.add(this.label, BorderLayout.WEST);
        this.setLocale(null);
    }

    /**
     * 
     * @see ca.odell.glazedlists.matchers.Matcher#matches(java.lang.Object)
     */
    @Override
    public boolean matches(T item) {
        return this.pattern.matcher(String.valueOf(item)).find();
    }

    protected TreePath nextMatchTryTop() {
        try {
            TreePath current = this.sTree.getSelectedOrTopNodePath();
            return this.sTree.getNextMatch(current, this);
        } catch (IllegalArgumentException ex1) {
            try {
                TreePath current = this.sTree.getTopNodePath();
                return this.sTree.getNextMatch(current, this);
            } catch (IllegalArgumentException ex2) {
                return null;
            }
        }
    }

    protected void onMatch(TreePath nextMatch) {
        this.eTree.scrollPathToVisible(nextMatch);
        this.sTree.setSelectionPath(nextMatch);
    }

    protected void onNoMatch() {
        String message = Messages.getString(this.getLocale(), "ETree.SearchComponent.nomatch");//$NON-NLS-1$
        String title = Messages.getString(this.getLocale(), "ETree.SearchComponent.searchmatch");//$NON-NLS-1$
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    protected void search() {
        String text = this.input.getText();
        if (text.length() == 0) {
            return;
        }
        this.pattern = Pattern.compile(text, Pattern.CASE_INSENSITIVE);
        TreePath nextMatch = this.nextMatchTryTop();
        if (nextMatch != null) {
            this.onMatch(nextMatch);
        } else {
            this.onNoMatch();
        }
    }

    /**
     * 
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        this.commit.setToolTipText(Messages.getString(l, "ETree.SearchComponent.search"));//$NON-NLS-1$
        this.label.setText(Messages.getString(l, "ETree.SearchComponent.search") + ": "); //$NON-NLS-1$ //$NON-NLS-2$
    }
}

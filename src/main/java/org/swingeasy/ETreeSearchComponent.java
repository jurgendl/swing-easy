package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
public class ETreeSearchComponent<T> extends JComponent {
    /** serialVersionUID */
    private static final long serialVersionUID = 5196244125968828897L;

    protected final ETree<T> eTree;

    protected JTextComponent input;

    public ETreeSearchComponent(ETree<T> eTree) {
        this.eTree = eTree;
        this.init();
    }

    protected void init() {
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
        JButton commit = new EIconButton(new Dimension(18, 18), Resources.getImageResource("find.png"));//$NON-NLS-1$
        commit.setActionCommand("search");//$NON-NLS-1$
        commit.setToolTipText(Messages.getString("ETree.SearchComponent.search"));//$NON-NLS-1$
        commit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ETreeSearchComponent.this.search();

            }
        });
        this.add(commit, BorderLayout.EAST);
        JLabel label = new JLabel(Messages.getString("ETree.SearchComponent.search") + ": "); //$NON-NLS-1$ //$NON-NLS-2$
        this.add(label, BorderLayout.WEST);
    }

    protected void search() {
        String text = this.input.getText();
        if (text.length() == 0) {
            return;
        }
        TreePath current;
        try {
            current = this.eTree.getSelectionPath();
            if (current == null) {
                throw new NullPointerException();
            }
        } catch (Exception ex) {
            current = new TreePath(this.eTree.getModel().getRoot());
        }
        final Pattern pattern = Pattern.compile(text, Pattern.CASE_INSENSITIVE);
        ETreeI<T> stsi = this.eTree.stsi();
        TreePath nextMatch;
        try {
            nextMatch = stsi.getNextMatch(current, new Matcher<T>() {
                @Override
                public boolean matches(T item) {
                    return pattern.matcher(String.valueOf(item)).find();
                }
            });
        } catch (IllegalArgumentException ex) {
            current = new TreePath(this.eTree.getModel().getRoot());
            nextMatch = stsi.getNextMatch(current, new Matcher<T>() {
                @Override
                public boolean matches(T item) {
                    return pattern.matcher(String.valueOf(item)).find();
                }
            });
        }
        if (nextMatch != null) {
            stsi.setSelectionPath(nextMatch);
        } else {
            String message = Messages.getString("ETree.SearchComponent.nomatch");//$NON-NLS-1$
            String title = Messages.getString("ETree.SearchComponent.searchmatch");//$NON-NLS-1$
            JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
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
        this.input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ETreeSearchComponent.this.search();
                }
            }
        });
        this.add(this.input, BorderLayout.CENTER);
        JButton commit = new JButton("search");
        commit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ETreeSearchComponent.this.search();

            }
        });
        this.add(commit, BorderLayout.EAST);
        JLabel label = new JLabel("Search:");
        this.add(label, BorderLayout.WEST);
    }

    protected void search() {
        TreePath current;
        try {
            current = this.eTree.getSelectionPath();
            if (current == null) {
                throw new NullPointerException();
            }
        } catch (Exception ex) {
            current = new TreePath(this.eTree.getModel().getRoot());
        }
        final Pattern pattern = Pattern.compile(this.input.getText(), Pattern.CASE_INSENSITIVE);
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
            stsi.expandPath(nextMatch);
            stsi.setSelectionPath(nextMatch);
        }
    }
}

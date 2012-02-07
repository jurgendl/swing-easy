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

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.matchers.Matcher;

/**
 * @author Jurgen
 */
public class EListSearchComponent<T> extends JComponent implements Matcher<T>, EComponentI {
    /** serialVersionUID */
    private static final long serialVersionUID = -8699648472825404199L;

    protected final EList<T> eList;

    protected final EListI<T> sList;

    protected JTextComponent input;

    protected Pattern pattern = null;

    protected JLabel label;

    protected JButton commit;

    public EListSearchComponent(EList<T> eList) {
        this.eList = eList;
        this.sList = eList.stsi();
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
                    EListSearchComponent.this.search();
                }
            }
        });
        this.add(this.input, BorderLayout.CENTER);
        this.commit = new EIconButton(new Dimension(18, 18), Resources.getImageResource("find.png"));//$NON-NLS-1$
        this.commit.setActionCommand("search");//$NON-NLS-1$
        this.commit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EListSearchComponent.this.search();
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

    protected EListRecord<T> nextMatchTryTop() {
        EventList<EListRecord<T>> records = this.eList.getRecords();

        if (records.size() == 0) {
            return null;
        }

        int index = this.eList.getSelectedIndex();

        if (index == -1) {
            index = 0;
        }

        index++;

        while (index < records.size()) {
            EListRecord<T> record = records.get(index);
            if (this.pattern.matcher(record.getStringValue()).find()) {
                return record;
            }
            index++;
        }

        return null;
    }

    protected void onMatch(EListRecord<T> nextMatch) {
        this.eList.scrollToVisibleRecord(nextMatch);
        this.sList.setSelectedRecord(nextMatch);
    }

    protected void onNoMatch() {
        String message = Messages.getString(this.getLocale(), "EList.SearchComponent.nomatch");//$NON-NLS-1$
        String title = Messages.getString(this.getLocale(), "EList.SearchComponent.searchmatch");//$NON-NLS-1$
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    protected void search() {
        String text = this.input.getText();
        if (text.length() == 0) {
            return;
        }
        this.pattern = Pattern.compile(text, Pattern.CASE_INSENSITIVE);
        EListRecord<T> nextMatch = this.nextMatchTryTop();
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
        this.commit.setToolTipText(Messages.getString(l, "EList.SearchComponent.search"));//$NON-NLS-1$
        this.label.setText(Messages.getString(l, "EList.SearchComponent.search") + ": ");//$NON-NLS-1$ //$NON-NLS-2$
    }
}

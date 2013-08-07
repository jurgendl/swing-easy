package org.swingeasy.wizard;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.swingeasy.GradientPanel;
import org.swingeasy.GradientPanel.GradientOrientation;

/**
 * @author Jurgen
 */
public class EWizard extends JPanel {
    private class WizardListCellRenderer extends JLabel implements ListCellRenderer {
        private static final long serialVersionUID = -1156206181214408618L;

        private Font font;

        private Font selectedFont;

        public WizardListCellRenderer() {
            this.font = this.getFont();
            this.selectedFont = this.font.deriveFont(Font.BOLD);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            this.setFont(EWizard.this.wizardPages.get(EWizard.this.wizardPage) == value ? this.selectedFont : this.font);
            if (value != null) {
                WizardPage page = (WizardPage) value;
                value = (EWizard.this.wizardPages.indexOf(page) + 1) + ". " + page.getTitle();
            }
            this.setText(String.valueOf(value));
            return this;
        }
    }

    private static final long serialVersionUID = -1099152219083484268L;

    private JLabel lblIcon;

    private JPanel mainPanel;

    private int wizardPage = 0;

    private final List<WizardPage> wizardPages = new ArrayList<WizardPage>();

    private JSeparator separatorTop;

    private JButton btnHelp;

    private JButton btnCancel;

    private JButton btnBack;

    private JButton btnNext;

    private JButton btnFinish;

    private JList pageList;

    private JPanel topSubPanel;

    private JLabel lblTitle;

    private JLabel lblDescription;

    private GradientPanel leftPanel;

    /**
     * Created by Eclipse WindowBuilder.
     */
    public EWizard() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0 };
        gridBagLayout.columnWeights = new double[] { 1.0 };
        this.setLayout(gridBagLayout);

        GradientPanel topPanel = new GradientPanel();
        topPanel.setOrientation(GradientOrientation.HORIZONTAL);
        GridBagConstraints gbc_topPanel = new GridBagConstraints();
        gbc_topPanel.anchor = GridBagConstraints.NORTH;
        gbc_topPanel.fill = GridBagConstraints.HORIZONTAL;
        gbc_topPanel.gridx = 0;
        gbc_topPanel.gridy = 0;
        this.add(topPanel, gbc_topPanel);
        topPanel.setLayout(new BorderLayout(10, 10));

        this.lblIcon = new JLabel("");
        this.lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        this.lblIcon.setRequestFocusEnabled(false);
        this.lblIcon.setPreferredSize(new Dimension(80, 80));
        this.lblIcon.setMinimumSize(new Dimension(80, 80));
        this.lblIcon.setMaximumSize(new Dimension(80, 80));
        topPanel.add(this.lblIcon, BorderLayout.EAST);

        this.topSubPanel = new JPanel();
        this.topSubPanel.setOpaque(false);
        topPanel.add(this.topSubPanel, BorderLayout.CENTER);
        this.topSubPanel.setLayout(new BorderLayout(0, 0));

        this.lblTitle = new JLabel("title");
        this.lblTitle.setRequestFocusEnabled(false);
        this.lblTitle.setBorder(new EmptyBorder(8, 14, 0, 0));
        this.lblTitle.setFont(this.lblTitle.getFont().deriveFont(this.lblTitle.getFont().getStyle() | Font.BOLD));
        this.topSubPanel.add(this.lblTitle, BorderLayout.NORTH);

        this.lblDescription = new JLabel("description");
        this.lblDescription.setRequestFocusEnabled(false);
        this.lblDescription.setVerticalAlignment(SwingConstants.TOP);
        this.lblDescription.setBorder(new EmptyBorder(6, 28, 0, 4));
        this.topSubPanel.add(this.lblDescription, BorderLayout.CENTER);

        this.separatorTop = new JSeparator();
        GridBagConstraints gbc_separatorTop = new GridBagConstraints();
        gbc_separatorTop.fill = GridBagConstraints.HORIZONTAL;
        gbc_separatorTop.gridx = 0;
        gbc_separatorTop.gridy = 1;
        this.add(this.separatorTop, gbc_separatorTop);

        JPanel centerPanel = new JPanel();
        GridBagConstraints gbc_centerPanel = new GridBagConstraints();
        gbc_centerPanel.fill = GridBagConstraints.BOTH;
        gbc_centerPanel.gridx = 0;
        gbc_centerPanel.gridy = 2;
        this.add(centerPanel, gbc_centerPanel);
        GridBagLayout gbl_centerPanel = new GridBagLayout();
        gbl_centerPanel.columnWeights = new double[] { 0.0, 0.0, 1.0 };
        gbl_centerPanel.rowWeights = new double[] { 1.0 };
        centerPanel.setLayout(gbl_centerPanel);

        this.leftPanel = new GradientPanel();
        this.leftPanel.setPreferredSize(new Dimension(240, 0));
        this.leftPanel.setMinimumSize(new Dimension(240, 0));
        GridBagConstraints gbc_leftPanel = new GridBagConstraints();
        gbc_leftPanel.fill = GridBagConstraints.VERTICAL;
        gbc_leftPanel.gridx = 0;
        gbc_leftPanel.gridy = 0;
        centerPanel.add(this.leftPanel, gbc_leftPanel);

        JLabel lblPages = new JLabel("Steps:");
        Font original = lblPages.getFont();
        @SuppressWarnings("unchecked")
        Map<TextAttribute, Object> attributes = (Map<TextAttribute, Object>) original.getAttributes();
        attributes.put(java.awt.font.TextAttribute.UNDERLINE, java.awt.font.TextAttribute.UNDERLINE_ON);
        attributes.put(java.awt.font.TextAttribute.WEIGHT, java.awt.font.TextAttribute.WEIGHT_BOLD);
        lblPages.setFont(original.deriveFont(attributes));
        lblPages.setRequestFocusEnabled(false);

        this.pageList = new JList();
        this.pageList.setRequestFocusEnabled(false);
        this.pageList.setBorder(new EmptyBorder(8, 14, 10, 10));
        this.pageList.setOpaque(false);
        GroupLayout gl_leftPanel = new GroupLayout(this.leftPanel);
        gl_leftPanel.setHorizontalGroup(gl_leftPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(
                        gl_leftPanel
                                .createSequentialGroup()
                                .addContainerGap()
                                .addGroup(
                                        gl_leftPanel
                                                .createParallelGroup(Alignment.LEADING)
                                                .addGroup(
                                                        gl_leftPanel.createSequentialGroup()
                                                                .addComponent(this.pageList, GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                                                                .addContainerGap())
                                                .addGroup(
                                                        gl_leftPanel
                                                                .createSequentialGroup()
                                                                .addComponent(lblPages, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE).addGap(189)))));
        gl_leftPanel.setVerticalGroup(gl_leftPanel.createParallelGroup(Alignment.LEADING).addGroup(
                gl_leftPanel.createSequentialGroup().addGap(8).addComponent(lblPages).addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(this.pageList, GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE).addContainerGap()));
        this.leftPanel.setLayout(gl_leftPanel);

        JSeparator separator = new JSeparator();
        separator.setOrientation(SwingConstants.VERTICAL);
        GridBagConstraints gbc_separator = new GridBagConstraints();
        gbc_separator.fill = GridBagConstraints.VERTICAL;
        gbc_separator.gridx = 1;
        gbc_separator.gridy = 0;
        centerPanel.add(separator, gbc_separator);

        this.mainPanel = new JPanel();
        GridBagConstraints gbc_mainPanel = new GridBagConstraints();
        gbc_mainPanel.fill = GridBagConstraints.BOTH;
        gbc_mainPanel.gridx = 2;
        gbc_mainPanel.gridy = 0;
        centerPanel.add(this.mainPanel, gbc_mainPanel);
        this.mainPanel.setLayout(new CardLayout(0, 0));

        JSeparator separatorBottom = new JSeparator();
        GridBagConstraints gbc_separatorBottom = new GridBagConstraints();
        gbc_separatorBottom.fill = GridBagConstraints.HORIZONTAL;
        gbc_separatorBottom.gridx = 0;
        gbc_separatorBottom.gridy = 3;
        this.add(separatorBottom, gbc_separatorBottom);

        JPanel bottomPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) bottomPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        GridBagConstraints gbc_bottomPanel = new GridBagConstraints();
        gbc_bottomPanel.fill = GridBagConstraints.BOTH;
        gbc_bottomPanel.gridx = 0;
        gbc_bottomPanel.gridy = 4;
        this.add(bottomPanel, gbc_bottomPanel);

        this.btnHelp = new JButton("Help");
        this.btnHelp.setPreferredSize(new Dimension(80, 25));
        bottomPanel.add(this.btnHelp);

        this.btnCancel = new JButton("Cancel");
        this.btnCancel.setPreferredSize(new Dimension(80, 25));
        bottomPanel.add(this.btnCancel);

        this.btnBack = new JButton("< Back");
        this.btnBack.setPreferredSize(new Dimension(80, 25));
        this.btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EWizard.this.back();
            }
        });
        bottomPanel.add(this.btnBack);

        this.btnNext = new JButton("Next >");
        this.btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EWizard.this.next();
            }
        });
        this.btnNext.setPreferredSize(new Dimension(80, 25));
        bottomPanel.add(this.btnNext);

        this.btnFinish = new JButton("Finish");
        this.btnFinish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EWizard.this.finish();
            }
        });
        this.btnFinish.setPreferredSize(new Dimension(80, 25));
        bottomPanel.add(this.btnFinish);

    }

    public void addWizardPage(WizardPage page) {
        this.wizardPages.add(page);
    }

    protected void back() {
        if (0 < this.wizardPage) {
            this.wizardPage--;
            this.updatePage();
        }
    }

    protected void finish() {
        this.wizardPage = this.wizardPages.size() - 1;
        this.updatePage();
    }

    protected JButton getBtnBack() {
        return this.btnBack;
    }

    protected JButton getBtnCancel() {
        return this.btnCancel;
    }

    protected JButton getBtnFinish() {
        return this.btnFinish;
    }

    protected JButton getBtnHelp() {
        return this.btnHelp;
    }

    protected JButton getBtnNext() {
        return this.btnNext;
    }

    protected JLabel getLblDescription() {
        return this.lblDescription;
    }

    protected JLabel getLblIcon() {
        return this.lblIcon;
    }

    protected JLabel getLblTitle() {
        return this.lblTitle;
    }

    protected GradientPanel getLeftPanel() {
        return this.leftPanel;
    }

    protected JPanel getMainPanel() {
        return this.mainPanel;
    }

    protected JList getPageList() {
        return this.pageList;
    }

    public void init() {
        int i = 0;
        DefaultListModel pageListModel = new DefaultListModel();
        this.getPageList().setModel(pageListModel);
        this.getPageList().setCellRenderer(new WizardListCellRenderer());
        DefaultListSelectionModel selectionModel = new DefaultListSelectionModel() {
            private static final long serialVersionUID = 6618282932208651223L;

            @Override
            public void setSelectionInterval(int index0, int index1) {
                //
            }
        };
        this.getPageList().setSelectionModel(selectionModel);
        for (WizardPage page : this.wizardPages) {
            pageListModel.add(i, page);
            this.getMainPanel().add(page.createComponent(), String.valueOf(i++));
        }

        if (this.getBtnCancel().getAction() == null) {
            this.getBtnCancel().setVisible(false);
        }
        if (this.getBtnHelp().getAction() == null) {
            this.getBtnHelp().setVisible(false);
        }

        this.getLblDescription().setPreferredSize(new Dimension(0, 0));

        this.updatePage();
    }

    protected void next() {
        if (this.wizardPage < (this.wizardPages.size() - 1)) {
            this.wizardPage++;
            this.updatePage();
        }
    }

    public void setCancelAction(Action cancelAction) {
        String text = this.getBtnCancel().getText();
        this.getBtnCancel().setHideActionText(true);
        this.getBtnCancel().setAction(cancelAction);
        this.getBtnCancel().setText(text);
    }

    public void setHelpAction(Action helpAction) {
        String text = this.getBtnHelp().getText();
        this.getBtnHelp().setHideActionText(true);
        this.getBtnHelp().setAction(helpAction);
        this.getBtnHelp().setText(text);
    }

    public void setIcon(Icon icon) {
        this.getLblIcon().setIcon(icon);
    }

    public void setLeftPanelVisible(boolean b) {
        if (b) {
            this.getLeftPanel().setPreferredSize(new Dimension(240, 0));
            this.getLeftPanel().setMinimumSize(new Dimension(240, 0));
        } else {
            this.getLeftPanel().setPreferredSize(new Dimension(0, 0));
            this.getLeftPanel().setMinimumSize(new Dimension(0, 0));
        }
    }

    protected void updatePage() {
        CardLayout cl = (CardLayout) this.getMainPanel().getLayout();
        cl.show(this.getMainPanel(), String.valueOf(this.wizardPage));

        WizardPage page = this.wizardPages.get(this.wizardPage);
        this.getLblTitle().setText(page.getTitle());
        this.getLblDescription().setText(
                "<html><p>" + page.getDescription().replaceAll("\r\n", "<br>").replaceAll("\n", "<br>").replaceAll("\r", "<br>") + "</p></html>");

        this.getBtnBack().setEnabled(0 < this.wizardPage);
        this.getBtnNext().setEnabled(this.wizardPage < (this.wizardPages.size() - 1));
        this.getBtnFinish().setEnabled(this.wizardPage < (this.wizardPages.size() - 2));

        this.getPageList().setSelectedIndex(this.wizardPage);
        this.getPageList().repaint();
    }
}

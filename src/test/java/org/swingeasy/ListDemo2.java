package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 * @author Jurgen
 */
public class ListDemo2 {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        UIUtils.niceLookAndFeel();
        EListConfig cfg = new EListConfig();
        cfg.setSortable(false);
        @SuppressWarnings("rawtypes")
        EList cc = new EList(cfg);
        {
            JFrame f = new JFrame();
            f.getContentPane().add(
                    new JScrollPane(cc, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED),
                    BorderLayout.CENTER);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            cc = cc.stsi();
            f.setSize(200, 200);
            {
                JPanel jp = new JPanel(new FlowLayout());
                ButtonGroup bg = new ButtonGroup();
                {
                    JRadioButton jrben = new JRadioButton("en");
                    bg.add(jrben);
                    jrben.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            UIUtils.setCurrentLocale(Locale.ENGLISH);
                        }
                    });
                    jp.add(jrben);
                }
                {
                    JRadioButton jrbnl = new JRadioButton("nl");
                    bg.add(jrbnl);
                    jrbnl.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            UIUtils.setCurrentLocale(new Locale("nl"));
                        }
                    });
                    jp.add(jrbnl);
                }
                f.getContentPane().add(jp, BorderLayout.NORTH);
            }
            f.setVisible(true);
        }
        cc.addRecord(new EListRecord<Date>(new Date()));
        cc.addRecord(new EListRecord<Color>(Color.red));
        cc.addRecord(new EListRecord<Long>(1000l));
        cc.addRecord(new EListRecord<Integer>(100));
        cc.addRecord(new EListRecord<Float>(100.01f));
        cc.addRecord(new EListRecord<Double>(1000.001d));
        cc.addRecord(new EListRecord<Boolean>(true));
        System.out.println(cc.getCellRenderer());
    }
}

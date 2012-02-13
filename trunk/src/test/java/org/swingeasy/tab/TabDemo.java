package org.swingeasy.tab;

import java.awt.Container;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileSystemView;

import org.swingeasy.ETabbedPane;
import org.swingeasy.ETabbedPaneConfig;
import org.swingeasy.Rotation;
import org.swingeasy.UIUtils;

/**
 * @see http://java-swing-tips.blogspot.com/2010/02/tabtransferhandler.html
 * @see http://www.exampledepot.com/taxonomy/term/319
 */
public class TabDemo {
    public static void main(String[] args) {
        UIUtils.niceLookAndFeel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        TabDemo demo = new TabDemo();
        demo.addComponents(frame.getContentPane());
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Demo");
        frame.setVisible(true);
        demo.top.setDividerLocation(.5);
        demo.bottom.setDividerLocation(.5);
        demo.middle.setDividerLocation(.5);
    }

    private JTabbedPane tablefttop = new ETabbedPane(new ETabbedPaneConfig(true, true));

    private JTabbedPane tabrighttop = new ETabbedPane(new ETabbedPaneConfig(Rotation.COUNTER_CLOCKWISE, true, true));

    private JTabbedPane tableftbottom = new ETabbedPane(new ETabbedPaneConfig(Rotation.CLOCKWISE, true, true));

    private JTabbedPane tabrightbottom = new ETabbedPane(new ETabbedPaneConfig(true, true));

    private JSplitPane top = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.tablefttop, this.tabrighttop);

    private JSplitPane bottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.tableftbottom, this.tabrightbottom);

    private JSplitPane middle = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.top, this.bottom);

    private void add(JTabbedPane tab) {
        for (int i = 0; i < 5; i++) {
            tab.addTab("tab " + tab.getName() + " - " + i, FileSystemView.getFileSystemView().getSystemIcon(new File(".")), new JTextField("content "
                    + tab.getName() + " - " + i), "tooltip " + tab.getName() + " - " + i);
            tab.setEnabledAt(i, (i % 2) == 0);
        }
    }

    private void addComponents(Container container) {
        container.add(this.middle);

        this.top.setDividerSize(1);
        this.bottom.setDividerSize(1);
        this.middle.setDividerSize(1);

        this.tableftbottom.setTabPlacement(SwingConstants.RIGHT);
        this.tableftbottom.setName("tableftbottom");

        this.tablefttop.setTabPlacement(SwingConstants.TOP);
        this.tablefttop.setName("tablefttop");

        this.tabrightbottom.setTabPlacement(SwingConstants.BOTTOM);
        this.tabrightbottom.setName("tabrightbottom");

        this.tabrighttop.setTabPlacement(SwingConstants.LEFT);
        this.tabrighttop.setName("tabrighttop");

        this.add(this.tablefttop);
        this.add(this.tabrighttop);
        this.add(this.tableftbottom);
        this.add(this.tabrightbottom);
    }

}

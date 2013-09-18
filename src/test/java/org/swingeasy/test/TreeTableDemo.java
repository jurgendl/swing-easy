package org.swingeasy.test;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.swingeasy.ETreeTableConfig;
import org.swingeasy.UIUtils;

/**
 * @author Jurgen
 */
public class TreeTableDemo {
    public static class Rec {
        String key;

        String value;

        Rec parent;

        public Rec(Rec parent, String v, String t) {
            this.key = v;
            this.value = t;
            this.parent = parent;
        }

        public String getKey() {
            return this.key;
        }

        public Rec getParent() {
            return this.parent;
        }

        public String getValue() {
            return this.value;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setParent(Rec parent) {
            this.parent = parent;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }

    public static void main(String[] args) {
        UIUtils.systemLookAndFeel();

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Collection<ETreeTableRecord<Rec>> coll = new ArrayList<ETreeTableRecord<Rec>>();
        for (int i = 0; i < 10; i++) {
            Rec lvl1 = new Rec(null, "level 0 : " + i, "description level 0 : " + i);
            coll.add(new ETreeTableRecordBean<Rec>(lvl1));
            for (int j = 0; j < 10; j++) {
                Rec lvl2 = new Rec(lvl1, "level 1 : " + j, "description level 1 : " + i);
                coll.add(new ETreeTableRecordBean<Rec>(lvl2));
                for (int k = 0; k < 10; k++) {
                    coll.add(new ETreeTableRecordBean<Rec>(new Rec(lvl2, "level 2 : " + k, "value " + ((i * 100) + (j * 10) + k))));
                }
            }
        }
        ETreeTable<Rec> t = new ETreeTable<Rec>(new ETreeTableConfig());
        t.stsi().addRecords(coll);

        f.getContentPane().add(new JScrollPane(t));
        f.pack();
        f.setVisible(true);
    }
}

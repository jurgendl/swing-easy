package org.swingeasy;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * @author Jurgen
 */
public class TreeTableDemo {
    public static class Rec {
        String key;

        String value;

        String tmp;

        public Rec(String key, String value, String tmp) {
            this.key = key;
            this.value = value;
            this.tmp = tmp;
        }

        public String getKey() {
            return this.key;
        }

        public String getTmp() {
            return this.tmp;
        }

        public String getValue() {
            return this.value;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setTmp(String tmp) {
            this.tmp = tmp;
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

        String[] h = { "key", "value", "tmp" };

        Collection<ETreeTableRecord<Rec>> coll = new ArrayList<ETreeTableRecord<Rec>>();
        for (int i = 0; i < 10; i++) {
            Rec lvl1 = new Rec("level 0 : " + i, "description level 0 : " + i, "" + (i * 100));
            ETreeTableRecordBean<Rec> rec1 = new ETreeTableRecordBean<Rec>(null, lvl1, h);
            coll.add(rec1);
            for (int j = 0; j < 10; j++) {
                Rec lvl2 = new Rec("level 1 : " + j, "description level 1 : " + i, "" + ((i * 100) + (j * 10)));
                ETreeTableRecordBean<Rec> rec2 = new ETreeTableRecordBean<Rec>(rec1, lvl2, h);
                coll.add(rec2);
                for (int k = 0; k < 10; k++) {
                    Rec lvl3 = new Rec("level 2 : " + k, "value " + ((i * 100) + (j * 10) + k), "" + ((i * 100) + (j * 10) + k));
                    ETreeTableRecordBean<Rec> rec3 = new ETreeTableRecordBean<Rec>(rec2, lvl3, h);
                    coll.add(rec3);
                }
            }
        }

        ETreeTableHeaders<Rec> headers = new ETreeTableHeaders<Rec>(h);
        ETreeTable<Rec> t = new ETreeTable<Rec>(new ETreeTableConfig(), headers);
        t.stsi().addRecords(coll);
        f.getContentPane().add(new JScrollPane(t));
        f.pack();
        f.setVisible(true);
    }
}

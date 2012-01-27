package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.TransferHandler;

/**
 * @author Jurgen
 */
public class ListDnDDemo {
    public static class DelegatingTransferable implements Transferable {
        List<Transferable> sources = new ArrayList<Transferable>();

        public DelegatingTransferable(Transferable... sources) {
            this.sources.addAll(Arrays.asList(sources));
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            for (Transferable t : this.sources) {
                if (t.isDataFlavorSupported(flavor)) {
                    return t.getTransferData(flavor);
                }
            }
            return null;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            List<DataFlavor> l = new ArrayList<DataFlavor>();
            for (Transferable t : this.sources) {
                for (DataFlavor f : t.getTransferDataFlavors()) {
                    l.add(f);
                }
            }
            return l.toArray(new DataFlavor[0]);
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            for (Transferable t : this.sources) {
                if (t.isDataFlavorSupported(flavor)) {
                    return true;
                }
            }
            return false;
        }

    }

    private static class EListTransferHandler<T> extends TransferHandler {

        private static final long serialVersionUID = 5818426933041278034L;

        @Override
        public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
            return true;
        }

        @Override
        public boolean canImport(TransferSupport support) {
            return true;
        }

        @SuppressWarnings("unchecked")
        protected Collection<T> castDataCollection(Object obj) {
            return (Collection<T>) obj;
        }

        @SuppressWarnings("unchecked")
        protected EList<T> castEList(JComponent comp) {
            return (EList<T>) comp;
        }

        @SuppressWarnings("unchecked")
        protected Collection<EListRecord<T>> castEListRecordCollection(Object obj) {
            return (Collection<EListRecord<T>>) obj;
        }

        @Override
        protected Transferable createTransferable(JComponent comp) {
            EList<T> elist = this.castEList(comp);
            Collection<EListRecord<T>> records = elist.getSelectedRecords();
            Collection<T> data = new ArrayList<T>();
            for (EListRecord<T> record : records) {
                data.add(record.get());
            }
            return new DelegatingTransferable(new DataHandler(data, DataFlavor.javaJVMLocalObjectMimeType), new StringSelection(String.valueOf(data)));
        }

        @Override
        public int getSourceActions(JComponent c) {
            return TransferHandler.COPY_OR_MOVE; // COPY, MOVE, COPY_OR_MOVE
        }

        /**
         * 
         * @see javax.swing.TransferHandler#importData(javax.swing.JComponent, java.awt.datatransfer.Transferable)
         */
        @Override
        public boolean importData(JComponent comp, Transferable t) {
            EList<T> elist = this.castEList(comp);
            Collection<EListRecord<T>> records = new ArrayList<EListRecord<T>>();
            try {
                for (T data : this.castDataCollection(t.getTransferData(t.getTransferDataFlavors()[0]))) {
                    records.add(new EListRecord<T>(data));
                }
            } catch (UnsupportedFlavorException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            elist.addRecords(records);
            return true;
        }

    }

    private static <T> void drag(EList<T> list) {
        list.setDragEnabled(true);
        list.setTransferHandler(new EListTransferHandler<T>());

    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        JSplitPane jsp = new JSplitPane();
        f.getContentPane().add(jsp, BorderLayout.CENTER);
        {
            EListConfig cfg = new EListConfig();
            EList<String> list = new EList<String>(cfg);
            ListDnDDemo.drag(list);
            list.addRecord(new EListRecord<String>("bean1"));
            list.addRecord(new EListRecord<String>("bean2"));
            list.addRecord(new EListRecord<String>("bean3"));
            jsp.setLeftComponent(list);
        }
        {
            EListConfig cfg = new EListConfig();
            EList<String> list = new EList<String>(cfg);
            ListDnDDemo.drag(list);
            jsp.setRightComponent(list);
        }
        jsp.setDividerLocation(200);
        f.setSize(400, 400);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}

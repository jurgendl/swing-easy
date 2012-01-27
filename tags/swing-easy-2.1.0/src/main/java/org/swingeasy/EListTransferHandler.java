package org.swingeasy;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 * http://docs.oracle.com/javase/tutorial/uiswing/dnd/together.html<br>
 * http://www.javaworld.com/javaworld/jw-08-1999/jw-08-draganddrop.html<br>
 * 
 * @author Jurgen
 */
public class EListTransferHandler<T> extends TransferHandler {
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
    protected T[] castData(Object obj) {
        return (T[]) obj;
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
        final T[] data = this.castData(new Object[records.size()]);
        int i = 0;
        for (EListRecord<T> record : records) {
            data[i++] = record.get();
        }
        final DataFlavor localDataFlavor = this.getLocalDataFlavor();
        Transferable local = new Transferable() {
            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                return data;
            }

            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[] { localDataFlavor };
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return localDataFlavor.equals(flavor);
            }
        };
        final DataFlavor remoteDataFlavor = this.getRemoteDataFlavor();
        Transferable remote = new Transferable() {
            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(data);
                oos.close();
                return new ByteArrayInputStream(baos.toByteArray());
            }

            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[] { remoteDataFlavor };
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return remoteDataFlavor.equals(flavor);
            }
        };
        Transferable string = new StringSelection(Arrays.toString(data));
        return new DelegatingTransferable(local, remote, string);
    }

    private DataFlavor getLocalDataFlavor() {
        try {
            return new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    private DataFlavor getRemoteDataFlavor() {
        try {
            return new DataFlavor(DataFlavor.javaRemoteObjectMimeType);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
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
        Collection<EListRecord<T>> records = null;

        try {
            Object transferData = t.getTransferData(this.getLocalDataFlavor());
            records = new ArrayList<EListRecord<T>>();
            for (T data : this.castData(transferData)) {
                records.add(new EListRecord<T>(data));
            }
        } catch (RuntimeException ex) {
            //
        } catch (UnsupportedFlavorException ex) {
            //
        } catch (IOException ex) {
            //
        }

        try {
            InputStream is = (InputStream) t.getTransferData(this.getRemoteDataFlavor());
            ObjectInputStream ois = new ObjectInputStream(is);
            Object transferData = ois.readObject();
            records = new ArrayList<EListRecord<T>>();
            for (T data : this.castData(transferData)) {
                records.add(new EListRecord<T>(data));
            }
        } catch (RuntimeException ex) {
            //
        } catch (UnsupportedFlavorException ex) {
            //
        } catch (IOException ex) {
            //
        } catch (ClassNotFoundException ex) {
            //
        }

        if (records == null) {
            return false;
        }
        elist.addRecords(records);
        return true;
    }
}
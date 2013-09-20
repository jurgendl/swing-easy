package org.swingeasy;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import org.swingeasy.EComponentPopupMenu.ReadableComponent;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TreeList;
import ca.odell.glazedlists.TreeList.ExpansionModel;
import ca.odell.glazedlists.TreeList.Format;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import ca.odell.glazedlists.swing.TreeTableSupport;

/**
 * @author Jurgen
 */
public class ETreeTable<T> extends JTable implements ETreeTableI<T>, ReadableComponent {
    private static final long serialVersionUID = -1389100924292211731L;

    protected ETreeTable<T> stsi;

    protected final ETreeTableConfig cfg;

    protected Action[] actions;

    protected EventList<ETreeTableRecord<T>> records;

    protected DefaultEventTableModel<ETreeTableRecord<T>> tableModel;

    protected DefaultEventSelectionModel<ETreeTableRecord<T>> tableSelectionModel;

    protected ETreeTableHeaders<T> tableFormat;

    protected TreeList<ETreeTableRecord<T>> treeList;

    protected Format<ETreeTableRecord<T>> format;

    protected ETreeTable() {
        this.cfg = null;
    }

    public ETreeTable(ETreeTableConfig cfg, ETreeTableHeaders<T> headers) {
        this.init(this.cfg = cfg._lock(), headers);
    }

    /**
     * @see org.swingeasy.ETreeTableI#addRecord(org.swingeasy.ETreeTableRecord)
     */
    @Override
    public void addRecord(final ETreeTableRecord<T> record) {
        this.records.add(record);
    }

    /**
     * @see org.swingeasy.ETreeTableI#addRecords(java.util.Collection)
     */
    @Override
    public void addRecords(final Collection<ETreeTableRecord<T>> r) {
        this.records.addAll(r);
    }

    /**
     * @see org.swingeasy.EComponentPopupMenu.ReadableComponent#copy(java.awt.event.ActionEvent)
     */
    @Override
    public void copy(ActionEvent e) {
        //
    }

    /**
     * @see org.swingeasy.HasParentComponent#getParentComponent()
     */
    @Override
    public JComponent getParentComponent() {
        return this;
    }

    /**
     * JDOC
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public ETreeTable<T> getSimpleThreadSafeInterface() {
        try {
            if (this.stsi == null) {
                this.stsi = EventThreadSafeWrapper.getSimpleThreadSafeInterface(ETreeTable.class, this, ETreeTableI.class);
            }
            return this.stsi;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected void init(ETreeTableConfig config, ETreeTableHeaders<T> headers) {
        this.tableFormat = headers;

        Collection<ETreeTableRecord<T>> coll = new ArrayList<ETreeTableRecord<T>>();
        this.records = GlazedLists.threadSafeList(GlazedLists.eventList(coll));

        this.format = new Format<ETreeTableRecord<T>>() {
            @Override
            public boolean allowsChildren(ETreeTableRecord<T> element) {
                return true;
            }

            @Override
            public Comparator<ETreeTableRecord<T>> getComparator(int depth) {
                // return (Comparator) GlazedLists.beanPropertyComparator(ETreeTableRecord/* <Rec> */.class, "key", "value");
                return new Comparator<ETreeTableRecord<T>>() {
                    @Override
                    public int compare(ETreeTableRecord<T> o1, ETreeTableRecord<T> o2) {
                        return o1.compareTo(o2);
                    }
                };
            }

            @Override
            public void getPath(List<ETreeTableRecord<T>> path, ETreeTableRecord<T> element) {
                ArrayList<ETreeTableRecord<T>> stack = new ArrayList<ETreeTableRecord<T>>();
                ETreeTableRecord<T> current = element;
                while (current != null) {
                    stack.add(current);
                    current = current.getParent();
                }
                for (int i = stack.size() - 1; i >= 0; i--) {
                    path.add(stack.get(i));
                }
            }
        };
        @SuppressWarnings("unchecked")
        ExpansionModel<ETreeTableRecord<T>> expansionModel = ca.odell.glazedlists.TreeList.NODES_START_COLLAPSED;
        this.treeList = new TreeList<ETreeTableRecord<T>>(this.records, this.format, expansionModel);

        this.tableModel = new DefaultEventTableModel<ETreeTableRecord<T>>(this.treeList, this.tableFormat);
        this.setModel(this.tableModel);

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    try {
                        TreeTableSupport support = TreeTableSupport.install(ETreeTable.this, ETreeTable.this.treeList, 0);
                        support.setArrowKeyExpansionEnabled(true);
                        support.setShowExpanderForEmptyParent(false);
                        support.setSpaceKeyExpansionEnabled(true);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        } catch (InterruptedException ex) {
            //
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex.getTargetException());
        }

        UIUtils.registerLocaleChangeListener((EComponentI) this);

        if (config.isDefaultPopupMenu()) {
            this.installPopupMenuAction(EComponentPopupMenu.installPopupMenu(this));
        }
    }

    /**
     * JDOC
     */
    protected void installPopupMenuAction(EComponentPopupMenu menu) {
        this.actions = new Action[] {};
        for (Action action : this.actions) {
            if (action == null) {
                menu.addSeparator();
            } else {
                menu.add(action);
                EComponentPopupMenu.accelerate(this, action);
            }
        }
        menu.checkEnabled();
        menu.addSeparator();
        // @SuppressWarnings({ "unchecked", "rawtypes" })
        // ServiceLoader<ETreeTableExporter<T>> exporterService = (ServiceLoader) ServiceLoader.load(ETreeTableExporter.class);
        // Iterator<ETreeTableExporter<T>> iterator = exporterService.iterator();
        // while (iterator.hasNext()) {
        // try {
        // ETreeTableExporter<T> exporter = iterator.next();
        // EComponentExporterAction<ETreeTable<T>> action = new EComponentExporterAction<ETreeTable<T>>(exporter, this);
        // menu.add(action);
        // } catch (ServiceConfigurationError ex) {
        // ex.printStackTrace(System.err);
        // }
        // }
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public ETreeTable<T> stsi() {
        return this.getSimpleThreadSafeInterface();
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public ETreeTable<T> STSI() {
        return this.getSimpleThreadSafeInterface();
    }
}

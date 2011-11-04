package org.swingeasy;

import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.TextFilterator;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import ca.odell.glazedlists.swing.EventComboBoxModel;

public class EComboBox<T> extends JComboBox implements EComboBoxI<T> {
    private static final long serialVersionUID = -3602504810131193505L;

    protected EComboBoxConfig cfg;

    protected EventList<T> list;

    public EComboBox(EComboBoxConfig cfg, T... values) {
        this.cfg = cfg;
        this.list = GlazedLists.eventList(Arrays.asList(values));
        EventComboBoxModel<T> model = new EventComboBoxModel<T>(this.list);
        this.setModel(model);
        if (cfg.isAutoComplete()) {
            this.getSimpleThreadSafeInterface().activateAutoCompletion();
        }
    }

    /**
     * 
     * @see org.swingeasy.EComboBoxI#activateAutoCompletion()
     */
    @Override
    public void activateAutoCompletion() {
        AutoCompleteSupport<T> support = AutoCompleteSupport.install(this, this.list, new TextFilterator<T>() {
            @Override
            public void getFilterStrings(List<String> baseList, T element) {
                baseList.add(String.valueOf(element));
            }
        });
        support.setFilterMode(TextMatcherEditor.CONTAINS);
    }

    /**
     * J_DOC
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public EComboBoxI<T> getSimpleThreadSafeInterface() {
        final EComboBoxI<T> combobox = this;
        javassist.util.proxy.ProxyFactory f = new javassist.util.proxy.ProxyFactory();
        f.setInterfaces(new Class[] { EComboBoxI.class });
        javassist.util.proxy.MethodHandler mi = new javassist.util.proxy.MethodHandler() {
            @Override
            public Object invoke(final Object self, final java.lang.reflect.Method method, final java.lang.reflect.Method paramMethod2,
                    final Object[] args) throws Throwable {
                boolean edt = javax.swing.SwingUtilities.isEventDispatchThread();

                if (edt) {
                    return method.invoke(combobox, args);
                }

                final Object[] values = new Object[] { null, null };
                Runnable doRun = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            values[0] = method.invoke(combobox, args);
                        } catch (Exception ex) {
                            values[1] = ex;
                        }
                    }
                };
                boolean wait = !method.getReturnType().equals(Void.TYPE);
                if (!wait) {
                    SwingUtilities.invokeLater(doRun);
                    return Void.TYPE;
                }
                SwingUtilities.invokeAndWait(doRun);
                if (values[1] != null) {
                    throw Exception.class.cast(values[1]);
                }
                return values[0];
            }
        };
        Object proxy;
        try {
            proxy = f.createClass().newInstance();
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        ((javassist.util.proxy.ProxyObject) proxy).setHandler(mi);
        return (EComboBoxI<T>) proxy;
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public EComboBoxI<T> stsi() {
        return this.getSimpleThreadSafeInterface();
    }

    /**
     * @see #getSimpleThreadSafeInterface()
     */
    public EComboBoxI<T> STSI() {
        return this.getSimpleThreadSafeInterface();
    }
}

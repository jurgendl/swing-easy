package org.swingeasy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import javax.swing.SwingUtilities;

/**
 * @author Jurgen
 */
public class EventThreadSafeWrapper {
    public static interface EventSafe {
        //
    }

    private static class ValueHolder<T> {
        private T value;

        public ValueHolder() {
            super();
        }

        public ValueHolder(T value) {
            this.value = value;
        }
    }

    public static <C, I> C getSimpleThreadSafeInterface(final Class<C> componentClass, final C component, final Class<I> interfaced) {
        if (component instanceof EventSafe) {
            return component;
        }
        ProxyFactory f = new ProxyFactory();
        f.setSuperclass(componentClass);
        f.setInterfaces(new Class[] { interfaced, EventSafe.class });
        final List<String> interfacedMethods = new ArrayList<String>();
        for (Method method : interfaced.getDeclaredMethods()) {
            String sig = method.getReturnType() + " " + method.getName() + "(" + Arrays.toString(method.getParameterTypes()) + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            interfacedMethods.add(sig);
        }
        MethodHandler mi = new MethodHandler() {
            @Override
            public Object invoke(final Object self, final Method method, final Method proceed, final Object[] args) throws Throwable {
                String sig = method.getReturnType() + " " + method.getName() + "(" + Arrays.toString(method.getParameterTypes()) + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                boolean interfacedMethod = interfacedMethods.contains(sig);
                if (!interfacedMethod) {
                    return method.invoke(component, args);
                }
                boolean edt = SwingUtilities.isEventDispatchThread();
                if (edt) {
                    return method.invoke(component, args);
                }
                final ValueHolder<Object> returnValue = new ValueHolder<Object>(Void.TYPE);
                final ValueHolder<Throwable> exceptionThrown = new ValueHolder<Throwable>();
                Runnable doRun = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            returnValue.value = method.invoke(component, args);
                        } catch (InvocationTargetException ex) {
                            exceptionThrown.value = ex.getTargetException();
                        } catch (Exception ex) {
                            exceptionThrown.value = ex;
                        }
                    }
                };
                SwingUtilities.invokeAndWait(doRun);
                if (exceptionThrown.value != null) {
                    throw exceptionThrown.value;
                }
                return returnValue.value;
            }
        };
        Object proxy;
        try {
            proxy = f.createClass().newInstance();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        ((ProxyObject) proxy).setHandler(mi);
        return componentClass.cast(proxy);
    }
}

package org.swingeasy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import javax.swing.SwingUtilities;

public class EventThreadSafeWrapper {
    public static interface EventSafe {
        //
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
            String sig = method.getReturnType() + " " + method.getName() + "(" + Arrays.toString(method.getParameterTypes()) + ")";
            interfacedMethods.add(sig);
        }
        MethodHandler mi = new MethodHandler() {
            @Override
            public Object invoke(final Object self, final Method method, final Method proceed, final Object[] args) throws Throwable {
                String sig = method.getReturnType() + " " + method.getName() + "(" + Arrays.toString(method.getParameterTypes()) + ")";
                boolean interfacedMethod = interfacedMethods.contains(sig);

                if (!interfacedMethod) {
                    return method.invoke(component, args);
                }

                boolean edt = SwingUtilities.isEventDispatchThread();

                if (edt) {
                    return method.invoke(component, args);
                }

                final Object[] values = new Object[] { null, null };
                Runnable doRun = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            values[0] = method.invoke(component, args);
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

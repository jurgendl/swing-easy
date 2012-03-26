package org.swingeasy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingUtilities;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author Jurgen
 */
public class CglibEventThreadSafeWrapper<C> implements MethodInterceptor {
    public static interface EventSafe {
        //
    }

    public static <C, I> C getSimpleThreadSafeInterface(final Class<C> componentClass, final C component, final Class<I> interfaced) {
        if (component instanceof EventSafe) {
            return component;
        }

        return new CglibEventThreadSafeWrapper<C>(componentClass, component, interfaced).createProxy();
    }

    protected final List<String> interfacedMethods;

    protected final C component;

    protected final Class<C> componentClass;

    private final Enhancer factory;

    protected C proxy;

    protected <I> CglibEventThreadSafeWrapper(final Class<C> componentClass, final C component, final Class<I> interfaced) {
        this.component = component;
        this.componentClass = componentClass;
        this.factory = new Enhancer();
        this.factory.setSuperclass(componentClass);
        this.factory.setInterfaces(new Class[] { interfaced, EventSafe.class });
        this.factory.setInterceptDuringConstruction(false);
        this.interfacedMethods = new ArrayList<String>();
        for (Method method : interfaced.getDeclaredMethods()) {
            String sig = method.getReturnType() + " " + method.getName() + "(" + Arrays.toString(method.getParameterTypes()) + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            this.interfacedMethods.add(sig);
        }
    }

    protected C createProxy() {
        try {
            this.factory.setCallback(this);
            this.proxy = this.componentClass.cast(this.factory.create());
            return this.proxy;
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /**
     * 
     * @see net.sf.cglib.proxy.MethodInterceptor#intercept(java.lang.Object, java.lang.reflect.Method, java.lang.Object[],
     *      net.sf.cglib.proxy.MethodProxy)
     */
    @Override
    public Object intercept(final Object obj, final Method method, final Object[] args, @SuppressWarnings("hiding") final MethodProxy proxy)
            throws Throwable {
        String sig = method.getReturnType() + " " + method.getName() + "(" + Arrays.toString(method.getParameterTypes()) + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        boolean interfacedMethod = this.interfacedMethods.contains(sig);
        if (!interfacedMethod) {
            return method.invoke(this.component, args);
        }
        boolean edt = SwingUtilities.isEventDispatchThread();
        if (edt) {
            try {
                return method.invoke(this.component, args);
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
                throw ex.getTargetException();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }
        final ValueHolder<Throwable> exceptionThrown = new ValueHolder<Throwable>();
        final ValueHolder<Object> returnValue = new ValueHolder<Object>(Void.TYPE);
        Runnable doRun = new Runnable() {
            @Override
            public void run() {
                try {
                    returnValue.setValue(method.invoke(CglibEventThreadSafeWrapper.this.component, args));
                } catch (InvocationTargetException ex) {
                    ex.printStackTrace();
                    exceptionThrown.setValue(ex.getTargetException());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    exceptionThrown.setValue(ex);
                }
            }
        };
        try {
            SwingUtilities.invokeAndWait(doRun);
        } catch (InterruptedException ex) {
            //
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
        if (exceptionThrown.getValue() != null) {
            throw exceptionThrown.getValue();
        }
        return returnValue.getValue();
    }
}

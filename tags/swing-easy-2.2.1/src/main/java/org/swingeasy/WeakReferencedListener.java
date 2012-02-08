package org.swingeasy;

import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.EventObject;

/**
 * @author Jurgen
 */
public final class WeakReferencedListener<T> {
    @SuppressWarnings("unchecked")
    public static <T> T wrap(final Class<T> interfaceClass, final T delegate) {
        return (T) java.lang.reflect.Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] { interfaceClass },
                new WeakReferencedListener<T>(interfaceClass, delegate).getInvocationHandler());
    }

    private final WeakReference<T> weakreference;

    private final Class<T> interfaceClass;

    private final int hashCode;

    private WeakReferencedListener(Class<T> interfaceClass, T delegate) {
        this.interfaceClass = interfaceClass;
        this.weakreference = new WeakReference<T>(delegate);
        this.hashCode = delegate.hashCode();
    }

    private java.lang.reflect.InvocationHandler getInvocationHandler() {
        return new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                PropertyChangeListener listener = (PropertyChangeListener) WeakReferencedListener.this.weakreference.get();

                if (Object.class.getMethod("equals", Object.class).equals(method)) {
                    return proxy == args[0];
                }

                if (Object.class.getMethod("hashCode").equals(method)) {
                    return WeakReferencedListener.this.hashCode;
                }

                if (listener == null) {
                    if ((args != null) && (args.length == 1) && (args[0] instanceof EventObject)) {
                        EventObject event = EventObject.class.cast(args[0]);
                        Object source = event.getSource();
                        source.getClass()
                                .getMethod("remove" + WeakReferencedListener.this.interfaceClass.getSimpleName(),
                                        WeakReferencedListener.this.interfaceClass).invoke(source, proxy);
                    }
                    return null;
                }

                return method.invoke(listener, args);
            }
        };
    }

}

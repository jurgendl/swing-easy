package org.swingeasy;

/**
 * @author Jurgen
 */
public class EventThreadSafeWrapper<C> {
    public static enum WrapperType {
        JAVASSIST, CGLIB, NONE;
    }

    private static WrapperType type = WrapperType.JAVASSIST;

    public static <C, I> C getSimpleThreadSafeInterface(final Class<C> componentClass, final C component, final Class<I> interfaced) {
        switch (EventThreadSafeWrapper.type) {
            case JAVASSIST:
                try {
                    return org.swingeasy.JavassistEventThreadSafeWrapper.getSimpleThreadSafeInterface(componentClass, component, interfaced);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                    EventThreadSafeWrapper.type = WrapperType.NONE;
                    return component;
                }
            case CGLIB:
                try {
                    return org.swingeasy.CglibEventThreadSafeWrapper.getSimpleThreadSafeInterface(componentClass, component, interfaced);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                    EventThreadSafeWrapper.type = WrapperType.NONE;
                    return component;
                }
            case NONE:
                return component;
        }
        return component;
    }

    public static WrapperType getType() {
        return EventThreadSafeWrapper.type;
    }

    public static void setType(WrapperType type) {
        EventThreadSafeWrapper.type = type;
    }
}

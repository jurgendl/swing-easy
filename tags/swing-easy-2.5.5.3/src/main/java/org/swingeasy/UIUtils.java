package org.swingeasy;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

import org.swingeasy.system.SystemSettings;

/**
 * @author Jurgen
 */
public class UIUtils {
    /**
     * {@link MouseListener} that moves around a {@link Window} when dragged
     */
    public static class MoveMouseListener implements MouseListener, MouseMotionListener {
        Point start_drag;

        Point start_loc;

        JComponent target;

        public MoveMouseListener(JComponent target) {
            this.target = target;
            target.addMouseListener(this);
            target.addMouseMotionListener(this);
        }

        public Window getFrame(Container container) {
            if (container == null) {
                return null;
            }
            if (container instanceof Window) {
                return (Window) container;
            }
            return this.getFrame(container.getParent());
        }

        Point getScreenLocation(MouseEvent e) {
            Point cursor = e.getPoint();
            Point target_location = this.target.getLocationOnScreen();
            return new Point((int) (target_location.getX() + cursor.getX()), (int) (target_location.getY() + cursor.getY()));
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            //
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Point current = this.getScreenLocation(e);
            Point offset = new Point((int) current.getX() - (int) this.start_drag.getX(), (int) current.getY() - (int) this.start_drag.getY());
            Window frame = this.getFrame(this.target);
            Point new_location = new Point((int) (this.start_loc.getX() + offset.getX()), (int) (this.start_loc.getY() + offset.getY()));
            frame.setLocation(new_location);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            //
        }

        @Override
        public void mousePressed(MouseEvent e) {
            this.start_drag = this.getScreenLocation(e);
            Window frame = this.getFrame(this.target);
            if (frame == null) {
                return;
            }
            this.start_loc = frame.getLocation();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //
        }
    }

    protected static class PropertyChangeListenerDelegate implements PropertyChangeListener {
        protected Component weakReferencedComponent;

        protected EComponentI weakReferencedEComponentI;

        public PropertyChangeListenerDelegate(Component component) {
            this.weakReferencedComponent = component;
        }

        public PropertyChangeListenerDelegate(EComponentI component) {
            this.weakReferencedEComponentI = component;
        }

        public Object getDelageting() {
            if (this.weakReferencedComponent != null) {
                return this.weakReferencedComponent;
            } else if (this.weakReferencedEComponentI != null) {
                return this.weakReferencedEComponentI;
            } else {
                return null;
            }
        }

        /**
         * 
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
         */
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (this.weakReferencedComponent != null) {
                Component component = this.weakReferencedComponent;
                if (component != null) {
                    component.setLocale(Locale.class.cast(evt.getNewValue()));
                }
            }
            if (this.weakReferencedEComponentI != null) {
                EComponentI component = this.weakReferencedEComponentI;
                if (component != null) {
                    component.setLocale(Locale.class.cast(evt.getNewValue()));
                }
            }
        }

        /**
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "PropertyChangeListener[" + SystemSettings.LOCALE + "]@" + Integer.toHexString(this.hashCode());
        }

    }

    private static class StaticPropertyChangeListener implements PropertyChangeListener {
        /**
         * 
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
         */
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            UIUtils.setUILanguage(Locale.class.cast(evt.getNewValue()));
        }
    }

    /**
     * UncaughtExceptionHandler delegating to given instance
     */
    public static class UncaughtExceptionHandlerDelegate implements UncaughtExceptionHandler {
        private static UncaughtExceptionHandler delegate;

        public UncaughtExceptionHandlerDelegate() {
            super();
        }

        /**
         * 
         * @see org.swingeasy.UIUtils.UncaughtExceptionHandler#handle(java.lang.Throwable)
         */
        public void handle(Throwable t) {
            UncaughtExceptionHandlerDelegate.delegate.uncaughtException(Thread.currentThread(), t);
        }

        /**
         * 
         * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
         */
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            UncaughtExceptionHandlerDelegate.delegate.uncaughtException(t, e);
        }
    }

    {
        SystemSettings.getSingleton().addPropertyChangeListener(SystemSettings.LOCALE, new StaticPropertyChangeListener());
    }

    static {
        // http://tips4java.wordpress.com/2008/10/25/enter-key-and-button/
        // TODO
        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
    }

    static {
        UIUtils.setUILanguage(null);
    }

    protected static final UIUtils singleton = new UIUtils();

    protected static Map<String, Icon> cachedIcons = new HashMap<String, Icon>();

    protected static Map<String, String> cachedDescriptions = new HashMap<String, String>();

    /**
     * lists all current locale change listeners
     */
    public static void debugLocaleChangeListeners() {
        for (Object o : SystemSettings.getSingleton().propertyChangeSupport.getPropertyChangeListeners()) {
            try {
                PropertyChangeListenerProxy pclp = (PropertyChangeListenerProxy) o;
                if (!org.swingeasy.WeakReferencedListener.isWrapped(pclp.getListener())) {
                    continue;
                }
                Object component = WeakReferencedListener.<PropertyChangeListenerDelegate> unwrap(pclp.getListener()).getReference().getDelageting();
                System.out.println((component == null ? null : component.hashCode()) + " : " + component);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }

    /**
     * gets first displayable {@link JFrame}
     */
    public static JFrame getCurrentFrame() {
        for (Frame frame : Frame.getFrames()) {
            if ((frame instanceof JFrame) && frame.isDisplayable()) {
                return JFrame.class.cast(frame);
            }
        }
        return null;
    }

    /**
     * gets file system description for file type
     */
    public static String getDescriptionForFileType(String ext) {
        try {
            ext = ext.toLowerCase();
            String description = UIUtils.cachedDescriptions.get(ext);
            if (description == null) {
                File createTempFile = File.createTempFile("test", "." + ext);
                description = FileSystemView.getFileSystemView().getSystemTypeDescription(createTempFile);
                UIUtils.cachedDescriptions.put(ext, description);
            }
            return description;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * get filesystem icon for file
     */
    public static Icon getIconForFileType(String ext) {
        try {
            ext = ext.toLowerCase();
            Icon icon = UIUtils.cachedIcons.get(ext);
            if (icon == null) {
                File createTempFile = File.createTempFile("test", "." + ext);
                icon = FileSystemView.getFileSystemView().getSystemIcon(createTempFile);
                UIUtils.cachedIcons.put(ext, icon);
            }
            return icon;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * get rootwindow for a component
     */
    public static Window getRootWindow(Component component) {
        Component root = SwingUtilities.getRoot(component);
        if (root instanceof Window) {
            return (Window) root;
        }
        return null;
    }

    /**
     * prints exception to system error outputstream
     */
    private static void log(Throwable ex) {
        ex.printStackTrace();
    }

    /**
     * makes a window draggable by adding a listener as a {@link MouseListener} and {@link MouseMotionListener} to a {@link JComponent} in that window
     */
    public static MoveMouseListener makeDraggable(JComponent target) {
        return new MoveMouseListener(target);
    }

    /**
     * activate Nimbus look and feel or system look and feel if not java 1.7 (7) or higher
     */
    public static void niceLookAndFeel() {
        try {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); //$NON-NLS-1$
            } catch (Exception ex) {
                UIUtils.systemLookAndFeel();
            }
        } catch (Exception ex) {
            UIUtils.log(ex);
        }
    }

    /**
     * register default uncaught exception handler thats logs exceptions to system error outputstream and quits only if Error
     */
    public static void registerDefaultUncaughtExceptionHandler() {
        UIUtils.registerUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                try {
                    System.out.println(t);
                    UIUtils.log(e);
                    // insert your exception handling code here
                    // or do nothing to make it go away
                } catch (Exception tt) {
                    // don't let the exception get thrown out, will cause infinite looping!
                } catch (Throwable tt) {
                    System.exit(-1);
                }
            }
        });
    }

    /**
     * register component as a locale change listener
     */
    public static boolean registerLocaleChangeListener(final Component component) {
        component.setLocale(SystemSettings.getCurrentLocale());
        SystemSettings.getSingleton().addPropertyChangeListener(SystemSettings.LOCALE,
                WeakReferencedListener.wrap(PropertyChangeListener.class, new PropertyChangeListenerDelegate(component)));
        return true;
    }

    /**
     * register component as a locale change listener
     */
    public static boolean registerLocaleChangeListener(final EComponentI component) {
        for (Object o : SystemSettings.getSingleton().propertyChangeSupport.getPropertyChangeListeners()) {
            try {
                PropertyChangeListenerProxy pclp = (PropertyChangeListenerProxy) o;
                if (!org.swingeasy.WeakReferencedListener.isWrapped(pclp.getListener())) {
                    continue;
                }
                PropertyChangeListenerDelegate reference = WeakReferencedListener.<PropertyChangeListenerDelegate> unwrap(pclp.getListener())
                        .getReference();
                if (reference == null) {
                    continue;
                }
                Object component0 = reference.getDelageting();
                if (component == component0) {
                    return false;
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }

        component.setLocale(SystemSettings.getCurrentLocale());
        SystemSettings.getSingleton().addPropertyChangeListener(SystemSettings.LOCALE,
                WeakReferencedListener.wrap(PropertyChangeListener.class, new PropertyChangeListenerDelegate(component)));
        return true;
    }

    /**
     * register uncaught exception handler
     */
    public static void registerUncaughtExceptionHandler(Class<? extends UncaughtExceptionHandler> clazz) {
        try {
            UIUtils.registerUncaughtExceptionHandler(clazz.newInstance());
        } catch (InstantiationException ex) {
            throw new IllegalArgumentException(ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * register uncaught exception handler
     */
    @SuppressWarnings("unchecked")
    public static void registerUncaughtExceptionHandler(String className) {
        Class<? extends UncaughtExceptionHandler> instance;
        try {
            instance = (Class<? extends UncaughtExceptionHandler>) Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException(ex);
        }
        UIUtils.registerUncaughtExceptionHandler(instance);
    }

    /**
     * register uncaught exception handler
     */
    public static void registerUncaughtExceptionHandler(UncaughtExceptionHandler handler) {
        UncaughtExceptionHandlerDelegate.delegate = handler;
        System.setProperty("sun.awt.exception.handler", UncaughtExceptionHandlerDelegate.class.getName()); //$NON-NLS-1$
    }

    /**
     * sets rounded borders with arc of 20
     * 
     * @see http://java.sun.com/developer/technicalArticles/GUI/translucent_shaped_windows/
     */
    public static void rounded(Window w) {
        UIUtils.rounded(w, 20);
    }

    /**
     * sets rounded borders with given arc
     * 
     * @see http://java.sun.com/developer/technicalArticles/GUI/translucent_shaped_windows/
     */
    public static void rounded(Window w, float arc) {
        if (AWTUtilitiesWrapper.isTranslucencySupported(AWTUtilitiesWrapper.PERPIXEL_TRANSPARENT)) {
            try {
                Shape shape = new RoundRectangle2D.Float(0, 0, w.getWidth(), w.getHeight(), arc, arc);
                AWTUtilitiesWrapper.setWindowShape(w, shape);
            } catch (Exception ex) {
                UIUtils.log(ex);
            }
        }
    }

    /**
     * (re-)shows tooltips earlier (10 milliseconds) and longer (20 seconds)
     */
    public static void setLongerTooltips() {
        ToolTipManager sharedInstance = ToolTipManager.sharedInstance();
        sharedInstance.setReshowDelay(10);
        sharedInstance.setInitialDelay(10);
        sharedInstance.setDismissDelay(20000);
    }

    /**
     * @see http://www.java2s.com/Tutorial/Java/0240__Swing/CustomizingaJFileChooserLookandFeel.htm
     * @see http://www.java2s.com/Tutorial/Java/0240__Swing/CustomizingaJOptionPaneLookandFeel.htm
     * @see http://www.java2s.com/Tutorial/Java/0240__Swing/CustomizingaJColorChooserLookandFeel.htm
     */
    private static final void setUILanguage(Locale locale) {
        UIUtils.setUILanguage(locale, JOptionPane.class);
        UIUtils.setUILanguage(locale, JFileChooser.class);
        UIUtils.setUILanguage(locale, JColorChooser.class);

        // BufferedReader br = new BufferedReader(new InputStreamReader(ListInDialogDemo.class.getClassLoader().getResourceAsStream(
        // "javax/swing/"+simpleClassName+".keys.properties")));
        // String line;
        // while ((line = br.readLine()) != null) {
        // String key = line.split("\t")[0];
        // try {
        // System.out.println(key + "=" + UIManager.getString(key).toString());
        // } catch (Exception ex) {
        // //
        // }
        // }
    }

    private static final void setUILanguage(Locale locale, Class<? extends JComponent> componentClass) {
        String p1 = componentClass.getName().replace('.', '/') + ".keys.properties";
        String p2 = componentClass.getName();
        Locale p3 = locale == null ? SystemSettings.getCurrentLocale() : locale;
        String p4 = componentClass.getSimpleName();
        UIUtils.setUILanguage(p1, p2, p3, p4);
    }

    /**
     * sets localization, expects a properties file with name {prefix}_{locale.toString()}.properties in the directory javax/swing; for all possible
     * keys, see source documentation
     * 
     * @param resource
     * @param baseName
     * @param locale Locale
     * @param prefix
     */
    private static final void setUILanguage(String resource, String baseName, Locale locale, String prefix) {
        if (locale == null) {
            locale = SystemSettings.getCurrentLocale();
        }

        PropertyResourceBundle rb = (PropertyResourceBundle) ResourceBundle.getBundle(baseName, locale);

        try {
            Properties props = new Properties();
            props.load(UIUtils.class.getClassLoader().getResourceAsStream(resource));

            Set<String> missing = new HashSet<String>();

            for (Object k : props.keySet()) {
                String key = (String) k;
                String type = props.getProperty(key);
                String value = null;

                try {
                    value = rb.getString(key);
                } catch (MissingResourceException ex) {
                    //
                }

                if (key.startsWith(prefix + ".") && "String".equals(type)) {
                    if (value == null) {
                        missing.add(key);
                    }
                }
            }

            if (missing.size() > 0) {
                System.err.println("missing translations for " + baseName + " for locale " + locale);

                for (String key : missing) {
                    System.err.println(key + "=" + UIManager.get(key));
                }
            }
        } catch (Exception ex) {
            // kon niet controleren
            ex.printStackTrace();
        }

        final Enumeration<String> keys = rb.getKeys();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            UIManager.put(key.toString(), rb.getString(key));
        }
    }

    /**
     * give window a shape
     * 
     * @see http://java.sun.com/developer/technicalArticles/GUI/translucent_shaped_windows/
     */
    public static void shaped(Window w, Shape shape) {
        if (AWTUtilitiesWrapper.isTranslucencySupported(AWTUtilitiesWrapper.PERPIXEL_TRANSPARENT)) {
            try {
                AWTUtilitiesWrapper.setWindowShape(w, shape);
            } catch (Exception ex) {
                UIUtils.log(ex);
            }
        }
    }

    /**
     * activate system look and feel
     */
    public static void systemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            UIUtils.log(ex);
        }
    }

    /**
     * sets translucency with default value of .93
     * 
     * @see http://java.sun.com/developer/technicalArticles/GUI/translucent_shaped_windows/
     */
    public static void translucent(Window w) {
        UIUtils.translucent(w, .93f);
    }

    /**
     * sets window translucency value
     * 
     * @see http://java.sun.com/developer/technicalArticles/GUI/translucent_shaped_windows/
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static boolean translucent(Window w, Float f) {
        try {
            if (AWTUtilitiesWrapper.isTranslucencySupported(Enum.valueOf(
                    (Class<? extends Enum>) Class.forName("com.sun.awt.AWTUtilities$Translucency"), "TRANSLUCENT"))) {
                try {
                    AWTUtilitiesWrapper.setWindowOpacity(w, f);
                    return true;
                } catch (Exception ex) {
                    UIUtils.log(ex);
                }
            }
        } catch (Exception ex) {
            UIUtils.log(ex);
        }
        return false;
    }

    /**
     * singleton
     */
    private UIUtils() {
        super();
    }
}

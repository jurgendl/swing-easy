package org.swingeasy;

import java.awt.Container;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

/**
 * @see http://java.sun.com/developer/technicalArticles/GUI/translucent_shaped_windows/
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

    /**
     * interface UncaughtExceptionHandler
     */
    public static interface UncaughtExceptionHandler {
        public void handle(Throwable t);
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
        @Override
        public void handle(Throwable t) {
            UncaughtExceptionHandlerDelegate.delegate.handle(t);
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
     * prints exception to system error outputstream
     */
    private static void log(Throwable ex) {
        ex.printStackTrace();
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
            public void handle(Throwable t) {
                try {
                    UIUtils.log(t);
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
     */
    public static void rounded(Window w) {
        if (AWTUtilitiesWrapper.isTranslucencySupported(AWTUtilitiesWrapper.PERPIXEL_TRANSPARENT)) {
            try {
                Shape shape = new RoundRectangle2D.Float(0, 0, w.getWidth(), w.getHeight(), 20, 20);
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
     */
    public static void translucent(Window w) {
        UIUtils.translucent(w, .93f);
    }

    /**
     * sets window translucency value
     */
    @SuppressWarnings("restriction")
    public static void translucent(Window w, Float f) {
        if (AWTUtilitiesWrapper.isTranslucencySupported(com.sun.awt.AWTUtilities.Translucency.TRANSLUCENT)) {
            try {
                AWTUtilitiesWrapper.setWindowOpacity(w, f);
            } catch (Exception ex) {
                UIUtils.log(ex);
            }
        }
    }
}

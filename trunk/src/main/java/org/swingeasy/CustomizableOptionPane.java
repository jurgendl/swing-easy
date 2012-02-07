package org.swingeasy;

import java.awt.Component;
import java.awt.HeadlessException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * @author Jurgen
 */
public class CustomizableOptionPane {
    private static class CustomizableOptionPaneImpl extends JOptionPane {
        /** serialVersionUID */
        private static final long serialVersionUID = 6539025260851538675L;

        private static int _styleFromMessageType(int messageType) {
            // thank you for private methods but no use setting this one anything but private
            try {
                Method m = JOptionPane.class.getDeclaredMethod("styleFromMessageType", Integer.TYPE);
                m.setAccessible(true);
                return (Integer) m.invoke(null, messageType);
            } catch (SecurityException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }

        @SuppressWarnings({ "deprecation" })
        public static ResultType showDialog(Component parentComponent, Object message, String title, MessageType messageType, OptionType optionType,
                Icon icon, OptionPaneCustomizer customizer) throws HeadlessException {
            CustomizableOptionPaneImpl pane = new CustomizableOptionPaneImpl(message, messageType.code, optionType.code, icon, null, null);
            pane.setInitialValue(null);
            pane.setComponentOrientation(((parentComponent == null) ? JOptionPane.getRootFrame() : parentComponent).getComponentOrientation());

            int style = CustomizableOptionPaneImpl._styleFromMessageType(messageType.code);
            JDialog dialog = pane._createDialog(parentComponent, title, style);

            if (customizer != null) {
                customizer.customize(parentComponent, messageType, optionType, pane, dialog);
            }

            pane.selectInitialValue();
            dialog.show();
            dialog.dispose();

            Object selectedValue = pane.getValue();

            if (selectedValue == null) {
                return ResultType.CLOSED;
            }

            if (selectedValue instanceof Integer) {
                int counter = Integer.class.cast(selectedValue).intValue();
                return optionType.getResultType(counter);
            }

            return ResultType.CLOSED;
        }

        private CustomizableOptionPaneImpl(Object message, int messageType, int optionType, Icon icon, Object[] options, Object initialValue) {
            super(message, messageType, optionType, icon, options, initialValue);
        }

        private JDialog _createDialog(Component parentComponent, String title, int style) throws HeadlessException {
            // thank you for private methods but no use setting this one anything but private
            try {
                Method m = JOptionPane.class.getDeclaredMethod("createDialog", Component.class, String.class, Integer.TYPE);
                m.setAccessible(true);
                return (JDialog) m.invoke(this, parentComponent, title, style);
            } catch (SecurityException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static ResultType showDialog(Component parentComponent, Object message, String title, MessageType messageType, OptionType optionType,
            Icon icon, OptionPaneCustomizer customizer) throws HeadlessException {
        return CustomizableOptionPaneImpl.showDialog(parentComponent, message, title, messageType, optionType, icon, customizer);
    }
}

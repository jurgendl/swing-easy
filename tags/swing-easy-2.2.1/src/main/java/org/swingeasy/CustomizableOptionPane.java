package org.swingeasy;

import java.awt.Component;
import java.awt.HeadlessException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * see {@link CustomizableOptionPane#showDialog(Component, JComponent, String, MessageType, OptionType, Icon, OptionPaneCustomizer)}
 * 
 * @author Jurgen
 */
public class CustomizableOptionPane {
    private static class CustomizableOptionPaneImpl extends JOptionPane {
        /** serialVersionUID */
        private static final long serialVersionUID = 6539025260851538675L;

        private static Method styleFromMessageTypeMethod;

        private static Method createDialogMethod;

        private static int _styleFromMessageType(int messageType) {
            return CustomizableOptionPaneImpl.invoke(CustomizableOptionPaneImpl.getStyleFromMessageTypeMethod(), Integer.class, null, messageType);
        }

        private static Method getStyleFromMessageTypeMethod() {
            // thank you for private methods but no use setting this one anything but private
            if (CustomizableOptionPaneImpl.styleFromMessageTypeMethod == null) {
                try {
                    CustomizableOptionPaneImpl.styleFromMessageTypeMethod = JOptionPane.class.getDeclaredMethod("styleFromMessageType", Integer.TYPE);
                    CustomizableOptionPaneImpl.styleFromMessageTypeMethod.setAccessible(true);
                } catch (SecurityException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchMethodException ex) {
                    throw new RuntimeException(ex);
                } catch (IllegalArgumentException ex) {
                    throw new RuntimeException(ex);
                }
            }
            return CustomizableOptionPaneImpl.styleFromMessageTypeMethod;
        }

        @SuppressWarnings("unchecked")
        private static <T> T invoke(Method method, @SuppressWarnings("unused") Class<T> returnClass, Object obj, Object... params) {
            try {
                return (T) method.invoke(obj, params);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }

        private static ResultType showDialog(Component parentComponent, JComponent component, String title, MessageType messageType,
                OptionType optionType, Icon icon, OptionPaneCustomizer customizer) throws HeadlessException {
            CustomizableOptionPaneImpl pane = new CustomizableOptionPaneImpl(component, messageType.code, optionType.code, icon, null, null);
            pane.setInitialValue(null);
            pane.setComponentOrientation(((parentComponent == null) ? JOptionPane.getRootFrame() : parentComponent).getComponentOrientation());

            int style = CustomizableOptionPaneImpl._styleFromMessageType(messageType.code);
            JDialog dialog = pane._createDialog(parentComponent, title, style);

            if (customizer != null) {
                customizer.customize(parentComponent, messageType, optionType, pane, dialog);
            }

            pane.selectInitialValue();
            dialog.setVisible(true);
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
            return CustomizableOptionPaneImpl.invoke(this.getCreateDialogMethod(), JDialog.class, this, parentComponent, title, style);
        }

        private Method getCreateDialogMethod() {
            // thank you for private methods but no use setting this one anything but private
            try {
                CustomizableOptionPaneImpl.createDialogMethod = JOptionPane.class.getDeclaredMethod("createDialog", Component.class, String.class,
                        Integer.TYPE);
                CustomizableOptionPaneImpl.createDialogMethod.setAccessible(true);
                return CustomizableOptionPaneImpl.createDialogMethod;
            } catch (SecurityException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * show customizable (by {@link OptionPaneCustomizer}) dialog with {@link JComponent} as content, uses enums as parameters and return value
     * 
     * @param parentComponent
     * @param component
     * @param title
     * @param messageType
     * @param optionType
     * @param icon
     * @param customizer
     * 
     * @return
     * 
     * @throws HeadlessException
     */
    public static ResultType showDialog(Component parentComponent, JComponent component, String title, MessageType messageType,
            OptionType optionType, Icon icon, OptionPaneCustomizer customizer) throws HeadlessException {
        return CustomizableOptionPaneImpl.showDialog(parentComponent, component, title, messageType, optionType, icon, customizer);
    }
}

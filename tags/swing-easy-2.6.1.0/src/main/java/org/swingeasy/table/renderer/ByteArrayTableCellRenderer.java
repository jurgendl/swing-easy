package org.swingeasy.table.renderer;

import java.util.Locale;

import javax.swing.table.DefaultTableCellRenderer;

import org.swingeasy.EComponentI;

/**
 * @author Jurgen
 */
public class ByteArrayTableCellRenderer extends DefaultTableCellRenderer.UIResource implements EComponentI {
    private static final long serialVersionUID = 393779263932701309L;

    private static final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            ByteArrayTableCellRenderer.toHex(hexChars, v, j);
        }
        return new String(hexChars);
    }

    public static String bytesToHex(Byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            ByteArrayTableCellRenderer.toHex(hexChars, v, j);
        }
        return new String(hexChars);
    }

    public static void toHex(char[] hexChars, int v, int j) {
        hexChars[j * 2] = ByteArrayTableCellRenderer.hexArray[v >>> 4];
        hexChars[(j * 2) + 1] = ByteArrayTableCellRenderer.hexArray[v & 0x0F];
    }

    public ByteArrayTableCellRenderer() {
        super();
    }

    /**
     * 
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
    }

    /**
     * 
     * @see javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
     */
    @Override
    protected void setValue(Object value) {
        if (value instanceof byte[]) {
            byte[] ba = (byte[]) value;
            this.setText(ByteArrayTableCellRenderer.bytesToHex(ba));
        } else if (value instanceof Byte[]) {
            Byte[] ba = (Byte[]) value;
            this.setText(ByteArrayTableCellRenderer.bytesToHex(ba));
        } else {
            this.setText((value == null) ? "" : String.valueOf(value)); //$NON-NLS-1$
        }
    }
}
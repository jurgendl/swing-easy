package org.swingeasy.form;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.swingeasy.ELabel;

/**
 * @author Jurgen
 */
public class FormBuilder {
    public static enum HAlign {
        left, right, center;
    }

    public static enum VAlign {
        top, bottom, right, center;
    }

    protected static final String UNRELATED = "10px";

    protected final Container container;

    protected final Integer cols;

    protected boolean initialized = false;

    protected boolean debug = false;

    protected String minimum = "25pt";

    /**
     * absoluut: bv 50,50,*,50 = 50 pixels,50 pixels,de rest,50 pixels<br>
     * relatief: bv 1,2,3,4 = 1x, 2x, 3x, 4x grootte<br>
     * wordt ook door {@link #setColumnLayout(String)} opgevuld
     */
    protected final String[] columnWidths;

    public FormBuilder(Container container, int cols) {
        this.container = container;
        this.cols = cols;
        this.columnWidths = null;
    }

    public FormBuilder(Container container, String[] columnWidths) {
        this.container = container;
        this.cols = null;
        this.columnWidths = columnWidths;
    }

    public FormBuilder(int cols) {
        this(new JPanel(), cols);
    }

    public FormBuilder(String[] columnWidths) {
        this(new JPanel(), columnWidths);
    }

    public void addComponent(String label, JComponent component) {
        this.addComponent(label, component, 1);
    }

    public void addComponent(String label, JComponent component, int colspan) {
        this.addComponent(label, component, colspan, 1);
    }

    public void addComponent(String label, JComponent component, int colspan, int rowspan) {
        this.addComponent(label, component, colspan, rowspan, null, null);
    }

    public void addComponent(String label, JComponent component, int colspan, int rowspan, HAlign halign, VAlign valign) {
        this.debug(label);
        this.addToContainer(new ELabel(label), "");
        StringBuilder sb = new StringBuilder("growx, growy").append(", spanx ").append((colspan * 2) - 1).append(", spany ").append(rowspan);
        if (halign != null) {
            sb.append(", alignx ").append(halign.name());
        }
        if (valign != null) {
            sb.append(", aligny ").append(valign.name());
        }
        this.addToContainer(component, this.debug(sb.toString()));
    }

    public void addTitle(String title) {
        this.addTitle(title, 1);
    }

    public void addTitle(String title, int colspan) {
        this.debug(title);
        this.addToContainer(new TitledLine(title), this.debug("spanx " + (colspan * 2)));
    }

    protected void addToContainer(Component component, String constraint) {
        if (!this.initialized) {
            this.container.setLayout(new MigLayout(this.debug(this.getMigLayoutContraints()), this.debug(this.getMigLayoutColumnContraints()), this
                    .debug(this.getMigLayoutRowContraints())));
            this.initialized = true;
        }
        this.container.add(component, constraint);
    }

    protected <T> T debug(T object) {
        if (this.debug) {
            System.out.println(object);
        }

        return object;
    }

    protected String[] getColumnWidths() {
        return this.columnWidths;
    }

    protected Container getContainer() {
        return this.container;
    }

    /**
     * effectief aantal kolommen = columnWidths.length wanneer die niet null is (dus voorrang) of cols dewelke niet null kan zijn
     */
    protected int getEffectiveColumnCount() {
        return this.columnWidths == null ? (this.cols == null ? 2 : this.cols) : this.columnWidths.length;
    }

    /**
     * miglayout column-constraints
     */
    protected String getMigLayoutColumnContraints() {
        int countStars = 0;
        int colnr = this.getEffectiveColumnCount();
        String[] grows = new String[colnr]; // om relatief velden tov elkaar te laten groeien (bv [1,2,3] (units) of [20,60,20] (percentages)
        String[] sizes = new String[colnr];
        if ((this.columnWidths != null) && (this.columnWidths.length > 0)) {
            for (String i : this.columnWidths) {
                if ("*".equals(i)) {
                    countStars++;
                }
            }
            if (countStars == 0) {
                // relatieve nummers
                for (int i = 0; i < this.columnWidths.length; i++) {
                    grows[i] = ",grow " + Integer.parseInt(this.columnWidths[i]);
                    sizes[i] = "," + this.minimum + "::";
                }
            } else {
                // absolute nummers (pixels) en de rest relatief en evenredig
                for (int i = 0; i < this.columnWidths.length; i++) {
                    if (!"*".equals(this.columnWidths[i])) {
                        grows[i] = "";
                        sizes[i] = "," + Integer.parseInt(this.columnWidths[i]) + "pt!";
                    } else {
                        grows[i] = ",grow " + String.valueOf(100 / countStars);
                        sizes[i] = "";
                    }
                }
            }
        } else {
            for (int i = 0; i < grows.length; i++) {
                grows[i] = ",grow 100";
                sizes[i] = "," + this.minimum + "::";
            }
        }

        // voor elke kolom (label & component paar)
        StringBuilder cc = new StringBuilder();
        for (int i = 0; i < colnr; i++) {
            // label met related gap (gerelateerd met component)
            cc.append("[min!]").append("rel");
            // component met unrelated gap (niet gerelateerd met volgend veld)
            cc.append("[shrink,fill").append(grows[i]).append(sizes[i]).append("]").append(FormBuilder.UNRELATED);
            // enkel voor de duidelijkheid om label&component pairs (=kolommen) duidelijker te laten uitkomen
            cc.append(" ");
        }
        return cc.toString();
    }

    /**
     * miglayout layout-constraints
     */
    protected String getMigLayoutContraints() {
        StringBuilder lc = new StringBuilder("insets ")// insets...
                .append(FormBuilder.UNRELATED).append(" ")// ...top: unrelated
                .append(FormBuilder.UNRELATED).append(" ") // ...left: unrelated
                .append(0).append(" ")// ...bottom: geen
                .append(FormBuilder.UNRELATED)// ...right: unrelated
                .append(", fillx") // componenten nemen altijd volledige ruimte in horizontaal
                .append(", wrap ").append(2 * this.cols); // wrap achter # (maw 2x aantal cols)
        if (this.debug) {
            lc.append(", debug");
        }
        return lc.toString();
    }

    /**
     * miglayout row-constraints: bv ruimte tussen rows
     */
    protected String getMigLayoutRowContraints() {
        // geen
        return "";
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}

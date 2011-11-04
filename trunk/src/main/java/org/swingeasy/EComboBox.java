package org.swingeasy;

import java.util.Arrays;

import javax.swing.JComboBox;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.EventComboBoxModel;

public class EComboBox<T> extends JComboBox {
    private static final long serialVersionUID = -3602504810131193505L;

    public EComboBox(EComboBoxConfig cfg, T... values) {
        EventList<T> list = GlazedLists.eventList(Arrays.asList(values));
        EventComboBoxModel<T> model = new EventComboBoxModel<T>(list);
        this.setModel(model);
    }
}

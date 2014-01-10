package org.swingeasy.table.editor;

/**
 * @author Jurgen
 */
public class TimeTableCellEditor extends DateTimeTableCellEditor {
    private static final long serialVersionUID = 7036312170850978563L;

    public TimeTableCellEditor() {
        super(DateTimeTableCellEditor.Type.TIME);
    }
}

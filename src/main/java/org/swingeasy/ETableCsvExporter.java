package org.swingeasy;

/**
 * @author Jurgen
 */
public class ETableCsvExporter<T> extends ETableExporterImpl<T> {
    /**
     * 
     * @see org.swingeasy.ETableExporterImpl#exportString(org.swingeasy.ETable)
     */
    @Override
    public String exportString(ETable<T> table) {
        StringBuilder sb = new StringBuilder();
        for (ETableRecord<T> record : table) {
            for (int column = 0; column < record.size(); column++) {
                if (Number.class.isAssignableFrom(table.getHeaders().getColumnClass(column))) {
                    sb.append(record.getStringValue(column));
                } else {
                    sb.append("'").append(record.getStringValue(column)).append("'");
                }
                if (column < (record.size() - 1)) {
                    sb.append(",");
                }
            }
            sb.append(EComponentPopupMenu.newline);
        }
        return sb.toString();
    }

    /**
     * 
     * @see org.swingeasy.ETableExporter#getAction()
     */
    @Override
    public String getAction() {
        return "csv-export";
    }

    /**
     * 
     * @see org.swingeasy.ETableExporter#getFileExtension()
     */
    @Override
    public String getFileExtension() {
        return "csv";
    }
}

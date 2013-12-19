package cz.vutbr.fit.pdb.nichcz.gui.temporal;

import cz.vutbr.fit.pdb.nichcz.model.temporal.LoggingHistoryEntity;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.List;

/**
 * User: Petr Přikry
 * Date: 19.12.13
 * Time: 1:20
 *
 * Trida modelu pro tabulku zobrazujici produkci jednotlivych spolecnosti.
 */
public class ProductionTableModel implements TableModel {

    List<LoggingHistoryEntity> entities;
    int colCount, rowCount;
    private static final String[] colNames = {"Company name", "Amount of logs"};


    public List<LoggingHistoryEntity> getEntities() {
        return entities;
    }


    public ProductionTableModel(List<LoggingHistoryEntity> entities) {
        this.entities = entities;
        this.colCount = colNames.length;
        this.rowCount = entities.size();
    }


    @Override
    public int getRowCount() {
        return rowCount;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getColumnCount() {
        return colCount;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getColumnName(int i) {
        return colNames[i];
    }

    @Override
    public Class<?> getColumnClass(int i) {
        if (i == 0) {
            return String.class;
        } else if (i == 1) {
            return Integer.class;
        } else {
            return Long.class;
        }
    }

    @Override
    public boolean isCellEditable(int i, int i2) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return entities.get(rowIndex).getCompanyName();
        } else if (columnIndex == 1) {
            return entities.get(rowIndex).getLogsPerDay();
        } else {
            return entities.get(rowIndex).getCompanyId();
        }
    }

    @Override
    public void setValueAt(Object o, int i, int i2) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addTableModelListener(TableModelListener tableModelListener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeTableModelListener(TableModelListener tableModelListener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

package cz.vutbr.fit.pdb.nichcz.gui.temporal;

import cz.vutbr.fit.pdb.nichcz.model.temporal.LoggingHistoryEntity;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nich
 * Date: 16.12.13
 * Time: 23:21
 * To change this template use File | Settings | File Templates.
 */
public class LoggingHistoryTableModel implements TableModel {

    List<LoggingHistoryEntity> entities;
    int colCount, rowCount;
    private static final String[] colNames = {"Id", "Company name", "Logging area", "Logs per day", "ValidFrom", "ValidTo", "Company id"};


    public List<LoggingHistoryEntity> getEntities() {
        return entities;
    }


    public LoggingHistoryTableModel(List<LoggingHistoryEntity> entities) {
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
            return Long.class;
        } else if (i == 1) {
            return String.class;
        } else if (i == 2) {
            return String.class;
        } else if (i == 3) {
            return Integer.class;
        } else if (i == 4) {
            return Date.class;
        } else if (i == 5) {
            return Date.class;
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
            return entities.get(rowIndex).getId();
        } else if (columnIndex == 1) {
            return entities.get(rowIndex).getCompanyName();
        } else if (columnIndex == 2) {
            return entities.get(rowIndex).getLoggingArea();
        } else if (columnIndex == 3) {
            return entities.get(rowIndex).getLogsPerDay();
        } else if (columnIndex == 4) {
            return entities.get(rowIndex).getValidFrom();
        } else if (columnIndex == 5) {
            return entities.get(rowIndex).getValidTo();
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

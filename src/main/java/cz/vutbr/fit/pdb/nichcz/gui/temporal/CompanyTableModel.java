package cz.vutbr.fit.pdb.nichcz.gui.temporal;

import cz.vutbr.fit.pdb.nichcz.model.temporal.CompanyEntity;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nich
 * Date: 15.12.13
 * Time: 13:42
 * To change this template use File | Settings | File Templates.
 */
public class CompanyTableModel implements TableModel {

    List<CompanyEntity> entities;
    int colCount, rowCount;
    private static final String[] colNames = {"Id", "Name", "ValidFrom", "ValidTo"};


    public List<CompanyEntity> getEntities() {
        return entities;
    }


    public CompanyTableModel(List<CompanyEntity> entities) {
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
            return Date.class;
        } else if (i == 3) {
            return Date.class;
        } else {
            return null;
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
            return entities.get(rowIndex).getName();
        } else if (columnIndex == 2) {
            return entities.get(rowIndex).getValidFrom();
        } else if (columnIndex == 3) {
            return entities.get(rowIndex).getValidTo();
        } else {
            return null;
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

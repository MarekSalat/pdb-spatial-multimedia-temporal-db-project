package cz.vutbr.fit.pdb.nichcz.gui.spatial;

import cz.vutbr.fit.pdb.nichcz.model.spatial.SpatialEntity;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.Date;
import java.util.List;

/**
 * User: Petr Přikryl
 * Date: 14.12.13
 * Time: 19:24
 *
 * Trida modelu tabulky. Slouzi k mapovani entit z databaze do tabulky v GUI.
 */
public class SpatialTableModel implements TableModel {

    List<SpatialEntity> entities;
    int colCount, rowCount;
    private static final String[] colNames = {"Id", "ObjectType", "Name", "ValidFrom", "ValidTo"};


    public List<SpatialEntity> getEntities() {
        return entities;
    }


    public SpatialTableModel(List<SpatialEntity> entities) {
        this.entities = entities;
        this.colCount = colNames.length;
        this.rowCount = entities.size();
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return colCount;
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
            return SpatialEntity.TYPE.class;
        } else if (i == 2) {
            return String.class;
        } else if (i == 3) {
            return Date.class;
        } else if (i == 4) {
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
            return entities.get(rowIndex).getObjectType();
        } else if (columnIndex == 2) {
            return entities.get(rowIndex).getName();
        } else if (columnIndex == 3) {
            return entities.get(rowIndex).getValidFrom();
        } else if (columnIndex == 4) {
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

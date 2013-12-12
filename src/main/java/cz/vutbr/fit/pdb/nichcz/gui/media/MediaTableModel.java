package cz.vutbr.fit.pdb.nichcz.gui.media;

import cz.vutbr.fit.pdb.nichcz.model.media.MediaEntity;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 7.12.13
 * Time: 15:44
 * To change this template use File | Settings | File Templates.
 */
public class MediaTableModel  implements TableModel {

    List<MediaEntity> entities;
    int colCount, rowCount;
    private static final String[] colNames = {"ID", "Name"};


    public List<MediaEntity> getEntities() {
        return entities;
    }


    public MediaTableModel(List<MediaEntity> entities) {
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
    public String getColumnName(int columnIndex) {
        return colNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Long.class;
        } else if (columnIndex == 1) {
            return String.class;
        } else {
            return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return entities.get(rowIndex).getId();
        } else if (columnIndex == 1) {
            return entities.get(rowIndex).getName();
        } else {
            return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        /*if (columnIndex == 0) {
            entities.get(rowIndex).setId((Long) aValue);
        } else */
        if (columnIndex == 1) {
            entities.get(rowIndex).setName((String) aValue);
            entities.get(rowIndex).setNameChanged(true);
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

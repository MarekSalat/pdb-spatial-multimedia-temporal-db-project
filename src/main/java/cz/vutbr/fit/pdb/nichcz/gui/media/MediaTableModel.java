package cz.vutbr.fit.pdb.nichcz.gui.media;

import cz.vutbr.fit.pdb.nichcz.model.media.MediaEntity;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.sql.SQLException;
import java.util.List;

/**
 * User: Michal Pracuch
 * Date: 7.12.13
 * Time: 15:44
 *
 * Trida modelu tabulky. Slouzi k mapovani entit z databaze do tabulky v GUI.
 */
public class MediaTableModel implements TableModel {

    private List<MediaEntity> entities;
    private int colCount, rowCount;
    private static final String[] colNames = {"Index", "ID", "Name", /*"Format",*/ "Width", "Height"};

    private static int i = 0;

    public static final int COL_INDEX = i++;
    public static final int COL_ID = i++;
    public static final int COL_NAME = i++;
    //    public static final int COL_FORMAT = i++;
    public static final int COL_WIDTH = i++;
    public static final int COL_HEIGHT = i++;


    /**
     * Getter seznamu entit.
     * @return Vraci seznam entit.
     */
    public List<MediaEntity> getEntities() {
        return entities;
    }


    /**
     * Vytvori MediaTableModel.
     * @param entities Seznam entit z databaze.
     */
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
        if (columnIndex == COL_INDEX) {
            return Integer.class;
        } else if (columnIndex == COL_ID) {
            return Long.class;
        } else if (columnIndex == COL_NAME) {
            return String.class;
//        } else if (columnIndex == COL_FORMAT) {
//            return String.class;
        } else if (columnIndex == COL_WIDTH) {
            return Integer.class;
        } else if (columnIndex == COL_HEIGHT) {
            return Integer.class;
        } else {
            return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == COL_NAME;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            if (columnIndex == COL_INDEX) {
                return rowIndex + 1;
            } else if (columnIndex == COL_ID) {
                return entities.get(rowIndex).getId();
            } else if (columnIndex == COL_NAME) {
                return entities.get(rowIndex).getName();
//            } else if (columnIndex == COL_FORMAT) {
//                return entities.get(rowIndex).getImgProxy().getFormat();
            } else if (columnIndex == COL_WIDTH) {
                return entities.get(rowIndex).getImgProxy().getWidth();
            } else if (columnIndex == COL_HEIGHT) {
                return entities.get(rowIndex).getImgProxy().getHeight();
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == COL_NAME) {
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

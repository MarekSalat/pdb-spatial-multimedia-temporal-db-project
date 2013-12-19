package cz.vutbr.fit.pdb.nichcz.gui.temporal;

import cz.vutbr.fit.pdb.nichcz.model.temporal.SameLogersEntity;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * User: Petr Přikryl
 * Date: 19.12.13
 * Time: 0:25
 *
 * Trida modelu pro tabulku zobrazujici spolecnosti, ktere tezi ve stejne oblasti ve stejne casove periode.
 */
public class SameLogersModel implements TableModel {

    DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    List<SameLogersEntity> entities;
    int colCount, rowCount;
    private static final String[] colNames = {"Logging area",
            "C1 = 1. company",
            "C2 = 2. company",
            "C1 logs/day",
            "C2 logs/day",
            "C1 valid",
            "C2 valid"
    };

    public List<SameLogersEntity> getEntities() {
        return entities;
    }

    public SameLogersModel(List<SameLogersEntity> entities) {
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
            return String.class;
        } else if (i == 2) {
            return String.class;
        } else if (i == 3) {
            return Integer.class;
        } else if (i == 4) {
            return Integer.class;
        } else if (i == 5) {
            return String.class;
        } else if (i == 6) {
            return String.class;
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
            return entities.get(rowIndex).getLoggingArea();
        } else if (columnIndex == 1) {
            return entities.get(rowIndex).getFirstCompany();
        } else if (columnIndex == 2) {
            return entities.get(rowIndex).getSecondCompany();
        } else if (columnIndex == 3) {
            return entities.get(rowIndex).getFirstLoggsPerDay();
        } else if (columnIndex == 4) {
            return entities.get(rowIndex).getSecondLoggsPerDay();
        } else if (columnIndex == 5) {
            return df.format(entities.get(rowIndex).getFirstValidFrom()) +
                   " - " +
                   df.format(entities.get(rowIndex).getFirstValidTo());
        } else if (columnIndex == 6) {
            return df.format(entities.get(rowIndex).getSecondValidFrom()) +
                    " - " +
                    df.format(entities.get(rowIndex).getSecondValidTo());
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

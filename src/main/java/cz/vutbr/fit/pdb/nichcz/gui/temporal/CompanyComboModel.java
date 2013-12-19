package cz.vutbr.fit.pdb.nichcz.gui.temporal;

import cz.vutbr.fit.pdb.nichcz.model.temporal.CompanyEntity;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nich
 * Date: 16.12.13
 * Time: 23:53
 * To change this template use File | Settings | File Templates.
 */
public class CompanyComboModel implements ComboBoxModel<CompanyEntity> {

    List<CompanyEntity> entityList;
    CompanyEntity selected;

    CompanyComboModel(List<CompanyEntity> entityList) {
        super();
        this.entityList = entityList;
    }

    @Override
    public void setSelectedItem(Object o) {
        selected = (CompanyEntity) o;
    }

    @Override
    public Object getSelectedItem() {
        return selected;
    }

    @Override
    public int getSize() {
        return entityList.size();
    }

    @Override
    public CompanyEntity getElementAt(int i) {
        return entityList.get(i);
    }

    @Override
    public void addListDataListener(ListDataListener listDataListener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeListDataListener(ListDataListener listDataListener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}



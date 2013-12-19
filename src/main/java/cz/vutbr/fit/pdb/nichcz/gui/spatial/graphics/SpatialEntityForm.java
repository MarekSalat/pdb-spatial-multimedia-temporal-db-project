package cz.vutbr.fit.pdb.nichcz.gui.spatial.graphics;

import cz.vutbr.fit.pdb.nichcz.context.Context;
import cz.vutbr.fit.pdb.nichcz.gui.BaseFrame;
import cz.vutbr.fit.pdb.nichcz.model.spatial.SpatialDBMapper;
import cz.vutbr.fit.pdb.nichcz.model.spatial.SpatialEntity;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * User: Marek Salát
 * Date: 8.12.13
 * Time: 14:13
 *
 * Formular pro editaci vlastnosti prostorovych entit.
 */
public class SpatialEntityForm extends BaseFrame{

    private final SpatialDBMapper mapper;
    private SpatialEntity entity;
    private JPanel panel;

    private JTextField nameTextField;
    private JTextField adminTextField;
    private JTextField ownerTextField;
    private JTextPane noteTextPane;
    private JLabel name;
    private JLabel admin;
    private JLabel owner;
    private JLabel note;
    private JLabel typeValue;
    // private JLabel validFromValue;
    // private JLabel validToValue;
    private JLabel modifiedVaule;
    private JLabel validFrom;
    private JLabel validTo;
    private JLabel modified;
    private JLabel type;
    private JTextField categoryTextField;
    private JLabel category;
    private JButton save;
    private JButton delete;
    private JXDatePicker validFromValue;
    private JXDatePicker validToValue;
    private JXDatePicker deleteFromValue;
    private JXDatePicker deleteToValue;

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    public interface OnDeleteListener {
        void onDelete(SpatialEntity entity);
    };
    public interface OnSaveListener {
        void onSave(SpatialEntity entity);
    };

    public SpatialEntityForm(Context ctx, SpatialDBMapper mapper, SpatialEntity spatialEntity) {
        super(ctx);
        this.mapper = mapper;
        this.entity = spatialEntity;
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonSave();
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonDelete();
            }
        });

        validFromValue.setFormats(new String[]{"d.MM.yyyy"});
        validToValue.setFormats(new String[]{"d.MM.yyyy"});
        deleteFromValue.setFormats(new String[]{"d.MM.yyyy"});
        deleteToValue.setFormats(new String[]{"d.MM.yyyy"});

        setEntity(spatialEntity);
    }

    public SpatialEntityForm(Context context, SpatialDBMapper mapper) {
        this(context, mapper, null);
    }

    private ArrayList<OnDeleteListener> onDeleteListeners = new ArrayList<>();
    public void addListener(OnDeleteListener onDeleteListener){
        onDeleteListeners.add(onDeleteListener);
    }
    public void removeListener(OnDeleteListener onDeleteListener){
        onDeleteListeners.remove(onDeleteListener);
    }

    private ArrayList<OnSaveListener> onSaveListeners = new ArrayList<>();
    public void addListener(OnSaveListener onSaveListener){
        onSaveListeners.add(onSaveListener);
    }
    public void removeListener(OnSaveListener onSaveListener){
        onSaveListeners.remove(onSaveListener);
    }

    private void buttonDelete() {
        if (deleteFromValue.getDate() == null || deleteToValue.getDate() == null) {
            mapper.delete(entity);
        }
        else {

            if (deleteFromValue.getDate().getTime() >= deleteToValue.getDate().getTime()) {
                JOptionPane.showMessageDialog(panel, "Delete from must be lesser than delete to.",
                        "Date error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date from =  deleteFromValue.getDate();
            Date to =  deleteToValue.getDate();
            mapper.temporalDelete(entity, from, to);

        }

        for(OnDeleteListener listener: onDeleteListeners){
            listener.onDelete(entity);
        }

        entity = null;
        setData(null);
    }


    public SpatialEntity getEntity() {
        return entity;
    }

    public void setEntity(SpatialEntity newEntity){
        if(this.entity != null && isModified(this.entity)){
            getData(this.entity);
        }

        setData(newEntity);
        this.entity = newEntity;
        if(newEntity == null){
            save.setEnabled(false);
            delete.setEnabled(false);
        }
        else {
            save.setEnabled(true);
            delete.setEnabled(true);
        }
    }

    private void buttonSave() {
        if (validFromValue.getDate() == null || validToValue.getDate() == null) {
            JOptionPane.showMessageDialog(panel, "Valid from and valid to must be in date format dd.mm.yyyy",
                    "Date error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (validFromValue.getDate().getTime() >= validToValue.getDate().getTime()) {
            JOptionPane.showMessageDialog(panel, "Valid from must be lesser than valid to.",
                    "Date error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        getData(entity);
        mapper.save(entity);

        for(OnSaveListener listener: onSaveListeners){
            listener.onSave(entity);
        }
    }

    public void setData(SpatialEntity data) {

        typeValue.setText(data != null ? data.getObjectType().toString() : "");
        nameTextField.setText(data != null ? data.getName() : "");
        adminTextField.setText(data != null ? data.getAdmin() : "");
        ownerTextField.setText(data != null ? data.getOwner() : "");
        noteTextPane.setText(data != null ? data.getNote() : "");
        categoryTextField.setText(data != null ? data.getCategory() : "");
        validFromValue.setDate(data != null ? data.getValidFrom() : null); // setText(data != null && data.getValidFrom() != null ? data.getValidFrom().toString() : "");
        validToValue.setDate(data != null ? data.getValidTo() : null); // setText(data != null && data.getValidTo() != null ? data.getValidTo().toString() : "");
        modifiedVaule.setText(data != null && data.getModified() != null ? data.getModified().toString() : "");
    }

    public void getData(SpatialEntity data) {
        if(data == null) return;

        data.setName(nameTextField.getText());
        data.setAdmin(adminTextField.getText());
        data.setOwner(ownerTextField.getText());
        data.setNote(noteTextPane.getText());
        data.setCategory(categoryTextField.getText());
        data.setValidFrom(validFromValue.getDate());
        data.setValidTo(validToValue.getDate());
    }

    public boolean isModified(SpatialEntity data) {
        if(data == null) return false;

        if (nameTextField.getText() != null ? !nameTextField.getText().equals(data.getName()) : data.getName() != null)
            return true;
        if (adminTextField.getText() != null ? !adminTextField.getText().equals(data.getAdmin()) : data.getAdmin() != null)
            return true;
        if (ownerTextField.getText() != null ? !ownerTextField.getText().equals(data.getOwner()) : data.getOwner() != null)
            return true;
        if (noteTextPane.getText() != null ? !noteTextPane.getText().equals(data.getNote()) : data.getNote() != null)
            return true;
        if (categoryTextField.getText() != null ? !categoryTextField.getText().equals(data.getCategory()) : data.getCategory() != null)
            return true;
        if (validFromValue.getDate() != null ? !validFromValue.getDate().equals(data.getValidFrom()) : data.getValidFrom() != null)
            return true;
        if (validToValue.getDate() != null ? !validToValue.getDate().equals(data.getValidFrom()) : data.getValidTo() != null)
            return true;
        return false;
    }

}

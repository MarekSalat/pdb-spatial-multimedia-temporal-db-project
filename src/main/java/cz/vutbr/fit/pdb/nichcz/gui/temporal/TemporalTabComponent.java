package cz.vutbr.fit.pdb.nichcz.gui.temporal;

import cz.vutbr.fit.pdb.nichcz.context.Context;
import cz.vutbr.fit.pdb.nichcz.gui.BaseFrame;
import cz.vutbr.fit.pdb.nichcz.gui.Main;
import cz.vutbr.fit.pdb.nichcz.gui.spatial.SpatialTableModel;
import cz.vutbr.fit.pdb.nichcz.model.spatial.SpatialDBMapper;
import cz.vutbr.fit.pdb.nichcz.model.temporal.CompanyDBMapper;
import cz.vutbr.fit.pdb.nichcz.model.temporal.CompanyEntity;
import cz.vutbr.fit.pdb.nichcz.model.temporal.LoggingHistoryDBMapper;
import cz.vutbr.fit.pdb.nichcz.model.temporal.LoggingHistoryEntity;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Nich
 * Date: 15.12.13
 * Time: 2:44
 * To change this template use File | Settings | File Templates.
 */
public class TemporalTabComponent extends BaseFrame {
    private JPanel panel1;
    private JTable companyTable;
    private JTextField companyName;
    private JButton deleteCompany;
    private JButton addCompany;
    private JButton updateCompany;
    private JComboBox companyId;
    private JButton deleteLoggingHistory;
    private JTable loggingHistory;
    private JSpinner logsPerDay;
    private JButton addLoggingHistory;
    private JButton updateLoggingHistory;
    private JTable loggingAreas;
    private JTextField loggingArea;
    private JButton button1;
    private JButton button2;
    private JTable table4;
    private JTable table5;
    private JXDatePicker companyValidFrom;
    private JXDatePicker companyValidTo;
    private JXDatePicker loggingHistoryValidFrom;
    private JXDatePicker loggingHistoryValidTo;

    private CompanyDBMapper companyDBMapper;
    private SpatialDBMapper spatialDBMapper;
    private LoggingHistoryDBMapper loggingHistoryDBMapper;

    private CompanyEntity selectedCompany = null;
    private LoggingHistoryEntity selectedLoggingHistory = null;

    public TemporalTabComponent(Context ctx) {
        super(ctx);
        setContentPane(panel1);
        companyDBMapper = new CompanyDBMapper(ctx);
        spatialDBMapper = new SpatialDBMapper(ctx);
        loggingHistoryDBMapper = new LoggingHistoryDBMapper(ctx);

        companyValidFrom.setFormats(DateFormat.getDateInstance());
        companyValidTo.setFormats(DateFormat.getDateInstance());
        loggingHistoryValidFrom.setFormats(DateFormat.getDateInstance());
        loggingHistoryValidTo.setFormats(DateFormat.getDateInstance());

        addCompany.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CompanyEntity company = getCompany();
                if (company == null) { return; }
                companyDBMapper.create(company);
                updateTables();
            }
        });
        updateCompany.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CompanyEntity company = getCompany();
                if (company == null || selectedCompany == null) { return; }
                company.setId(selectedCompany.getId());
                companyDBMapper.save(company);
                updateTables();
            }
        });
        deleteCompany.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CompanyEntity company = getCompany();
                if (company == null || selectedCompany == null) { return; }
                company.setId(selectedCompany.getId());
                companyDBMapper.delete(company, company.getValidFrom(), company.getValidTo());
                updateTables();
            }
        });

        companyTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                Integer row = companyTable.getSelectedRow();

                if (row < 0) { setCompany(null); return; }

                CompanyEntity c = new CompanyEntity();
                c.setId((Long) companyTable.getValueAt(row, 0));
                c.setName((String) companyTable.getValueAt(row, 1));
                c.setValidFrom((Date) companyTable.getValueAt(row, 2));
                c.setValidTo((Date) companyTable.getValueAt(row, 3));

                setCompany(c);
            }
        });

        addLoggingHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                LoggingHistoryEntity e = getLoggingHistory();
                if (e == null) { return; }
                loggingHistoryDBMapper.create(e);
                updateTables();
            }
        });
        updateLoggingHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                LoggingHistoryEntity e = getLoggingHistory();
                if (e == null || selectedLoggingHistory == null) { return; }
                e.setId(selectedLoggingHistory.getId());
                loggingHistoryDBMapper.save(e);
                updateTables();
            }
        });
        deleteLoggingHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                LoggingHistoryEntity e = getLoggingHistory();
                if (e == null || selectedLoggingHistory == null) { return; }
                e.setId(selectedLoggingHistory.getId());
                loggingHistoryDBMapper.delete(e, e.getValidFrom(), e.getValidTo());
                updateTables();
            }
        });

        loggingHistory.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                Integer row = loggingHistory.getSelectedRow();

                if (row < 0) { setLoggingHistory(null); return; }

                LoggingHistoryEntity e = new LoggingHistoryEntity();
                e.setId((Long) loggingHistory.getValueAt(row, 0));
                e.setCompanyName((String) loggingHistory.getValueAt(row, 1));
                e.setLoggingArea((String) loggingHistory.getValueAt(row, 2));
                e.setLogsPerDay((Integer) loggingHistory.getValueAt(row, 3));
                e.setValidFrom((Date) loggingHistory.getValueAt(row, 4));
                e.setValidTo((Date) loggingHistory.getValueAt(row, 5));
                e.setCompanyId((Long) loggingHistory.getModel().getValueAt(row, 6));

                setLoggingHistory(e);
            }
        });
    }

    private CompanyEntity getCompany() {
        if (!validateCompany()) {
            return null;
        }
        CompanyEntity company = new CompanyEntity();
        company.setName(companyName.getText());
        company.setValidFrom(companyValidFrom.getDate());
        company.setValidTo(companyValidTo.getDate());

        return company;
    }

    private void setCompany(CompanyEntity c) {
        selectedCompany = c;
        companyName.setText(c == null || c.getName() == null || c.getName().isEmpty() ? "" : c.getName() );
        companyValidFrom.setDate(c == null || c.getValidFrom() == null ? null : c.getValidFrom() );
        companyValidTo.setDate(c == null || c.getValidTo() == null ? null : c.getValidTo() );
    }

    private Boolean validateCompany() {
        if (companyName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(panel1, "Name is empty.", "Name error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (companyValidFrom.getDate() == null || companyValidTo.getDate() == null) {
            JOptionPane.showMessageDialog(panel1, "One of dates is empty.", "Date error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (companyValidFrom.getDate().getTime() >= companyValidTo.getDate().getTime()) {
            JOptionPane.showMessageDialog(panel1, "Valid from must be lesser than valid to.", "Date error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private LoggingHistoryEntity getLoggingHistory() {
        if (!validateLoggingHistory()) {
            return null;
        }
        LoggingHistoryEntity e = new LoggingHistoryEntity();

        e.setCompanyId( ((CompanyEntity) companyId.getSelectedItem()).getId() );
        e.setCompanyName(((CompanyEntity) companyId.getSelectedItem()).getName());
        e.setLoggingArea(loggingArea.getText());
        e.setLogsPerDay( (Integer) logsPerDay.getValue());
        e.setValidFrom(loggingHistoryValidFrom.getDate());
        e.setValidTo(loggingHistoryValidTo.getDate());

        return e;
    }

    private void setLoggingHistory(LoggingHistoryEntity e) {
        selectedLoggingHistory = e;

        if (e == null) { return; }

        CompanyEntity c = new CompanyEntity();
        c.setId(e.getCompanyId());
        c.setName(e.getCompanyName());
        c.setValidFrom(e.getValidFrom());
        c.setValidTo(e.getValidTo());

        companyId.getModel().setSelectedItem(c);
        loggingArea.setText(e.getLoggingArea());
        logsPerDay.setValue(e.getLogsPerDay());
        loggingHistoryValidFrom.setDate(e == null || e.getValidFrom() == null ? null : e.getValidFrom() );
        loggingHistoryValidTo.setDate(e == null || e.getValidTo() == null ? null : e.getValidTo() );
    }

    private Boolean validateLoggingHistory() {
        if (companyId.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(panel1, "Company is empty.", "Company error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (loggingArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(panel1, "Logging area is empty.", "Name error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (loggingHistoryValidFrom.getDate() == null || loggingHistoryValidTo.getDate() == null) {
            JOptionPane.showMessageDialog(panel1, "One of dates is empty.", "Date error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (loggingHistoryValidFrom.getDate().getTime() >= loggingHistoryValidTo.getDate().getTime()) {
            JOptionPane.showMessageDialog(panel1, "Valid from must be lesser than valid to.", "Date error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void updateTables() {
        updateCompanyTable();
        updateLoggingTable();
        updateLoggingHistoryTable();
        updateCompanyCombo();
    }

    private void updateCompanyTable() {
        CompanyTableModel companyTableModel = new CompanyTableModel(companyDBMapper.findWhere(" 1=1 order by id asc, valid_from asc"));
        companyTable.setModel(companyTableModel);
    }

    private void updateLoggingTable() {
        SpatialTableModel spatialTableModel = new SpatialTableModel(spatialDBMapper.findWhere(" OBJECT_TYPE = 'LOGGING_AREA'"));
        loggingAreas.setModel(spatialTableModel);
    }

    private void updateLoggingHistoryTable() {
        LoggingHistoryTableModel loggingHistoryTableModel = new LoggingHistoryTableModel(loggingHistoryDBMapper.
                findWhere(" 1=1 order by id asc, company_id asc, valid_from asc"));
        loggingHistory.setModel(loggingHistoryTableModel);
        loggingHistory.removeColumn(loggingHistory.getColumnModel().getColumn(6));
    }

    private void updateCompanyCombo() {
        companyId.setModel(new CompanyComboModel( companyDBMapper.findWhere("1=1 order by name asc, valid_from asc")) );
    }




}

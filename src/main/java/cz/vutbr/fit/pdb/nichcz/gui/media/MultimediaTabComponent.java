package cz.vutbr.fit.pdb.nichcz.gui.media;

import cz.vutbr.fit.pdb.nichcz.context.Context;
import cz.vutbr.fit.pdb.nichcz.gui.BaseFrame;
import cz.vutbr.fit.pdb.nichcz.model.media.MediaDBMapper;
import cz.vutbr.fit.pdb.nichcz.model.media.MediaEntity;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 4.12.13
 * Time: 10:22
 * To change this template use File | Settings | File Templates.
 */
public class MultimediaTabComponent extends BaseFrame {

    private JPanel panel1;
    private JButton insertButton;
    private JButton deleteButton;
    private JTable table1;
    private JLabel imageLabel;
    private JTextField textField1;
    private JButton rotateButton;
    private JTextField textField2;
    private JButton scaleButton;
    private JButton flipButton;
    private JButton mirrorButton;
    private JButton saveButton;
    private JButton similarButton;
    private JButton showAllButton;
    private JTextField textField3;
    private JButton contrastButton;

    private MediaDBMapper mapper;

    private NumberFormat nf;

    private static final int ID_COL = 0;
    private static final int NO_ROW = -1;


    public MultimediaTabComponent(Context ctx) {
        super(ctx);

        setContentPane(panel1);

        mapper = new MediaDBMapper(ctx);

        //Create a file chooser
        final JFileChooser fc = new JFileChooser();
//        fc.addChoosableFileFilter(new ImageFilter());
        fc.setFileFilter(new ImageFilter());



        nf = NumberFormat.getNumberInstance();

//        add(panel1);

        updateTable();

        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);



        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.out.println("Clicked Insert");

                int returnVal = fc.showOpenDialog(MultimediaTabComponent.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    System.out.println("Opening: " + file.getPath() + ".");

                    MediaEntity e = mapper.create();
//                    e.setFilePath(file.getPath());
                    e.setName(file.getName().substring(0, file.getName().lastIndexOf('.')));

                    mapper.loadImageFromFile(e, file.getPath());

                    mapper.save(e);

                    System.out.println("Saved");

                    updateTable();
                } else {
                    System.out.println("Open command cancelled by user.");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                int rowIndex = table1.getSelectedRow();
                if (rowIndex != NO_ROW) {
//                    System.out.println(rowIndex);
//                    System.out.println(table1.getValueAt(rowIndex, 0));
                    MediaEntity e = mapper.findById((Long) table1.getValueAt(rowIndex, ID_COL));
                    mapper.delete(e);

                    updateTable();

                    clearImageLabel();
                }
            }
        });

        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting())
                {
                    int rowIndex = table1.getSelectedRow();
                    if (rowIndex != NO_ROW) {
//                        System.out.println(rowIndex);
//                        System.out.println(table1.getValueAt(rowIndex, 0));
                        MediaEntity e = mapper.findById((Long) table1.getValueAt(rowIndex, ID_COL));

                        BufferedImage myPicture;

                        try {
                            myPicture = ImageIO.read(e.getImgProxy().getDataInStream());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            throw new RuntimeException(ex);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            throw new RuntimeException(ex);
                        }

                        imageLabel.setIcon(new ImageIcon(myPicture));

                        imageLabel.updateUI();
                    }
                }
            }
        });

        rotateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                int rowIndex = table1.getSelectedRow();
                if (rowIndex != NO_ROW) {
                    String res = getInput(textField1, false);

                    if (res == null)
                        return;

                    executeProcess("rotate=\"" + res + "\"", rowIndex);
                }
            }
        });

        scaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                int rowIndex = table1.getSelectedRow();
                if (rowIndex != NO_ROW) {
                    String res = getInput(textField2, true);

                    if (res == null)
                        return;

                    System.out.println(res);

                    executeProcess("scale=\"" + res + "\"", rowIndex);
                }
            }
        });

        flipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowIndex = table1.getSelectedRow();
                if (rowIndex != NO_ROW) {
                    executeProcess("flip", rowIndex);
                }
            }
        });

        mirrorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowIndex = table1.getSelectedRow();
                if (rowIndex != NO_ROW) {
                    executeProcess("mirror", rowIndex);
                }
            }
        });


        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                MediaTableModel mediaTableModel = (MediaTableModel) table1.getModel();
                List<MediaEntity> entities = mediaTableModel.getEntities();

                for (Iterator<MediaEntity> i = entities.iterator(); i.hasNext();) {
                    MediaEntity e = i.next();
                    if (e.isNameChanged()) {
                        mapper.save(e);
                        e.setNameChanged(false);
                    }
                }
            }
        });

        similarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                int rowIndex = table1.getSelectedRow();
                if (rowIndex != NO_ROW) {
//                    MediaEntity e = mapper.findById((Long) table1.getValueAt(rowIndex, ID_COL));
                    List<MediaEntity> similar = mapper.findSimilar((Long) table1.getValueAt(rowIndex, ID_COL));
                    MediaTableModel mediaTableModel = new MediaTableModel(similar);

                    table1.setModel(mediaTableModel);
                    table1.getColumnModel().getColumn(ID_COL).setMinWidth(0);
                    table1.getColumnModel().getColumn(ID_COL).setMaxWidth(0);

                    clearImageLabel();
                }
            }
        });

        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable();
                clearImageLabel();
            }
        });

        contrastButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowIndex = table1.getSelectedRow();
                if (rowIndex != NO_ROW) {
                    String res = getInput(textField3, true);

                    if (res == null)
                        return;

                    System.out.println(res);

                    executeProcess("contrast=\"" + res + "\"", rowIndex);
                }
            }
        });
    }



    private String getInput(JTextField textField, boolean getAbs) {
        float factor = 0;
        boolean isNum = false;
        String res;

        try {
            factor = Float.valueOf(textField.getText());
            isNum = true;
        } catch (NumberFormatException e1) {
            System.out.println("Input is not a Float, trying to parse.");
//                        return;
        }

        if (getAbs)
            factor = Math.abs(factor);
//                    System.out.println("factor: " + factor);
//
//                    System.out.println("formatted factor: " + nf.format(factor));

        res = nf.format(factor);

        if (!isNum)
        {
            Number parse = null;
            try {
                parse = nf.parse(textField2.getText());
            } catch (ParseException e) {
                System.out.println("Input is not a number.");
                return null;
            }

            if (getAbs)
                parse = Math.abs(parse.floatValue());

//                        System.out.println("parse: " + parse);
//                        System.out.println("formatted parse: " + nf.format(parse));
//                        System.out.println();

            res = nf.format(parse);
        }

        return res;
    }

    private void updateTable()
    {
        MediaTableModel mediaTableModel = new MediaTableModel(mapper.findAll());

        table1.setModel(mediaTableModel);
//        table1.removeColumn(table1.getColumnModel().getColumn(0));
        table1.getColumnModel().getColumn(ID_COL).setMinWidth(0);
        table1.getColumnModel().getColumn(ID_COL).setMaxWidth(0);
    }

    private void updateImageLabel(int rowIndex)
    {
        MediaEntity e = mapper.findById((Long) table1.getValueAt(rowIndex, ID_COL));

        BufferedImage myPicture;

        try {
            myPicture = ImageIO.read(e.getImgProxy().getDataInStream());
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        imageLabel.setIcon(new ImageIcon(myPicture));

        imageLabel.updateUI();
    }

    private void clearImageLabel() {
        imageLabel.setIcon(null);

        imageLabel.updateUI();
    }

    private void executeProcess(String command, int rowIndex)
    {
        MediaEntity e = mapper.findByIdForUpdate((Long) table1.getValueAt(rowIndex, ID_COL));

        try {
            e.getImgProxy().process(command);
        }
        catch (SQLException ex) { ex.printStackTrace(); throw new RuntimeException(ex); }

        mapper.save(e);

        updateImageLabel(rowIndex);
    }
}

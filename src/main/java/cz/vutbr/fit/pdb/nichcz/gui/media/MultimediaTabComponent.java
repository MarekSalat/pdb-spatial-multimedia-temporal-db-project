package cz.vutbr.fit.pdb.nichcz.gui.media;

import cz.vutbr.fit.pdb.nichcz.context.Context;
import cz.vutbr.fit.pdb.nichcz.gui.BaseFrame;
import cz.vutbr.fit.pdb.nichcz.model.media.MediaDBMapper;
import cz.vutbr.fit.pdb.nichcz.model.media.MediaEntity;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

/**
 * User: Michal Pracuch
 * Date: 4.12.13
 * Time: 10:22
 *
 * Trida reprezentujici formular MultimediaTabComponent pro zalozku hlavni aplikace.
 */
public class MultimediaTabComponent extends BaseFrame {

    private JPanel rootPanel;
    private JButton insertButton;
    private JButton deleteButton;
    private JTable mediaTable;
    private JLabel imageLabel;
    private JTextField rotateTextField;
    private JButton rotateButton;
    private JTextField scaleTextField;
    private JButton scaleButton;
    private JButton flipButton;
    private JButton mirrorButton;
    private JButton saveButton;
    private JButton similarButton;
    private JButton showAllButton;
    private JTextField contrastTextField;
    private JButton contrastButton;

    private MediaDBMapper mapper;

    private NumberFormat nf;

    private static final int NO_ROW = -1;


    /**
     * Vytvori novy formular MultimediaTabComponent pro zalozku hlavni aplikace.
     * @param ctx Kontext s pripojenim na databazi
     */
    public MultimediaTabComponent(Context ctx) {
        super(ctx);

        setContentPane(rootPanel);

        mapper = new MediaDBMapper(ctx);

        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new ImageFilter());

        nf = NumberFormat.getNumberInstance();

//        updateTable();

        mediaTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.out.println("Clicked Insert");

                int returnVal = fc.showOpenDialog(MultimediaTabComponent.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    System.out.println("Opening: " + file.getPath() + ".");

                    MediaEntity e = mapper.create();
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
                int rowIndex = mediaTable.getSelectedRow();
                if (rowIndex != NO_ROW) {
//                    System.out.println(rowIndex);

                    MediaEntity e = mapper.findById((Long) mediaTable.getValueAt(rowIndex, MediaTableModel.COL_ID));
                    mapper.delete(e);

                    updateTable();
                }
            }
        });

        mediaTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int rowIndex = mediaTable.getSelectedRow();
                    if (rowIndex != NO_ROW) {
//                        System.out.println(rowIndex);
//                        System.out.println(mediaTable.getValueAt(rowIndex, 0));

                        updateImageLabel(rowIndex);
                    }
                }
            }
        });

        rotateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                int rowIndex = mediaTable.getSelectedRow();
                if (rowIndex != NO_ROW) {
                    String res = getInput(rotateTextField, false);

                    if (res == null)
                        return;

                    executeProcess("rotate=\"" + res + "\"", rowIndex);
                }
            }
        });

        scaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                int rowIndex = mediaTable.getSelectedRow();
                if (rowIndex != NO_ROW) {
                    String res = getInput(scaleTextField, true);

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
                int rowIndex = mediaTable.getSelectedRow();
                if (rowIndex != NO_ROW) {
                    executeProcess("flip", rowIndex);
                }
            }
        });

        mirrorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowIndex = mediaTable.getSelectedRow();
                if (rowIndex != NO_ROW) {
                    executeProcess("mirror", rowIndex);
                }
            }
        });


        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                MediaTableModel mediaTableModel = (MediaTableModel) mediaTable.getModel();
                List<MediaEntity> entities = mediaTableModel.getEntities();

                for (Iterator<MediaEntity> i = entities.iterator(); i.hasNext(); ) {
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
                int rowIndex = mediaTable.getSelectedRow();
                if (rowIndex != NO_ROW) {
                    List<MediaEntity> similar = mapper.findSimilar((Long) mediaTable.getValueAt(rowIndex, MediaTableModel.COL_ID));
                    MediaTableModel mediaTableModel = new MediaTableModel(similar);

                    updateTable(mediaTableModel);
                }
            }
        });

        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable();
            }
        });

        contrastButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowIndex = mediaTable.getSelectedRow();
                if (rowIndex != NO_ROW) {
                    String res = getInput(contrastTextField, true);

                    if (res == null)
                        return;

                    System.out.println(res);

                    executeProcess("contrast=\"" + res + "\"", rowIndex);
                }
            }
        });

        rootPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);    //To change body of overridden methods use File | Settings | File Templates.
                updateTable();
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
        } catch (NumberFormatException e) {
            System.out.println("Input is not a Float, trying to parse.");
//                        return;
        }

        if (getAbs)
            factor = Math.abs(factor);
//                    System.out.println("factor: " + factor);
//
//                    System.out.println("formatted factor: " + nf.format(factor));

        res = nf.format(factor);

        if (!isNum) {
            Number parse = null;
            try {
                parse = nf.parse(scaleTextField.getText());
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


    private void updateTable() {
        MediaTableModel mediaTableModel = new MediaTableModel(mapper.findAll());

        updateTable(mediaTableModel);
    }

    private void updateTable(MediaTableModel mediaTableModel) {
        mediaTable.setModel(mediaTableModel);

        mediaTable.getColumnModel().getColumn(MediaTableModel.COL_ID).setMinWidth(0);
        mediaTable.getColumnModel().getColumn(MediaTableModel.COL_ID).setMaxWidth(0);

        mediaTable.getColumnModel().getColumn(MediaTableModel.COL_INDEX).setMinWidth(10);
        mediaTable.getColumnModel().getColumn(MediaTableModel.COL_INDEX).setPreferredWidth(20);
        mediaTable.getColumnModel().getColumn(MediaTableModel.COL_INDEX).setMaxWidth(50);

        mediaTable.getColumnModel().getColumn(MediaTableModel.COL_WIDTH).setMinWidth(30);
        mediaTable.getColumnModel().getColumn(MediaTableModel.COL_WIDTH).setPreferredWidth(40);
        mediaTable.getColumnModel().getColumn(MediaTableModel.COL_WIDTH).setMaxWidth(50);

        mediaTable.getColumnModel().getColumn(MediaTableModel.COL_HEIGHT).setMinWidth(30);
        mediaTable.getColumnModel().getColumn(MediaTableModel.COL_HEIGHT).setPreferredWidth(40);
        mediaTable.getColumnModel().getColumn(MediaTableModel.COL_HEIGHT).setMaxWidth(50);

        clearImageLabel();
    }

    private void updateImageLabel(int rowIndex) {
        MediaEntity e = mapper.findById((Long) mediaTable.getValueAt(rowIndex, MediaTableModel.COL_ID));

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

    private void executeProcess(String command, int rowIndex) {
        MediaEntity e = mapper.findByIdForUpdate((Long) mediaTable.getValueAt(rowIndex, MediaTableModel.COL_ID));

        try {
            e.getImgProxy().process(command);
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        mapper.save(e);

        updateImageLabel(rowIndex);
    }
}

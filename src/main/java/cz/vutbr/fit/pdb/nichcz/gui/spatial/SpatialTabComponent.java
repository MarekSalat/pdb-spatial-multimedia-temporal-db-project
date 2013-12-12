package cz.vutbr.fit.pdb.nichcz.gui.spatial;

import cz.vutbr.fit.pdb.nichcz.context.Context;
import cz.vutbr.fit.pdb.nichcz.gui.BaseFrame;
import cz.vutbr.fit.pdb.nichcz.gui.spatial.graphics.SpatialEntityForm;
import cz.vutbr.fit.pdb.nichcz.model.spatial.Point2DShape;
import cz.vutbr.fit.pdb.nichcz.model.spatial.SpatialDBMapper;
import cz.vutbr.fit.pdb.nichcz.model.spatial.SpatialEntity;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * User: Marek Salát
 * Date: 8.12.13
 * Time: 13:29
 */
public class SpatialTabComponent extends BaseFrame {
    private JPanel root;
    private SpatialCanvas canvas;
    private SpatialEntityForm entityForm;
    private JButton newButton;
    private JComboBox typeComboBox;
    private JButton saveAllButton;
    private JLabel status;
    private JTextField statusValue;
    private JTextArea entityInfo;
    private JPanel selectors;
    private JTextArea generalInfo;

    private Point2D lastPosition = new Point2D.Double();
    private SpatialDBMapper mapper;

    private ArrayList<JCheckBox> checkBoxes;

    public SpatialTabComponent(Context ctx) {
        super(ctx);
        setContentPane(root);
        saveAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAll();
            }
        });
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entityNew();
            }
        });

        selectorsChanged(null);

        canvas.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                lastPosition.setLocation(e.getPoint());
            }
        });
    }

    private void saveAll() {
        String status = "";
        for(SpatialEntity entity : canvas.entities){
            try {
            mapper.save(entity);
            } catch (RuntimeException e){
                status += entity + " can not be saved: " + e.toString();
            }
        }

        statusValue.setText(status.isEmpty() ? "All entities has been saved successfully." : status);
    }

    private void entityNew() {
        SpatialEntity.TYPE type = SpatialEntity.TYPE.valueOf(typeComboBox.getSelectedItem().toString());

        Shape shape;
        int x = (int) lastPosition.getX();
        int y = (int) lastPosition.getY();
        switch (type) {
            // polygons
            case FOREST:
            case WATER:
            case LOGGING_AREA:
            case FIELD:
                Polygon p = new Polygon();
                p.addPoint(x, y);
                p.addPoint(x+50, y);
                p.addPoint(x+50, y+50);
                shape = p;
                break;
            // lines
            case TRACK:
            case STREAM:
                Path2D p2 = new Path2D.Double();
                p2.moveTo(x, y);
                p2.lineTo(x+50, y+50);
                p2.lineTo(x+50, y-50);
                shape = p2;
                break;
            // circles
            case HUNTING_AREA:
                shape = new Ellipse2D.Double(x, y, 50, 50);
                break;
            // points
            case VIEW:
            case FEEDING_RACK:
            case UNKNOWN:
            default:
                shape = new Point2DShape(x, y);
                break;
        }

        SpatialEntity entity = mapper.create();
        entity.setObjectType(type);
        entity.setGeometry(shape);
        mapper.save(entity);

        canvas.addEntityAsDraggable(entity);
        canvas.repaint();
        entitySelected(entity);

        statusValue.setText("Entity with has been created.");
    }

    public SpatialDBMapper getMapper(){
        if(mapper != null) return mapper;
        mapper = new SpatialDBMapper(getContext());
        return mapper;
    }

    private void createUIComponents() {
        canvas = new SpatialCanvas(getContext(), getMapper());
        canvas.addListener(new SpatialCanvas.SelectedListener() {
            @Override
            public void onSelected(SpatialEntity entity) {
                entitySelected(entity);
            }

            @Override
            public void onUnselected(SpatialEntity entity) {
                entityUnselected(entity);
            }
        });

        entityForm = new SpatialEntityForm(getContext(), getMapper());
        entityForm.addListener(new SpatialEntityForm.OnDeleteListener() {
            @Override
            public void onDelete(SpatialEntity entity) {
                entityOnDeleted(entity);
                statusValue.setText("Entity with has been deleted.");
            }
        });

        checkBoxes = new ArrayList<>();
        selectors = new JPanel();
        selectors.setMaximumSize(new Dimension(-1, 442));

        for(SpatialEntity.TYPE type : SpatialEntity.TYPE.values()){
            if(type == SpatialEntity.TYPE.UNKNOWN) continue;

            JCheckBox jCheckBox = new JCheckBox(type.toString());
            jCheckBox.setName(type.toString());
            jCheckBox.setSelected(true);
            selectors.add(jCheckBox);
            checkBoxes.add(jCheckBox);

            jCheckBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectorsChanged(e);
                }
            });
        }


        selectors.revalidate();
        selectors.repaint();
    }

    private void selectorsChanged(ActionEvent e) {
        String where = " OBJECT_TYPE IN (";
        int j=0;
        for (int i = 0; i < checkBoxes.size(); i++) {
            JCheckBox checkBox = checkBoxes.get(i);
            if (!checkBox.isSelected()) continue;
            where += (j==0 ? "" : ", ")  + "'"+checkBox.getName()+"'";
            j++;
        }
        if(j == 0) where += "'null'";
        where += ")";

        canvas.removeAllEntities();
        for(SpatialEntity entity : mapper.findWhere(where)){
            canvas.addEntityAsDraggable(entity);
        }
        canvas.repaint();

        System.out.println(where);
    }

    private SpatialEntity curr;
    private SpatialEntity prev;
    private void entityOnDeleted(SpatialEntity entity) {
        canvas.removeEntity(entity);
        canvas.repaint();
    }

    private void entitySelected(SpatialEntity entity) {
        prev = curr;
        curr = entity;

        entityForm.setEntity(entity);

        entityInfo.setText("");
        entityInfo.append(String.format(
            "Area : %g\n" +
            "Length : %g\n" +
            "Distance to [name] = %s is : %g\n",
            mapper.getGeometryArea(curr),
            mapper.getGeometryLenght(curr),
            prev == null ? "none" : prev.getName(),
            prev == null ? 0 : mapper.getGeometriesShortestDistance(curr, prev)
        ));

        generalInfo.setText("");
        SpatialEntity biggest = mapper.getBiggestAreaEntity(entity.getObjectType());
        generalInfo.append(String.format(
            "Type: %s\n"+
            "Total area of type: %g\n"+
            "Total length of type: %g\n"+
            "Biggest area of type is %s with %g\n",
            entity.getObjectType().toString(),
            mapper.getTotalAreaOf(entity.getObjectType()),
            mapper.getTotalLengthOf(entity.getObjectType()),
            biggest.getName(), mapper.getGeometryArea(biggest)
        ));
    }

    private void entityUnselected(SpatialEntity entity) {
        entityForm.setEntity(null);
    }
}

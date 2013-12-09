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

    private Point2D lastPosition = new Point2D.Double();


    private SpatialDBMapper mapper;

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

        for (SpatialEntity entity : mapper.findAll()) {
            canvas.addEntity(entity);
        }

        canvas.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                lastPosition.setLocation(e.getPoint());
            }
        });
    }

    private void saveAll() {
        for(SpatialEntity entity : canvas.entities){
            mapper.save(entity);
        }
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

        canvas.addEntity(entity);
        canvas.repaint();
        entitySelected(entity);
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
            }
        });
    }

    private void entityOnDeleted(SpatialEntity entity) {
        canvas.removeEntity(entity);
        canvas.repaint();
    }

    private void entitySelected(SpatialEntity entity) {
        entityForm.setEntity(entity);
    }

    private void entityUnselected(SpatialEntity entity) {
        entityForm.setEntity(null);
    }
}

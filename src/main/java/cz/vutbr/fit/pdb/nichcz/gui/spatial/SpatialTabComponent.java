package cz.vutbr.fit.pdb.nichcz.gui.spatial;

import cz.vutbr.fit.pdb.nichcz.context.Context;
import cz.vutbr.fit.pdb.nichcz.gui.BaseComponent;
import cz.vutbr.fit.pdb.nichcz.gui.spatial.graphics.*;
import cz.vutbr.fit.pdb.nichcz.model.spatial.SpatialDBMapper;
import cz.vutbr.fit.pdb.nichcz.model.spatial.SpatialEntity;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

/**
 * User: Marek Salát
 * Date: 27.11.13
 * Time: 16:12
 */
public class SpatialTabComponent extends BaseComponent{


    private Color color;

    private CompositeDrawable draggable = new CompositeDrawable();
    private SpatialDBMapper mapper;

    public SpatialTabComponent(Context ctx) {
        super(ctx);
        Listener listener = new Listener();
        addMouseMotionListener(listener);
        addMouseListener(listener);

        color = new Color(60, 90, 238);

        CompositeDrawable c1 = new CompositeDrawable();
        CompositeDrawable c2 = new CompositeDrawable();

        for(int i = 0; i <= 5; i++){
            c1.addArea(new DraggableArea(
                new Ellipse2D.Double(Math.random() * 600, Math.random() * 400, 8, 8)
            ));

            c2.addArea(new DraggableArea(
                    new Ellipse2D.Double(Math.random() * 600, Math.random() * 400, 8, 8)
            ));
        }

        draggable.addArea(c1);
        draggable.addArea(c2);

        mapper = new SpatialDBMapper(ctx);
    }

    public class Listener extends MouseAdapter{
        private Point start = new Point();
        private Selectable selected = null;
        private boolean isDragged = false;
        private boolean isDraggedEverything = false;

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);

            if(selected != null) selected.onUnselected();

            Drawable shape = draggable.getIntersectedDrawable(e.getX(), e.getY());
            if(shape == null || shape == draggable) return;

            if(shape instanceof Selectable){
                selected = (Selectable) shape;
                selected.onSelected();
            }

            if(shape instanceof Draggable) {
                isDragged = true;
                start.setLocation(e.getPoint());

                if(e.getButton() == 3) {
                    selected.onUnselected();
                    selected = draggable;
                    selected.onSelected();
                    isDraggedEverything = true;
                }
            }

            repaint();
        }

        Point delta = new Point();
        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            if(!isDragged || selected == null || !(selected instanceof Draggable))
                return;

            delta.setLocation(e.getX() - start.getX(), e.getY() - start.getY());

            ((Draggable)selected).onDragged(delta);
            draggable.reset();

            start.setLocation(e.getPoint());
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);

            isDragged = false;
            isDraggedEverything = false;
            repaint();
        }
    }

    @Override
    public void paint(Graphics graphics){
        super.paint(graphics);
        Graphics2D g2 = (Graphics2D) graphics;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


//        Path2D path2D = new Path2D.Double();
//        path2D.moveTo(50, 50);
//        path2D.lineTo(100, 50);
//        path2D.lineTo(125, 25);
//        path2D.moveTo(100, 50);
//        path2D.lineTo(125, 75);
//
//        g2.draw(path2D);

//        draggable.draw(g2);
//
//        g2.setColor(Color.BLACK);
//        g2.draw(draggable.getBounds2D());

        SpatialEntity e = mapper.findById(Long.valueOf(42));

        g2.draw(e.getGeometry());

    }
}
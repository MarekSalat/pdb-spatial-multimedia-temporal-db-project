package cz.vutbr.fit.pdb.nichcz.gui.spatial.graphics;

import cz.vutbr.fit.pdb.nichcz.model.spatial.Point2DShape;
import cz.vutbr.fit.pdb.nichcz.model.spatial.SpatialEntity;
import cz.vutbr.fit.pdb.nichcz.setting.Setting;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
* User: Marek Salát
* Date: 8.12.13
* Time: 9:25
 *
 * TODO: Split this class to Point manipulator, ellipse manipulator, circle manipulator, path/polygon manipulator
*/
public class Manipulator extends DraggableArea {
    final double HOOK_SIZE = 16;
    private DraggableContainer container;
    public SpatialEntity spatialEntity;

    public ArrayList<Hook> hookList = new ArrayList<>();

    public Manipulator(DraggableContainer container, SpatialEntity spatialEntity) {
        this.container = container;
        this.spatialEntity = spatialEntity;
    }

    @Override
    public void draw(Graphics2D g2) {
        for(Hook hook : hookList){
            hook.draw(g2);
        }
    }

    @Override
    public void onDragged(Point delta) {
        super.onDragged(delta);
        if(spatialEntity.getGeometry() instanceof Ellipse2D){
            Ellipse2D old = (Ellipse2D) spatialEntity.getGeometry();
            spatialEntity.setGeometry( new Ellipse2D.Double(old.getX()+delta.getX(), old.getY()+delta.getY(), old.getWidth(), old.getHeight()));
        }

        for(Hook handler : hookList) {
            handler.onDragged(delta);
        }
    }

    @Override
    public boolean isSelected() {
        for(Hook hook:hookList){
            if(hook.isSelected()) return true;
        }
        return super.isSelected();
    }

    public void createHooks(){
        if(spatialEntity.getGeometry() instanceof Point2DShape){
            return;
        }
        if(spatialEntity.getGeometry() instanceof Ellipse2D){
            Ellipse2D el = (Ellipse2D) spatialEntity.getGeometry();
            Ellipse2D.Double dot = new Ellipse2D.Double(
                    el.getCenterX()+el.getWidth()/2  - HOOK_SIZE /2,
                    el.getCenterY()                  - HOOK_SIZE /2,
                    HOOK_SIZE, HOOK_SIZE
            );
            addHook(new Hook(this, dot));
            return;
        }

        double [] segment = new double[6];
        double [] first = null;
        for(PathIterator pi = spatialEntity.getGeometry().getPathIterator(null); !pi.isDone() ; pi.next()){
            int type = pi.currentSegment(segment);
            if(type == PathIterator.SEG_CLOSE) break;

            if(first == null)     {
                first = new double[6];
                System.arraycopy( segment, 0, first, 0, segment.length );
            }
            else if (Arrays.equals(segment, first)) continue;

            Ellipse2D.Double dot = new Ellipse2D.Double(segment[0] - HOOK_SIZE /2, segment[1] - HOOK_SIZE /2, HOOK_SIZE, HOOK_SIZE);
            addHook(new Hook(this, dot));
        }
    }

    public void addHook(Hook h){
        container.addDraggable(h);
        hookList.add(h);
    }

    public void removeHooks(){
        if(isSelected()) return;
        for (Iterator<Hook> iterator = hookList.iterator(); iterator.hasNext(); ) {
            Hook hook = iterator.next();
            container.removeDraggable(hook);
            iterator.remove();
        }
    }

    public Shape createNewShape(){
        if(spatialEntity.getGeometry() instanceof Ellipse2D){
            Hook hook = hookList.get(0);
            Ellipse2D old = (Ellipse2D) spatialEntity.getGeometry();
            Point2D newCenter = new Point2D.Double();

            newCenter.setLocation(old.getCenterX(), old.getCenterY());
            double dx = hook.dot.getCenterX()-old.getCenterX();
            double dy = hook.dot.getCenterY()-old.getCenterY();
            double newWidth = Math.sqrt( dx*dx + dy*dy)*2;

            Ellipse2D el = new Ellipse2D.Double(
                newCenter.getX()-newWidth/2,
                newCenter.getY()-newWidth/2,
                newWidth, newWidth
            );
            return el;
        }

        Path2D path = new Path2D.Double();
        double [] segment = new double[6];
        double [] first = null;
        Iterator<Hook> iterator = hookList.iterator();

        Point2D src = new Point2D.Double();
        Point2D dst = new Point2D.Double();

        PathIterator pi = spatialEntity.getGeometry().getPathIterator(null);
        path.setWindingRule(pi.getWindingRule());

        Hook h = null;

        for(; !pi.isDone() ; pi.next()){
            int type = pi.currentSegment(segment);
            src.setLocation(segment[0], segment[1]);

            h = iterator.hasNext() ? iterator.next() : h;
            h.getAt().transform(src, dst);
            h.setAt(new AffineTransform());

            if(type == PathIterator.SEG_CLOSE){
                path.closePath();
                break;
            }
            if(first == null) {
                first = new double[6];
                System.arraycopy( segment, 0, first, 0, segment.length );
            }
            else if(Arrays.equals(segment, first)) continue;

            if(type == PathIterator.SEG_MOVETO)
                path.moveTo(dst.getX(), dst.getY());
            if(type == PathIterator.SEG_LINETO)
                path.lineTo(dst.getX(), dst.getY());
        }

        return path;
    }

    public void updateShape(){
        if(spatialEntity.getGeometry() instanceof Point2DShape){
            return;
        }
        spatialEntity.setGeometry(createNewShape());
    }


    public class Hook extends DraggableArea {

        protected final Manipulator manipulator;

        protected Ellipse2D dot;

        public Hook(Manipulator manipulator, Ellipse2D dot) {
            super(dot);
            this.dot = dot;
            this.manipulator = manipulator;
        }

        @Override
        public void draw(Graphics2D g2) {
            if(isSelected())
                g2.setColor(Setting.Colors.pomegranate.getColor());
            else
                g2.setColor(Setting.Colors.midnight_blue.getColor());
            g2.fill(this);
        }

        @Override
        public void onDragged(Point delta) {
            super.onDragged(delta);
            dot.setFrame(dot.getX()+delta.getX(), dot.getY()+delta.getY(), dot.getWidth(), dot.getHeight());
            manipulator.updateShape();
        }

        @Override
        public void onUnselected() {
            super.onUnselected();
        }
    }
}

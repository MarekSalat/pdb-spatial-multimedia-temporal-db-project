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
 * Trida popisujici objekt, se kterym se manipuluje na platne.
 *
 * TODO: Split this class to Point manipulator, ellipse manipulator, circle manipulator, path/polygon manipulator
 * TODO: Hnusny kod! zrefaktorovat
*/
public class Manipulator extends DraggableArea {
    final double HOOK_SIZE = 16;
    private DraggableContainer container;
    public SpatialEntity entity;

    public ArrayList<Hook> hookList = new ArrayList<>();

    public Manipulator(DraggableContainer container, SpatialEntity spatialEntity) {
        this.container = container;
        this.entity = spatialEntity;
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
        if(entity.getGeometry() instanceof Ellipse2D){
            Ellipse2D old = (Ellipse2D) entity.getGeometry();
            entity.setGeometry( new Ellipse2D.Double(old.getX()+delta.getX(), old.getY()+delta.getY(), old.getWidth(), old.getHeight()));
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

    public void addPointAfter(Hook hook, Point2D point){
        if(entity.getGeometry() instanceof Ellipse2D || entity.getGeometry() instanceof Point2DShape) return;

        PathIterator pi = entity.getGeometry().getPathIterator(null);
        Path2D path = new Path2D.Double();
        path.setWindingRule(pi.getWindingRule());

        Iterator<Hook> iterator = hookList.iterator();

        Hook h = null;
        int hookIdx = 0;
        double [] segment = new double[6];
        double[] first = null;
        boolean added = false;

        for(; !pi.isDone() ; pi.next()){
            int type = pi.currentSegment(segment);

            h = iterator.hasNext() ? iterator.next() : h;

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
                path.moveTo(segment[0], segment[1]);
            if(type == PathIterator.SEG_LINETO)
                path.lineTo(segment[0], segment[1]);

            if(!added){
                if(h == hook) {
                    path.lineTo(point.getX(), point.getY());
                    added = true;
                }
                else
                    hookIdx++;
            }
        }

        addHook(hookIdx+1, point.getX(), point.getY());
        entity.setGeometry(path);
    }

    public void removePointAt(Hook hook){
        if(entity.getGeometry() instanceof Ellipse2D || entity.getGeometry() instanceof Point2DShape) return;

        PathIterator pi = entity.getGeometry().getPathIterator(null);
        Path2D path = new Path2D.Double();
        path.setWindingRule(pi.getWindingRule());

        Iterator<Hook> iterator = hookList.iterator();

        Hook tmpHook = null;
        double [] segment = new double[6];
        double[] first = null;

        for(; !pi.isDone() ; pi.next()){
            int type = pi.currentSegment(segment);
            if(first == null) type = PathIterator.SEG_MOVETO;

            tmpHook = iterator.hasNext() ? iterator.next() : tmpHook;

            if(type == PathIterator.SEG_CLOSE){
                path.closePath();
                break;
            }

            if(tmpHook == hook) {
                continue;
            }

            if(first == null) {
                first = new double[6];
                System.arraycopy( segment, 0, first, 0, segment.length );
            }
            else if(Arrays.equals(segment, first)) continue;

            if(type == PathIterator.SEG_MOVETO)
                path.moveTo(segment[0], segment[1]);
            if(type == PathIterator.SEG_LINETO)
                path.lineTo(segment[0], segment[1]);

        }
        hookList.remove(hook);
        container.removeDraggable(hook);
        entity.setGeometry(path);
    }


    public void addSegmentAt(Point2D point){
        if(entity.getGeometry() instanceof Ellipse2D || entity.getGeometry() instanceof Point2DShape) return;

        Path2D path = new Path2D.Double(entity.getGeometry());
        path.moveTo(point.getX(), point.getY());
        addHook(point.getX(), point.getY());
        entity.setGeometry(path);
    }


    public void createHooks(){
        if(entity.getGeometry() instanceof Point2DShape){
            return;
        }
        if(entity.getGeometry() instanceof Ellipse2D){
            Ellipse2D el = (Ellipse2D) entity.getGeometry();

            addHook(el.getCenterX()+el.getWidth()/2, el.getCenterY());
            return;
        }

        double [] segment = new double[6];
        double [] first = null;
        for(PathIterator pi = entity.getGeometry().getPathIterator(null); !pi.isDone() ; pi.next()){
            int type = pi.currentSegment(segment);
            if(type == PathIterator.SEG_CLOSE) break;

            if(first == null)     {
                first = new double[6];
                System.arraycopy( segment, 0, first, 0, segment.length );
            }
            else if (Arrays.equals(segment, first)) continue;

            addHook(segment[0], segment[1]);
        }
    }

    public void addHook(double x, double y){
        addHook(-1, x, y);
    }
    public void addHook(int idx, double x, double y){
        Ellipse2D.Double dot = new Ellipse2D.Double( x - HOOK_SIZE/2, y - HOOK_SIZE/2, HOOK_SIZE, HOOK_SIZE );
        Hook h = new Hook(this, entity, dot);
        container.addDraggable(h);
        if(idx >= 0)
            hookList.add(idx, h);
        else
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
        if(entity.getGeometry() instanceof Ellipse2D){
            Hook hook = hookList.get(0);
            Ellipse2D old = (Ellipse2D) entity.getGeometry();
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

        PathIterator pi = entity.getGeometry().getPathIterator(null);
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
        if(entity.getGeometry() instanceof Point2DShape){
            return;
        }
        entity.setGeometry(createNewShape());
    }

    public class Hook extends DraggableArea {

        protected final Manipulator manipulator;

        private SpatialEntity entity;
        protected Ellipse2D dot;

        public Hook(Manipulator manipulator, SpatialEntity entity, Ellipse2D dot) {
            super(dot);
            this.entity = entity;
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

        public SpatialEntity getEntity() {
            return entity;
        }

        public void setEntity(SpatialEntity entity) {
            this.entity = entity;
        }

        public Manipulator getManipulator() {
            return manipulator;
        }
    }
}

package cz.vutbr.fit.pdb.nichcz.gui.spatial.graphics;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

/**
* User: Marek Salát
* Date: 1.12.13
* Time: 16:51
*/
public class CompositeDrawable extends Area implements Drawable, Selectable, Draggable {
    public abstract class DrawableArea extends Area implements Drawable{}

    ArrayList<Drawable> drawables = new ArrayList<>();
    ArrayList<Selectable> selectables = new ArrayList<>();
    ArrayList<Draggable> draggables = new ArrayList<>();

    CompositeDrawable parent = null;

    public CompositeDrawable() {
        // super();
        parent = null;
    }

    public CompositeDrawable(Area a) {
        this();
        this.addArea(a);
    }

    public void addArea(Area area){
        if(area instanceof Drawable) drawables.add((Drawable) area);
        else {
            final Area _area = area;
            drawables.add(new DrawableArea(){
                @Override
                public void draw(Graphics2D graphics) {
                    graphics.draw(_area);
                }
            });
        }
        if(area instanceof Selectable) selectables.add((Selectable) area);
        if(area instanceof Draggable) draggables.add((Draggable) area);
        if(area instanceof CompositeDrawable) ((CompositeDrawable) area).parent = this;

        reset();
    }

    @Override
    public void draw(Graphics2D graphics) {
        AffineTransform old_at = graphics.getTransform();
        for(Drawable child : drawables){
            child.draw(graphics);
        }
        graphics.setTransform(old_at);
    }

    @Override
    public void onDragged(Point delta) {
        for(Draggable d : draggables)
            d.onDragged(delta);
        invalidateArea();
    }

    @Override
    public void onSelected() {
        for(Selectable s: selectables)
            s.onSelected();
    }

    @Override
    public void onUnselected() {
        for(Selectable s: selectables)
            s.onUnselected();
    }

    public Area getArea(){
        return getCachedArea();
    }

    public Drawable getIntersectedDrawable(double x, double y) {
        if (!getBounds2D().contains(x, y)) {
            return null;
        }
        for(Drawable d : drawables){
            if(d instanceof CompositeDrawable) return ((CompositeDrawable) d).getIntersectedDrawable(x, y);
            if(d.contains(x, y)) return d;
        }
        return this;
    }

    private Area cachedArea;
    private Area getCachedArea() {
        if (cachedArea != null) {
            return cachedArea;
        }
        cachedArea = new Area();
        for(Drawable d : drawables){
            if(d instanceof CompositeDrawable)
                cachedArea.add( ((CompositeDrawable) d).getArea() );
            else
                cachedArea.add(new Area(d));
        }
        return cachedArea;
    }

    private void invalidateArea() {
        if(parent != null)
            parent.cachedArea = null;
        cachedArea = null;
    }

    @Override
    public Rectangle2D getBounds2D() {
        return getArea().getBounds2D();
    }

    @Override
    public Rectangle getBounds() {
        return getArea().getBounds();
    }

    @Override
    public boolean contains(double x, double y) {
        return getIntersectedDrawable(x, y) != null;
    }

    @Override
    public boolean contains(Point2D p) {
        return contains(p.getX(), p.getY());
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void add(Area rhs) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void subtract(Area rhs) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void intersect(Area rhs) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void exclusiveOr(Area rhs) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void reset(){
        invalidateArea();
    }

    public boolean isEmpty(){
        return drawables.isEmpty();
    }

    @Override
    public boolean isPolygonal(){
        return getArea().isPolygonal();
    }

    @Override
    public boolean isRectangular(){
        return getArea().isRectangular();
    }

    @Override
    public boolean isSingular(){
        return getArea().isSingular();
    }

    @Override
    public void transform(AffineTransform t) {
        for(Drawable d : drawables){
            ((Area) d).transform(t);
        }
        invalidateArea();
    }

    @Override
    public Area createTransformedArea(AffineTransform t) {
        Area a = new Area( getArea() );
        a.transform(t);
        return a;
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return getArea().getPathIterator(at);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return getArea().getPathIterator(at, flatness);
    }
}

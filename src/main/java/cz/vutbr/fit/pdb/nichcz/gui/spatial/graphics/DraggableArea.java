package cz.vutbr.fit.pdb.nichcz.gui.spatial.graphics;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

/**
* User: Marek Salát
* Date: 1.12.13
* Time: 16:53
*/
public abstract class DraggableArea extends Area implements Draggable, Drawable, Selectable {
    protected AffineTransform at = new AffineTransform();

    boolean isSelected = false;

    public DraggableArea() {
        super();
        this.transform(at);
    }

    public DraggableArea(Shape s) {
        super(s);
        this.transform(at);
    }

    public DraggableArea(Area a) {
        super(a);
        this.transform(at);
    }

    @Override
    public void onDragged(Point delta) {
        at.setToTranslation(delta.getX(), delta.getY());
        transform(at);
    }

    @Override
    public void onSelected() {
        isSelected = true;
    }

    @Override
    public void onUnselected() {
        isSelected = false;
    }

    public boolean isSelected(){
        return  isSelected;
    }

    public AffineTransform getAt() {
        return at;
    }

    public void setAt(AffineTransform at) {
        this.at = at;
    }
}

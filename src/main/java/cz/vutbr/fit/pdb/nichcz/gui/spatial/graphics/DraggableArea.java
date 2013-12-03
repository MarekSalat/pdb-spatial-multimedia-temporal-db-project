package cz.vutbr.fit.pdb.nichcz.gui.spatial.graphics;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

/**
* User: Marek Salát
* Date: 1.12.13
* Time: 16:53
*/
public class DraggableArea extends Area implements Draggable, Drawable, Selectable {
    protected AffineTransform at = new AffineTransform();

    boolean isSelected = false;
    Color color = new Color(60, 90, 238);

    public DraggableArea() {
        super();
        this.transform(at);
    }

    public DraggableArea(Shape s) {
        super(s);
        this.transform(at);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(!isSelected ? color : Color.RED);
        g2.fill(this);
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
}

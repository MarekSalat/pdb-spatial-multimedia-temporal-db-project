package cz.vutbr.fit.pdb.nichcz.gui.spatial;

import cz.vutbr.fit.pdb.nichcz.gui.spatial.graphics.DraggableArea;
import cz.vutbr.fit.pdb.nichcz.gui.spatial.graphics.DraggableContainer;
import cz.vutbr.fit.pdb.nichcz.gui.spatial.graphics.Manipulator;
import cz.vutbr.fit.pdb.nichcz.model.spatial.SpatialEntity;
import cz.vutbr.fit.pdb.nichcz.setting.Setting;

import java.awt.*;
import java.awt.geom.Area;

/**
* User: Marek Salát
* Date: 8.12.13
* Time: 9:28
*/
public class SpatialEntityCanvasItem extends DraggableArea {

    private SpatialEntity spatialEntity;
    private Shape oldGeometry;
    private Manipulator manipulator;
    private DraggableContainer container;

    public SpatialEntityCanvasItem(DraggableContainer container, SpatialEntity spatialEntity) {
        super(spatialEntity.getGeometry());
        this.spatialEntity = spatialEntity;
        this.oldGeometry = spatialEntity.getGeometry();
        this.container = container;

        manipulator = new Manipulator(container, spatialEntity);
        manipulator.createHooks();
    }

    @Override
    public void draw(Graphics2D g2) {
        if(oldGeometry != spatialEntity.getGeometry()){
            reset();
            add(new Area(spatialEntity.getGeometry()));
        }

        if(isSelected())
            g2.setColor(Setting.Colors.pumpkin.getColor());
        else
            g2.setColor(Setting.Colors.belize_hole.getColor());

        if(spatialEntity.getObjectType() == SpatialEntity.TYPE.STREAM || spatialEntity.getObjectType() == SpatialEntity.TYPE.TRACK) {
            Stroke oldStroke = g2.getStroke();
            g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.draw(spatialEntity.getGeometry());
            g2.setStroke(oldStroke);
        }
        else {
            g2.fill(spatialEntity.getGeometry());
        }

        //if(isSelected())
        manipulator.draw(g2);
    }

    @Override
    public void onDragged(Point delta) {
        super.onDragged(delta);
        if(manipulator == null) return;

        manipulator.onDragged(delta);
    }

    @Override
    public void onSelected() {
        super.onSelected();
//            if(manipulator != null)
//                manipulator.createHooks();
    }

    @Override
    public void onUnselected() {
        super.onUnselected();
//            if(manipulator != null && !manipulator.isSelected())
//                manipulator.removeHooks();
    }

    public SpatialEntity getEntity() {
        return spatialEntity;
    }

    public void setSpatialEntity(SpatialEntity spatialEntity) {
        this.spatialEntity = spatialEntity;
    }

    public Manipulator getManipulator() {
        return manipulator;
    }
}

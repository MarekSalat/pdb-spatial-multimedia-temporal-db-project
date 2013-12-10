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

    private SpatialEntity entity;
    private Shape oldGeometry;
    private Manipulator manipulator;
    private DraggableContainer container;

    public SpatialEntityCanvasItem(DraggableContainer container, SpatialEntity entity) {
        super(entity.getGeometry());
        this.entity = entity;
        this.oldGeometry = entity.getGeometry();
        this.container = container;

        manipulator = new Manipulator(container, entity);
    }

    @Override
    public void draw(Graphics2D g2) {
        if(oldGeometry != entity.getGeometry()){
            reset();
            add(new Area(entity.getGeometry()));
        }

        g2.setColor(getColor());

        if(entity.getObjectType() == SpatialEntity.TYPE.STREAM || entity.getObjectType() == SpatialEntity.TYPE.TRACK) {
            Stroke oldStroke = g2.getStroke();
            g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.draw(entity.getGeometry());
            g2.setStroke(oldStroke);
        }
        else {
            g2.fill(entity.getGeometry());
        }

        //if(isSelected())
        manipulator.draw(g2);
    }

    private Color getColor(){
        Setting.Colors color;
        int alpha = 96;
        switch (entity.getObjectType()){
            // polygons
            case FOREST:
                color = Setting.Colors.emerald;
                break;
            case STREAM:
                alpha = 255;
            case WATER:
                color = Setting.Colors.peter_river;
                break;
            case LOGGING_AREA:
                color = Setting.Colors.carrot;
                alpha = 156;
                break;
            case FIELD:
                color = Setting.Colors.sun_flower;
                break;
            // lines
            case TRACK:
                color = Setting.Colors.wet_asphalt;
                alpha = 255;
                break;
            // circles
            case HUNTING_AREA:
                color = Setting.Colors.alizarin;
                break;
            // points
            case VIEW:
                color = Setting.Colors.amethyst;
                alpha = 255;
                break;
            case FEEDING_RACK:
                color = Setting.Colors.midnight_blue;
                alpha = 255;
                break;
            case UNKNOWN:
            default:
                color = Setting.Colors.concrete;
        }

        if(isSelected()) return Setting.Colors.pomegranate.getColor(alpha+30 > 255 ? 255 : alpha+30);

        return color.getColor(alpha);
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
        return entity;
    }

    public void setEntity(SpatialEntity entity) {
        this.entity = entity;
    }

    public Manipulator getManipulator() {
        return manipulator;
    }
}

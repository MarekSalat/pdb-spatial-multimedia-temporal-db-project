package cz.vutbr.fit.pdb.nichcz.gui.spatial;

import cz.vutbr.fit.pdb.nichcz.context.Context;
import cz.vutbr.fit.pdb.nichcz.gui.spatial.graphics.Draggable;
import cz.vutbr.fit.pdb.nichcz.gui.spatial.graphics.DraggableContainer;
import cz.vutbr.fit.pdb.nichcz.gui.spatial.graphics.Drawable;
import cz.vutbr.fit.pdb.nichcz.gui.spatial.graphics.Selectable;
import cz.vutbr.fit.pdb.nichcz.model.spatial.SpatialDBMapper;
import cz.vutbr.fit.pdb.nichcz.model.spatial.SpatialEntity;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

public class SpatialCanvas extends JPanel {
    public interface SelectedListener {
        void onSelected(SpatialEntity entity);
        void onUnselected(SpatialEntity entity);
    }

    private ArrayList<SelectedListener> selectedListeners = new ArrayList<>();

    final DraggableContainer draggableContainer = new DraggableContainer();
    private SpatialDBMapper mapper;
    public ArrayList<SpatialEntityCanvasItem> canvasItems = new ArrayList<SpatialEntityCanvasItem>();
    public ArrayList<SpatialEntity>           entities = new ArrayList<SpatialEntity>();

    public SpatialCanvas(Context ctx, SpatialDBMapper mapper) {
        super();
        this.mapper = mapper;
        SpatialCanvas.Listener listener = new SpatialCanvas.Listener();
        addMouseMotionListener(listener);
        addMouseListener(listener);
    }

    public void addListener(SelectedListener onSelectedListener){
        selectedListeners.add(onSelectedListener);
    }
    public void removeListener(SelectedListener onSelectedListener){
        selectedListeners.remove(onSelectedListener);
    }

    protected void onCanvasItemSelected(SpatialEntityCanvasItem item){
        for(SelectedListener listener : selectedListeners){
            listener.onSelected(item.getEntity());
        }
    }
    protected  void onCanvasItemUnselected(SpatialEntityCanvasItem item){
        for(SelectedListener listener : selectedListeners){
            listener.onUnselected(item.getEntity());
        }
    }

    public void addEntity(SpatialEntity entity){
        entities.add(entity);
        SpatialEntityCanvasItem spatialEntityGUI = new SpatialEntityCanvasItem(draggableContainer, entity);
        draggableContainer.addDraggable(spatialEntityGUI);
        canvasItems.add(spatialEntityGUI);
    }

    public void removeEntity(SpatialEntity entity){
        entities.remove(entity);
        for (Iterator<SpatialEntityCanvasItem> iterator = getCanvasItems().iterator(); iterator.hasNext(); ) {
            SpatialEntityCanvasItem item = iterator.next();
            if (item.getEntity() == entity) iterator.remove();
        }
    }

    public ArrayList<SpatialEntityCanvasItem> getCanvasItems() {
        return canvasItems;
    }

    private Image image;
    public Image getImage(){
        if(image != null) return image;

        Image img = null;
        try {
             img = ImageIO.read(new File(ClassLoader.getSystemResource("mapa.png").toURI()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return img;
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        Graphics2D g2 = (Graphics2D) graphics;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(getImage(), 0, 0, null);

        for (SpatialEntityCanvasItem canvasItem : getCanvasItems()) {
            canvasItem.draw(g2);
        }
    }

    class Listener extends MouseAdapter {
        private Point start = new Point();
        private Selectable selected = null;
        private boolean isDragged = false;

        private void selelct(Selectable s){
            s.onSelected();
            if(s instanceof SpatialEntityCanvasItem)
                onCanvasItemSelected((SpatialEntityCanvasItem) s);
        }

        private void unselelct(Selectable s){
            s.onUnselected();
            if(s instanceof SpatialEntityCanvasItem)
                onCanvasItemUnselected((SpatialEntityCanvasItem) s);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);

            if (selected != null) unselelct(selected);

            Drawable shape = getIntersectedDrawable(e.getX(), e.getY());
            if (shape == null) return;

            if (shape instanceof Selectable) {
                selected = (Selectable) shape;
                selelct(selected);
            }

            if (shape instanceof Draggable) {
                isDragged = true;
                start.setLocation(e.getPoint());
            }

            repaint();
        }

        private Drawable getIntersectedDrawable(int x, int y) {
            for (Drawable d : draggableContainer.drawables) {
                if (d.contains(x, y))
                    return d;
            }
            return null;
        }

        Point delta = new Point();

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            if (!isDragged || selected == null || !(selected instanceof Draggable))
                return;

            delta.setLocation(e.getX() - start.getX(), e.getY() - start.getY());

            ((Draggable) selected).onDragged(delta);

            start.setLocation(e.getPoint());
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);

            isDragged = false;
            repaint();
        }
    }
}
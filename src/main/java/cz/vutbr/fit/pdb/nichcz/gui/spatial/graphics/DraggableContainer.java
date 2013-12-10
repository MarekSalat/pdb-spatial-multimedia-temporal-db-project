package cz.vutbr.fit.pdb.nichcz.gui.spatial.graphics;

import java.io.Serializable;
import java.util.ArrayList;

public class DraggableContainer implements Serializable {
    public ArrayList<Drawable> drawables = new ArrayList<>();
    public ArrayList<Selectable> selectables = new ArrayList<>();
    public ArrayList<Draggable> draggables = new ArrayList<>();

    public DraggableContainer() {
    }

    public void addDraggable(Draggable draggable) {
        draggables.add(draggable);
        addSelectable(draggable);
    }

    public void addSelectable(Selectable selectable) {
        selectables.add(selectable);
        addDrawable(selectable);
    }

    public void addDrawable(Drawable drawable) {
        drawables.add(drawable);
    }

    public void removeDraggable(Draggable draggable) {
        draggables.remove(draggable);
        removeSelectable(draggable);
    }

    void removeSelectable(Selectable selectable) {
        selectables.remove(selectable);
        removeDrawable(selectable);
    }

    void removeDrawable(Drawable drawable) {
        drawables.remove(drawable);
    }
}
package cz.vutbr.fit.pdb.nichcz.gui.spatial.graphics;

import java.awt.*;

/**
* User: Marek Salát
* Date: 1.12.13
* Time: 16:49
 *
 * Rozhrani pro uchopitelny objekt na platne.
*/
public interface Draggable extends Selectable {
    /**
     * Reakce na tazeni.
     * @param delta Bod posunuti.
     */
    public void onDragged(Point delta);
}

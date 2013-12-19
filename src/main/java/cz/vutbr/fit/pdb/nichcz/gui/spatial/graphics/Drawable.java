package cz.vutbr.fit.pdb.nichcz.gui.spatial.graphics;

import java.awt.*;

/**
* User: Marek Salát
* Date: 1.12.13
* Time: 16:47
 *
 * Rozhrani pro vykreslitelny objekt na platne.
*/
public interface Drawable extends Shape {
    /**
     * Vykresli objekt.
     * @param graphics 
     */
    public void draw(Graphics2D graphics);
}

package cz.vutbr.fit.pdb.nichcz.gui.spatial.graphics;

import java.awt.*;

/**
* User: Marek Salát
* Date: 1.12.13
* Time: 16:49
*/
public interface Resizable extends Selectable {
    public void resize(double scale, Point center);
    public void resize(double scale);
}

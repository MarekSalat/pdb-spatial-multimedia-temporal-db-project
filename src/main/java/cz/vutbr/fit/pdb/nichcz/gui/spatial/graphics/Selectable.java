package cz.vutbr.fit.pdb.nichcz.gui.spatial.graphics;

/**
* User: Marek Salát
* Date: 1.12.13
* Time: 16:48
*/
public interface Selectable extends Drawable {
    public void onSelected();
    public void onUnselected();
}
